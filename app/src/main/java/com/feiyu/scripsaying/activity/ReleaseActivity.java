package com.feiyu.scripsaying.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.constant.GlobalConstant;
import com.feiyu.scripsaying.util.HD;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by YueDong on 2016/12/27.
 * 发布
 */
public class ReleaseActivity extends BaseActivity {
    @BindView(R.id.btn_album)
    Button btnAlbum;
    @BindView(R.id.btn_camera)
    Button btnCamera;
    @BindView(R.id.btn_edit_paper)
    Button btnEditPaper;
    //调用系统相册-选择图片
    private static final int IMAGE = 1;
    @BindView(R.id.iv_photo)
    ImageView ivPhoto;

    private String imgPath;
    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        ButterKnife.bind(this);
        ctx = this;
    }

    @OnClick({R.id.btn_album, R.id.btn_camera, R.id.btn_edit_paper})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_album:
                //打开相册
                openAlbum();
                break;
            case R.id.btn_camera:
                //开启相机


                break;
            case R.id.btn_edit_paper:

                break;


        }
    }

    private void openAlbum() {
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取图片路径
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            imgPath = c.getString(columnIndex);

            Bitmap bm = BitmapFactory.decodeFile(imgPath);
            ivPhoto.setImageBitmap(bm);
            c.close();

            if (imgPath.isEmpty()){
                HD.TLOG("请先选择一张图片");
                return;
            }else{
                Intent intent = new Intent(ctx,PaperEditImgActivity.class);
                intent.putExtra(GlobalConstant.CHOOSE_IMG_KEY,imgPath);
                startActivity(intent);
            }
        }
    }
}

package com.feiyu.scripsaying.activity;

import android.app.Activity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        ButterKnife.bind(this);
    }

    public void initView() {

    }

    public void initData() {

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
                startActivity(new Intent(this, PaperEditImgActivity.class));
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
            String imagePath = c.getString(columnIndex);

            Bitmap bm = BitmapFactory.decodeFile(imagePath);
            ivPhoto.setImageBitmap(bm);
            c.close();
        }
    }
}

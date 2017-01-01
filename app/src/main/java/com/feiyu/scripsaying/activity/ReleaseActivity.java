package com.feiyu.scripsaying.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.constant.GlobalConstant;
import com.feiyu.scripsaying.util.FileUtil;
import com.feiyu.scripsaying.util.HD;

import java.io.File;

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
    private static final int CAMERA_REQUEST = 2;
    private static final String takeCameraSaveDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DCIM" + File.separator + "Camera";
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
                openCamera();

                break;
            case R.id.btn_edit_paper:
                startActivity(new Intent(ctx,PaperEditTextActivity.class));
                break;


        }
    }

    private void openCamera() {
        // 照相
        Intent cameraIntent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
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
            String imgPath = c.getString(columnIndex);

            Bitmap bm = BitmapFactory.decodeFile(imgPath);
            ivPhoto.setImageBitmap(bm);
            c.close();

            if (imgPath.isEmpty()) {
                HD.TLOG("请先选择一张图片");
                return;
            } else {
                Intent intent = new Intent(ctx,PaperEditImgActivity.class);
                intent.putExtra(GlobalConstant.CHOOSE_IMG_KEY,imgPath);
                startActivity(intent);
            }
        }


        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data.getExtras().get("data") != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            File baseFile = FileUtil.getPicBaseFile(takeCameraSaveDir);
            if(baseFile == null){
                Toast.makeText(ReleaseActivity.this, "SD卡不可用,请检查SD卡情况", Toast.LENGTH_SHORT).show();
                return;
            }
            String fileName = FileUtil.getFileName();   //图片名称
            String photoPath = FileUtil.saveBitmap(photo, fileName, baseFile);
            if (!TextUtils.isEmpty(photoPath)) {
                Intent intent = new Intent(ctx, PaperEditImgActivity.class);
                intent.putExtra(GlobalConstant.CHOOSE_IMG_KEY, photoPath);
                startActivity(intent);
            }
        }
    }
}

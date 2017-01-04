package com.feiyu.scripsaying.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.bean.UserInfo;
import com.feiyu.scripsaying.constant.GlobalConstant;
import com.feiyu.scripsaying.util.HD;
import com.feiyu.scripsaying.util.ScripContext;
import com.feiyu.scripsaying.view.GlideCircleTransform;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MeActivity extends AppCompatActivity {

    //子布局控件初始化
    @BindView(R.id.line_my_head_me)
    LinearLayout lineMyHead;
    @BindView(R.id.line_my_name_me)
    LinearLayout lineMyName;
    @BindView(R.id.line_my_sign_me)
    LinearLayout lineMySign;
    @BindView(R.id.line_my_id_me)
    LinearLayout lineMyId;
    @BindView(R.id.line_my_gender)
    LinearLayout lineMyGender;
    @BindView(R.id.line_my_type)
    LinearLayout lineMyType;
    @BindView(R.id.line_my_send_scrip_me)
    LinearLayout lineMySendScrip;
    //具体控件初始化
    @BindView(R.id.img_my_head_me)
    ImageView imgMyHead;
    @BindView(R.id.tv_my_name_me)
    TextView tvMyName;
    @BindView(R.id.tv_my_sign_me)
    TextView tvMySign;
    @BindView(R.id.tv_my_id_me)
    TextView tvMyId;
    @BindView(R.id.tv_my_gender_me)
    TextView tvMyGender;
    @BindView(R.id.tv_my_type)
    TextView tvMyType;

    private String userId;
    private String userName;
    private String userSign;
    private String userGender;
    private String userType;
    private String userIcon;
    //调用系统相册-选择图片
    private static final int IMAGE = 2;

    private Context ctx;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);
        ctx = this;
        if (ScripContext.getInstance() != null) {
            sharedPreferences = ScripContext.getInstance().getSharedPreferences();
            edit = ScripContext.getInstance().getSharedPreferences().edit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次进来都刷新页面
        //因为onCreate方法不是每次进来都执行，所以刷新页面最好在onResume里做。
        userId = sharedPreferences.getString(GlobalConstant.CURRENT_ID, "default");
        userName = sharedPreferences.getString(userId + GlobalConstant.USER_NAME, "default");
        userSign = sharedPreferences.getString(userId + GlobalConstant.USER_SIGN, "暂无签名");
        userGender = sharedPreferences.getString(userId + GlobalConstant.USER_GENDER, "性别未知");
        userType = sharedPreferences.getString(userId + GlobalConstant.USER_TYPE, "default");
        userIcon = sharedPreferences.getString(userId + GlobalConstant.USER_ICON, "default");
        if (userIcon.equals(GlobalConstant.DEFAULT_USER_ICON_URL)) {
            HD.LOG("onResume： 更新头像  "+userIcon);
            Glide.with(ctx).load(R.mipmap.default_head)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.ic_launcher)
                    .transform(new GlideCircleTransform(ctx))
                    .into(imgMyHead);
        } else {
            Glide.with(ctx).load(userIcon)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.ic_launcher)
                    .transform(new GlideCircleTransform(ctx))
                    .into(imgMyHead);
        }
        tvMyId.setText(userId);
        tvMyName.setText(userName);
        tvMySign.setText(userSign);
        tvMyGender.setText(userGender);
        tvMyType.setText(userType);
    }

    @OnClick({R.id.line_my_head_me, R.id.line_my_name_me, R.id.line_my_sign_me, R.id.line_my_gender, R.id.line_my_type, R.id.line_my_send_scrip_me, R.id.line_login_out})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.line_my_head_me:
                //修改头像
                openAlbum();
                break;
            case R.id.line_my_name_me:
                //修改昵称
                startActivity(new Intent(ctx, EditNameActivity.class));
                break;
            case R.id.line_my_sign_me:
                //修改签名
                startActivity(new Intent(ctx, EditSignActivity.class));
                break;
            case R.id.line_my_gender:
                //修改性别 用弹出框修改
                break;
            case R.id.line_my_type:
                //根据性别 修改类型
                startActivity(new Intent(ctx, ChooseMyTypeActivity.class));
                break;
            case R.id.line_my_send_scrip_me:
                //地图展示我发送的纸片
                startActivity(new Intent(this, CustomMarkerActivity.class));
                break;
            case R.id.line_login_out:
                //退出登录
                edit.putBoolean(GlobalConstant.LOGIN_OUT, true);
                edit.apply();
                startActivity(new Intent(ctx, LoginActivity.class));
                finish();
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
            String imgPath = c.getString(columnIndex);

//            Bitmap bm = BitmapFactory.decodeFile(imgPath);
//            imgMyHead.setImageBitmap(bm);
            //先保存本地图片到文件
            edit.putString(userId+GlobalConstant.USER_ICON,imgPath);
            edit.apply();
            c.close();
            HD.TLOG("更新头像..."+imgPath);
            Glide.with(ctx).load(imgPath)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.mipmap.ic_launcher)
                    .transform(new GlideCircleTransform(ctx))
                    .into(imgMyHead);
            File file = new File(imgPath);
            final BmobFile bmobFile = new BmobFile(file);
            bmobFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    final UserInfo u2 = new UserInfo();
                    u2.setUserIcon(bmobFile.getUrl());
                    u2.update(userId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            //执行了onResume方法会快于这个回调，在这个回调里保存头像的网络路径
                            edit.putString(userId+GlobalConstant.USER_ICON,bmobFile.getUrl());
                            HD.TLOG("用户头像更新成功 "+bmobFile.getUrl());
                            edit.apply();
                        }
                    });
                }
            });

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ctx,ScripSayingActivity.class));
        finish();
    }
}

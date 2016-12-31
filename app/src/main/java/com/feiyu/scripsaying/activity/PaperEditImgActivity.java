package com.feiyu.scripsaying.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.bean.ScripMessage;
import com.feiyu.scripsaying.constant.GlobalConstant;
import com.feiyu.scripsaying.util.HD;
import com.feiyu.scripsaying.util.ScripContext;
import com.feiyu.scripsaying.util.recorder.RecordButton;
import com.feiyu.scripsaying.view.GlideCircleTransform;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

/**
 * Created by YueDong on 2016/12/27.
 */
public class PaperEditImgActivity extends BaseActivity {

    @BindView(R.id.edit_img_usericon)
    ImageView imgUserIcon;
    @BindView(R.id.edit_img_scrip_tag)
    ImageView scripTag;
    @BindView(R.id.edit_img_content)
    ImageView scripImgContent;
    @BindView(R.id.edit_btn_scrip_audio)
    RecordButton scripAudio;
    @BindView(R.id.edit_tv_audio_time)
    TextView scripaudioTime;
    @BindView(R.id.edit_text_content)
    EditText scripTextContent;
    @BindView(R.id.edit_send)
    ImageView scripSend;

    private Context ctx;
    private int chooseTag;
    private String chooseImg;
    private String userIcon;
    private String audioPath;
    private String scripText = "";


    //标志位 用来标示上传文件的组合
    //0: 上传文件是: 图片
    //1: 图片+tag             file[1]=tag
    //2：图片+语音    file[1]=audio
    //3: 图片+文字
    //4: 图片+tag+文字      file[1]=tag
    //5: 图片+语音+文字 file[1]=audio
    //6: 图片+语音+tag+文字 file[1]=audio file[2]=tag
    // c42+1=7
    //综上file[]的类型有三种：1: file[1]=tag 2: file[1]=audio 3:file[1]=audio file[2]=tag

    //fileCombination=1  1: file[1]=tag
    //fileCombination=2  2: file[1]=audio
    //fileCombination=3  3: file[1]=audio file[2]=tag

    private int fileCombination = 0;

    //高德定位
    private double lat;//维度
    private double lng;//经度
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    lng = amapLocation.getLongitude();
                    lat = amapLocation.getLatitude();
                    HD.TLOG("lat: " + lat + "   lng: " + lng + "\n" + "位置： " + amapLocation.getAddress());
                    //todo 获取tag图片路径
                    scripText = scripTextContent.getText().toString();
                    sendPaperMessage(chooseImg, audioPath, chooseImg, scripText, new BmobGeoPoint(lng, lat));
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    HD.TOS("location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_edit);
        ButterKnife.bind(this);
        ctx = this;
        initView();

        //加载纸片图片
        chooseImg = getIntent().getStringExtra(GlobalConstant.CHOOSE_IMG_KEY);
        Glide.with(ctx).load(chooseImg)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.mipmap.ic_launcher)
                .into(scripImgContent);
        //加载头像
        if (ScripContext.getInstance() != null) {
            userIcon = ScripContext.getInstance().getSharedPreferences().getString(GlobalConstant.USER_ICON, "default");
            if (!userIcon.equals("default")) {
                Glide.with(ctx).load(userIcon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.mipmap.ic_launcher)
                        .transform(new GlideCircleTransform(ctx))
                        .into(imgUserIcon);
            }
        }


    }

    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();

        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //低功耗定位模式：不会使用GPS和其他传感器，只会使用网络定位（Wi-Fi和基站定位）；
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
//        mLocationOption.setLocationMode(AMapLocationMode.Battery_Saving);

        // 仅用设备定位模式：不需要连接网络，只使用GPS进行定位，这种模式下不支持室内环境的定位，自 v2.9.0 版本支持返回地址描述信息。
        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
//        mLocationOption.setLocationMode(AMapLocationMode.Device_Sensors);

//       获取最近3s内精度最高的一次定位结果：
//设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    private void initView() {
        scripAudio.setAudioFinishRecorderListener(new RecordButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(float seconds, String filePath) {
                HD.TLOG("录音时间： " + seconds + "  录音路径： " + filePath);
                audioPath = filePath;
                scripaudioTime.setText("录音时间：" + seconds + "秒");
            }
        });
    }

    @OnClick({R.id.edit_img_scrip_tag, R.id.edit_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_img_scrip_tag:
                Intent intent = new Intent(ctx, ChooseTagActivity.class);
                startActivityForResult(intent, GlobalConstant.CHOOSE_TAG);
                break;

            case R.id.edit_send:
                //todo 发布
                //初始化定位，获取经纬度
                initLocation();
                break;
        }
    }

    private void sendPaperMessage(String imgurl, String audiourl, String typeurl, String text, BmobGeoPoint bmobGeoPoint) {
        HD.TLOG("sendPaperMessage");
        String userId = ScripContext.getInstance().getSharedPreferences().getString(GlobalConstant.USER_ID, "default");
        String userGender = ScripContext.getInstance().getSharedPreferences().getString(GlobalConstant.USER_GENDER, "default");
        String userType = ScripContext.getInstance().getSharedPreferences().getString(GlobalConstant.USER_TYPE, "default");
        final ScripMessage scripMessage = new ScripMessage();
        scripMessage.setBmobGeoPoint(bmobGeoPoint);
        scripMessage.setScriptext(text);
        scripMessage.setSendUserGender(userGender);
        scripMessage.setSendUserType(userType);
        scripMessage.setSendUserId(userId);
        scripMessage.setLevel("1");

        HD.TLOG(userId + " " + userGender + " " + userType + " " + text);
        //上传多个文件
        String[] filePaths = new String[2];
        //图片路径肯定有的
        filePaths[0] = imgurl;
        //语音文件不一定有，因为用户不一定录音
        if (audiourl.isEmpty()) {
            //没有录音文件，判断是不是有tag
            //如果有tag就加上tag文件
            if (!typeurl.isEmpty()) {
                filePaths[1] = typeurl;
                fileCombination = 1;
            }
        } else {
            //有录音文件
            filePaths[1] = audiourl;
            fileCombination = 2;
            //如果有tag就加上tag文件
            if (!typeurl.isEmpty()) {
                filePaths[2] = typeurl;
                fileCombination = 3;
            }
        }

        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                HD.TLOG("insertDataWithMany -onSuccess :" + urls.size() + "-----" + files + "----" + urls);
                scripMessage.setScripImg(files.get(0));
                switch (fileCombination) {
                    //fileCombination=1  1: file[1]=tag  一共两个文件
                    case 1:
                        if (urls.size() == 2) {//如果全部上传完，则更新该条记录
                            scripMessage.setScripType(files.get(1));
                            insertObject(scripMessage);
                        } else {
                            //有可能上传不完整，中间可能会存在未上传成功的情况，你可以自行处理
                        }
                        break;
                    //fileCombination=2  2: file[1]=audio
                    case 2:
                        if (urls.size() == 2) {//如果全部上传完，则更新该条记录
                            scripMessage.setScripAudio(files.get(1));
                            insertObject(scripMessage);
                        } else {
                            //有可能上传不完整，中间可能会存在未上传成功的情况，你可以自行处理
                        }
                        break;
                    //fileCombination=3  3: file[1]=audio file[2]=tag
                    case 3:
                        if (urls.size() == 3) {//如果全部上传完，则更新该条记录
                            scripMessage.setScripAudio(files.get(1));
                            scripMessage.setScripType(files.get(2));
                            insertObject(scripMessage);
                        } else {
                            //有可能上传不完整，中间可能会存在未上传成功的情况，你可以自行处理
                        }
                        break;
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                // TODO Auto-generated method stub
                HD.TLOG("错误码" + statuscode + ",错误描述：" + errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total,
                                   int totalPercent) {
                HD.TLOG("上传进度 -onProgress :" + curIndex + "---" + curPercent + "---" + total + "----" + totalPercent);
            }
        });

    }

    private void insertObject(final ScripMessage scripMessage) {
        scripMessage.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                HD.TLOG("发布成功！");
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GlobalConstant.CHOOSE_TAG) {
            if (resultCode == RESULT_OK) {
                //加载纸片类型
                chooseTag = data.getIntExtra(GlobalConstant.CHOOSE_TAG_KEY, 0);
                HD.TLOG("纸片类型： " + chooseTag);
                if (chooseTag == 1) {
                    Glide.with(ctx).load(R.mipmap.tag1)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.mipmap.ic_launcher)
                            .transform(new GlideCircleTransform(ctx))
                            .into(scripTag);
                } else if (chooseTag == 2) {
                    Glide.with(ctx).load(R.mipmap.tag2)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.mipmap.ic_launcher)
                            .transform(new GlideCircleTransform(ctx))
                            .into(scripTag);
                } else if (chooseTag == 3) {
                    Glide.with(ctx).load(R.mipmap.tag3)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.mipmap.ic_launcher)
                            .transform(new GlideCircleTransform(ctx))
                            .into(scripTag);
                } else if (chooseTag == 4) {
                    Glide.with(ctx).load(R.mipmap.tag4)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.mipmap.ic_launcher)
                            .transform(new GlideCircleTransform(ctx))
                            .into(scripTag);
                }

            }
        }
    }
}

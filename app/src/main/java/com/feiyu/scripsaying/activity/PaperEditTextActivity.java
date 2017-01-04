package com.feiyu.scripsaying.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.feiyu.scripsaying.view.GlideCircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class PaperEditTextActivity extends BaseActivity {

    @BindView(R.id.img_usericon_scrip_text)
    ImageView imgUserIcon;
    @BindView(R.id.img_scrip_tag_scrip_text)
    ImageView imgScripTag;
    @BindView(R.id.edit_text_content_script_text)
    EditText editTextContent;
    @BindView(R.id.btn_send_scrip_text)
    Button btnSend;

    private String userId;
    private String userIcon;
    private String userGender;
    private String userType;
    private String sendContent;
    private Context ctx;

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
                    sendPaperMessage("textTag", sendContent, new BmobGeoPoint(lng, lat));
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
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_edit_text);
        ButterKnife.bind(this);
        ctx = this;
        if (ScripContext.getInstance() != null) {
            userId = ScripContext.getInstance().getSharedPreferences().getString(GlobalConstant.CURRENT_ID, "default");
            userIcon = ScripContext.getInstance().getSharedPreferences().getString(userId + GlobalConstant.USER_ICON, "default");
            userGender = ScripContext.getInstance().getSharedPreferences().getString(userId + GlobalConstant.USER_GENDER, "default");
            userType = ScripContext.getInstance().getSharedPreferences().getString(userId + GlobalConstant.USER_TYPE, "default");
            userName = ScripContext.getInstance().getSharedPreferences().getString(userId + GlobalConstant.USER_NAME, "default");


            if (!userIcon.equals("default")) {
                Glide.with(ctx).load(userIcon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.mipmap.ic_launcher)
                        .transform(new GlideCircleTransform(ctx))
                        .into(imgUserIcon);
            } else {
                Glide.with(ctx).load(R.mipmap.default_head)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.mipmap.ic_launcher)
                        .transform(new GlideCircleTransform(ctx))
                        .into(imgUserIcon);
            }
        }
    }

    @OnClick({R.id.img_scrip_tag_scrip_text, R.id.btn_send_scrip_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_scrip_tag_scrip_text:
                //todo 选择文字标签
                HD.TLOG("选择文字标签，待开发");
                break;
            case R.id.btn_send_scrip_text:
                HD.TLOG("开始发布");
                sendContent = editTextContent.getText().toString();
                if (!sendContent.isEmpty()) {
                    initLocation();
                } else {
                    HD.TLOG("请输入内容");
                    return;
                }
                break;
        }
    }

    private void sendPaperMessage(String textType, String text, BmobGeoPoint bmobGeoPoint) {
        HD.TLOG("sendPaperMessage");
        ScripMessage scripMessage = new ScripMessage();
        scripMessage.setUserId(userId);
        scripMessage.setUserType(userType);
        scripMessage.setUserGender(userGender);
        scripMessage.setScripText(text);
        scripMessage.setScripTypeText(textType);
        scripMessage.setLevel("1");
        scripMessage.setBmobGeoPoint(bmobGeoPoint);
        scripMessage.setUserName(userName);
        scripMessage.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                HD.TLOG("发布完成");
                finish();
            }
        });
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

}

package com.feiyu.scripsaying.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.adapter.DisCoverPagerAdapter;
import com.feiyu.scripsaying.bean.ScripMessage;
import com.feiyu.scripsaying.util.HD;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.rong.imkit.RongIM;

public class DiscoverActivity extends AppCompatActivity {

    @BindView(R.id.viewpager_discover)
    ViewPager viewPagerDiscover;
    @BindView(R.id.btn_my_message)
    Button btnMyMessage;
    @BindView(R.id.btn_send_scrip)
    Button btnSendScrip;
    @BindView(R.id.img_discover)
    ImageView imgDiscover;

    private Context ctx;
    private List<View> scripViews;
    private DisCoverPagerAdapter disCoverPagerAdapter;

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
                    discoverScrips(lng, lat);

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
        setContentView(R.layout.activity_discover);
        ButterKnife.bind(this);
        ctx = this;
//        scripViews = new ArrayList<View>();
//        LayoutInflater inflater = getLayoutInflater();
//        for (int i = 0; i < 5; i++) {
//            View view = inflater.inflate(R.layout.item_discover_scrip, null);
//            scripViews.add(view);
//        }
//        HD.TLOG("viewpager size: " + scripViews.size());
//        disCoverPagerAdapter = new DisCoverPagerAdapter(scripViews);
//        viewPagerDiscover.setAdapter(disCoverPagerAdapter);
//        HD.TLOG(" viewPagerDiscover.setAdapter(pagerAdapter);");
        imgDiscover.setVisibility(View.VISIBLE);
    }


    @OnClick({R.id.img_discover, R.id.btn_my_message, R.id.btn_send_scrip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_discover:
                //当没有纸片或者滑完纸片的时候显示，点击发现新纸片
                initLocation();
                break;
            case R.id.btn_my_message:
                //我的消息列表
                if (RongIM.getInstance() != null) {
                    HD.LOG("jump_conversation_list");
                    RongIM.getInstance().startConversationList(ctx, null);
                }
                break;
            case R.id.btn_send_scrip:
                //发布纸片
                startActivity(new Intent(ctx, ReleaseActivity.class));
                finish();
                break;
        }
    }

    private void discoverScrips(double mlng, double mlat) {
        BmobQuery query = new BmobQuery<ScripMessage>("ScripMessage");
        double a = 500 / 1000;
        query.addWhereWithinKilometers("gpsAdd", new BmobGeoPoint(mlng, mlat), a);

//      query.addWhereNear("gpsAdd",new BmobGeoPoint(lng,lat));//返回最近的纸片
        Log.i("LHD", "D发现的经纬度： " + "\n" +
                "D经度：" + lng + "\n" +
                "D维度：" + lat);
        //最多展示附近的10条数据
        query.setLimit(10);

        query.findObjects(new FindListener<ScripMessage>() {
            @Override
            public void done(List<ScripMessage> list, BmobException e) {
                if (e == null) {
                    HD.TLOG("发现的纸片：" + list.size());
                    for (ScripMessage s :
                            list) {
                        HD.LOG("发现的纸片：" + s.getSendUserId());
                    }
                } else {
                    HD.TLOG("发现异常： " + e.getMessage());
                }
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

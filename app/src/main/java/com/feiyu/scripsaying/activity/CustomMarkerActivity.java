package com.feiyu.scripsaying.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMapLoadedListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMap.OnMarkerDragListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.maps2d.model.Text;
import com.amap.api.maps2d.model.TextOptions;
import com.feiyu.scripsaying.App;
import com.feiyu.scripsaying.R;
import com.feiyu.scripsaying.bean.ScripMessage;
import com.feiyu.scripsaying.constant.GlobalConstant;
import com.feiyu.scripsaying.util.Constants;
import com.feiyu.scripsaying.util.LogUtil;
import com.feiyu.scripsaying.util.ScripContext;
import com.feiyu.scripsaying.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * AMapV1地图中简单介绍一些Marker的用法.
 */
public class CustomMarkerActivity extends Activity implements OnMarkerClickListener,
        OnInfoWindowClickListener, OnMarkerDragListener, OnMapLoadedListener,
        OnClickListener, InfoWindowAdapter {
    private static final String TAG = "CustomMarkerActivity";
    private MarkerOptions markerOption;
    private TextView markerText;
    private Button markerButton;// 获取屏幕内所有marker的button
    private RadioGroup radioOption;
    private AMap aMap;
    private MapView mapView;
    private Marker marker2;// 有跳动效果的marker对象
    private LatLng latlng = new LatLng(36.061, 103.834);

    //AMapV2地图中介绍自定义定位小蓝点
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private RadioGroup mGPSModeGroup;

    private TextView mLocationErrText;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private ArrayList<BitmapDescriptor> giflist;
    private MarkerOptions markerOptions;
    private LatLng latLng;

    private List<ScripMessage> mList;
    private LatLngBounds bounds;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
        setContentView(R.layout.custommarker_activity);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState); // 此方法必须重写
        init();
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        markerText = (TextView) findViewById(R.id.mark_listenter_text);
        radioOption = (RadioGroup) findViewById(R.id.custom_info_window_options);
        markerButton = (Button) findViewById(R.id.marker_button);
        markerButton.setOnClickListener(this);
        Button clearMap = (Button) findViewById(R.id.clearMap);
        clearMap.setOnClickListener(this);
        Button resetMap = (Button) findViewById(R.id.resetMap);
        resetMap.setOnClickListener(this);
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
//定位当前位置相关
        mLocationErrText = (TextView) findViewById(R.id.location_errInfo_text);
        mLocationErrText.setVisibility(View.GONE);
    }

    private void setUpMap() {
        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        //获取各个纸片的信息
        getPapersPositonFromBmob();


        //AMapV2地图中介绍自定义定位小蓝点
        aMap.setLocationSource(mLocationSource);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        setupLocationStyle();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
//		deactivate();
        stopLocation();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    /**
     * 获取各个纸片的信息
     */
    private void getPapersPositonFromBmob() {
        if (ScripContext.getInstance() != null) {
            String userId = ScripContext.getInstance().getSharedPreferences().getString(GlobalConstant.CURRENT_ID, "default");
            //根据userId查询发送
            BmobQuery<ScripMessage> bmobQuery = new BmobQuery<ScripMessage>("ScripMessage");
            bmobQuery.addWhereEqualTo("userId",userId);
            bmobQuery.setLimit(15);
            bmobQuery.findObjects(new FindListener<ScripMessage>() {


                @Override
                public void done(List<ScripMessage> list, BmobException e) {
                    mList = list;
                    LogUtil.d(TAG,"发送过纸片：："+list.size());
                    if (e == null) {
                        // 往地图上添加marker
                        addMarkersToMap(list);
                    } else {
                        Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        }
    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap(List<ScripMessage> list) {
        //文字显示标注，可以设置显示内容，位置，字体大小颜色，背景色旋转角度,Z值等
        TextOptions textOptions = new TextOptions().position(Constants.BEIJING)
                .text("Text").fontColor(Color.BLACK)
                .backgroundColor(Color.BLUE).fontSize(30).rotate(20).align(Text.ALIGN_CENTER_HORIZONTAL, Text.ALIGN_CENTER_VERTICAL)
                .zIndex(1.f).typeface(Typeface.DEFAULT_BOLD);
        aMap.addText(textOptions);

        for(ScripMessage message:list){
            // 动画效果
            if(giflist ==null){
                giflist = new ArrayList<BitmapDescriptor>();
            }
            giflist.add(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            giflist.add(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_RED));
            giflist.add(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));

            if(markerOptions==null){
                markerOptions = new MarkerOptions();
            }

            if(latLng==null){
                latLng = new LatLng(message.getBmobGeoPoint().getLatitude(), message.getBmobGeoPoint().getLongitude());
            }

            // 设置所有maker显示在当前可视区域地图中
            bounds = new LatLngBounds.Builder()
                    .include(latLng).build();


            markerOptions.anchor(0.5f, 0.5f)
                    .position(latLng)
                    .title("发布时间：")
                    .snippet(message.getCreatedAt())
                    .draggable(true).period(10)
//                    .icons(giflist);
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));

            aMap.addMarker(markerOptions);

            onMapLoaded();
        }





//      .anchor(0.5f, 0.5f)定义marker 图标的锚点。锚点是marker 图标接触地图平面的点。
//      图标的左顶点为（0,0）点，右底点为（1,1）点。默认情况下，锚点为（0.5,1.0）
//        aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//                .position(Constants.CHENGDU).title("成都市")
//                .snippet("成都市:30.679879, 104.064855").draggable(true));
//
//        markerOption = new MarkerOptions();
//        markerOption.position(Constants.XIAN);
//        markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
//        markerOption.draggable(true);
//        markerOption.icon(BitmapDescriptorFactory
//                .fromResource(R.drawable.arrow));
//        marker2 = aMap.addMarker(markerOption);
//        marker2.showInfoWindow();
//        // marker旋转90度
//        marker2.setRotateAngle(90);
//
//        // 动画效果
//        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
//        giflist.add(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        giflist.add(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        giflist.add(BitmapDescriptorFactory
//                .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
//        aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//                .position(Constants.ZHENGZHOU).title("郑州市").icons(giflist)
//                .draggable(true).period(10));
//
//        drawMarkers();// 添加10个带有系统默认icon的marker
    }

    /**
     * 绘制系统默认的1种marker背景图片
     */
//    public void drawMarkers() {
//        Marker marker = aMap.addMarker(new MarkerOptions()
//                .position(latlng)
//                .title("好好学习")
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//                .draggable(true));
//        marker.showInfoWindow();// 设置默认显示一个infowinfow
//    }

    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.equals(marker2)) {//"西安市"
            if (aMap != null) {
                jumpPoint(marker);
            }
        }
        markerText.setText("你点击的是" + marker.getSnippet());
        return false;
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        Point startPoint = proj.toScreenLocation(Constants.XIAN);
        startPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * Constants.XIAN.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * Constants.XIAN.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                aMap.invalidate();// 刷新地图
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });

    }

    /**
     * 监听点击infowindow窗口事件回调
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        ToastUtil.show(this, "你点击了infoWindow窗口" + marker.getTitle());
    }

    /**
     * 监听拖动marker时事件回调
     */
    @Override
    public void onMarkerDrag(Marker marker) {
        String curDes = marker.getTitle() + "拖动时当前位置:(lat,lng)\n("
                + marker.getPosition().latitude + ","
                + marker.getPosition().longitude + ")";
        markerText.setText(curDes);
    }

    /**
     * 监听拖动marker结束事件回调
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        markerText.setText(marker.getTitle() + "停止拖动");
    }

    /**
     * 监听开始拖动marker事件回调
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
        markerText.setText(marker.getTitle() + "开始拖动");
    }

    /**
     * 监听amap地图加载成功事件回调
     */
    @Override
    public void onMapLoaded() {
        showMarkerInMap(mList);

        // 设置所有maker显示在当前可视区域地图中
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(Constants.XIAN).include(Constants.CHENGDU)
//                .include(latlng).include(Constants.ZHENGZHOU).include(Constants.BEIJING).build();
//        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
    }

    /**
     * 设置所有maker显示在当前可视区域地图中
     */
    private void showMarkerInMap(List<ScripMessage> list) {
        if(list!=null&&list.size()>0){
            // 设置所有maker显示在当前可视区域地图中
//            LatLngBounds bounds = new LatLngBounds.Builder()
//                    .include(Constants.XIAN).include(Constants.CHENGDU)
//                    .include(latlng).include(Constants.ZHENGZHOU).include(Constants.BEIJING).build();
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
        }
    }

    /**
     * 监听自定义infowindow窗口的infocontents事件回调
     */
    @Override
    public View getInfoContents(Marker marker) {
        if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_contents) {
            return null;
        }
        View infoContent = getLayoutInflater().inflate(
                R.layout.custom_info_contents, null);
        render(marker, infoContent);
        return infoContent;
    }

    /**
     * 监听自定义infowindow窗口的infowindow事件回调
     */
    @Override
    public View getInfoWindow(Marker marker) {
        if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_window) {
            return null;
        }
        View infoWindow = getLayoutInflater().inflate(
                R.layout.custom_info_window, null);

        render(marker, infoWindow);
        return infoWindow;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
        if (radioOption.getCheckedRadioButtonId() == R.id.custom_info_contents) {
            ((ImageView) view.findViewById(R.id.badge))
                    .setImageResource(R.drawable.badge_sa);
        } else if (radioOption.getCheckedRadioButtonId() == R.id.custom_info_window) {
            ImageView imageView = (ImageView) view.findViewById(R.id.badge);
            imageView.setImageResource(R.drawable.badge_wa);
        }
        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
                    titleText.length(), 0);
            titleUi.setTextSize(15);
            titleUi.setText(titleText);

        } else {
            titleUi.setText("");
        }
        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null) {
            SpannableString snippetText = new SpannableString(snippet);
            snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
                    snippetText.length(), 0);
            snippetUi.setTextSize(20);
            snippetUi.setText(snippetText);
        } else {
            snippetUi.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 清空地图上所有已经标注的marker
             */
            case R.id.clearMap:
                if (aMap != null) {
                    aMap.clear();
                }
                break;
            /**
             * 重新标注所有的marker
             */
            case R.id.resetMap:
                if (aMap != null) {
                    aMap.clear();
                    getPapersPositonFromBmob();
                }
                break;
            // 获取屏幕所有marker
            case R.id.marker_button:
                if (aMap != null) {
                    List<Marker> markers = aMap.getMapScreenMarkers();
                    if (markers == null || markers.size() == 0) {
                        ToastUtil.show(this, "当前屏幕内没有Marker");
                        return;
                    }
                    String tile = "屏幕内有:";
                    for (Marker marker : markers) {
                        tile = tile + marker.getSnippet()+"#";

                    }
                    ToastUtil.show(this, tile+(markers.size()-1)+"张纸片");

                }
                break;
            default:
                break;
        }
    }

    private void setupLocationStyle() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

    LocationSource mLocationSource = new LocationSource() {

        /**
         * 激活定位
         */
        @Override
        public void activate(OnLocationChangedListener listener) {
            mListener = listener;
            if (mlocationClient == null) {
                mlocationClient = new AMapLocationClient(App.getContext());
                mLocationOption = new AMapLocationClientOption();
                //设置定位监听
                mlocationClient.setLocationListener(mAMapLocationListener);
                //设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                //设置定位参数
                mlocationClient.setLocationOption(mLocationOption);
                // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
                // 在定位结束后，在合适的生命周期调用onDestroy()方法
                // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
                mlocationClient.startLocation();
            }
        }

        /**
         * 停止定位
         */
        @Override
        public void deactivate() {
            stopLocation();
        }
    };

    private void stopLocation() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    AMapLocationListener mAMapLocationListener = new AMapLocationListener() {
        /**
         * 定位成功后回调函数
         */
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (mListener != null && amapLocation != null) {
                if (amapLocation != null
                        && amapLocation.getErrorCode() == 0) {
                    mLocationErrText.setVisibility(View.GONE);
                    mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
//				aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
                } else {
                    String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                    Log.e("AmapErr", errText);
                    mLocationErrText.setVisibility(View.VISIBLE);
                    mLocationErrText.setText(errText);
                }
            }
        }
    };
}

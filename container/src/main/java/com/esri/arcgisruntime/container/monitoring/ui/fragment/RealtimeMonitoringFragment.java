package com.esri.arcgisruntime.container.monitoring.ui.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.adapter.SearchPopWindowAdapter;
import com.esri.arcgisruntime.container.monitoring.application.AppManager;
import com.esri.arcgisruntime.container.monitoring.base.BaseFragment;
import com.esri.arcgisruntime.container.monitoring.bean.LocationDetailsBean;
import com.esri.arcgisruntime.container.monitoring.bean.NumberCache;
import com.esri.arcgisruntime.container.monitoring.bean.QueryRuteResult;
import com.esri.arcgisruntime.container.monitoring.bean.RealtimeMonitorBean;
import com.esri.arcgisruntime.container.monitoring.bean.RealtimeMonitorBean.RowsBean;
import com.esri.arcgisruntime.container.monitoring.bean.SearchNumberBean;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.popwindow.PopwindowUtils;
import com.esri.arcgisruntime.container.monitoring.presenter.RealtimeMonitorPresenter;
import com.esri.arcgisruntime.container.monitoring.ui.activity.LoginActivity;
import com.esri.arcgisruntime.container.monitoring.utils.ACache;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;
import com.esri.arcgisruntime.container.monitoring.utils.StatusBarUtils;
import com.esri.arcgisruntime.container.monitoring.view.LocationDetailsLinearLayout;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IRealtimeMonitoring;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.ArcGISVectorTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.IdentifyGraphicsOverlayResult;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by libo on 2019/3/4.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 实时监控
 */
public class RealtimeMonitoringFragment extends BaseFragment implements IRealtimeMonitoring{

    @BindView(R.id.mapView)
    MapView mMapView;
    @BindView(R.id.tvScale)
    TextView tvScale;
    @BindView(R.id.viewline)
    View viewLine;
    @BindView(R.id.content_frame)
    FrameLayout contentFrame;
    @BindView(R.id.tvQueryNumber)
    TextView tvQueryNumber;
    @BindView(R.id.ivArrow)
    ImageView ivArrow;
    @BindView(R.id.tvInputNumber)
    TextView tvInputNumber;
    @BindView(R.id.llQueryNumber)
    LinearLayout llQueryNumber;
    @BindView(R.id.rlRoot)
    RelativeLayout rlRoot;
    Graphic polylineGraphic;
    private ProgressDialog mProgressDialog;
    private GraphicsOverlay mGraphicsOverlay;
    LayoutInflater inflater;
    int initScale = Constants.initScale;
    PictureMarkerSymbol startSourceSymbol;
    PictureMarkerSymbol endSourceSymbol;
    PictureMarkerSymbol pinSourceSymbol;
    PictureMarkerSymbol lockSourceSymbol;
    RealtimeMonitorPresenter realtimeMonitorPresenter;
    int flag; //标记选择 的是集装箱编号 还是关锁编号

    //集装箱编号搜索缓存
    List<String> containerNumberCache;

    //关锁编号搜索缓存
    List<String> lockNumberCache;

    NumberCache numberCahche;

    private Subscription subscribe;

    android.graphics.Point screenPoint;

    SearchPopWindowAdapter popWindowAdapter;

    private List<SearchNumberBean.RowsBean> containerRows;

    private List<SearchNumberBean.RowsBean> lockRows;

    @Override
    protected void setView() {
        initMapView();
        setupSymbols();
        setListener();
        setViewTreeObserver();

        realtimeMonitorPresenter.realtimeMonitorResult(getAllParams());
    }


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_realtime_monitoring_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void initData() {

        realtimeMonitorPresenter = new RealtimeMonitorPresenter(this);
        inflater = getLayoutInflater();
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.progress_title));
        mProgressDialog.setMessage(getString(R.string.progress_message));



    }

    public void initMapView() {
        // create new Vector Tiled Layer from service url
//        ArcGISVectorTiledLayer mVectorTiledLayer = new ArcGISVectorTiledLayer(getResources().getString(R.string.navigation_vector));
        ArcGISTiledLayer mVectorTiledLayer = new ArcGISTiledLayer(getResources().getString(R.string.navigation_vector));

        // set tiled layer as basemap
        Basemap basemap = new Basemap(mVectorTiledLayer);
        // create a map with the basemap
        ArcGISMap mMap = new ArcGISMap(basemap);
        // create a viewpoint from lat, long, scale
        Viewpoint sanDiegoPoint = new Viewpoint(Constants.latitude, Constants.longitude, initScale);
        // set initial map extent
        mMap.setInitialViewpoint(sanDiegoPoint);
        // set the map to be displayed in this view

//        Basemap.Type basemapType = Basemap.Type.TOPOGRAPHIC_VECTOR;
//        final ArcGISMap map = new ArcGISMap(basemapType, 39.877899, 116.37494, 11);
//        mMapView.setMap(map);

        mMapView.setMap(mMap);
        mMapView.setAttributionTextVisible(false);


    }

    /**
     * Set up the Source, Destination and mRouteSymbol graphics symbol
     */
    private void setupSymbols() {

        // set stop locations
        mGraphicsOverlay = new GraphicsOverlay();

        //add the overlay to the map view
        mMapView.getGraphicsOverlays().add(mGraphicsOverlay);

        BitmapDrawable startDrawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.mipmap.location2);
        try {
            pinSourceSymbol = PictureMarkerSymbol.createAsync(startDrawable).get();
            pinSourceSymbol.loadAsync();
            pinSourceSymbol.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                }
            });
            pinSourceSymbol.setOffsetY(20);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        BitmapDrawable lock = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.mipmap.lock);
        try {
            lockSourceSymbol = PictureMarkerSymbol.createAsync(lock).get();
            lockSourceSymbol.loadAsync();
            lockSourceSymbol.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                }
            });
            lockSourceSymbol.setOffsetY(11);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        BitmapDrawable start = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.mipmap.start_site);
        try {
            startSourceSymbol = PictureMarkerSymbol.createAsync(start).get();
            startSourceSymbol.loadAsync();
            startSourceSymbol.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                }
            });
            startSourceSymbol.setOffsetY(11);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        //[DocRef: END]
        BitmapDrawable endDrawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.mipmap.end_site);
        try {
            endSourceSymbol = PictureMarkerSymbol.createAsync(endDrawable).get();
            endSourceSymbol.loadAsync();
            endSourceSymbol.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    //add a new graphic as end point
                }
            });

            //调整图标在地图上标点的偏移量 让图标下部尖点正好在坐标点上
//            pinSourceSymbolFindroute.setOffsetY(12);
//            pinSourceSymbolFindroute.setOffsetX(9);
            endSourceSymbol.setOffsetY(11);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void setListener() {

        //点击标点弹出弹窗的设定
        mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(getActivity(), mMapView) {

            public boolean onSingleTapConfirmed(MotionEvent e) {
                screenPoint = new android.graphics.Point((int) e.getX(), (int) e.getY());


                // identify graphics on the graphics overlay
                final ListenableFuture<IdentifyGraphicsOverlayResult> identifyGraphic = mMapView.identifyGraphicsOverlayAsync(mGraphicsOverlay, screenPoint, 10.0, false, 1);

                identifyGraphic.addDoneListener(new Runnable() {

                    public void run() {
                        try {
                            IdentifyGraphicsOverlayResult grOverlayResult = identifyGraphic.get();
                            // get the list of graphics returned by identify graphic overlay
                            List<Graphic> graphics = grOverlayResult.getGraphics();

                            Callout mCallout = mMapView.getCallout();

                            if (mCallout.isShowing()) {
                                mCallout.dismiss();
                            }

                            if (!graphics.isEmpty()) {
                                // get callout, set content and show
                                Map map = graphics.get(0).getAttributes();
                                if (map != null && map.containsKey("id")) {

                                    String data = graphics.get(0).getAttributes().get("id").toString();

                                    RowsBean siteInfoBean = new Gson().fromJson(data, RowsBean.class);

//                                TextView calloutContent = new TextView(getActivity().getApplicationContext());
//                                calloutContent.setText("clickId== "+id);

                                    Point mapPoint = mMapView.screenToLocation(screenPoint);
//
                                    View view = inflater.inflate(R.layout.callout_layout_loaction, null);
                                    LocationDetailsLinearLayout tvContainerNumber = view.findViewById(R.id.llContainerNumber);
                                    LocationDetailsLinearLayout tvLockNumber = view.findViewById(R.id.llLockNumber);
                                    LocationDetailsLinearLayout tvPathNumber = view.findViewById(R.id.llPathNumber);
                                    LocationDetailsLinearLayout tvStartSite = view.findViewById(R.id.llStartSite);
                                    LocationDetailsLinearLayout tvDestSite = view.findViewById(R.id.llDestSite);
                                    LocationDetailsLinearLayout tvNumberPlate = view.findViewById(R.id.llNumberPlate);
                                    LocationDetailsLinearLayout tvLongitude = view.findViewById(R.id.llLongitude);
                                    LocationDetailsLinearLayout tvLatitude = view.findViewById(R.id.llLatitude);
                                    LocationDetailsLinearLayout tvGetTime = view.findViewById(R.id.llGetTime);
                                    LocationDetailsLinearLayout tvVehicleSpeed = view.findViewById(R.id.llVehicleSpeed);

                                    ImageView ivClose = view.findViewById(R.id.ivClose);
                                    RelativeLayout rl = view.findViewById(R.id.rl);

                                    //设置弹窗的宽为屏幕宽度的 70%
                                    ViewGroup.LayoutParams params = rl.getLayoutParams();
                                    params.width = (int) (ScreenUtils.getScreenWidth(getActivity()) * 0.7);
                                    rl.setLayoutParams(params);


                                    tvContainerNumber.setValue(siteInfoBean.getContainer_code());  //集装箱编号
                                    tvLockNumber.setValue(siteInfoBean.getLock_code());            //关锁编号
                                    tvPathNumber.setValue(siteInfoBean.getRoute_code());           //路径编号
                                    tvStartSite.setValue(siteInfoBean.getLauSiteName());           //起点
                                    tvDestSite.setValue(siteInfoBean.getDesSiteName());            //终点
                                    tvNumberPlate.setValue(siteInfoBean.getPlate_number());        //车牌号
                                    tvLongitude.setValue(siteInfoBean.getLongitude());             //经度
                                    tvLatitude.setValue(siteInfoBean.getLatitude());               //纬度
                                    tvGetTime.setValue(siteInfoBean.getTime());                    //获取时间
                                    tvVehicleSpeed.setValue(siteInfoBean.getSpeed());              //车辆速度
//
//
                                    ivClose.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mCallout.dismiss();
                                        }
                                    });

                                    mCallout.setLocation(mapPoint);
                                    mCallout.setContent(view);
                                    Callout.Style style = new Callout.Style(getActivity());
                                    style.setBorderWidth(1); //设置边框宽度
                                    style.setBorderColor(Color.GRAY); //设置边框颜色
                                    style.setBackgroundColor(Color.WHITE); //设置背景颜色
                                    style.setCornerRadius(5); //设置圆角半径
                                    mCallout.setStyle(style);
                                    mCallout.show();
                                }

                            }
                        } catch (InterruptedException | ExecutionException ie) {
                            ie.printStackTrace();
                        }

                    }
                });

                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                showScale(0);
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                showScale(0);
                return super.onScale(detector);

            }
        });


    }

    private Map<String,String> getLocationDetailsParam(String containerCode,String lockCode) {
        Map<String, String> params = new HashMap<>();
        params.put("Container_code",containerCode);
        params.put("Lock_code",lockCode);
        params = MD5Utils.encryptParams(params);
        return params;

    }

    //定位到当前位置
    private void getCurrentLocation() {

        if (Build.VERSION.SDK_INT >= 23) { //如果系统版本号大于等于23 也就是6.0，就必须动态请求敏感权限（也要配置清单）
            RxPermissions.getInstance(getActivity()).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean granted) {
                    if (granted) { //请求获取权限成功后的操作
                        LocationDisplay locationDisplay = mMapView.getLocationDisplay();
                        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER);

                        //定位后不显示图标
                        locationDisplay.setShowLocation(false);//隐藏符号
                        locationDisplay.setShowAccuracy(false);//隐藏符号的缓存区域
                        locationDisplay.setShowPingAnimation(false);//隐藏位置更新的符号动画

                        locationDisplay.startAsync();

                        Point point = locationDisplay.getMapLocation();

                        Log.i("sss=", point.toString());
                    } else {
//                            MyToast.showShort("需要获取SD卡读取权限来保存图片");
                        Toast.makeText(getActivity(), "获取权限失败", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else { //否则如果是6.0以下系统不需要动态申请权限直接配置清单就可以了

        }
    }

    //显示比例尺
    private void showScale(int initScale) {
        double scale = 0;
        if (initScale == 0) {
            scale = mMapView.getMapScale() / 100;
        } else {
            scale = (double) initScale / 100;
        }

        String unit = "m";

        if (scale > 1000) {

            unit = "km";
            scale = scale / 1000;

        }

        try {
            BigDecimal bigDecimal = new BigDecimal(scale);
            scale = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        }catch (Exception e){

        }

        String result = scale + unit;

        tvScale.setText(result);
    }


    //view加载完成时回调
    public void setViewTreeObserver() {


        rlRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                //10mm 转换成 px像素 设置给比例尺的的宽度
                float wid = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 10, getResources().getDisplayMetrics());
                ViewGroup.LayoutParams params = viewLine.getLayoutParams();
                params.width = (int) wid;
                viewLine.setLayoutParams(params);
                showScale(initScale);

                //只要界面一动就相当于在加载view ，只要加载完 那就会调用这个监听，所以会重复大量的调用，在执行完所需功能后注销掉监听
                rlRoot.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            }
        });

    }


    @Override
    public void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.resume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.dispose();
        if (subscribe!=null)
            subscribe.unsubscribe();
    }


    @OnClick({R.id.tvQueryNumber, R.id.tvInputNumber})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvQueryNumber:

                ArrayList<String> stlist = new ArrayList<>();
                stlist.add(getResources().getString(R.string.container_number));
                stlist.add(getResources().getString(R.string.lock_number));


                PopwindowUtils.PullDownPopWindow(mainActivity, tvQueryNumber, stlist, new PopwindowUtils.OnClickNumberType() {
                    @Override
                    public void onNumberType(String context, int pos) {
                        tvQueryNumber.setText(context);
                        flag = pos;
                        tvInputNumber.setText(getResources().getText(R.string.input_number));

                    }
                });

                break;
            case R.id.tvInputNumber: //隐藏mainActivity title栏，弹出整个遮罩popwindow

                llQueryNumber.setVisibility(View.GONE);
                mainActivity.getViewbg().setVisibility(View.VISIBLE);
                StatusBarUtils.setWindowStatusBarColor(mainActivity,R.color.white);
                changStatusIconCollor(true);

                numberCahche = (NumberCache) ACache.get(mainActivity).getAsObject(Constants.KEY_ACACHE_NUMBERCACHE);
                if (numberCahche == null) {
                    numberCahche = new NumberCache();
                    ACache.get(mainActivity).put(Constants.KEY_ACACHE_NUMBERCACHE, numberCahche); //方便在其他地方拿取
                }
//                containerNumberCache = numberCahche.getContainerNumberCache();
//                lockNumberCache = numberCahche.getLockNumberCache();

                containerRows = numberCahche.getContainerRows();
                lockRows      = numberCahche.getLockRows();



                PopwindowUtils.popWindowQueryNumber(mainActivity, tvInputNumber, flag, numberCahche, new PopwindowUtils.OnCallBackNumberType() {
                    @Override
                    public void dimssPop(EditText editText,int type) {
                        llQueryNumber.setVisibility(View.VISIBLE);
                        mainActivity.getViewbg().setVisibility(View.GONE);
                        StatusBarUtils.setWindowStatusBarColor(mainActivity,R.color.blue);
                        changStatusIconCollor(false);
                        KeyboardUtils.hideSoftInput(mainActivity,editText);

                        flag = type;

                        if (type == 0 ){
                            tvQueryNumber.setText(getResources().getString(R.string.container_number));
                        } else if (type == 1){
                            tvQueryNumber.setText(getResources().getString(R.string.lock_number));
                        }

                    }

                    @Override
                    public void search(String number, int type,SearchPopWindowAdapter adapter) {

                        flag = type;

                        popWindowAdapter = adapter;

                        tvInputNumber.setText(number);

                        //编号为空就搜索全部
                        if (TextUtils.isEmpty(number)){
                            PopwindowUtils.dimssWindow();
                            realtimeMonitorPresenter.realtimeMonitorResult(getAllParams());
                        } else
                            realtimeMonitorPresenter.realtimeMonitorSingleResult(getParams(number, type));
                    }

                    @Override
                    public void onclickSearchHistory(SearchNumberBean.RowsBean rowsBean ,int type) {


                        flag = type;

                        if (flag == 0){

                            tvInputNumber.setText(rowsBean.getContainer_code());

                            boolean isContains = false;

                            String number = rowsBean.getContainer_code();
                            for (int i = 0; i < containerRows.size(); i++) {
                                if (number.equals(containerRows.get(i).getContainer_code())){
                                    //有相同的编号 包含
                                    isContains = true;
                                    break;
                                }
                            }

                            if (!isContains) {

                                if (containerRows.size() < 20) {
                                    containerRows.add(rowsBean);
                                } else {
                                    containerRows.remove(0);
                                    containerRows.add(rowsBean);
                                }
                            }

                        }else if(flag==1){

                            tvInputNumber.setText(rowsBean.getLock_code());

                            boolean isContains = false;

                            String number = rowsBean.getLock_code();
                            for (int i = 0; i < lockRows.size(); i++) {
                                if (number.equals(lockRows.get(i).getLock_code())){
                                    //有相同的编号 包含
                                    isContains = true;
                                    break;
                                }
                            }

                            if (!isContains) {

                                if (lockRows.size() < 20) {
                                    lockRows.add(rowsBean);
                                } else {
                                    lockRows.remove(0);
                                    lockRows.add(rowsBean);
                                }
                            }
                        }

                        numberCahche.setContainerRows(containerRows);
                        numberCahche.setLockRows(lockRows);
                        ACache.get(mainActivity).put(Constants.KEY_ACACHE_NUMBERCACHE, numberCahche);

                        realtimeMonitorPresenter.getLocationDetails(getLocationDetailsParam(rowsBean.getContainer_code(),rowsBean.getLock_code()));

                    }

                    @Override
                    public void onshow(EditText editText) {

                        waitNs(editText);
                    }
                });

                break;
        }
    }




    /**
     * 添加一个 标点
     */
    private void addPoint(RowsBean siteInfoBean) {


        Map attributes = new HashMap();

        Gson gson = new Gson();
        String data = gson.toJson(siteInfoBean);
        attributes.put("id", data);

        double longitude = Constants.longitude;
        double latitude = Constants.latitude;

        if(!TextUtils.isEmpty(siteInfoBean.getLongitude())){
            longitude = Double.valueOf(siteInfoBean.getLongitude());
        }

        if(!TextUtils.isEmpty(siteInfoBean.getLatitude())){
            latitude = Double.valueOf(siteInfoBean.getLatitude());
        }

        Point mSourcePoint = new Point(longitude, latitude, SpatialReferences.getWgs84());
        Graphic pinSourceGraphic = new Graphic(mSourcePoint, attributes, pinSourceSymbol);
        mGraphicsOverlay.getGraphics().add(pinSourceGraphic);
    }

    /**
     * 添加一个 标点
     */
    private void addPoint(LocationDetailsBean locationDetailsBean,int pointType) {

        double longitude = Constants.longitude;
        double latitude = Constants.latitude;


        Graphic pinSourceGraphic = null;
        Map attributes = new HashMap();
        if (pointType == 1){

            RowsBean siteInfoBean = new RowsBean();
            siteInfoBean.setContainer_code(locationDetailsBean.getContainer_code());
            siteInfoBean.setLock_code(locationDetailsBean.getLock_code());
            siteInfoBean.setRoute_code(locationDetailsBean.getRoute_code());
            siteInfoBean.setLauSiteName(locationDetailsBean.getLauSiteName());
            siteInfoBean.setDesSiteName(locationDetailsBean.getDesSiteName());
            siteInfoBean.setPlate_number(locationDetailsBean.getPlate_number());
            siteInfoBean.setLongitude(locationDetailsBean.getLongitude());
            siteInfoBean.setLatitude(locationDetailsBean.getLatitude());
            siteInfoBean.setTime(locationDetailsBean.getTime());
            siteInfoBean.setSpeed(locationDetailsBean.getSpeed());

            Gson gson = new Gson();
            String data = gson.toJson(siteInfoBean);
            attributes.put("id", data);

            if(!TextUtils.isEmpty(locationDetailsBean.getLongitude())){
                longitude = Double.valueOf(locationDetailsBean.getLongitude());
            }

            if(!TextUtils.isEmpty(locationDetailsBean.getLatitude())){
                latitude = Double.valueOf(locationDetailsBean.getLatitude());
            }
            Point mSourcePoint = new Point(longitude, latitude, SpatialReferences.getWgs84());

            if (flag == 0)
                pinSourceGraphic = new Graphic(mSourcePoint, attributes, pinSourceSymbol);
            else if (flag == 1)
                pinSourceGraphic = new Graphic(mSourcePoint, attributes, lockSourceSymbol);

        }else if (pointType == 2){

            if(!TextUtils.isEmpty(locationDetailsBean.getStartLng())){
                longitude = Double.valueOf(locationDetailsBean.getStartLng());
            }

            if(!TextUtils.isEmpty(locationDetailsBean.getStartLat())){
                latitude = Double.valueOf(locationDetailsBean.getStartLat());
            }
            Point mSourcePoint = new Point(longitude, latitude, SpatialReferences.getWgs84());
            pinSourceGraphic = new Graphic(mSourcePoint, attributes, startSourceSymbol);

        }else if (pointType == 3){

            if(!TextUtils.isEmpty(locationDetailsBean.getEndLng())){
                longitude = Double.valueOf(locationDetailsBean.getEndLng());
            }

            if(!TextUtils.isEmpty(locationDetailsBean.getEndLat())){
                latitude = Double.valueOf(locationDetailsBean.getEndLat());
            }
            Point mSourcePoint = new Point(longitude, latitude, SpatialReferences.getWgs84());
            pinSourceGraphic = new Graphic(mSourcePoint, attributes, endSourceSymbol);

        }

        mGraphicsOverlay.getGraphics().add(pinSourceGraphic);
    }

    /**
     * 清除地图上所有标记
     */
    private void removeAllSymbol() {
        mGraphicsOverlay.getGraphics().clear();
        Callout mCallout = mMapView.getCallout();
        if (mCallout.isShowing()) {
            mCallout.dismiss();
        }
    }

    //获取到的所有标点数据
    @Override
    public void rmResult(RealtimeMonitorBean realtimeMonitorBean) {

        removeAllSymbol();
        //更新标点数据
        List<RowsBean> rows = realtimeMonitorBean.getRows();


        //测试数据只打开前500个点 当前服务器返回了 2500多个点 正式数据不可能这么多 顶多500个

        if (rows!=null && rows.size()>0){

            if (rows.size()>500){
                for (int i = 0; i < 500; i++) {
                    addPoint(rows.get(i));
                }
            }else {
                for (RowsBean rowsBean : rows) {
                    addPoint(rowsBean);
                }
            }
        }

    }

    //根据编号查询到结果列表
    @Override
    public void rmSingleResult(SearchNumberBean rowsBean) {

        if (rowsBean!=null){
            List<SearchNumberBean.RowsBean> rows = rowsBean.getRows();
            popWindowAdapter.setIshowDel(false);
            PopwindowUtils.poptvDelAll.setVisibility(View.GONE);
            popWindowAdapter.setDataLists(rows,flag);
        }else {
            popWindowAdapter.setIshowDel(false);
            PopwindowUtils.poptvDelAll.setVisibility(View.GONE);
            popWindowAdapter.setDataLists(null,flag);
        }

    }

    @Override
    public void rmLocationDetails(LocationDetailsBean locationDetailsBean) {
        removeAllSymbol();

        addPoint(locationDetailsBean,1);
        addPoint(locationDetailsBean,2);
        addPoint(locationDetailsBean,3);

        double Slongitude = Constants.testlongitude;
        double Slatitude = Constants.testlatitude;

        double Elongitude = Constants.longitude;
        double Elatitude = Constants.latitude;

        List<Point> points = new ArrayList<>();
        if(!TextUtils.isEmpty(locationDetailsBean.getStartLng())){
            Slongitude = Double.valueOf(locationDetailsBean.getStartLng());
        }

        if(!TextUtils.isEmpty(locationDetailsBean.getStartLat())){
            Slatitude = Double.valueOf(locationDetailsBean.getStartLat());
        }

        if(!TextUtils.isEmpty(locationDetailsBean.getEndLng())){
            Elongitude = Double.valueOf(locationDetailsBean.getEndLng());
        }

        if(!TextUtils.isEmpty(locationDetailsBean.getEndLat())){
            Elatitude = Double.valueOf(locationDetailsBean.getEndLat());
        }

        Point point1 = new Point(Slongitude, Slatitude);
        Point point2 = new Point(Elongitude, Elatitude);

        points.add(point1);
        points.add(point2);


        findRoute(points);

    }


    //绘制路线
    private void findRoute(List<Point> points) {
        mProgressDialog.show();

        //根据经纬度点连接路线
        PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());

        //Create polyline geometry
        for (int j = 0; j < points.size(); j++) {
            polylinePoints.add(points.get(j));
        }

        Polyline polyline = new Polyline(polylinePoints);

        //Create symbol for polyline
        SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3.0f);

        //Create a polyline graphic with geometry and symbol
        polylineGraphic = new Graphic(polyline, polylineSymbol);

        //Add polyline to graphics overlay
        mGraphicsOverlay.getGraphics().add(polylineGraphic);

        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    //获取全部数据
    private Map<String, String> getAllParams() {
        Map<String, String> params = new HashMap<>();
        params = MD5Utils.encryptParams(params);
        return params;
    }

    //根据编号查询数据
    private Map<String, String> getParams(String number, int type) {
        Map<String, String> params = new HashMap<>();

        if (type == 0) //1、Container_code；2、Lock_code
            params.put("type", "Container_code");
        else if (type == 1)
            params.put("type", "Lock_code");

        params.put("code", number);
        params = MD5Utils.encryptParams(params);
        return params;
    }

    //修改状态栏上面图标和字的颜色 true - 黑色，false  - 白色
    public void changStatusIconCollor(boolean setDark) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            View decorView = mainActivity.getWindow().getDecorView();
            if(decorView != null){
                int vis = decorView.getSystemUiVisibility();
                if(setDark){
                    vis |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                } else{
                    vis &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                }
                decorView.setSystemUiVisibility(vis);
            }
        }
    }


    //如等待 N秒 执行
    private void waitNs(EditText editText){
        subscribe = Observable.timer(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        //等待 Ns 后执行的事件
                        KeyboardUtils.showSoftInput(mainActivity,editText);
                    }
                });
    }



}

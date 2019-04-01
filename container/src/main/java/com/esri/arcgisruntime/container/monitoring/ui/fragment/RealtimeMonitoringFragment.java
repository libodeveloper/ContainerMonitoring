package com.esri.arcgisruntime.container.monitoring.ui.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.ScreenUtils;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.container.monitoring.DemoActivity;
import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.base.BaseFragment;
import com.esri.arcgisruntime.container.monitoring.bean.NumberCache;
import com.esri.arcgisruntime.container.monitoring.bean.QueryRuteBean;
import com.esri.arcgisruntime.container.monitoring.bean.QueryRuteResult;
import com.esri.arcgisruntime.container.monitoring.bean.RealtimeMonitorBean;
import com.esri.arcgisruntime.container.monitoring.bean.RealtimeMonitorBean.RowsBean;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.popwindow.PopwindowUtils;
import com.esri.arcgisruntime.container.monitoring.presenter.QueryRoutePresenter;
import com.esri.arcgisruntime.container.monitoring.presenter.RealtimeMonitorPresenter;
import com.esri.arcgisruntime.container.monitoring.utils.ACache;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IQueryRoute;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IRealtimeMonitoring;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.SpatialReferences;
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
import com.esri.arcgisruntime.tasks.networkanalysis.Route;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.functions.Action1;

/**
 * Created by libo on 2019/3/4.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 实时监控
 */
public class RealtimeMonitoringFragment extends BaseFragment implements IRealtimeMonitoring, IQueryRoute {
    private static final String TAG = DemoActivity.class.getSimpleName();
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
    @BindView(R.id.tvStartSite)
    TextView tvStartSite;
    @BindView(R.id.tvEndSite)
    TextView tvEndSite;
    @BindView(R.id.tvSelectRoute)
    TextView tvSelectRoute;
    @BindView(R.id.llFindRoute)
    LinearLayout llFindRoute;
    Unbinder unbinder;

    private ProgressDialog mProgressDialog;
    private RouteTask mRouteTask;
    private RouteParameters mRouteParams;
    private Point mSourcePoint;
    private Point mDestinationPoint;
    private Route mRoute;
    private SimpleLineSymbol mRouteSymbol;
    private GraphicsOverlay mGraphicsOverlay;
    LayoutInflater inflater;
    int initScale = 1500000;
    Graphic pinSourceGraphic;
    Graphic destinationGraphic;
    Graphic polylineGraphic;
    PictureMarkerSymbol pinSourceSymbol;
    PictureMarkerSymbol pinSourceSymbolFindroute;
    RealtimeMonitorPresenter realtimeMonitorPresenter;
    int flag; //标记选择 的是集装箱编号 还是关锁编号

    List<String> numberCacheList;
    NumberCache numberCahche;

    //路线查询
    QueryRoutePresenter queryRoutePresenter;
    List<QueryRuteBean.SiteBean>  endsiteBeans; //终点列表
    private String startSiteId; //起点Id
    private String endSiteId;   //终点Id
    private QueryRuteResult queryRuteResult; //获取的路线数据

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


        queryRoutePresenter = new QueryRoutePresenter(this);

    }

    private void initMapView() {
        // create new Vector Tiled Layer from service url
        ArcGISVectorTiledLayer mVectorTiledLayer = new ArcGISVectorTiledLayer(getResources().getString(R.string.navigation_vector));

        // set tiled layer as basemap
        Basemap basemap = new Basemap(mVectorTiledLayer);
        // create a map with the basemap
        ArcGISMap mMap = new ArcGISMap(basemap);
        // create a viewpoint from lat, long, scale
//        Viewpoint sanDiegoPoint = new Viewpoint(32.7157, -117.1611, initScale);116.37494 , 39.877899
//        Viewpoint sanDiegoPoint = new Viewpoint(-11.5, 17.5, initScale);
        Viewpoint sanDiegoPoint = new Viewpoint(31.214138, 120.869255, initScale);
        // set initial map extent
        mMap.setInitialViewpoint(sanDiegoPoint);
        // set the map to be displayed in this view

//        Basemap.Type basemapType = Basemap.Type.TOPOGRAPHIC_VECTOR;
//        final ArcGISMap map = new ArcGISMap(basemapType, 39.877899, 116.37494, 11);

        mMapView.setMap(mMap);
//        mMapView.setMap(map);
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

        BitmapDrawable startDrawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.mipmap.location);
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


        BitmapDrawable endDrawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.findqueryicon);
        try {
            pinSourceSymbolFindroute = PictureMarkerSymbol.createAsync(endDrawable).get();
            pinSourceSymbolFindroute.loadAsync();
            pinSourceSymbolFindroute.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    //add a new graphic as end point
                }
            });
            pinSourceSymbolFindroute.setOffsetY(20);


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
                final android.graphics.Point screenPoint = new android.graphics.Point((int) e.getX(), (int) e.getY());


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

                                    View view = inflater.inflate(R.layout.callout_layout, null);
                                    TextView tvNumber = view.findViewById(R.id.tvNumber);
                                    TextView tvContainerNumber = view.findViewById(R.id.tvContainerNumber);
                                    TextView tvLockNumber = view.findViewById(R.id.tvLockNumber);
                                    TextView tvPathNumber = view.findViewById(R.id.tvPathNumber);
                                    TextView tvStartSite = view.findViewById(R.id.tvStartSite);
                                    TextView tvDestSite = view.findViewById(R.id.tvDestSite);
                                    TextView tvNumberPlate = view.findViewById(R.id.tvNumberPlate);
                                    TextView tvLongitude = view.findViewById(R.id.tvLongitude);
                                    TextView tvLatitude = view.findViewById(R.id.tvLatitude);
                                    TextView tvGetTime = view.findViewById(R.id.tvGetTime);
                                    TextView tvVehicleSpeed = view.findViewById(R.id.tvVehicleSpeed);
                                    ImageView ivClose = view.findViewById(R.id.ivClose);
                                    RelativeLayout rl = view.findViewById(R.id.rl);

                                    //设置弹窗的宽为屏幕宽度的 70%
                                    ViewGroup.LayoutParams params = rl.getLayoutParams();
                                    params.width = (int) (ScreenUtils.getScreenWidth(getActivity()) * 0.7);
                                    rl.setLayoutParams(params);


//                                    tvNumber.setText(siteInfoBean.getNumber()); 头部编号
                                    tvContainerNumber.setText(siteInfoBean.getContainer_code());
                                    tvLockNumber.setText(siteInfoBean.getLock_code());
                                    tvPathNumber.setText(siteInfoBean.getRoute_code());
                                    tvStartSite.setText(siteInfoBean.getLauSiteName());
                                    tvDestSite.setText(siteInfoBean.getDesSiteName());
                                    tvNumberPlate.setText(siteInfoBean.getPlate_number());
                                    tvLongitude.setText(siteInfoBean.getLongitude());
                                    tvLatitude.setText(siteInfoBean.getLatitude());
                                    tvGetTime.setText(siteInfoBean.getTime());
                                    tvVehicleSpeed.setText(siteInfoBean.getSpeed());


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

        BigDecimal bigDecimal = new BigDecimal(scale);
        scale = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

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


    //设置编号显示隐藏
    public void setQueryNumberVisibilityStatus(boolean visibilityStatus){
        if (llQueryNumber!=null){
            if (visibilityStatus)
                llQueryNumber.setVisibility(View.VISIBLE);
            else
                llQueryNumber.setVisibility(View.GONE);
        }
    }

    //设置路线查询显示隐藏
    public void setFindRouteVisibilityStatus(boolean visibilityStatus){
        if (llFindRoute!=null){
            if (visibilityStatus)
                llFindRoute.setVisibility(View.VISIBLE);
            else
                llFindRoute.setVisibility(View.GONE);
        }
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
    }


    @OnClick({R.id.tvQueryNumber, R.id.tvInputNumber,R.id.tvSelectRoute,  R.id.tvStartSite, R.id.tvEndSite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvQueryNumber:
                ArrayList<String> stlist = new ArrayList<>();
                stlist.add(getResources().getString(R.string.all));
                stlist.add(getResources().getString(R.string.container_number));
                stlist.add(getResources().getString(R.string.lock_number));

                PopwindowUtils.PullDownPopWindow(getActivity(), tvQueryNumber, stlist, new PopwindowUtils.OnClickNumberType() {
                    @Override
                    public void onNumberType(String context, int pos) {
                        tvQueryNumber.setText(context);
                        flag = pos;
                    }
                });

                break;
            case R.id.tvInputNumber: //隐藏mainActivity title栏，弹出整个遮罩popwindow
                llQueryNumber.setVisibility(View.GONE);
                mainActivity.getViewbg().setVisibility(View.VISIBLE);

                numberCahche = (NumberCache) ACache.get(mainActivity).getAsObject(Constants.KEY_ACACHE_NUMBERCACHE);
                if (numberCahche == null) {
                    numberCahche = new NumberCache();
                }
                numberCacheList = numberCahche.getNumberCache();

                PopwindowUtils.popWindowQueryNumber(mainActivity, tvInputNumber, flag, numberCacheList, new PopwindowUtils.OnCallBackNumberType() {
                    @Override
                    public void dimssPop() {
                        llQueryNumber.setVisibility(View.VISIBLE);
                        mainActivity.getViewbg().setVisibility(View.GONE);
                    }

                    @Override
                    public void search(String number, int type) {
                        if (!numberCacheList.contains(number)) {
                            if (numberCacheList.size() < 10) {
                                numberCacheList.add(number);
                            } else {
                                numberCacheList.remove(0);
                                numberCacheList.add(number);
                            }
                            numberCahche.setNumberCache(numberCacheList);
                            ACache.get(mainActivity).put(Constants.KEY_ACACHE_NUMBERCACHE, numberCahche);
                        }

                        realtimeMonitorPresenter.realtimeMonitorSingleResult(getParams(number, type));
                    }

                    @Override
                    public void onclickSearchHistory(int pos) {

                    }
                });
                break;

                //路线查询
            case R.id.tvSelectRoute:
                String start = tvStartSite.getText().toString();
                String end   = tvEndSite.getText().toString();
                if (start.equals(getResources().getString(R.string.start_site))){
                    MyToast.showShort(getResources().getString(R.string.please_choose_the_starting_point));
                }else if(end.equals(getResources().getString(R.string.dest_site))){
                    MyToast.showShort(getResources().getString(R.string.please_choose_the_end_point));
                }else {

                    ArrayList<String> routelist = new ArrayList<>();
                    List<QueryRuteResult.RowsBean> rows = queryRuteResult.getRows();

                    for (QueryRuteResult.RowsBean rowsBean : rows) {
                        String siteName = rowsBean.getSiteName();//getAddress()?
//                           if (!TextUtils.isEmpty(siteName))
                        routelist.add(siteName);
                    }

                    PopwindowUtils.PullDownPopWindow(getActivity(), tvSelectRoute, routelist, new PopwindowUtils.OnClickNumberType() {
                        @Override
                        public void onNumberType(String context,int pos) {

                            tvSelectRoute.setText(context);

                            //拿出对应路线的经纬度集合 绘制路线
                            List<String> latLongArray = rows.get(pos).getArray();

                            findRoute(createPoint(latLongArray));  //选择一条线路后地图进行绘制

                        }
                    });

                }

                break;
            case R.id.tvStartSite:
                tvSelectRoute.setText(getResources().getText(R.string.choose_the_route));
                Map<String ,String> params = new HashMap<>();
                params = MD5Utils.encryptParams(params);
                queryRoutePresenter.queryStartRoute(params);

                break;
            case R.id.tvEndSite:
                String start1 = tvStartSite.getText().toString();
                if (start1.equals(getResources().getString(R.string.start_site))){
                    MyToast.showShort(getResources().getString(R.string.please_choose_the_starting_point));
                }else {

                    ArrayList<String> endlist = new ArrayList<>();

                    for (QueryRuteBean.SiteBean siteBean : endsiteBeans) {
                        String siteName = siteBean.getSiteName();
//                           if (!TextUtils.isEmpty(siteName))
                        endlist.add(siteName);
                    }

                    PopwindowUtils.PullDownPopWindow(getActivity(), tvEndSite, endlist, new PopwindowUtils.OnClickNumberType() {
                        @Override
                        public void onNumberType(String context,int pos) {
                            tvEndSite.setText(context);

                            //终点也请求成功后 拿取 起点 终点数据请求相应路线
                            endSiteId = endsiteBeans.get(pos).getEnd_site_id();
                            Map<String ,String> params = new HashMap<>();
                            params.put("startPoint",startSiteId);
                            params.put("endPoint",endSiteId);
                            params = MD5Utils.encryptParams(params);
                            queryRoutePresenter.queryRouteResult(params);

                        }
                    });
                }

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

        double longitude = Double.valueOf(siteInfoBean.getLongitude());
        double latitude = Double.valueOf(siteInfoBean.getLatitude());


        Point mSourcePoint = new Point(longitude, latitude, SpatialReferences.getWgs84());
        Graphic pinSourceGraphic = new Graphic(mSourcePoint, attributes, pinSourceSymbol);
        mGraphicsOverlay.getGraphics().add(pinSourceGraphic);
    }

    /**
     * 清除地图上所有标记
     */
    private void removeAllSymbol() {
        mGraphicsOverlay.getGraphics().clear();
    }

    //获取到的所有标点数据
    @Override
    public void rmResult(RealtimeMonitorBean realtimeMonitorBean) {
        removeAllSymbol();
        //更新标点数据
        List<RowsBean> rows = realtimeMonitorBean.getRows();
        for (RowsBean rowsBean : rows) {
            addPoint(rowsBean);
        }

    }

    //根据编号查询到的某点
    @Override
    public void rmSingleResult(RowsBean rowsBean) {
        removeAllSymbol();
        //更新标点数据
        addPoint(rowsBean);
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
        params.put("type", type + "");
        params.put("code", number);
        params = MD5Utils.encryptParams(params);
        return params;
    }


    //路线查询
    //绘制路线
    private void findRoute(List<Point> points) {
        mProgressDialog.show();

        // create RouteTask instance
        mRouteTask = new RouteTask(getActivity().getApplicationContext(), getString(R.string.routing_service));

        final ListenableFuture<RouteParameters> listenableFuture = mRouteTask.createDefaultParametersAsync();
        listenableFuture.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    if (listenableFuture.isDone()) {
                        int i = 0;
                        mRouteParams = listenableFuture.get();


                        //根据经纬度点连接路线
                        PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());

                        //把这里的点替换成接口返回的
//                                Point point1 = new Point(116.37494 , 39.877899);
//                                Point point2 = new Point(116.315889 , 39.991886);
//                        Point point3 = new Point(116.374254, 39.889227); //前经，后纬
//                        Point point4 = new Point(116.374254, 39.894495);
//                        Point point5 = new Point(116.374254, 39.899763);
//                        Point point6 = new Point(116.374254, 39.903977);
//                        Point point7 = new Point(116.374254, 39.906874);
//                        Point point8 = new Point(116.38215, 39.906874);
//                        Point point9 = new Point(116.38627, 39.906874);
//                        Point point10 = new Point(116.39657, 39.906874);
//                        Point point11 = new Point(116.401719, 39.906874);
//                        Point point12 = new Point(116.407899, 39.906874);
//                        Point point13 = new Point(116.413049, 39.906874);
//                        Point point14 = new Point(116.415796, 39.906874);
//                        Point point15 = new Point(116.417512, 39.916355);

                        removeAllSymbol();

                        //Create polyline geometry
                        for (int j = 0; j < points.size(); j++) {
                            polylinePoints.add(points.get(j));
                        }

//                        polylinePoints.add(point3);
//                        polylinePoints.add(point4);
//                        polylinePoints.add(point5);
//                        polylinePoints.add(point6);
//                        polylinePoints.add(point7);
//                        polylinePoints.add(point8);
//                        polylinePoints.add(point9);
//                        polylinePoints.add(point10);
//                        polylinePoints.add(point11);
//                        polylinePoints.add(point12);
//                        polylinePoints.add(point13);
//                        polylinePoints.add(point14);
//                        polylinePoints.add(point15);

                        Polyline polyline = new Polyline(polylinePoints);

                        //Create symbol for polyline
                        SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3.0f);

                        //Create a polyline graphic with geometry and symbol
                        polylineGraphic = new Graphic(polyline, polylineSymbol);

                        //Add polyline to graphics overlay
                        mGraphicsOverlay.getGraphics().add(polylineGraphic);

                        //起点终点的标点
                        addPoint(points.get(0));
                        addPoint(points.get(points.size()-1));



                        if (mProgressDialog.isShowing()) {
                            mProgressDialog.dismiss();
                        }

                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }


    private List<Point> createPoint(List<String> pointSourceList){
        List<Point> points = new ArrayList<>();
//待接口经纬度返回正常后在打开
//        if (pointSourceList!=null){
//            for (int i = 0; i < pointSourceList.size(); i++) {
//                String data = pointSourceList.get(i);
//                String[] datas = data.split(",");
//                double lat = Double.valueOf(datas[0]);
//                double lon = Double.valueOf(datas[1]);
//                Point point = new Point(lon, lat);
//                points.add(point);
//            }
//        }


        Point point1 = new Point(121.523066, 31.271619);
        Point point2 = new Point(121.437922, 31.285703);
        Point point3 = new Point(121.446162, 31.237574);

        points.add(point1);
        points.add(point2);
        points.add(point3);

        return points;
    }


    /**
     * 添加一个 标点
     */
    private void addPoint(Point point){
        Map attributes = new HashMap();
        Point  mSourcePoint = new Point(point.getX(), point.getY(), SpatialReferences.getWgs84());
        Graphic pinSourceGraphic = new Graphic(mSourcePoint, attributes, pinSourceSymbolFindroute);
        mGraphicsOverlay.getGraphics().add(pinSourceGraphic);
    }

    //路线查询   起点列表
    @Override
    public void queryStartSucceed(QueryRuteBean queryRuteBean) {
        List<QueryRuteBean.SiteBean>  siteBeans = queryRuteBean.getRows();
        ArrayList<String> startlist = new ArrayList<>();

        for (QueryRuteBean.SiteBean siteBean : siteBeans) {
            String siteName = siteBean.getSiteName();
//                if (!TextUtils.isEmpty(siteName))
            startlist.add(siteName);
        }

        PopwindowUtils.PullDownPopWindow(getActivity(), tvStartSite, startlist, new PopwindowUtils.OnClickNumberType() {
            @Override
            public void onNumberType(String context,int pos) {
                tvStartSite.setText(context);
                //选择好起点后 根据起点，去请求对应的各个终点
                startSiteId = siteBeans.get(pos).getStart_site_id();
                Map<String ,String> params = new HashMap<>();
                params.put("startPoint",startSiteId);
                params = MD5Utils.encryptParams(params);
                queryRoutePresenter.queryEndRoute(params);
            }
        });
    }

    //路线查询   终点列表
    @Override
    public void queryEndSucceed(QueryRuteBean queryRuteBean) {
        //存储起点对应的终点列表 供点击终点时取出来展示
                endsiteBeans = queryRuteBean.getRows();
    }

    //路线查询   查询的路线
    @Override
    public void queryRouteSucceed(QueryRuteResult queryRuteResult) {
//储存结果线路
        //最终路线请求成功后 点击请选择路线时，把结果列表展示出来，根据选择某一条路线进行地图标注展示
        this.queryRuteResult = queryRuteResult;
    }
}

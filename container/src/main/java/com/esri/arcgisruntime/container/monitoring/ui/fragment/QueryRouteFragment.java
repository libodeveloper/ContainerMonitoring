package com.esri.arcgisruntime.container.monitoring.ui.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.esri.arcgisruntime.container.monitoring.bean.SiteInfoBean;
import com.esri.arcgisruntime.container.monitoring.popwindow.PopwindowUtils;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;
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
import rx.functions.Action1;

/**
 * Created by libo on 2019/3/4.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 路线查询
 */
public class QueryRouteFragment extends BaseFragment {
    private static final String TAG = DemoActivity.class.getSimpleName();
    @BindView(R.id.mapView)
    MapView mMapView;
    @BindView(R.id.directionFAB)
    FloatingActionButton mDirectionFab;
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
    @BindView(R.id.tvStartSite)
    TextView tvStartSite;
    @BindView(R.id.tvEndSite)
    TextView tvEndSite;
    @BindView(R.id.tvRoute)
    TextView tvRoute;
    @BindView(R.id.llFindRoute)
    LinearLayout llFindRoute;
    @BindView(R.id.rlRoot)
    RelativeLayout rlRoot;

    private ProgressDialog mProgressDialog;
    private RouteTask mRouteTask;
    private RouteParameters mRouteParams;
    private Point mSourcePoint;
    private Point mDestinationPoint;
    private Route mRoute;
    private SimpleLineSymbol mRouteSymbol;
    private GraphicsOverlay mGraphicsOverlay;
    LayoutInflater inflater;
    int initScale = 200000;
    Graphic pinSourceGraphic;
    Graphic destinationGraphic;
    Graphic polylineGraphic;
    PictureMarkerSymbol pinSourceSymbol;
    PictureMarkerSymbol pinSourceSymbolFindroute;
    @Override
    protected void setView() {
        setQueryNumberVisibilityStatus(false);
        setFindRouteVisibilityStatus(true);
        initMapView();
        setupSymbols();
        setListener();
        setViewTreeObserver();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_realtime_monitoring_layout, container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {

    }

    @Override
    public void initData() {

        inflater = getLayoutInflater();
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.progress_title));
        mProgressDialog.setMessage(getString(R.string.progress_message));


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
        Viewpoint sanDiegoPoint = new Viewpoint(39.877899, 116.37494, initScale);
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
        //[DocRef: END]
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
        //[DocRef: END]
        mRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 5);
    }

    private void setListener() {
        // update UI when attribution view changes
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mDirectionFab.getLayoutParams();
        mMapView.addAttributionViewLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int heightDelta = (bottom - oldBottom);
                params.bottomMargin += heightDelta;
            }
        });


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
                                if (map!=null&&map.containsKey("id")){

                                    String data = graphics.get(0).getAttributes().get("id").toString();


                                    SiteInfoBean siteInfoBean = new Gson().fromJson(data,SiteInfoBean.class);

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


                                    tvNumber.setText(siteInfoBean.getNumber());
                                    tvContainerNumber.setText(siteInfoBean.getContainerNumber());
                                    tvLockNumber.setText(siteInfoBean.getLockNumber());
                                    tvPathNumber.setText(siteInfoBean.getPathNumber());
                                    tvStartSite.setText(siteInfoBean.getStartSite());
                                    tvDestSite.setText(siteInfoBean.getDestSite());
                                    tvNumberPlate.setText(siteInfoBean.getNumberPlate());
                                    tvLongitude.setText(siteInfoBean.getLongitude());
                                    tvLatitude.setText(siteInfoBean.getLatitude());
                                    tvGetTime.setText(siteInfoBean.getGetTime());
                                    tvVehicleSpeed.setText(siteInfoBean.getVehicleSpeed());


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

    //查询路线
    private void findRoute() {
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
//                                Point point1 = new Point(116.37494 , 39.877899);
//                                Point point2 = new Point(116.315889 , 39.991886);
                        Point point3 = new Point(116.374254, 39.889227);
                        Point point4 = new Point(116.374254, 39.894495);
                        Point point5 = new Point(116.374254, 39.899763);
                        Point point6 = new Point(116.374254, 39.903977);
                        Point point7 = new Point(116.374254, 39.906874);
                        Point point8 = new Point(116.38215, 39.906874);
                        Point point9 = new Point(116.38627, 39.906874);
                        Point point10 = new Point(116.39657, 39.906874);
                        Point point11 = new Point(116.401719, 39.906874);
                        Point point12 = new Point(116.407899, 39.906874);
                        Point point13 = new Point(116.413049, 39.906874);
                        Point point14 = new Point(116.415796, 39.906874);
                        Point point15 = new Point(116.417512, 39.916355);

                        removeAllSymbol();



//                                double weidu = 39.889227;

//                                for (int j = 0; j < 1000000; j++) {
//                                    weidu+=0.001;
//                                    Point point  = new Point(116.374254,weidu);
//                                    polylinePoints.add(point);
//                                }


                        //Create polyline geometry


                        polylinePoints.add(point3);
                        polylinePoints.add(point4);
                        polylinePoints.add(point5);
                        polylinePoints.add(point6);
                        polylinePoints.add(point7);
                        polylinePoints.add(point8);
                        polylinePoints.add(point9);
                        polylinePoints.add(point10);
                        polylinePoints.add(point11);
                        polylinePoints.add(point12);
                        polylinePoints.add(point13);
                        polylinePoints.add(point14);
                        polylinePoints.add(point15);

                        Polyline polyline = new Polyline(polylinePoints);

                        //Create symbol for polyline
                        SimpleLineSymbol polylineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 3.0f);

                        //Create a polyline graphic with geometry and symbol
                        polylineGraphic = new Graphic(polyline, polylineSymbol);

                        //Add polyline to graphics overlay
                        mGraphicsOverlay.getGraphics().add(polylineGraphic);

                        SiteInfoBean siteInfoBean = new SiteInfoBean("AAA");
                        SiteInfoBean siteInfoBean2 = new SiteInfoBean("BBB");

                        addPoint(116.374254, 39.889227,"id",siteInfoBean);
                        addPoint(116.417512, 39.916355,"id",siteInfoBean2);


                        //规划路线
//                        // create stops
//                        Stop stop1 = new Stop(new Point(-117.15083257944445, 32.741123367963446, SpatialReferences.getWgs84()));
//                        Stop stop2 = new Stop(new Point(-117.15557279683529, 32.703360305883045, SpatialReferences.getWgs84()));
//
//
//                        List<Stop> routeStops = new ArrayList<>();
//                        // add stops
//                        routeStops.add(stop1);
//                        routeStops.add(stop2);
//                        mRouteParams.setStops(routeStops);
//
//                        // set return directions as true to return turn-by-turn directions in the result of
//                        // getDirectionManeuvers().
//                        mRouteParams.setReturnDirections(true);
//
//                        // solve
//                        RouteResult result = mRouteTask.solveRouteAsync(mRouteParams).get();
//                        final List routes = result.getRoutes();
//                        mRoute = (Route) routes.get(0);
//                        // create a mRouteSymbol graphic
//                        Graphic routeGraphic = new Graphic(mRoute.getRouteGeometry(), mRouteSymbol);
//                        // add mRouteSymbol graphic to the map
//                        mGraphicsOverlay.getGraphics().add(routeGraphic);
//
//                        // get directions
//                        // NOTE: to get turn-by-turn directions Route Parameters should set returnDirection flag as true
//                        final List<DirectionManeuver> directions = mRoute.getDirectionManeuvers();
//
//                        String[] directionsArray = new String[directions.size()];
//
//                        for (DirectionManeuver dm : directions) {
//                            directionsArray[i++] = dm.getDirectionText();
//                        }
//
//                        Log.d(TAG, directions.get(0).getGeometry().getExtent().getXMin() + "");
//                        Log.d(TAG, directions.get(0).getGeometry().getExtent().getYMin() + "");


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


    public void setFABvisibilityStatus(boolean status) {

        if (mDirectionFab != null) {
//            if (status) mDirectionFab.setVisibility(View.VISIBLE);
//            else mDirectionFab.setVisibility(View.GONE);
        }
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


    @OnClick({R.id.tvQueryNumber, R.id.tvInputNumber,  R.id.tvStartSite, R.id.tvEndSite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvQueryNumber:
                ArrayList<String> stlist = new ArrayList<>();
                stlist.add(getResources().getString(R.string.container_number));
                stlist.add(getResources().getString(R.string.lock_number));
                PopwindowUtils.PullDownPopWindow(getActivity(), tvQueryNumber, stlist, new PopwindowUtils.OnClickNumberType() {
                    @Override
                    public void onNumberType(String context) {
                        tvQueryNumber.setText(context);
                    }
                });

                break;
            case R.id.tvInputNumber: //隐藏mainActivity title栏，弹出整个遮罩popwindow
                llQueryNumber.setVisibility(View.GONE);

                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                      int temp = i+1;
                      list.add("LNTSS00000"+temp);
                }

                PopwindowUtils.popWindowQueryNumber(mainActivity, tvInputNumber, list, new PopwindowUtils.OnCallBackNumberType() {
                    @Override
                    public void dimssPop() {
                        llQueryNumber.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void search(String number) {
                        MyToast.showShort("search number "+number);
                        removeAllSymbol();
                        addPoint(116.38627, 39.906874,"id",new SiteInfoBean("CCC"));
                    }

                    @Override
                    public void onclickSearchHistory(int pos) {

                    }
                });

                break;
            case R.id.tvStartSite:
                ArrayList<String> startlist = new ArrayList<>();
                startlist.add("起点路线111");
                startlist.add("起点路线222");
                PopwindowUtils.PullDownPopWindow(getActivity(), tvStartSite, startlist, new PopwindowUtils.OnClickNumberType() {
                    @Override
                    public void onNumberType(String context) {
                        tvStartSite.setText(context);
                    }
                });
                break;
            case R.id.tvEndSite:
                ArrayList<String> endlist = new ArrayList<>();
                endlist.add("终点路线11111111");
                endlist.add("终点路线22222222");
                PopwindowUtils.PullDownPopWindow(getActivity(), tvEndSite, endlist, new PopwindowUtils.OnClickNumberType() {
                    @Override
                    public void onNumberType(String context) {
                        tvEndSite.setText(context);

                        String route = tvStartSite.getText().toString() + tvEndSite.getText().toString();

                        tvRoute.setText(route);

                        findRoute();
                    }
                });
                break;
        }
    }


    /**
     * 添加一个 标点
     * @param longitude  经度
     * @param latitude   纬度
     */
    private void addPoint(double longitude,double latitude,String key,SiteInfoBean siteInfoBean){
        Map attributes = new HashMap();

        Gson gson = new Gson();
        String data = gson.toJson(siteInfoBean);

        attributes.put(key, data);

        Point  mSourcePoint = new Point(longitude, latitude, SpatialReferences.getWgs84());
        Graphic pinSourceGraphic = new Graphic(mSourcePoint, attributes, pinSourceSymbol);
        mGraphicsOverlay.getGraphics().add(pinSourceGraphic);
    }


    /**
     * 添加一个 标点 路线查询时用这个
     * @param longitude  经度
     * @param latitude   纬度
     */
    private void addPointforFindRoute(double longitude,double latitude,String key,String value){
        Map attributes = new HashMap();
        attributes.put(key, value);
        Point  mSourcePoint = new Point(longitude, latitude, SpatialReferences.getWgs84());
        Graphic pinSourceGraphic = new Graphic(mSourcePoint, attributes, pinSourceSymbolFindroute);
        mGraphicsOverlay.getGraphics().add(pinSourceGraphic);
    }


    /**
     * 清除地图上所有标记
     */
    private void removeAllSymbol(){
        mGraphicsOverlay.getGraphics().clear();
    }

}

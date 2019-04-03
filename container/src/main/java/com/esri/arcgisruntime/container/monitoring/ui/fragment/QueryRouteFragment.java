package com.esri.arcgisruntime.container.monitoring.ui.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.esri.arcgisruntime.container.monitoring.bean.QueryRuteBean;
import com.esri.arcgisruntime.container.monitoring.bean.QueryRuteResult;
import com.esri.arcgisruntime.container.monitoring.bean.QueryRuteResult.RowsBean.ArrayBean;
import com.esri.arcgisruntime.container.monitoring.bean.SiteInfoBean;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.popwindow.PopwindowUtils;
import com.esri.arcgisruntime.container.monitoring.presenter.QueryRoutePresenter;
import com.esri.arcgisruntime.container.monitoring.utils.BuilderParams;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IQueryRoute;
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
public class QueryRouteFragment extends BaseFragment implements IQueryRoute{
    @BindView(R.id.tsetmapView)
    MapView mMapView;
    @BindView(R.id.tvScale)
    TextView tvScale;
    @BindView(R.id.viewline)
    View viewLine;
    @BindView(R.id.content_frame)
    FrameLayout contentFrame;
    @BindView(R.id.tvStartSite)
    TextView tvStartSite;
    @BindView(R.id.tvEndSite)
    TextView tvEndSite;
    @BindView(R.id.tvSelectRoute)
    TextView tvSelectRoute;
    @BindView(R.id.llFindRoute)
    LinearLayout llFindRoute;
    @BindView(R.id.rlRoot)
    RelativeLayout rlRoot;
    private ProgressDialog mProgressDialog;
    private RouteTask mRouteTask;
    private SimpleLineSymbol mRouteSymbol;
    private GraphicsOverlay mGraphicsOverlay;
    LayoutInflater inflater;
//    int initScale = Constants.initScale;
    int initScale = 200000;
    Graphic polylineGraphic;
    PictureMarkerSymbol pinSourceSymbol;
    PictureMarkerSymbol pinSourceSymbolFindroute;

    QueryRoutePresenter queryRoutePresenter;
    List<QueryRuteBean.SiteBean>  siteBeans; //起点列表
    ArrayList<String> startlist; //起点名称列表传入下拉菜单用的
    List<QueryRuteBean.SiteBean>  endsiteBeans; //终点列表
    private String startSiteId; //起点Id
    private String endSiteId;   //终点Id
    private QueryRuteResult queryRuteResult; //获取的路线数据

    //起点标点
    private String startLat;
    private String startLng;

    //终点标点
    private String endLat;
    private String endLng;



    @Override
    protected void setView() {

        initMapView();
        setupSymbols();
//        setListener(); 不用点击标点弹窗
        setViewTreeObserver();
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_query_route_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void initData() {
        queryRoutePresenter = new QueryRoutePresenter(this);
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
//        Viewpoint sanDiegoPoint = new Viewpoint(-11.5, 17.5, initScale); 安哥拉位置
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

            //调整图标在地图上标点的偏移量 让图标下部尖点正好在坐标点上
            pinSourceSymbolFindroute.setOffsetY(12);
            pinSourceSymbolFindroute.setOffsetX(9);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //[DocRef: END]
        mRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 5);
    }

    private void setListener() {


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

                        //起点终点的标点 测试数据 待服务器接口完善后注掉，打开下面正式数据
                        addPoint(points.get(0));
                        addPoint(points.get(points.size()-1));

                        //正式 数据
//                        double slat = Double.valueOf(startLat);
//                        double slng = Double.valueOf(startLng);
//
//                        double elat = Double.valueOf(endLat);
//                        double elng = Double.valueOf(endLng);
//
//                        addPoint(slng,slat);
//                        addPoint(elng,elat);

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


    private List<Point> createPoint(List<ArrayBean> pointSourceList){
        List<Point> points = new ArrayList<>();
        //待接口经纬度返回正常后在打开
//        if (pointSourceList!=null){
//            for (int i = 0; i < pointSourceList.size(); i++) {
//                ArrayBean data = pointSourceList.get(i);
//                double lat = Double.valueOf(data.getLat());
//                double lon = Double.valueOf(data.getLng());
//                Point point = new Point(lon, lat);
//                points.add(point);
//            }
//        }

//test数据 接口返回的经纬度 明显错误
        Point point1 = new Point(121.523066, 31.271619);
        Point point2 = new Point(121.437922, 31.285703);
        Point point3 = new Point(121.446162, 31.237574);

        points.add(point1);
        points.add(point2);
        points.add(point3);

        return points;
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


    @OnClick({ R.id.tvSelectRoute,  R.id.tvStartSite, R.id.tvEndSite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvSelectRoute:
                String start = tvStartSite.getText().toString();
                String end   = tvEndSite.getText().toString();
                if (start.equals(getResources().getString(R.string.start_site))){
                    MyToast.showShort(getResources().getString(R.string.please_choose_the_starting_point));
                }else if(end.equals(getResources().getString(R.string.dest_site))){
                    MyToast.showShort(getResources().getString(R.string.please_choose_the_end_point));
                }else {
                    if (queryRuteResult==null)return;

                    ArrayList<String> routelist = new ArrayList<>();
                    List<QueryRuteResult.RowsBean> rows = queryRuteResult.getRows();

                   if (rows!=null && rows.size()>0){

                        for (QueryRuteResult.RowsBean rowsBean : rows) {
                            String siteName = rowsBean.getSiteName();//getAddress()?
    //                           if (!TextUtils.isEmpty(siteName))
                            if (TextUtils.isEmpty(siteName))
                                siteName="testRoute";

                            routelist.add(siteName);
                        }

                        PopwindowUtils.PullDownPopWindow(getActivity(), tvSelectRoute, routelist, new PopwindowUtils.OnClickNumberType() {
                            @Override
                            public void onNumberType(String context,int pos) {

                                tvSelectRoute.setText(context);

                                //拿出对应路线的经纬度集合 绘制路线
                                List<ArrayBean> latLongArray = rows.get(pos).getArray();

    //                            if (latLongArray!=null && latLongArray.size()>0) TODO 测试注掉，待接口完整打开
                                findRoute(createPoint(latLongArray));  //选择一条线路后地图进行绘制

                            }
                        });

                    }

                }

                break;
            case R.id.tvStartSite:
                if (siteBeans!=null && siteBeans.size()>0){
                    showStartPullList();
                }else {
                    Map<String ,String> params = new HashMap<>();
                    params = MD5Utils.encryptParams(params);
                    queryRoutePresenter.queryStartRoute(params);
                }

                break;
            case R.id.tvEndSite:
                String start1 = tvStartSite.getText().toString();
                if (start1.equals(getResources().getString(R.string.start_site))){
                    MyToast.showShort(getResources().getString(R.string.please_choose_the_starting_point));
                }else {

                    if (endsiteBeans!=null && endsiteBeans.size()>0){

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
                                    endLat = endsiteBeans.get(pos).getLat();
                                    endLng = endsiteBeans.get(pos).getLng();
                                    Map<String ,String> params = new HashMap<>();
                                    params.put("startPoint",startSiteId);
                                    params.put("endPoint",endSiteId);
                                    params = MD5Utils.encryptParams(params);
                                    queryRoutePresenter.queryRouteResult(params);

                                 }
                             });
                        }else {
                        MyToast.showLong("没有对应终点");
                    }

                }

                break;
        }
    }


    /**
     * 添加一个 标点
     * @param longitude  经度
     * @param latitude   纬度
     */
    private void addPoint(double longitude,double latitude){
        Map attributes = new HashMap();

//        Gson gson = new Gson();
//        String data = gson.toJson(siteInfoBean);
//
//        attributes.put(key, data);

        Point  mSourcePoint = new Point(longitude, latitude, SpatialReferences.getWgs84());
        Graphic pinSourceGraphic = new Graphic(mSourcePoint, attributes, pinSourceSymbolFindroute);
        mGraphicsOverlay.getGraphics().add(pinSourceGraphic);
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


    /**
     * 清除地图上所有标记
     */
    private void removeAllSymbol(){
        mGraphicsOverlay.getGraphics().clear();
    }


    @Override
    public void queryStartSucceed(QueryRuteBean queryRuteBean) {

        siteBeans = queryRuteBean.getRows();
        startlist = new ArrayList<>();

        for (QueryRuteBean.SiteBean siteBean : siteBeans) {
                String siteName = siteBean.getSiteName();
//                if (!TextUtils.isEmpty(siteName))
                        startlist.add(siteName);
        }

        showStartPullList();
    }

    //显示起点下拉列表
    private void showStartPullList() {
        PopwindowUtils.PullDownPopWindow(getActivity(), tvStartSite, startlist, new PopwindowUtils.OnClickNumberType() {
            @Override
            public void onNumberType(String context,int pos) { //点选某个起点后的回调
                tvStartSite.setText(context);
                //当点选了起点 把终点和选择路线重置
                tvEndSite.setText(getResources().getText(R.string.dest_site));
                tvSelectRoute.setText(getResources().getText(R.string.choose_the_route));
                endsiteBeans = null;
                queryRuteResult = null;

                //选择好起点后 根据起点，去请求对应的各个终点
                startSiteId = siteBeans.get(pos).getStart_site_id();
                startLat = siteBeans.get(pos).getLat();
                startLng = siteBeans.get(pos).getLng();

                Map<String ,String> params = new HashMap<>();
                params.put("startPoint",startSiteId);
                params = MD5Utils.encryptParams(params);
                queryRoutePresenter.queryEndRoute(params);
            }
        });
    }

    @Override
    public void queryEndSucceed(QueryRuteBean queryRuteBean) {
        //存储起点对应的终点列表 供点击终点时取出来展示
        endsiteBeans = queryRuteBean.getRows();
    }

    @Override
    public void queryRouteSucceed(QueryRuteResult queryRuteResult) {
        //储存结果线路
        //最终路线请求成功后 点击请选择路线时，把结果列表展示出来，根据选择某一条路线进行地图标注展示
        this.queryRuteResult = queryRuteResult;
    }
}

package com.esri.arcgisruntime.container.monitoring.ui.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.ScreenUtils;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.container.monitoring.DemoActivity;
import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.geometry.Geometry;
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
import com.esri.arcgisruntime.tasks.networkanalysis.DirectionManeuver;
import com.esri.arcgisruntime.tasks.networkanalysis.Route;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteParameters;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteResult;
import com.esri.arcgisruntime.tasks.networkanalysis.RouteTask;
import com.esri.arcgisruntime.tasks.networkanalysis.Stop;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import rx.functions.Action1;

/**
 * Created by libo on 2019/3/4.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 实时监控
 */
public class RealtimeMonitoringFragment extends Fragment {
    private static final String TAG = DemoActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private Button bt1;
    private Button bt2;
    private Button bt3;
    private Button bt4;
    private MapView mMapView;
    private RouteTask mRouteTask;
    private RouteParameters mRouteParams;
    private Point mSourcePoint;
    private Point mDestinationPoint;
    private Route mRoute;
    private SimpleLineSymbol mRouteSymbol;
    private GraphicsOverlay mGraphicsOverlay;
    private TextView tvScale;
    private View viewLine;
    private RelativeLayout rlRoot;
    FloatingActionButton mDirectionFab;
    LayoutInflater inflater;
    int initScale=200000;
    Graphic pinSourceGraphic;
    Graphic destinationGraphic;
    Graphic polylineGraphic;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater=getLayoutInflater();
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.progress_title));
        mProgressDialog.setMessage(getString(R.string.progress_message));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_realtime_monitoring_layout, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        setupSymbols();
        setListener();
        setViewTreeObserver();
    }

    private void initView(View view){

        rlRoot = view.findViewById(R.id.rlRoot);
        mDirectionFab = view.findViewById(R.id.directionFAB);
        mMapView = view.findViewById(R.id.mapView);
        tvScale =  view.findViewById(R.id.tvScale);
        viewLine =  view.findViewById(R.id.viewline);

        bt1=view.findViewById(R.id.bt1);
        bt2=view.findViewById(R.id.bt2);
        bt3=view.findViewById(R.id.bt3);
        bt4=view.findViewById(R.id.bt4);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mGraphicsOverlay.getGraphics().add(pinSourceGraphic);
                mGraphicsOverlay.getGraphics().remove(pinSourceGraphic);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mGraphicsOverlay.getGraphics().add(pinSourceGraphic);
                mGraphicsOverlay.getGraphics().remove(destinationGraphic);
            }
        });

        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mGraphicsOverlay.getGraphics().add(pinSourceGraphic);
                mGraphicsOverlay.getGraphics().remove(polylineGraphic);
            }
        });

        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mGraphicsOverlay.getGraphics().add(pinSourceGraphic);
                mMapView.getGraphicsOverlays().remove(mGraphicsOverlay);
            }
        });

    }

    private void initData(){
        // create new Vector Tiled Layer from service url
        ArcGISVectorTiledLayer mVectorTiledLayer = new ArcGISVectorTiledLayer(
                getResources().getString(R.string.navigation_vector));

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

        Map attributes = new HashMap();
        attributes.put("id","A");

        Map attributes2 = new HashMap();
        attributes2.put("id","B");

        //[DocRef: Name=Picture Marker Symbol Drawable-android, Category=Fundamentals, Topic=Symbols and Renderers]
        //Create a picture marker symbol from an app resource
        BitmapDrawable startDrawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.mipmap.location);
        final PictureMarkerSymbol pinSourceSymbol;
        try {
            pinSourceSymbol = PictureMarkerSymbol.createAsync(startDrawable).get();
            pinSourceSymbol.loadAsync();
            pinSourceSymbol.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    //add a new graphic as start point
//          mSourcePoint = new Point(-117.15083257944445, 32.741123367963446, SpatialReferences.getWgs84());
//          Graphic pinSourceGraphic = new Graphic(mSourcePoint, pinSourceSymbol);
//          mGraphicsOverlay.getGraphics().add(pinSourceGraphic);
                }
            });
            pinSourceSymbol.setOffsetY(20);

            mSourcePoint = new Point(-117.15083257944445, 32.741123367963446, SpatialReferences.getWgs84());
            pinSourceGraphic = new Graphic(mSourcePoint, attributes,pinSourceSymbol);
            mGraphicsOverlay.getGraphics().add(pinSourceGraphic);

            mDestinationPoint = new Point(-117.15557279683529, 32.703360305883045, SpatialReferences.getWgs84());
             destinationGraphic = new Graphic(mDestinationPoint, attributes2,pinSourceSymbol);
            mGraphicsOverlay.getGraphics().add(destinationGraphic);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //[DocRef: END]
        BitmapDrawable endDrawable = (BitmapDrawable) ContextCompat.getDrawable(getActivity(), R.drawable.ic_destination);
        final PictureMarkerSymbol pinDestinationSymbol;
        try {
            pinDestinationSymbol = PictureMarkerSymbol.createAsync(endDrawable).get();
            pinDestinationSymbol.loadAsync();
            pinDestinationSymbol.addDoneLoadingListener(new Runnable() {
                @Override
                public void run() {
                    //add a new graphic as end point
//          mDestinationPoint = new Point(-117.15557279683529, 32.703360305883045, SpatialReferences.getWgs84());
//          Graphic destinationGraphic = new Graphic(mDestinationPoint, pinDestinationSymbol);
//          mGraphicsOverlay.getGraphics().add(destinationGraphic);
                }
            });
            pinDestinationSymbol.setOffsetY(20);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //[DocRef: END]
        mRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 5);
    }

    private void setListener(){
        // update UI when attribution view changes
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mDirectionFab.getLayoutParams();
        mMapView.addAttributionViewLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(
                    View view, int left, int top, int right, int bottom,
                    int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int heightDelta = (bottom - oldBottom);
                params.bottomMargin += heightDelta;
            }
        });


        mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(getActivity(),mMapView){

            public boolean onSingleTapConfirmed(MotionEvent e) {
                final android.graphics.Point screenPoint = new android.graphics.Point((int) e.getX(), (int) e.getY());


                // identify graphics on the graphics overlay
                final ListenableFuture<IdentifyGraphicsOverlayResult> identifyGraphic =
                        mMapView.identifyGraphicsOverlayAsync(mGraphicsOverlay, screenPoint, 10.0, false, 2);

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
                                String id = graphics.get(0).getAttributes().get("id").toString();
//                                TextView calloutContent = new TextView(getActivity().getApplicationContext());
//                                calloutContent.setText("clickId== "+id);
                                Point mapPoint = mMapView.screenToLocation(screenPoint);

                                View view = inflater.inflate(R.layout.callout_layout,null);
                                TextView textView = view.findViewById(R.id.tv);
                                ImageView ivClose = view.findViewById(R.id.ivClose);
                                RelativeLayout rl = view.findViewById(R.id.rl);
                                ViewGroup.LayoutParams params =rl.getLayoutParams();
                                params.width=(int)( ScreenUtils.getScreenWidth(getActivity())*0.7);
                                rl.setLayoutParams(params);

                                if (id.equals("A")){
                                    textView.setText("id= "+id+"\n"
                                            +"aaaaaaaaaaa\n"
                                            +"aaaaaaaaaaa\n"
                                            +"aaaaaaaaaaa\n"
                                            +"aaaaaaaaaaa\n"
                                            +"aaaaaaaaaaa\n"
                                    );
                                }else {
                                    textView.setText("id= "+id+"\n"
                                            +"bbbbbbbbbbb\n"
                                            +"bbbbbbbbbbb\n"
                                            +"bbbbbbbbbbb\n"
                                            +"bbbbbbbbbbb\n"
                                            +"bbbbbbbbbbb\n"
                                    );
                                }

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


        mDirectionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                                PointCollection polylinePoints = new PointCollection(SpatialReferences.getWgs84());
//                                Point point1 = new Point(116.37494 , 39.877899);
//                                Point point2 = new Point(116.315889 , 39.991886);
                                Point point3 = new Point(116.374254 , 39.889227);
                                Point point4 = new Point(116.374254 , 39.894495);
                                Point point5 = new Point(116.374254 , 39.899763);
                                Point point6 = new Point(116.374254, 39.903977);
                                Point point7 = new Point(116.374254 , 39.906874);
                                Point point8 = new Point(116.38215 , 39.906874);
                                Point point9 = new Point(116.38627  , 39.906874);
                                Point point10 = new Point(116.39657 , 39.906874);
                                Point point11 = new Point(116.401719, 39.906874);
                                Point point12 = new Point(116.407899 , 39.906874);
                                Point point13 = new Point(116.413049 , 39.906874);
                                Point point14 = new Point(116.415796, 39.906874);
                                Point point15 = new Point(116.417512, 39.916355);

                                double weidu = 39.889227;

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

                                // create stops
                                Stop stop1 = new Stop(new Point(-117.15083257944445, 32.741123367963446, SpatialReferences.getWgs84()));
                                Stop stop2 = new Stop(new Point(-117.15557279683529, 32.703360305883045, SpatialReferences.getWgs84()));


                                List<Stop> routeStops = new ArrayList<>();
                                // add stops
                                routeStops.add(stop1);
                                routeStops.add(stop2);
                                mRouteParams.setStops(routeStops);

                                // set return directions as true to return turn-by-turn directions in the result of
                                // getDirectionManeuvers().
                                mRouteParams.setReturnDirections(true);

                                // solve
                                RouteResult result = mRouteTask.solveRouteAsync(mRouteParams).get();
                                final List routes = result.getRoutes();
                                mRoute = (Route) routes.get(0);
                                // create a mRouteSymbol graphic
                                Graphic routeGraphic = new Graphic(mRoute.getRouteGeometry(), mRouteSymbol);
                                // add mRouteSymbol graphic to the map
                                mGraphicsOverlay.getGraphics().add(routeGraphic);

                                // get directions
                                // NOTE: to get turn-by-turn directions Route Parameters should set returnDirection flag as true
                                final List<DirectionManeuver> directions = mRoute.getDirectionManeuvers();

                                String[] directionsArray = new String[directions.size()];

                                for (DirectionManeuver dm : directions) {
                                    directionsArray[i++] = dm.getDirectionText();
                                }

                                Log.d(TAG, directions.get(0).getGeometry().getExtent().getXMin() + "");
                                Log.d(TAG, directions.get(0).getGeometry().getExtent().getYMin() + "");


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


        });

    }


    //定位到当前位置
    private void getCurrentLocation() {

        if (Build.VERSION.SDK_INT >= 23) { //如果系统版本号大于等于23 也就是6.0，就必须动态请求敏感权限（也要配置清单）
            RxPermissions.getInstance(getActivity()).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean granted) {
                    if (granted) { //请求获取权限成功后的操作
                        LocationDisplay locationDisplay = mMapView.getLocationDisplay();
                        locationDisplay.setAutoPanMode(LocationDisplay.AutoPanMode.RECENTER );

                        //定位后不显示图标
                        locationDisplay.setShowLocation(false);//隐藏符号
                        locationDisplay.setShowAccuracy(false);//隐藏符号的缓存区域
                        locationDisplay.setShowPingAnimation(false);//隐藏位置更新的符号动画

                        locationDisplay.startAsync();

                        Point point=locationDisplay.getMapLocation();

                        Log.i("sss=",point.toString());
                    } else {
//                            MyToast.showShort("需要获取SD卡读取权限来保存图片");
                        Toast.makeText(getActivity(),"获取权限失败",Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else { //否则如果是6.0以下系统不需要动态申请权限直接配置清单就可以了

        }
    }

    //显示比例尺
    private void showScale(int initScale) {
        double scale=0;
        if (initScale==0){
            scale = mMapView.getMapScale()/100;
        }else {
            scale =(double)initScale/100;
        }

        String unit = "m";

        if(scale > 1000){

            unit = "km";
            scale = scale/1000;

        }

        BigDecimal bigDecimal = new BigDecimal(scale);
        scale = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        String result = scale+unit;


        tvScale.setText(result);
    }


    //view加载完成时回调
    public void setViewTreeObserver(){


        rlRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                float wid = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,10,getResources().getDisplayMetrics());
                ViewGroup.LayoutParams params =viewLine.getLayoutParams();
                params.width = (int)wid;
                viewLine.setLayoutParams(params);
                showScale(initScale);

                //只要界面一动就相当于在加载view ，只要加载完 那就会调用这个监听，所以会重复大量的调用，在执行完所需功能后注销掉监听
                rlRoot.getViewTreeObserver().removeGlobalOnLayoutListener(this);

            }
        });

    }


    public void setFABvisibilityStatus(boolean status){

        if (mDirectionFab!=null){
            if (status)
                mDirectionFab.setVisibility(View.VISIBLE);
            else
                mDirectionFab.setVisibility(View.GONE);
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

}
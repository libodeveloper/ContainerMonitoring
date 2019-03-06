/* Copyright 2017 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.esri.arcgisruntime.container.monitoring;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.ScreenUtils;
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.Point;
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

public class DemoActivity extends AppCompatActivity {

  private static final String TAG = DemoActivity.class.getSimpleName();
  private ProgressDialog mProgressDialog;

  private MapView mMapView;
  private RouteTask mRouteTask;
  private RouteParameters mRouteParams;
  private Point mSourcePoint;
  private Point mDestinationPoint;
  private Route mRoute;
  private SimpleLineSymbol mRouteSymbol;
  private GraphicsOverlay mGraphicsOverlay;

  private DrawerLayout mDrawerLayout;
  private ListView mDrawerList;
  private ActionBarDrawerToggle mDrawerToggle;
  private TextView tvScale;
  private View viewLine;
  DrawerLayout drawerLayout;
  LayoutInflater inflater;
  int initScale=200000;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.directions_drawer);
    // license with a license key
    ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud6021751119,none,MJJC7XLS1MJLTK118227");
    drawerLayout = findViewById(R.id.drawer_layout);
    inflater=getLayoutInflater();
    // inflate MapView from layout
    mMapView = (MapView) findViewById(R.id.mapView);
    tvScale =  findViewById(R.id.tvScale);
    viewLine =  findViewById(R.id.viewline);
    // create new Vector Tiled Layer from service url
    ArcGISVectorTiledLayer mVectorTiledLayer = new ArcGISVectorTiledLayer(
        getResources().getString(R.string.navigation_vector));

    // set tiled layer as basemap
    Basemap basemap = new Basemap(mVectorTiledLayer);
    // create a map with the basemap
    ArcGISMap mMap = new ArcGISMap(basemap);
    // create a viewpoint from lat, long, scale
    Viewpoint sanDiegoPoint = new Viewpoint(32.7157, -117.1611, initScale);
    // set initial map extent
    mMap.setInitialViewpoint(sanDiegoPoint);
    // set the map to be displayed in this view
    mMapView.setMap(mMap);
    mMapView.setAttributionTextVisible(false);
    // inflate navigation drawer
    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawerList = (ListView) findViewById(R.id.left_drawer);

    FloatingActionButton mDirectionFab = (FloatingActionButton) findViewById(R.id.directionFAB);

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

    setupDrawer();
    setupSymbols();
    setLinster();
    setViewTreeObserver();
    mProgressDialog = new ProgressDialog(this);
    mProgressDialog.setTitle(getString(R.string.progress_title));
    mProgressDialog.setMessage(getString(R.string.progress_message));

    mDirectionFab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        mProgressDialog.show();

        if (getSupportActionBar() != null) {
          getSupportActionBar().setDisplayHomeAsUpEnabled(true);
          getSupportActionBar().setHomeButtonEnabled(true);
          setTitle(getString(R.string.app_name));
        }
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        // create RouteTask instance
        mRouteTask = new RouteTask(getApplicationContext(), getString(R.string.routing_service));

        final ListenableFuture<RouteParameters> listenableFuture = mRouteTask.createDefaultParametersAsync();
        listenableFuture.addDoneListener(new Runnable() {
          @Override
          public void run() {
            try {
              if (listenableFuture.isDone()) {
                int i = 0;
                mRouteParams = listenableFuture.get();

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

                // Set the adapter for the list view
                mDrawerList.setAdapter(new ArrayAdapter<>(getApplicationContext(),
                    R.layout.directions_layout, directionsArray));

                if (mProgressDialog.isShowing()) {
                  mProgressDialog.dismiss();
                }
                mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mGraphicsOverlay.getGraphics().size() > 3) {
                      mGraphicsOverlay.getGraphics().remove(mGraphicsOverlay.getGraphics().size() - 1);
                    }

                    mDrawerLayout.closeDrawers();
                    DirectionManeuver dm = directions.get(position);
                    Geometry gm = dm.getGeometry();
                    Viewpoint vp = new Viewpoint(gm.getExtent(), 20);
                    mMapView.setViewpointAsync(vp, 1);
                    SimpleLineSymbol selectedRouteSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID,
                        Color.GREEN, 5);
                    Graphic selectedRouteGraphic = new Graphic(directions.get(position).getGeometry(),
                        selectedRouteSymbol);
                    mGraphicsOverlay.getGraphics().add(selectedRouteGraphic);


                  }
                });

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
      RxPermissions.getInstance(DemoActivity.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
            Toast.makeText(DemoActivity.this,"获取权限失败",Toast.LENGTH_LONG).show();
          }
        }
      });
    } else { //否则如果是6.0以下系统不需要动态申请权限直接配置清单就可以了

    }
  }

  private void setLinster(){

    mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(this,mMapView){

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
                TextView calloutContent = new TextView(getApplicationContext());
                calloutContent.setText("clickId== "+id);
                Point mapPoint = mMapView.screenToLocation(screenPoint);

                View view = inflater.inflate(R.layout.callout_layout,null);
                TextView textView = view.findViewById(R.id.tv);
                ImageView ivClose = view.findViewById(R.id.ivClose);
                RelativeLayout rl = view.findViewById(R.id.rl);
                ViewGroup.LayoutParams params =rl.getLayoutParams();
                params.width=(int)( ScreenUtils.getScreenWidth(DemoActivity.this)*0.7);
                rl.setLayoutParams(params);

                if (id.equals("A")){
                  textView.setText("id= "+id+"\n"
                          +"咦... 小薇薇在看我...");
                }else {
                  textView.setText("id= "+id+"\n"
                          +"嘿...小女孩还在看我...哈哈哈哈....");
                }

                ivClose.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                    mCallout.dismiss();
                  }
                });

                mCallout.setLocation(mapPoint);
                mCallout.setContent(view);
                Callout.Style style = new Callout.Style(DemoActivity.this);
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

  /**
   * Set up the Source, Destination and mRouteSymbol graphics symbol
   */
  private void setupSymbols() {

    // set stop locations
//    SpatialReference ESPG_3857 = SpatialReference.create(102100);
//    Point stop1Loc = new Point(116.415618, 39.932803, ESPG_3857);
//    Point stop2Loc = new Point(116.373733, 39.932276, ESPG_3857);

    mGraphicsOverlay = new GraphicsOverlay();

    //add the overlay to the map view
    mMapView.getGraphicsOverlays().add(mGraphicsOverlay);


    Map attributes = new HashMap();
    attributes.put("id","A");

    Map attributes2 = new HashMap();
    attributes2.put("id","B");

    //[DocRef: Name=Picture Marker Symbol Drawable-android, Category=Fundamentals, Topic=Symbols and Renderers]
    //Create a picture marker symbol from an app resource
    BitmapDrawable startDrawable = (BitmapDrawable) ContextCompat.getDrawable(this, R.mipmap.location);
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
      Graphic pinSourceGraphic = new Graphic(mSourcePoint, attributes,pinSourceSymbol);
      mGraphicsOverlay.getGraphics().add(pinSourceGraphic);

      mDestinationPoint = new Point(-117.15557279683529, 32.703360305883045, SpatialReferences.getWgs84());
      Graphic destinationGraphic = new Graphic(mDestinationPoint, attributes2,pinSourceSymbol);
      mGraphicsOverlay.getGraphics().add(destinationGraphic);

    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    //[DocRef: END]
    BitmapDrawable endDrawable = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.ic_destination);
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

  @Override
  protected void onPause() {
    super.onPause();
    mMapView.pause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    mMapView.resume();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mMapView.dispose();
  }

  /**
   * set up the drawer
   */
  private void setupDrawer() {
    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

      /** Called when a drawer has settled in a completely open state. */
      public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }

      /** Called when a drawer has settled in a completely closed state. */
      public void onDrawerClosed(View view) {
        super.onDrawerClosed(view);
        invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
      }
    };

    mDrawerToggle.setDrawerIndicatorEnabled(true);
    mDrawerLayout.addDrawerListener(mDrawerToggle);

    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // Sync the toggle state after onRestoreInstanceState has occurred.
    mDrawerToggle.syncState();
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    super.onConfigurationChanged(newConfig);

    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.

    // Activate the navigation drawer toggle
    return (mDrawerToggle.onOptionsItemSelected(item)) || super.onOptionsItemSelected(item);
  }


//view加载完成时回调
    public void setViewTreeObserver(){


    drawerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
    @Override
    public void onGlobalLayout() {

          float wid = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,10,getResources().getDisplayMetrics());
          ViewGroup.LayoutParams params =viewLine.getLayoutParams();
          params.width = (int)wid;
          viewLine.setLayoutParams(params);
          showScale(initScale);

          //只要界面一动就相当于在加载view ，只要加载完 那就会调用这个监听，所以会重复大量的调用，在执行完所需功能后注销掉监听
          drawerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

        }
      });

    }


}

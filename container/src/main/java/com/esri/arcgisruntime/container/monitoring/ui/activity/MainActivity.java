package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.adapter.FmTabPagerAdapter;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.bean.User;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.http.ResponseJson;
import com.esri.arcgisruntime.container.monitoring.ui.fragment.BillQueryFragment;
import com.esri.arcgisruntime.container.monitoring.ui.fragment.BusinessQueryFragment;
import com.esri.arcgisruntime.container.monitoring.ui.fragment.PersonalFragment;
import com.esri.arcgisruntime.container.monitoring.ui.fragment.QueryRouteFragment;
import com.esri.arcgisruntime.container.monitoring.ui.fragment.RealtimeMonitoringFragment;
import com.esri.arcgisruntime.container.monitoring.utils.LogUtil;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;
import com.esri.arcgisruntime.container.monitoring.view.SuperViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by libo on 2019/3/4.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.viewPager)
    SuperViewPager viewPager;
    @BindView(R.id.main_radio)
    RadioGroup radioGroup;
    @BindView(R.id.viewbg)
    View viewbg;

    private RealtimeMonitoringFragment realtimeMonitoringFragment;
    private QueryRouteFragment queryRouteFragment;
    private BillQueryFragment billQueryFragment;
    private BusinessQueryFragment businessQueryFragment;
    private PersonalFragment personalFragment;

    private ArrayList<Fragment> fragments;
    FmTabPagerAdapter fmTabPagerAdapter;

    private int currIndex;

    private long startTime;
    private long endTime;
    private int total;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_layout);
        ButterKnife.bind(this);
        getPermissions();
        // license with a license key
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud6021751119,none,MJJC7XLS1MJLTK118227");

    }

    @Override
    protected void initData() {
        initFragment();
        setListener();
        radioGroup.check(R.id.rbRealtimeMonitoring);

    }


    private void initFragment() {
        fragments = new ArrayList<>();
        realtimeMonitoringFragment = new RealtimeMonitoringFragment();
        queryRouteFragment = new QueryRouteFragment();
        billQueryFragment = new BillQueryFragment();
        businessQueryFragment = new BusinessQueryFragment();
        personalFragment = new PersonalFragment();

        fragments.add(realtimeMonitoringFragment);
        fragments.add(queryRouteFragment);
        fragments.add(billQueryFragment);
        fragments.add(businessQueryFragment);
        fragments.add(personalFragment);

        fmTabPagerAdapter = new FmTabPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fmTabPagerAdapter);

        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(5); //缓冲好4个来回切换时不必再重复创建
    }

    private void setListener() {

        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDubug();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbRealtimeMonitoring:   //实时监控
                        tvTitle.setText(getResources().getText(R.string.realtime_monitoring));
                        changeChildFragment(0);
                        break;
                    case R.id.rbFindRoute:  //路线查询
                        tvTitle.setText(getResources().getText(R.string.find_route));
                        changeChildFragment(1);
                        break;
                    case R.id.rbBillQuery:  //单据查询
                        tvTitle.setText(getResources().getText(R.string.bill_query));
                        changeChildFragment(2);
                        break;
                    case R.id.rbBusinessQuery:  //业务查询
                        tvTitle.setText(getResources().getText(R.string.business_query));
                        changeChildFragment(3);
                        break;
                    case R.id.rbPersonal: //个人资料
                        tvTitle.setText(getResources().getText(R.string.personal));
                        changeChildFragment(4);
                        break;
                }
            }
        });
    }

    /**
     * 切换子fragment
     *
     * @param position
     */
    public void changeChildFragment(int position) {
        currIndex = position;
        viewPager.setCurrentItem(position, true); //true 切换动画必须开启否则两页fragment地图无法正常切换
    }


    private void getPermissions() {

        if (Build.VERSION.SDK_INT >= 23) { //如果系统版本号大于等于23 也就是6.0，就必须动态请求敏感权限（也要配置清单）
            RxPermissions.getInstance(this).request(Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean granted) {
                    if (granted) { //请求获取权限成功后的操作

                        Log.e("sss", "获取权限成功");
                    } else {
//                            MyToast.showShort("需要获取SD卡读取权限来保存图片");
                        Toast.makeText(MainActivity.this, getResources().getText(R.string.permission_failed), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else { //否则如果是6.0以下系统不需要动态申请权限直接配置清单就可以了

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState); 注掉简单解决fragment重叠问题
    }

    // 用来计算返回键的点击间隔时间
    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                //弹出提示，可以有多种方式
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public View getViewbg(){
        return viewbg;
    }

    /**
     * Created by 李波 on 2018/11/21.
     * 开启关闭清除方案的dubug模式
     */
    private void changeDubug(){
        if(total>=15)
            return;
        startTime = endTime;
        endTime = System.currentTimeMillis();
        if(endTime-startTime>1000){//证明其中有一次不够连续，重新来过
            LogUtil.e("times","断开了.......");
            total = 0;
            return;
        }
        total++;

        if (total>10)
            MyToast.showShort("连续点击 "+total+" 次");

        if(total==15){
            total = 0;
            Constants.dubug =!Constants.dubug;

            if (Constants.dubug)
                MyToast.showShort("当前为Dubug模式");
            else
                MyToast.showShort("当前为正式模式");
        }
    }

}

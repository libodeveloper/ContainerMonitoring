package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.ui.fragment.BillQueryFragment;
import com.esri.arcgisruntime.container.monitoring.ui.fragment.BusinessQueryFragment;
import com.esri.arcgisruntime.container.monitoring.ui.fragment.PersonalFragment;
import com.esri.arcgisruntime.container.monitoring.ui.fragment.RealtimeMonitoringFragment;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.mapping.view.LocationDisplay;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * Created by libo on 2019/3/4.
 */

public class MainActivity extends FragmentActivity {

    private RadioGroup radioGroup;
    private TextView tvTitle;
    private SparseArray<Fragment> mfragArray;
    private RealtimeMonitoringFragment realtimeMonitoringFragment;
    private BillQueryFragment billQueryFragment;
    private BusinessQueryFragment businessQueryFragment;
    private PersonalFragment personalFragment;
    private int currIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        getPermissions();
        radioGroup = findViewById(R.id.main_radio);
        tvTitle = findViewById(R.id.tvTitle);
        // license with a license key
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud6021751119,none,MJJC7XLS1MJLTK118227");
        initFragment();
        setListener();

        radioGroup.check(R.id.rbRealtimeMonitoring);
    }



    private void initFragment(){
        mfragArray = new SparseArray<Fragment>();
        realtimeMonitoringFragment = new RealtimeMonitoringFragment();
        billQueryFragment = new BillQueryFragment();
        businessQueryFragment = new BusinessQueryFragment();
        personalFragment = new PersonalFragment();

        mfragArray.put(0, realtimeMonitoringFragment);
        mfragArray.put(1, billQueryFragment);
        mfragArray.put(2, businessQueryFragment);
        mfragArray.put(3, personalFragment);

        addFragmentToStack(realtimeMonitoringFragment);
        addFragmentToStack(billQueryFragment);
        addFragmentToStack(businessQueryFragment);
        addFragmentToStack(personalFragment);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.show(realtimeMonitoringFragment);
        fragmentTransaction.hide(billQueryFragment);
        fragmentTransaction.hide(businessQueryFragment);
        fragmentTransaction.hide(personalFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }


    private void addFragmentToStack(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.framlayout, fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }


    private void setListener(){

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbRealtimeMonitoring:   //实时监控
                        tvTitle.setText(getResources().getText(R.string.realtime_monitoring));
                        realtimeMonitoringFragment.setFABvisibilityStatus(false);
                        changeChildFragment(0);
                        break;
                    case R.id.rbFindRoute:  //路线查询
                        tvTitle.setText(getResources().getText(R.string.find_route));
                        realtimeMonitoringFragment.setFABvisibilityStatus(true);
                        changeChildFragment(0);
                        break;
                    case R.id.rbBillQuery:  //单据查询
                        tvTitle.setText(getResources().getText(R.string.bill_query));
                        changeChildFragment(1);
                        break;
                    case R.id.rbBusinessQuery:  //业务查询
                        tvTitle.setText(getResources().getText(R.string.business_query));
                        changeChildFragment(2);
                        break;
                    case R.id.rbPersonal: //个人资料
                        tvTitle.setText(getResources().getText(R.string.personal));
                        changeChildFragment(3);
                        break;
                }
            }
        });
    }

    /**
     * 切换子fragment
     * @param position
     */
    public void changeChildFragment(int position) {
        currIndex = position;
        switchFragment(position);
    }

    /**
     * 切换fragment
     * @param
     * @param flag 对应索引
     */
    private void switchFragment(int flag) {
        showTab(flag);
    }

    private void showTab(int idx) {
        for (int i = 0; i < mfragArray.size(); i++) {
            Fragment fragment = mfragArray.get(i);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (idx == i) {
                fragmentTransaction.show(fragment);
            } else {
                fragmentTransaction.hide(fragment);
            }
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void getPermissions() {

        if (Build.VERSION.SDK_INT >= 23) { //如果系统版本号大于等于23 也就是6.0，就必须动态请求敏感权限（也要配置清单）
            RxPermissions.getInstance(this).request(Manifest.permission.INTERNET,
                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                                    Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Action1<Boolean>() {
                @Override
                public void call(Boolean granted) {
                    if (granted) { //请求获取权限成功后的操作

                        Log.e("sss","获取权限成功");
                    } else {
//                            MyToast.showShort("需要获取SD卡读取权限来保存图片");
                        Toast.makeText(MainActivity.this,"获取权限失败",Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else { //否则如果是6.0以下系统不需要动态申请权限直接配置清单就可以了

        }
    }

}

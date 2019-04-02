package com.esri.arcgisruntime.container.monitoring;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.esri.arcgisruntime.container.monitoring.adapter.FmTabPagerAdapter;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.ui.fragment.PersonalFragment;
import com.esri.arcgisruntime.container.monitoring.ui.fragment.QueryRouteFragment;
import com.esri.arcgisruntime.container.monitoring.ui.fragment.RealtimeMonitoringFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/4/2.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class DemoViewPagerActivity extends BaseActivity{

    @BindView(R.id.viewPager)
    SuperViewPager viewPager;
    private ArrayList<Fragment> fragments;
    FmTabPagerAdapter fmTabPagerAdapter;
    private RealtimeMonitoringFragment realtimeMonitoringFragment;
    private QueryRouteFragment queryRouteFragment;
    private PersonalFragment personalFragment;


    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_test_viewpager);
        ButterKnife.bind(this);

        fragments = new ArrayList<>();
        realtimeMonitoringFragment = new RealtimeMonitoringFragment();
        queryRouteFragment = new QueryRouteFragment();
        personalFragment = new PersonalFragment();

        fragments.add(realtimeMonitoringFragment);
        fragments.add(queryRouteFragment);
        fragments.add(personalFragment);

        fmTabPagerAdapter = new FmTabPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fmTabPagerAdapter);

        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3); //缓冲好4个来回切换时不必再重复创建
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.bt111, R.id.bt222,R.id.bt333})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt111:
                viewPager.setCurrentItem(0,true);
                break;
            case R.id.bt222:
                viewPager.setCurrentItem(1,true);
                break;
            case R.id.bt333:
                viewPager.setCurrentItem(2,true);
                break;
        }
    }


}

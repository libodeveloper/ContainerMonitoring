package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.application.CMApplication;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.bean.User;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.presenter.LoginPresenter;
import com.esri.arcgisruntime.container.monitoring.utils.ACache;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.ILogin;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by libo on 2019/3/10.
 */

public class SplashActivity extends BaseActivity implements ILogin {

    private Subscription subscribe;
    LoginPresenter loginPresenter;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void initData() {
//        getPermissions();
        loginPresenter = new LoginPresenter(this);
        wait2s();
    }

    //如等待 2秒 执行
    private void wait2s(){
        subscribe = Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        //等待 2s 后执行的事件
                        User user = CMApplication.getUser();
                        if (user!=null && !TextUtils.isEmpty(user.getAccount()) && !TextUtils.isEmpty(user.getPassword())){
                            //user不等于空 有缓存 自动登录
                            autoLogin();
                        }else {
                            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                            finish();
                        }

                    }
                });
    }

    private void autoLogin() {

        loginPresenter.login(getParams());

    }

    private Map<String, String> getParams() {
        User user = CMApplication.getUser();
        String useName = user.getAccount();
        String password = user.getPassword();

        Map<String, String> params = new HashMap<>();
        params.put("userName",useName);
        params.put("password",password);
        return params;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscribe!=null)
            subscribe.unsubscribe();
    }

    private void getPermissions() {

        if (Build.VERSION.SDK_INT >= 23) { //如果系统版本号大于等于23 也就是6.0，就必须动态请求敏感权限（也要配置清单）
            RxPermissions.getInstance(this).request(
                    Manifest.permission.INTERNET,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Action1<Boolean>() {

                @Override
                public void call(Boolean granted) {
                    if (granted) { //请求获取权限成功后的操作

                        Log.e("sss","获取权限成功");
                    } else {
//                            MyToast.showShort("需要获取SD卡读取权限来保存图片");
                        Toast.makeText(SplashActivity.this,"获取权限失败",Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else { //否则如果是6.0以下系统不需要动态申请权限直接配置清单就可以了

        }
    }


    @Override
    public void Succeed(User user) {

        user.setPassword(CMApplication.getUser().getPassword());

        if (!TextUtils.isEmpty(user.getKey()))
                MD5Utils.PRIVATE_KEY = user.getKey();
        ACache.get(this).put(Constants.KEY_ACACHE_USER,user);

        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void Failed() {
        //如果自动登录出现失败的情况跳转到 登录界面
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
}

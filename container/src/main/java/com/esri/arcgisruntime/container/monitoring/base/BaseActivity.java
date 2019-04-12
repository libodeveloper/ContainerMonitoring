package com.esri.arcgisruntime.container.monitoring.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.esri.arcgisruntime.container.monitoring.application.AppManager;
import com.esri.arcgisruntime.container.monitoring.dialog.ShowDialogTool;
import com.esri.arcgisruntime.container.monitoring.utils.LocalManageUtil;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;
import com.umeng.analytics.MobclickAgent;


public abstract class BaseActivity extends FragmentActivity implements IBaseView {
    protected String TAG = getClass().getName();
    private ShowDialogTool showDialogTool;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocalManageUtil.setLocal(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        showDialogTool = new ShowDialogTool();
        initViews(savedInstanceState);
        initData();
    }

    /**
     * 初始化布局和控件
     */
    protected abstract void initViews(Bundle savedInstanceState);

    /**
     * 设置相关数据
     */
    protected abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    /**
     * 显示dialog
     */
    public void showDialog() {
        if (!isFinishing())
        showDialogTool.showLoadingDialog(this);

    }

    /**
     * 显示dialog
     */
    public void showDialog(String msg) {
        if (!isFinishing())
        showDialogTool.showLoadingDialog(this, msg);
    }

    /**
     * 关闭dialog
     */
    public void dismissDialog() {
        showDialogTool.dismissLoadingDialog();
    }

    /**
     * 显示错误信息
     *
     * @param error 错误信息
     */
    public void showError(String error) {
        if (!TextUtils.isEmpty(error))
            MyToast.showLong(error);
    }


    /**
     * 不带Extra跳转activity
     *
     * @param clazz
     * @param needFinish 是否finish当前activity
     */
    protected void jump(Class<?> clazz, boolean needFinish) {
        Intent intent = new Intent(this, clazz);
        jump(intent, needFinish);
    }

    /**
     * 带Extra跳转activity
     *
     * @param intent
     * @param needFinish 是否finish当前activity
     */
    protected void jump(Intent intent, boolean needFinish) {
        startActivity(intent);
        if (needFinish) {
            finish();
        }
    }
}

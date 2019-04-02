package com.esri.arcgisruntime.container.monitoring.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.arcgisruntime.container.monitoring.dialog.ShowDialogTool;
import com.esri.arcgisruntime.container.monitoring.ui.activity.MainActivity;
import com.esri.arcgisruntime.container.monitoring.utils.LogUtil;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;


public abstract class BaseFragment extends Fragment implements IBaseView{
    protected String TAG = BaseFragment.this.getClass().getSimpleName();
    protected MainActivity mainActivity;
    private ShowDialogTool showDialogTool;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showDialogTool = new ShowDialogTool();
        initData();
    }

    public void onResume() {
        super.onResume();
    }
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = initViews(inflater, container, savedInstanceState);
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();
    }

    /***
     * run in onActivityCreated
     */
    protected abstract void setView();

    /***
     * run in onCreateView
     */
    protected abstract View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    /***
     * run in onCreate
     */
    protected abstract void initData();

    protected void jump(Class dest) {
        Intent intent = new Intent(mainActivity, dest);
        startActivity(intent);
    }

    protected void jump(Intent intent) {
        startActivity(intent);
    }



    /**
     * 显示错误信息
     *
     * @param error 错误信息
     */
    @Override
    public void showError(String error) {
        LogUtil.e(TAG,error);
        if (!TextUtils.isEmpty(error))
            MyToast.showLong(error);
    }

    /**
     * 显示加载
     */
    @Override
    public void showDialog() {
        showDialogTool.showLoadingDialog(mainActivity);
    }

    /**
     * 显示dialog
     */
    public void showDialog(String msg) {
        if (!mainActivity.isFinishing())
            showDialogTool.showLoadingDialog(mainActivity, msg);
    }

    /**
     * 关闭加载
     */
    @Override
    public void dismissDialog() {
        showDialogTool.dismissLoadingDialog();
    }


}

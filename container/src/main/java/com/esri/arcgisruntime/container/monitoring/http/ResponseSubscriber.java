package com.esri.arcgisruntime.container.monitoring.http;

import android.content.Intent;
import android.text.TextUtils;


import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.application.AppManager;
import com.esri.arcgisruntime.container.monitoring.application.CMApplication;
import com.esri.arcgisruntime.container.monitoring.base.IBaseView;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.ui.activity.LoginActivity;
import com.esri.arcgisruntime.container.monitoring.utils.LogUtil;
import com.esri.arcgisruntime.container.monitoring.utils.NetWorkTool;

import java.io.IOException;

import okhttp3.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by 李波 on 2017/3/24.
 * Rxjava 观察者基类
 */
public abstract class ResponseSubscriber<T extends ResponseJson> extends Subscriber<T> {
    private IBaseView view;
    private boolean showLoading;
    private String loadingText;

    /**
     * Created by 李波 on 2017/3/24.
     * 默认显示dialog
     */
    public ResponseSubscriber(IBaseView view) {
        this.view = view;
        this.showLoading = true;
    }

    public ResponseSubscriber(IBaseView view, boolean showLoading) {
        this(view);
        this.showLoading = showLoading;
    }

    public ResponseSubscriber(IBaseView view, boolean showLoading, String loadingText) {
        this(view, showLoading);
        this.loadingText = loadingText;
    }

    @Override
    public void onCompleted() {
        if (showLoading)
            view.dismissDialog();
    }

    /**
     * Created by 李波 on 2017/3/24.
     * 请求异常通用提示，如果需特殊处理，覆写此方法即可
     */
    @Override
    public void onError(Throwable e) {
        view.dismissDialog();
        String error;
        if (e instanceof ResponseErrorException) {
            error = e.getMessage();
        }/* else if (e instanceof IOException) {
            error = "请检查您的网络后重试";
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            error = httpException.getMessage();
        }*/else {
            error = CMApplication.getAppContext().getResources().getText(R.string.request_server_failed).toString();
            e.printStackTrace();
            LogUtil.e("RequestFailedAction", e.getMessage());
        }

        if (showLoading)
            view.showError(error);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (showLoading) {

//            if (!TextUtils.isEmpty(loadingText)) {
//                view.showDialog(loadingText);
//            } else {
//                view.showLoading();
//            }
            view.showDialog();
        }
    }

    @Override
    public void onNext(T t) {
        int status = t.getStatus();
        if (status == Constants.SUCCESS_STATUS_CODE) {
            onSuccess(t);

            //账号密码错误
        }else if(status == Constants.SUCCESS_STATUS_CODE_302){
            throw new ResponseErrorException(CMApplication.getAppContext().getResources().getText(R.string.wrong_account_or_password).toString());

            //请求服务器失败 错误码 2 开头的需要跳转到登录页面重新登录
        }else if(status == Constants.SUCCESS_STATUS_CODE_201 || status == Constants.SUCCESS_STATUS_CODE_202 || status == Constants.SUCCESS_STATUS_CODE_203
                || status == Constants.SUCCESS_STATUS_CODE_204 ){

            Intent intent = new Intent(AppManager.getAppManager().currentActivity(), LoginActivity.class);
            AppManager.getAppManager().currentActivity().startActivity(intent);
            AppManager.getAppManager().finishAllActivity(LoginActivity.class);

            //请求服务器失败
        }else if(status == Constants.SUCCESS_STATUS_CODE_201 || status == Constants.SUCCESS_STATUS_CODE_202 || status == Constants.SUCCESS_STATUS_CODE_203
                || status == Constants.SUCCESS_STATUS_CODE_204 || status == Constants.SUCCESS_STATUS_CODE_301 || status == Constants.SUCCESS_STATUS_CODE_303 ){
            throw new ResponseErrorException(CMApplication.getAppContext().getResources().getText(R.string.request_server_failed).toString());

            //暂无可用数据
        }else if(status == Constants.SUCCESS_STATUS_CODE_602 || status == Constants.SUCCESS_STATUS_CODE_610 || status == Constants.SUCCESS_STATUS_CODE_612
                || status == Constants.SUCCESS_STATUS_CODE_613 || status == Constants.SUCCESS_STATUS_CODE_614 || status == Constants.SUCCESS_STATUS_CODE_615
                || status == Constants.SUCCESS_STATUS_CODE_616 || status == Constants.SUCCESS_STATUS_CODE_617 || status == Constants.SUCCESS_STATUS_CODE_618
                || status == Constants.SUCCESS_STATUS_CODE_619 || status == Constants.SUCCESS_STATUS_CODE_620){
            throw new ResponseErrorException(CMApplication.getAppContext().getResources().getText(R.string.no_data_available_yet).toString());

            // 未查询到单据详情信息
        } else if(status == Constants.SUCCESS_STATUS_CODE_604){
            throw new ResponseErrorException(CMApplication.getAppContext().getResources().getText(R.string.document_details_not_found).toString());

            //查询的编号不存在
        }else if(status == Constants.SUCCESS_STATUS_CODE_605 || status == Constants.SUCCESS_STATUS_CODE_606 || status == Constants.SUCCESS_STATUS_CODE_611 ){
            throw new ResponseErrorException(CMApplication.getAppContext().getResources().getText(R.string.the_number_of_the_query_does_not_exist).toString());

            //起点查询失败
        }else if(status == Constants.SUCCESS_STATUS_CODE_607){
            throw new ResponseErrorException(CMApplication.getAppContext().getResources().getText(R.string.no_start_point_location_info).toString());

            //终点查询失败
        }else if(status == Constants.SUCCESS_STATUS_CODE_608){
            throw new ResponseErrorException(CMApplication.getAppContext().getResources().getText(R.string.no_end_point).toString());

            //未查询到指定路线
        }else if(status == Constants.SUCCESS_STATUS_CODE_609){
//            throw new ResponseErrorException(CMApplication.getAppContext().getResources().getText(R.string.the_specified_route_was_not_queried).toString());
            throw new ResponseErrorException(CMApplication.getAppContext().getResources().getText(R.string.no_valid_path_data).toString());
        }

        //实时监控详情数据缺失
        else if(status == Constants.SUCCESS_STATUS_CODE_621){
            throw new ResponseErrorException(CMApplication.getAppContext().getResources().getText(R.string.missing_real_time_monitoring_detail_data).toString());
        }else {
            throw new ResponseErrorException(CMApplication.getAppContext().getResources().getText(R.string.request_server_failed).toString());
        }
    }

    /**
     * Created by 李波 on 2017/3/24.
     * 请求成功的处理
     */
    public abstract void onSuccess(T response);
}

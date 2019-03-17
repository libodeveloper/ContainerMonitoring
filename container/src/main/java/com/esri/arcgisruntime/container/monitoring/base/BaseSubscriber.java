package com.esri.arcgisruntime.container.monitoring.base;

import android.text.TextUtils;

import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.http.ResponseErrorException;
import com.esri.arcgisruntime.container.monitoring.http.ResponseJson;
import com.esri.arcgisruntime.container.monitoring.utils.LogUtil;
import com.esri.arcgisruntime.container.monitoring.utils.NetWorkTool;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;


public abstract  class BaseSubscriber<T extends ResponseJson> extends Subscriber<T> {
    private static final String TAG = BaseSubscriber.class.getSimpleName();
    private IBaseView baseView;

    public BaseSubscriber(IBaseView baseView) {
        this.baseView = baseView;
    }
    @Override
    public void onCompleted() {
        if(baseView!=null){
            baseView.dismissDialog();
        }
    }

    @Override
    public void onError(Throwable e) {
        if(baseView!=null){
            baseView.dismissDialog();
        }
        String error = "";
        if(e instanceof ResponseErrorException){
            error = e.getMessage();
        }else if(e instanceof IOException){
            error = "请检查您的网络后重试";
        }else if(e instanceof HttpException){
            HttpException httpException = (HttpException)e;
            error =httpException.getMessage();
        }else{
            if (!NetWorkTool.isConnect())
                error = "没有网络";
            e.printStackTrace();
            LogUtil.e("RequestFailedAction",e.getMessage());
        }
        if (TextUtils.isEmpty(error))
            error = "服务器响应异常，请稍后重试";
        baseView.showError(error);
    }

    @Override
    public void onNext(T t) {
        int status = t.getStatus();
        String msg = t.getMsg();
       if(status== Constants.SUCCESS_STATUS_CODE){
                onSuccess(t);
        }else{
            throw new ResponseErrorException(msg);
        }
    }

    public abstract void onSuccess(T response);
}

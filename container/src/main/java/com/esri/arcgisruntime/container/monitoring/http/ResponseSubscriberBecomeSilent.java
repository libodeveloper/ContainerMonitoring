package com.esri.arcgisruntime.container.monitoring.http;

import com.esri.arcgisruntime.container.monitoring.base.IBaseView;
import com.esri.arcgisruntime.container.monitoring.global.Constants;

import rx.Subscriber;

/**
 * Created by 李波 on 2017/3/24.
 * Rxjava 观察者基类 - 静默 不显示弹窗也不显示Toast
 */
public abstract class ResponseSubscriberBecomeSilent<T extends ResponseJson> extends Subscriber<T> {
    private IBaseView view;

    /**
     * Created by 李波 on 2017/3/24.
     * 默认显示dialog
     */
    public ResponseSubscriberBecomeSilent(IBaseView view) {
        this.view = view;
    }


    @Override
    public void onCompleted() {
    }

    /**
     * Created by 李波 on 2017/3/24.
     * 请求异常通用提示，如果需特殊处理，覆写此方法即可
     */
    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onNext(T t) {
        int status = t.getStatus();
        if (status == Constants.SUCCESS_STATUS_CODE) {
            onSuccess(t);
        }else {
            throw new ResponseErrorException(t.getMsg());
        }
    }

    /**
     * Created by 李波 on 2017/3/24.
     * 请求成功的处理
     */
    public abstract void onSuccess(T response);
}

package com.esri.arcgisruntime.container.monitoring.base;

public abstract class BasePresenter<T> {
    protected String TAG = getClass().getName();
    protected T baseView;

    public BasePresenter(T from) {
        this.baseView = from;
    }
}

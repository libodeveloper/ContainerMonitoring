package com.esri.arcgisruntime.container.monitoring.presenter;

import com.esri.arcgisruntime.container.monitoring.base.BasePresenter;
import com.esri.arcgisruntime.container.monitoring.bean.RealtimeMonitorBean;
import com.esri.arcgisruntime.container.monitoring.http.ApiManager;
import com.esri.arcgisruntime.container.monitoring.http.ResponseJson;
import com.esri.arcgisruntime.container.monitoring.http.ResponseSubscriber;
import com.esri.arcgisruntime.container.monitoring.http.RxThreadUtil;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IRealtimeMonitoring;

import java.util.Map;

/**
 * 实时监控
 */
public class RealtimeMonitorPresenter extends BasePresenter<IRealtimeMonitoring> {
    public RealtimeMonitorPresenter(IRealtimeMonitoring from) {
        super(from);
    }

    //获取实时监控全部数据
    public void realtimeMonitorResult(Map<String,String> params){

        ApiManager.getApiServer().realtimeMonitorResult(params)
                .compose(RxThreadUtil.<ResponseJson<RealtimeMonitorBean>>networkSchedulers())
                .subscribe(new ResponseSubscriber<ResponseJson<RealtimeMonitorBean>>(baseView) {
                    @Override
                    public void onSuccess(ResponseJson<RealtimeMonitorBean> response) {
                        RealtimeMonitorBean realtimeMonitorBean = response.getData();
                        baseView.rmResult(realtimeMonitorBean);
                    }
                });
    }

    //获取实时监控数据
    public void realtimeMonitorSingleResult(Map<String,String> params){

        ApiManager.getApiServer().realtimeMonitorSingleResult(params)
                .compose(RxThreadUtil.<ResponseJson<RealtimeMonitorBean.RowsBean>>networkSchedulers())
                .subscribe(new ResponseSubscriber<ResponseJson<RealtimeMonitorBean.RowsBean>>(baseView) {
                    @Override
                    public void onSuccess(ResponseJson<RealtimeMonitorBean.RowsBean> response) {
                        RealtimeMonitorBean.RowsBean rowsBean = response.getData();
                        baseView.rmSingleResult(rowsBean);
                    }
                });
    }



}

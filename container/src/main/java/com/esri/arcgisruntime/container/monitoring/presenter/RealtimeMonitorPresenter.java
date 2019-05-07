package com.esri.arcgisruntime.container.monitoring.presenter;

import com.esri.arcgisruntime.container.monitoring.base.BasePresenter;
import com.esri.arcgisruntime.container.monitoring.bean.LocationDetailsBean;
import com.esri.arcgisruntime.container.monitoring.bean.RealtimeMonitorBean;
import com.esri.arcgisruntime.container.monitoring.bean.SearchNumberBean;
import com.esri.arcgisruntime.container.monitoring.http.ApiManager;
import com.esri.arcgisruntime.container.monitoring.http.ResponseJson;
import com.esri.arcgisruntime.container.monitoring.http.ResponseSubscriber;
import com.esri.arcgisruntime.container.monitoring.http.ResponseSubscriberBecomeSilent;
import com.esri.arcgisruntime.container.monitoring.http.RxThreadUtil;
import com.esri.arcgisruntime.container.monitoring.utils.NetWorkTool;
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

        if (NetWorkTool.isConnect()){

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
    }

    //静默获取实时监控全部数据
    public void realtimeMonitorResultBS(Map<String,String> params){

        if (NetWorkTool.isConnectBS()){

            ApiManager.getApiServer().realtimeMonitorResult(params)
                    .compose(RxThreadUtil.<ResponseJson<RealtimeMonitorBean>>networkSchedulers())
                    .subscribe(new ResponseSubscriberBecomeSilent<ResponseJson<RealtimeMonitorBean>>(baseView) {
                        @Override
                        public void onSuccess(ResponseJson<RealtimeMonitorBean> response) {
                            RealtimeMonitorBean realtimeMonitorBean = response.getData();
                            baseView.rmResult(realtimeMonitorBean);
                        }
                    });
        }
    }

    //获取实时监控数据 根据编号
    public void realtimeMonitorSingleResult(Map<String,String> params){

        if (NetWorkTool.isConnect()){

            ApiManager.getApiServer().realtimeMonitorSingleResult(params)
                    .compose(RxThreadUtil.<ResponseJson<SearchNumberBean>>networkSchedulers())
                    .subscribe(new ResponseSubscriber<ResponseJson<SearchNumberBean>>(baseView) {
                        @Override
                        public void onSuccess(ResponseJson<SearchNumberBean> response) {
                            SearchNumberBean rowsBean = response.getData();
                            baseView.rmSingleResult(rowsBean);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            baseView.rmSingleResult(null);

                        }
                    });
        }
    }


    //获取显示位置的详情数据
    public void getLocationDetails(Map<String,String> params){
        if (NetWorkTool.isConnect()){

            ApiManager.getApiServer().locationDetails(params)
                    .compose(RxThreadUtil.<ResponseJson<LocationDetailsBean>>networkSchedulers())
                    .subscribe(new ResponseSubscriber<ResponseJson<LocationDetailsBean>>(baseView) {
                        @Override
                        public void onSuccess(ResponseJson<LocationDetailsBean> response) {
                            LocationDetailsBean locationDetailsBean = response.getData();
                            baseView.rmLocationDetails(locationDetailsBean);
                        }
                    });
        }
    }

    //获取显示位置的详情数据
    public void getLocationDetailsBS(Map<String,String> params){
        if (NetWorkTool.isConnectBS()){

            ApiManager.getApiServer().locationDetails(params)
                    .compose(RxThreadUtil.<ResponseJson<LocationDetailsBean>>networkSchedulers())
                    .subscribe(new ResponseSubscriberBecomeSilent<ResponseJson<LocationDetailsBean>>(baseView) {
                        @Override
                        public void onSuccess(ResponseJson<LocationDetailsBean> response) {
                            LocationDetailsBean locationDetailsBean = response.getData();
                            baseView.rmLocationDetails(locationDetailsBean);
                        }
                    });
        }
    }



}

package com.esri.arcgisruntime.container.monitoring.presenter;

import com.esri.arcgisruntime.container.monitoring.base.BasePresenter;
import com.esri.arcgisruntime.container.monitoring.bean.QueryRuteBean;
import com.esri.arcgisruntime.container.monitoring.bean.QueryRuteResult;
import com.esri.arcgisruntime.container.monitoring.http.ApiManager;
import com.esri.arcgisruntime.container.monitoring.http.ResponseJson;
import com.esri.arcgisruntime.container.monitoring.http.ResponseSubscriber;
import com.esri.arcgisruntime.container.monitoring.http.RxThreadUtil;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IQueryRoute;

import java.util.Map;

/**
 * 路线查询请求累
 */
public class QueryRoutePresenter extends BasePresenter<IQueryRoute> {
    public QueryRoutePresenter(IQueryRoute from) {
        super(from);
    }

    //查询起点列表
    public void queryStartRoute(Map<String,String> params){

        ApiManager.getApiServer().queryStartRoute(params)
                .compose(RxThreadUtil.<ResponseJson<QueryRuteBean>>networkSchedulers())
                .subscribe(new ResponseSubscriber<ResponseJson<QueryRuteBean>>(baseView) {
                    @Override
                    public void onSuccess(ResponseJson<QueryRuteBean> response) {
                        QueryRuteBean queryRuteBean = response.getData();
                        baseView.queryStartSucceed(queryRuteBean);
                    }
                });
    }

    //查询终点列表
    public void queryEndRoute(Map<String,String> params){

        ApiManager.getApiServer().queryEndRoute(params)
                .compose(RxThreadUtil.<ResponseJson<QueryRuteBean>>networkSchedulers())
                .subscribe(new ResponseSubscriber<ResponseJson<QueryRuteBean>>(baseView) {
                    @Override
                    public void onSuccess(ResponseJson<QueryRuteBean> response) {
                        QueryRuteBean queryRuteBean = response.getData();
                        baseView.queryEndSucceed(queryRuteBean);
                    }
                });
    }



    //查询的路线结果
    public void queryRouteResult(Map<String,String> params){

        ApiManager.getApiServer().queryRouteResult(params)
                .compose(RxThreadUtil.<ResponseJson<QueryRuteResult>>networkSchedulers())
                .subscribe(new ResponseSubscriber<ResponseJson<QueryRuteResult>>(baseView) {
                    @Override
                    public void onSuccess(ResponseJson<QueryRuteResult> response) {
                        QueryRuteResult queryRuteResult = response.getData();
                        baseView.queryRouteSucceed(queryRuteResult);
                    }
                });
    }


}

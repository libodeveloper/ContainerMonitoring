package com.esri.arcgisruntime.container.monitoring.presenter;

import com.esri.arcgisruntime.container.monitoring.base.BasePresenter;
import com.esri.arcgisruntime.container.monitoring.bean.BillQueryBean;
import com.esri.arcgisruntime.container.monitoring.http.ApiManager;
import com.esri.arcgisruntime.container.monitoring.http.ResponseJson;
import com.esri.arcgisruntime.container.monitoring.http.ResponseSubscriber;
import com.esri.arcgisruntime.container.monitoring.http.RxThreadUtil;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IBillQueryResult;

import java.util.List;
import java.util.Map;

/**
 * 单据查询
 */
public class BillQueryPresenter extends BasePresenter<IBillQueryResult> {
    public BillQueryPresenter(IBillQueryResult from) {
        super(from);
    }

    //单据查询
    public void billQuery(Map<String,String> params){

        ApiManager.getApiServer().billQuery(params)
                .compose(RxThreadUtil.<ResponseJson<List<BillQueryBean>>>networkSchedulers())
                .subscribe(new ResponseSubscriber<ResponseJson<List<BillQueryBean>>>(baseView) {
                    @Override
                    public void onSuccess(ResponseJson<List<BillQueryBean>> response) {
                        List<BillQueryBean> billQueryBeanList = response.getData();
                        baseView.billQuerySucceed(billQueryBeanList);
                    }
                });
    }


}

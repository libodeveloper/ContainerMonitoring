package com.esri.arcgisruntime.container.monitoring.presenter;

import com.esri.arcgisruntime.container.monitoring.base.BasePresenter;
import com.esri.arcgisruntime.container.monitoring.bean.BillDetailsBean;
import com.esri.arcgisruntime.container.monitoring.http.ApiManager;
import com.esri.arcgisruntime.container.monitoring.http.ResponseJson;
import com.esri.arcgisruntime.container.monitoring.http.ResponseSubscriber;
import com.esri.arcgisruntime.container.monitoring.http.RxThreadUtil;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IBillDetails;

import java.util.Map;

/**
 * 单据详情
 */
public class BillDetailsPresenter extends BasePresenter<IBillDetails> {
    public BillDetailsPresenter(IBillDetails from) {
        super(from);
    }

    //单据详情
    public void billDetails(Map<String,String> params){

        ApiManager.getApiServer().billDetails(params)
                .compose(RxThreadUtil.<ResponseJson<BillDetailsBean>>networkSchedulers())
                .subscribe(new ResponseSubscriber<ResponseJson<BillDetailsBean>>(baseView) {
                    @Override
                    public void onSuccess(ResponseJson<BillDetailsBean> response) {
                        BillDetailsBean billDetailsBean = response.getData();
                        baseView.billDetailSucceed(billDetailsBean);
                    }
                });
    }


}

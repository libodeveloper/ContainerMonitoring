package com.esri.arcgisruntime.container.monitoring.presenter;

import com.esri.arcgisruntime.container.monitoring.base.BasePresenter;
import com.esri.arcgisruntime.container.monitoring.bean.BusinessQueryResultBean;
import com.esri.arcgisruntime.container.monitoring.http.ApiManager;
import com.esri.arcgisruntime.container.monitoring.http.ResponseJson;
import com.esri.arcgisruntime.container.monitoring.http.ResponseSubscriber;
import com.esri.arcgisruntime.container.monitoring.http.RxThreadUtil;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IBusinessQuery;

import java.util.Map;

/**
 * 业务查询
 */
public class BusinessQueryPresenter extends BasePresenter<IBusinessQuery> {
    public BusinessQueryPresenter(IBusinessQuery from) {
        super(from);
    }

    //单据查询
    public void businessQuery(Map<String,String> params){

        ApiManager.getApiServer().businessQuery(params)
                .compose(RxThreadUtil.<ResponseJson<BusinessQueryResultBean>>networkSchedulers())
                .subscribe(new ResponseSubscriber<ResponseJson<BusinessQueryResultBean>>(baseView) {
                    @Override
                    public void onSuccess(ResponseJson<BusinessQueryResultBean> response) {
                        BusinessQueryResultBean businessQueryResultBean = response.getData();
                        baseView.businessQuerySucceed(businessQueryResultBean);
                    }
                });
    }


}

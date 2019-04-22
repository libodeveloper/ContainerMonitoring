package com.esri.arcgisruntime.container.monitoring.presenter;

import com.esri.arcgisruntime.container.monitoring.base.BasePresenter;
import com.esri.arcgisruntime.container.monitoring.bean.BusinessQueryResultBean;
import com.esri.arcgisruntime.container.monitoring.bean.SiteBean;
import com.esri.arcgisruntime.container.monitoring.http.ApiManager;
import com.esri.arcgisruntime.container.monitoring.http.ResponseJson;
import com.esri.arcgisruntime.container.monitoring.http.ResponseSubscriber;
import com.esri.arcgisruntime.container.monitoring.http.RxThreadUtil;
import com.esri.arcgisruntime.container.monitoring.utils.NetWorkTool;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IBusinessQuery;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IBussQuerySite;

import java.util.Map;

/**
 * 业务查询 站点查询
 */
public class BusinessQuerySitePresenter extends BasePresenter<IBussQuerySite> {
    public BusinessQuerySitePresenter(IBussQuerySite from) {
        super(from);
    }

    //单据查询
    public void businessQuerySite(Map<String,String> params){

        if (NetWorkTool.isConnect()){

            ApiManager.getApiServer().businessQuerySite(params)
                    .compose(RxThreadUtil.<ResponseJson<SiteBean>>networkSchedulers())
                    .subscribe(new ResponseSubscriber<ResponseJson<SiteBean>>(baseView) {
                        @Override
                        public void onSuccess(ResponseJson<SiteBean> response) {
                            SiteBean siteBean = response.getData();
                            baseView.bussQuerySiteSucceed(siteBean);
                        }
                    });
        }
    }


}

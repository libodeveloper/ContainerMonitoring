package com.esri.arcgisruntime.container.monitoring.viewinterfaces;


import com.esri.arcgisruntime.container.monitoring.base.IBaseView;
import com.esri.arcgisruntime.container.monitoring.bean.QueryRuteBean;
import com.esri.arcgisruntime.container.monitoring.bean.QueryRuteResult;

/**
 * 路线查询接口
 */
public interface IQueryRoute extends IBaseView {
    void queryStartSucceed(QueryRuteBean queryRuteBean);
    void queryEndSucceed(QueryRuteBean queryRuteBean);
    void queryRouteSucceed(QueryRuteResult queryRuteResult);

}

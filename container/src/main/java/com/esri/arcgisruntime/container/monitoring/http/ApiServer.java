package com.esri.arcgisruntime.container.monitoring.http;

import com.esri.arcgisruntime.container.monitoring.bean.BillDetailsBean;
import com.esri.arcgisruntime.container.monitoring.bean.BillQueryBean;
import com.esri.arcgisruntime.container.monitoring.bean.BusinessQueryResultBean;
import com.esri.arcgisruntime.container.monitoring.bean.QueryRuteBean;
import com.esri.arcgisruntime.container.monitoring.bean.QueryRuteResult;
import com.esri.arcgisruntime.container.monitoring.bean.RealtimeMonitorBean;
import com.esri.arcgisruntime.container.monitoring.bean.SiteBean;
import com.esri.arcgisruntime.container.monitoring.bean.User;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @desc:ERP本App相关接口
 */
public interface ApiServer {
    public  static final String BASE_URL="http://220.194.42.2:8803";

    @FormUrlEncoded
    @POST("/appServer/login.json")
    public Observable<ResponseJson<User>> login(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("/appServer/servlet/queryRouteStartList.json")      //查询起点列表
    public Observable<ResponseJson<QueryRuteBean>> queryStartRoute(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("/appServer/servlet/queryRouteEnd.json")     //查询终点列表
    public Observable<ResponseJson<QueryRuteBean>> queryEndRoute(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/appServer/servlet/queryRouteList.json")     //根据起点终点查询的路线结果
    public Observable<ResponseJson<QueryRuteResult>> queryRouteResult(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/appServer/servlet/monitorList.json")     //实时监控全部数据
    public Observable<ResponseJson<RealtimeMonitorBean>> realtimeMonitorResult(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/appServer/servlet/queryMonitor.json")     //实时监控根据编号查询数据
    public Observable<ResponseJson<RealtimeMonitorBean.RowsBean>> realtimeMonitorSingleResult(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/appServer/servlet/queryBillList.json")     //单据查询
    public Observable<ResponseJson<BillQueryBean>> billQuery(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/appServer/servlet/bill.json")     //单据详情
    public Observable<ResponseJson<BillDetailsBean>> billDetails(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/appServer/servlet/queryTransactionList.json")     //业务查询
    public Observable<ResponseJson<BusinessQueryResultBean>> businessQuery(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("/appServer/servlet/TransactionAllList.json")     //站点
    public Observable<ResponseJson<SiteBean>> businessQuerySite(@FieldMap Map<String, String> params);
//
//    @FormUrlEncoded
//    @POST("/api/cartypeinfo/GetDiff")
//    public Observable<ResponseJson<CarTypeSelectModel>> getCarDiff(@FieldMap Map<String, String> params);

}

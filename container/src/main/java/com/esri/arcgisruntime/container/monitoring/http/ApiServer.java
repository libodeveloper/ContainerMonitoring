package com.esri.arcgisruntime.container.monitoring.http;

import com.esri.arcgisruntime.container.monitoring.bean.BillDetailsBean;
import com.esri.arcgisruntime.container.monitoring.bean.BillQueryBean;
import com.esri.arcgisruntime.container.monitoring.bean.BusinessQueryResultBean;
import com.esri.arcgisruntime.container.monitoring.bean.LocationDetailsBean;
import com.esri.arcgisruntime.container.monitoring.bean.QueryRuteBean;
import com.esri.arcgisruntime.container.monitoring.bean.QueryRuteResult;
import com.esri.arcgisruntime.container.monitoring.bean.RealtimeMonitorBean;
import com.esri.arcgisruntime.container.monitoring.bean.SearchNumberBean;
import com.esri.arcgisruntime.container.monitoring.bean.SiteBean;
import com.esri.arcgisruntime.container.monitoring.bean.User;
import com.esri.arcgisruntime.container.monitoring.bean.UserInfo;
import com.esri.arcgisruntime.container.monitoring.global.Constants;

import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @desc:本App相关接口
 */
public interface ApiServer {


//    public  static final String BASE_URL="http://220.194.42.2:8803";
      public  static final String BASE_URL= "http://58.49.50.98:10003";
      public  static final String BASE_URL_TEST="http://58.49.50.98:10008";

    @FormUrlEncoded
    @POST("/appServer/login.json") //登录
    public Observable<ResponseJson<User>> login(@FieldMap Map<String, String> params);

    @FormUrlEncoded   // 退出登录
    @POST("/appServer/servlet/logout.json")
    public Observable<ResponseJson<User>> loginOut(@FieldMap Map<String, String> params);

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
    public Observable<ResponseJson<SearchNumberBean>> realtimeMonitorSingleResult(@FieldMap Map<String, String> params);

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

    @FormUrlEncoded
    @POST("/appServer/servlet/getUserInfo.json") //获取用户信息
    public Observable<ResponseJson<UserInfo>> getUserInfo(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/appServer/servlet/updPassword.json") //修改密码
    public Observable<ResponseJson<Object>> fixPassword(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("/appServer/servlet/monitor.json") //获取标点详情 实时位置详情
    public Observable<ResponseJson<LocationDetailsBean>> locationDetails(@FieldMap Map<String, String> params);

}

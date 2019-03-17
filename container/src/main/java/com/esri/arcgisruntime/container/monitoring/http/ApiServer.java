package com.esri.arcgisruntime.container.monitoring.http;


import com.esri.arcgisruntime.container.monitoring.bean.User;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @desc:ERP本App相关接口
 */
public interface ApiServer {
    public  static final String BASE_URL= "http://192.168.0.140:8066";
//    public  static final String BASE_URL= BuildConfig.BASE_URL;


    @FormUrlEncoded
    @POST("/api/user/login")
    public Observable<ResponseJson<User>> login(@FieldMap Map<String, String> params);

//    @FormUrlEncoded
//    @POST("/api/CarTypeInfo/GetParams")
//    public Observable<ResponseJson<List<CarConfigModel>>> getCarConfig(@FieldMap Map<String, String> params);
//
//    @FormUrlEncoded
//    @POST("/api/cartypeinfo/GetDiff")
//    public Observable<ResponseJson<CarTypeSelectModel>> getCarDiff(@FieldMap Map<String, String> params);

}

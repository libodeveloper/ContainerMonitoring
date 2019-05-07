package com.esri.arcgisruntime.container.monitoring.http;

import com.esri.arcgisruntime.container.monitoring.global.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {

    public static ApiServer apiServer;

    /**
     * 初始化网络连接
     */
    private static ApiServer createServer(String url) {
        OkHttpClient client = CustomerOkHttpClient.getClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(ApiServer.class);
    }

    public static ApiServer getApiServer() {
        if (apiServer == null) {
            apiServer = createServer(ApiServer.BASE_URL);
        }
        if (Constants.isTestURL){
            apiServer = createServer(ApiServer.BASE_URL_TEST);
        }

        return apiServer;
    }

}

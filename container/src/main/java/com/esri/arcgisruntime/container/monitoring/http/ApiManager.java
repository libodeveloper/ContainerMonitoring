package com.esri.arcgisruntime.container.monitoring.http;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    public  static final String BASE_URL="http://192.168.0.140:8066";

    private static ApiServer apiServer;
    private static ApiServer globalServer;
    private static ApiServer apiServerTest;

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
            apiServer = createServer(BASE_URL);
        }
        return apiServer;
    }

    public static ApiServer getApiServerTest(String baseUrl) {
        if (apiServerTest == null) {
            apiServerTest = createServer(baseUrl);
        }
        return apiServerTest;
    }
}

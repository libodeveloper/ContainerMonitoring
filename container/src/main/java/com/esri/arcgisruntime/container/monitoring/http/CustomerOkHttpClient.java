package com.esri.arcgisruntime.container.monitoring.http;

import android.accounts.NetworkErrorException;
import android.text.TextUtils;

import com.esri.arcgisruntime.container.monitoring.application.CMApplication;
import com.esri.arcgisruntime.container.monitoring.bean.User;
import com.esri.arcgisruntime.container.monitoring.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CustomerOkHttpClient {

    public static OkHttpClient client;
    private static final int DEFAULT_TIMEOUT = 60;

    private CustomerOkHttpClient() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private static void create() {
        int maxCacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(CMApplication.getAppContext().getCacheDir(), maxCacheSize);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response = chain.proceed(request);

                String cacheControl = request.cacheControl().toString();
                if (TextUtils.isEmpty(cacheControl)) {
                    cacheControl = "public, max-age=60 ,max-stale=2419200";
                }
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            }
        };

        client = new OkHttpClient.Builder()
                .addNetworkInterceptor(cacheInterceptor)
                .addInterceptor(new LoggingInterceptor())
//                .addInterceptor(new ParamsInterceptor())
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache)
                .build();

    }

    public static OkHttpClient getClient() {
        if (client == null) {
            create();
        }
        return client;
    }




    static class LoggingInterceptor implements Interceptor {
        private static final String TAG = "CustomerOkHttpClient";

        @Override
        public Response intercept(Chain chain) throws IOException {
            try {
                if(chain!=null){
                    Request request = chain.request();
                    long t1 = System.nanoTime();
                    LogUtil.e(TAG, String.format("Request: %s", request.url()));
                    if(request!=null){
                        Response response = chain.proceed(request);
                        if (response!=null) {


                            long t2 = System.nanoTime();
                            String responseString = new String(response.body().bytes());
                            LogUtil.e(TAG, String.format("Access Server in %.1fms ,Response: %s", (t2 - t1) / 1e6d,responseString));
                            LogUtil.e(TAG, "返回 == "+response.request().url());
                            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), responseString)).build();
                        } else {
                            return null;
                        }
                    }else{
                        return null;
                    }
                }else{
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    throw new NetworkErrorException();
                } catch (NetworkErrorException e1) {
                    e1.printStackTrace();
                }
                return null;
            }
        }
    }

    static class ParamsInterceptor implements Interceptor{
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder1 = request.newBuilder();
            int userId = 0;
            User user = CMApplication.getUser();
            if(user!=null){
                userId = user.getUserId();
            }
            if (request.method().toUpperCase().equals("POST")) {
                builder1.url(request.url().url() + "?userId=" +(userId==0?"":String.valueOf(userId)));
                LogUtil.e("CustomerOkHttpClient", "请求 == "+request.url().url());
                builder1.addHeader("X-Requested-With", "XMLHttpRequest");
                builder1.tag(builder1.tag(null).build());

            } else {
                builder1.addHeader("X-Requested-With", "XMLHttpRequest");
            }
            builder1.addHeader("X-Requested-With", "XMLHttpRequest");
            Request request1 = builder1.build();
            return chain.proceed(request1);
        }

    }



}

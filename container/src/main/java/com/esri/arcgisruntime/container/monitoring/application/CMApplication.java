package com.esri.arcgisruntime.container.monitoring.application;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.esri.arcgisruntime.container.monitoring.bean.NumberCache;
import com.esri.arcgisruntime.container.monitoring.bean.User;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.http.ApiServer;
import com.esri.arcgisruntime.container.monitoring.http.CustomerOkHttpClient;
import com.esri.arcgisruntime.container.monitoring.utils.ACache;
import com.esri.arcgisruntime.container.monitoring.utils.LocalManageUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wangyd on 16/7/21.
 */
public class CMApplication extends Application {
    private static final String TAG = CMApplication.class.getSimpleName();

    private static int mMainThreadId = -1;
    private static Thread mMainThread;
    private static Handler mMainThreadHandler;
    private static Looper mMainLooper;

    private static CMApplication app;
    private static ApiServer apiServer;
    //登录的用户对象，用get方法获取，如果返回为空，从Acache中获取
    private static User user;

//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(base);
//        MultiDex.install(this);
//    }

    @Override
    protected void attachBaseContext(Context base) {
        //保存系统选择语言
        LocalManageUtil.saveSystemCurrentLanguage(base);
        super.attachBaseContext(LocalManageUtil.setLocal(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //保存系统选择语言
        LocalManageUtil.onConfigurationChanged(getApplicationContext());
    }


    @Override
    public void onCreate() {
        super.onCreate();
        LocalManageUtil.setApplicationLanguage(this);
        app = this;
        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();

        initApiServer();

        initUM();
//        JzgCrashHandler.getInstance().init(this);//崩溃日志
    }

    private void initUM(){

        UMConfigure.setLogEnabled(true);

        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        // 选用AUTO页面采集模式 ( 采用自动模式后 Activity 里就不用写 MobclickAgent.onPause(this);了 自动采集 fragment里依然要写  MobclickAgent.onPageStart(TAG); )
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }

    /**
     * 初始化网络连接
     */
    public void initApiServer() {
        OkHttpClient client = CustomerOkHttpClient.getClient();
        Retrofit retrofit = null;
        if (apiServer == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiServer.BASE_URL)
                    .client(client)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiServer = retrofit.create(ApiServer.class);

        }
    }

    public static ApiServer getApiServer() {
        return apiServer;
    }

    public static CMApplication getAppContext() {
        return app;
    }


    public static int getMainThreadId() {
        return mMainThreadId;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    /**
     * 获取用户登录对象
     */
    public static User getUser() {
            user = (User) ACache.get(app).getAsObject(Constants.KEY_ACACHE_USER);
        return user;
    }

}

package com.esri.arcgisruntime.container.monitoring.presenter;

import com.esri.arcgisruntime.container.monitoring.base.BasePresenter;
import com.esri.arcgisruntime.container.monitoring.bean.User;
import com.esri.arcgisruntime.container.monitoring.http.ApiManager;
import com.esri.arcgisruntime.container.monitoring.http.ResponseJson;
import com.esri.arcgisruntime.container.monitoring.http.ResponseSubscriber;
import com.esri.arcgisruntime.container.monitoring.http.RxThreadUtil;
import com.esri.arcgisruntime.container.monitoring.utils.LogUtil;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
import com.esri.arcgisruntime.container.monitoring.utils.UIUtils;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.ILogin;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by 李波 on 2017/1/20.
 *
 * mvp 模式 Presenter层的网络请求
 */

public class LoginPresenter extends BasePresenter<ILogin> {
    public LoginPresenter(ILogin from) {
        super(from);
    }

    //登录
    public void login(final String username, final String password, final boolean isShowDialog){
        Map<String,String> params = new HashMap<>();
        params.put("username",username);
        params.put("password",password);
        params = MD5Utils.encryptParams(params);
        LogUtil.e(TAG, UIUtils.getUrl(params));

        ApiManager.getApiServer().login(params)
                .compose(RxThreadUtil.<ResponseJson<User>>networkSchedulers())
                .subscribe(new ResponseSubscriber<ResponseJson<User>>(baseView) {
                    @Override
                    public void onSuccess(ResponseJson<User> response) {
                        User user = response.getMemberValue();
                        baseView.loginSucceed(user);
                    }
                });

    }



    //获取token
//    public void getToken(final String username, final String password, final boolean isShowDialog){
//        Map<String,String> params = new HashMap<>();
//        params.put("username",username);
//        params.put("password",password);
//        params = MD5Utils.encryptParams(params);
//        LogUtil.e(TAG, UIUtils.getUrl(params));
//
//        ApiManager.getApiServer().token(params)
//                .compose(RxThreadUtil.<ResponseJson<User>>networkSchedulers())
//                .subscribe(new ResponseSubscriber<ResponseJson<User>>(baseView) {
//                    @Override
//                    public void onSuccess(ResponseJson<User> response) {
//                        User user = response.getMemberValue();
//                        baseView.getTokenSucceed();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) { //请求失败的单独处理
//                        super.onError(e);
//                        baseView.getTokenFailed();
//                    }
//                });
//
//    }
}

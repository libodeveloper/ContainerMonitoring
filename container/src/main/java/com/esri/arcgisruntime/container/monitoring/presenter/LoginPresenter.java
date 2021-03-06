package com.esri.arcgisruntime.container.monitoring.presenter;

import com.esri.arcgisruntime.container.monitoring.base.BasePresenter;
import com.esri.arcgisruntime.container.monitoring.bean.User;
import com.esri.arcgisruntime.container.monitoring.http.ApiManager;
import com.esri.arcgisruntime.container.monitoring.http.ResponseJson;
import com.esri.arcgisruntime.container.monitoring.http.ResponseSubscriber;
import com.esri.arcgisruntime.container.monitoring.http.RxThreadUtil;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.ILogin;

import java.util.Map;


public class LoginPresenter extends BasePresenter<ILogin> {
    public LoginPresenter(ILogin from) {
        super(from);
    }

    //登录
    public void login(Map<String,String> params){

        ApiManager.getApiServer().login(params)
                .compose(RxThreadUtil.<ResponseJson<User>>networkSchedulers())
                .subscribe(new ResponseSubscriber<ResponseJson<User>>(baseView) {
                    @Override
                    public void onSuccess(ResponseJson<User> response) {
                        User user = response.getData();
                        baseView.loginSucceed(user);
                    }
                });

    }

}

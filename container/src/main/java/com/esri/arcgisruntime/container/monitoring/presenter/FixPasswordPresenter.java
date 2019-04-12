package com.esri.arcgisruntime.container.monitoring.presenter;

import com.esri.arcgisruntime.container.monitoring.base.BasePresenter;
import com.esri.arcgisruntime.container.monitoring.bean.UserInfo;
import com.esri.arcgisruntime.container.monitoring.http.ApiManager;
import com.esri.arcgisruntime.container.monitoring.http.ResponseJson;
import com.esri.arcgisruntime.container.monitoring.http.ResponseSubscriber;
import com.esri.arcgisruntime.container.monitoring.http.RxThreadUtil;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IFixPassword;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IUserInfo;

import java.util.Map;

/**
 * 密码修改
 */
public class FixPasswordPresenter extends BasePresenter<IFixPassword> {

    public FixPasswordPresenter(IFixPassword from) {
        super(from);
    }

    public void fixPassword(Map<String,String> params){

        ApiManager.getApiServer().fixPassword(params)
                .compose(RxThreadUtil.<ResponseJson<Object>>networkSchedulers())
                .subscribe(new ResponseSubscriber<ResponseJson<Object>>(baseView) {
                    @Override
                    public void onSuccess(ResponseJson<Object> response) {
                        baseView.showError(response.getMsg());
                        baseView.Succeed();
                    }
                });
    }


}

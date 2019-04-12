package com.esri.arcgisruntime.container.monitoring.presenter;

import com.esri.arcgisruntime.container.monitoring.base.BasePresenter;
import com.esri.arcgisruntime.container.monitoring.bean.BillQueryBean;
import com.esri.arcgisruntime.container.monitoring.bean.UserInfo;
import com.esri.arcgisruntime.container.monitoring.http.ApiManager;
import com.esri.arcgisruntime.container.monitoring.http.ResponseJson;
import com.esri.arcgisruntime.container.monitoring.http.ResponseSubscriber;
import com.esri.arcgisruntime.container.monitoring.http.RxThreadUtil;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IBillQueryResult;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IUserInfo;

import java.util.Map;

/**
 * 用户查询
 */
public class UserInfoPresenter extends BasePresenter<IUserInfo> {

    public UserInfoPresenter(IUserInfo from) {
        super(from);
    }

    //单据查询
    public void getUserInfo(Map<String,String> params){

        ApiManager.getApiServer().getUserInfo(params)
                .compose(RxThreadUtil.<ResponseJson<UserInfo>>networkSchedulers())
                .subscribe(new ResponseSubscriber<ResponseJson<UserInfo>>(baseView) {
                    @Override
                    public void onSuccess(ResponseJson<UserInfo> response) {
                        UserInfo userInfo = response.getData();
                        baseView.Succeed(userInfo);
                    }
                });
    }


}

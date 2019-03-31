package com.esri.arcgisruntime.container.monitoring.viewinterfaces;


import com.esri.arcgisruntime.container.monitoring.base.IBaseView;
import com.esri.arcgisruntime.container.monitoring.bean.User;

public interface ILogin extends IBaseView {
    void loginSucceed(User user);
    void loginFailed();
}

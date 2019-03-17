package com.esri.arcgisruntime.container.monitoring.viewinterfaces;


import com.esri.arcgisruntime.container.monitoring.base.IBaseView;
import com.esri.arcgisruntime.container.monitoring.bean.User;

/**
 * Created by 李波 on 2017/1/20.
 *
 * mvp 模式接口
 */
public interface ILogin extends IBaseView {
    void loginSucceed(User user);
    void loginFailed();

    void getTokenSucceed();
    void getTokenFailed();
}

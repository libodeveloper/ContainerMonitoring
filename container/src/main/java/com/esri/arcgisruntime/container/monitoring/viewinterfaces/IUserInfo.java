package com.esri.arcgisruntime.container.monitoring.viewinterfaces;

import com.esri.arcgisruntime.container.monitoring.base.IBaseView;
import com.esri.arcgisruntime.container.monitoring.bean.UserInfo;

/**
 * Created by libo on 2019/4/12.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public interface IUserInfo extends IBaseView{
    void Succeed(UserInfo user);
}

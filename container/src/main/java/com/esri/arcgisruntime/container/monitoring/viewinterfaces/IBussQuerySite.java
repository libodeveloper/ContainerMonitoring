package com.esri.arcgisruntime.container.monitoring.viewinterfaces;

import com.esri.arcgisruntime.container.monitoring.base.IBaseView;
import com.esri.arcgisruntime.container.monitoring.bean.SiteBean;

/**
 * Created by libo on 2019/4/1.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public interface IBussQuerySite extends IBaseView {

    void bussQuerySiteSucceed(SiteBean siteBean);

}

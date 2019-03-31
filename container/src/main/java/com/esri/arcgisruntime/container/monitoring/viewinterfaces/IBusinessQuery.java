package com.esri.arcgisruntime.container.monitoring.viewinterfaces;

import com.esri.arcgisruntime.container.monitoring.base.IBaseView;
import com.esri.arcgisruntime.container.monitoring.bean.BusinessQueryResultBean;

/**
 * Created by libo on 2019/3/31.
 * 业务查询
 */

public interface IBusinessQuery extends IBaseView{

    void businessQuerySucceed(BusinessQueryResultBean businessQueryResultBean);

}

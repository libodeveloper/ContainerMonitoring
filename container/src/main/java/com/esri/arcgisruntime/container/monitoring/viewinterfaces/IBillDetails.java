package com.esri.arcgisruntime.container.monitoring.viewinterfaces;

import com.esri.arcgisruntime.container.monitoring.base.IBaseView;
import com.esri.arcgisruntime.container.monitoring.bean.BillDetailsBean;

/**
 * Created by libo on 2019/3/31.
 * 单据详情
 */

public interface IBillDetails extends IBaseView {

    void billDetailSucceed(BillDetailsBean billDetailsBean);

}

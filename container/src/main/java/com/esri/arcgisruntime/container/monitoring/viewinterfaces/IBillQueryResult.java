package com.esri.arcgisruntime.container.monitoring.viewinterfaces;

import com.esri.arcgisruntime.container.monitoring.base.IBaseView;
import com.esri.arcgisruntime.container.monitoring.bean.BillQueryBean;

import java.util.List;

/**
 * Created by libo on 2019/3/30.
 */

public interface IBillQueryResult extends IBaseView {

    void billQuerySucceed(BillQueryBean billQueryBean);

}

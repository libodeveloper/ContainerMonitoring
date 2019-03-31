package com.esri.arcgisruntime.container.monitoring.viewinterfaces;

import com.esri.arcgisruntime.container.monitoring.base.IBaseView;
import com.esri.arcgisruntime.container.monitoring.bean.RealtimeMonitorBean;

/**
 * Created by libo on 2019/3/30.
 */

public interface IRealtimeMonitoring extends IBaseView {

    void  rmResult(RealtimeMonitorBean realtimeMonitorBean);
    void  rmSingleResult(RealtimeMonitorBean.RowsBean rowsBean);

}

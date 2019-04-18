package com.esri.arcgisruntime.container.monitoring.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by libo on 2019/3/31.
 */

public class NumberCache implements Serializable {
//
//    private List<String> containerNumberCache = new ArrayList<>(); //集装箱编号搜索缓存
//
//    private List<String> LockNumberCache = new ArrayList<>();      //关锁编号搜索缓存

    private List<SearchNumberBean.RowsBean> containerRows = new ArrayList<>();

    private List<SearchNumberBean.RowsBean> lockRows = new ArrayList<>();

    public List<SearchNumberBean.RowsBean> getContainerRows() {
        return containerRows;
    }

    public void setContainerRows(List<SearchNumberBean.RowsBean> containerRows) {
        this.containerRows = containerRows;
    }

    public List<SearchNumberBean.RowsBean> getLockRows() {
        return lockRows;
    }

    public void setLockRows(List<SearchNumberBean.RowsBean> lockRows) {
        this.lockRows = lockRows;
    }


//    public List<String> getLockNumberCache() {
//        return LockNumberCache;
//    }
//
//    public void setLockNumberCache(List<String> lockNumberCache) {
//        LockNumberCache = lockNumberCache;
//    }
//
//    public List<String> getContainerNumberCache() {
//        return containerNumberCache;
//    }
//
//    public void setContainerNumberCache(List<String> containerNumberCache) {
//        this.containerNumberCache = containerNumberCache;
//    }
}

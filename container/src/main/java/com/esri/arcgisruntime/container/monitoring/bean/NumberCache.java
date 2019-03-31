package com.esri.arcgisruntime.container.monitoring.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by libo on 2019/3/31.
 */

public class NumberCache implements Serializable {

    private List<String> numberCache = new ArrayList<>();

    public List<String> getNumberCache() {
        return numberCache;
    }

    public void setNumberCache(List<String> numberCache) {
        this.numberCache = numberCache;
    }
}

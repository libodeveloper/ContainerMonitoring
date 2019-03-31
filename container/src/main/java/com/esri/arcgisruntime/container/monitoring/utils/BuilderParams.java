package com.esri.arcgisruntime.container.monitoring.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by libo on 2019/3/30.
 */

public class BuilderParams {

    Map<String,String> params;

   public BuilderParams(){
       params = new HashMap<>();
    }

    public Map<String,String> put(String key,String value){
       params.put(key,value);
       return params;
    }

    public Map<String,String> returnParams(){
        params = MD5Utils.encryptParams(params);
       return params;
    }



}

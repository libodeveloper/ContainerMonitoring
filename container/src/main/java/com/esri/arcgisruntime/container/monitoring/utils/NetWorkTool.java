package com.esri.arcgisruntime.container.monitoring.utils;

import com.blankj.utilcode.utils.NetworkUtils;
import com.esri.arcgisruntime.container.monitoring.application.CMApplication;

/**
 * @author: voiceofnet
 * email: 郑有权
 * @time: 2017/2/18 14:50
 * @desc:
 */
public class NetWorkTool {


    public static boolean isConnect(){
        if (NetworkUtils.isConnected(CMApplication.getAppContext())){
            return true;
        }else{
            MyToast.showLong("没有网络");
            return false;
        }
    }

    public static boolean isConnect(String msg){
        if (NetworkUtils.isConnected(CMApplication.getAppContext())){
            return true;
        }else{
            MyToast.showLong(msg);
            return false;
        }

    }


}

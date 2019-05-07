package com.esri.arcgisruntime.container.monitoring.utils;

import com.blankj.utilcode.utils.NetworkUtils;
import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.application.CMApplication;

/**
 * @desc: 检测网络连接
 */
public class NetWorkTool {


    public static boolean isConnect(){
        if (NetworkUtils.isConnected(CMApplication.getAppContext())){
            return true;
        }else{
            MyToast.showLong(CMApplication.getAppContext().getResources().getString(R.string.please_check_your_network));
            return false;
        }
    }

    public static boolean isConnectBS(){
        if (NetworkUtils.isConnected(CMApplication.getAppContext())){
            return true;
        }else{
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

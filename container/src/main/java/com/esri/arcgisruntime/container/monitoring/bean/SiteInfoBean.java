package com.esri.arcgisruntime.container.monitoring.bean;

/**
 * Created by libo on 2019/3/14.
 * 地图标点点击后的信息
 */

public class SiteInfoBean{


    private String number;           //编号
    private String containerNumber;  //集装箱编号
    private String lockNumber;       //关锁编号
    private String pathNumber;       //路径编号
    private String startSite;        //起点站点
    private String destSite;         //目的站点
    private String numberPlate;      //车牌号
    private String longitude;        //经度
    private String latitude;         //纬度
    private String getTime;          //获取时间
    private String vehicleSpeed;     //车辆速度

    public SiteInfoBean(String str){
        number = "C00123"+str;
        containerNumber = "CNTL0013920919"+str;
        lockNumber = "GSBH13456"+str;
        pathNumber = "LJBH15662323"+str;
        startSite = "纳米比亚"+str;
        destSite = "金沙萨"+str;
        numberPlate = "KLL55652"+str;
        longitude = "110.55"+str;
        latitude = "35.98"+str;
        getTime = "2019-08-9-18:00"+str;
        vehicleSpeed = "120"+str;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    public String getLockNumber() {
        return lockNumber;
    }

    public void setLockNumber(String lockNumber) {
        this.lockNumber = lockNumber;
    }

    public String getPathNumber() {
        return pathNumber;
    }

    public void setPathNumber(String pathNumber) {
        this.pathNumber = pathNumber;
    }

    public String getStartSite() {
        return startSite;
    }

    public void setStartSite(String startSite) {
        this.startSite = startSite;
    }

    public String getDestSite() {
        return destSite;
    }

    public void setDestSite(String destSite) {
        this.destSite = destSite;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getGetTime() {
        return getTime;
    }

    public void setGetTime(String getTime) {
        this.getTime = getTime;
    }

    public String getVehicleSpeed() {
        return vehicleSpeed;
    }

    public void setVehicleSpeed(String vehicleSpeed) {
        this.vehicleSpeed = vehicleSpeed;
    }



}

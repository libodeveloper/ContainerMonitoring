package com.esri.arcgisruntime.container.monitoring.bean;

import java.util.List;

/**
 * Created by libo on 2019/4/18.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class LocationDetailsBean {


    /**
     * array : []
     * container_code : LEEtest
     * desSiteName : beijing
     * destination_site_id : 4aea47a268dbd9720168e736dc320064
     * dispatch_id :
     * endLat : 32.123456
     * endLng : 121.23457
     * geo_json :
     * gps_status : 0
     * is_locked : 1
     * is_open : 0
     * latitude : 31.311678
     * lauSiteName : shanghai
     * launch_site_id : 4aea47a268dbd9720168e74cd9a80087
     * lock_code : CNTL0024317336
     * lock_stick_status : 0
     * longitude : 120.869255
     * mid : C4
     * outer_status : 0
     * plate_number : 1234567
     * route_code : 1111
     * speed : 1.16676
     * startLat : 32.123456
     * startLng : 121.23457
     * state : 0
     * time : 2015-01-07 09:01:01
     */

//     "container_code": "集装箱编号",
//             "lock_code": "关锁编号",
//             "route_code": "路径编号",
//             "plate_number": "车牌号",
//             "launch_site_id": "启运站点主键",
//             "destination_site_id": "目的站点主键",
//             "longitude": "经度",
//             "latitude": "纬度",
//             "time": "获取时间",
//             "speed": "车辆速度",
//             "mid": "信息状态",
//             "lock_stick_status": "锁杆状态",
//             "outer_status": "外壳状态",
//             "gps_status": " gps状态",
//             "is_locked": "施封状态",
//             "is_open": "开锁状态",
//             "lauSiteName": "启运站点名称",
//             "desSiteName": "目的站点名称",
//             "geo_json": "路径的一些数据，轨迹点相关",
//             "startLng": "开始站点经度",
//             "startLat": "开始站点纬度",
//             "endLng": "目的站点经度",
//             "endLat": "目的站点纬度",
//             "state": 0,
//             "array": []


    private String container_code;      //集装箱编号
    private String lock_code;           //关锁编号
    private String route_code;          //路径编号
    private String lauSiteName;         //起点
    private String desSiteName;         //终点
    private String plate_number;        //车牌号
    private String longitude;           //经度
    private String latitude;            //纬度
    private String time;                //获取时间
    private String speed;               //车辆速度
    private String destination_site_id;
    private String dispatch_id;
    private String endLat;
    private String endLng;
    private String geo_json;
    private String gps_status;
    private String is_locked;
    private String is_open;
    private String launch_site_id;
    private String lock_stick_status;
    private String mid;
    private String outer_status;
    private String startLat;
    private String startLng;
    private int state;
    private List<?> array;

    public String getContainer_code() {
        return container_code;
    }

    public void setContainer_code(String container_code) {
        this.container_code = container_code;
    }

    public String getDesSiteName() {
        return desSiteName;
    }

    public void setDesSiteName(String desSiteName) {
        this.desSiteName = desSiteName;
    }

    public String getDestination_site_id() {
        return destination_site_id;
    }

    public void setDestination_site_id(String destination_site_id) {
        this.destination_site_id = destination_site_id;
    }

    public String getDispatch_id() {
        return dispatch_id;
    }

    public void setDispatch_id(String dispatch_id) {
        this.dispatch_id = dispatch_id;
    }

    public String getEndLat() {
        return endLat;
    }

    public void setEndLat(String endLat) {
        this.endLat = endLat;
    }

    public String getEndLng() {
        return endLng;
    }

    public void setEndLng(String endLng) {
        this.endLng = endLng;
    }

    public String getGeo_json() {
        return geo_json;
    }

    public void setGeo_json(String geo_json) {
        this.geo_json = geo_json;
    }

    public String getGps_status() {
        return gps_status;
    }

    public void setGps_status(String gps_status) {
        this.gps_status = gps_status;
    }

    public String getIs_locked() {
        return is_locked;
    }

    public void setIs_locked(String is_locked) {
        this.is_locked = is_locked;
    }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLauSiteName() {
        return lauSiteName;
    }

    public void setLauSiteName(String lauSiteName) {
        this.lauSiteName = lauSiteName;
    }

    public String getLaunch_site_id() {
        return launch_site_id;
    }

    public void setLaunch_site_id(String launch_site_id) {
        this.launch_site_id = launch_site_id;
    }

    public String getLock_code() {
        return lock_code;
    }

    public void setLock_code(String lock_code) {
        this.lock_code = lock_code;
    }

    public String getLock_stick_status() {
        return lock_stick_status;
    }

    public void setLock_stick_status(String lock_stick_status) {
        this.lock_stick_status = lock_stick_status;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getOuter_status() {
        return outer_status;
    }

    public void setOuter_status(String outer_status) {
        this.outer_status = outer_status;
    }

    public String getPlate_number() {
        return plate_number;
    }

    public void setPlate_number(String plate_number) {
        this.plate_number = plate_number;
    }

    public String getRoute_code() {
        return route_code;
    }

    public void setRoute_code(String route_code) {
        this.route_code = route_code;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getStartLat() {
        return startLat;
    }

    public void setStartLat(String startLat) {
        this.startLat = startLat;
    }

    public String getStartLng() {
        return startLng;
    }

    public void setStartLng(String startLng) {
        this.startLng = startLng;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<?> getArray() {
        return array;
    }

    public void setArray(List<?> array) {
        this.array = array;
    }
}

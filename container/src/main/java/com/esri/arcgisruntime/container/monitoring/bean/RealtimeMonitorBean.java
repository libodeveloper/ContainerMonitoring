package com.esri.arcgisruntime.container.monitoring.bean;

import java.util.List;

/**
 * Created by libo on 2019/3/30.
 */

public class RealtimeMonitorBean {

    /**
     * total : 2615
     * rows : [{"dispatch_id":"","container_code":"LEEtest","lock_code":"CNTL0024317336","route_code":"","plate_number":"","launch_site_id":"","destination_site_id":"","longitude":"120.869255","latitude":"31.480017","time":"","speed":"","mid":"","lock_stick_status":"","outer_status":"","gps_status":"","is_locked":"","is_open":"","lauSiteName":"","desSiteName":"","state":0}]
     */

    private int total;
    private List<RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * dispatch_id :
         * container_code : LEEtest
         * lock_code : CNTL0024317336
         * route_code :
         * plate_number :
         * launch_site_id :
         * destination_site_id :
         * longitude : 120.869255
         * latitude : 31.480017
         * time :
         * speed :
         * mid :
         * lock_stick_status :
         * outer_status :
         * gps_status :
         * is_locked :
         * is_open :
         * lauSiteName :
         * desSiteName :
         * state : 0
         */

        private String dispatch_id;
        private String container_code;
        private String lock_code;
        private String route_code;
        private String plate_number;
        private String launch_site_id;
        private String destination_site_id;
        private String longitude;
        private String latitude;
        private String time;
        private String speed;
        private String mid;
        private String lock_stick_status;
        private String outer_status;
        private String gps_status;
        private String is_locked;
        private String is_open;
        private String lauSiteName;
        private String desSiteName;
        private int state;

        public String getDispatch_id() {
            return dispatch_id;
        }

        public void setDispatch_id(String dispatch_id) {
            this.dispatch_id = dispatch_id;
        }

        public String getContainer_code() {
            return container_code;
        }

        public void setContainer_code(String container_code) {
            this.container_code = container_code;
        }

        public String getLock_code() {
            return lock_code;
        }

        public void setLock_code(String lock_code) {
            this.lock_code = lock_code;
        }

        public String getRoute_code() {
            return route_code;
        }

        public void setRoute_code(String route_code) {
            this.route_code = route_code;
        }

        public String getPlate_number() {
            return plate_number;
        }

        public void setPlate_number(String plate_number) {
            this.plate_number = plate_number;
        }

        public String getLaunch_site_id() {
            return launch_site_id;
        }

        public void setLaunch_site_id(String launch_site_id) {
            this.launch_site_id = launch_site_id;
        }

        public String getDestination_site_id() {
            return destination_site_id;
        }

        public void setDestination_site_id(String destination_site_id) {
            this.destination_site_id = destination_site_id;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public String getLock_stick_status() {
            return lock_stick_status;
        }

        public void setLock_stick_status(String lock_stick_status) {
            this.lock_stick_status = lock_stick_status;
        }

        public String getOuter_status() {
            return outer_status;
        }

        public void setOuter_status(String outer_status) {
            this.outer_status = outer_status;
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

        public String getLauSiteName() {
            return lauSiteName;
        }

        public void setLauSiteName(String lauSiteName) {
            this.lauSiteName = lauSiteName;
        }

        public String getDesSiteName() {
            return desSiteName;
        }

        public void setDesSiteName(String desSiteName) {
            this.desSiteName = desSiteName;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}

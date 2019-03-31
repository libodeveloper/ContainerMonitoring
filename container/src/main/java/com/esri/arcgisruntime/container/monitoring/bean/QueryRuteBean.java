package com.esri.arcgisruntime.container.monitoring.bean;

import java.util.List;

/**
 * Created by libo on 2019/3/30.
 */

public class QueryRuteBean {

    /**
     * total : 25
     * rows : [{"id":"","start_site_id":"402880b6691418530169141cbc8e000a","end_site_id":"","default_flag":"","siteName":"导入站点1","address":"站点地址","lng":"13.8","lat":"-8.8","path_no":"","geo_json":"","state":0,"array":null}]
     */

    private int total;
    private List<SiteBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SiteBean> getRows() {
        return rows;
    }

    public void setRows(List<SiteBean> rows) {
        this.rows = rows;
    }

    public static class SiteBean {
        /**
         * id :
         * start_site_id : 402880b6691418530169141cbc8e000a
         * end_site_id :
         * default_flag :
         * siteName : 导入站点1
         * address : 站点地址
         * lng : 13.8
         * lat : -8.8
         * path_no :
         * geo_json :
         * state : 0
         * array : null
         */

        private String id;
        private String start_site_id;
        private String end_site_id;
        private String default_flag;
        private String siteName;
        private String address;
        private String lng;
        private String lat;
        private String path_no;
        private String geo_json;
        private int state;
        private Object array;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStart_site_id() {
            return start_site_id;
        }

        public void setStart_site_id(String start_site_id) {
            this.start_site_id = start_site_id;
        }

        public String getEnd_site_id() {
            return end_site_id;
        }

        public void setEnd_site_id(String end_site_id) {
            this.end_site_id = end_site_id;
        }

        public String getDefault_flag() {
            return default_flag;
        }

        public void setDefault_flag(String default_flag) {
            this.default_flag = default_flag;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getPath_no() {
            return path_no;
        }

        public void setPath_no(String path_no) {
            this.path_no = path_no;
        }

        public String getGeo_json() {
            return geo_json;
        }

        public void setGeo_json(String geo_json) {
            this.geo_json = geo_json;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public Object getArray() {
            return array;
        }

        public void setArray(Object array) {
            this.array = array;
        }
    }
}

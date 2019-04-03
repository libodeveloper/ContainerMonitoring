package com.esri.arcgisruntime.container.monitoring.bean;

import java.util.List;

/**
 * Created by libo on 2019/3/30.
 */

public class QueryRuteResult {


    /**
     * total : 4
     * rows : [{"id":"4aea47a268dbd9720168eb89a4c001b8","start_site_id":"4aea47a268dbd9720168e74cd9a80087","end_site_id":"4aea47a268dbd9720168e736dc320064","default_flag":"1","siteName":"","address":"","lng":"","lat":"","path_no":"2233","geo_json":"已转换成数组","state":0,"array":[null]},{"id":"4aea47a268dbd9760168e79289ff00bd","start_site_id":"4aea47a268dbd9720168e74cd9a80087","end_site_id":"4aea47a268dbd9720168e736dc320064","default_flag":"0","siteName":"","address":"","lng":"","lat":"","path_no":"01","geo_json":"已转换成数组","state":0,"array":["","","","1471981.8400273055,-984187.713836475",",","1477087.9288064397,-985204.3262543497",",","1477898.1617727962,-982888.2843556288","","","","",""]}]
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
         * address :
         * array : [{"lat":"-984187.713836475","lng":"1471981.8400273055"},{"lat":"-985204.3262543497","lng":"1477087.9288064397"},{"lat":"-982888.2843556288","lng":"1477898.1617727962"}]
         * default_flag : 0
         * end_site_id : 4aea47a268dbd9720168e736dc320064
         * geo_json :
         * id : 4aea47a268dbd9760168e79289ff00bd
         * lat :
         * lng :
         * path_no : 01
         * siteName :
         * start_site_id : 4aea47a268dbd9720168e74cd9a80087
         * state : 0
         */

        private String address;
        private String default_flag;
        private String end_site_id;
        private String geo_json;
        private String id;
        private String lat;
        private String lng;
        private String path_no;
        private String siteName;
        private String start_site_id;
        private int state;
        private List<ArrayBean> array;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDefault_flag() {
            return default_flag;
        }

        public void setDefault_flag(String default_flag) {
            this.default_flag = default_flag;
        }

        public String getEnd_site_id() {
            return end_site_id;
        }

        public void setEnd_site_id(String end_site_id) {
            this.end_site_id = end_site_id;
        }

        public String getGeo_json() {
            return geo_json;
        }

        public void setGeo_json(String geo_json) {
            this.geo_json = geo_json;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getPath_no() {
            return path_no;
        }

        public void setPath_no(String path_no) {
            this.path_no = path_no;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public String getStart_site_id() {
            return start_site_id;
        }

        public void setStart_site_id(String start_site_id) {
            this.start_site_id = start_site_id;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public List<ArrayBean> getArray() {
            return array;
        }

        public void setArray(List<ArrayBean> array) {
            this.array = array;
        }

        public static class ArrayBean {
            /**
             * lat : -984187.713836475
             * lng : 1471981.8400273055
             */

            private String lat;
            private String lng;

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLng() {
                return lng;
            }

            public void setLng(String lng) {
                this.lng = lng;
            }
        }
    }
}

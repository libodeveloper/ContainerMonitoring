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
         * id : 4aea47a268dbd9720168eb89a4c001b8
         * start_site_id : 4aea47a268dbd9720168e74cd9a80087
         * end_site_id : 4aea47a268dbd9720168e736dc320064
         * default_flag : 1
         * siteName :
         * address :
         * lng :
         * lat :
         * path_no : 2233
         * geo_json : 已转换成数组
         * state : 0
         * array :  [
         "",
         "1471981.8400273055,-984187.713836475",
         ",",
         "1477087.9288064397,-985204.3262543497",
         ",",
         "1477898.1617727962,-982888.2843556288",
         "",
         ""
         ]
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
        private List<String> array;

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

        public List<String> getArray() {
            return array;
        }

        public void setArray(List<String> array) {
            this.array = array;
        }
    }
}

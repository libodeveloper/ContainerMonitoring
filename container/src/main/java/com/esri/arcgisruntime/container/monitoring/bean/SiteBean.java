package com.esri.arcgisruntime.container.monitoring.bean;

import java.util.List;

/**
 * Created by libo on 2019/4/1.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 业务查询站点
 */
public class SiteBean {


    /**
     * total : 27
     * rows : [{"dataCount":0,"pageSize":20,"pageNo":1,"startRecord":0,"endRecord":0,"pageCount":0,"levelCode":0,"totalSize":0,"page":0,"rows":20,"order":"","sort":"","searchString":"","id":"","lock_code":"","destinationName":"导入站点1","launchName":"","destName":"","route_code":"","launch_time":"","arrive_time":"","num":"","state":0,"startNo":0,"endNo":0}]
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
         * dataCount : 0
         * pageSize : 20
         * pageNo : 1
         * startRecord : 0
         * endRecord : 0
         * pageCount : 0
         * levelCode : 0
         * totalSize : 0
         * page : 0
         * rows : 20
         * order :
         * sort :
         * searchString :
         * id :
         * lock_code :
         * destinationName : 导入站点1
         * launchName :
         * destName :
         * route_code :
         * launch_time :
         * arrive_time :
         * num :
         * state : 0
         * startNo : 0
         * endNo : 0
         */

        private int dataCount;
        private int pageSize;
        private int pageNo;
        private int startRecord;
        private int endRecord;
        private int pageCount;
        private int levelCode;
        private int totalSize;
        private int page;
        private int rows;
        private String order;
        private String sort;
        private String searchString;
        private String id;
        private String lock_code;
        private String destinationName;   //站点名称
        private String launchName;
        private String destName;
        private String route_code;
        private String launch_time;
        private String arrive_time;
        private String num;
        private int state;
        private int startNo;
        private int endNo;

        public int getDataCount() {
            return dataCount;
        }

        public void setDataCount(int dataCount) {
            this.dataCount = dataCount;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getStartRecord() {
            return startRecord;
        }

        public void setStartRecord(int startRecord) {
            this.startRecord = startRecord;
        }

        public int getEndRecord() {
            return endRecord;
        }

        public void setEndRecord(int endRecord) {
            this.endRecord = endRecord;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getLevelCode() {
            return levelCode;
        }

        public void setLevelCode(int levelCode) {
            this.levelCode = levelCode;
        }

        public int getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(int totalSize) {
            this.totalSize = totalSize;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getSearchString() {
            return searchString;
        }

        public void setSearchString(String searchString) {
            this.searchString = searchString;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLock_code() {
            return lock_code;
        }

        public void setLock_code(String lock_code) {
            this.lock_code = lock_code;
        }

        public String getDestinationName() {
            return destinationName;
        }

        public void setDestinationName(String destinationName) {
            this.destinationName = destinationName;
        }

        public String getLaunchName() {
            return launchName;
        }

        public void setLaunchName(String launchName) {
            this.launchName = launchName;
        }

        public String getDestName() {
            return destName;
        }

        public void setDestName(String destName) {
            this.destName = destName;
        }

        public String getRoute_code() {
            return route_code;
        }

        public void setRoute_code(String route_code) {
            this.route_code = route_code;
        }

        public String getLaunch_time() {
            return launch_time;
        }

        public void setLaunch_time(String launch_time) {
            this.launch_time = launch_time;
        }

        public String getArrive_time() {
            return arrive_time;
        }

        public void setArrive_time(String arrive_time) {
            this.arrive_time = arrive_time;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getStartNo() {
            return startNo;
        }

        public void setStartNo(int startNo) {
            this.startNo = startNo;
        }

        public int getEndNo() {
            return endNo;
        }

        public void setEndNo(int endNo) {
            this.endNo = endNo;
        }
    }
}

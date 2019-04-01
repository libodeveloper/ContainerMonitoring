package com.esri.arcgisruntime.container.monitoring.bean;

import java.util.List;

/**
 * Created by libo on 2019/3/31.
 */

public class BusinessQueryResultBean {

    private int total;

    private List<RowsBean> rows;

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class RowsBean {
        /**
         * arrive_time :
         * dataCount : 0
         * destName :
         * destinationName : beijing
         * endNo : 0
         * endRecord : 0
         * id :
         * launchName :
         * launch_time :
         * levelCode : 0
         * lock_code : CNTL0023869702
         * num : 1
         * order :
         * page : 0
         * pageCount : 0
         * pageNo : 1
         * pageSize : 20
         * route_code :
         * rows : 20
         * searchString :
         * sort :
         * startNo : 0
         * startRecord : 0
         * state : 0
         * totalSize : 0
         */

        private String arrive_time;
        private int dataCount;
        private String destName;
        private String destinationName;   //所属站点
        private int endNo;
        private int endRecord;
        private String id;
        private String launchName;
        private String launch_time;
        private int levelCode;
        private String lock_code;     //关锁编号
        private String num;           //次数
        private String order;
        private int page;
        private int pageCount;
        private int pageNo;
        private int pageSize;
        private String route_code;
        private int rows;
        private String searchString;
        private String sort;
        private int startNo;
        private int startRecord;
        private int state;
        private int totalSize;

        private int seniority;      //排行

        public int getSeniority() {
            return seniority;
        }

        public void setSeniority(int seniority) {
            this.seniority = seniority;
        }

        public String getArrive_time() {
            return arrive_time;
        }

        public void setArrive_time(String arrive_time) {
            this.arrive_time = arrive_time;
        }

        public int getDataCount() {
            return dataCount;
        }

        public void setDataCount(int dataCount) {
            this.dataCount = dataCount;
        }

        public String getDestName() {
            return destName;
        }

        public void setDestName(String destName) {
            this.destName = destName;
        }

        public String getDestinationName() {
            return destinationName;
        }

        public void setDestinationName(String destinationName) {
            this.destinationName = destinationName;
        }

        public int getEndNo() {
            return endNo;
        }

        public void setEndNo(int endNo) {
            this.endNo = endNo;
        }

        public int getEndRecord() {
            return endRecord;
        }

        public void setEndRecord(int endRecord) {
            this.endRecord = endRecord;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLaunchName() {
            return launchName;
        }

        public void setLaunchName(String launchName) {
            this.launchName = launchName;
        }

        public String getLaunch_time() {
            return launch_time;
        }

        public void setLaunch_time(String launch_time) {
            this.launch_time = launch_time;
        }

        public int getLevelCode() {
            return levelCode;
        }

        public void setLevelCode(int levelCode) {
            this.levelCode = levelCode;
        }

        public String getLock_code() {
            return lock_code;
        }

        public void setLock_code(String lock_code) {
            this.lock_code = lock_code;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getPageNo() {
            return pageNo;
        }

        public void setPageNo(int pageNo) {
            this.pageNo = pageNo;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public String getRoute_code() {
            return route_code;
        }

        public void setRoute_code(String route_code) {
            this.route_code = route_code;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public String getSearchString() {
            return searchString;
        }

        public void setSearchString(String searchString) {
            this.searchString = searchString;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public int getStartNo() {
            return startNo;
        }

        public void setStartNo(int startNo) {
            this.startNo = startNo;
        }

        public int getStartRecord() {
            return startRecord;
        }

        public void setStartRecord(int startRecord) {
            this.startRecord = startRecord;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public int getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(int totalSize) {
            this.totalSize = totalSize;
        }
    }
}

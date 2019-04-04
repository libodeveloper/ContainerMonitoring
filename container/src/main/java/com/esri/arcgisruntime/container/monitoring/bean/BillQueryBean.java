package com.esri.arcgisruntime.container.monitoring.bean;

import java.util.List;

/**
 * Created by libo on 2019/3/7.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 单据查询数据
 */
public class BillQueryBean {


    /**
     * rows : [{"arrive_time":"","container_code":"CIXU3197513","container_color":"","container_specifications":"","custom_code":"1","dataCount":0,"du":"","endNo":0,"endRecord":0,"id":"","import_time":"","launch_time":"","levelCode":0,"loading_port":"","moeda":"","order":"","page":0,"pageCount":0,"pageNo":1,"pageSize":20,"pesobruto":"","pesoquido":"","pod":"","price":"","product_name":"","quantity":"","rdia":"","rows":20,"searchString":"","sort":"","startNo":0,"startRecord":0,"state":0,"total":"","totalSize":0,"transporttool":"","warn_time":"","yy":""}]
     * total : 1
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
         * arrive_time :
         * container_code : CIXU3197513
         * custom_code : 1
         * dataCount : 0
         * endNo : 0
         * endRecord : 0
         * launch_time :
         * levelCode : 0
         * order :
         * page : 0
         * pageCount : 0
         * pageNo : 1
         * pageSize : 20
         * rows : 20
         * searchString :
         * sort :
         * startNo : 0
         * startRecord : 0
         * state : 0
         * totalSize : 0
         */

        private String arrive_time;
        private String container_code;
        private String custom_code;
        private int dataCount;
        private int endNo;
        private int endRecord;
        private String launch_time;
        private int levelCode;
        private String order;
        private int page;
        private int pageCount;
        private int pageNo;
        private int pageSize;
        private int rows;
        private String searchString;
        private String sort;
        private int startNo;
        private int startRecord;
        private int state;
        private int totalSize;

        private String sequenceNumbe;    //序号

        public String getSequenceNumbe() {
            return sequenceNumbe;
        }

        public void setSequenceNumbe(String sequenceNumbe) {
            this.sequenceNumbe = sequenceNumbe;
        }

        public String getArrive_time() {
            return arrive_time;
        }

        public void setArrive_time(String arrive_time) {
            this.arrive_time = arrive_time;
        }

        public String getContainer_code() {
            return container_code;
        }

        public void setContainer_code(String container_code) {
            this.container_code = container_code;
        }

        public String getCustom_code() {
            return custom_code;
        }

        public void setCustom_code(String custom_code) {
            this.custom_code = custom_code;
        }

        public int getDataCount() {
            return dataCount;
        }

        public void setDataCount(int dataCount) {
            this.dataCount = dataCount;
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

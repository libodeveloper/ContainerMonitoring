package com.esri.arcgisruntime.container.monitoring.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by libo on 2019/4/18.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class SearchNumberBean implements Serializable{

    /**
     * rows : [{"container_code":"LEEtest","latitude":"","lock_code":"CNTL0024317336","longitude":"","state":0}]
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

    public static class RowsBean implements Serializable{
        /**
         * container_code : LEEtest
         * latitude :
         * lock_code : CNTL0024317336
         * longitude :
         * state : 0
         */

        private String container_code;
        private String latitude;
        private String lock_code;
        private String longitude;
        private int state;

        public String getContainer_code() {
            return container_code;
        }

        public void setContainer_code(String container_code) {
            this.container_code = container_code;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLock_code() {
            return lock_code;
        }

        public void setLock_code(String lock_code) {
            this.lock_code = lock_code;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }
    }
}

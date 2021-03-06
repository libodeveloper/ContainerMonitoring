package com.esri.arcgisruntime.container.monitoring.bean;


/**
 * Created by libo on 2019/3/7.
 * 业务查询结果
 */
public class BusinessQueryBean  {


    private int seniority;      //排行
    private String locknumber;  //关锁编号
    private String site;        //所属站点
    private String times;       //次数

    public BusinessQueryBean(int seniority, String locknumber, String site, String times) {
        this.seniority = seniority;
        this.locknumber = locknumber;
        this.site = site;
        this.times = times;
    }

    public int getSeniority() {
        return seniority;
    }

    public void setSeniority(int seniority) {
        this.seniority = seniority;
    }

    public String getLocknumber() {
        return locknumber;
    }

    public void setLocknumber(String locknumber) {
        this.locknumber = locknumber;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }


}

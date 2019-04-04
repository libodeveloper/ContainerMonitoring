package com.esri.arcgisruntime.container.monitoring.bean;

/**
 * Created by libo on 2019/3/31.
 * 单据详情
 */

public class BillDetailsBean {

//    String container_code /** 集装箱编号 */
//    String custom_code /** 报关单据编号 */
//    String dun /** 提单号*/
//    String yy /** 提单数量 */
//    String container_specifications /** 集装箱类型 */
//    String duType /** 提单类型 */
//    String pod /** 起运国 */（出发地点）
//    String sUserId /** 施封人员代码 */
//    String midSite /** 中转站点 */
//    String transporttool /** 交通工具 */（船名）
//    String product_name /** 商品名称*/（商品说明）
//    String moeda /** 币制 */（货币）
//    String space /** 转运地 */
//    String quantity /** 货物数量 */
//    String total /** 总价 */（申报价值）
//    String pesoquido /** 净重 */（净吨位）
//    String pesobruto /** 总毛重 */
//    String midNo /** 中转单号 */
//    String container_status /** 集装箱状态 */
//    String launch_time /** 出发日期 */
//    String arrive_time /** 到达日期 */
//    String lock_code /** 关锁编号 */
//    String domesticdestination /** 境内目的地 */（抵达地点）
//    String transportcompany /** 运输公司 */
//    String creation_time /** 物流注册日期 */（仓单注册日期）
//    String despachante /** 清关代理 */(船运代理)
//    String origin /** 原产国 */
//    String destination /** 最终目的国 */（卸载地点）
//    String transport_code /** 航运号码 */
//    String snum  /** 数量 */
//    String country  /** 船舶国籍 */
//    String st  /** 总吨位 */
//    String cnum /** 集装箱数量 */
//    String importer /** 进口商名 */
//    String deleted_flag /** 删除标志*/（提单状态）
//    String yLockCode /** 原关锁号 */
//    String tuNo /** 转运单号 */


    private String arrive_time;
    private String cnum;
    private String container_code;
    private String container_specifications;
    private String container_status;
    private String country;
    private String creation_time;
    private String custom_code;
    private String deleted_flag;
    private String despachante;
    private String destination;
    private String domesticdestination;
    private String duType;
    private String dun;
    private String importer;
    private String launch_time;
    private String lock_code;
    private String midNo;
    private String midSite;
    private String moeda;
    private String origin;
    private String pesobruto;
    private String pesoquido;
    private String pod;
    private String product_name;
    private String quantity;
    private String snum;
    private String space;
    private String st;
    private int state;
    private String suserId;
    private String total;
    private String transport_code;
    private String transportcompany;
    private String transporttool;
    private String tuNo;
    private String ylockCode;
    private String yy;

    public String getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time(String arrive_time) {
        this.arrive_time = arrive_time;
    }

    public String getCnum() {
        return cnum;
    }

    public void setCnum(String cnum) {
        this.cnum = cnum;
    }

    public String getContainer_code() {
        return container_code;
    }

    public void setContainer_code(String container_code) {
        this.container_code = container_code;
    }

    public String getContainer_specifications() {
        return container_specifications;
    }

    public void setContainer_specifications(String container_specifications) {
        this.container_specifications = container_specifications;
    }

    public String getContainer_status() {
        return container_status;
    }

    public void setContainer_status(String container_status) {
        this.container_status = container_status;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(String creation_time) {
        this.creation_time = creation_time;
    }

    public String getCustom_code() {
        return custom_code;
    }

    public void setCustom_code(String custom_code) {
        this.custom_code = custom_code;
    }

    public String getDeleted_flag() {
        return deleted_flag;
    }

    public void setDeleted_flag(String deleted_flag) {
        this.deleted_flag = deleted_flag;
    }

    public String getDespachante() {
        return despachante;
    }

    public void setDespachante(String despachante) {
        this.despachante = despachante;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDomesticdestination() {
        return domesticdestination;
    }

    public void setDomesticdestination(String domesticdestination) {
        this.domesticdestination = domesticdestination;
    }

    public String getDuType() {
        return duType;
    }

    public void setDuType(String duType) {
        this.duType = duType;
    }

    public String getDun() {
        return dun;
    }

    public void setDun(String dun) {
        this.dun = dun;
    }

    public String getImporter() {
        return importer;
    }

    public void setImporter(String importer) {
        this.importer = importer;
    }

    public String getLaunch_time() {
        return launch_time;
    }

    public void setLaunch_time(String launch_time) {
        this.launch_time = launch_time;
    }

    public String getLock_code() {
        return lock_code;
    }

    public void setLock_code(String lock_code) {
        this.lock_code = lock_code;
    }

    public String getMidNo() {
        return midNo;
    }

    public void setMidNo(String midNo) {
        this.midNo = midNo;
    }

    public String getMidSite() {
        return midSite;
    }

    public void setMidSite(String midSite) {
        this.midSite = midSite;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPesobruto() {
        return pesobruto;
    }

    public void setPesobruto(String pesobruto) {
        this.pesobruto = pesobruto;
    }

    public String getPesoquido() {
        return pesoquido;
    }

    public void setPesoquido(String pesoquido) {
        this.pesoquido = pesoquido;
    }

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSnum() {
        return snum;
    }

    public void setSnum(String snum) {
        this.snum = snum;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getSt() {
        return st;
    }

    public void setSt(String st) {
        this.st = st;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSuserId() {
        return suserId;
    }

    public void setSuserId(String suserId) {
        this.suserId = suserId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTransport_code() {
        return transport_code;
    }

    public void setTransport_code(String transport_code) {
        this.transport_code = transport_code;
    }

    public String getTransportcompany() {
        return transportcompany;
    }

    public void setTransportcompany(String transportcompany) {
        this.transportcompany = transportcompany;
    }

    public String getTransporttool() {
        return transporttool;
    }

    public void setTransporttool(String transporttool) {
        this.transporttool = transporttool;
    }

    public String getTuNo() {
        return tuNo;
    }

    public void setTuNo(String tuNo) {
        this.tuNo = tuNo;
    }

    public String getYlockCode() {
        return ylockCode;
    }

    public void setYlockCode(String ylockCode) {
        this.ylockCode = ylockCode;
    }

    public String getYy() {
        return yy;
    }

    public void setYy(String yy) {
        this.yy = yy;
    }
}

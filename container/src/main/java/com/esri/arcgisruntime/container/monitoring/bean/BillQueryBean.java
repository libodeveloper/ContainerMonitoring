package com.esri.arcgisruntime.container.monitoring.bean;

/**
 * Created by libo on 2019/3/7.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 单据查询数据
 */
public class BillQueryBean {

  private String sequenceNumbe;    //序号
  private String containerNumber;  //集装箱编号
  private String orderNumber;      //单据编号

    public BillQueryBean(String sequenceNumbe, String containerNumber, String orderNumber) {
        this.sequenceNumbe = sequenceNumbe;
        this.containerNumber = containerNumber;
        this.orderNumber = orderNumber;
    }

    public String getSequenceNumbe() {
        return sequenceNumbe;
    }

    public void setSequenceNumbe(String sequenceNumbe) {
        this.sequenceNumbe = sequenceNumbe;
    }

    public String getContainerNumber() {
        return containerNumber;
    }

    public void setContainerNumber(String containerNumber) {
        this.containerNumber = containerNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }



}

package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.bean.BillDetailsBean;
import com.esri.arcgisruntime.container.monitoring.presenter.BillDetailsPresenter;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
import com.esri.arcgisruntime.container.monitoring.view.BillDetailsLinearLayout;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IBillDetails;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/3/6.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 单据查询结果详情
 */
public class BillQueryDetailsActivity extends BaseActivity implements IBillDetails {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    BillDetailsPresenter billDetailsPresenter;
    @BindView(R.id.blContainerType)
    BillDetailsLinearLayout blContainerType;
    @BindView(R.id.blContainerNumber)
    BillDetailsLinearLayout blContainerNumber;
    @BindView(R.id.blContainerStatus)
    BillDetailsLinearLayout blContainerStatus;
    @BindView(R.id.blLockNumber)
    BillDetailsLinearLayout blLockNumber;
    @BindView(R.id.blShippingNumber)
    BillDetailsLinearLayout blShippingNumber;
    @BindView(R.id.blDateRegis)
    BillDetailsLinearLayout blDateRegis;
    @BindView(R.id.blDepartureDate)
    BillDetailsLinearLayout blDepartureDate;
    @BindView(R.id.blArrivalDate)
    BillDetailsLinearLayout blArrivalDate;
    @BindView(R.id.blCodeDeparture)
    BillDetailsLinearLayout blCodeDeparture;
    @BindView(R.id.blCodeArrival)
    BillDetailsLinearLayout blCodeArrival;
    @BindView(R.id.blTransportadora)
    BillDetailsLinearLayout blTransportadora;
    @BindView(R.id.blShippingAgency)
    BillDetailsLinearLayout blShippingAgency;
    @BindView(R.id.blTotalTD)
    BillDetailsLinearLayout blTotalTD;
    @BindView(R.id.blNumberVolumes)
    BillDetailsLinearLayout blNumberVolumes;
    @BindView(R.id.blTotalGrossWeight)
    BillDetailsLinearLayout blTotalGrossWeight;
    @BindView(R.id.blVesselNationality)
    BillDetailsLinearLayout blVesselNationality;
    @BindView(R.id.blVesselName)
    BillDetailsLinearLayout blVesselName;
    @BindView(R.id.blGrossTonnage)
    BillDetailsLinearLayout blGrossTonnage;
    @BindView(R.id.blNetTonnage)
    BillDetailsLinearLayout blNetTonnage;
    @BindView(R.id.blContainers)
    BillDetailsLinearLayout blContainers;
    @BindView(R.id.blNumber)
    BillDetailsLinearLayout blNumber;
    @BindView(R.id.blType)
    BillDetailsLinearLayout blType;
    @BindView(R.id.blPlaceUnloading)
    BillDetailsLinearLayout blPlaceUnloading;
    @BindView(R.id.blImporterName)
    BillDetailsLinearLayout blImporterName;
    @BindView(R.id.blGoodsDescription)
    BillDetailsLinearLayout blGoodsDescription;
    @BindView(R.id.blGoodsOrign)
    BillDetailsLinearLayout blGoodsOrign;
    @BindView(R.id.blCustomsValue)
    BillDetailsLinearLayout blCustomsValue;
    @BindView(R.id.blCurrency)
    BillDetailsLinearLayout blCurrency;
    @BindView(R.id.blSealNumber)
    BillDetailsLinearLayout blSealNumber;
    @BindView(R.id.blCodeResponsible)
    BillDetailsLinearLayout blCodeResponsible;
    @BindView(R.id.blBlstatus)
    BillDetailsLinearLayout blBlstatus;
    @BindView(R.id.blVolumesDescription)
    BillDetailsLinearLayout blVolumesDescription;
    @BindView(R.id.blUngroupedNumber)
    BillDetailsLinearLayout blUngroupedNumber;
    @BindView(R.id.blCustomsOfficeTraffic)
    BillDetailsLinearLayout blCustomsOfficeTraffic;
    @BindView(R.id.blTranshipmentPlace)
    BillDetailsLinearLayout blTranshipmentPlace;
    @BindView(R.id.blTransitDocument)
    BillDetailsLinearLayout blTransitDocument;
    @BindView(R.id.blReferenceTransbord)
    BillDetailsLinearLayout blReferenceTransbord;
    private String custom_code;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bill_query_details);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        custom_code = getIntent().getStringExtra("custom_code");
        billDetailsPresenter = new BillDetailsPresenter(this);
        billDetailsPresenter.billDetails(getParams());

    }

    private Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("custom_code", custom_code);
        params = MD5Utils.encryptParams(params);
        return params;

    }


    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }


    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }
        return new String(c);
    }


    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void billDetailSucceed(BillDetailsBean billDetailsBean) {
        if (billDetailsBean != null) {
            //集装箱信息
            blContainerType.setValue(billDetailsBean.getContainer_specifications()); //集装箱类型
            blContainerNumber.setValue(billDetailsBean.getContainer_code()); //集装箱编号
            blContainerStatus.setValue(billDetailsBean.getContainer_status()); //集装箱状态
            blLockNumber.setValue(billDetailsBean.getLock_code()); // 关锁编号

            //物流信息
            blShippingNumber.setValue(billDetailsBean.getTransport_code()); //航运号码
            blDepartureDate.setValue(billDetailsBean.getLaunch_time()); //出发日期
            blArrivalDate.setValue(billDetailsBean.getArrive_time()); //达到日期
            blDateRegis.setValue(billDetailsBean.getCreation_time()); //仓单注册日期
//            blCodeDeparture.setValue(billDetailsBean.getcode); //出发地点代码
//            blCodeArrival.setValue(billDetailsBean.getcode);   //抵达地点代码
            blTransportadora.setValue(billDetailsBean.getTransportcompany()); //运输公司
            blShippingAgency.setValue(billDetailsBean.getDespachante()); //船运代理
            blTotalTD.setValue(billDetailsBean.getYy());//提单数量
            blNumberVolumes.setValue(billDetailsBean.getSnum());//数量
            blTotalGrossWeight.setValue(billDetailsBean.getPesobruto());//总毛重
            blVesselNationality.setValue(billDetailsBean.getCountry()); //船舶国籍
//            blVesselName.setValue(billDetailsBean.get); //船名
            blGrossTonnage.setValue(billDetailsBean.getSt()); //总吨位
            blNetTonnage.setValue(billDetailsBean.getPesoquido()); //净吨位
            blContainers.setValue(billDetailsBean.getCnum()); //集装箱数量

            //货物信息
            blNumber.setValue(billDetailsBean.getDun()); //提单号
            blType.setValue(billDetailsBean.getDuType()); //提单类型
            blPlaceUnloading.setValue(billDetailsBean.getDestination());//卸载地点
            blImporterName.setValue(billDetailsBean.getImporter());//进口商名
            blGoodsDescription.setValue(billDetailsBean.getProduct_name());//商品说明
            blGoodsOrign.setValue(billDetailsBean.getOrigin()); //原产国
            blCustomsValue.setValue(billDetailsBean.getTotal()); //申报价值
            blCurrency.setValue(billDetailsBean.getMoeda());//货币
            blSealNumber.setValue(billDetailsBean.getYlockCode());//原关锁号
            blCodeResponsible.setValue(billDetailsBean.getSuserId()); //施封人员代码
            blBlstatus.setValue(billDetailsBean.getDeleted_flag());//提单状态
            blVolumesDescription.setValue(billDetailsBean.getQuantity());//货物数量
            blUngroupedNumber.setValue(billDetailsBean.getYy()); //提单数量
            blCustomsOfficeTraffic.setValue(billDetailsBean.getMidSite());//中转站点
            blTranshipmentPlace.setValue(billDetailsBean.getSpace());//转运地
            blTransitDocument.setValue(billDetailsBean.getMidNo());//中转单号
            blReferenceTransbord.setValue(billDetailsBean.getTuNo());//转运单号
        }
    }

}

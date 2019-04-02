package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.bean.BillDetailsBean;
import com.esri.arcgisruntime.container.monitoring.presenter.BillDetailsPresenter;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
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
    @BindView(R.id.container_type)
    TextView containerType;
    @BindView(R.id.container_number)
    TextView containerNumber;
    @BindView(R.id.container_status)
    TextView containerStatus;
    @BindView(R.id.lock_number)
    TextView lockNumber;
    @BindView(R.id.shipping_number)
    TextView shippingNumber;
    @BindView(R.id.departure_date)
    TextView departureDate;
    @BindView(R.id.arrival_date)
    TextView arrivalDate;
    @BindView(R.id.dateRm)
    TextView dateRm;
    @BindView(R.id.code_of_departure)
    TextView codeOfDeparture;
    @BindView(R.id.code_of_arrival)
    TextView codeOfArrival;
    @BindView(R.id.transportadora)
    TextView transportadora;
    @BindView(R.id.shipping_agency)
    TextView shippingAgency;
    @BindView(R.id.total_transport_documents)
    TextView totalTransportDocuments;
    @BindView(R.id.number_of_volumes)
    TextView numberOfVolumes;
    @BindView(R.id.total_gross_weight)
    TextView totalGrossWeight;
    @BindView(R.id.vessel_nationality)
    TextView vesselNationality;
    @BindView(R.id.vessel_name)
    TextView vesselName;
    @BindView(R.id.gross_tonnage)
    TextView grossTonnage;
    @BindView(R.id.net_tonnage)
    TextView netTonnage;
    @BindView(R.id.containers)
    TextView containers;
    @BindView(R.id.bl_number)
    TextView blNumber;
    @BindView(R.id.bl_type)
    TextView blType;
    @BindView(R.id.place_of_unloading)
    TextView placeOfUnloading;
    @BindView(R.id.importer_name)
    TextView importerName;
    @BindView(R.id.goods_description)
    TextView goodsDescription;
    @BindView(R.id.goods_origin)
    TextView goodsOrigin;
    @BindView(R.id.customs_value)
    TextView customsValue;
    @BindView(R.id.currency)
    TextView currency;
    @BindView(R.id.seal_number)
    TextView sealNumber;
    @BindView(R.id.code_responsible)
    TextView codeResponsible;
    @BindView(R.id.bl_status)
    TextView blStatus;
    @BindView(R.id.volumes_description)
    TextView volumesDescription;
    @BindView(R.id.bl_ungrouped_number)
    TextView blUngroupedNumber;
    @BindView(R.id.customs_office_of_traffic)
    TextView customsOfficeOfTraffic;
    @BindView(R.id.transhipment_place)
    TextView transhipmentPlace;
    @BindView(R.id.transit_document)
    TextView transitDocument;
    @BindView(R.id.reference_transbord)
    TextView referenceTransbord;
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

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void billDetailSucceed(BillDetailsBean billDetailsBean) {
        if (billDetailsBean!=null){
            containerNumber.setText(billDetailsBean.getContainer_code());
            lockNumber.setText(billDetailsBean.getCustom_code());
        }
    }

}

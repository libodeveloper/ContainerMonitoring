package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.bean.BillDetailsBean;
import com.esri.arcgisruntime.container.monitoring.presenter.BillDetailsPresenter;
import com.esri.arcgisruntime.container.monitoring.utils.BuilderParams;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IBillDetails;

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

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bill_query_details);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        billDetailsPresenter = new BillDetailsPresenter(this);
        billDetailsPresenter.billDetails(getParams());
    }

    private Map<String, String> getParams() {
        Map<String, String> params = new BuilderParams()
//                                    .addParams()
//                                    .addParams()
//                                    .addParams()
                .returnParams();

        return params;

    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void billDetailSucceed(BillDetailsBean billDetailsBean) {

    }
}

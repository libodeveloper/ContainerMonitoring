package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/3/6.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 单据查询结果详情
 */
public class BillQueryDetailsActivity extends BaseActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bill_query_details);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }
}

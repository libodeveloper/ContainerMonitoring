package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.adapter.BillQueryAdapter;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.bean.BillQueryBean;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/3/6.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 单据查询结果
 */
public class BillQueryResultActivity extends BaseActivity {


    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.rvBillQueryResult)
    RecyclerView rvBillQueryResult;

    private BillQueryAdapter billQueryAdapter;
    private ArrayList<BillQueryBean> billQueryList;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bill_query_result);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        billQueryList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            BillQueryBean billQueryBean = new BillQueryBean("001","ADWQ2284664200","AB123456");
            billQueryList.add(billQueryBean);
        }

        billQueryAdapter = new BillQueryAdapter(this,billQueryList);
        rvBillQueryResult.setLayoutManager(new LinearLayoutManager(this));
        rvBillQueryResult.setAdapter(billQueryAdapter);
        billQueryAdapter.setOnItemClickListener(new BillQueryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Intent intent = new Intent(BillQueryResultActivity.this,BillQueryDetailsActivity.class);
                startActivity(intent);
            }
        });
    }


    @OnClick({R.id.ivBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }
}

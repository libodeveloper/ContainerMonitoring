package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.adapter.BusinessQueryResultAdapter;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.bean.BusinessQueryBean;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/3/6.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 业务查询结果
 */
public class BusinessQueryResultActivity extends BaseActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.rlSortAscending)
    RelativeLayout rlSortAscending;
    @BindView(R.id.rlGradeDown)
    RelativeLayout rlGradeDown;
    @BindView(R.id.rvBusinessQueryResult)
    RecyclerView rvBusinessQueryResult;
    BusinessQueryResultAdapter businessQueryResultAdapter;

    ArrayList<BusinessQueryBean> businessQueryList;
    @BindView(R.id.vwSortAscending)
    View vwSortAscending;
    @BindView(R.id.vwGradeDown)
    View vwGradeDown;
    @BindView(R.id.tvSortAscending)
    TextView tvSortAscending;
    @BindView(R.id.tvGradeDown)
    TextView tvGradeDown;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_business_query_result);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        businessQueryList = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            int temp = i + 1;
            BusinessQueryBean businessQueryBean = new BusinessQueryBean(temp, "CNTL0013920919", "10Letters", temp * 100 + "");
            businessQueryList.add(businessQueryBean);
        }

        businessQueryResultAdapter = new BusinessQueryResultAdapter(this, businessQueryList);
        rvBusinessQueryResult.setLayoutManager(new LinearLayoutManager(this));
        rvBusinessQueryResult.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvBusinessQueryResult.setAdapter(businessQueryResultAdapter);

    }


    @OnClick({R.id.ivBack, R.id.rlSortAscending, R.id.rlGradeDown})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.rlSortAscending:  //升序
                vwSortAscending.setVisibility(View.VISIBLE);
                vwGradeDown.setVisibility(View.GONE);
                tvSortAscending.setTextColor(getResources().getColor(R.color.black));
                tvGradeDown.setTextColor(getResources().getColor(R.color.gray));
                MyToast.showShort("升序");
                break;
            case R.id.rlGradeDown:   //降序
                vwSortAscending.setVisibility(View.GONE);
                vwGradeDown.setVisibility(View.VISIBLE);
                tvSortAscending.setTextColor(getResources().getColor(R.color.gray));
                tvGradeDown.setTextColor(getResources().getColor(R.color.black));
                MyToast.showShort("降序");
                break;
        }
    }


}

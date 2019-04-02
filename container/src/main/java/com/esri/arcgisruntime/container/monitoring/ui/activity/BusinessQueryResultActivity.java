package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.adapter.BusinessQueryResultAdapter;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.bean.BusinessQueryResultBean;
import com.esri.arcgisruntime.container.monitoring.presenter.BusinessQueryPresenter;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IBusinessQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/3/6.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 业务查询结果
 */
public class BusinessQueryResultActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, IBusinessQuery {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.rlSortAscending)
    RelativeLayout rlSortAscending;
    @BindView(R.id.rlGradeDown)
    RelativeLayout rlGradeDown;
    @BindView(R.id.rvBusinessQueryResult)
    RecyclerView rvBusinessQueryResult;
    BusinessQueryResultAdapter businessQueryResultAdapter;

    ArrayList<BusinessQueryResultBean.RowsBean> businessQueryList;
    @BindView(R.id.vwSortAscending)
    View vwSortAscending;
    @BindView(R.id.vwGradeDown)
    View vwGradeDown;
    @BindView(R.id.tvSortAscending)
    TextView tvSortAscending;
    @BindView(R.id.tvGradeDown)
    TextView tvGradeDown;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager layoutManager;
    @BindView(R.id.llLockCode)
    LinearLayout llLockCode;
    @BindView(R.id.llRouteLock)
    LinearLayout llRouteLock;
    private int page = 1;
    private boolean isRefresh = true;
    BusinessQueryPresenter businessQueryPresenter;
    Intent intent;
    private String type;
    private String starttime;
    private String endtime;
    private String point;
    private int queryType=1; //1-关锁  ，2-路径 传入给adapter用 不做其他使用

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_business_query_result);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        intent = getIntent();
        type = intent.getStringExtra("type");
        starttime = intent.getStringExtra("starttime");
        endtime = intent.getStringExtra("endtime");
        point = intent.getStringExtra("point");
            //1、Lock_code；2、Route_code
         if (type.equals("Route_code")){
             queryType = 2;
            llLockCode.setVisibility(View.GONE);
            llRouteLock.setVisibility(View.VISIBLE);
        }


        businessQueryPresenter = new BusinessQueryPresenter(this);
        businessQueryPresenter.businessQuery(getParams());

        businessQueryList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        businessQueryResultAdapter = new BusinessQueryResultAdapter(this, queryType ,businessQueryList);
        rvBusinessQueryResult.setLayoutManager(layoutManager);
        rvBusinessQueryResult.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvBusinessQueryResult.setAdapter(businessQueryResultAdapter);


        setListener();
    }


    private void setListener() {
        swipeRefreshLayout.setOnRefreshListener(this);//SwipeRefreshLayout.OnRefreshListener
        /**
         * 设置上拉加载更多的监听
         */
        rvBusinessQueryResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && scrollToBottom() /*&& !isRefresh*/) {
                    loadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    @OnClick({R.id.ivBack, R.id.rlSortAscending, R.id.rlGradeDown})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.rlSortAscending:  //升序
                if (vwSortAscending.getVisibility() == View.GONE) {
                    vwSortAscending.setVisibility(View.VISIBLE);
                    vwGradeDown.setVisibility(View.GONE);
                    tvSortAscending.setTextColor(getResources().getColor(R.color.black));
                    tvGradeDown.setTextColor(getResources().getColor(R.color.gray));
//                    MyToast.showShort("升序");
                    if (businessQueryList!=null && businessQueryList.size()>0){
                        Collections.sort(businessQueryList, new AscendingComparator());
                        businessQueryResultAdapter.notifyDataSetChanged();
                        rvBusinessQueryResult.smoothScrollToPosition(0); //定位到顶部
                    }
                }
                break;
            case R.id.rlGradeDown:   //降序
                if (vwGradeDown.getVisibility() == View.GONE) {
                    vwSortAscending.setVisibility(View.GONE);
                    vwGradeDown.setVisibility(View.VISIBLE);
                    tvSortAscending.setTextColor(getResources().getColor(R.color.gray));
                    tvGradeDown.setTextColor(getResources().getColor(R.color.black));
//                    MyToast.showShort("降序");
                    if (businessQueryList!=null && businessQueryList.size()>0){
                        Collections.sort(businessQueryList, new GradeDownomparator());
                        businessQueryResultAdapter.notifyDataSetChanged();
                        rvBusinessQueryResult.smoothScrollToPosition(0); //定位到顶部
                    }
                }
                break;
        }
    }

    private Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("starttime", starttime);
        params.put("endtime", endtime);
        params.put("point", point);
        params.put("sort", "asc"); //1、asc(升序)；2、desc（降序）
        params.put("page", page + "");
        params = MD5Utils.encryptParams(params);
        return params;
    }

    @Override
    public void businessQuerySucceed(BusinessQueryResultBean businessQueryResultBean) {
        //获取到业务查询的结果列表

        swipeRefreshLayout.setRefreshing(false); //刷新后 关闭circleview 加载动画

        if (businessQueryResultBean==null || businessQueryResultBean.getRows() == null || businessQueryResultBean.getRows().size()==0) return;

        if (isRefresh) {
            businessQueryList.clear();
        }

        businessQueryList.addAll(businessQueryResultBean.getRows());

        for (int i = 0; i < businessQueryList.size(); i++) {
            int temp = i + 1;
            businessQueryList.get(i).setSeniority(temp);
        }

        businessQueryResultAdapter.setData(businessQueryList);

    }


    //正序（升序）从小 --> 大
    class AscendingComparator implements Comparator<BusinessQueryResultBean.RowsBean> {
        public int compare(BusinessQueryResultBean.RowsBean o1, BusinessQueryResultBean.RowsBean o2) {
            return Double.compare(o1.getSeniority(), o2.getSeniority());
        }
    }

    //倒序（降序）从大 --> 小
    class GradeDownomparator implements Comparator<BusinessQueryResultBean.RowsBean> {
        public int compare(BusinessQueryResultBean.RowsBean o1, BusinessQueryResultBean.RowsBean o2) {
            return Double.compare(o2.getSeniority(), o1.getSeniority());
        }
    }

    //判断是否到了底部
    private boolean scrollToBottom() {
        if (layoutManager != null && layoutManager.canScrollVertically()) {
            if (layoutManager.getItemCount() > 19) { //9 代表滑动到那个位置开始加载，一般如果一页10个，那么久滑动到10的时候就加载下一页
                return !rvBusinessQueryResult.canScrollVertically(1);
            } else {
                return false;
            }
        } else {
            return !rvBusinessQueryResult.canScrollHorizontally(1);
        }
    }


    @Override
    public void onRefresh() {
        isRefresh = true;
        page = 1;
        businessQueryPresenter.businessQuery(getParams());
    }

    /**
     * 上拉加载更多
     */
    private void loadMore() {
        isRefresh = false;
        page++;
        businessQueryPresenter.businessQuery(getParams());
    }

}

package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.adapter.BillQueryAdapter;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.bean.BillQueryBean;
import com.esri.arcgisruntime.container.monitoring.presenter.BillQueryPresenter;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
import com.esri.arcgisruntime.container.monitoring.view.CustomDividerItemDecoration;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IBillQueryResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/3/6.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 单据查询结果
 */
public class BillQueryResultActivity extends BaseActivity implements IBillQueryResult, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.rvBillQueryResult)
    RecyclerView rvBillQueryResult;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private BillQueryAdapter billQueryAdapter;
    private ArrayList<BillQueryBean.RowsBean> billQueryList;
    BillQueryPresenter billQueryPresenter;
    private String code;
    private String starttime;
    private String endtime;
    private String type;
    private Intent intent;
    private int page =1;
    private boolean isRefresh = true;
    LinearLayoutManager layoutManager;
    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_bill_query_result);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        billQueryPresenter = new BillQueryPresenter(this);
        intent = getIntent();

        starttime = intent.getStringExtra("starttime");
        endtime = intent.getStringExtra("endtime");
        type = intent.getStringExtra("type");
        code = intent.getStringExtra("code");

        swipeRefreshLayout.setColorSchemeResources(
                R.color.blue,
                R.color.green,
                R.color.red,
                R.color.black
        );

        //单据查询
        billQueryPresenter.billQuery(getParams());

        billQueryList = new ArrayList<>();
        layoutManager = new LinearLayoutManager(this);
        billQueryAdapter = new BillQueryAdapter(this, billQueryList);
        rvBillQueryResult.setLayoutManager(layoutManager);
        rvBillQueryResult.addItemDecoration(new CustomDividerItemDecoration(this, CustomDividerItemDecoration.VERTICAL_LIST));
        rvBillQueryResult.setAdapter(billQueryAdapter);
        billQueryAdapter.setOnItemClickListener(new BillQueryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int pos) {
                Intent intent = new Intent(BillQueryResultActivity.this, BillQueryDetailsActivity.class);
                intent.putExtra("custom_code",billQueryList.get(pos).getCustom_code());
                startActivity(intent);
            }
        });

        setListener();
    }

    private void setListener(){
        swipeRefreshLayout.setOnRefreshListener(this);//SwipeRefreshLayout.OnRefreshListener
        /**
         * 设置上拉加载更多的监听
         */
        rvBillQueryResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    @OnClick({R.id.ivBack})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
        }
    }

    private Map<String, String> getParams() {
//                type	1、Container_code；2、Custom_code
//                code	集装箱编号(type为1)或海关编号(type为2)或为空
//                starttime	开始时间（例：2019/03/07）
//                endtime	结束时间（例：2019/03/07）
//                page	第几页，必须有，第一次请求给1

        Map<String, String> params = new HashMap<>();
        params.put("type",type);
        params.put("code",code);
        params.put("starttime",starttime);
        params.put("endtime",endtime);
        params.put("page",page+"");
        params = MD5Utils.encryptParams(params);
        return params;

    }

    @Override
    public void billQuerySucceed(BillQueryBean billQueryBean) {

        swipeRefreshLayout.setRefreshing(false); //刷新后 关闭circleview 加载动画

        if (billQueryBean == null || billQueryBean.getRows() == null || billQueryBean.getRows().size()==0 ) return;

        if (isRefresh){
            billQueryList.clear();
        }

        billQueryList.addAll(billQueryBean.getRows());
        //重新设置序号
        for (int i = 0; i < billQueryList.size(); i++) {
            int number = i+1;

            String temp = number+"";
            if(temp.length()==1)
                temp = "00"+temp;
            else if (temp.length()==2)
                temp = "0"+temp;

            billQueryList.get(i).setSequenceNumbe(temp);
        }

        billQueryAdapter.setData(billQueryList);

    }



    @Override
    public void onRefresh() {
        isRefresh = true;
        page = 1;
        billQueryPresenter.billQuery(getParams());

//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                try {
//
//                    BillQueryBean billQueryBean = new BillQueryBean("001", "刷新出来的", "刷新出来的");
//                    billQueryList.add(0,billQueryBean);
//
//                    Thread.sleep(2000);
//                }catch (Exception e){
//
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        billQueryAdapter.setData(billQueryList);
//                        swipeRefreshLayout.setRefreshing(false); //刷新后 关闭circleview 加载动画
//                    }
//                });
//
//            }
//        }.start();

    }

    /**
     * 上拉加载更多
     */
    private void loadMore(){
        isRefresh =false;
        page++;
        billQueryPresenter.billQuery(getParams());

//        new Thread(){
//            @Override
//            public void run() {
//                super.run();
//                try {
//
//                    for (int i = 0; i < 20; i++) {
//
//                        BillQueryBean billQueryBean = new BillQueryBean("001", "上拉加载更多", "更多更多");
//                        billQueryList.add(billQueryBean);
//                    }
//
//
//                    Thread.sleep(2000);
//                }catch (Exception e){
//
//                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        billQueryAdapter.setData(billQueryList);
//                        swipeRefreshLayout.setRefreshing(false); //刷新后 关闭circleview 加载动画
//                    }
//                });
//
//            }
//        }.start();

    }

    //判断是否到了底部
    private boolean scrollToBottom(){
        if (layoutManager != null && layoutManager.canScrollVertically()) {
            if(layoutManager.getItemCount()>19) { //9 代表滑动到那个位置开始加载，一般如果一页10个，那么久滑动到10的时候就加载下一页
                return !rvBillQueryResult.canScrollVertically(1);
            }else{
                return false;
            }
        } else {
            return !rvBillQueryResult.canScrollHorizontally(1);
        }
    }

}

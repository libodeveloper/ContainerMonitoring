package com.esri.arcgisruntime.container.monitoring.ui.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.base.BaseFragment;
import com.esri.arcgisruntime.container.monitoring.bean.BusinessQueryResultBean;
import com.esri.arcgisruntime.container.monitoring.bean.SiteBean;
import com.esri.arcgisruntime.container.monitoring.http.ResponseErrorException;
import com.esri.arcgisruntime.container.monitoring.popwindow.PopwindowUtils;
import com.esri.arcgisruntime.container.monitoring.presenter.BusinessQueryPresenter;
import com.esri.arcgisruntime.container.monitoring.presenter.BusinessQuerySitePresenter;
import com.esri.arcgisruntime.container.monitoring.ui.activity.BusinessQueryResultActivity;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;
import com.esri.arcgisruntime.container.monitoring.utils.TimeUtil;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IBusinessQuery;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IBussQuerySite;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/3/4.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 业务查询
 */
public class BusinessQueryFragment extends BaseFragment implements IBussQuerySite {

    @BindView(R.id.btSearch)
    Button btSearch;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.llStartTime)
    LinearLayout llStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.llEndTime)
    LinearLayout llEndTime;
    @BindView(R.id.tvStatisticsType)
    TextView tvStatisticsType;
    @BindView(R.id.rlStatisticsType)
    RelativeLayout rlStatisticsType;
    @BindView(R.id.tvSite)
    TextView tvSite;
    @BindView(R.id.rlSite)
    RelativeLayout rlSite;
    BusinessQuerySitePresenter businessQuerySitePresenter;
    List<SiteBean.RowsBean> siteData; //获取的站点数据
    ArrayList<String> sitelist; //站点名称展示列表

    private String site;

    int type = 1;  //1、Lock_code；2、Route_code

    @Override
    protected void setView() {
        String curTime = TimeUtils.milliseconds2String(System.currentTimeMillis(),new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
        tvStartTime.setText(curTime);
        tvEndTime.setText(curTime);
        site = getResources().getString(R.string.allsite);
        tvSite .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvSite.setTextColor(getResources().getColor(R.color.black));
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_query, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        businessQuerySitePresenter = new BusinessQuerySitePresenter(this);
    }



    @OnClick({R.id.llStartTime, R.id.llEndTime, R.id.btSearch,R.id.rlStatisticsType, R.id.rlSite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llStartTime:
                TimeUtil.selectDate(getActivity(), tvStartTime, getResources().getString(R.string.start_time), false);
                break;
            case R.id.llEndTime:
                TimeUtil.selectDate(getActivity(), tvEndTime, getResources().getString(R.string.end_time), false);
                break;
            case R.id.btSearch:


                String startTime = tvStartTime.getText().toString();
                String endTime = tvEndTime.getText().toString();
                if (TimeUtil.compareStartAndEndTime(getActivity(),startTime,endTime)) return;

                Intent intent = new Intent(getActivity(), BusinessQueryResultActivity.class);
                if (type==1) //1、Lock_code；2、Route_code
                    intent.putExtra("type","Lock_code");
                else if (type == 2)
                    intent.putExtra("type","Route_code");

                    intent.putExtra("starttime",startTime);
                    intent.putExtra("endtime",endTime);

                    if (site.equals(getResources().getString(R.string.allsite)))
                        intent.putExtra("point","All");
                    else
                        intent.putExtra("point",site);

                startActivity(intent);
                break;
            case R.id.rlStatisticsType:  //统计类型
                ArrayList<String> stlist = new ArrayList<>();
                stlist.add(getResources().getString(R.string.lock_rate_statistics));
                stlist.add(getResources().getString(R.string.route_use_statistics));

                PopwindowUtils.PullDownPopWindow(getActivity(), rlStatisticsType, stlist, new PopwindowUtils.OnClickNumberType() {
                    @Override
                    public void onNumberType(String context,int pos) {
                        tvStatisticsType.setText(context);
                        type = pos+1;
                    }
                });

                break;
            case R.id.rlSite:   //所属站点
                if (siteData!=null && siteData.size()>0)
                    showSitePullList();
                else
                    businessQuerySitePresenter.businessQuerySite(getParams());
                break;
        }
    }

        private Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params = MD5Utils.encryptParams(params);
        return params;

    }

    @Override
    public void bussQuerySiteSucceed(SiteBean siteBean) {

        if (siteBean==null || siteBean.getRows()==null)return;

        siteData = siteBean.getRows();
        sitelist = new ArrayList<>();

        for (int i = 0; i < siteData.size(); i++) {
            sitelist.add(siteData.get(i).getDestinationName());
        }

        Collections.sort(sitelist);

        sitelist.add(0,getResources().getString(R.string.allsite));

        showSitePullList();

    }


    private void showSitePullList() {
        PopwindowUtils.PullDownPopWindow(getActivity(), rlSite, sitelist, new PopwindowUtils.OnClickNumberType() {
            @Override
            public void onNumberType(String context,int pos) {
                tvSite.setText(context);
                if (context.equals(getResources().getString(R.string.allsite))){
                    //设置加粗 加黑
                    tvSite .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    tvSite.setTextColor(getResources().getColor(R.color.black));
                }else {
                    //设置不加粗 系统默认颜色
                    tvSite.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    tvSite.setTextColor(getResources().getColor(R.color.textview_system_default));
                }

                site = context;
            }
        });
    }
}

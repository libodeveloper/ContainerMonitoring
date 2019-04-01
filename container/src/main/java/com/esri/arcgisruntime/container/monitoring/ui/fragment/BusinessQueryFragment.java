package com.esri.arcgisruntime.container.monitoring.ui.fragment;

import android.content.Intent;
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

    private String site;

    int type = 1;  //1、Lock_code；2、Route_code

    @Override
    protected void setView() {
        String curTime = TimeUtils.milliseconds2String(System.currentTimeMillis(),new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
        tvStartTime.setText(curTime);
        tvEndTime.setText(curTime);
        site = getResources().getString(R.string.allsite);
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

        List<SiteBean.RowsBean> data = siteBean.getRows();
        ArrayList<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.allsite));
        for (int i = 0; i < data.size(); i++) {
            list.add(data.get(i).getDestinationName());
        }

        PopwindowUtils.PullDownPopWindow(getActivity(), rlSite, list, new PopwindowUtils.OnClickNumberType() {
            @Override
            public void onNumberType(String context,int pos) {
                tvSite.setText(context);
                site = context;
            }
        });

    }
}

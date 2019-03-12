package com.esri.arcgisruntime.container.monitoring.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.popwindow.PopwindowUtils;
import com.esri.arcgisruntime.container.monitoring.ui.activity.BusinessQueryResultActivity;
import com.esri.arcgisruntime.container.monitoring.utils.TimeUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/3/4.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 业务查询
 */
public class BusinessQueryFragment extends Fragment {


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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_query, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @OnClick({R.id.llStartTime, R.id.llEndTime, R.id.btSearch,R.id.rlStatisticsType, R.id.rlSite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llStartTime:
                TimeUtil.selectDate(getActivity(), tvStartTime, getResources().getString(R.string.start_time), false);
                break;
            case R.id.llEndTime:
                TimeUtil.selectDate(getActivity(), tvEndTime, getResources().getString(R.string.end_time), true);
                break;
            case R.id.btSearch:

                String startTime = tvStartTime.getText().toString();
                String endTime = tvEndTime.getText().toString();
                if (TimeUtil.compareStartAndEndTime(getActivity(),startTime,endTime)) return;

                Intent intent = new Intent(getActivity(), BusinessQueryResultActivity.class);
                startActivity(intent);
                break;
            case R.id.rlStatisticsType:  //统计类型
                ArrayList<String> stlist = new ArrayList<>();
                stlist.add(getResources().getString(R.string.lock_rate_statistics));
                stlist.add(getResources().getString(R.string.route_use_statistics));

                PopwindowUtils.PullDownPopWindow(getActivity(), rlStatisticsType, stlist, new PopwindowUtils.OnClickNumberType() {
                    @Override
                    public void onNumberType(String context) {
                        tvStatisticsType.setText(context);
                    }
                });

                break;
            case R.id.rlSite:   //所属站点
                ArrayList<String> list = new ArrayList<>();
                list.add(getResources().getString(R.string.allsite));
                list.add("site1");
                list.add("site22");
                list.add("site333");
                list.add("site4444");

                PopwindowUtils.PullDownPopWindow(getActivity(), rlSite, list, new PopwindowUtils.OnClickNumberType() {
                    @Override
                    public void onNumberType(String context) {
                        tvSite.setText(context);
                    }
                });

                break;
        }
    }


}

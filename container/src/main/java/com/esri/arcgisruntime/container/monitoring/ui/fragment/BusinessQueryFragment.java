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
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.ui.activity.BusinessQueryResultActivity;
import com.esri.arcgisruntime.container.monitoring.utils.TimeUtil;

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


    @OnClick({R.id.llStartTime, R.id.llEndTime,R.id.btSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llStartTime:
                TimeUtil.selectDate(getActivity(),tvStartTime,"开始时间",false);
                break;
            case R.id.llEndTime:
                TimeUtil.selectDate(getActivity(),tvEndTime,"结束时间",true);
                break;
            case R.id.btSearch:
                Intent intent = new Intent(getActivity(), BusinessQueryResultActivity.class);
                startActivity(intent);
                break;
        }
    }
}

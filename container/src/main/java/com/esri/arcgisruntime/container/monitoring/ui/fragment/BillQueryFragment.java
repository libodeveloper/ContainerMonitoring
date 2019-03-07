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
import com.esri.arcgisruntime.container.monitoring.ui.activity.BillQueryDetailsActivity;
import com.esri.arcgisruntime.container.monitoring.ui.activity.BillQueryResultActivity;
import com.esri.arcgisruntime.container.monitoring.utils.TimeUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/3/4.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 单据查询
 */
public class BillQueryFragment extends Fragment {

    @BindView(R.id.tvSelectNumberType)
    TextView tvSelectNumberType;
    @BindView(R.id.rlSelectNumberType)
    RelativeLayout rlSelectNumberType;
    @BindView(R.id.tvStartTime)
    TextView tvStartTime;
    @BindView(R.id.llStartTime)
    LinearLayout llStartTime;
    @BindView(R.id.tvEndTime)
    TextView tvEndTime;
    @BindView(R.id.llEndTime)
    LinearLayout llEndTime;
    @BindView(R.id.btSearch)
    Button btSearch;
    private ArrayList<String> spinnerList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spinnerList = new ArrayList<String>();
        spinnerList.add("集装箱编号");
        spinnerList.add("关锁编号");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bills_query, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @OnClick({R.id.rlSelectNumberType, R.id.llStartTime, R.id.llEndTime, R.id.btSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlSelectNumberType:
                PopwindowUtils.popWindow(getActivity(), rlSelectNumberType, spinnerList, new PopwindowUtils.OnClickNumberType() {
                    @Override
                    public void onNumberType(String context) {
                        tvSelectNumberType.setText(context);
                    }
                });
                break;
            case R.id.llStartTime:
                TimeUtil.selectDate(getActivity(),tvStartTime,"开始时间",false);
                break;
            case R.id.llEndTime:
                TimeUtil.selectDate(getActivity(),tvEndTime,"结束时间",true);
                break;
            case R.id.btSearch:
                Intent intent = new Intent(getActivity(), BillQueryResultActivity.class);
                startActivity(intent);
                break;
        }
    }
}

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

import com.blankj.utilcode.utils.TimeUtils;
import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.base.BaseFragment;
import com.esri.arcgisruntime.container.monitoring.popwindow.PopwindowUtils;
import com.esri.arcgisruntime.container.monitoring.ui.activity.BillQueryResultActivity;
import com.esri.arcgisruntime.container.monitoring.utils.TimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/3/4.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 单据查询
 */
public class BillQueryFragment extends BaseFragment {

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
    protected void setView() {
        String curTime = TimeUtils.milliseconds2String(System.currentTimeMillis(),new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
        tvStartTime.setText(curTime);
        tvEndTime.setText(curTime);
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bills_query, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
        spinnerList = new ArrayList<String>();
        spinnerList.add(getResources().getString(R.string.container_number));
        spinnerList.add(getResources().getString(R.string.lock_number));
    }


    @OnClick({R.id.rlSelectNumberType, R.id.llStartTime, R.id.llEndTime, R.id.btSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlSelectNumberType:
                PopwindowUtils.PullDownPopWindow(getActivity(), rlSelectNumberType, spinnerList, new PopwindowUtils.OnClickNumberType() {
                    @Override
                    public void onNumberType(String context) {
                        tvSelectNumberType.setText(context);
                    }
                });
                break;
            case R.id.llStartTime:
                TimeUtil.selectDate(getActivity(),tvStartTime,getResources().getString(R.string.start_time),false);
                break;
            case R.id.llEndTime:
                TimeUtil.selectDate(getActivity(),tvEndTime,getResources().getString(R.string.end_time),false);
                break;
            case R.id.btSearch:

                String startTime = tvStartTime.getText().toString();
                String endTime = tvEndTime.getText().toString();
                if (TimeUtil.compareStartAndEndTime(getActivity(),startTime,endTime)) return;

                Intent intent = new Intent(getActivity(), BillQueryResultActivity.class);
                startActivity(intent);
                break;
        }
    }
}

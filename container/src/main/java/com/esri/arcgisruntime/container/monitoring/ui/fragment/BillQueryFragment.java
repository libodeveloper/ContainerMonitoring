package com.esri.arcgisruntime.container.monitoring.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.base.BaseFragment;
import com.esri.arcgisruntime.container.monitoring.popwindow.PopwindowUtils;
import com.esri.arcgisruntime.container.monitoring.ui.activity.BillQueryResultActivity;
import com.esri.arcgisruntime.container.monitoring.utils.MyNumberKeyListener;
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
    @BindView(R.id.etNumber)
    EditText etNumber;
    private ArrayList<String> spinnerList;

    int type = 1; //1 - 集装箱编号  2 - 海关编号
    @Override
    protected void setView() {
        String curTime = TimeUtils.milliseconds2String(System.currentTimeMillis(),new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
        tvStartTime.setText(curTime);
        tvEndTime.setText(curTime);
        //设置限制字符 只能是数字 和 英文
        etNumber.setKeyListener(new MyNumberKeyListener());
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
        spinnerList.add(getResources().getString(R.string.customs_number));
    }


    @OnClick({R.id.rlSelectNumberType, R.id.llStartTime, R.id.llEndTime, R.id.btSearch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlSelectNumberType:
                PopwindowUtils.PullDownPopWindow(getActivity(), rlSelectNumberType, spinnerList, new PopwindowUtils.OnClickNumberType() {
                    @Override
                    public void onNumberType(String context,int selflag) {
                        tvSelectNumberType.setText(context);
                        type = selflag+1;
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

                String number = etNumber.getText().toString();

//                if(TextUtils.isEmpty(number)){
//                    MyToast.showShort(getResources().getString(R.string.number_null));
//                    return;
//                }

                String startTime = tvStartTime.getText().toString();
                String endTime = tvEndTime.getText().toString();
                if (TimeUtil.compareStartAndEndTime(getActivity(),startTime,endTime)) return;

                Intent intent = new Intent(getActivity(), BillQueryResultActivity.class);
                intent.putExtra("starttime",startTime);
                intent.putExtra("endtime",endTime);
                intent.putExtra("code",number);
                if (type ==1)
                    intent.putExtra("type", "Container_code");
                else if(type ==2)
                    intent.putExtra("type", "Custom_code");
                startActivity(intent);
                break;
        }
    }
}

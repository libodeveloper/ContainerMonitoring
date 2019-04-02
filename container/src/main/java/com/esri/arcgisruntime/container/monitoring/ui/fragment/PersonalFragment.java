package com.esri.arcgisruntime.container.monitoring.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.application.CMApplication;
import com.esri.arcgisruntime.container.monitoring.base.BaseFragment;
import com.esri.arcgisruntime.container.monitoring.bean.User;
import com.esri.arcgisruntime.container.monitoring.dialog.ShowMsgDialog;
import com.esri.arcgisruntime.container.monitoring.ui.activity.SetActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by libo on 2019/3/4.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 个人资料
 */
public class PersonalFragment extends BaseFragment {


    @BindView(R.id.rlSet)
    RelativeLayout rlSet;
    @BindView(R.id.btExit)
    Button btExit;
    Unbinder unbinder;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.tvAccount1)
    TextView tvAccount1;

    @Override
    protected void setView() {
        String userName = CMApplication.getUser().getAccount();
        tvAccount.setText(userName);
        tvAccount1.setText(userName);
    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {

    }

    /**
     * 退出前提示用户 by zealjiang
     */
    private void exit() {
        ShowMsgDialog.showMaterialDialog2Btn(mainActivity, getResources().getString(R.string.prompt), getResources().getString(R.string.exit_system), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainActivity.finish();
            }

        }, getResources().getString(R.string.cancle), getResources().getString(R.string.exit));
    }


    @OnClick({R.id.rlSet, R.id.btExit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlSet:
                Intent intent = new Intent(mainActivity, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.btExit:
                exit();
                break;
        }
    }

}

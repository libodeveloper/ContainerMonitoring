package com.esri.arcgisruntime.container.monitoring.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.base.BaseFragment;
import com.esri.arcgisruntime.container.monitoring.dialog.ShowMsgDialog;

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

    @Override
    protected void setView() {

    }

    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    protected void initData() {

    }

    /**
     * 退出前提示用户 by zealjiang
     */
    private void exit() {
        ShowMsgDialog.showMaterialDialog2Btn(mainActivity, "提示", "是否退出系统", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消

            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mainActivity.finish();
            }

        }, "取消", "退出");
    }


    @OnClick({R.id.rlSet, R.id.btExit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlSet:
                break;
            case R.id.btExit:
                exit();
                break;
        }
    }
}

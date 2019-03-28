package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.utils.LocalManageUtil;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;
import com.esri.arcgisruntime.container.monitoring.utils.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/3/11.
 */

public class SetActivity extends BaseActivity {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.main_radio)
    RadioGroup mainRadio;
    @BindView(R.id.tvVersionName)
    TextView tvVersionName;
    @BindView(R.id.rlVersionName)
    RelativeLayout rlVersionName;
    @BindView(R.id.rbCH)
    RadioButton rbCH;
    @BindView(R.id.rbEN)
    RadioButton rbEN;
    @BindView(R.id.rbPt)
    RadioButton rbPt;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {


        switch (SPUtil.getInstance(this).getSelectLanguage()) {
            case 1:
                mainRadio.check(R.id.rbCH);
                break;
            case 2:
                mainRadio.check(R.id.rbEN);
                break;
            case 3:
                mainRadio.check(R.id.rbPt);
                break;
            default:
                    mainRadio.check(R.id.rbCH);
                break;
        }

        setListener();
    }


    private void setListener() {

        mainRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rbCH:   //中文
                        selectLanguage(Constants.CH);
                        break;
                    case R.id.rbEN:  //英文
                        selectLanguage(Constants.EN);
                        break;
                    case R.id.rbPt:  //葡萄牙
                        selectLanguage(Constants.PT);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.ivBack, R.id.rlVersionName})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.rlVersionName:
                MyToast.showShort("versionCode " + tvVersionName.getText().toString());
                break;
        }
    }

    //语言
    public void reStart() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    //选择语言
    private void selectLanguage(int select) {
        Log.e("SSSSSSSSS", "selectLanguage: "+select);
        LocalManageUtil.saveSelectLanguage(this, select);
        reStart();
    }

}
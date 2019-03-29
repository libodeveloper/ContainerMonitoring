package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.utils.LocalManageUtil;
import com.esri.arcgisruntime.container.monitoring.utils.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/3/29.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 语言切换
 */
public class LanguageChangeActivity extends BaseActivity {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.rbCH)
    TextView rbCH;
    @BindView(R.id.ivChok)
    ImageView ivChok;
    @BindView(R.id.rbEN)
    TextView rbEN;
    @BindView(R.id.ivENok)
    ImageView ivENok;
    @BindView(R.id.rbPt)
    TextView rbPt;
    @BindView(R.id.ivPtok)
    ImageView ivPtok;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_language);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        switch (SPUtil.getInstance(this).getSelectLanguage()) {
            case 1:
                changeStatus(1);
                break;
            case 2:
                changeStatus(2);
                break;
            case 3:
                changeStatus(3);
                break;
            default:
                changeStatus(1);
                break;
        }

    }

    private void changeStatus(int status) {
        switch (status) {
            case 1:
                ivChok.setVisibility(View.VISIBLE);
                ivENok.setVisibility(View.GONE);
                ivPtok.setVisibility(View.GONE);
                break;
            case 2:
                ivChok.setVisibility(View.GONE);
                ivENok.setVisibility(View.VISIBLE);
                ivPtok.setVisibility(View.GONE);
                break;
            case 3:
                ivChok.setVisibility(View.GONE);
                ivENok.setVisibility(View.GONE);
                ivPtok.setVisibility(View.VISIBLE);
                break;
            default:
                ivChok.setVisibility(View.VISIBLE);
                ivENok.setVisibility(View.GONE);
                ivPtok.setVisibility(View.GONE);
                break;
        }
    }


    //语言
    public void reStart() {
//        Intent intent = new Intent(this, SetActivity.class);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    //选择语言
    private void selectLanguage(int select) {
        Log.e("SSSSSSSSS", "selectLanguage: " + select);
        LocalManageUtil.saveSelectLanguage(this, select);
        reStart();
    }


    @OnClick({R.id.ivBack, R.id.rbCH, R.id.rbEN, R.id.rbPt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.rbCH:
                selectLanguage(Constants.CH);
                changeStatus(1);
                break;
            case R.id.rbEN:
                selectLanguage(Constants.EN);
                changeStatus(2);
                break;
            case R.id.rbPt:
                selectLanguage(Constants.PT);
                changeStatus(3);
                break;
        }
    }
}

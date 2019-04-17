package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/3/11.
 */

public class SetActivity extends BaseActivity {
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvVersionName)
    TextView tvVersionName;
    @BindView(R.id.rlLanguage)
    RelativeLayout rlLanguage;
    @BindView(R.id.rlVersionName)
    RelativeLayout rlVersionName;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_set);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {

    }

    private void setListener() {
    }

    @OnClick({R.id.ivBack, R.id.rlVersionName,R.id.rlLanguage,R.id.rlFixPassword})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.rlVersionName:
                MyToast.showShort("versionCode " + tvVersionName.getText().toString());
                break;
            case R.id.rlLanguage:
                Intent intent = new Intent(this,LanguageChangeActivity.class);
                startActivity(intent);
                break;
            case R.id.rlFixPassword:
                Intent intent1 = new Intent(this,FixPasswordActivity.class);
                startActivity(intent1);
                break;
        }
    }

}

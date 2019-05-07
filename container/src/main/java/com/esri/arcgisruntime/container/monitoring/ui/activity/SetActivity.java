package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.application.AppManager;
import com.esri.arcgisruntime.container.monitoring.application.CMApplication;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.http.ApiManager;
import com.esri.arcgisruntime.container.monitoring.utils.ACache;
import com.esri.arcgisruntime.container.monitoring.utils.LogUtil;
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

    private long startTime;
    private long endTime;
    private int total;

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
//                MyToast.showShort("versionCode " + tvVersionName.getText().toString());
                changeBaseUrl();
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

    /**
     * Created by 李波 on 2018/11/21.
     * 切换测试与正式地址
     */
    private void changeBaseUrl(){
        if(total>=15)
            return;
        startTime = endTime;
        endTime = System.currentTimeMillis();
        if(endTime-startTime>1000){//证明其中有一次不够连续，重新来过
            LogUtil.e("times","断开了.......");
            total = 0;
            return;
        }
        total++;

        if (total>10)
            MyToast.showShort("连续点击 "+total+" 次");

        if(total==15){
            total = 0;
            Constants.isTestURL =!Constants.isTestURL;
            ApiManager.apiServer = null;

            if (Constants.isTestURL)
                MyToast.showShort("当前为测试地址 10008");
            else
                MyToast.showShort("当前为正式地址 10003");



            Intent intent = new Intent(AppManager.getAppManager().currentActivity(), LoginActivity.class);
            AppManager.getAppManager().currentActivity().startActivity(intent);
            AppManager.getAppManager().finishAllActivity(LoginActivity.class);

            ACache.get(CMApplication.getAppContext()).remove(Constants.KEY_ACACHE_USER);
            ACache.get(CMApplication.getAppContext()).remove(Constants.KEY_ACACHE_NUMBERCACHE);
            ACache.get(CMApplication.getAppContext()).remove(Constants.KEY_ACACHE_USERINFO);

        }
    }

}

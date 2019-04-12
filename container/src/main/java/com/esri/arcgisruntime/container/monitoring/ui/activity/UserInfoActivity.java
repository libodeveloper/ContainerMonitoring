package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.application.CMApplication;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.bean.UserInfo;
import com.esri.arcgisruntime.container.monitoring.presenter.UserInfoPresenter;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IUserInfo;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/4/12.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class UserInfoActivity extends BaseActivity implements IUserInfo {

    @BindView(R.id.tvInfo)
    TextView tvInfo;

    UserInfoPresenter userInfoPresenter;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        userInfoPresenter = new UserInfoPresenter(this);
        userInfoPresenter.getUserInfo(getParams());
    }



    private Map<String, String> getParams() {

        Map<String, String> params = new HashMap<>();
        params.put("account", CMApplication.getUser().getAccount());
        params = MD5Utils.encryptParams(params);
        return params;

    }

    @OnClick(R.id.ivBack)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void Succeed(UserInfo user) {
        /**
         * account : test123
         * passwordOld : $2a$10$J6KTSSCv2aevAgL3oOwOr.EX9YYKhY0pFp3TAhPT8rHKWPEmTen5a
         * passwordNew :
         * roleName : 站点技术人员
         */
        tvInfo.setText(user.getAccount()+"\n"
                        +user.getRoleName()+"\n"
                        +user.getPasswordOld()+"\n"
                        +user.getPasswordNew());

    }
}

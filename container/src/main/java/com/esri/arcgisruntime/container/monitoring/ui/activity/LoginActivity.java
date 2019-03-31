package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.bean.User;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.presenter.LoginPresenter;
import com.esri.arcgisruntime.container.monitoring.utils.ACache;
import com.esri.arcgisruntime.container.monitoring.utils.BuilderParams;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.ILogin;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by libo on 2019/3/5.
 */

public class LoginActivity extends BaseActivity implements ILogin{


    @BindView(R.id.etUseName)
    EditText etUseName;
    @BindView(R.id.etPassWord)
    EditText etPassWord;
    @BindView(R.id.btLogin)
    TextView btLogin;
    LoginPresenter loginPresenter;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        loginPresenter = new LoginPresenter(this);
        btLogin.setClickable(false);
        setLinster();

    }

    private void setLinster() {
        etUseName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                //根据字数长度自动变换按钮颜色 和 可点击状态
                if (s.length() >0 && !TextUtils.isEmpty(etPassWord.getText().toString().trim())) {
                    btLogin.setClickable(true);
                    btLogin.setBackgroundResource(R.drawable.selector_blue1_to_buttonbg);
                    ColorStateList csl = getResources().getColorStateList(R.color.login_text_color);
                    btLogin.setTextColor(csl);
                }else {
                    btLogin.setClickable(false);
                    btLogin.setBackgroundResource(R.drawable.buttonbg);
                    btLogin.setTextColor(getResources().getColor(R.color.color1));
                }
            }
        });

        etPassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                //根据字数长度自动变换按钮颜色 和 可点击状态
                if (s.length() >0 && !TextUtils.isEmpty(etUseName.getText().toString().trim())) {
                    btLogin.setClickable(true);
                    btLogin.setBackgroundResource(R.drawable.selector_blue1_to_buttonbg);
                    ColorStateList csl = getResources().getColorStateList(R.color.login_text_color);
                    btLogin.setTextColor(csl);
                }else {
                    btLogin.setClickable(false);
                    btLogin.setBackgroundResource(R.drawable.buttonbg);
                    btLogin.setTextColor(getResources().getColor(R.color.color1));
                }
            }
        });
    }

    @OnClick(R.id.btLogin)
    public void onViewClicked() {
        loginPresenter.login(getParams());

    }

    private Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("userName",etUseName.getText().toString().trim());
        params.put("password",etPassWord.getText().toString().trim());
        return params;
    }

    @Override
    public void loginSucceed(User user) {

        user.setAccount(etUseName.getText().toString().trim());
        user.setPassword(etPassWord.getText().toString().trim());

        if (!TextUtils.isEmpty(user.getKey()))
//                MD5Utils.PRIVATE_KEY = user.getKey();

        ACache.get(this).put(Constants.KEY_ACACHE_USER,user);

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFailed() {

    }
}

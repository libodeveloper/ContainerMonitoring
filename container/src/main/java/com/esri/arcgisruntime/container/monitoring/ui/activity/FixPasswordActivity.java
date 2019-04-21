package com.esri.arcgisruntime.container.monitoring.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.blankj.utilcode.utils.RegularUtils;
import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.application.AppManager;
import com.esri.arcgisruntime.container.monitoring.application.CMApplication;
import com.esri.arcgisruntime.container.monitoring.base.BaseActivity;
import com.esri.arcgisruntime.container.monitoring.presenter.FixPasswordPresenter;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
import com.esri.arcgisruntime.container.monitoring.utils.MyNumberKeyListener;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;
import com.esri.arcgisruntime.container.monitoring.utils.RegularUtil;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IFixPassword;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by libo on 2019/4/12.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 修改密码
 */
public class FixPasswordActivity extends BaseActivity implements IFixPassword {

    @BindView(R.id.etOldPassword)
    EditText etOldPassword;
    @BindView(R.id.etNewPassword)
    EditText etNewPassword;
    @BindView(R.id.etConFirmPassword)
    EditText etConFirmPassword;
    FixPasswordPresenter fixPasswordPresenter;
    private Subscription subscribe;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        setContentView(R.layout.activity_fix_password);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        //设置限制字符 只能是数字 和 英文
        MyNumberKeyListener myNumberKeyListener = new MyNumberKeyListener();
        etOldPassword.setKeyListener(myNumberKeyListener);
        etNewPassword.setKeyListener(myNumberKeyListener);
        etConFirmPassword.setKeyListener(myNumberKeyListener);

        fixPasswordPresenter = new FixPasswordPresenter(this);
        waitNs(etOldPassword);
    }


    @OnClick({R.id.ivBack, R.id.btConfirm,R.id.ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.ok:
                String old = etOldPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String rnewPassword = etConFirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(old) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(rnewPassword)){
                    MyToast.showLong(getResources().getString(R.string.password_no_empty).toString());
                    return;
                }

                if (!newPassword.equals(rnewPassword)){
                    MyToast.showLong(getResources().getString(R.string.new_no_same));
                    return;
                }

                if (old.equals(newPassword)){
                    MyToast.showLong(getResources().getString(R.string.old_new_same));
                    return;
                }

                if (!RegularUtil.isCorrectPassword(old)) return;

                if (!RegularUtil.isCorrectPassword(newPassword)) return;

                //旧密码输入错误
                String password = CMApplication.getAppContext().getUser().getPassword();
                if (!old.equals(password)){
                    MyToast.showLong(getResources().getString(R.string.error_entering_old_password));
                    return;
                }

                fixPasswordPresenter.fixPassword(getParams(old,newPassword));
                break;
        }
    }

    private Map<String, String> getParams(String oldPassword,String newPassword) {

        Map<String, String> params = new HashMap<>();
        params.put("passwordOld", oldPassword);
        params.put("passwordNew", newPassword);
        params = MD5Utils.encryptParams(params);
        return params;

    }

    @Override
    public void Succeed() {
        //修改密码成功后的操作 看是否需要重新登录
        AppManager.getAppManager().finishAllActivity();
        jump(LoginActivity.class,true);

    }

    //如等待 N秒 执行
    private void waitNs(EditText editText){
        subscribe = Observable.timer(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        //等待 Ns 后执行的事件
                        KeyboardUtils.showSoftInput(FixPasswordActivity.this,editText);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (subscribe!=null)
            subscribe.unsubscribe();
    }


}

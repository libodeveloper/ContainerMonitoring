package com.esri.arcgisruntime.container.monitoring.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.application.CMApplication;
import com.esri.arcgisruntime.container.monitoring.base.BaseFragment;
import com.esri.arcgisruntime.container.monitoring.bean.NumberCache;
import com.esri.arcgisruntime.container.monitoring.bean.User;
import com.esri.arcgisruntime.container.monitoring.dialog.ShowMsgDialog;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.presenter.LoginPresenter;
import com.esri.arcgisruntime.container.monitoring.ui.activity.SetActivity;
import com.esri.arcgisruntime.container.monitoring.utils.ACache;
import com.esri.arcgisruntime.container.monitoring.utils.ImageUtil;
import com.esri.arcgisruntime.container.monitoring.utils.LogUtil;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.ILogin;

import java.util.HashMap;
import java.util.Map;

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
public class PersonalFragment extends BaseFragment implements ILogin {


    @BindView(R.id.rlSet)
    RelativeLayout rlSet;
    @BindView(R.id.btExit)
    Button btExit;
    Unbinder unbinder;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.tvAccount1)
    TextView tvAccount1;
    @BindView(R.id.ivPerson)
    ImageView ivPerson;
    LoginPresenter loginPresenter;
    @Override
    protected void setView() {
        //先把drawable图片转成biemap
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.icperson);
        //将bitmap做成圆形图片
        Bitmap output= ImageUtil.toRoundBitmap(bitmap);
        ivPerson.setImageBitmap(output);

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
        loginPresenter = new LoginPresenter(this);
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
                loginPresenter.loginOut(getParams());
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

    private Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        params = MD5Utils.encryptParams(params);
        return params;
    }

    @Override
    public void Succeed(User user) {
        MyToast.showLong("exit");

        //清除缓存
        ACache.get(CMApplication.getAppContext()).remove(Constants.KEY_ACACHE_USER);
        ACache.get(CMApplication.getAppContext()).remove(Constants.KEY_ACACHE_NUMBERCACHE);

        User user1 = CMApplication.getUser();
        NumberCache numberCache = (NumberCache) ACache.get(CMApplication.getAppContext()).getAsObject(Constants.KEY_ACACHE_NUMBERCACHE);
        LogUtil.e("use","exit use = "+user1);
        LogUtil.e("use","exit numberCache = "+numberCache);
    }

    @Override //只要点击了退出 就算退出接口调用失败也得清理缓存
    public void Failed() {
        //清除缓存
        ACache.get(CMApplication.getAppContext()).remove(Constants.KEY_ACACHE_USER);
        ACache.get(CMApplication.getAppContext()).remove(Constants.KEY_ACACHE_NUMBERCACHE);


        User user1 = CMApplication.getUser();
        NumberCache numberCache = (NumberCache) ACache.get(CMApplication.getAppContext()).getAsObject(Constants.KEY_ACACHE_NUMBERCACHE);
        LogUtil.e("use","use = "+user1);
        LogUtil.e("use","numberCache = "+numberCache);
    }

}

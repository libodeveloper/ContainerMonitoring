package com.esri.arcgisruntime.container.monitoring.ui.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.ConstUtils;
import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.ImageUtils;
import com.blankj.utilcode.utils.ScreenUtils;
import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.application.CMApplication;
import com.esri.arcgisruntime.container.monitoring.base.BaseFragment;
import com.esri.arcgisruntime.container.monitoring.bean.NumberCache;
import com.esri.arcgisruntime.container.monitoring.bean.User;
import com.esri.arcgisruntime.container.monitoring.bean.UserInfo;
import com.esri.arcgisruntime.container.monitoring.dialog.ActionSheet;
import com.esri.arcgisruntime.container.monitoring.dialog.MyUniversalDialog;
import com.esri.arcgisruntime.container.monitoring.dialog.ShowMsgDialog;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.presenter.LoginPresenter;
import com.esri.arcgisruntime.container.monitoring.presenter.UserInfoPresenter;
import com.esri.arcgisruntime.container.monitoring.ui.activity.FixPasswordActivity;
import com.esri.arcgisruntime.container.monitoring.ui.activity.SetActivity;
import com.esri.arcgisruntime.container.monitoring.ui.activity.UserInfoActivity;
import com.esri.arcgisruntime.container.monitoring.utils.ACache;
import com.esri.arcgisruntime.container.monitoring.utils.ImageUtil;
import com.esri.arcgisruntime.container.monitoring.utils.LogUtil;
import com.esri.arcgisruntime.container.monitoring.utils.MD5Utils;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;
import com.esri.arcgisruntime.container.monitoring.utils.UIUtils;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.ILogin;
import com.esri.arcgisruntime.container.monitoring.viewinterfaces.IUserInfo;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;

/**
 * Created by libo on 2019/3/4.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 个人资料
 */
public class PersonalFragment extends BaseFragment implements ILogin, ActionSheet.OnActionSheetSelected, IUserInfo {


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
    @BindView(R.id.rlUseInfo)
    RelativeLayout rlUseInfo;
    @BindView(R.id.rlFixPassword)
    RelativeLayout rlFixPassword;
    Unbinder unbinder1;

    private String tempPicPath;     //拍照保存的临时图片路径
    private String tempZoomPicPath; //拍照保存压缩后的临时图片路径

    UserInfoPresenter userInfoPresenter;

    @Override
    protected void setView() {

        if (FileUtils.isFileExists(tempZoomPicPath)){
            Bitmap bitmap = ImageUtils.getBitmapByFile(tempZoomPicPath);
            Bitmap output = ImageUtil.toRoundBitmap(bitmap);
            ivPerson.setImageBitmap(output);
        }else {
            //先把drawable图片转成biemap
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icperson);
            //将bitmap做成圆形图片
            Bitmap output = ImageUtil.toRoundBitmap(bitmap);
            ivPerson.setImageBitmap(output);
        }

        String userName = CMApplication.getUser().getAccount();
        UserInfo userInfo = (UserInfo) ACache.get(mainActivity).getAsObject(Constants.KEY_ACACHE_USERINFO);

        if (userInfo!=null){
            String userNameType = userInfo.getRoleName();
            tvAccount.setText(userNameType);
        }

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
        createMkdirs();
        tempPicPath = Constants.APP_EXTERNAL_PATH + "/temp.jpg";
        tempZoomPicPath = Constants.APP_EXTERNAL_PATH + "/tempZoom.jpg";

        loginPresenter = new LoginPresenter(this);
        userInfoPresenter = new UserInfoPresenter(this);
        userInfoPresenter.getUserInfo(getUserInfoParams());
    }

    /**
     * 退出前提示用户 by zealjiang
     */
    private void exit() {
        View v = UIUtils.inflate(R.layout.dialog_exit);
        MyUniversalDialog myUniversalDialog = new MyUniversalDialog(mainActivity);
        int screenW = ScreenUtils.getScreenWidth(mainActivity);
        int screenH = ScreenUtils.getScreenHeight(mainActivity);
//        int dialogH = (int)(screenH*0.5);
        int dialogH = UIUtils.dip2px(mainActivity,90)+1;
        int dialogW = (int)(screenW*0.8);
        myUniversalDialog.setLayout(v, MyUniversalDialog.DialogGravity.CENTER,dialogW,dialogH);

        TextView cancle = v.findViewById(R.id.cancel);
        TextView exit = v.findViewById(R.id.exit);

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUniversalDialog.cancel();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.loginOut(getParams());
                mainActivity.finish();
            }
        });

        myUniversalDialog.show();

//        ShowMsgDialog.showMaterialDialog2Btn(mainActivity, getResources().getString(R.string.prompt), getResources().getString(R.string.exit_system), new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //取消
//
//            }
//        }, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                loginPresenter.loginOut(getParams());
//                mainActivity.finish();
//            }
//
//        }, getResources().getString(R.string.cancle), getResources().getString(R.string.exit));
    }


    @OnClick({R.id.rlSet, R.id.btExit,R.id.rlUseInfo,R.id.rlFixPassword,R.id.ivPerson})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlSet:
                Intent intent = new Intent(mainActivity, SetActivity.class);
                startActivity(intent);
                break;
            case R.id.btExit:
                exit();
                break;
            case R.id.rlUseInfo:
//                jump(UserInfoActivity.class);
                break;
            case R.id.rlFixPassword:
                jump(FixPasswordActivity.class);
                break;
            case R.id.ivPerson:
                ActionSheet.showSheet(mainActivity, this, getResources().getString(R.string.select_from_album),
                        getResources().getString(R.string.photograph),getResources().getString(R.string.cancle));
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
        //清除缓存  只要点击了退出调用了接口上传了参数不管返回成功失败服务器都将keyId 对应的key 取消，本地要清除缓存
        ACache.get(CMApplication.getAppContext()).remove(Constants.KEY_ACACHE_USER);
        ACache.get(CMApplication.getAppContext()).remove(Constants.KEY_ACACHE_NUMBERCACHE);

        User user1 = CMApplication.getUser();
        NumberCache numberCache = (NumberCache) ACache.get(CMApplication.getAppContext()).getAsObject(Constants.KEY_ACACHE_NUMBERCACHE);
        LogUtil.e("use", "exit use = " + user1);
        LogUtil.e("use", "exit numberCache = " + numberCache);
    }

    @Override //只要点击了退出 就算退出接口调用失败也得清理缓存
    public void Failed() {
        //清除缓存
        ACache.get(CMApplication.getAppContext()).remove(Constants.KEY_ACACHE_USER);
        ACache.get(CMApplication.getAppContext()).remove(Constants.KEY_ACACHE_NUMBERCACHE);


        User user1 = CMApplication.getUser();
        NumberCache numberCache = (NumberCache) ACache.get(CMApplication.getAppContext()).getAsObject(Constants.KEY_ACACHE_NUMBERCACHE);
        LogUtil.e("use", "use = " + user1);
        LogUtil.e("use", "numberCache = " + numberCache);
    }

    private void createMkdirs() {
        File file = new File(Constants.APP_EXTERNAL_PATH);
        if (!file.exists()) file.mkdirs();
    }

    @Override
    public void onWhichClick(int whichButton) {
        switch (whichButton) {
            case ActionSheet.PHOTO: //相册选择  -> 李波 on 2017/3/29.


                    Intent in = new Intent(Intent.ACTION_PICK, null);
                    in.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Constants.IMAGE_UNSPECIFIED);
                    startActivityForResult(in, Constants.PIC_PHOTO);


                break;
            case ActionSheet.CAMERA: //拍照  -> 李波 on 2017/3/29.

                //Manifest.permission.CAMERA 相机权限
                //Manifest.permission.WRITE_EXTERNAL_STORAGE SD卡读写权限
                if (Build.VERSION.SDK_INT >= 23) { //如果系统版本号大于等于23 也就是6.0，就必须动态请求敏感权限（也要配置清单）
                    RxPermissions.getInstance(mainActivity).request(Manifest.permission.CAMERA).subscribe
                            (new Action1<Boolean>() {
                                @Override
                                public void call(Boolean granted) {
                                    if (granted) { //请求获取权限成功后的操作
                                        cameraPic();
                                    } else {
                                        MyToast.showShort("需要获取相机权限");
                                    }
                                }
                            });
                } else { //否则如果是6.0以下系统不需要动态申请权限直接配置清单就可以了
                    cameraPic();
                }

                break;
        }
    }

    /**
     * Created by 李波 on 2017/3/29.
     * 启动相机拍照
     */
    private void cameraPic() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri;
        if (Build.VERSION.SDK_INT >= 24) {
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            imageUri = FileProvider.getUriForFile(mainActivity, "com.base.test", new File(tempPicPath));
        } else {
            imageUri = Uri.fromFile(new File(tempPicPath));
        }
        // 指定照片保存路径（SD卡），temp.jpg为一个临时文件，每次拍照后这个图片都会被替换
        if (imageUri != null) {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(cameraIntent, Constants.PIC_CAMERA);
        }

    }


    /**
     * Created by 李波 on 2017/3/29.
     * 裁剪照片
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent();
        //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //不加报错
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");// mUri是已经选择的图片Uri
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);// 裁剪框比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);// 输出图片大小
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        //intent.putExtra("return-data", true);
        intent.putExtra("return-data", false);
        //设置压缩后的临时图片路径  -> 李波 on 2017/3/29.
        //是裁剪以后的图片保存的地方。也就是我们要写入此Uri.故不需要用FileProvider
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(tempZoomPicPath)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, Constants.PIC_ZOOM);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constants.PIC_PHOTO:
                // 从相册传递
                startPhotoZoom(data.getData());


//                    String tempPath = ImageUtil.getPathforUri(mainActivity,data.getData());
//
//                    ImageUtil.compressLuban(mainActivity, tempPath, new ImageUtil.IcompressListener() {
//                        @Override
//                        public void compressStart() {
//                            showDialog();
//                        }
//
//                        @Override
//                        public void compressEnd() {
//                            dismissDialog();
//                        }
//
//                        @Override
//                        public void compressSuc(File file) {
//                            Bitmap zoomBitmap = ImageUtils.getBitmapByFile(file);
//                            Bitmap output = ImageUtil.toRoundBitmap(zoomBitmap);
//                            ivPerson.setImageBitmap(output);
//                            if (FileUtils.isFileExists(tempZoomPicPath)) FileUtils.deleteFile(tempZoomPicPath);
//                            FileUtils.copyFile(file,new File(tempZoomPicPath));
//                        }
//                    });



                break;
            case Constants.PIC_CAMERA:

//               访问相册需要被限制，需要通过FileProvider创建一个content类型的Uri
//                Uri imageUri;
//                if (Build.VERSION.SDK_INT >= 24) {
//                    //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
//                    imageUri = FileProvider.getUriForFile(mainActivity, "com.base.test", new File(tempPicPath));
//                } else {
//                    imageUri = Uri.fromFile(new File(tempPicPath));
//                }
//                startPhotoZoom(imageUri);

                ImageUtil.compressLuban(mainActivity, tempPicPath, new ImageUtil.IcompressListener() {
                    @Override
                    public void compressStart() {
                        showDialog();
                    }

                    @Override
                    public void compressEnd() {
                        dismissDialog();
                    }

                    @Override
                    public void compressSuc(File file) {
                        Bitmap zoomBitmap = ImageUtils.getBitmapByFile(file);
                        Bitmap output = ImageUtil.toRoundBitmap(zoomBitmap);
                        ivPerson.setImageBitmap(output);
                        if (FileUtils.isFileExists(tempZoomPicPath)) FileUtils.deleteFile(tempZoomPicPath);
                        FileUtils.copyFile(file,new File(tempZoomPicPath));
                    }
                });


                break;
            case Constants.PIC_ZOOM:
                //裁切图片后上传图片 上传的是压缩后的图片
//                mPresenter.upLoadImage("6",tempZoomPicPath);
//                FrescoCacheHelper.clearSingleCacheByUrl(tempZoomPicPath, false);
//                ivAvatar.setImageURI("file://" + tempZoomPicPath);

                  Bitmap bitmap = ImageUtils.getBitmapByFile(tempZoomPicPath);
                  //将bitmap做成圆形图片
                  Bitmap output = ImageUtil.toRoundBitmap(bitmap);
                  ivPerson.setImageBitmap(output);

                break;
        }
    }


    private Map<String, String> getUserInfoParams() {

        Map<String, String> params = new HashMap<>();
        params.put("account", CMApplication.getUser().getAccount());
        params = MD5Utils.encryptParams(params);
        return params;

    }

    @Override
    public void Succeed(UserInfo user) {
        /**
         * account : test123
         * passwordOld : $2a$10$J6KTSSCv2aevAgL3oOwOr.EX9YYKhY0pFp3TAhPT8rHKWPEmTen5a
         * passwordNew :
         * roleName : 站点技术人员
         */
        tvAccount.setText(user.getRoleName());
        ACache.get(mainActivity).put(Constants.KEY_ACACHE_USERINFO,user);
    }
}

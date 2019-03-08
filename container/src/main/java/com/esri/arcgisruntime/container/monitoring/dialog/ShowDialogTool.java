package com.esri.arcgisruntime.container.monitoring.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


import com.esri.arcgisruntime.container.monitoring.R;


/**
 * 弹出框控制类
 */
public class ShowDialogTool {


    private Dialog mLoadingDialog;

    public void showLoadingDialog(Context context,String msg) {
        if (context == null) return;
        if (mLoadingDialog == null) {

            if(android.os.Build.VERSION.SDK_INT>=21){
                mLoadingDialog = new Dialog(context, android.R.style.Theme_Material_Light_Dialog);
            }else{
                mLoadingDialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog);
            }
            mLoadingDialog.setContentView(R.layout.dialog_loading_layout);
            if(!TextUtils.isEmpty(msg)){
                ((TextView)mLoadingDialog.findViewById(R.id.tvDialogMsg)).setText(msg);
            }

			mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.getWindow().setGravity(Gravity.CENTER);
            mLoadingDialog.setCancelable(true);
			mLoadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_SEARCH){
						return true;
					}
					return false;
				}
			});
        }
        if (!mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }

    public void showLoadingDialog(Context context,String msg,boolean isShowLonding) {
        if (context == null) return;
        if (mLoadingDialog == null) {

            if(android.os.Build.VERSION.SDK_INT>=21){
                mLoadingDialog = new Dialog(context, android.R.style.Theme_Material_Light_Dialog);
            }else{
                mLoadingDialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog);
            }
            mLoadingDialog.setContentView(R.layout.dialog_loading_layout);
            if(!TextUtils.isEmpty(msg)){
                ((TextView)mLoadingDialog.findViewById(R.id.tvDialogMsg)).setText(msg);
            }

            if(!isShowLonding){
                mLoadingDialog.findViewById(R.id.progressBar).setVisibility(View.GONE);
            }else{
                mLoadingDialog.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            }

            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.getWindow().setGravity(Gravity.CENTER);
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK){
                        return true;
                    }
                    return false;
                }
            });
        }
        if (!mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }




    public void showLoadingDialog(Context context) {
        if (context == null) return;
        if (mLoadingDialog == null) {

            if(android.os.Build.VERSION.SDK_INT>=21){
                mLoadingDialog = new Dialog(context, android.R.style.Theme_Material_Light_Dialog);
            }else{
                mLoadingDialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Dialog);
            }
            mLoadingDialog.setContentView(R.layout.dialog_loading_layout);
			mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.getWindow().setGravity(Gravity.CENTER);
            mLoadingDialog.setCancelable(true);
			mLoadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
					if(keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK){
						return true;
					}
					return false;
				}
			});
        }
        if (!mLoadingDialog.isShowing())
            mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog = null;
    }



}

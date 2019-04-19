package com.esri.arcgisruntime.container.monitoring.utils;

import android.widget.Toast;

import com.esri.arcgisruntime.container.monitoring.application.CMApplication;

import java.util.Timer;
import java.util.TimerTask;

public class MyToast {
	private static Toast toast;
	public static void showLong(String content){

		Toast.makeText(CMApplication.getAppContext(), content, Toast.LENGTH_LONG).show();

//		if(toast==null){
//			toast = Toast.makeText(CMApplication.getAppContext(), content, Toast.LENGTH_LONG);
//		}else {
//			toast.setText(content);
//		}
//		toast.show();

	}
	public static void showShort(int resId){

		Toast.makeText(CMApplication.getAppContext(), resId, Toast.LENGTH_SHORT).show();

//		if(toast==null){
//			toast = Toast.makeText(CMApplication.getAppContext(), resId, Toast.LENGTH_SHORT);
//		}else{
//			toast.setText(resId);
//		}
//		toast.show();
	}

	public static void showShort(String content){
		Toast.makeText(CMApplication.getAppContext(), content, Toast.LENGTH_SHORT).show();

//		if(toast==null){
//			toast = Toast.makeText(CMApplication.getAppContext(), content, Toast.LENGTH_SHORT);
//		}else{
//			toast.setText(content);
//		}
//		toast.show();
	}
	public static void showLong(int resId){
		if(toast==null){
			toast = Toast.makeText(CMApplication.getAppContext(), resId, Toast.LENGTH_LONG);
		}else{
			toast.setText(resId);
		}
		toast.show();
	}

	/**
	 * Created by 李波 on 2019/3/19.
	 * 自定义时长的Toast
	 * @param cnt 自定义时长
	 */
	public static void showMyToast(final Toast toast, final int cnt) {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				toast.show();
			}
		}, 0, 3000);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				toast.cancel();
				timer.cancel();
			}
		}, cnt );
	}

}

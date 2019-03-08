package com.esri.arcgisruntime.container.monitoring.utils;

import android.widget.Toast;

import com.esri.arcgisruntime.container.monitoring.application.CMApplication;

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
}

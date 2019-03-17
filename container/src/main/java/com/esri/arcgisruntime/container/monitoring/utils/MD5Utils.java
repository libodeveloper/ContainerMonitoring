/**
 * Project Name:JZGPingGuShi
 * File Name:MD5Utils.java
 * Package Name:com.gc.jzgpinggushi.uitls
 * Date:2014-9-1上午10:38:59
 * Copyright (c) 2014, wangyd523@gmail.com All Rights Reserved.
 *
 */

package com.esri.arcgisruntime.container.monitoring.utils;

import android.text.TextUtils;

import com.blankj.utilcode.utils.AppUtils;
import com.esri.arcgisruntime.container.monitoring.application.CMApplication;
import com.esri.arcgisruntime.container.monitoring.bean.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MD5Utils {
	private static String TAG = "MD5Utils";

	public static Map<String,String> encryptParams(Map<String,String> params){
		Map<String,String> mapObject = new HashMap<>();
		Map<String,String> map = generatePublicParams();
		params.putAll(map);
		mapObject.putAll(params);
		String sign = getMD5Sign(mapObject);
		params.put("sign",sign);
		LogUtil.e(TAG,UIUtils.getUrl(params));
		return params;
	}

	public static Map<String, String> generatePublicParams() {
		Map<String, String> params = new HashMap<>();
		params.put("equipmentNo", CommonUtil.devUniqueID(CMApplication.getAppContext()));
		params.put("Telephone", "");
		params.put("appVersion", AppUtils.getAppInfo(CMApplication.getAppContext()).getVersionName());
		params.put("tokenid", "6");
		params.put("From", "android");
		params.put("platform", "1");//1：android，2：iOS
		User user = CMApplication.getAppContext().getUser();
		if (user != null) {
			params.put("userId", String.valueOf(user.getUserId()));
		}
		return params;
	}

	public static Map<String, RequestBody> generateBodyPublicParams(Map<String, String> params,Map<String, RequestBody> mapRequestBody) {
		RequestBody sign = null;
		Map<String, RequestBody> params1 = new HashMap<>();
		RequestBody equipmentNo = RequestBody.create(MediaType.parse("text/plain"), CommonUtil.devUniqueID(CMApplication.getAppContext()));
		RequestBody telephone = RequestBody.create(MediaType.parse("text/plain"),"");
		RequestBody token = RequestBody.create(MediaType.parse("text/plain"), "6");
		RequestBody platType = RequestBody.create(MediaType.parse("text/plain"), "1");
		RequestBody from = RequestBody.create(MediaType.parse("text/plain"), "android");
		RequestBody appVersion = RequestBody.create(MediaType.parse("text/plain"), AppUtils.getAppInfo(CMApplication.getAppContext()).getVersionName());

		if (mapRequestBody==null){
			sign = RequestBody.create(MediaType.parse("text/plain"), MD5Utils.getMD5Sign(params));
		}else {
			params.putAll(generatePublicParams());
			sign = RequestBody.create(MediaType.parse("text/plain"), MD5Utils.getMD5Sign(params));
			params1.putAll(mapRequestBody);
		}


		User user = CMApplication.getAppContext().getUser();
		if (user != null) {
			RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user.getUserId()));
			params1.put("userId", userid);
		}
		params1.put("tokenid", token);
		params1.put("equipmentNo", equipmentNo);
		params1.put("telephone", telephone);
		params1.put("sign", sign);
		params1.put("appVersion", appVersion);
		params1.put("platType", platType);
		params1.put("From", from);

		return params1;
	}

	private static final String PRIVATE_KEY = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".toLowerCase();

	/**
	 *
	 * getMD5Sign
	 *
	 * @Title: getMD5Sign
	 * @Description: 获取MD5加密后的sign字符串
	 * @param @param params 需要传递的参数Map集合，无参状态下可以为null
	 * @return String
	 * @throws
	 */
	public static String getMD5Sign(Map<String, String> params){
		if (params == null || params.size() <= 0)
			return newMD5(PRIVATE_KEY);
		StringBuffer signValue = new StringBuffer();
		List<Map.Entry<String, String>> infos = sortTreeMap(params);
		for (int i = 0; i < infos.size(); i++){
			if(!TextUtils.isEmpty(infos.get(i).toString())){
				String value = infos.get(i).toString();
				signValue.append(value);
			}
		}
		signValue.append(PRIVATE_KEY);
		return newMD5(signValue.toString().toLowerCase());
	}

	/**
	 * sortTreeMap
	 * @Title: sortTreeMap
	 * @Description: 对参数列表进行排序（按照key的字母大小）
	 * @param @param params
	 * @param @return
	 * @return Map<String,Object>
	 * @throws
	 */
	private static List<Map.Entry<String, String>> sortTreeMap(Map<String, String> params){
		List<Map.Entry<String, String>> entrys = new ArrayList<>(params.entrySet());
		Collections.sort(entrys, new Comparator<Map.Entry<String, String>>(){
			public int compare(Map.Entry<String, String> o1,Map.Entry<String, String> o2){
				return (o1.getKey()).toLowerCase().compareTo(o2.getKey().toLowerCase());
			}
		});
		return entrys;
	}


	/**
	 * 
	 * MD5
	 * 
	 * @Title: MD5
	 * @Description: MD5加密方法
	 * @param @param s 需要加密的字符串
	 * @param @return
	 * @return String
	 * @throws
	 */
	private final static String newMD5(String s) {
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f'};
		try {
			byte[] strTemp = s.getBytes("UTF-8");
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str).toUpperCase();
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * 获取文件的MD5值
	 *
	 * @return md5
	 */
	public static String getFileMd5(String fileName) {
		File file = new File(fileName);
		MessageDigest messageDigest;
		FileInputStream fis = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			if (file == null) {
				return "";
			}
			if (!file.exists()) {
				return "";
			}
			int len = 0;
			fis = new FileInputStream(file);
			//普通流读取方式
			byte[] buffer = new byte[1024 * 1024 * 10];
			while ((len = fis.read(buffer)) > 0) {
				//该对象通过使用 update（）方法处理数据
				messageDigest.update(buffer, 0, len);
			}
			BigInteger bigInt = new BigInteger(1, messageDigest.digest());
			String md5 = bigInt.toString(16);
			while (md5.length() < 32) {
				md5 = "0" + md5;
			}
			return md5;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
					fis = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}

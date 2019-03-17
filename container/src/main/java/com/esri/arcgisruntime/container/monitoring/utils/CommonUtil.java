package com.esri.arcgisruntime.container.monitoring.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


public class CommonUtil {

    private static final String TAG = CommonUtil.class.getSimpleName();
    private static String devuid = "";

    /**
     * 设备的唯一标识
     *
     * @param
     * @return
     */
    public static String devUniqueID(Context context) {
        if (!TextUtils.isEmpty(devuid)) {
            return devuid;
        }

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10; //13 digits

        String uuid = "";
        try {
            String androidId = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            UUID uid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
            uuid = uid.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String m_szLongID = uuid + m_szDevIDShort;
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        m.update(m_szLongID.getBytes(), 0, m_szLongID.length());
        byte p_md5Data[] = m.digest();
        String m_szUniqueID = new String();
        for (int i = 0; i < p_md5Data.length; i++) {
            int b = (0xFF & p_md5Data[i]);
            if (b <= 0xF)
                m_szUniqueID += "0";
            m_szUniqueID += Integer.toHexString(b);
        }
        devuid = m_szUniqueID.toUpperCase();
        return devuid;
    }


}
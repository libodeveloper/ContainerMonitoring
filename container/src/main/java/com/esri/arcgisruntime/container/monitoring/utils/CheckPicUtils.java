package com.esri.arcgisruntime.container.monitoring.utils;

import android.util.Log;

import com.blankj.utilcode.utils.FileUtils;
import com.esri.arcgisruntime.container.monitoring.global.Constants;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by libo on 2017/12/15.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class CheckPicUtils {

    private static final String TAG = CheckPicUtils.class.getSimpleName();

    /**
     * 使用正则表达式来判断字符串中是否包含字母
     * @param str 待检验的字符串
     * @return 返回是否包含
     * true: 包含字母 ;false 不包含字母
     */
    public static boolean judgeContainsStr(String str) {
        String regex=".*[a-zA-Z]+.*";
        Matcher m= Pattern.compile(regex).matcher(str);
        return m.matches();
    }

    /**
     * Created by 李波 on 2017/12/15.
     * 判断字符串是否为数字
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * Created by 李波 on 2017/12/15.
     * 检测本地照片名是否被恶意篡改，造成逻辑错误问题
     */
    public static void checkLocalPic(){
                    if(FileUtils.isFileExists(Constants.APP_EXTERNAL_PATH)){
                        File TaskFile = new File(Constants.APP_EXTERNAL_PATH);
                        File[] files =  TaskFile.listFiles();
                        for (File file : files) {
                            String fileName = file.getName();

                            if (fileName.endsWith(".jpg")||fileName.endsWith(".png")) {
                                if (fileName.contains("_")){ //一旦包含下划线的就是被系统恶意命名的 最典型的小米手机
                                    String  correctName = fileName.substring(0,fileName.indexOf("_"))+fileName.substring(fileName.indexOf("."));
                                    reName(fileName, correctName);
                                }
                            }
                        }
                    }
    }

    /**
     * 给恶意篡改照片重命名
     * @param fileName     恶意篡改后的文件名
     * @param correctName  正确的文件名
     */
    private static void reName(String fileName, String correctName) {
        boolean isopyFileSuccess = FileUtils.copyFile(Constants.APP_EXTERNAL_PATH + fileName,Constants.APP_EXTERNAL_PATH + correctName);
        if (isopyFileSuccess){
            FileUtils.deleteFile(Constants.APP_EXTERNAL_PATH + fileName);
        }
    }

}

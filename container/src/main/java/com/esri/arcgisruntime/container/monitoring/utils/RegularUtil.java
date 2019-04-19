package com.esri.arcgisruntime.container.monitoring.utils;

import android.util.Log;
import android.widget.Toast;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.application.CMApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by libo on 2019/4/19.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 密码规则判断
 */
public class RegularUtil {

    /**密码修改符合密码规则：
        建议新密码符合以下复杂度要求。
        密码不能包含用户名或者倒序的用户名。
        不能少于8个字符，或超过32个字符。
        同一字符不能出现超过3次。
        至少包括一个大写字母（A~Z），一个小写字母（a~z），一个数字字符（0~9）。
        不能与最近的3个历史密码重复。*/

    public static boolean isCorrectPassword(String password){

        boolean correct = true;

        if (password.length()< 8 || password.length() > 32){
//            MyToast.showLong("长度不能小于8位 或者 大于32位");
            correct = false;
        }

        if (!judgeUserName(password)) correct = false;

        if (!judgeCharTimes(password)) correct = false;

        if (!judgeChar(password))  correct = false;

        if (!correct){
            Toast toast=Toast.makeText(CMApplication.getAppContext(), CMApplication.getAppContext().getResources().getString(R.string.password_reg), Toast.LENGTH_LONG);
            MyToast.showMyToast(toast, 10*1000);
        }

        return correct;
    }


    /**
     * Created by 李波 on 2019/4/19.
     * 判断是否包含用户名或者倒序用户名
     */
    private static boolean  judgeUserName(String password){
        String userName= CMApplication.getUser().getAccount();

        String resultString = "";

        Log.e("password", "正: "+userName );

        if (password.contains(userName)){
//            MyToast.showShort("密码不能包含用户名");
            return false;
        }

        char[] charArray = userName.toCharArray();

        for (int i=charArray.length-1; i>=0; i--){
            resultString += charArray[i];
        }

        Log.e("password", "反: "+resultString );

        if (password.contains(resultString)){
//            MyToast.showShort("密码不能包含倒序用户名");
            return false;
        }

        return true;
    }

    /**
     * Created by 李波 on 2019/4/19.
     * 判断同一字符不能出现3次
     */
    private static boolean judgeCharTimes(String password){

        char[] pw = password.toCharArray();

        for (int i = 0; i < pw.length; i++) {
            Pattern p = Pattern.compile(String.valueOf(pw[i]));
            Matcher m = p.matcher(password);
            int count = 0;
                while (m.find()) {
                    count++;
                    if (count>=3){
//                        MyToast.showShort("同一字符不能出现超过3次");
                        return false;
                    }
                }
        }

        return true;
    }

    /**
     * Created by 李波 on 2019/4/19.
     * 至少包括一个大写字母（A~Z），一个小写字母（a~z），一个数字字符（0~9）
     * 里面也包含了 8 到 32 位数的判断
     */
    private static boolean judgeChar(String password){
//        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(.{8,32})$");
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9]{8,32}$");
        Matcher matcher = pattern.matcher(password);
        boolean b = matcher.matches();
//        if (!b) MyToast.showShort("至少包括一个大写字母（A~Z），一个小写字母（a~z），一个数字字符（0~9）");
        return b;

    }

}

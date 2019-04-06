package com.esri.arcgisruntime.container.monitoring.utils;

/**
 * Created by libo on 2017/3/3.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 限制EditText输入的字符 只能是数字和英文
 */
public class MyNumberKeyListener extends android.text.method.NumberKeyListener{
    @Override
    protected char[] getAcceptedChars() {
        return new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p',
                'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P',
                'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
    }

    @Override
    public int getInputType() {
        return 1;
    }
}
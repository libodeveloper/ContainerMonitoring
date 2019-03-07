package com.esri.arcgisruntime.container.monitoring.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.dialog.DayPickerDialog;

import java.util.Calendar;

/**
 * Created by libo on 2019/3/7.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 时间工具类
 */
public class TimeUtil {

    /**
     * 滚轮日历
     *
     * @param textView     需要显示日期的 控件
     * @param titlePrefix  title显示如"开始日期"
     * @param laterThanNow 能否选择今天之后的日期 true表示可以，false表示不可以
     */
    public static void selectDate(Context context, final TextView textView, final String titlePrefix, final boolean laterThanNow) {
        int year, month, day;
        String date = textView.getText().toString();
        if (TextUtils.isEmpty(date) || date.length() < 8) {
            Calendar calendar = Calendar.getInstance();
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DATE);
        } else {
            String[] ymd = date.split("-");
            year = Integer.valueOf(ymd[0]);
            month = Integer.valueOf(ymd[1]);
            day = Integer.valueOf(ymd[2]);
        }
        DayPickerDialog dayPickerDialog = new DayPickerDialog(context, titlePrefix, year, month, day, laterThanNow,DayPickerDialog.defaultAfterYearCount);
        dayPickerDialog.createDialog();
        dayPickerDialog.setDayPickerOkListenter(new DayPickerDialog.DayPickerOkListenter() {

            @Override
            public void selectDate(int year, int month, int day, String date) {
                textView.setText(date);
            }
        });
    }

}

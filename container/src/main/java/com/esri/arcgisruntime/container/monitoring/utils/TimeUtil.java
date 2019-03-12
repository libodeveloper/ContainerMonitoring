package com.esri.arcgisruntime.container.monitoring.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
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



//        if (TextUtils.isEmpty(date) || date.length() < 8) {
        if (!date.contains("-")) {
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

    /**
     * Created by 李波 on 2019/3/8.
     * 比较开始时间与结束时间的大小，开始时间不能大于结束时间
     */
    public static boolean compareStartAndEndTime(Context context,String startTime,String endTime) {

        String[] startTimes= startTime.split("-");
        String[] endTimes= endTime.split("-");


        if (startTime.equals(context.getString(R.string.select_starttime))){
            MyToast.showShort(R.string.select_starttime);
            return true;
        }

        if (endTime.equals(context.getString(R.string.select_endtime))){
            MyToast.showShort(R.string.select_endtime);
            return true;
        }

        int startYear = Integer.valueOf(startTimes[0]);
        int startMonth = Integer.valueOf(startTimes[1]);
        int startDay= Integer.valueOf(startTimes[2]);

        int endYear = Integer.valueOf(endTimes[0]);
        int endMonth = Integer.valueOf(endTimes[1]);
        int endDay= Integer.valueOf(endTimes[2]);

        if (startYear>endYear){
            MyToast.showShort(R.string.time_prompt);
            return true;
        }

        if (startYear == endYear){
            if (startMonth>endMonth){
                MyToast.showShort(R.string.time_prompt);
                return true;
            }

            if (startMonth == endMonth){
                if (startDay>endDay){
                    MyToast.showShort(R.string.time_prompt);
                    return true;
                }
            }
        }
        return false;
    }


}

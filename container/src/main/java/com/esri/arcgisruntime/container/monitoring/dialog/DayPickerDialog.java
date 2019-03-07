package com.esri.arcgisruntime.container.monitoring.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.NumberPicker;


import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.utils.MyToast;
import com.esri.arcgisruntime.container.monitoring.utils.UIUtils;
import com.esri.arcgisruntime.container.monitoring.view.QNumberPicker;

import java.util.Calendar;

/**
 * Created by 李波 on 2019/3/7.
 * 日期选择dialog
 */
public class DayPickerDialog {

    private Context context;
    private QNumberPicker npYear;
    private QNumberPicker npMonth;
    private QNumberPicker npDay;
    private String title;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;
    private boolean laterThanNow = true;
    private int moreCurYear;
    private int afterYearCount; //从当前年份往后延多少年  -> 李波 on 2018/3/2.
    public static int  defaultAfterYearCount = 6; //默认往后延6年
    public DayPickerDialog(Context context, String titlePrefix, int selectedYear, int selectedMonth, int selectedDay, final boolean laterThanNow, int afterYearCount) {
        this.context = context;
        this.title = titlePrefix;
        this.selectedYear = selectedYear;
        this.selectedMonth = selectedMonth;
        this.selectedDay = selectedDay;
        this.laterThanNow = laterThanNow;
        this.afterYearCount = afterYearCount;
    }

    public void createDialog(){
        View v = UIUtils.inflate(R.layout.layout_day_picker);
        npYear = (QNumberPicker) v.findViewById(R.id.npYear);
        npMonth = (QNumberPicker) v.findViewById(R.id.npMonth);
        npDay = (QNumberPicker) v.findViewById(R.id.npDay);
        npYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        npDay.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        Calendar calendar = Calendar.getInstance();
        final int nowYear = calendar.get(Calendar.YEAR);
        final int nowMonth = calendar.get(Calendar.MONTH);
        final int nowDay = calendar.get(Calendar.DATE);
        if(laterThanNow){
            moreCurYear = afterYearCount;
        }
        npYear.setMaxValue(nowYear+moreCurYear);
        npYear.setMinValue(nowYear-15);
        npMonth.setMaxValue(12);
        npMonth.setMinValue(1);

        npYear.setValue(selectedYear);
        npMonth.setValue(selectedMonth);
        reSetNpDay();
        npDay.setValue(selectedDay);

        npYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(oldVal!=newVal) {
                    selectedYear = newVal;
                    reSetNpDay();
                }
            }
        });

        npMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener(){

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(oldVal!=newVal){
                    selectedMonth = newVal;
                    reSetNpDay();
                }
            }
        });

        android.app.AlertDialog.Builder builder= new android.app.AlertDialog.Builder(context);
        builder.setView(v);
        builder.setTitle(context.getString(R.string.please_choose)+title).setPositiveButton(context.getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectYear = npYear.getValue();
                int selectMonth = npMonth.getValue();
                int selectDay = npDay.getValue();
                if(!laterThanNow){
                    if(selectYear>nowYear){
                        MyToast.showLong(title+context.getString(R.string.data_err_point));
                        return;
                    }
                    if(selectYear==nowYear && selectMonth>(nowMonth+1)){
                        MyToast.showLong(title+context.getString(R.string.data_err_point));
                        return;
                    }
                    if(selectYear==nowYear && selectMonth==(nowMonth+1)&& selectDay>nowDay){
                        MyToast.showLong(title+context.getString(R.string.data_err_point));
                        return;
                    }
                }
                if(dayPickerOkListenter!=null){
                    dayPickerOkListenter.selectDate(selectYear,selectMonth,selectDay,selectYear+"-"+selectMonth+"-"+selectDay);
                }
                dialog.dismiss();
            }
        }).setNegativeButton(context.getString(R.string.cancle), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }


    private void reSetNpDay(){
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.YEAR,selectedYear);
        ca.set(Calendar.MONTH,selectedMonth-1);
        ca.set(Calendar.DAY_OF_MONTH,1);
        int lastDay = ca.getActualMaximum(Calendar.DATE);

        npDay.setMaxValue(lastDay);
        npDay.setMinValue(1);

    }

    public interface DayPickerOkListenter{
        public void selectDate(int year, int month, int day, String date);
    }

    private DayPickerOkListenter dayPickerOkListenter;

    public void setDayPickerOkListenter(DayPickerOkListenter dayPickerOkListenter){
        this.dayPickerOkListenter = dayPickerOkListenter;
    }
}
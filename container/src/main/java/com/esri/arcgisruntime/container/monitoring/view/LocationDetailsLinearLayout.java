package com.esri.arcgisruntime.container.monitoring.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.esri.arcgisruntime.container.monitoring.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by libo on 2018/12/17.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 位置详情
 */
public class LocationDetailsLinearLayout extends LinearLayout {

    @BindView(R.id.tvPartName)
    TextView tvPartName;
    @BindView(R.id.tvValue)
    TextView tvValue;

    public LocationDetailsLinearLayout(Context context) {
        super(context);
    }

    public LocationDetailsLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LocationDetailsLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.location_details, this);
        ButterKnife.bind(this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LocationDetailsLinearLayout);
        String partName = a.getString(R.styleable.LocationDetailsLinearLayout_partName);
        a.recycle();//当所有属性获取完毕，必须调用此方法
        tvPartName.setText(partName);
    }


    public void setValue(String value){
        tvValue.setText(value);
    }


}



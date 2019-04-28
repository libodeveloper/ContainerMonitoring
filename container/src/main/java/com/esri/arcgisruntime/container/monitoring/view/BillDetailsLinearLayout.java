package com.esri.arcgisruntime.container.monitoring.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by libo on 2018/12/17.
 *
 * @Email: libo@jingzhengu.com
 * @Description: 单据详情布局
 */
public class BillDetailsLinearLayout extends LinearLayout {

    @BindView(R.id.tvPartName)
    TextView tvPartName;
    @BindView(R.id.tvValue)
    TextView tvValue;

    public BillDetailsLinearLayout(Context context) {
        super(context);
    }

    public BillDetailsLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BillDetailsLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.bill_details_layout, this);
        ButterKnife.bind(this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BillDetailsLinearLayout);
        String typeName = a.getString(R.styleable.BillDetailsLinearLayout_typeName);
        a.recycle();//当所有属性获取完毕，必须调用此方法
        tvPartName.setText(typeName);
    }


    public void setValue(String value){
//        if (!TextUtils.isEmpty(value))
            tvValue.setText(value);
    }


}



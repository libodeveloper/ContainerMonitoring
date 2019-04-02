package com.esri.arcgisruntime.container.monitoring;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by libo on 2019/4/2.
 *
 * @Email: libo@jingzhengu.com
 * @Description:
 */
public class SuperViewPager extends ViewPager {

    private ViewPageHelper helper;

    private boolean noScroll = true;

    public SuperViewPager(Context context) {
        this(context,null);
    }

    public SuperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        helper=new ViewPageHelper(this);
    }

    @Override
    public void setCurrentItem(int item) {
        setCurrentItem(item,true);
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        MScroller scroller=helper.getScroller();
        scroller.setNoDuration(true);

        if(Math.abs(getCurrentItem()-item)>1){
            super.setCurrentItem(item, smoothScroll);
//            scroller.setNoDuration(false);
        }else{
//            scroller.setNoDuration(false);
            super.setCurrentItem(item, smoothScroll);
        }
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(ev);

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(ev);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

}

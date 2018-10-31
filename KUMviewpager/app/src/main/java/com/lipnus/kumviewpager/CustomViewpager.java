package com.lipnus.kumviewpager;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by Sunpil on 2017-01-21.
 */

public class CustomViewpager extends android.support.v4.view.ViewPager {

    //생성자 재정의1
    public CustomViewpager(Context context) {
        super(context);
    }

    //생성자 재정의2
    public CustomViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("SSS", "시발");
        return super.onTouchEvent(ev);
    }
}

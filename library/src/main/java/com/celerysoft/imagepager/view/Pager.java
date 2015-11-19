package com.celerysoft.imagepager.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2015-11-18.
 */
public class Pager extends ViewPager {

    public Pager(Context context) {
        super(context);
        initPager();
    }

    private void initPager() {
        this.setOffscreenPageLimit(10);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


}

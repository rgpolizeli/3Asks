package com.rgp.asks.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class DisableSwipeViewPager extends ViewPager {
    private boolean swipeEnabled;

    public DisableSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.swipeEnabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.swipeEnabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.swipeEnabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    /**
     * Enable or disable the ability of swipe to change viewPager's pages.
     *
     * @param swipeEnabled boolean to decide. Default value is true.
     */
    public void setSwipeEnabled(boolean swipeEnabled) {
        this.swipeEnabled = swipeEnabled;
    }
}

package com.yunding.dut.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/5/20.
 */

public class DUTScrollView  extends NestedScrollView{


    public interface ScrollViewListener {
        void onScrollChanged(DUTScrollView dutScrollView, int x, int y, int oldx, int oldy);
    }

    private ScrollViewListener scrollViewListener = null;

    public DUTScrollView(Context context) {
        super(context);
    }

    public DUTScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DUTScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null&&Math.abs(t-oldt)>10) {
            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }


    }



}

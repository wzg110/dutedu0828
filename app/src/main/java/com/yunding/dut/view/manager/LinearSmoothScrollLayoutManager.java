package com.yunding.dut.view.manager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;


public class LinearSmoothScrollLayoutManager extends LinearLayoutManager {

    private Context mContext;

    public LinearSmoothScrollLayoutManager(Context context) {
        super(context);
        this.mContext = context;
        setSpeedFast();
    }

    public LinearSmoothScrollLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        this.mContext = context;
        setSpeedFast();
    }

    private float MILLISECONDS_PER_INCH = 0.03f;

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller scroller = new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.density;
            }
        };
        scroller.setTargetPosition(position);
        startSmoothScroll(scroller);
    }

    public void setSpeedSlow() {
        //自己在这里用density去乘，希望不同分辨率设备上滑动速度相同
        //0.3f是自己估摸的一个值，可以根据不同需求自己修改
        MILLISECONDS_PER_INCH = mContext.getResources().getDisplayMetrics().density * 0.3f;
    }

    public void setSpeedFast() {
        MILLISECONDS_PER_INCH = mContext.getResources().getDisplayMetrics().density * 0.000000001f;
    }
}

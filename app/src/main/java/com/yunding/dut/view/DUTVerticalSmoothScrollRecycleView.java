package com.yunding.dut.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import com.yunding.dut.view.manager.LinearSmoothScrollLayoutManager;


public class DUTVerticalSmoothScrollRecycleView extends DUTVerticalRecyclerView {

    public DUTVerticalSmoothScrollRecycleView(Context context) {
        super(context);
        setLayoutManager(context);
    }

    public DUTVerticalSmoothScrollRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(context);
    }

    public DUTVerticalSmoothScrollRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(context);
    }

    private void setLayoutManager(Context context) {
        LinearSmoothScrollLayoutManager manager = new LinearSmoothScrollLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        setLayoutManager(manager);
    }

}

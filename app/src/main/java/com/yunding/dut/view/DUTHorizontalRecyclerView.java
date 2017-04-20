package com.yunding.dut.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 类 名 称：DUTVerticalRecyclerView
 * <P/>描    述：水平方向上的RecycleView
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 17:28
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 17:28
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class DUTHorizontalRecyclerView extends RecyclerView {
    public DUTHorizontalRecyclerView(Context context) {
        super(context);
        setLayoutManager(context);
    }

    public DUTHorizontalRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setLayoutManager(context);
    }

    public DUTHorizontalRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setLayoutManager(context);
    }

    private void setLayoutManager(Context context) {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        setLayoutManager(manager);
    }
}

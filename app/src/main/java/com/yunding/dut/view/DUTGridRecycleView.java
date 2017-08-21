package com.yunding.dut.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 类 名 称：DUTGridRecycleView
 * <P/>描    述：网格RecycleView 默认2列垂直
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 19:42
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 19:42
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DUTGridRecycleView extends RecyclerView {

    private int mSpanCount=2;

    public DUTGridRecycleView(Context context) {
        super(context);
    }

    public DUTGridRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DUTGridRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setSpanCount(Context context, int spanCount) {
        this.mSpanCount = spanCount;
        setLayoutManager(context);
    }

    private void setLayoutManager(Context context) {
        GridLayoutManager manager = new GridLayoutManager(context, mSpanCount);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        setLayoutManager(manager);
    }
}

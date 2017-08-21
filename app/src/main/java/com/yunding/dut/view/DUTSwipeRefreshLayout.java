package com.yunding.dut.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.yunding.dut.R;

/**
 * 类 名 称：DUTSwipeRefreshLayout
 * <P/>描    述：SwipeRefreshLayout
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 17:33
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 17:33
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DUTSwipeRefreshLayout extends SwipeRefreshLayout {

    public DUTSwipeRefreshLayout(Context context) {
        super(context);
        initSchemeColors(context);
    }

    public DUTSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSchemeColors(context);
    }

    private void initSchemeColors(Context context) {
        setColorSchemeColors(getResources().getColor(R.color.textColorShow));
    }
}

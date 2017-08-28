package com.yunding.dut.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * 类 名 称：dutedu
 * <P/>描    述：
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/22
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/22
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DUTVerticalViewPager extends VerticalViewPager {
    public DUTVerticalViewPager(Context context) {
        super(context);
    }

    public DUTVerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        }catch (Exception e){
        return false;
    }

    }
}

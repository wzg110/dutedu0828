package com.yunding.dut.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 类 名 称：NoScrollViewPager
 * <P/>描    述：NoScrollViewPager
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/19 18:43
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/19 18:43
 * <P/>修改备注：
 * <P/>版    本：
 */
public class NoScrollViewPager extends ViewPager {

	public NoScrollViewPager(Context context) {
		super(context);
	}

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return false;
	}

}

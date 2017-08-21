package com.yunding.dut.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 类 名 称：DUTViewPager
 * <P/>描    述：控制左右滑动viewpager
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/21 11:53
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/21 11:53
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DUTViewPager  extends ViewPager {
    private boolean isScroll=true;
    private float beforeX = -1;
    private boolean isLeft = false; // 判断是否滑向左边
    private SlideCallback sCallback; // 滑动回调
    public DUTViewPager(Context context) {
        super(context);

    }

    public DUTViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * @param isScroll 是否滑动（true 滑动，false 禁止）
     */
    public void setScroll(boolean isScroll){
        this.isScroll=isScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try{
            if(isScroll){
                return super.onInterceptTouchEvent(ev);
            }else  {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN://按下如果‘仅’作为‘上次坐标’，不妥，因为可能存在左滑，motionValue大于0的情况（来回滑，只要停止坐标在按下坐标的右边，左滑仍然能滑过去）
                        beforeX = ev.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float motionValue = ev.getX() - beforeX;
                        if (motionValue < -10) {
                            isLeft=true;
                            return false;
                        }else {
                            isLeft=false;
                        }
                        beforeX = ev.getX();//手指移动时，再把当前的坐标作为下一次的‘上次坐标’，解决上述问题
                        break;
                    case MotionEvent.ACTION_UP:
                        sCallback.changeView(isLeft);
                        break;
                    default:
                        break;
                }
                return super.onInterceptTouchEvent(ev);
            }
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 滑动回调
     */
    public interface SlideCallback {
        /**
         * 切换视图
         * @param left

         */
        void changeView(boolean left);


    }
    public void setsCallback(SlideCallback sCallback) {
        this.sCallback = sCallback;
    }


    }

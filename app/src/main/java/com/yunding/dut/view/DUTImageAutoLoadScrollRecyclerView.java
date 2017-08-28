package com.yunding.dut.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * 类 名 称：dutedu0824
 * <P/>描    述：
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/25
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/25
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DUTImageAutoLoadScrollRecyclerView extends DUTVerticalRecyclerView{
    public DUTImageAutoLoadScrollRecyclerView(Context context) {
        super(context);
    }

    public DUTImageAutoLoadScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        addOnScrollListener(new MyScrollListener());
    }

    public class MyScrollListener extends OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }


        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            switch (newState){
                case SCROLL_STATE_IDLE:
                    //屏幕停止滚动，加载图片
                    try {
                        if (getContext()!=null) Fresco.getImagePipeline().resume();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    break;
                case SCROLL_STATE_DRAGGING:
                    //屏幕滚动切用户的
                    try {
                        if (getContext()!=null) Fresco.getImagePipeline().pause();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case SCROLL_STATE_SETTLING:
                    //由于惯性滚动停止加载图片
                    try {
                        if (getContext()!=null) Fresco.getImagePipeline().pause();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }

}

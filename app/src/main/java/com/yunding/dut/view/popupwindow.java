package com.yunding.dut.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yunding.dut.R;
import com.yunding.dut.model.resp.translate.JSTranslateBean;
import com.yunding.dut.model.resp.translate.YDTranslateBean;

import java.io.IOException;

/**
 * 类 名 称：ReadingInputQuestionFragment
 * <P/>描    述：阅读填空题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/7/14
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/7/14
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class popupwindow {


    public static PopupWindow showTipPopupWindow(final View anchorView, final String content, final String time, final View.OnClickListener onClickListener) {
        final View contentView = LayoutInflater.from(anchorView.getContext()).inflate(R.layout.popuw_content_top_arrow_layout, null);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 创建PopupWindow时候指定高宽时showAsDropDown能够自适应
        // 如果设置为wrap_content,showAsDropDown会认为下面空间一直很充足（我以认为这个Google的bug）
        // 备注如果PopupWindow里面有ListView,ScrollView时，一定要动态设置PopupWindow的大小
        final PopupWindow popupWindow = new PopupWindow(contentView,
                contentView.getMeasuredWidth(), contentView.getMeasuredHeight(), false);
        TextView tv= (TextView) contentView.findViewById(R.id.tip_text);
        TextView tv_time= (TextView) contentView.findViewById(R.id.tv_time);
        LinearLayout tl= (LinearLayout) contentView.findViewById(R.id.rl_content);
        tv.setText(time);
        tv_time.setText(content);
//        contentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
//                onClickListener.onClick(v);
//            }
//        });

        tl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                onClickListener.onClick(v);
            }
        });
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                onClickListener.onClick(v);
            }
        });

        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 自动调整箭头的位置
                autoAdjustArrowPos(popupWindow, contentView, anchorView);
                contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        popupWindow.setClippingEnabled(true);

        // setOutsideTouchable设置生效的前提是setTouchable(true)和setFocusable(false)
        popupWindow.setOutsideTouchable(true);

        // 设置为true之后，PopupWindow内容区域 才可以响应点击事件
        popupWindow.setTouchable(true);

        // true时，点击返回键先消失 PopupWindow
        // 但是设置为true时setOutsideTouchable，setTouchable方法就失效了（点击外部不消失，内容区域也不响应事件）
        // false时PopupWindow不处理返回键
        popupWindow.setFocusable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;   // 这里面拦截不到返回键
            }
        });


        // 如果希望showAsDropDown方法能够在下面空间不足时自动在anchorView的上面弹出
        // 必须在创建PopupWindow的时候指定高度，不能用wrap_content
        popupWindow.showAsDropDown(anchorView);
        return popupWindow;
    }
    public static PopupWindow showYDTranslateWindow(Context context,final YDTranslateBean bean,final  float[]a){
        final View contentView = LayoutInflater.from(context).inflate(R.layout.layout_translate_popup, null);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 创建PopupWindow时候指定高宽时showAsDropDown能够自适应
        // 如果设置为wrap_content,showAsDropDown会认为下面空间一直很充足（我以认为这个Google的bug）
        // 备注如果PopupWindow里面有ListView,ScrollView时，一定要动态设置PopupWindow的大小
        final PopupWindow popupWindow = new PopupWindow(contentView,
                contentView.getMeasuredWidth(), contentView.getMeasuredHeight(), false);
        TextView tv_text_translate= (TextView) contentView.findViewById(R.id.tv_text_translate);
        TextView tv_phonogram= (TextView) contentView.findViewById(R.id.tv_phonogram1);
        tv_phonogram.setVisibility(View.GONE);
        ImageView iv_speak= (ImageView) contentView.findViewById(R.id.iv_speak);
        iv_speak.setVisibility(View.GONE);
        TextView tip_text= (TextView) contentView.findViewById(R.id.tip_text);
        tv_text_translate.setText(bean.getQuery());
        tip_text.setText(bean.getTranslation().get(0));
//        contentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                popupWindow.dismiss();
////                onClickListener.onClick(v);
//            }
//        });

//        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                // 自动调整箭头的位置
//                autoAdjustArrowPos(popupWindow, contentView, anchorView);
//                contentView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//            }
//        });
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setClippingEnabled(true);

        // setOutsideTouchable设置生效的前提是setTouchable(true)和setFocusable(false)
        popupWindow.setOutsideTouchable(true);

        // 设置为true之后，PopupWindow内容区域 才可以响应点击事件
        popupWindow.setTouchable(true);

        // true时，点击返回键先消失 PopupWindow
        // 但是设置为true时setOutsideTouchable，setTouchable方法就失效了（点击外部不消失，内容区域也不响应事件）
        // false时PopupWindow不处理返回键
        popupWindow.setFocusable(false);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;   // 这里面拦截不到返回键
            }
        });
        // 如果希望showAsDropDown方法能够在下面空间不足时自动在anchorView的上面弹出
        // 必须在创建PopupWindow的时候指定高度，不能用wrap_content
//        popupWindow.showAsDropDown(contentView,(int)a[0],(int)a[1]);

        popupWindow.showAtLocation( contentView , Gravity.CENTER,0,0);
        return popupWindow;
    }

    public static PopupWindow showJSTranslateWindow(Context context, final JSTranslateBean bean,final float[]a){
         final MediaPlayer player;
        player=new MediaPlayer();
        final View contentView = LayoutInflater.from(context).inflate(R.layout.layout_translate_popup, null);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 创建PopupWindow时候指定高宽时showAsDropDown能够自适应
        // 如果设置为wrap_content,showAsDropDown会认为下面空间一直很充足（我以认为这个Google的bug）
        // 备注如果PopupWindow里面有ListView,ScrollView时，一定要动态设置PopupWindow的大小
        final PopupWindow popupWindow = new PopupWindow(contentView,
                contentView.getMeasuredWidth(), contentView.getMeasuredHeight(), false);
        JSTranslateBean.SymbolsBean partsBean = bean.getSymbols().get(0);
        TextView tv_text_translate= (TextView) contentView.findViewById(R.id.tv_text_translate);
        TextView tv_phonogram= (TextView) contentView.findViewById(R.id.tv_phonogram1);
        ImageView iv_speak= (ImageView) contentView.findViewById(R.id.iv_speak);
        TextView tip_text= (TextView) contentView.findViewById(R.id.tip_text);
        tv_text_translate.setText(bean.getWord_name());
//        Log.e("仪表",partsBean.getPh_en());
        tv_phonogram.setVisibility(View.VISIBLE);

        tv_phonogram.setText("[" + partsBean.getPh_en() + "]");

        StringBuffer strTranslate = new StringBuffer("");
        for (int i = 0; i < partsBean.getParts().size(); i++) {
            strTranslate.append(partsBean.getParts().get(i).getPart() + "   " + partsBean.getParts().get(i).getMeans() + "\n");
        }
        String str = strTranslate.toString();
        if (str!=null){
            tip_text.setText(str);
        }else{
            tip_text.setText("Sorry，木有查到。");
        }
        String readUrl="";
        if (!TextUtils.isEmpty(partsBean.getPh_en_mp3())){
            readUrl=partsBean.getPh_en_mp3();
        }else if (!TextUtils.isEmpty(partsBean.getPh_am_mp3())){
            readUrl=partsBean.getPh_am_mp3();
        }else if (!TextUtils.isEmpty(partsBean.getPh_tts_mp3())){
            readUrl=partsBean.getPh_tts_mp3();
        }else{
            readUrl="";
                    }

        final String finalReadUrl = readUrl;
        if (TextUtils.isEmpty(finalReadUrl)){
            iv_speak.setVisibility(View.GONE);
        }else{
        iv_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalReadUrl != null) {

                            player.reset();
                            try {
                                player.setDataSource(finalReadUrl);
                                //准备
                                player.prepare();
                                //播放
                                player.start();


                            } catch (IOException e) {
                                e.printStackTrace();

                            }
                        }
                    }
                }).start();
                }});
        }
                popupWindow.setBackgroundDrawable(new ColorDrawable());
                popupWindow.setClippingEnabled(true);

                // setOutsideTouchable设置生效的前提是setTouchable(true)和setFocusable(false)
                popupWindow.setOutsideTouchable(true);

                // 设置为true之后，PopupWindow内容区域 才可以响应点击事件
                popupWindow.setTouchable(true);

                // true时，点击返回键先消失 PopupWindow
                // 但是设置为true时setOutsideTouchable，setTouchable方法就失效了（点击外部不消失，内容区域也不响应事件）
                // false时PopupWindow不处理返回键
                popupWindow.setFocusable(false);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;   // 这里面拦截不到返回键
                    }
                });
                // 如果希望showAsDropDown方法能够在下面空间不足时自动在anchorView的上面弹出
                // 必须在创建PopupWindow的时候指定高度，不能用wrap_content
//                popupWindow.showAsDropDown(contentView,(int)a[0],(int)a[1]);
            popupWindow.showAtLocation( contentView , Gravity.CENTER,0,0);
                return popupWindow;
            }

    private static void autoAdjustArrowPos(PopupWindow popupWindow, View contentView, View anchorView) {
        View upArrow = contentView.findViewById(R.id.up_arrow);
        View downArrow = contentView.findViewById(R.id.down_arrow);

        int pos[] = new int[2];
        contentView.getLocationOnScreen(pos);
        int popLeftPos = pos[0];
        anchorView.getLocationOnScreen(pos);
        int anchorLeftPos = pos[0];
        int arrowLeftMargin = anchorLeftPos - popLeftPos + anchorView.getWidth() / 2 - upArrow.getWidth() / 2;
        upArrow.setVisibility(popupWindow.isAboveAnchor() ? View.INVISIBLE : View.VISIBLE);
        downArrow.setVisibility(popupWindow.isAboveAnchor() ? View.VISIBLE : View.INVISIBLE);

        LinearLayout.LayoutParams upArrowParams = (LinearLayout.LayoutParams) upArrow.getLayoutParams();
        upArrowParams.leftMargin = arrowLeftMargin;
        LinearLayout.LayoutParams downArrowParams = (LinearLayout.LayoutParams) downArrow.getLayoutParams();
        downArrowParams.leftMargin = arrowLeftMargin;
    }
}

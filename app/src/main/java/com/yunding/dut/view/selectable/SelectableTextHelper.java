package com.yunding.dut.view.selectable;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.util.third.SizeUtils;

import java.util.List;

/**
 * Created by Jaeger on 16/8/30.
 * <p>
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
public class SelectableTextHelper {

    private final static int DEFAULT_SELECTION_LENGTH = 1;
    private static final int DEFAULT_SHOW_DURATION = 100;

    private CursorHandle mStartHandle;
    private CursorHandle mEndHandle;
    private OperateWindow mOperateWindow;
    private OperateWindowTwo mOperateWindowTwo;
    private SelectionInfo mSelectionInfo = new SelectionInfo();
    private OnSelectListener mSelectListener;
    private boolean mIsShowingSelect;

    private Context mContext;
    private TextView mTextView;
    private Spannable mSpannable;

    private int mTouchX;
    private int mTouchY;
    private  int mTouchXabs;
    private int mTouchYabs;
    private  int chooseTips;
    private int mSelectedColor;
    private int mCursorHandleColor;
    private int mCursorHandleSize;
    private BackgroundColorSpan mSpan;
    private boolean isHideWhenScroll;
    private boolean isHide = true;
    private  boolean whichEvent=false;//触发的是什么时间默认长按 点击 true
    private static final String TAG = "SelectableTextHelper";
    private List<ReadingListResp.DataBean.NewWordsBean> mSentenceInfoBean;
    private ReadingListResp.DataBean mReadingBean;

    public boolean isShowingOperateSelect() {
        return isShowingOperateSelect;
    }

    public void setShowingOperateSelect(boolean showingOperateSelect) {
        isShowingOperateSelect = showingOperateSelect;
    }

    private boolean isShowingOperateSelect=false;

    private ViewTreeObserver.OnPreDrawListener mOnPreDrawListener;
    ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener;

    public SelectableTextHelper(Builder builder) {
        mTextView = builder.mTextView;
        mContext = mTextView.getContext();
        mReadingBean=builder.mReadingBean;
        mSelectedColor = builder.mSelectedColor;
        mCursorHandleColor = builder.mCursorHandleColor;
        mCursorHandleSize = TextLayoutUtil.dp2px(mContext, builder.mCursorHandleSizeInDp);
        mSentenceInfoBean=builder.mSentenceInfoBean;
        mIsShowingSelect=builder.mIsShowingSelect;
        init();
    }
        public void updataNewWordsBean(List<ReadingListResp.DataBean.NewWordsBean> newBean){
            this.mSentenceInfoBean=newBean;

        }
        public void showTwo(int position){
            resetSelectionInfo();
            hideSelectView();
            whichEvent=true;
            isHide = false;
            chooseTips=position;
//                    Log.e("点击词",mSentenceInfoBean.get(chooseTips).getWord());

            mTouchXabs = (int) mSentenceInfoBean.get(chooseTips).getWordIndex();
            mTouchYabs= (int) mSentenceInfoBean.get(chooseTips).getWordIndex()+mSentenceInfoBean.get(chooseTips).getWordLength();
            if (mOperateWindowTwo!=null){
                if (mOperateWindowTwo.isShowing()){
                    mOperateWindowTwo.dismiss();
                }else{
                    mOperateWindowTwo.show();
                }

            }

        }

    private void init() {
        mTextView.setText(mTextView.getText(), TextView.BufferType.SPANNABLE);



        mTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {



                whichEvent=false;
                String positionInfo=checkPositionIncludeTips(mTouchX,mTouchY);
                if (TextUtils.isEmpty(positionInfo)){
                    if (mOperateWindowTwo!=null){
                        mOperateWindowTwo.dismiss();
                    }
                    showSelectView(mTouchX, mTouchY);
                }else{

                }

                return true;
            }
        });

        mTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTouchX = (int) event.getX()-5;
                mTouchY = (int) event.getY()-5;


                return false;
            }
        });

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("返回值",mTouchX+"    "+mTouchY);
                String positionInfo=checkPositionIncludeTips(mTouchX,mTouchY);
//                Log.e("返回值",positionInfo);
                if (TextUtils.isEmpty(positionInfo)){
//                    判断点击位置的文章没有标记
//                    标记则弹operateTwo
                    whichEvent=false;

//                    hideOperateWindowTwo();
                    resetSelectionInfo();
//                    hideSelectView();
                    if (mSelectListener!=null){
                        mSelectListener.onTextClick(mTouchX,mTouchY);
                    }

                }else{
                    resetSelectionInfo();
                    hideSelectView();
                    whichEvent=true;
                    isHide = false;
                    chooseTips=Integer.valueOf(positionInfo);
//                    Log.e("点击词",mSentenceInfoBean.get(chooseTips).getWord());

                    mTouchXabs = (int) mSentenceInfoBean.get(chooseTips).getWordIndex();
                    mTouchYabs= (int) mSentenceInfoBean.get(chooseTips).getWordIndex()+mSentenceInfoBean.get(chooseTips).getWordLength();
                    if (mOperateWindowTwo!=null){
                        if (mOperateWindowTwo.isShowing()){
                            mOperateWindowTwo.dismiss();
                        }else{
                            mOperateWindowTwo.show();
                        }

                    }


                }

            }
        });
        mTextView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                destroy();
            }
        });

        mOnPreDrawListener = new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (isHideWhenScroll) {
                    isHideWhenScroll = false;
                    postShowSelectView(DEFAULT_SHOW_DURATION);
                }
                return true;
            }
        };
        mTextView.getViewTreeObserver().addOnPreDrawListener(mOnPreDrawListener);

        mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
//                Log.e("dasdasdasd",isHideWhenScroll+"-----"+isHide+"");
                if (!isHideWhenScroll && !isHide) {
                    isHideWhenScroll = true;
                    if (mOperateWindow != null) {
                        mOperateWindow.dismiss();
                    }
                    if (mStartHandle != null) {
                        mStartHandle.dismiss();
                    }
                    if (mEndHandle != null) {
                        mEndHandle.dismiss();
                    }
                    if (mOperateWindowTwo!=null){
                        mOperateWindowTwo.dismiss();
                    }
                }
            }
        };
        mTextView.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener);

        mOperateWindow = new OperateWindow(mContext);
        mOperateWindowTwo=new OperateWindowTwo(mContext);

    }

    /**
     * 关闭窗口
     */
    private void hideOperateWindowTwo() {
        isHide = true;
        if (mOperateWindowTwo != null) {
            mOperateWindowTwo.dismiss();
        }
    }

    /**
     * 校验点击是否有标记
     * @param mTouchX
     * @param mTouchY
     * @return 具体第几个标记
     */
    private String checkPositionIncludeTips(int mTouchX, int mTouchY) {
//     点击位置的起始位置
        int startOffsetTouch = TextLayoutUtil.getPreciseOffset(mTextView, mTouchX, mTouchY);
//        Log.e("校验点击是否有标记",startOffsetTouch+"");

        for (int i=0;i<mSentenceInfoBean.size();i++){
            int startPosition=mSentenceInfoBean.get(i).getWordIndex();
            int endPostion=startPosition+mSentenceInfoBean.get(i).getWordLength();
            if (startPosition <=startOffsetTouch &&
                    startOffsetTouch<=endPostion){

                return i+"";

            }
        }
        return "";
    }

    private void postShowSelectView(int duration) {
        mTextView.removeCallbacks(mShowSelectViewRunnable);
        if (duration <= 0) {
            mShowSelectViewRunnable.run();
        } else {
            mTextView.postDelayed(mShowSelectViewRunnable, duration);
        }
    }

    private final Runnable mShowSelectViewRunnable = new Runnable() {
        @Override
        public void run() {

            if (isHide) return;

            if (whichEvent){
                if (mOperateWindowTwo!=null){
                    mOperateWindowTwo.show();
                }
            }else{


            if (mOperateWindow != null) {
                mOperateWindow.show();
            }
            if (mStartHandle != null) {
                showCursorHandle(mStartHandle);
            }
            if (mEndHandle != null) {
                showCursorHandle(mEndHandle);
            }

            }
        }
    };

    private void hideSelectView() {
        isHide = true;
        if (mStartHandle != null) {
            mStartHandle.dismiss();
        }
        if (mEndHandle != null) {
            mEndHandle.dismiss();
        }
        if (mOperateWindow != null) {
            mOperateWindow.dismiss();
        }
    }

    private void resetSelectionInfo() {
        mSelectionInfo.mSelectionContent = null;
        if (mSpannable != null && mSpan != null) {
            mSpannable.removeSpan(mSpan);
            mSpan = null;
        }
    }

    private void showSelectView(int x, int y) {
        hideSelectView();
        resetSelectionInfo();
        isHide = false;
        if (mStartHandle == null) mStartHandle = new CursorHandle(true);
        if (mEndHandle == null) mEndHandle = new CursorHandle(false);

        int startOffset = TextLayoutUtil.getPreciseOffset(mTextView, x, y);
        int endOffset = startOffset + DEFAULT_SELECTION_LENGTH;
        int startNew=startOffset;

        int endNew=startOffset + DEFAULT_SELECTION_LENGTH;
        char [] text=mTextView.getText().toString().toCharArray();
//        Log.e("来了",startOffset+"       "+text.length);

        if (startOffset==text.length){

            startNew=text.length-2;
            endNew=text.length-1;


        }else{
            for(int i=startOffset;i>0;i--){
            if (Character.isWhitespace(text[i])){
                startNew=i+1;
//                Log.e("i",i+"");
                break;
            }else{
                startNew=0;
            }
            }
            for(int j=startOffset;j<text.length;j++){
                if (Character.isWhitespace(text[j])){
                    endNew=j;
                    break;
                }
            }
        }

        if (mTextView.getText() instanceof Spannable) {
            mSpannable = (Spannable) mTextView.getText();
        }
        if (mSpannable == null || startOffset >= mTextView.getText().length()) {
            return;
        }
        selectText(startNew, endNew);
        showCursorHandle(mStartHandle);
        showCursorHandle(mEndHandle);
        mOperateWindow.show();
    }

    private void showCursorHandle(CursorHandle cursorHandle) {
        Layout layout = mTextView.getLayout();
        int offset = cursorHandle.isLeft ? mSelectionInfo.mStart : mSelectionInfo.mEnd;
        cursorHandle.show((int) layout.getPrimaryHorizontal(offset), layout.getLineBottom(layout.getLineForOffset(offset)));
    }

    private void selectText(int startPos, int endPos) {
        if (startPos != -1) {
            mSelectionInfo.mStart = startPos;
        }
        if (endPos != -1) {
            mSelectionInfo.mEnd = endPos;
        }
        if (mSelectionInfo.mStart > mSelectionInfo.mEnd) {
            int temp = mSelectionInfo.mStart;
            mSelectionInfo.mStart = mSelectionInfo.mEnd;
            mSelectionInfo.mEnd = temp;
        }

        if (mSpannable != null) {
            if (mSpan == null) {
                mSpan = new BackgroundColorSpan(mSelectedColor);
            }
            mSelectionInfo.mSelectionContent = mSpannable.subSequence(mSelectionInfo.mStart, mSelectionInfo.mEnd).toString();
            mSpannable.setSpan(mSpan, mSelectionInfo.mStart, mSelectionInfo.mEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            if (mSelectListener != null) {
//                mSelectListener.onTextSelected(mSelectionInfo.mSelectionContent);//Edit by masiyuan
//                mSelectListener.onTextSelected(mSelectionInfo.mSelectionContent, mSelectionInfo.mStart, mSelectionInfo.mEnd);//Edit by masiyuan
            }
        }
    }

    public void setSelectListener(OnSelectListener selectListener) {
        mSelectListener = selectListener;
    }
public void dismissWindow(boolean aaa){
    isShowingOperateSelect=false;
    if (mOperateWindow!=null&&mOperateWindow.isShowing()&&aaa){
                 hideSelectView();
    }
    if (mOperateWindowTwo!=null&&mOperateWindowTwo.isShowing()&&aaa){
                 hideOperateWindowTwo();
    }
}


    public void destroy() {
        mTextView.getViewTreeObserver().removeOnScrollChangedListener(mOnScrollChangedListener);
        mTextView.getViewTreeObserver().removeOnPreDrawListener(mOnPreDrawListener);
        resetSelectionInfo();
        hideSelectView();
        hideOperateWindowTwo();
        mStartHandle = null;
        mEndHandle = null;
        mOperateWindow = null;
        mOperateWindowTwo=null;
    }



    private class OperateWindowTwo{
        private PopupWindow mWindows;
        private int[] mTempCoors = new int[2];

        private int mWidth;
        private int mHeight;
        private ImageView iv_pink,iv_green,iv_yellow,iv_blue,iv_purple;
        private LinearLayout ll_operate,ll_toast;
        private Button btn_ok,btn_cancel;
        public OperateWindowTwo(final Context context){
            View contentView = LayoutInflater.from(context).inflate(R.layout.layout_operate_two, null);
            contentView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            mWidth = contentView.getMeasuredWidth();
            mHeight = contentView.getMeasuredHeight();
            mWindows =
                    new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, SizeUtils.dp2px(60), false);
            mWindows.setClippingEnabled(false);

            iv_pink= (ImageView) contentView.findViewById(R.id.iv_pink);
            iv_yellow= (ImageView) contentView.findViewById(R.id.iv_yellow);
            iv_green= (ImageView) contentView.findViewById(R.id.iv_green);
            iv_blue= (ImageView) contentView.findViewById(R.id.iv_blue);
            iv_purple= (ImageView) contentView.findViewById(R.id.iv_purple);

            ll_operate= (LinearLayout) contentView.findViewById(R.id.ll_operate);
            ll_toast= (LinearLayout) contentView.findViewById(R.id.ll_toast);
            btn_cancel= (Button) contentView.findViewById(R.id.btn_cancel);
            btn_ok= (Button) contentView.findViewById(R.id.btn_ok);

//            ll_operate.setVisibility(View.VISIBLE);
//            ll_toast.setVisibility(View.GONE);

            //做笔记
            contentView.findViewById(R.id.tv_notes).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectListener != null) {
                        mSelectListener.onTextNote(mSentenceInfoBean.get(chooseTips).getWord(),chooseTips);

                    }
//                    SelectableTextHelper.this.resetSelectionInfo();
//                    SelectableTextHelper.this.hideSelectView();
                    SelectableTextHelper.this.hideOperateWindowTwo();
                }
            });
//            收藏
            contentView.findViewById(R.id.tv_collection).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectListener != null) {

                        mSelectListener.onTextCollect( mSentenceInfoBean.get(chooseTips).getWord(),mSentenceInfoBean.get(chooseTips).getNewWordId());
                    }
//                    SelectableTextHelper.this.resetSelectionInfo();
//                    SelectableTextHelper.this.hideSelectView();
                    SelectableTextHelper.this.hideOperateWindowTwo();
                }
            });
//            删除
            contentView.findViewById(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSentenceInfoBean.get(chooseTips).getNoted()==0){
                        mSelectListener.onTextDelete(mSentenceInfoBean.get(chooseTips).getNewWordId());
                        SelectableTextHelper.this.hideOperateWindowTwo();
                    }else{


                    ll_operate.setVisibility(View.GONE);
                    ll_toast.setVisibility(View.VISIBLE);
                    btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mSelectListener != null) {
//                        mSelectListener.onTextSelected(mSelectionInfo.mSelectionContent, mSelectionInfo.mStart, mSelectionInfo.mEnd);
                                mSelectListener.onTextDelete(mSentenceInfoBean.get(chooseTips).getNewWordId());
//                        Toast.makeText(context,"删除",Toast.LENGTH_SHORT).show();
                                ll_operate.setVisibility(View.VISIBLE);
                                ll_toast.setVisibility(View.GONE);
                            }
                            SelectableTextHelper.this.hideOperateWindowTwo();
                        }
                    });
                    btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ll_operate.setVisibility(View.VISIBLE);
                            ll_toast.setVisibility(View.GONE);
                            SelectableTextHelper.this.hideOperateWindowTwo();

                        }
                    });

                    }

//                    SelectableTextHelper.this.resetSelectionInfo();
//                    SelectableTextHelper.this.hideSelectView();
                }

            });
            //            翻译
            contentView.findViewById(R.id.tv_translate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    float [] a=new float[2];
                    a[0]=mTouchX;
                    a[1]=mTouchY;
                    if (mSelectListener != null) {
                        mSelectListener.onTextTranslate(mSentenceInfoBean.get(chooseTips).getWord(),a);
                    }
//                    SelectableTextHelper.this.resetSelectionInfo();
//                    SelectableTextHelper.this.hideSelectView();
                    SelectableTextHelper.this.hideOperateWindowTwo();
                }
            });
            //           复制
            contentView.findViewById(R.id.tv_copy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectListener != null) {
                        ClipboardManager clip = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        clip.setPrimaryClip(ClipData.newPlainText(mSentenceInfoBean.get(chooseTips).getWord(),mSentenceInfoBean.get(chooseTips).getWord()));
                        Toast.makeText(DUTApplication.getInstance().getApplicationContext(),"已复制到剪贴板",Toast.LENGTH_SHORT).show();
//                        SelectableTextHelper.this.resetSelectionInfo();
//                        SelectableTextHelper.this.hideSelectView();
                    }
                    SelectableTextHelper.this.hideOperateWindowTwo();
//                    SelectableTextHelper.this.resetSelectionInfo();
//                    SelectableTextHelper.this.hideSelectView();
                }
            });
            //            切换粉色

            iv_pink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectListener != null) {
//                        mSelectListener.onTextSelected(mSelectionInfo.mSelectionContent, mSelectionInfo.mStart, mSelectionInfo.mEnd);
                        mSelectListener.onChangeColor(chooseTips,R.style.style_pink,mSentenceInfoBean.get(chooseTips).getNewWordId(),"1");
                        iv_pink.setImageResource(R.drawable.ic_pink_long);
                        iv_yellow.setImageResource(R.drawable.ic_yellow_short);
                        iv_green.setImageResource(R.drawable.ic_green_short);
                        iv_blue.setImageResource(R.drawable.ic_blue_short);
                        iv_purple.setImageResource(R.drawable.ic_purple_short);
                    }
//                    SelectableTextHelper.this.resetSelectionInfo();
//                    SelectableTextHelper.this.hideSelectView();
//                    SelectableTextHelper.this.hideOperateWindowTwo();
                }
            });

            //            切换黄色
            iv_yellow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectListener != null) {
//                        mSelectListener.onTextSelected(mSelectionInfo.mSelectionContent, mSelectionInfo.mStart, mSelectionInfo.mEnd);
                        mSelectListener.onChangeColor(chooseTips,R.style.style_yellow,mSentenceInfoBean.get(chooseTips).getNewWordId(),"2");
                        iv_yellow.setImageResource(R.drawable.ic_yellow_long);
                        iv_pink.setImageResource(R.drawable.ic_pink_short);
                        iv_green.setImageResource(R.drawable.ic_green_short);
                        iv_blue.setImageResource(R.drawable.ic_blue_short);
                        iv_purple.setImageResource(R.drawable.ic_purple_short);

                    }
//                    SelectableTextHelper.this.resetSelectionInfo();
//                    SelectableTextHelper.this.hideSelectView();
                }
            });
            //            切换绿色
            iv_green.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectListener != null) {
//                        mSelectListener.onTextSelected(mSelectionInfo.mSelectionContent, mSelectionInfo.mStart, mSelectionInfo.mEnd);

                        mSelectListener.onChangeColor(chooseTips,R.style.style_green,mSentenceInfoBean.get(chooseTips).getNewWordId(),"3");
                        iv_green.setImageResource(R.drawable.ic_green_long);
                        iv_pink.setImageResource(R.drawable.ic_pink_short);
                        iv_yellow.setImageResource(R.drawable.ic_yellow_short);
                        iv_blue.setImageResource(R.drawable.ic_blue_short);
                        iv_purple.setImageResource(R.drawable.ic_purple_short);
                    }
//                    SelectableTextHelper.this.resetSelectionInfo();
//                    SelectableTextHelper.this.hideSelectView();
                }
            });
            //            切换蓝色
            contentView.findViewById(R.id.iv_blue).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectListener != null) {
//                        mSelectListener.onTextSelected(mSelectionInfo.mSelectionContent, mSelectionInfo.mStart, mSelectionInfo.mEnd);

                        mSelectListener.onChangeColor(chooseTips, R.style.style_blue,mSentenceInfoBean.get(chooseTips).getNewWordId(),"4");
                        iv_green.setImageResource(R.drawable.ic_green_short);
                        iv_pink.setImageResource(R.drawable.ic_pink_short);
                        iv_yellow.setImageResource(R.drawable.ic_yellow_short);
                        iv_blue.setImageResource(R.drawable.ic_blue_long);
                        iv_purple.setImageResource(R.drawable.ic_purple_short);

                    }
//                    SelectableTextHelper.this.resetSelectionInfo();
//                    SelectableTextHelper.this.hideSelectView();
                }
            });
            //            切换紫色
            contentView.findViewById(R.id.iv_purple).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectListener != null) {
//                        mSelectListener.onTextSelected(mSelectionInfo.mSelectionContent, mSelectionInfo.mStart, mSelectionInfo.mEnd);
                        mSelectListener.onChangeColor(chooseTips,R.style.style_purple,mSentenceInfoBean.get(chooseTips).getNewWordId(),"5");
                        iv_green.setImageResource(R.drawable.ic_green_short);
                        iv_pink.setImageResource(R.drawable.ic_pink_short);
                        iv_yellow.setImageResource(R.drawable.ic_yellow_short);
                        iv_blue.setImageResource(R.drawable.ic_blue_short);
                        iv_purple.setImageResource(R.drawable.ic_purple_long);

                    }
//                    SelectableTextHelper.this.resetSelectionInfo();
//                    SelectableTextHelper.this.hideSelectView();
                }
            });

        }
        public boolean isShowing(){
            if (mOperateWindowTwo!=null&&mWindows.isShowing()){
                return true;
            }else{
                return  false;
            }

        }
        public void show() {
//            获取textveiw在父控件中间的位置

            isShowingOperateSelect=true;
            if (mSentenceInfoBean.get(chooseTips).getWordColor()==1){
                iv_pink.setImageResource(R.drawable.ic_pink_long);
                iv_yellow.setImageResource(R.drawable.ic_yellow_short);
                iv_green.setImageResource(R.drawable.ic_green_short);
                iv_blue.setImageResource(R.drawable.ic_blue_short);
                iv_purple.setImageResource(R.drawable.ic_purple_short);

            }else if(mSentenceInfoBean.get(chooseTips).getWordColor()==2){
                iv_yellow.setImageResource(R.drawable.ic_yellow_long);
                iv_pink.setImageResource(R.drawable.ic_pink_short);
                iv_green.setImageResource(R.drawable.ic_green_short);
                iv_blue.setImageResource(R.drawable.ic_blue_short);
                iv_purple.setImageResource(R.drawable.ic_purple_short);

            }else if(mSentenceInfoBean.get(chooseTips).getWordColor()==3){
                iv_green.setImageResource(R.drawable.ic_green_long);
                iv_pink.setImageResource(R.drawable.ic_pink_short);
                iv_yellow.setImageResource(R.drawable.ic_yellow_short);
                iv_blue.setImageResource(R.drawable.ic_blue_short);
                iv_purple.setImageResource(R.drawable.ic_purple_short);

            }else if(mSentenceInfoBean.get(chooseTips).getWordColor()==4){
                iv_green.setImageResource(R.drawable.ic_green_short);
                iv_pink.setImageResource(R.drawable.ic_pink_short);
                iv_yellow.setImageResource(R.drawable.ic_yellow_short);
                iv_blue.setImageResource(R.drawable.ic_blue_long);
                iv_purple.setImageResource(R.drawable.ic_purple_short);

            }else if(mSentenceInfoBean.get(chooseTips).getWordColor()==5){
                iv_green.setImageResource(R.drawable.ic_green_short);
                iv_pink.setImageResource(R.drawable.ic_pink_short);
                iv_yellow.setImageResource(R.drawable.ic_yellow_short);
                iv_blue.setImageResource(R.drawable.ic_blue_short);
                iv_purple.setImageResource(R.drawable.ic_purple_long);

            }else{
                iv_yellow.setImageResource(R.drawable.ic_yellow_long);
                iv_pink.setImageResource(R.drawable.ic_pink_short);
                iv_green.setImageResource(R.drawable.ic_green_short);
                iv_blue.setImageResource(R.drawable.ic_blue_short);
                iv_purple.setImageResource(R.drawable.ic_purple_short);


            }
            mTextView.getLocationInWindow(mTempCoors);
            Layout layout = mTextView.getLayout();
            int posX = (int) layout.getPrimaryHorizontal(mTouchXabs) + mTempCoors[0];
            int posY = layout.getLineTop(layout.getLineForOffset(mTouchYabs)) + mTempCoors[1] - mHeight - 16;
            if (posX <= 0) posX = 16;
            if (posY < 0) posY = 16;
            if (posX + mWidth > TextLayoutUtil.getScreenWidth(mContext)) {
                posX = TextLayoutUtil.getScreenWidth(mContext) - mWidth - 10;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWindows.setElevation(8f);
            }
            mWindows.showAtLocation(mTextView, Gravity.NO_GRAVITY, posX, posY);
        }
        public void dismiss()
        {
            isShowingOperateSelect=true;
            mWindows.dismiss();
        }
    }



    /**
     * Operate windows : copy, select all
     */
    private class OperateWindow {

        private PopupWindow mWindow;
        private int[] mTempCoors = new int[2];

        private int mWidth;
        private int mHeight;

        public OperateWindow(final Context context) {
            View contentView = LayoutInflater.from(context).inflate(R.layout.layout_operation_one, null);
            contentView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            mWidth = contentView.getMeasuredWidth();
            mHeight = contentView.getMeasuredHeight();
            mWindow =
                    new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT, false);
            mWindow.setClippingEnabled(false);

            //标记生词
            contentView.findViewById(R.id.tv_marker).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectListener != null) {
                        mSelectListener.onTextSelected(mSelectionInfo.mSelectionContent, mSelectionInfo.mStart, mSelectionInfo.mEnd,"2");
                    }
                    SelectableTextHelper.this.resetSelectionInfo();
                    SelectableTextHelper.this.hideSelectView();
                }
            });
//            //选择复制
            contentView.findViewById(R.id.tv_copy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clip = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    clip.setPrimaryClip(ClipData.newPlainText(mSelectionInfo.mSelectionContent, mSelectionInfo.mSelectionContent));
                    Toast.makeText(DUTApplication.getInstance().getApplicationContext(),"已复制到剪贴板",Toast.LENGTH_SHORT).show();
                    SelectableTextHelper.this.resetSelectionInfo();
                    SelectableTextHelper.this.hideSelectView();
                }
            });
//            //全选
//            contentView.findViewById(R.id.tv_select_all).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    hideSelectView();
//                    selectText(0, mTextView.getText().length());
//                    isHide = false;
//                    showCursorHandle(mStartHandle);
//                    showCursorHandle(mEndHandle);
//                    mOperateWindow.show();
//                }
//            });
//            选择翻译
            contentView.findViewById(R.id.tv_translate).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSelectListener != null) {
                        float [] a=new float[2];
                        a[0]=mTouchX;
                        a[1]=mTouchY;
                        mSelectListener.onTextTranslate(mSelectionInfo.mSelectionContent,a);
                    }

                    SelectableTextHelper.this.resetSelectionInfo();
                    SelectableTextHelper.this.hideSelectView();
                }
            });
//            //选择收藏
//            contentView.findViewById(R.id.tv_collection).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mSelectListener != null) {
//                        mSelectListener.onTextSelected(mSelectionInfo.mSelectionContent, mSelectionInfo.mStart, mSelectionInfo.mEnd);
//                        mSelectListener.onTextCollect(mSelectionInfo.mSelectionContent);
//                    }
//                    SelectableTextHelper.this.resetSelectionInfo();
//                    SelectableTextHelper.this.hideSelectView();
//                }
//            });

        }

        public void show() {
            isShowingOperateSelect=true;
            mTextView.getLocationInWindow(mTempCoors);
            Layout layout = mTextView.getLayout();
            int posX = (int) layout.getPrimaryHorizontal(mSelectionInfo.mStart) + mTempCoors[0];
            int posY = layout.getLineTop(layout.getLineForOffset(mSelectionInfo.mStart)) + mTempCoors[1] - mHeight - 10;
            if (posX <= 0) posX = 10;
            if (posY < 0) posY = 10;
            if (posX + mWidth > TextLayoutUtil.getScreenWidth(mContext)) {
                posX = TextLayoutUtil.getScreenWidth(mContext) - mWidth - 10;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mWindow.setElevation(8f);
            }
            mWindow.showAtLocation(mTextView, Gravity.NO_GRAVITY, posX, posY);
        }

        public void dismiss() {

            mWindow.dismiss();
            isShowingOperateSelect=false;
        }

        public boolean isShowing() {
            return mWindow.isShowing();
        }
    }




    private class CursorHandle extends View {

        private PopupWindow mPopupWindow;
        private Paint mPaint;

        private int mCircleRadius = mCursorHandleSize / 2;
        private int mWidth = mCircleRadius * 2;
        private int mHeight = mCircleRadius * 2;
        private int mPadding = 25;
        private boolean isLeft;

        public CursorHandle(boolean isLeft) {
            super(mContext);
            this.isLeft = isLeft;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(mCursorHandleColor);

            mPopupWindow = new PopupWindow(this);
            mPopupWindow.setClippingEnabled(false);
            mPopupWindow.setWidth(mWidth + mPadding * 2);
            mPopupWindow.setHeight(mHeight + mPadding / 2);
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawCircle(mCircleRadius + mPadding, mCircleRadius, mCircleRadius, mPaint);
            if (isLeft) {
                canvas.drawRect(mCircleRadius + mPadding, 0, mCircleRadius * 2 + mPadding, mCircleRadius, mPaint);
            } else {
                canvas.drawRect(mPadding, 0, mCircleRadius + mPadding, mCircleRadius, mPaint);
            }
        }

        private int mAdjustX;
        private int mAdjustY;

        private int mBeforeDragStart;
        private int mBeforeDragEnd;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mBeforeDragStart = mSelectionInfo.mStart;
                    mBeforeDragEnd = mSelectionInfo.mEnd;
                    mAdjustX = (int) event.getX();
                    mAdjustY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mOperateWindow.show();
                    break;
                case MotionEvent.ACTION_MOVE:
                    mOperateWindow.dismiss();
                    int rawX = (int) event.getRawX();
                    int rawY = (int) event.getRawY();
                    update(rawX + mAdjustX - mWidth, rawY + mAdjustY - mHeight);
                    break;
            }
            return true;
        }

        private void changeDirection() {
            isLeft = !isLeft;
            invalidate();
        }

        public void dismiss() {
            mPopupWindow.dismiss();
        }

        private int[] mTempCoors = new int[2];

        public void update(int x, int y) {
            mTextView.getLocationInWindow(mTempCoors);
            int oldOffset;
            if (isLeft) {
                oldOffset = mSelectionInfo.mStart;
            } else {
                oldOffset = mSelectionInfo.mEnd;
            }

//            x-=mTempCoors[0];//手动添加
            y -= mTempCoors[1];

            int offset = TextLayoutUtil.getHysteresisOffset(mTextView, x, y, oldOffset);

            if (offset != oldOffset) {
                resetSelectionInfo();
                if (isLeft) {
                    if (offset > mBeforeDragEnd) {
                        CursorHandle handle = getCursorHandle(false);
                        changeDirection();
                        handle.changeDirection();
                        mBeforeDragStart = mBeforeDragEnd;
                        selectText(mBeforeDragEnd, offset);
                        handle.updateCursorHandle();
                    } else {
                        selectText(offset, -1);
                    }
                    updateCursorHandle();
                } else {
                    if (offset < mBeforeDragStart) {
                        CursorHandle handle = getCursorHandle(true);
                        handle.changeDirection();
                        changeDirection();
                        mBeforeDragEnd = mBeforeDragStart;
                        selectText(offset, mBeforeDragStart);
                        handle.updateCursorHandle();
                    } else {
                        selectText(mBeforeDragStart, offset);
                    }
                    updateCursorHandle();
                }
            }
        }

        private void updateCursorHandle() {
            mTextView.getLocationInWindow(mTempCoors);
            Layout layout = mTextView.getLayout();
            if (isLeft) {
                mPopupWindow.update((int) layout.getPrimaryHorizontal(mSelectionInfo.mStart) - mWidth + getExtraX(),
                        layout.getLineBottom(layout.getLineForOffset(mSelectionInfo.mStart)) + getExtraY(), -1, -1);
            } else {
                mPopupWindow.update((int) layout.getPrimaryHorizontal(mSelectionInfo.mEnd) + getExtraX(),
                        layout.getLineBottom(layout.getLineForOffset(mSelectionInfo.mEnd)) + getExtraY(), -1, -1);
            }
        }

        public void show(int x, int y) {
            mTextView.getLocationInWindow(mTempCoors);
            int offset = isLeft ? mWidth : 0;
            mPopupWindow.showAtLocation(mTextView, Gravity.NO_GRAVITY, x - offset + getExtraX(), y + getExtraY());
        }

        public int getExtraX() {
            return mTempCoors[0] - mPadding + mTextView.getPaddingLeft();
        }

        public int getExtraY() {
            return mTempCoors[1] + mTextView.getPaddingTop();
        }
    }

    private CursorHandle getCursorHandle(boolean isLeft) {
        if (mStartHandle.isLeft == isLeft) {
            return mStartHandle;
        } else {
            return mEndHandle;
        }
    }

    public static class Builder {
        private TextView mTextView;
        private int mCursorHandleColor = 0xFF1379D6;
        private int mSelectedColor = 0xFFAFE1F4;
        private float mCursorHandleSizeInDp = 24;
        private boolean mIsShowingSelect;
        private List<ReadingListResp.DataBean.NewWordsBean> mSentenceInfoBean;

        private ReadingListResp.DataBean mReadingBean;
        public Builder(TextView textView) {
            mTextView = textView;
        }

        public Builder setCursorHandleColor(@ColorInt int cursorHandleColor) {
            mCursorHandleColor = cursorHandleColor;
            return this;
        }

        public Builder setReadingBean(ReadingListResp.DataBean readingBean ) {
            mReadingBean = readingBean;
            return this;
        }
        public Builder setIsShowing (Boolean isShowingSelect){
            mIsShowingSelect = isShowingSelect;
            return this;

        }
        public Builder setCursorHandleSizeInDp(float cursorHandleSizeInDp) {
            mCursorHandleSizeInDp = cursorHandleSizeInDp;
            return this;
        }

        public Builder setSelectedColor(@ColorInt int selectedBgColor) {
            mSelectedColor = selectedBgColor;
            return this;
        }
        public Builder setSentenceInfoBean(List<ReadingListResp.DataBean.NewWordsBean> SentenceInfoBean){
            mSentenceInfoBean = SentenceInfoBean;
            return this;
        }

        public SelectableTextHelper build() {
            return new SelectableTextHelper(this);
        }
    }
}



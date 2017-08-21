package com.yunding.dut.ui.reading;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.reading.ReadingDataResp;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.model.resp.reading.markResp;
import com.yunding.dut.model.resp.translate.JSTranslateBean;
import com.yunding.dut.model.resp.translate.YDTranslateBean;
import com.yunding.dut.presenter.reading.ReadingPresenter;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.FontsUtils;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.SizeUtils;
import com.yunding.dut.util.third.TimeUtils;
import com.yunding.dut.view.DUTHorizontalProgressBarWithNumber;
import com.yunding.dut.view.DUTScrollView;
import com.yunding.dut.view.popupwindow;
import com.yunding.dut.view.selectable.OnSelectListener;
import com.yunding.dut.view.selectable.SelectableTextHelper;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ReadingNewArticleFragment extends BaseFragmentInReading implements IReadingArticleView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.sv_reading)
    DUTScrollView svReading;
    @BindView(R.id.btn_last)
    Button btnLast;
    @BindView(R.id.progress)
    DUTHorizontalProgressBarWithNumber progress;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.rg_radio)
    RadioGroup rgRadio;
    @BindView(R.id.btn_size_small)
    RadioButton btnSizeSmall;
    @BindView(R.id.btn_size_middle)
    RadioButton btnSizeMiddle;
    @BindView(R.id.btn_size_big)
    RadioButton btnSizeBig;
    @BindView(R.id.ll_operation_content)
    LinearLayout llOperationContent;
    Unbinder unbinder;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    @BindView(R.id.ll_btn_finish)
    LinearLayout llBtnFinish;
    @BindView(R.id.iv_voice_translate_en)
    ImageView ivVoiceTranslateEn;
    @BindView(R.id.tv_content_translate)
    TextView tvContentTranslate;
    @BindView(R.id.tv_soundmark_translate)
    TextView tvSoundmarkTranslate;
    @BindView(R.id.tv_translate_translate)
    TextView tvTranslateTranslate;
    @BindView(R.id.lila_translate)
    LinearLayout lilaTranslate;
    @BindView(R.id.btn_max)
    Button btnMax;
    @BindView(R.id.btn_middle)
    Button btnMiddle;
    @BindView(R.id.btn_small)
    Button btnSmall;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.frame_content)
    AbsoluteLayout frameContent;
    @BindView(R.id.ll_reading)
    RelativeLayout llReading;
    private ReadingActivity ra;
    private ReadingListResp.DataBean mReadingInfo;

    private ReadingPresenter mPresenter;

    //当前选中句子的索引
    private int mSentenceIndex = 0;

    //上一个句子的阅读开始时间
    private long mReadingStartTime = 0;

    //记录该篇文章是否读完
    private boolean mIsFinished = false;
    private Button btn_cancel, btn_send;
    private EditText et_notes;
    private TextView tv_note;
    private View view;
    private Dialog dialog;
    private boolean isShowingOperateWindow = true;
    private SelectableTextHelper mSelectableTextHelper;
    private float tuochX, tuochY;
    private String Iv_tips_size="40";  //60  40  20
    private  String  tv_text_size="16";//18  16  14
    private boolean isShowReadingData=true;


    public ReadingNewArticleFragment() {
    }

    //读单词用
    private MediaPlayer mMediaPlayer;
    //读单词的url
    private String readUrl;
    private static final String TAG = "ReadingArticleFragment";
    private int isArticleFinished;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new_article_content;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        //读单词用
        mMediaPlayer = new MediaPlayer();
        mReadingInfo = (ReadingListResp.DataBean) getArguments().getSerializable(ReadingActivity.READING_INFO);
        mPresenter = new ReadingPresenter(this);
        ra = (ReadingActivity) getActivity();
        ra.setToolbarTitle(mReadingInfo.getUnitTitle());

        if (mReadingInfo != null) {

            if (DUTApplication.getAcache().getAsString(DUTApplication.getUserInfo().getUserId()+mReadingInfo.getClassId()+mReadingInfo.getCourseId())==null
                    ||"null".equals(DUTApplication.getAcache().getAsString(DUTApplication.getUserInfo().getUserId()+mReadingInfo.getClassId()+mReadingInfo.getCourseId()))
                    ||"".equals(DUTApplication.getAcache().getAsString(DUTApplication.getUserInfo().getUserId()+mReadingInfo.getClassId()+mReadingInfo.getCourseId()))){

            }else{
                String [] a=DUTApplication.getAcache().getAsString(DUTApplication.getUserInfo().getUserId()+mReadingInfo.getClassId()+mReadingInfo.getCourseId()).split(",");
                tv_text_size=a[0];
                Iv_tips_size=a[1];
                if (tv_text_size.equals("18")){
                    btnMax.setTextColor(getHoldingActivity().getResources().getColor(R.color.orange));
                    btnMiddle.setTextColor(getHoldingActivity().getResources().getColor(R.color.bg_white));
                    btnSmall.setTextColor(getHoldingActivity().getResources().getColor(R.color.bg_white));
                    tvContent.setTextSize(18);

                }else if (tv_text_size.equals("16")){
                    btnMiddle.setTextColor(getHoldingActivity().getResources().getColor(R.color.orange));
                    btnMax.setTextColor(getHoldingActivity().getResources().getColor(R.color.bg_white));
                    btnSmall.setTextColor(getHoldingActivity().getResources().getColor(R.color.bg_white));
                    tvContent.setTextSize(16);
                }else if (tv_text_size.equals("14")){
                    btnSmall.setTextColor(getHoldingActivity().getResources().getColor(R.color.orange));
                    btnMax.setTextColor(getHoldingActivity().getResources().getColor(R.color.bg_white));
                    btnMiddle.setTextColor(getHoldingActivity().getResources().getColor(R.color.bg_white));
                    tvContent.setTextSize(14);
                }
                if (Iv_tips_size.equals("60")){
                    Iv_tips_size="60";
                    tvContent.setLineSpacing(9, 2f);
                    btnSizeBig.setChecked(true);
                    btnSizeMiddle.setChecked(false);
                    btnSizeSmall.setChecked(false);
                }else if (Iv_tips_size.equals("40")){
                    Iv_tips_size="40";
                    tvContent.setLineSpacing(6, 1.5f);
                    btnSizeBig.setChecked(false);
                    btnSizeMiddle.setChecked(true);
                    btnSizeSmall.setChecked(false);

                }else if (Iv_tips_size.equals("20")){
                    Iv_tips_size="20";
                    tvContent.setLineSpacing(3, 1f);
                    btnSizeBig.setChecked(false);
                    btnSizeMiddle.setChecked(false);
                    btnSizeSmall.setChecked(true);

                }


            }


            // TODO: 2017/7/28 三期
            if (mReadingInfo.getCompleted()==1||mReadingInfo.getCompleted()==2){
                ra.getToolbarExit().setVisibility(View.VISIBLE);
                ra.getToolbarExit().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.showReadingDataA(mReadingInfo.getClassId(),mReadingInfo.getCourseId());
                    }
                });
            }else{
                ra.getToolbarExit().setVisibility(View.GONE);
            }
            // TODO: 2017/7/28 三期
            if (FontsUtils.isContainChinese(mReadingInfo.getCourseTitle())
                    || TextUtils.isEmpty(mReadingInfo.getCourseTitle())) {

            } else {
                tvTitle.setTypeface(DUTApplication.getHsbwTypeFace());
            }
//            if (FontsUtils.isContainChinese(mReadingInfo.getCourseContent())
//                    || TextUtils.isEmpty(mReadingInfo.getCourseContent())) {
//
//            } else {
                tvContent.setTypeface(DUTApplication.getHsbwTypeFace());
//            }
            tvTitle.setText(mReadingInfo.getCourseTitle());
            tvContent.setText(mReadingInfo.getCourseContent());



            if (mReadingInfo.getCompleted()!=0){
                ra.setNavigationIcon(R.drawable.ic_title_bac1);

//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        ra.setMenuIcon(R.drawable.ic_exit);
//                    }
//                },100);
//                ra.setExitTextColor(R.color.textColorShow);
                isArticleFinished=1;
                llOperationContent.setVisibility(View.GONE);
                mIsFinished = true;
                llBtnFinish.setVisibility(View.VISIBLE);
                tvContent.setTextColor(getResources().getColor(R.color.text_color_primary));
                mSentenceIndex = mReadingInfo.getSentenceInfo().size();

            }else{
                isArticleFinished = mReadingInfo.getArticleFinish();
                if (isArticleFinished == 1) {
//                    ra.setNavigationIcon(R.drawable.ic_title_bac1);
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            ra.setMenuIcon(R.drawable.ic_exit);
//                        }
//                    },100);
                    llOperationContent.setVisibility(View.GONE);
//                    ra.setExitTextColor(R.color.textColorShow);
                    mIsFinished = true;
                    llBtnFinish.setVisibility(View.VISIBLE);
//                    btnFinish.setFocusable(true);
//                    btnFinish.setClickable(true);
//                    btnFinish.setFocusableInTouchMode(true);

                    tvContent.setTextColor(getResources().getColor(R.color.text_color_primary));
                    mSentenceIndex = mReadingInfo.getReadingLineIndex();
                } else {

                    ra.setNavigationIcon(R.drawable.ic_title_bac1_white);
                    ra.setToolbarBGC(getResources().getColor(R.color.aablack));
                    ra.setToolbarTitleColor(getResources().getColor(R.color.bg_white));

//                    ra.getexit(true);
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            ra.setMenuIcon(R.drawable.ic_exit_white);
//                        }
//                    },100);
//                    ra.setExitTextColor(R.color.bg_white);

                    llBtnFinish.setVisibility(View.GONE);
                    mIsFinished = false;//初始化阅读状态
                    mSentenceIndex = mReadingInfo.getReadingLineIndex();//
                    mReadingStartTime = System.currentTimeMillis();//初始化时间

                }
            }

        }
        if (mReadingInfo.getArticleFinish()==1||mReadingInfo.getCompleted()!=0){

        }else{


        mSelectableTextHelper = new SelectableTextHelper.Builder(tvContent)

                .setSelectedColor(getResources().getColor(R.color.orange))
                .setCursorHandleSizeInDp(20)
                .setSentenceInfoBean(mReadingInfo.getNewWords())
                .setReadingBean(mReadingInfo)
                .setIsShowing(isShowingOperateWindow)
                .setCursorHandleColor(getResources().getColor(R.color.translucent))
                .build();


        mSelectableTextHelper.setSelectListener(new OnSelectListener() {
            @Override
            public void onTextSelected(CharSequence content, int startIndex, int endIndex,String color) {
                mPresenter.markerWords(mReadingInfo.getCourseId(), startIndex, content.length(), content.toString(),mReadingInfo.getClassId(),color);
            }

            //点翻译的回调
            @Override
            public void onTextTranslate(CharSequence content,float[] postion) {
                String str = deleteInterpunction(
                        (String) content);
                mPresenter.getTranslation(str,postion);

            }

            //点击收藏的回调
            @Override
            public void onTextCollect(CharSequence content,String id) {
                String str = deleteInterpunction(
                        (String) content);
                mPresenter.collectWords(str, mReadingInfo.getCourseId(), mReadingInfo.getCourseTitle(),id);
            }

            // 点击笔记毁掉
            @Override
            public void onTextNote(CharSequence content,int position) {
                Toast.makeText(getHoldingActivity(), content, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getHoldingActivity(), AddNotesActivity.class);
//                自定义弹窗
                showDialog(content.toString(),position);

            }

            @Override
            public void onTextDelete(String position) {
                mPresenter.deleteNewWord(position);
            }

            @Override
            public void onChangeColor(int position, int color, String id,String colorid) {
                mPresenter.changeColor(id,colorid);
                changeChooseBg(position, colorid);
            }


            @Override
            public void onTextClick(int x, int y) {
                WindowManager winManager = (WindowManager) ra.getSystemService(Context.WINDOW_SERVICE);

                int windowsWeidth = winManager.getDefaultDisplay().getWidth();
                int windowsHeight = winManager.getDefaultDisplay().getHeight();


                if (0 <= x && x <= (windowsWeidth / 3)) {
                    if (mSelectableTextHelper.isShowingOperateSelect()){
                        mSelectableTextHelper.dismissWindow(true);
                        mSelectableTextHelper.setShowingOperateSelect(false);

                    }else{
                        if (isShowingOperateWindow) {
                            isShowingOperateWindow = false;
                            getHoldingActivity().getmToolbar().animate().translationY(-(getHoldingActivity().getmToolbar().getHeight())).setInterpolator(new AccelerateInterpolator(2)).start();
                            llReading.setSystemUiVisibility(View.INVISIBLE);
                            if (mIsFinished) {
                                llBtnFinish.animate().translationY(llBtnFinish.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

                            } else {
                                llOperationContent.animate().translationY(llOperationContent.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                            }
                        } else {

                            if (mIsFinished) {

                                isShowingOperateWindow = true;

                                llReading.setSystemUiVisibility(View.VISIBLE);
                                getHoldingActivity().getmToolbar().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                                llBtnFinish.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();


                            } else {

                                if (mSentenceIndex > 0) {
                                    commitLastSentenceTime();
                                    mSentenceIndex -= 1;
                                    moveToPosition(mSentenceIndex);
                                    if (mReadingInfo.getReadingMode()==1){
                                        btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_sentence));
                                    }else if(mReadingInfo.getReadingMode()==2){
                                        btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_paragraph));
                                    }else if (mReadingInfo.getReadingMode()==3){
                                        btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_finish));
                                    }

                                } else {
                                    showToast("已经到第一句了");
                                }


                            }

                        }
                    }
//                    showToast("左");

                } else if (x > (windowsWeidth / 3) && x <= (2 * windowsWeidth / 3)) {
                    //          点击的中间        显示toorBar
//                    showToast("中");
                    if (mSelectableTextHelper.isShowingOperateSelect()){
                        mSelectableTextHelper.dismissWindow(true);
                        mSelectableTextHelper.setShowingOperateSelect(false);
                    }else{
                        if (isShowingOperateWindow) {
                            isShowingOperateWindow = false;
                            getHoldingActivity().getmToolbar().animate().translationY(-(getHoldingActivity().getmToolbar().getHeight())).setInterpolator(new AccelerateInterpolator(2)).start();
                            ;
                            llReading.setSystemUiVisibility(View.INVISIBLE);
                            if (mIsFinished) {
                                llBtnFinish.animate().translationY(llBtnFinish.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

                            } else {
                                llOperationContent.animate().translationY(llOperationContent.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                            }


                        } else {
                            isShowingOperateWindow = true;

                            llReading.setSystemUiVisibility(View.VISIBLE);
                            getHoldingActivity().getmToolbar().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                            if (mIsFinished) {
                                llBtnFinish.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                            } else {
                                llOperationContent.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                            }

                        }
                    }



                } else {
                    //点击右边
//                    showToast("右");
                    if (mSelectableTextHelper.isShowingOperateSelect()){
                        mSelectableTextHelper.dismissWindow(true);
                        mSelectableTextHelper.setShowingOperateSelect(false);
                    }else{


                    if (isShowingOperateWindow) {
                        isShowingOperateWindow = false;
                        getHoldingActivity().getmToolbar().animate().translationY(-(getHoldingActivity().getmToolbar().getHeight())).setInterpolator(new AccelerateInterpolator(2)).start();
                        ;
                        llReading.setSystemUiVisibility(View.INVISIBLE);
                        if (mIsFinished) {
                            llBtnFinish.animate().translationY(llBtnFinish.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

                        } else {
                            llOperationContent.animate().translationY(llOperationContent.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                        }

                    } else {

                        if (mIsFinished) {
                            isShowingOperateWindow = true;
                            llReading.setSystemUiVisibility(View.VISIBLE);

                            getHoldingActivity().getmToolbar().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                            llBtnFinish.setVisibility(View.VISIBLE);

                            llBtnFinish.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                            llOperationContent.setVisibility(View.GONE);
                        } else {


                            if (btnNext.getText().toString().contains(getHoldingActivity().getResources().getString(R.string.text_next_finish))) {
                                isShowingOperateWindow = true;
                                llReading.setSystemUiVisibility(View.VISIBLE);
                                getHoldingActivity().getmToolbar().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                                llOperationContent.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                            } else {


                                if (mSentenceIndex < (mReadingInfo.getSentenceInfo().size() - 1)) {
                                    commitNextSentenceTime();
                                    mSentenceIndex += 1;
//
                                    if (mSentenceIndex > mReadingInfo.getSentenceInfo().size() - 1) {
                                        btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_finish));
                                    }

                                    moveToPosition(mSentenceIndex);
                                } else {
                                    btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_finish));
                                    isShowingOperateWindow = true;
                                    llReading.setSystemUiVisibility(View.VISIBLE);
                                    getHoldingActivity().getmToolbar().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                                    llOperationContent.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                                }
                                }
                            }
                        }
                    }

                }

            }


        });
        }

        frameContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tuochX = event.getX();
                tuochY = event.getY();
                WindowManager winManager = (WindowManager) ra.getSystemService(Context.WINDOW_SERVICE);

                int windowsWeidth = winManager.getDefaultDisplay().getWidth();
                int windowsHeight = winManager.getDefaultDisplay().getHeight();


                if (0 <= tuochX && tuochX <= (windowsWeidth / 3)) {
//                    showToast("←");

                    if (
                            mSelectableTextHelper!=null&&mSelectableTextHelper.isShowingOperateSelect()){
                        mSelectableTextHelper.dismissWindow(true);
                        mSelectableTextHelper.setShowingOperateSelect(false);
                    }else{


                    if (isShowingOperateWindow) {
                        isShowingOperateWindow = false;
                        getHoldingActivity().getmToolbar().animate().translationY(-(getHoldingActivity().getmToolbar().getHeight())).setInterpolator(new AccelerateInterpolator(2)).start();
                        llReading.setSystemUiVisibility(View.INVISIBLE);
                        if (mIsFinished) {
                            llBtnFinish.animate().translationY(llBtnFinish.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

                        } else {
                            llOperationContent.animate().translationY(llOperationContent.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                        }
                    } else {
                        if (mIsFinished) {
                            isShowingOperateWindow = true;

                            llReading.setSystemUiVisibility(View.VISIBLE);
                            getHoldingActivity().getmToolbar().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                            llBtnFinish.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                        } else {


                            if (mSentenceIndex > 0) {
                                commitLastSentenceTime();
                                mSentenceIndex -= 1;
                                moveToPosition(mSentenceIndex);
                                if (mReadingInfo.getReadingMode()==1){
                                    btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_sentence));
                                }else if(mReadingInfo.getReadingMode()==2){
                                    btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_paragraph));
                                }else if (mReadingInfo.getReadingMode()==3){
                                    btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_finish));
                                }
                            } else {
                                showToast("已经到第一句了");
                            }
                        }
                    }}
                } else if (tuochX > (windowsWeidth / 3) && tuochX <= (2 * windowsWeidth / 3)) {
//                    showToast("-");
                    //          点击的中间        显示toorBar
                    if (mSelectableTextHelper!=null&&mSelectableTextHelper.isShowingOperateSelect()){
                        mSelectableTextHelper.dismissWindow(true);
                        mSelectableTextHelper.setShowingOperateSelect(false);
                    }else{


                    if (isShowingOperateWindow) {
                        isShowingOperateWindow = false;
                        getHoldingActivity().getmToolbar().animate().translationY(-(getHoldingActivity().getmToolbar().getHeight())).setInterpolator(new AccelerateInterpolator(2)).start();
                        ;
                        llReading.setSystemUiVisibility(View.INVISIBLE);
                        if (mIsFinished) {
                            llBtnFinish.animate().translationY(llBtnFinish.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

                        } else {
                            llOperationContent.animate().translationY(llOperationContent.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                        }


                    } else {
                        isShowingOperateWindow = true;

                        llReading.setSystemUiVisibility(View.VISIBLE);
                        getHoldingActivity().getmToolbar().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                        if (mIsFinished) {
                            llBtnFinish.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                        } else {
                            llOperationContent.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                        }

                    }}


                } else {
//                    showToast("→");
                    if (mSelectableTextHelper!=null&&mSelectableTextHelper.isShowingOperateSelect()){
                        mSelectableTextHelper.dismissWindow(true);
                        mSelectableTextHelper.setShowingOperateSelect(false);
                    }else{


                    if (isShowingOperateWindow) {
                        isShowingOperateWindow = false;
                        getHoldingActivity().getmToolbar().animate().translationY(-(getHoldingActivity().getmToolbar().getHeight())).setInterpolator(new AccelerateInterpolator(2)).start();

                        llReading.setSystemUiVisibility(View.INVISIBLE);
                        if (mIsFinished) {
                            llBtnFinish.animate().translationY(llBtnFinish.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();

                        } else {
                            llOperationContent.animate().translationY(llOperationContent.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
                        }
                    } else {
                        if (mIsFinished) {

                            isShowingOperateWindow = true;

                            llReading.setSystemUiVisibility(View.VISIBLE);
                            getHoldingActivity().getmToolbar().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                            llBtnFinish.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                            llOperationContent.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                        } else {


                            if (btnNext.getText().toString().contains(getHoldingActivity().getResources().getString(R.string.text_next_finish))) {
                                isShowingOperateWindow = true;
                                llReading.setSystemUiVisibility(View.VISIBLE);
                                getHoldingActivity().getmToolbar().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                                llBtnFinish.setVisibility(View.VISIBLE);
                                llBtnFinish.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                                llOperationContent.setVisibility(View.GONE);
                            } else {

                                if (mSentenceIndex < (mReadingInfo.getSentenceInfo().size() - 1)) {
                                    commitNextSentenceTime();
                                    mSentenceIndex += 1;
//
                                    if (mSentenceIndex > mReadingInfo.getSentenceInfo().size() - 1) {
                                        btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_finish));
                                    }
                                    moveToPosition(mSentenceIndex);
                                } else {
                                    isShowingOperateWindow = true;
                                    llReading.setSystemUiVisibility(View.VISIBLE);
                                    getHoldingActivity().getmToolbar().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                                    llOperationContent.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

                                    btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_finish));

                                }
                            }

                        }
                    }
                    }
                }

                return false;
            }
        });

        rgRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.btn_size_big:
                        Iv_tips_size="60";
                        tvContent.setLineSpacing(9, 2f);

                        for (int j=0;j<mReadingInfo.getNewWords().size();j++){
                            for (int i=0;i<frameContent.getChildCount();i++){
                                if (mReadingInfo.getNewWords().get(j).getNoted()==1){
                                    if (frameContent.getChildAt(i).getId()==Integer.valueOf(mReadingInfo.getNewWords().get(j).getNewWordId())){
                                        frameContent.removeViewAt(i);
                                    }
                                }

                            }
                        }


                        Message ms3=new Message();
                        ms3.what=132;
                        handler.sendMessage(ms3);
                        break;
                    case R.id.btn_size_middle:
                        Iv_tips_size="40";
                        tvContent.setLineSpacing(6, 1.5f);
                        int b=frameContent.getChildCount();
                        for (int j=0;j<mReadingInfo.getNewWords().size();j++){
                            for (int i=0;i<frameContent.getChildCount();i++){
                                if (mReadingInfo.getNewWords().get(j).getNoted()==1){
                                    if (frameContent.getChildAt(i).getId()==Integer.valueOf(mReadingInfo.getNewWords().get(j).getNewWordId())){
                                        frameContent.removeViewAt(i);
                                    }
                                }

                            }
                        }
                        Message ms4=new Message();
                        ms4.what=132;
                        handler.sendMessage(ms4);
                        break;
                    case R.id.btn_size_small:
                        Iv_tips_size="20";
                        tvContent.setLineSpacing(3, 1f);
                        int a=frameContent.getChildCount();
                        for (int j=0;j<mReadingInfo.getNewWords().size();j++){
                            for (int i=0;i<frameContent.getChildCount();i++){
                                if (mReadingInfo.getNewWords().get(j).getNoted()==1){
                                    if (frameContent.getChildAt(i).getId()==Integer.valueOf(mReadingInfo.getNewWords().get(j).getNewWordId())){
                                        frameContent.removeViewAt(i);
                                    }
                                }

                            }
                        }
                        Message ms5=new Message();
                        ms5.what=132;
                        handler.sendMessage(ms5);

                        break;
                }

            }
        });
       if (mReadingInfo.getReadingMode()==1){
           btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_sentence));
           btnLast.setText(getHoldingActivity().getResources().getString(R.string.text_last_sentence));


       }else if(mReadingInfo.getReadingMode()==2){
           btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_paragraph));
           btnLast.setText(getHoldingActivity().getResources().getString(R.string.text_last_paragraph));


       }else if (mReadingInfo.getReadingMode()==3){
           btnLast.setVisibility(View.GONE);
           btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_finish));
       }

        moveToPosition(mSentenceIndex);


        refreshProgress();
        Message msg3=new Message();
        msg3.what=132;
        handler.sendMessage(msg3);


    }
Handler handler=new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case 132:

//                frameContent.removeAllViews();
//                for (int i=0;i<frameContent.getChildCount();i++){
//                    if (frameContent.getChildAt(i).getId()==)
//                }
                        addNoteTips();
                break;
            case 133:

                addNoteTipsOne(msg.arg1);
                break;
        }
    }
};



    private void refreshProgress() {
        int progress1 = (100 * (mSentenceIndex + 1)) / mReadingInfo.getSentenceInfo().size();
        progress.setProgress(progress1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    /**
     * 功能简述:标记完成更改文字背景颜色
     *
     * @param startIndex [参数]
     * @param startIndex [参数]
     */
    private void changeMarkerBg(int startIndex, int endIndex, String content) {
        ReadingListResp.DataBean.NewWordsBean newWordsBean = new ReadingListResp.DataBean.NewWordsBean();
        newWordsBean.setWord(content);
        newWordsBean.setWordIndex(startIndex);
        newWordsBean.setWordLength(endIndex - startIndex);
        mReadingInfo.getNewWords().add(newWordsBean);
//        initMarkerWords();
        moveToPosition(mSentenceIndex);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DUTApplication.getAcache().put(DUTApplication.getUserInfo().getUserId()+mReadingInfo.getClassId()+mReadingInfo.getCourseId(),
                tv_text_size+","+Iv_tips_size,60*60*60*24);
        if (mReadingInfo.getArticleFinish() == 1) {
//            阅读完成了就不去计算了
        } else {
//            请求借口计算跳出次数
            mPresenter.dapFromArticle(mReadingInfo.getCourseId(),mReadingInfo.getClassId(),mSentenceIndex);
        }
        if (isShowingOperateWindow){

        }else{
            llReading.setSystemUiVisibility(View.VISIBLE);
            getHoldingActivity().getmToolbar().animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();

            if (mIsFinished) {
                llBtnFinish.animate().translationY(llBtnFinish.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
            } else {
                llOperationContent.animate().translationY(llOperationContent.getHeight()).setInterpolator(new AccelerateInterpolator(2)).start();
            }

        }
//        ra.setMenuIcon(R.drawable.ic_exit);
        ra.setNavigationIcon(R.drawable.ic_title_bac1);
        ra.setToolbarTitleColor(getResources().getColor(R.color.title));
//        ra.getexit(false);

//                ra.setExitTextColor(R.color.textColorShow);
        mMediaPlayer.reset();
        mMediaPlayer.release();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_last, R.id.btn_next, R.id.btn_finish,
            R.id.btn_max, R.id.btn_middle, R.id.btn_small})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_max:
                tvContent.setTextSize(18);
                tv_text_size="18";

                btnMax.setTextColor(getHoldingActivity().getResources().getColor(R.color.orange));
                btnMiddle.setTextColor(getHoldingActivity().getResources().getColor(R.color.bg_white));
                btnSmall.setTextColor(getHoldingActivity().getResources().getColor(R.color.bg_white));
                int c=frameContent.getChildCount();
                for (int j=0;j<mReadingInfo.getNewWords().size();j++){
                    for (int i=0;i<frameContent.getChildCount();i++){
                        if (mReadingInfo.getNewWords().get(j).getNoted()==1){
                            if (frameContent.getChildAt(i).getId()==Integer.valueOf(mReadingInfo.getNewWords().get(j).getNewWordId())){
                                frameContent.removeViewAt(i);
                            }
                        }

                    }
                }

                Message ms3=new Message();
                ms3.what=132;
                handler.sendMessage(ms3);
                break;
            case R.id.btn_middle:
                tvContent.setTextSize(16);
                tv_text_size="16";

                btnMiddle.setTextColor(getHoldingActivity().getResources().getColor(R.color.orange));
                btnMax.setTextColor(getHoldingActivity().getResources().getColor(R.color.bg_white));
                btnSmall.setTextColor(getHoldingActivity().getResources().getColor(R.color.bg_white));
                int b=frameContent.getChildCount();
                for (int j=0;j<mReadingInfo.getNewWords().size();j++){
                    for (int i=0;i<frameContent.getChildCount();i++){
                        if (mReadingInfo.getNewWords().get(j).getNoted()==1){
                            if (frameContent.getChildAt(i).getId()==Integer.valueOf(mReadingInfo.getNewWords().get(j).getNewWordId())){
                                frameContent.removeViewAt(i);
                            }
                        }

                    }
                }


                Message ms4=new Message();
                ms4.what=132;
                handler.sendMessage(ms4);
                break;
            case R.id.btn_small:
                tvContent.setTextSize(14);
                tv_text_size="14";

                btnSmall.setTextColor(getHoldingActivity().getResources().getColor(R.color.orange));
                btnMiddle.setTextColor(getHoldingActivity().getResources().getColor(R.color.bg_white));
                btnMax.setTextColor(getHoldingActivity().getResources().getColor(R.color.bg_white));
                int a=frameContent.getChildCount();
                for (int j=0;j<mReadingInfo.getNewWords().size();j++){
                    for (int i=0;i<frameContent.getChildCount();i++){
                        if (mReadingInfo.getNewWords().get(j).getNoted()==1){
                            if (frameContent.getChildAt(i).getId()==Integer.valueOf(mReadingInfo.getNewWords().get(j).getNewWordId())){
                                frameContent.removeViewAt(i);
                            }
                        }

                    }
                }

                Message ms5=new Message();
                ms5.what=132;
                handler.sendMessage(ms5);
                break;
            case R.id.btn_last:
                if (mSentenceIndex > 0) {
                    commitLastSentenceTime();
                    mSentenceIndex -= 1;
                    moveToPosition(mSentenceIndex);
                    if (mReadingInfo.getReadingMode()==1){
                        btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_sentence));
                    }else if(mReadingInfo.getReadingMode()==2){
                        btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_paragraph));
                    }

                } else {
                    showToast("已经到第一句了");
                }
                break;
            case R.id.btn_next:
                if (btnNext.getText().toString().contains(getHoldingActivity().getResources().getString(R.string.text_next_finish))) {
                    // TODO: 2017/7/28 三期
                            if (isShowReadingData){
                                commitNextSentenceTime();
//                                goNext();
                            }else{
                                goNext();
                            }
                    // TODO: 2017/7/28 三期

                } else {


                    if (mSentenceIndex < (mReadingInfo.getSentenceInfo().size() - 1)) {
                        commitNextSentenceTime();
                        mSentenceIndex += 1;
//                    if (mSentenceIndex == mReadingInfo.getSentenceInfo().size() - 1) {
//                        mIsFinished = true;
//                    }
                        if (mSentenceIndex > mReadingInfo.getSentenceInfo().size() - 1) {
                            btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_finish));
                        }

                        moveToPosition(mSentenceIndex);
                    } else {
                        btnNext.setText(getHoldingActivity().getResources().getString(R.string.text_next_finish));

                    }
                }
                break;
            case R.id.btn_finish:
                if (mReadingInfo.getCompleted()!=0){
                    goNext();
                }else{
//                    Log.e("adasd","mSentenceIndex"+
//                            mSentenceIndex+"-----mReadingInfo"+(mReadingInfo.getSentenceInfo().size() - 1));
                    if (mSentenceIndex == mReadingInfo.getSentenceInfo().size() - 1) {

                        if (mReadingInfo.getArticleFinish() == 0) {
                            commitNextSentenceTime();
                        }
                        goNext();
                    }
                }

                break;
        }
    }

    /**
     * 功能简述:点击下一句提交
     */
    private void commitNextSentenceTime() {
        long second = TimeUtils.getTimeSpanByNow(mReadingStartTime, ConstUtils.TimeUnit.MSEC);//该句子的阅读时间
        int isFinish = mSentenceIndex == (mReadingInfo.getSentenceInfo().size() - 1) ? 1 : 0;
        if (isFinish == 1) {
            mReadingInfo.setArticleFinish(1);
        }
        mPresenter.commitReadingTime(mReadingInfo.getCourseId(), mSentenceIndex, mSentenceIndex - 1, second, isFinish, mReadingInfo.getClassId());
        mReadingStartTime = System.currentTimeMillis();//更新时间
    }

    /**
     * 功能简述:点击上一句提交
     */
    private void commitLastSentenceTime() {
        long second = TimeUtils.getTimeSpanByNow(mReadingStartTime, ConstUtils.TimeUnit.MSEC);//该句子的阅读时间
        if (mSentenceIndex == mReadingInfo.getSentenceInfo().size() - 1) {
            mReadingInfo.setArticleFinish(1);
            mPresenter.commitReadingTime(mReadingInfo.getCourseId(), mSentenceIndex, mSentenceIndex - 1, second, 1, mReadingInfo.getClassId());
        } else {
            mPresenter.commitReadingTime(mReadingInfo.getCourseId(), mSentenceIndex, mSentenceIndex + 1, second, 0, mReadingInfo.getClassId());
        }
        mReadingStartTime = System.currentTimeMillis();//更新时间
    }

    private void changeChooseBg(int position, String color) {
        mReadingInfo.getNewWords().get(position).setWordColor(Integer.valueOf(color));
        moveToPosition(mSentenceIndex);

    }

    /**
     * 功能简述:改变文字颜色
     *
     * @param currentPosition [当前变色位置]
     */
    private void moveToPosition(int currentPosition) {
        try{



        List<ReadingListResp.DataBean.SentenceInfoBean> sentenceInfoList = mReadingInfo.getSentenceInfo();

        if (sentenceInfoList == null) return;


        if (currentPosition >sentenceInfoList.size() || currentPosition < 0) return;

//      可以将同一段string以不同样式提现的builder
        final SpannableStringBuilder builder = new SpannableStringBuilder(tvContent.getText().toString());

        if (!mIsFinished) {

            ReadingListResp.DataBean.SentenceInfoBean sentenceInfo = sentenceInfoList.get(mSentenceIndex);
            int startIndex = sentenceInfo.getIndex();
            int endIndex = sentenceInfo.getIndex() + sentenceInfo.getLength();
            TextAppearanceSpan spangray = new TextAppearanceSpan(getHoldingActivity(), R.style.style_gray);
            TextAppearanceSpan spanBlack = new TextAppearanceSpan(getHoldingActivity(), R.style.style_black);
            builder.setSpan(spangray, 0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(spanBlack, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(spangray, endIndex, tvContent.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {

//            ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
            TextAppearanceSpan spanBlack = new TextAppearanceSpan(getHoldingActivity(), R.style.style_black);
//            builder.setSpan(new ImageSpan(getHoldingActivity(),R.drawable.ic_blue_long), 34, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(spanBlack, 0, tvContent.getText().toString().length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }


        //改变生词底色
        final List<ReadingListResp.DataBean.NewWordsBean> newWords = mReadingInfo.getNewWords();

        for (ReadingListResp.DataBean.NewWordsBean newWord : newWords) {

            int wordStartIndex = newWord.getWordIndex();
            int wordEndIndex = wordStartIndex + newWord.getWordLength();
            int colorId = 0;
            if (newWord.getWordColor()==1) {
                colorId = R.style.style_pink;
            } else if (newWord.getWordColor()==2) {
                colorId = R.style.style_yellow;
            } else if (newWord.getWordColor()==3) {
                colorId = R.style.style_green;
            } else if (newWord.getWordColor()==4) {
                colorId = R.style.style_blue;
            } else if (newWord.getWordColor()==5) {
                colorId = R.style.style_purple;
            } else {
                colorId = R.style.style_yellow;
            }
            CharacterStyle cs = new TextAppearanceSpan(getHoldingActivity(), colorId);
            CharacterStyle cs2 = new UnderlineSpan();
            builder.setSpan(cs, wordStartIndex, wordEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(cs2, wordStartIndex, wordEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }

        if (mReadingInfo.getShowKeyWord().equals("1")){
            List<ReadingListResp.DataBean.EducationKeyword> keyWord=mReadingInfo.getEducationkeywords();
            for (ReadingListResp.DataBean.EducationKeyword bean:keyWord){
                int startIndex=bean.getWordIndex();
                int endIndex=startIndex+bean.getWordLength();
                CharacterStyle cs = new StyleSpan(Typeface.ITALIC);
                CharacterStyle cs2 = new UnderlineSpan();
                builder.setSpan(cs,startIndex,endIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.setSpan(cs2,startIndex,endIndex,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        tvContent.setText(builder);
        refreshProgress();
        }catch (Exception e){
            Log.e("cuowu",e.getMessage().toString());
        }
    }
    //添加一个
    private void addNoteTipsOne(final int po) {
        int positon = 0;
        for (int i=0;i<mReadingInfo.getNewWords().size();i++){
            if(Integer.valueOf(mReadingInfo.getNewWords().get(i).getNewWordId())==po){
                positon=i;
            }
        }

        float[] aa= getPosition(po);
        final ImageView iv=new ImageView(getHoldingActivity());
        iv.setImageResource(R.drawable.notes);

        iv.setId(Integer.valueOf(mReadingInfo.getNewWords().get(positon).getNewWordId()));
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int  i=0;i<mReadingInfo.getNewWords().size();i++){
                    if (mReadingInfo.getNewWords().get(i).getNewWordId().equals(String.valueOf(iv.getId()))){
                        final int finalI = i;
                        popupwindow.showTipPopupWindow(v, mReadingInfo.getNewWords().get(i).getNotes().getNoteTime(),
                                mReadingInfo.getNewWords().get(i).getNotes().getNoteContent(), new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showDialog(mReadingInfo.getNewWords().get(finalI).getWord(),finalI);
                                    }
                                });
                    }
                }

            }
        });
        AbsoluteLayout.LayoutParams lp = null;
        if (Iv_tips_size.equals("60")){
            if (tv_text_size.endsWith("18")){
                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(16),
                        SizeUtils.dp2px(16),(int)aa[0]+SizeUtils.sp2px(18)-SizeUtils.dp2px(5),(int)aa[1]+SizeUtils.dp2px(5));

            }else if ( tv_text_size.endsWith("16")){
                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(16),
                        SizeUtils.dp2px(16),(int)aa[0]+SizeUtils.sp2px(16)-SizeUtils.dp2px(5),(int)aa[1]+SizeUtils.dp2px(5));

            }else{
                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(16),
                        SizeUtils.dp2px(16),(int)aa[0]+SizeUtils.sp2px(14)-SizeUtils.dp2px(5),(int)aa[1]+SizeUtils.dp2px(5));

            }

        }else if (Iv_tips_size.equals("40")){
            if (tv_text_size.endsWith("18")){
                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(13),
                        SizeUtils.dp2px(13),(int)aa[0]+SizeUtils.sp2px(18)-SizeUtils.dp2px(5),(int)aa[1]+SizeUtils.dp2px(5));

            }else if ( tv_text_size.endsWith("16")){
                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(13),
                        SizeUtils.dp2px(13),(int)aa[0]+SizeUtils.sp2px(16)-SizeUtils.dp2px(5),(int)aa[1]+SizeUtils.dp2px(5));

            }else{
                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(13),
                        SizeUtils.dp2px(13),(int)aa[0]+SizeUtils.sp2px(14)-SizeUtils.dp2px(5),(int)aa[1]+SizeUtils.dp2px(5));

            }
        }else if(Iv_tips_size.equals("20")){
            if (tv_text_size.endsWith("18")){
                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(10),
                        SizeUtils.dp2px(10),(int)aa[0]+SizeUtils.sp2px(18)-SizeUtils.dp2px(5),(int)aa[1]+SizeUtils.dp2px(11));

            }else if ( tv_text_size.endsWith("16")){
                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(9),
                        SizeUtils.dp2px(9),(int)aa[0]+SizeUtils.sp2px(16)-SizeUtils.dp2px(5),(int)aa[1]+SizeUtils.dp2px(11));

            }else{
                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(8),
                        SizeUtils.dp2px(8),(int)aa[0]+SizeUtils.sp2px(14)-SizeUtils.dp2px(5),(int)aa[1]+SizeUtils.dp2px(11));

            }
        }

        frameContent.addView(iv,lp);
    }


    private void addNoteTips(){

        final List<ReadingListResp.DataBean.NewWordsBean> newWords = mReadingInfo.getNewWords();

        for (int i=0;i<newWords.size();i++){
                    if (mReadingInfo.getNewWords().get(i).getNoted()==0){

                    }else{

                        float[] aa= getPosition(Integer.valueOf(mReadingInfo.getNewWords().get(i).getNewWordId()));
                        final ImageView iv=new ImageView(getHoldingActivity());
                        iv.setImageResource(R.drawable.notes);

                        iv.setId(Integer.valueOf(mReadingInfo.getNewWords().get(i).getNewWordId()));
                        iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int  i=0;i<mReadingInfo.getNewWords().size();i++){
                                    final int finalI=i;
                                    if (mReadingInfo.getNewWords().get(i).getNewWordId().equals(String.valueOf(iv.getId()))){
                                        popupwindow.showTipPopupWindow(v, mReadingInfo.getNewWords().get(i).getNotes().getNoteTime(),
                                                mReadingInfo.getNewWords().get(i).getNotes().getNoteContent(), new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        showDialog(mReadingInfo.getNewWords().get(finalI).getWord(),finalI);

                                                    }
                                                });
                                    }
                                }
                            }
                        });
                        AbsoluteLayout.LayoutParams lp = null;
                        if (Iv_tips_size.equals("60")){
                            if (tv_text_size.endsWith("18")){
                                lp =new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(16),
                                        SizeUtils.dp2px(16),(int)aa[0]+ SizeUtils.sp2px(18)-SizeUtils.dp2px(5),
                                        (int)aa[1]+SizeUtils.dp2px(5));

                            }else if ( tv_text_size.endsWith("16")){
                                lp =new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(16),
                                        SizeUtils.dp2px(16),(int)aa[0]+ SizeUtils.sp2px(16)-SizeUtils.dp2px(5),
                                        (int)aa[1]+SizeUtils.dp2px(5));

                            }else{
                                lp =new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(16),
                                        SizeUtils.dp2px(16),(int)aa[0]+ SizeUtils.sp2px(14)-SizeUtils.dp2px(5),
                                        (int)aa[1]+SizeUtils.dp2px(5));


                            }


                        }else if (Iv_tips_size.equals("40")){
                            if (tv_text_size.endsWith("18")){
                                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(13),
                                        SizeUtils.dp2px(13),(int)aa[0]+SizeUtils.sp2px(18)-SizeUtils.dp2px(5),
                                        (int)aa[1]+SizeUtils.dp2px(5));

                            }else if ( tv_text_size.endsWith("16")){
                                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(13),
                                        SizeUtils.dp2px(13),(int)aa[0]+SizeUtils.sp2px(16)-SizeUtils.dp2px(5),
                                        (int)aa[1]+SizeUtils.dp2px(5));

                            }else{
                                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(13),
                                        SizeUtils.dp2px(13),(int)aa[0]+SizeUtils.sp2px(14)-SizeUtils.dp2px(5),
                                        (int)aa[1]+SizeUtils.dp2px(5));

                            }


                        }else if(Iv_tips_size.equals("20")){
                            if (tv_text_size.endsWith("18")){
                                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(10),
                                        SizeUtils.dp2px(10),(int)aa[0]+SizeUtils.sp2px(18)-SizeUtils.dp2px(5),(int)aa[1]+SizeUtils.dp2px(11));

                            }else if ( tv_text_size.endsWith("16")){
                                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(9),
                                        SizeUtils.dp2px(9),(int)aa[0]+SizeUtils.sp2px(16)-SizeUtils.dp2px(5),(int)aa[1]+SizeUtils.dp2px(11));

                            }else{
                                lp=new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(8),
                                        SizeUtils.dp2px(8),(int)aa[0]+SizeUtils.sp2px(14)-SizeUtils.dp2px(5),(int)aa[1]+SizeUtils.dp2px(11));

                            }


                        }

                        frameContent.addView(iv,lp);
                    }
                }

    }

    /**
     * 使用金山的回调
     */
    @Override
    public void showJsTranslate(JSTranslateBean jsTranslateBean,float[] positon) {
        if (jsTranslateBean != null) {
//            lilaTranslate.setVisibility(View.VISIBLE);
//            ivVoiceTranslateEn.setVisibility(View.VISIBLE);
//            tvSoundmarkTranslate.setVisibility(View.VISIBLE);


            if (jsTranslateBean.getWord_name() != null) {
//                JSTranslateBean.SymbolsBean partsBean = jsTranslateBean.getSymbols().get(0);
//                readUrl = partsBean.getPh_tts_mp3();
//                tvContentTranslate.setText(jsTranslateBean.getWord_name());
//                tvSoundmarkTranslate.setText("英式发音:   " + partsBean.getPh_en() + "\n" + "美式发音:   " + partsBean.getPh_am());
//                StringBuffer strTranslate = new StringBuffer("");
//                for (int i = 0; i < partsBean.getParts().size(); i++) {
//                    strTranslate.append(partsBean.getParts().get(i).getPart() + "   " + partsBean.getParts().get(i).getMeans() + "\n");
//                }
//                String str = strTranslate.toString();
//                if (!str.isEmpty()) {
//                    tvTranslateTranslate.setText(str);

//                }
                popupwindow.showJSTranslateWindow(getHoldingActivity(),jsTranslateBean,positon);

            } else showMsg("输入单词格式有误");


        }

    }

    /**
     * 使用有道的回调
     */

    @Override
    public void showYdTranslate(YDTranslateBean ydTranslateBean,float[] positon) {

        if (ydTranslateBean != null) {
//            lilaTranslate.setVisibility(View.VISIBLE);
//            ivVoiceTranslateEn.setVisibility(View.GONE);
//            tvSoundmarkTranslate.setVisibility(View.GONE);
            if (ydTranslateBean != null) {

                popupwindow.showYDTranslateWindow(getHoldingActivity(),ydTranslateBean,positon);
//                tvContentTranslate.setText(ydTranslateBean.getQuery());
//                tvTranslateTranslate.setText(ydTranslateBean.getTranslation().get(0));
            }
        } else showMsg("输入格式有误");

    }
//删除成功
    @Override
    public void deleteSuccess(String msg,String newWordId) {
        showToast(msg);
            /**
             * 删除本地数据中的生词 更新页面
             */
        List< ReadingListResp.DataBean.NewWordsBean> newWordsBeen=mReadingInfo.getNewWords();
        for (int i=0;i<newWordsBeen.size();i++){
            if (newWordsBeen.get(i).getNewWordId().equals(newWordId)){
                for (int j=0;j<frameContent.getChildCount();j++){
                    if (frameContent.getChildAt(j).getId()==Integer.valueOf(newWordId)){
                        frameContent.removeViewAt(j);
                    }
                }
                mReadingInfo.getNewWords().remove(i);
            }
        }

        moveToPosition(mSentenceIndex);

    }


//保存笔记成功
    @Override
    public void savaNoteSuccess(String context, String position, final int po, String time) {

        //Content 保存内容 positon 生词Id
        showToast("添加成功");

        if (mReadingInfo.getNewWords().get(po).getNotes()==null){
            //应该永远都不会进来
            mReadingInfo.getNewWords().get(po).setNoted(1);
            ReadingListResp.DataBean.NewWordsBean.notesBean notebean=new ReadingListResp.DataBean.NewWordsBean.notesBean();
            notebean.setNoteTime(time);
            notebean.setNoteContent(context);
            mReadingInfo.getNewWords().get(po).setNotes(notebean);
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            Message msg=new Message();
            msg.what=132;
            handler.sendMessage(msg);

        }else{
            if (mReadingInfo.getNewWords().get(po).getNoted()==0){
                mReadingInfo.getNewWords().get(po).setNoted(1);
                mReadingInfo.getNewWords().get(po).getNotes().setNoteContent(context);
                mReadingInfo.getNewWords().get(po).getNotes().setNoteTime(time);
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
                Message msg=new Message();
                msg.what=133;
                msg.arg1=Integer.valueOf(mReadingInfo.getNewWords().get(po).getNewWordId());
                handler.sendMessage(msg);

            }else{
                mReadingInfo.getNewWords().get(po).getNotes().setNoteContent(context);
                mReadingInfo.getNewWords().get(po).getNotes().setNoteTime(time);
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }


        }


//            moveToPosition(mSentenceIndex);
//        刷页面

//        addNoteTips();

    }


//标记成功回调
    @Override
    public void showMarkSuccess(markResp.DataBean newwordBean) {
        //标记成功将新词放入本地
//        如果有笔记保存笔记
//        删除交叉项，包含项
//        更新页面

        ReadingListResp.DataBean.NewWordsBean newWordsBean = new ReadingListResp.DataBean.NewWordsBean();
        newWordsBean.setWord(newwordBean.getSavedWord().getWord());
        newWordsBean.setWordIndex(newwordBean.getSavedWord().getWordIndex());
        newWordsBean.setWordLength(newwordBean.getSavedWord().getWordLength());
        newWordsBean.setNewWordId(newwordBean.getSavedWord().getNewWordId());
        newWordsBean.setWordColor(newwordBean.getSavedWord().getWordColor());
        newWordsBean.setNoted(newwordBean.getNoted());
//if (newwordBean.getSavedWord().getNotesBean().)
        if (newwordBean.getNoted()==0){
            ReadingListResp.DataBean.NewWordsBean.notesBean no=new ReadingListResp.DataBean.NewWordsBean.notesBean();
            no.setNoteContent("");
            no.setNoteTime("");
            newWordsBean.setNotes(no);

        }else{
            ReadingListResp.DataBean.NewWordsBean.notesBean no=new ReadingListResp.DataBean.NewWordsBean.notesBean();
            no.setNoteContent(newwordBean.getNotesBean().getNoteContent());
            no.setNoteTime(newwordBean.getNotesBean().getNoteTime());
            newWordsBean.setNotes(no);
        }

        mReadingInfo.getNewWords().add(newWordsBean);

        if (newwordBean.getToDeleteNewWordIds().isEmpty()){

        }else{
            int count = frameContent.getChildCount();
            for (int i=0;i<mReadingInfo.getNewWords().size();i++){
                for (int j=0;j<newwordBean.getToDeleteNewWordIds().size();j++){
                    if (mReadingInfo.getNewWords().get(i).getNewWordId().
                            equals(newwordBean.getToDeleteNewWordIds().get(j))){

                                for(int k=0;k<frameContent.getChildCount();k++){
                                    if(frameContent.getChildAt(k).getId()==Integer.valueOf(mReadingInfo.getNewWords().get(i).getNewWordId())){
                                        frameContent.removeViewAt(k);
                                    }
                                }
                        mReadingInfo.getNewWords().remove(i);
                    }
                }
            }
        }
        if (newwordBean.getNoted()==0){

        }else{
            Message msg=new Message();
            msg.what=133;
            msg.arg1=Integer.valueOf(newwordBean.getSavedWord().getNewWordId());
            handler.sendMessage(msg);

        }
        int position=mReadingInfo.getNewWords().indexOf(newWordsBean);

//        initMarkerWords();
        moveToPosition(mSentenceIndex);
        mSelectableTextHelper.updataNewWordsBean(mReadingInfo.getNewWords());
        mSelectableTextHelper.showTwo(position);
//        Message msss=new Message();
//        msss.what=132;
//        handler.sendMessage(msss);


    }

    @Override
    public void showReadingDataSuccess(ReadingDataResp.DataBean resp) {
        //获取数据显示
        showReadingDataDialog(resp);


    }

    @Override
    public void commitSuccess() {
        // TODO: 2017/7/28 三期
        if (isShowReadingData){

            if (mSentenceIndex +1==mReadingInfo.getSentenceInfo().size()
                    &&mReadingInfo.getArticleFinish()==1){
                if (mReadingInfo.getExercises().size()==0){
                    mPresenter.showReadingDataA(mReadingInfo.getClassId(),mReadingInfo.getCourseId());

                }else{
                    isShowReadingData=false;
                    goNext();

                }
            }else{
//                goNext();
            }

        }
        mReadingInfo.setReadingLineIndex(mSentenceIndex);
        // TODO: 2017/7/28 三期
    }

    public void showReadingDataDialog(ReadingDataResp.DataBean resp) {
        isShowReadingData=false;
        dialog=new Dialog(getHoldingActivity(),R.style.ActionSheetDialogStyle);
        view= LayoutInflater.from(getHoldingActivity()).inflate(R.layout.rlayout_reading_data_show,null);
        TextView tv_words_number= (TextView) view.findViewById(R.id.tv_words_number);
        TextView tv_reading_time= (TextView) view.findViewById(R.id.tv_reading_time);
        TextView tv_reading_speed= (TextView) view.findViewById(R.id.tv_reading_speed);
        TextView tv_collection_number= (TextView) view.findViewById(R.id.tv_collection_number);
        TextView tv_notes_number= (TextView) view.findViewById(R.id.tv_notes_number111);
        ImageView rl_exit= (ImageView) view.findViewById(R.id.rl_exit);
        tv_words_number.setText(resp.getWordsQuantity()+"个");
        tv_reading_time.setText(resp.getTimeConsumedDesc());
        tv_reading_speed.setText(resp.getReadingSpeed());
        tv_collection_number.setText(resp.getWordsCollected()+"");
        tv_notes_number.setText(resp.getNotesQuantity()+"");
        rl_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.show();//显示对话框

    }

    private float[] getPosition(int po) {
        float []  aa=new float[2];
//        获取位置
        for (int i=0;i<mReadingInfo.getNewWords().size();i++){
            if (po==Integer.valueOf(mReadingInfo.getNewWords().get(i).getNewWordId())){
                int start=mReadingInfo.getNewWords().get(i).getWordIndex();
                int end=start+mReadingInfo.getNewWords().get(i).getWordLength();

                Layout layout = tvContent.getLayout();
                Rect bound = new Rect();
                int line = layout.getLineForOffset(end);
                layout.getLineBounds(line, bound);
                float yAxisTop = bound.top;//字符顶部y坐标
                float  xAxisRight = layout.getSecondaryHorizontal(end);//字符右边x坐标

                aa[0]=xAxisRight;
                aa[1]=yAxisTop;
            }
        }

        return aa;

    }

    private void showDialog(String content, final int po) {
        dialog = new Dialog(getHoldingActivity(), R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        view = LayoutInflater.from(getHoldingActivity()).inflate(R.layout.layout_add_notes_activity, null);
        //初始化控件
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        btn_send = (Button) view.findViewById(R.id.btn_send);
        et_notes = (EditText) view.findViewById(R.id.et_notes);
        tv_note = (TextView) view.findViewById(R.id.tv_notes_sentence);
        tv_note.setText(content);
        if (mReadingInfo.getNewWords().get(po).getNoted()==0){

        }else{
            et_notes.setText(mReadingInfo.getNewWords().get(po).getNotes().getNoteContent());
        }
//        getHoldingActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(et_notes.getText().toString())) {

                   mPresenter.saveWordNote(mReadingInfo.getNewWords().get(po).getNewWordId(),et_notes.getText().toString(),po);

                } else {

                    Toast.makeText(getHoldingActivity(), et_notes.getText().toString(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });
        //将布局设置给Dialog
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager m = getHoldingActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 1); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

//
//        lp.y = 200;//设置Dialog距离底部的距离
//       将属性设置给窗体
//        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }


    private String deleteInterpunction(String s) {

        if (s.endsWith(".")||s.endsWith(",") || s.endsWith("!") || s.endsWith("！")
               || s.endsWith("?")||s.endsWith("\"")) {

            for(int i=0;i<3;i++){
                if (s.endsWith(".")||s.endsWith(",") || s.endsWith("!") || s.endsWith("！")
                        || s.endsWith("?")||s.endsWith("\"")) {
                    s = s.substring(0, s.length() - 1).trim();
                }

                if (s.startsWith(".")||s.startsWith(",") || s.startsWith("!") || s.startsWith("！")|| s.startsWith("?")||s.startsWith("\"")){
                s = s.substring(1, s.length()).trim();
            }

            }
        }
        return s;
    }


    /**
     * 功能简述:下一步
     */

    private void goNext() {
        //还有课后小题
        if (mReadingInfo.getExercises().size() == 0) {
            //// TODO: 2017/7/28 三期
            if (isShowReadingData&&mReadingInfo.getArticleFinish()!=1){
//

            }else{

                showToast("没有课后小题");
                getHoldingActivity().finish();
            }
            //// TODO: 2017/7/28 三期

            return;
        }
        ReadingListResp.DataBean.ExercisesBean bean = mReadingInfo.getExercises().get(0);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ReadingActivity.READING_INFO, mReadingInfo);
        bundle.putSerializable(ReadingActivity.READING_QUESTION, bean);

        switch (bean.getQuestionType()) {
            case ReadingActivity.TYPE_CHOICE:
                //选择题
                ReadingChoiceQuestionFragment choiceQuestionFragment = new ReadingChoiceQuestionFragment();
                choiceQuestionFragment.setArguments(bundle);
                addFragment(choiceQuestionFragment);
                break;
            case ReadingActivity.TYPE_INPUT:
                //填空题
                ReadingInputNewFragment inputQuestionFragment = new ReadingInputNewFragment();
                inputQuestionFragment.setArguments(bundle);
                addFragment(inputQuestionFragment);
                break;
            case ReadingActivity.TYPE_MULTI_CHOICE:
//                    多选题
                ReadingMultiChoiceQuestionFragment multiChoiceFragment = new ReadingMultiChoiceQuestionFragment();
                multiChoiceFragment.setArguments(bundle);
                addFragment(multiChoiceFragment);
                break;
            case ReadingActivity.TYPE_TRANSLATE:
//                    翻译题
                ReadingTranslateQuestionFragment translateQuestionFragment = new ReadingTranslateQuestionFragment();
                translateQuestionFragment.setArguments(bundle);
                addFragment(translateQuestionFragment);
                break;
            default:
                showSnackBar("没有该题型，请反馈客服");
                break;
        }
    }
    @Override
    public void showProgress() {
        showProgressDialog();

    }

    @Override
    public void hideProgress() {
        hideProgressDialog();

    }

    @Override
    public void showMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            showToast(R.string.server_error);
        } else {
            showToast(msg);
        }

    }

}

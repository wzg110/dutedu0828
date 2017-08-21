package com.yunding.dut.ui.ppt;

import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.adapter.PPTSelfButtonAdapter;
import com.yunding.dut.adapter.PPTSelfmediaAdapter;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.ppt.AutoAnswerSingleResp;
import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.model.resp.ppt.pptSelfListResp;
import com.yunding.dut.presenter.ppt.PPTAnswerPresenter;
import com.yunding.dut.ui.reading.ReadingActivity;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.NetworkUtils;
import com.yunding.dut.util.third.TimeUtils;
import com.yunding.dut.util.third.Utility;
import com.yunding.dut.view.DUTHorizontalRecyclerView;
import com.yunding.dut.view.HorizontalListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yunding.dut.ui.ppt.PPTActivity.PPT_QUESTION_INFO;
import static com.yunding.dut.ui.ppt.PPTSelfActivity.PPTINFO;
import static com.yunding.dut.ui.ppt.PPTSelfActivity.PPT_INFO_ITEM;

/**
 * 类 名 称：ReadingInputQuestionFragment
 * <P/>描    述：阅读填空题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/7/20
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/7/20
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class PPTQuestionMutiChoiceSelfFragment extends BackHandledFragment implements IPPTContentView {
    //    @BindView(R.id.horizontalListview_button)
//    HorizontalListView horizontalListviewButton;
    @BindView(R.id.tv_pagesize)
    TextView tvPagesize;
    @BindView(R.id.img_ppt)
    SimpleDraweeView imgPpt;
    @BindView(R.id.tv_page)
    TextView tvPage;
    @BindView(R.id.horizontalListview_media)
    HorizontalListView horizontalListviewMedia;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.iv_stop)
    ImageView ivStop;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.tv_all_time)
    TextView tvAllTime;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.ll_record_progress)
    LinearLayout llRecordProgress;
    @BindView(R.id.rl_media)
    RelativeLayout rlMedia;
    @BindView(R.id.tv_question_count)
    TextView tvQuestionCount;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.list_question)
    ListView listQuestion;
    @BindView(R.id.tv_analysis)
    TextView tvAnalysis;
    @BindView(R.id.layout_analysis)
    NestedScrollView layoutAnalysis;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.scrollview_list)
    ScrollView scrollviewList;
    @BindView(R.id.btn_last)
    Button btnLast;
    @BindView(R.id.btn_next)
    Button btnNext;

    @BindView(R.id.tv_time)
    TextView tvTime;
    Unbinder unbinder;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.tv_right_answer)
    TextView tvRightAnswer;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.horizontalListview_button)
    DUTHorizontalRecyclerView horizontalListviewButton;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    private Map<String, Boolean> multiChoice = new HashMap<>();//存放已经保存的选项
    public static Map<Integer, Boolean> isSelected = new HashMap<>();
    private singleChoiceAdapter adapter;
    private Dialog dialog;
    private View view;
    private PPTAnswerPresenter mPresenter;
    private MediaPlayer player;
    private PPTSelfButtonAdapter pptButtonAdapter;
    private PPTSelfmediaAdapter pptMediaAdapter;
    private int mQuestionIndex;
    private boolean mediaSetClick = true;//没有覆有点击事件 false 不能点击有覆盖
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int currentPosition;//当前音乐播放的进度
    private Timer timer;
    private pptSelfListResp.DataBean mPPTInfo;
    private pptSelfListResp.DataBean.SlidesBean mPPTInfoItem;
    private pptSelfListResp.DataBean.SlidesBean.slideQuestionsBean mPPTQuestionBean;
    private int quesionQuantity;
    private int pptIndex;
    private String recordUrl;
    private long startTime;
    private String exitWhich = "0";//0 退出  1 上一页  2下一页 3 remove
    private int buttonPosition;
    private String studyMode = "-1";
    private List<String> pptImageList = new ArrayList<>();
    private TextView tvfeedback;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question_mutichoice_self_fragment;
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    protected boolean onBackPressed() {
        String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
        mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), String.valueOf(mPPTInfoItem.getSelfTaughtId()), mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                , mPPTInfoItem.getTeachingId(), mPPTInfo.getSlides().get(pptIndex - 1).getPptStartTime(), endTime);

        getHoldingActivity().finish();
        return true;
    }

    @Override
    public void showMsg(String msg) {
        showToast(msg);

    }

    @Override
    public void commitSuccess() {

        btnNext.setVisibility(View.VISIBLE);
        ivRight.setVisibility(View.VISIBLE);
        mPPTQuestionBean.setQuestionCompleted(1);
        mPPTInfoItem.getSlideQuestions().set(mQuestionIndex, mPPTQuestionBean);
        int count = 0;
        for (int i = 0; i < mPPTInfoItem.getSlideQuestions().size(); i++) {
            if (mPPTInfoItem.getSlideQuestions().get(i).getQuestionCompleted() == 1) {
                count++;
            }
        }
        if (count == mPPTInfoItem.getSlideQuestions().size()) {
            mPPTInfoItem.setQuestionsCompleted(1);
        }
        if ((mQuestionIndex + 1) == mPPTInfoItem.getSlideQuestions().size()) {
            if (pptIndex == mPPTInfo.getSlides().size()) {
                btnNext.setVisibility(View.GONE);
                ivRight.setVisibility(View.GONE);
            } else {
                btnNext.setText("下一页");
            }
        } else {

            btnNext.setText("下一题");
        }


        mPPTInfo.getSlides().set(pptIndex - 1, mPPTInfoItem);
        btnCommit.setVisibility(View.GONE);
        if (mPPTQuestionBean.getSendAnalysisFlag() == 1) {
            layoutAnalysis.setVisibility(View.VISIBLE);
        }
        listQuestion.setFocusable(false);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getAnswerShow(String state) {

    }

    @Override
    public void saveAutoResp(PPTResp bean) {

    }

    @Override
    public void getNewPPTimage(String url) {

    }

    @Override
    public void getPollingPPTQuestion(List<? extends PPTResp.DataBean.slideQuestionsBean> dataList) {

    }

    @Override
    public void getAutoAnswerSingle(AutoAnswerSingleResp.DataBean dataBean) {

    }

    @Override
    public void getAnalysisFlag(String flag) {

    }


    @Override
    protected void initView(View view, Bundle saveInstanceState) {

        getHoldingActivity().getmToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pptIndex == mPPTInfo.getSlides().size() &&
                        (mQuestionIndex + 1) == mPPTInfoItem.getSlideQuestions().size()
                        && mPPTInfoItem.getQuestionsCompleted() == 1) {
                    String ss = "";
                    if (studyMode.equals("1")) {
                        ss = "此次学习已结束，是否退出?";
                    } else {
                        ss = "此次闯关已结束，是否退出?";
                    }

                    new MaterialDialog.Builder(getHoldingActivity())
                            .title("提示")
                            .content(ss)
                            .positiveText("确定")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                                    mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), String.valueOf(mPPTInfoItem.getSelfTaughtId()), mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                                            , mPPTInfoItem.getTeachingId(), mPPTInfo.getSlides().get(pptIndex - 1).getPptStartTime(), endTime);

                                    getHoldingActivity().finish();
                                }
                            })
                            .show();


                } else {
                    String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                    mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), String.valueOf(mPPTInfoItem.getSelfTaughtId()), mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                            , mPPTInfoItem.getTeachingId(), mPPTInfo.getSlides().get(pptIndex - 1).getPptStartTime(), endTime);

                    getHoldingActivity().finish();
                }


            }
        });
        tvfeedback = getHoldingActivity().getFeedBack();
        tvfeedback.setVisibility(View.VISIBLE);
        tvfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFeedBackDialog();
            }
        });
        startTime = TimeUtils.getNowTimeMills();
        PPTSelfActivity pa = (PPTSelfActivity) getActivity();
        pa.setToolbarTitle("自学");
        mPPTInfo = (pptSelfListResp.DataBean) getArguments().getSerializable(PPTINFO);//全部PPT信息
        mPPTQuestionBean = (pptSelfListResp.DataBean.SlidesBean.slideQuestionsBean)
                getArguments().getSerializable(PPT_QUESTION_INFO);//问题信息
        mPPTInfoItem = (pptSelfListResp.DataBean.SlidesBean) getArguments().getSerializable(PPT_INFO_ITEM);//单个ppt信息
        mPresenter = new PPTAnswerPresenter(this);

        mQuestionIndex = mPPTInfoItem.getSlideQuestions().indexOf(mPPTQuestionBean);
        pptIndex = Integer.valueOf(mPPTInfoItem.getPageIndex());
        quesionQuantity = mPPTInfoItem.getSlideQuestions().size();
        llRecordProgress.setVisibility(View.GONE);
        tvPage.setText("第" + mPPTInfoItem.getPageIndex() + "页");
        buttonPosition = pptIndex - 1;
        studyMode = getArguments().getString("STUDY_MODE");
        pptImageList = mPPTInfo.getPptImageList();
        if (studyMode.equals("2")) {
//闯关
            tvTime.setText("此课件已被老师设置成闯关模式回答正确即可进入下一题");
        } else {
            tvTime.setText("此课件已被老师设置成学习模式回答完成即可进入下一题");
        }

        imgPpt.setImageURI(Uri.parse(Apis.TEST_URL2 + mPPTInfoItem.getSlideImage()));
        tvQuestionCount.setText("多选" + (mQuestionIndex + 1) + "/" + quesionQuantity);
        adapter = new singleChoiceAdapter(mPPTQuestionBean.getOptionsList());
        listQuestion.setAdapter(adapter);
        Utility.setListViewHeightBasedOnChildren1(listQuestion);
//        setHeight();
        adapter.notifyDataSetChanged();

//有问题
        if (TextUtils.isEmpty(mPPTInfoItem.getPptStartTime())) {
            mPPTInfo.getSlides().get(pptIndex - 1).
                    setPptStartTime(TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss"));
        }
        if (mQuestionIndex == 0) {
            if (pptIndex == 1) {
                ivLeft.setVisibility(View.GONE);
                btnLast.setVisibility(View.GONE);
            } else {
                btnLast.setText("上一页");
            }

        } else {
            btnLast.setText("上一题");
        }
        if (mPPTQuestionBean.getQuestionCompleted() == 1) {
            btnNext.setVisibility(View.VISIBLE);
            ivRight.setVisibility(View.VISIBLE);
            btnCommit.setVisibility(View.GONE);
        } else {
            btnCommit.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.GONE);
            ivRight.setVisibility(View.GONE);
        }
        if ((mQuestionIndex + 1) == mPPTInfoItem.getSlideQuestions().size()) {
            if (pptIndex == mPPTInfo.getSlides().size()) {
                btnNext.setVisibility(View.GONE);
                ivRight.setVisibility(View.GONE);
            } else {
                btnNext.setText("下一页");
            }

        } else {

            btnNext.setText("下一题");
        }

        tvAnalysis.setText(mPPTQuestionBean.getAnalysis());
        if (mPPTQuestionBean.getQuestionCompleted() == 1) {
            layoutAnalysis.setVisibility(View.VISIBLE);
        } else {
            layoutAnalysis.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(mPPTQuestionBean.getRightAnswer().toString())) {

            llRight.setVisibility(View.GONE);
        } else {
            tvRightAnswer.setText(mPPTQuestionBean.getRightAnswer().toString());
            llRight.setVisibility(View.VISIBLE);
        }
        pptButtonAdapter = new PPTSelfButtonAdapter(mPPTInfo.getSlides(), pptIndex);
        horizontalListviewButton.setAdapter(pptButtonAdapter);
        pptButtonAdapter.notifyDataSetChanged();
        horizontalListviewButton.smoothScrollToPosition(pptIndex - 1);
        horizontalListviewButton.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mPPTInfo.getSlides().get(position).getQuestionsCompleted() == 1) {
                    String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                    mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), String.valueOf(mPPTInfoItem.getSelfTaughtId()), mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                            , mPPTInfoItem.getTeachingId(), mPPTInfo.getSlides().get(pptIndex - 1).getPptStartTime(), endTime);

                    addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.getSlides().get(position), studyMode));

                } else {
                    if (mPPTInfoItem.getQuestionsCompleted() == 1) {
                        if (position == pptIndex) {
                            String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                            mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), String.valueOf(mPPTInfoItem.getSelfTaughtId()), mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                                    , mPPTInfoItem.getTeachingId(), mPPTInfo.getSlides().get(pptIndex - 1).getPptStartTime(), endTime);

                            addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.getSlides().get(position), studyMode));
                        } else {
                            showToast("选择的PPT尚未完成不能进入。");
                        }

                    } else {
                        showToast("当前的PPT尚未完成不能进入。");
                    }
                }
            }
        });
//
        tvContent.setText(mPPTQuestionBean.getQuestionContent());
        if (mPPTQuestionBean.getQuestionCompleted() == 1) {
            listQuestion.setFocusable(false);
            btnCommit.setVisibility(View.GONE);
        }
        if (mPPTInfoItem.getSlideFiles().size() == 0) {
            rlMedia.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        } else {
            rlMedia.setVisibility(View.VISIBLE);

            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置音频类型
            pptMediaAdapter = new PPTSelfmediaAdapter(mPPTInfoItem.getSlideFiles(), getHoldingActivity());
            horizontalListviewMedia.setAdapter(pptMediaAdapter);
            pptMediaAdapter.notifyDataSetChanged();
//            seekbar.setFocusable(false);
//            seekbar.setClickable(false);
//            seekbar.setEnabled(false);
//            seekbar.setSelected(false);
            horizontalListviewMedia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mediaSetClick) {
//                         1-视频 2-音频 3-动图
                        if (3 == mPPTInfoItem.getSlideFiles().get(position).getFileType()) {
//                        gif
                            GifShowFragment vf = new GifShowFragment();
                            Bundle nb = new Bundle();
                            nb.putString("gifUrl", Apis.TEST_URL2 + mPPTInfoItem.getSlideFiles().get(position).getFileUrl());
                            vf.setArguments(nb);
                            addFragment(vf);
                        } else if (2 == mPPTInfoItem.getSlideFiles().get(position).getFileType()) {
//                        音频
                            ivPlay.setVisibility(View.VISIBLE);
                            ivStop.setVisibility(View.GONE);
                            if (player == null) {
                                player = new MediaPlayer();
                                player.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置音频类型
                            }

                            recordUrl = mPPTInfoItem.getSlideFiles().get(position).getFileUrl();
                            llRecordProgress.setVisibility(View.VISIBLE);
//                            rlMedia.setVisibility(View.GONE);
                            horizontalListviewMedia.setVisibility(View.GONE);
                            seekbar.setOnSeekBarChangeListener(new MySeekBar());//试着滚动条事件

                            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    timer.cancel();
                                    timer.purge();

                                    ivPlay.setVisibility(View.VISIBLE);
                                    ivStop.setVisibility(View.GONE);
                                    player.stop();
                                    player.release();
                                    player = null;
//                                    seekbar.setFocusable(false);
//                                    seekbar.setClickable(false);
//                                    seekbar.setSelected(false);
//                                    seekbar.setEnabled(false);
                                    currentPosition = 0;
//                                    player.seekTo(currentPosition);
                                    llRecordProgress.setVisibility(View.GONE);
                                    horizontalListviewMedia.setVisibility(View.VISIBLE);
                                    showToast("播放结束");
                                }
                            });

                        } else {
//                        视频
                            VideoShowFragment vf = new VideoShowFragment();
                            Bundle nb = new Bundle();
                            nb.putString("videoUrl", Apis.TEST_URL2 + mPPTInfoItem.getSlideFiles().get(position).getFileUrl());
                            vf.setArguments(nb);
                            addFragment(vf);
                        }

                    } else {

                    }
                }
            });

        }
        scrollviewList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (layoutAnalysis != null) {
                    layoutAnalysis.getParent().requestDisallowInterceptTouchEvent(false);
                }

                return false;
            }
        });
        layoutAnalysis.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

    }

    private void showFeedBackDialog() {
        dialog = new Dialog(getHoldingActivity(), R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        view = LayoutInflater.from(getHoldingActivity()).inflate(R.layout.layout_add_notes_activity, null);
        //初始化控件
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_send = (Button) view.findViewById(R.id.btn_send);
        final EditText et_notes = (EditText) view.findViewById(R.id.et_notes);
        TextView tv_note = (TextView) view.findViewById(R.id.tv_notes_sentence);
        tv_note.setVisibility(View.GONE);
        btn_send.setText("提交");
        et_notes.setHint("您对于该页PPT的问题反馈");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_notes.getText().toString().trim())) {
                    showToast("请输入您的问题描述");

                } else {
                    mPresenter.sendFeedBack(mPPTInfoItem.getTeachingId(), mPPTInfoItem.getSlideId()
                            , mPPTQuestionBean.getQuestionId(), mPPTInfoItem.getClassId(),
                            mPPTInfoItem.getSelfTaughtId() + "", et_notes.getText().toString().trim());
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

    public void setHeight() {
        int height = 0;
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View temp = adapter.getView(i, null, listQuestion);
            temp.measure(0, 0);
            height += temp.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = this.listQuestion.getLayoutParams();
        params.width = ViewGroup.LayoutParams.FILL_PARENT;
        params.height = height + 50;
        listQuestion.setLayoutParams(params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        tvfeedback.setVisibility(View.GONE);
        if (player != null) {
            player.stop();
            player.release();
            if (timer != null) {
                timer.cancel();
                timer.purge();
                timer = null;
            }
            player = null;
        }
    }

    @OnClick({R.id.img_ppt, R.id.iv_delete, R.id.iv_play, R.id.iv_stop, R.id.btn_commit, R.id.btn_last, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_ppt:
                if (player != null && player.isPlaying()) {
                    timer.cancel();
                    timer.purge();
                    player.stop();
                    player.release();
                    player = null;
                    ivPlay.setVisibility(View.VISIBLE);
                    ivStop.setVisibility(View.GONE);
                    llRecordProgress.setVisibility(View.GONE);
                    horizontalListviewMedia.setVisibility(View.VISIBLE);
                }

                ImageSelf vf = new ImageSelf();
                Bundle nb = new Bundle();
                nb.putString("imagePath", Apis.TEST_URL2 + mPPTInfoItem.getSlideImage());
                nb.putSerializable("pptImage", (Serializable) mPPTInfo.getPptImageList());
                nb.putSerializable(PPTINFO, mPPTInfo);
                nb.putInt("position", pptIndex - 1);
                vf.setArguments(nb);
                addFragment(vf);
                break;
            case R.id.btn_last:
                if (player != null && player.isPlaying()) {
                    timer.cancel();
                    timer.purge();
                    player.stop();
                    player.release();
                    player = null;
                    ivPlay.setVisibility(View.VISIBLE);
                    ivStop.setVisibility(View.GONE);
                    llRecordProgress.setVisibility(View.GONE);
                    horizontalListviewMedia.setVisibility(View.VISIBLE);
                }
                if (mQuestionIndex == 0) {
                    String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                    mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), String.valueOf(mPPTInfoItem.getSelfTaughtId()), mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                            , mPPTInfoItem.getTeachingId(), mPPTInfo.getSlides().get(pptIndex - 1).getPptStartTime(), endTime);
                    if ((pptIndex-1)==0){
                        getHoldingActivity().finish();
                    }else{
                        addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.getSlides().get(pptIndex - 2), studyMode));
                    }
                } else {

//                    removeFragment();
                    goPrevious();
                }
                break;
            case R.id.btn_next:
                if (player != null && player.isPlaying()) {
                    timer.cancel();
                    timer.purge();
                    player.stop();
                    player.release();
                    player = null;
                    ivPlay.setVisibility(View.VISIBLE);
                    ivStop.setVisibility(View.GONE);
                    llRecordProgress.setVisibility(View.GONE);
                    horizontalListviewMedia.setVisibility(View.VISIBLE);
                }
//
                if (mPPTInfoItem.getSlideQuestions().size() > (mQuestionIndex + 1)) {
//                    还有题进下一道题 页码题目
                    goNext();

                } else {

                    if (pptIndex < mPPTInfo.getSlides().size()) {
                        String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                        mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), String.valueOf(mPPTInfoItem.getSelfTaughtId()), mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                                , mPPTInfoItem.getTeachingId(), mPPTInfo.getSlides().get(pptIndex - 1).getPptStartTime(), endTime);
                        addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.getSlides().get(pptIndex), studyMode));


                    } else {
                        if (pptIndex == mPPTInfo.getSlides().size() &&
                                (mQuestionIndex + 1) == mPPTInfoItem.getSlideQuestions().size()
                                && mPPTInfoItem.getQuestionsCompleted() == 1) {
                            String ss = "";
                            if (studyMode.equals("1")) {
                                ss = "此次学习已结束，是否退出?";
                            } else {
                                ss = "此次闯关已结束，是否退出?";
                            }

                            new MaterialDialog.Builder(getHoldingActivity())
                                    .title("提示")
                                    .content(ss)
                                    .positiveText("确定")
                                    .negativeText("取消")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                                            mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), String.valueOf(mPPTInfoItem.getSelfTaughtId()), mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                                                    , mPPTInfoItem.getTeachingId(), mPPTInfo.getSlides().get(pptIndex - 1).getPptStartTime(), endTime);

                                            getHoldingActivity().finish();
                                        }
                                    })
                                    .show();


                        }
                    }
//                    没有提 提示切换ppt 传PPT页码题目


                }

                break;
            case R.id.btn_commit:
                if (player != null && player.isPlaying()) {
                    timer.cancel();
                    timer.purge();
                    player.stop();
                    player.release();
                    player = null;
                    ivPlay.setVisibility(View.VISIBLE);
                    ivStop.setVisibility(View.GONE);
                    llRecordProgress.setVisibility(View.GONE);
                    horizontalListviewMedia.setVisibility(View.VISIBLE);
                }
                if (multiChoice.isEmpty()) {
                    showToast("请选择答案选项。");
                } else {


                    long endTime = TimeUtils.getNowTimeMills();
                    long duringTime = endTime - startTime;
                    StringBuffer userChoice = new StringBuffer();

                    for (Map.Entry<String, Boolean> entry : multiChoice.entrySet()) {
                        userChoice = userChoice.append(entry.getKey()).append(",");
                    }
                    String userAnswer = userChoice.toString().substring(0, userChoice.length() - 1);


                    String[] riga = mPPTQuestionBean.getRightAnswer().split(",");
                    String[] answerts = userChoice.toString().split(",");
                    Arrays.sort(riga, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {

                            return o1.compareTo(o2);
                        }
                    });
                    Arrays.sort(answerts, new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {

                            return o1.compareTo(o2);
                        }
                    });


                    if (studyMode.equals("2")) {
                        if (Arrays.equals(riga, answerts)) {
                            mPPTInfo.getSlides().get(pptIndex - 1).getSlideQuestions().get(mQuestionIndex).setAnswerContent(userAnswer);

                            mPresenter.commitAnswer(mPPTInfoItem.getSlideId(), mPPTQuestionBean.getQuestionId(),
                                    mPPTInfoItem.getTeachingId(), String.valueOf(mPPTInfoItem.getSelfTaughtId()), userAnswer, duringTime);
                        } else {
                            multiChoice.clear();
                            isSelected.clear();
                            initChoice();
                            adapter.notifyDataSetChanged();
                            new MaterialDialog.Builder(getHoldingActivity())
                                    .title("提示")
                                    .content("回答错误")
                                    .positiveText("确定")
                                    .show();

                        }
                    } else {
                        mPPTInfo.getSlides().get(pptIndex - 1).getSlideQuestions().get(mQuestionIndex).setAnswerContent(userAnswer);

                        mPresenter.commitAnswer(mPPTInfoItem.getSlideId(), mPPTQuestionBean.getQuestionId(),
                                mPPTInfoItem.getTeachingId(), String.valueOf(mPPTInfoItem.getSelfTaughtId()), userAnswer, duringTime);
                    }

                }
                break;
            case R.id.iv_play:


                if (NetworkUtils.isWifiConnected()) {
                    //bofang

//                        if (currentPosition!=0){
//
//                        }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            player.reset();
                        }
                    }).start();
//                        }


                    ivPlay.setVisibility(View.GONE);
                    ivStop.setVisibility(View.VISIBLE);
                    play();

                } else {
                    showDialog();
                }


                break;
            case R.id.iv_stop:
                if (player.isPlaying()) {
                    timer.cancel();
                    timer.purge();//移除所有任务;
                    ivPlay.setVisibility(View.VISIBLE);
                    ivStop.setVisibility(View.GONE);
                    currentPosition = player.getCurrentPosition();//记录播放的位置
                    player.stop();//暂停状态
//                    seekbar.setEnabled(false);
//                    seekbar.setFocusable(false);
//                    seekbar.setClickable(false);
//                    seekbar.setSelected(false);
                }
                break;
            case R.id.iv_delete:
                if (player.isPlaying()) {
                    timer.cancel();
                    timer.purge();
                    player.stop();
                    player.release();
                    player = null;
                    currentPosition = 0;
                    tvStartTime.setText("00:00");

//                        player.reset();
                    ivPlay.setVisibility(View.VISIBLE);
                    ivStop.setVisibility(View.GONE);
                    llRecordProgress.setVisibility(View.GONE);
                    horizontalListviewMedia.setVisibility(View.VISIBLE);
                    tvAllTime.setText("00:00");
                } else {
//                        player.release();
                    ivPlay.setVisibility(View.VISIBLE);
                    ivStop.setVisibility(View.GONE);
                    llRecordProgress.setVisibility(View.GONE);
                    horizontalListviewMedia.setVisibility(View.VISIBLE);
//                    seekbar.setFocusable(false);
//                    seekbar.setClickable(false);
//                    seekbar.setSelected(false);
//                    seekbar.setEnabled(false);
//                    rlMedia.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void initChoice() {

        for (int i = 0; i < mPPTQuestionBean.getOptionsList().size(); i++) {
            isSelected.put(i, false);
        }

    }

    public class singleChoiceAdapter extends BaseAdapter {
        private List<pptSelfListResp.DataBean.SlidesBean.slideQuestionsBean.optionsListBean> mDataList;

        public singleChoiceAdapter(List<pptSelfListResp.DataBean.SlidesBean.slideQuestionsBean.optionsListBean> datalist) {
            this.mDataList = datalist;
            initChoice();

        }


        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false; // true 可以点击,false 不可以点击
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getHoldingActivity(), R.layout.multi_choice_item, null);

                holder.tv_index = (TextView) convertView.findViewById(R.id.tv_option_index);
                holder.tv_content = (TextView) convertView.findViewById(R.id.multi_option_content);
                holder.ll_multi_item = (LinearLayout) convertView.findViewById(R.id.ll_multi_choice_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ll_multi_item.setTag(position);

            if (mPPTQuestionBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED) {
                if (mPPTQuestionBean.getAnswerContent().contains(mDataList.get(position).getOptionIndex())) {
                    isSelected.put(position, true);
                    holder.tv_index.setTextColor(getResources().getColor(R.color.textColorShow));
                    holder.tv_content.setTextColor(getResources().getColor(R.color.textColorShow));
                    holder.ll_multi_item.setBackgroundResource(R.drawable.choice_blue_bg);
                } else {
                    holder.tv_index.setTextColor(getResources().getColor(R.color.text_color));
                    holder.tv_content.setTextColor(getResources().getColor(R.color.text_color));
                    holder.ll_multi_item.setBackgroundResource(R.drawable.choice_gray_bg);
                }
            } else {
                if (isSelected.get(position)) {
                    holder.tv_index.setTextColor(getResources().getColor(R.color.textColorShow));
                    holder.tv_content.setTextColor(getResources().getColor(R.color.textColorShow));
                    holder.ll_multi_item.setBackgroundResource(R.drawable.choice_blue_bg);
                } else {
                    holder.tv_index.setTextColor(getResources().getColor(R.color.text_color));
                    holder.tv_content.setTextColor(getResources().getColor(R.color.text_color));
                    holder.ll_multi_item.setBackgroundResource(R.drawable.choice_gray_bg);
                }
                holder.ll_multi_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer tag = (Integer) v.getTag();
                        if (isSelected.get(tag)) {
                            isSelected.put(tag, false);
                            multiChoice.remove(mDataList.get(position).getOptionIndex());
                        } else {

                            isSelected.put(tag, true);
                            multiChoice.put(mDataList.get(position).getOptionIndex(), true);
                        }
                        notifyDataSetChanged();
                    }

                });

            }
            holder.tv_index.setVisibility(View.VISIBLE);
            holder.tv_index.setTypeface(DUTApplication.getHsbwTypeFace());
            holder.tv_content.setTypeface(DUTApplication.getHsbwTypeFace());
            holder.tv_index.setText(mDataList.get(position).getOptionIndex() + ".");
            holder.tv_content.setText(mDataList.get(position).getOptionContent());

            return convertView;
        }
    }

    class ViewHolder {
        LinearLayout ll_multi_item;
        TextView tv_index;
        TextView tv_content;

    }

    /*进度条处理*/
    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
        }

        /*滚动时,应当暂停后台定时器*/
        public void onStartTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = true;
        }

        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            if (player.isPlaying()) {

                player.seekTo(seekBar.getProgress());
            } else {
                currentPosition = seekBar.getProgress();
            }
        }
    }

    private void showDialog() {
        dialog = new Dialog(getHoldingActivity(), R.style.ActionSheetDialogStyle);
        view = LayoutInflater.from(getHoldingActivity()).inflate(R.layout.login_exit_dialog, null);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        TextView tv_aa = (TextView) view.findViewById(R.id.tv_message_content);
        tv_aa.setText("确认使用非WIFI网络播放音频？");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                removeFragment();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                play();

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

    private void play() {
        try {
            player.setDataSource(Apis.TEST_URL2 + recordUrl);
            player.prepareAsync();

            //数据缓冲
                /*监听缓存 事件，在缓冲完毕后，开始播放*/
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                    player.seekTo(currentPosition);
                    ivPlay.setVisibility(View.GONE);
                    ivStop.setVisibility(View.VISIBLE);
                    seekbar.setMax(player.getDuration());
                    if (tvAllTime.getText().toString().equals("00:00") || TextUtils.isEmpty(tvAllTime.getText().toString())) {
                        tvAllTime.setText(TimeUtils.millis2String
                                (player.getDuration(), "mm:ss"));
                    }
//                    seekbar.setEnabled(true);
//                    seekbar.setFocusable(true);
//                    seekbar.setClickable(true);
//                    seekbar.setSelected(true);
                }
            });
            //监听播放时回调函数
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!isSeekBarChanging) {
                        seekbar.setProgress(player.getCurrentPosition());

                        Message msg = new Message();
                        msg.arg1 = player.getCurrentPosition();
                        handler.sendMessage(msg);
                    }
                }
            }, 0, 1000);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvStartTime.setText(TimeUtils.millis2String
                    (msg.arg1, "mm:ss"));
        }
    };

    private void goNext() {
        if (mPPTInfoItem.getSlideQuestions().size() > (mQuestionIndex + 1)) {
            pptSelfListResp.DataBean.SlidesBean.slideQuestionsBean bean = mPPTInfoItem.getSlideQuestions().get(mQuestionIndex + 1);
            Bundle bundle = new Bundle();
            bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
            bundle.putSerializable(PPT_QUESTION_INFO, bean);
            bundle.putSerializable(PPT_INFO_ITEM, mPPTInfoItem);
            bundle.putString("STUDY_MODE", studyMode);
            switch (bean.getQuestionType()) {
                case PPTActivity.TYPE_CHOICE:
                    PPTQuestionChoiceSelfFragment qcf = new PPTQuestionChoiceSelfFragment();
                    qcf.setArguments(bundle);
                    addFragment(qcf);
                    break;
                case PPTActivity.TYPE_EM:
                    PPTQuestionEnumerateSelfFragment qef = new PPTQuestionEnumerateSelfFragment();
                    qef.setArguments(bundle);
                    addFragment(qef);
                    break;
                case PPTActivity.TYPE_INPUT:
                    PPTQuestionInputSelfNewFragment qif = new PPTQuestionInputSelfNewFragment();
                    qif.setArguments(bundle);
                    addFragment(qif);
                    break;
                case PPTActivity.TYPE_MULTI_CHOICE:
                    PPTQuestionMutiChoiceSelfFragment qmf = new PPTQuestionMutiChoiceSelfFragment();
                    qmf.setArguments(bundle);
                    addFragment(qmf);
                    break;
                case PPTActivity.TYPE_QUESTION_ANSWER:
                    PPTQuestionAnswerSelfFragment qaf = new PPTQuestionAnswerSelfFragment();
                    qaf.setArguments(bundle);
                    addFragment(qaf);
                    break;

            }
        }

    }
    private void goPrevious(){

        pptSelfListResp.DataBean.SlidesBean.slideQuestionsBean bean = mPPTInfoItem.getSlideQuestions().get(mQuestionIndex-1);
        Bundle bundle = new Bundle();
        bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
        bundle.putSerializable(PPT_QUESTION_INFO, bean);
        bundle.putSerializable(PPT_INFO_ITEM, mPPTInfoItem);
        bundle.putString("STUDY_MODE", studyMode);
        switch (bean.getQuestionType()) {
            case PPTActivity.TYPE_CHOICE:
                PPTQuestionChoiceSelfFragment qcf = new PPTQuestionChoiceSelfFragment();
                qcf.setArguments(bundle);
                addFragment(qcf);
                break;
            case PPTActivity.TYPE_EM:
                PPTQuestionEnumerateSelfFragment qef = new PPTQuestionEnumerateSelfFragment();
                qef.setArguments(bundle);
                addFragment(qef);
                break;
            case PPTActivity.TYPE_INPUT:
                PPTQuestionInputSelfNewFragment qif = new PPTQuestionInputSelfNewFragment();
                qif.setArguments(bundle);
                addFragment(qif);
                break;
            case PPTActivity.TYPE_MULTI_CHOICE:
                PPTQuestionMutiChoiceSelfFragment qmf = new PPTQuestionMutiChoiceSelfFragment();
                qmf.setArguments(bundle);
                addFragment(qmf);
                break;
            case PPTActivity.TYPE_QUESTION_ANSWER:
                PPTQuestionAnswerSelfFragment qaf = new PPTQuestionAnswerSelfFragment();
                qaf.setArguments(bundle);
                addFragment(qaf);
                break;


        }


    }
}

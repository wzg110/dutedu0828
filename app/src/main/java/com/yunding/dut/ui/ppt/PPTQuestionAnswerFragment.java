package com.yunding.dut.ui.ppt;

import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.text.InputType;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.yunding.dut.adapter.DiscussQuestionInputAdapter;
import com.yunding.dut.adapter.PPTButtonAdapter;
import com.yunding.dut.adapter.PPTMediaAdapter;
import com.yunding.dut.model.resp.ppt.AutoAnswerSingleResp;
import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.presenter.ppt.PPTAnswerPresenter;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.NetworkUtils;
import com.yunding.dut.util.third.TimeUtils;
import com.yunding.dut.view.DUTHorizontalRecyclerView;
import com.yunding.dut.view.HorizontalListView;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.yunding.dut.ui.ppt.PPTActivity.PPTINFO;
import static com.yunding.dut.ui.ppt.PPTActivity.PPT_INFO_ITEM;
import static com.yunding.dut.ui.ppt.PPTActivity.PPT_QUESTION_INFO;

/**
 * 类 名 称：PPTQuestionAnswerFragment
 * <P/>描    述：PPT小题->问答
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 17:01
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 17:01
 * <P/>修改备注：
 * <P/>版    本：
 */

public class PPTQuestionAnswerFragment extends BackHandledFragment implements IPPTContentView {

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
    @BindView(R.id.et_answer)
    EditText etAnswer;
    //    @BindView(R.id.sl_question)
//    NestedScrollView slQuestion;
    @BindView(R.id.layout_analysis)
    NestedScrollView layoutAnalysis;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.btn_last)
    Button btnLast;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.srl_em)
    FrameLayout srlEm;
    Unbinder unbinder;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.iv_sandyglass)
    ImageView ivSandyglass;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.tv_right_answer)
    TextView tvRightAnswer;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.horizontalListview_button)
    DUTHorizontalRecyclerView horizontalListviewButton;
    @BindView(R.id.tv_analysis)
    TextView tvAnalysis;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    private Dialog dialog;
    private View view;
    private PPTAnswerPresenter mPresenter;
    private MediaPlayer player;
    private DiscussQuestionInputAdapter mInputAdapter;
    private PPTButtonAdapter pptButtonAdapter;
    private PPTMediaAdapter pptMediaAdapter;
    private int mQuestionIndex;
    private boolean mediaSetClick = true;//没有覆有点击事件 false 不能点击有覆盖
    private boolean recordStartOrStop = true;//音乐播放还是停止
    private boolean isCellPlay;/*在挂断电话的时候，用于判断是否为是来电时中断*/
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int currentPosition;//当前音乐播放的进度
    private Timer timer;
    DiscussionCountDown mCountDown;
    private String PPTType;//学习 闯关
    private boolean wifiStatus = false;
    private List<PPTResp.DataBean> mPPTInfo;
    private PPTResp.DataBean mPPTInfoItem;
    private PPTResp.DataBean.slideQuestionsBean mPPTQuestionBean;
    private int quesionQuantity;
    private int pptIndex;
    private String recordUrl;
    private long startTime;
    private String exitWhich = "0";//0 退出  1 上一页  2下一页 3 remove
    private int buttonPosition;
    private boolean isRefreshing = true;
    private TextView tvfeedback;
    private myRunnable runnable;
    private myRunnableFlag runnableFlag;
    private int LastOrNextFlag = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ppt_question_answer;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        isRefreshing = true;
        srlEm.clearFocus();
        getHoldingActivity().getmToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPPTInfoItem.getQuestionsCompleted() == 0) {
//                    未完成提示
                    new MaterialDialog.Builder(getHoldingActivity())
                            .title("提示")
                            .content("当前PPT尚未完成是否确认退出？退出则视为提交空答案。")
                            .positiveText("确定")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    mPresenter.savePPTAnswerAuto(mPPTInfoItem.getSlideId(), mPPTInfoItem.getTeachingId());
                                    exitWhich = "0";
                                }
                            })
                            .show();
                } else {
                    String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                    mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), "", mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                            , mPPTInfoItem.getTeachingId(), mPPTInfo.get(pptIndex - 1).getPptStartTime(), endTime);

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
        tvTips.setVisibility(View.GONE);
        startTime = TimeUtils.getNowTimeMills();
        PPTActivity pa = (PPTActivity) getActivity();
        pa.setToolbarTitle("课堂");
        mPPTInfo = (List<PPTResp.DataBean>) getArguments().getSerializable(PPTINFO);//全部PPT信息
        mPPTQuestionBean = (PPTResp.DataBean.slideQuestionsBean) getArguments().getSerializable(PPT_QUESTION_INFO);//问题信息
        mPPTInfoItem = (PPTResp.DataBean) getArguments().getSerializable(PPT_INFO_ITEM);//单个ppt信息
        mPresenter = new PPTAnswerPresenter(this);
        mQuestionIndex = mPPTInfoItem.getSlideQuestions().indexOf(mPPTQuestionBean);
        pptIndex = (getArguments().getInt("position") + 1);
        quesionQuantity = mPPTInfoItem.getSlideQuestions().size();
        llRecordProgress.setVisibility(View.GONE);
        tvPage.setText("第" + mPPTInfoItem.getPageIndex() + "页");
        buttonPosition = pptIndex - 1;
        tvContent.setText(mPPTQuestionBean.getQuestionContent());
        /**
         * 请求
         */
        // TODO: 2017/8/2
        mPresenter.pollingPPTImage(mPPTInfoItem.getTeachingId(), mPPTInfoItem.getSlideId());
        // TODO: 2017/8/2
        /**
         * 发布答案接口
         */
        if (mPPTQuestionBean.getSendAnalysisFlag() == 1) {

        } else {
            mPresenter.getAnalysisFlag(mPPTQuestionBean.getQuestionId());
        }

        // TODO: 2017/8/14
        /**
         * 轮询小题
         */
        StringBuilder sbQuestionIds = new StringBuilder();
        for (int i = 0; i < mPPTInfoItem.getSlideQuestions().size(); i++) {
            sbQuestionIds = sbQuestionIds.append(mPPTInfoItem.getSlideQuestions().get(i).getQuestionId()).append(",");
        }
        String idsString = sbQuestionIds.toString().substring(0, sbQuestionIds.length() - 1);
//        Log.e("长度", idsString);
        mPresenter.pollingPPTQuestion(mPPTInfoItem.getTeachingId(), mPPTInfoItem.getSlideId()
                , mPPTInfoItem.getSlideQuestions().size() + "", idsString);
        // TODO: 2017/8/14

        if (mQuestionIndex == 0) {
            mPPTInfo.get(pptIndex - 1).setPptStartTime(TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss"));
        }
        /**
         * 其实这段对于btn显示的代码使没有用的了
         */
        if (mQuestionIndex == 0) {
            if (pptIndex == 1) {
                btnLast.setVisibility(View.GONE);
                ivLeft.setVisibility(View.GONE);
            } else {
                btnLast.setText("上一页");
            }

        } else {
            btnLast.setText("上一题");
        }
        if ((mQuestionIndex + 1) == mPPTInfoItem.getSlideQuestions().size()) {
            if (pptIndex == mPPTInfo.size()) {
                btnNext.setVisibility(View.GONE);
                ivRight.setVisibility(View.GONE);
            } else {
                btnNext.setText("下一页");
            }

        } else {
            btnNext.setText("下一题");
        }
        tvAnalysis.setText(mPPTQuestionBean.getAnalysis());
/**
 * 答案解析显示逻辑处理
 */
        if (mPPTQuestionBean.getQuestionCompleted() == 1) {
            //不推送
            if (mPPTQuestionBean.getSendAnalysisFlag() == 0) {
                layoutAnalysis.setVisibility(View.GONE);
            } else {
                layoutAnalysis.setVisibility(View.VISIBLE);
            }
            etAnswer.setInputType(InputType.TYPE_NULL);
            btnCommit.setVisibility(View.GONE);
            if (TextUtils.isEmpty(mPPTQuestionBean.getAnswerContent())) {
                etAnswer.setText("");
            } else {
                etAnswer.setText(mPPTQuestionBean.getAnswerContent());
            }
            etAnswer.setFocusable(false);
        } else {
            //      倒计时
            etAnswer.setInputType(InputType.TYPE_CLASS_TEXT);
            etAnswer.setFocusable(true);
            etAnswer.setFocusableInTouchMode(true);
            etAnswer.requestFocus();
            layoutAnalysis.setVisibility(View.GONE);
            btnCommit.setVisibility(View.VISIBLE);
        }

        if (mPPTQuestionBean.getQuestionCompleted() == 1) {
            ivSandyglass.setVisibility(View.GONE);
            tvTime.setText("已完成");
            etAnswer.setEnabled(true);
            etAnswer.setFocusable(true);
            etAnswer.clearFocus();

        } else {
            if (mPPTQuestionBean.getAnswerTimeLimit() > 0) {
                mCountDown = new DiscussionCountDown(mPPTQuestionBean.getAnswerTimeLimit() * 60 * 1000, 1000);
                mCountDown.start();
            } else {

                ivSandyglass.setVisibility(View.GONE);
                tvTime.setText("无时限");

            }
        }

        /**
         * 横滚list输出显示处理
         */
        pptButtonAdapter = new PPTButtonAdapter(mPPTInfo, pptIndex);
        horizontalListviewButton.setAdapter(pptButtonAdapter);
        pptButtonAdapter.notifyDataSetChanged();
        horizontalListviewButton.smoothScrollToPosition(pptIndex - 1);


//        媒体数据不等于空的时候才显示要不gone
        imgPpt.setImageURI(Uri.parse(Apis.TEST_URL2 + mPPTInfoItem.getSlideImage()));
        tvQuestionCount.setText("问答" + (mQuestionIndex + 1) + "/" + quesionQuantity);
        horizontalListviewButton.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, final int position) {

                if (mPPTInfoItem.getQuestionsCompleted() == 0) {
                    new MaterialDialog.Builder(getHoldingActivity())
                            .title("提示")
                            .content("当前PPT尚未完成是否确认切换PPT？切换则视为提交空答案。")
                            .positiveText("确定")
                            .negativeText("取消")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    exitWhich = "4";
                                    mPresenter.savePPTAnswerAuto(mPPTInfoItem.getSlideId(),
                                            mPPTInfoItem.getTeachingId());

                                    buttonPosition = position;

                                }
                            })
                            .show();
                } else {

                    String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                    mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), "", mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                            , mPPTInfoItem.getTeachingId(), mPPTInfo.get(pptIndex - 1).getPptStartTime(), endTime);

                    addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.get(position), position));
                }


            }
        });

        if (mPPTInfoItem.getSlideFiles().size() == 0) {
            rlMedia.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        } else {
            rlMedia.setVisibility(View.VISIBLE);
            player = new MediaPlayer();
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置音频类型
            pptMediaAdapter = new PPTMediaAdapter(mPPTInfoItem.getSlideFiles(), getHoldingActivity());
            horizontalListviewMedia.setAdapter(pptMediaAdapter);
            pptMediaAdapter.notifyDataSetChanged();
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
                            horizontalListviewMedia.setVisibility(View.GONE);
                            seekbar.setOnSeekBarChangeListener(new MySeekBar());//试着滚动条事件
                            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    timer.cancel();
                                    timer.purge();
                                    recordStartOrStop = true;
                                    ivPlay.setVisibility(View.VISIBLE);
                                    ivStop.setVisibility(View.GONE);
                                    player.stop();
                                    player.release();
                                    player = null;
                                    currentPosition = 0;
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
        scrollview.setOnTouchListener(new View.OnTouchListener() {
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
        runnable = new myRunnable();
        runnableFlag = new myRunnableFlag();
    }

    /**
     * 问题反馈
     */
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
                            , mPPTQuestionBean.getQuestionId(), mPPTInfoItem.getClassId(), "", et_notes.getText().toString().trim());
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


    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String msg) {
        showToast(msg);

    }

    @Override
    public void commitSuccess() {
        mPPTQuestionBean.setAnswerContent(etAnswer.getText().toString());
        mPPTQuestionBean.setQuestionCompleted(1);
        mPPTInfoItem.getSlideQuestions().set(mQuestionIndex, mPPTQuestionBean);
        etAnswer.setFocusable(false);
        etAnswer.setInputType(InputType.TYPE_NULL);
        btnCommit.setVisibility(View.GONE);
        if (mPPTQuestionBean.getSendAnalysisFlag() == 1) {
            layoutAnalysis.setVisibility(View.VISIBLE);
        } else {
            layoutAnalysis.setVisibility(View.GONE);
        }
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
            if (pptIndex == mPPTInfo.size()) {
                btnNext.setVisibility(View.GONE);
                ivRight.setVisibility(View.GONE);
            } else {
                btnNext.setText("下一页");
            }

        } else {
            btnNext.setText("下一题");
        }
        mPPTInfo.set(pptIndex - 1, mPPTInfoItem);

    }

    @Override
    public void getAnswerShow(String state) {

    }

    @Override
    public void saveAutoResp(PPTResp bean) {
        mPPTInfoItem.setQuestionsCompleted(1);
        mPPTInfoItem.setSlideQuestions(bean.getData().get(0).getSlideQuestions());
        mPPTInfo.set(pptIndex - 1, mPPTInfoItem);
        String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
        mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), "", mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                , mPPTInfoItem.getTeachingId(), mPPTInfo.get(pptIndex - 1).getPptStartTime(), endTime);

        if (exitWhich.equals("0")) {
            getHoldingActivity().finish();

        } else if (exitWhich.equals("1")) {
            addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.get(pptIndex - 2), pptIndex - 2));


        } else if (exitWhich.equals("2")) {
            if (pptIndex < mPPTInfo.size()) {
                addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.get(pptIndex), pptIndex));
            } else {
                getHoldingActivity().finish();
            }
        } else if (exitWhich.equals("3")) {
            removeFragment();
        } else if (exitWhich.equals("4")) {
            addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.get(buttonPosition), buttonPosition));
        } else {
            showToast("答题时间结束，未完成题目系统已经自动作答。");
        }


    }

    /**
     * 轮训图片
     *
     * @param url
     */
    @Override
    public void getNewPPTimage(String url) {
        if (mPPTInfoItem.getSlideImage().equals(url)) {

        } else {
            mPPTInfoItem.setSlideImage(url);
            mPPTInfoItem.getPptImageList().set(pptIndex-1,url);
            mPPTInfo.set(pptIndex - 1, mPPTInfoItem);

            imgPpt.setImageURI(Uri.parse(Apis.TEST_URL2 + mPPTInfoItem.getSlideImage()));
        }
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        if (isRefreshing)
                            mPresenter.pollingPPTImage(mPPTInfoItem.getTeachingId(), mPPTInfoItem.getSlideId());

                    }
                });

    }

    // TODO: 2017/8/14 获取轮训小题
    @Override
    public void getPollingPPTQuestion(List<? extends PPTResp.DataBean.slideQuestionsBean> dataList) {
        if (dataList == null || dataList.size() == 0) {

        } else {
            mPPTInfoItem.setQuestionsCompleted(0);
            mPPTInfoItem.getSlideQuestions().addAll(dataList);
            mPPTInfo.set(pptIndex - 1, mPPTInfoItem);
            tvQuestionCount.setText("问答" + (mQuestionIndex + 1) + "/" + (quesionQuantity + dataList.size()));
            btnNext.setVisibility(View.VISIBLE);
            ivRight.setVisibility(View.VISIBLE);
        }
        handler.postDelayed(runnable, 2000);
    }

    /**
     * 自动提交当前小题答案
     *
     * @param dataBean
     */
    @Override
    public void getAutoAnswerSingle(AutoAnswerSingleResp.DataBean dataBean) {
        mPPTQuestionBean.setQuestionCompleted(1);
        mPPTInfoItem.getSlideQuestions().set(mQuestionIndex, dataBean);
        mPPTInfo.set(pptIndex - 1, mPPTInfoItem);
        int count = 0;
        for (int i = 0; i < mPPTInfoItem.getSlideQuestions().size(); i++) {
            if (mPPTInfoItem.getSlideQuestions().get(i).getQuestionCompleted() == 1) {
                count++;
            }
        }
        if (count == mPPTInfoItem.getSlideQuestions().size()) {
            mPPTInfoItem.setQuestionsCompleted(1);
        }
        if (LastOrNextFlag == 1024) {
            removeFragment();
        } else if (LastOrNextFlag == 1025) {
            goNext();
        } else if (LastOrNextFlag == 1026) {
            showToast("答题时间结束，当前题目系统已经自动作答。");
        } else if (LastOrNextFlag == 1027) {
            showToast("老师已经发布答案，当前题目系统已经自动作答。");
        }


    }

    /**
     * 手动发布答案处理
     *
     * @param flag
     */
    @Override
    public void getAnalysisFlag(String flag) {
        if (flag.equals("1")) {
            if (runnableFlag != null) {
                handler.removeCallbacks(runnableFlag);
            }
            if (mPPTQuestionBean.getQuestionCompleted() == 0) {
                LastOrNextFlag = 1027;
                mPresenter.autoAnswerSingle(mPPTQuestionBean.getQuestionId(), mPPTInfoItem.getTeachingId());
            }
            if (mCountDown != null) {
                mCountDown.cancel();
            }
            tvTime.setText("已完成");
            ivSandyglass.setVisibility(View.GONE);
            btnCommit.setVisibility(View.GONE);
            etAnswer.setEnabled(false);
            etAnswer.setInputType(InputType.TYPE_NULL);
            mPPTQuestionBean.setSendAnalysisFlag(1);
            mPPTInfoItem.getSlideQuestions().set(mQuestionIndex, mPPTQuestionBean);
            mPPTInfo.set(pptIndex - 1, mPPTInfoItem);
            layoutAnalysis.setVisibility(View.VISIBLE);
        } else {
            handler.postDelayed(runnableFlag, 2000);
        }

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
        isRefreshing = false;
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
        if (mCountDown != null) {
            mCountDown.cancel();
        }
        unbinder.unbind();
    }

    @OnClick({R.id.img_ppt, R.id.btn_last, R.id.btn_next, R.id.btn_commit, R.id.iv_play, R.id.iv_delete, R.id.iv_stop})
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
                Image vf = new Image();
                Bundle nb = new Bundle();
                nb.putString("imagePath", Apis.TEST_URL2 + mPPTInfoItem.getSlideImage());
                nb.putSerializable("pptImage", (Serializable) mPPTInfo.get(0).getPptImageList());
                nb.putInt("position", pptIndex - 1);
                nb.putSerializable(PPTINFO, (Serializable) mPPTInfo);
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
                    if (mPPTInfoItem.getQuestionsCompleted() == 0) {
                        new MaterialDialog.Builder(getHoldingActivity())
                                .title("提示")
                                .content("当前PPT尚未完成是否确认切换PPT？切换则视为提交空答案。")
                                .positiveText("确定")
                                .negativeText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        mPresenter.savePPTAnswerAuto(mPPTInfoItem.getSlideId(), mPPTInfoItem.getTeachingId());
                                        exitWhich = "1";
                                    }
                                })
                                .show();

                    } else {
                        if ((pptIndex - 1) == 0) {
                            String endTime2 = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                            mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), "", mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                                    , mPPTInfoItem.getTeachingId(), mPPTInfo.get(pptIndex - 1).getPptStartTime(), endTime2);

                            getHoldingActivity().finish();
                        } else {
                            String endTime2 = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                            mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), "", mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                                    , mPPTInfoItem.getTeachingId(), mPPTInfo.get(pptIndex - 1).getPptStartTime(), endTime2);

                            addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.get(pptIndex - 2), pptIndex - 2));


                        }
                    }


                } else {
                    if (mPPTQuestionBean.getQuestionCompleted() == 1) {
                        removeFragment();
                    } else {
                        new MaterialDialog.Builder(getHoldingActivity())
                                .title("提示")
                                .content("当前小题尚未完成是否切换小题？切换则视为当前小题提交空答案。")
                                .positiveText("确定")
                                .negativeText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        LastOrNextFlag = 1024;
                                        mPresenter.autoAnswerSingle(mPPTQuestionBean.getQuestionId(), mPPTInfoItem.getTeachingId());
                                    }
                                })
                                .show();
                    }

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
                if (mPPTInfoItem.getSlideQuestions().size() > (mQuestionIndex + 1)) {
//                    还有题进下一道题 页码题目
                    if (mPPTQuestionBean.getQuestionCompleted() == 1) {
                        goNext();
                    } else {
                        new MaterialDialog.Builder(getHoldingActivity())
                                .title("提示")
                                .content("当前小题尚未完成是否切换小题？切换则视为当前小题提交空答案。")
                                .positiveText("确定")
                                .negativeText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        LastOrNextFlag = 1025;
                                        mPresenter.autoAnswerSingle(mPPTQuestionBean.getQuestionId(), mPPTInfoItem.getTeachingId());
                                    }
                                })
                                .show();
                    }

                } else {

                    if (mPPTInfoItem.getQuestionsCompleted() == 0) {
                        new MaterialDialog.Builder(getHoldingActivity())
                                .title("提示")
                                .content("当前PPT尚未完成是否确认切换PPT？切换则视为提交空答案。")
                                .positiveText("确定")
                                .negativeText("取消")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        mPresenter.savePPTAnswerAuto(mPPTInfoItem.getSlideId(), mPPTInfoItem.getTeachingId());
                                        exitWhich = "2";
                                    }
                                })
                                .show();

                    } else {
                        if (pptIndex < mPPTInfo.size()) {
                            String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                            mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), "", mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                                    , mPPTInfoItem.getTeachingId(), mPPTInfo.get(pptIndex - 1).getPptStartTime(), endTime);
                            addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.get(pptIndex), pptIndex));
                        } else {
//                             最后一页ppt
                            getHoldingActivity().finish();
                        }

                    }

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
                long endTime = TimeUtils.getNowTimeMills();

                long duringTime = endTime - startTime;
                mPPTQuestionBean.setAnswerContent(etAnswer.getText().toString());
                mPPTInfoItem.getSlideQuestions().set(mQuestionIndex, mPPTQuestionBean);
                mPPTInfo.set(pptIndex - 1, mPPTInfoItem);
                mPresenter.commitAnswer(mPPTInfoItem.getSlideId(), mPPTQuestionBean.getQuestionId(),
                        mPPTInfoItem.getTeachingId(), "", etAnswer.getText().toString().trim(), duringTime);

                break;
            case R.id.iv_play:


                if (NetworkUtils.isWifiConnected()) {
                    recordStartOrStop = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {

                            player.reset();
                        }
                    }).start();
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
                    recordStartOrStop = true;
                    currentPosition = player.getCurrentPosition();//记录播放的位置
                    player.stop();//暂停状态
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
                    ivPlay.setVisibility(View.VISIBLE);
                    ivStop.setVisibility(View.GONE);
                    llRecordProgress.setVisibility(View.GONE);
                    horizontalListviewMedia.setVisibility(View.VISIBLE);
                } else {
                    ivPlay.setVisibility(View.VISIBLE);
                    ivStop.setVisibility(View.GONE);
                    llRecordProgress.setVisibility(View.GONE);
                    horizontalListviewMedia.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    protected boolean onBackPressed() {
        if (mPPTInfoItem.getQuestionsCompleted() == 0) {
            if (mQuestionIndex == 0) {
                new MaterialDialog.Builder(getHoldingActivity())
                        .title("提示")
                        .content("当前PPT尚未完成是否确认退出？退出则视为提交空答案。")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                mPresenter.savePPTAnswerAuto(mPPTInfoItem.getSlideId(), mPPTInfoItem.getTeachingId());
                                exitWhich = "3";

                            }
                        })
                        .show();
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }
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

    private class DiscussionCountDown extends CountDownTimer {

        public DiscussionCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
//            long spanToNow = TimeUtils.getTimeSpanByNow(mPPTInfoItem.getStartTime(), ConstUtils.TimeUnit.MSEC);
//            long timeLeft = mPPTInfoItem.getAnswerTimeLimit() * 60 * 1000 - spanToNow;
            long spanToNow = TimeUtils.getTimeSpanByNow(startTime, ConstUtils.TimeUnit.MSEC);
            long timeLeft = mPPTQuestionBean.getAnswerTimeLimit() * 60 * 1000 - spanToNow;
            if (timeLeft <= 0) {
                tvTime.setText("已完成");
                if (mCountDown != null) {
                    mCountDown.cancel();
                }
                ivSandyglass.setVisibility(View.GONE);
                btnCommit.setVisibility(View.GONE);
                etAnswer.setInputType(InputType.TYPE_NULL);
                etAnswer.setEnabled(false);
            } else if (mPPTQuestionBean.getQuestionCompleted() == 1) {
                tvTime.setText("已完成");
                if (mCountDown != null) {
                    mCountDown.cancel();
                }
                ivSandyglass.setVisibility(View.GONE);
                btnCommit.setVisibility(View.GONE);
                etAnswer.setInputType(InputType.TYPE_NULL);
                etAnswer.setEnabled(false);

            } else {
                tvTime.setText(TimeUtils.millis2String(timeLeft, "mm:ss"));

            }
        }

        @Override
        public void onFinish() {
            if (mPPTQuestionBean.getQuestionCompleted() == 0) {
//                exitWhich = "5";
                LastOrNextFlag = 1026;
//                mPresenter.savePPTAnswerAuto(mPPTInfoItem.getSlideId(), mPPTInfoItem.getTeachingId());
                mPresenter.autoAnswerSingle(mPPTQuestionBean.getQuestionId(), mPPTInfoItem.getTeachingId());
            }
            tvTime.setText("已完成");
            ivSandyglass.setVisibility(View.GONE);
            btnCommit.setVisibility(View.GONE);
            etAnswer.setEnabled(false);
            etAnswer.setInputType(InputType.TYPE_NULL);
        }
    }

    private void goNext() {
        if (mPPTInfoItem.getSlideQuestions().size() > (mQuestionIndex + 1)) {
            PPTResp.DataBean.slideQuestionsBean bean = mPPTInfoItem.getSlideQuestions().get(mQuestionIndex + 1);
            Bundle bundle = new Bundle();
            bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
            bundle.putSerializable(PPT_QUESTION_INFO, bean);
            bundle.putSerializable(PPT_INFO_ITEM, mPPTInfoItem);
            bundle.putInt("position", pptIndex - 1);
            switch (bean.getQuestionType()) {
                case PPTActivity.TYPE_CHOICE:
                    PPTQuestionChoiceFragment qcf = new PPTQuestionChoiceFragment();
                    qcf.setArguments(bundle);
                    addFragment(qcf);
                    break;
                case PPTActivity.TYPE_EM:
                    PPTQuestionEnumerateFragment qef = new PPTQuestionEnumerateFragment();
                    qef.setArguments(bundle);
                    addFragment(qef);
                    break;
                case PPTActivity.TYPE_INPUT:
                    PPTQuestionInputNewFragment qif = new PPTQuestionInputNewFragment();
                    qif.setArguments(bundle);
                    addFragment(qif);
                    break;
                case PPTActivity.TYPE_MULTI_CHOICE:
                    PPTQuestionMutiChoiceFragment qmf = new PPTQuestionMutiChoiceFragment();
                    qmf.setArguments(bundle);
                    addFragment(qmf);
                    break;
                case PPTActivity.TYPE_QUESTION_ANSWER:
                    PPTQuestionAnswerFragment qaf = new PPTQuestionAnswerFragment();
                    qaf.setArguments(bundle);
                    addFragment(qaf);
                    break;
            }
        }

    }

    class myRunnable implements Runnable {

        @Override
        public void run() {
            if (isRefreshing) {
                StringBuilder sbQuestionIds = new StringBuilder();
                for (int i = 0; i < mPPTInfoItem.getSlideQuestions().size(); i++) {
                    sbQuestionIds = sbQuestionIds.append(mPPTInfoItem.getSlideQuestions().get(i).getQuestionId()).append(",");
                }
                String idsString = sbQuestionIds.toString().substring(0, sbQuestionIds.length() - 1);

                mPresenter.pollingPPTQuestion(mPPTInfoItem.getTeachingId(), mPPTInfoItem.getSlideId()
                        , mPPTInfoItem.getSlideQuestions().size() + "", idsString);
            }

        }
    }

    class myRunnableFlag implements Runnable {

        @Override
        public void run() {
            if (isRefreshing) {
                mPresenter.getAnalysisFlag(mPPTQuestionBean.getQuestionId());
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        LastOrNextFlag = -1;
        isRefreshing = false;
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        if (runnableFlag != null) {
            handler.removeCallbacks(runnableFlag);
        }

    }
}

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
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.FrameLayout;
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
import com.yunding.dut.adapter.DiscussQuestionInputAdapter;
import com.yunding.dut.adapter.PPTButtonAdapter;
import com.yunding.dut.adapter.PPTMediaAdapter;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.ppt.AutoAnswerSingleResp;
import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.presenter.ppt.PPTAnswerPresenter;
import com.yunding.dut.ui.reading.ReadingActivity;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.NetworkUtils;
import com.yunding.dut.util.third.TimeUtils;
import com.yunding.dut.util.third.Utility;
import com.yunding.dut.view.DUTHorizontalRecyclerView;
import com.yunding.dut.view.HorizontalListView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * 类 名 称：ReadingInputQuestionFragment
 * <P/>描    述：阅读填空题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/7/11
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/7/11
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class PPTQuestionMutiChoiceFragment extends BackHandledFragment implements
        IPPTContentView {
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
    Unbinder unbinder;
    @BindView(R.id.sfl_input)
    FrameLayout sflInput;
    @BindView(R.id.scrollview_list)
    ScrollView scrollviewList;
    @BindView(R.id.iv_sandyglass)
    ImageView ivSandyglass;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.tv_analysis)
    TextView tvAnalysis;
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
    private String exitWhich = "0";
    private int buttonPosition;
    //    private List<String> pptImageList=new ArrayList<>();
    private boolean isRefreshing = true;
    private TextView tvfeedback;
    private myRunnable runnable;
    private myRunnableFlag runnableFlag;
    private int LastOrNextFlag = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ppt_question_choice;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        isRefreshing = true;
        runnable = new myRunnable();
        runnableFlag = new myRunnableFlag();
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
        startTime = TimeUtils.getNowTimeMills();
//        sflInput.setOnRefreshListener(this);
//        传ppt的页码
        PPTActivity pa = (PPTActivity) getActivity();
        pa.setToolbarTitle("课堂");
        mPPTInfo = (List<PPTResp.DataBean>) getArguments().getSerializable(PPTINFO);//全部PPT信息
        mPPTQuestionBean = (PPTResp.DataBean.slideQuestionsBean) getArguments().getSerializable(PPT_QUESTION_INFO);//问题信息
        mPPTInfoItem = (PPTResp.DataBean) getArguments().getSerializable(PPT_INFO_ITEM);//单个ppt信息
//        pptImageList= (List<String>) getArguments().getSerializable("pptImage");

        mPresenter = new PPTAnswerPresenter(this);
        mQuestionIndex = mPPTInfoItem.getSlideQuestions().indexOf(mPPTQuestionBean);
        pptIndex = (getArguments().getInt("position") + 1);
        quesionQuantity = mPPTInfoItem.getSlideQuestions().size();
        llRecordProgress.setVisibility(View.GONE);
        tvPage.setText("第" + mPPTInfoItem.getPageIndex() + "页");
        imgPpt.setImageURI(Uri.parse(Apis.TEST_URL2 + mPPTInfoItem.getSlideImage()));
        tvQuestionCount.setText("多选" + (mQuestionIndex + 1) + "/" + quesionQuantity);
        tvContent.setText(mPPTQuestionBean.getQuestionContent());

        adapter = new singleChoiceAdapter(mPPTQuestionBean.getOptionsList());
        listQuestion.setAdapter(adapter);
        Utility.setListViewHeightBasedOnChildren1(listQuestion);
//        setHeight();
        adapter.notifyDataSetChanged();

        /**
         * 请求
         */
        // TODO: 2017/8/2
        mPresenter.pollingPPTImage(mPPTInfoItem.getTeachingId(), mPPTInfoItem.getSlideId());
        // TODO: 2017/8/2
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

        buttonPosition = pptIndex - 1;


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


//             PPT没结束所以得校验
        if (mPPTQuestionBean.getQuestionCompleted() == 1) {
            //不推送
            if (mPPTQuestionBean.getSendAnalysisFlag() == 0) {
                layoutAnalysis.setVisibility(View.GONE);
            } else {
                //推送
                layoutAnalysis.setVisibility(View.VISIBLE);
            }
            listQuestion.setFocusable(false);
            btnCommit.setVisibility(View.GONE);
            tvTime.setText("已完成");

                ivSandyglass.setVisibility(View.GONE);
        } else {
            //      倒计时
            if (mPPTQuestionBean.getAnswerTimeLimit() > 0) {
                mCountDown = new DiscussionCountDown(mPPTQuestionBean.getAnswerTimeLimit() * 60 * 1000, 1000);
                mCountDown.start();
            } else {
                ivSandyglass.setVisibility(View.GONE);
                tvTime.setText("无时限");
            }

            layoutAnalysis.setVisibility(View.GONE);
            btnCommit.setVisibility(View.VISIBLE);

        }

        if (TextUtils.isEmpty(mPPTQuestionBean.getRightAnswer().toString())) {

            llRight.setVisibility(View.GONE);
        } else {
            tvRightAnswer.setText(mPPTQuestionBean.getRightAnswer().toString());
            llRight.setVisibility(View.VISIBLE);
        }
        pptButtonAdapter = new PPTButtonAdapter(mPPTInfo, pptIndex);
        horizontalListviewButton.setAdapter(pptButtonAdapter);
        pptButtonAdapter.notifyDataSetChanged();
        horizontalListviewButton.smoothScrollToPosition(pptIndex - 1);
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
                                    buttonPosition = position;
                                    exitWhich = "4";
                                    mPresenter.savePPTAnswerAuto(mPPTInfoItem.getSlideId(),
                                            mPPTInfoItem.getTeachingId());
//                                    addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.get(position)));
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
//        new Handler().postDelayed(new Runnable() {
//
//
//            public void run() {
//                horizontalListviewButton.setSelection(pptIndex-1);
//            }
//        }, 100);
//        媒体数据不等于空的时候才显示要不gone

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
                            if (player == null) {
                                player = new MediaPlayer();
                                player.setAudioStreamType(AudioManager.STREAM_MUSIC);//设置音频类型
                            }
                            ivPlay.setVisibility(View.VISIBLE);
                            ivStop.setVisibility(View.GONE);
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
        tvAnalysis.setText(mPPTQuestionBean.getAnalysis());
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
//
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
//                    没有提 提示切换ppt 传PPT页码题目
                    if (mPPTInfoItem.getQuestionsCompleted() == 0) {
                        new MaterialDialog.Builder(getHoldingActivity())
                                .title("提示")
                                .content("当前PPT尚未完成是否确认切PPT？切换则视为提交空答案。")
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
//
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


                if (multiChoice.isEmpty()) {
                    showToast("请选择答案选项。");

                } else {
                    StringBuffer userChoice = new StringBuffer();
                    for (Map.Entry<String, Boolean> entry : multiChoice.entrySet()) {
                        userChoice = userChoice.append(entry.getKey()).append(",");
                    }
                    String userAnswer = userChoice.toString().substring(0, userChoice.length() - 1);

                    mPPTInfoItem.getSlideQuestions().get(mQuestionIndex).setAnswerContent(userAnswer);

                    mPPTInfo.set(pptIndex - 1, mPPTInfoItem);
                    long endTime = TimeUtils.getNowTimeMills();
                    long duringTime = endTime - startTime;
                    mPresenter.commitAnswer(mPPTInfoItem.getSlideId(), mPPTQuestionBean.getQuestionId(),
                            mPPTInfoItem.getTeachingId(), "", userAnswer, duringTime);
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
//                    seekbar.setEnabled(false);
//                    seekbar.setFocusable(false);
//                    seekbar.setClickable(false);
//                    seekbar.setSelected(false);
                }
                break;
            case R.id.iv_play:


                if (NetworkUtils.isWifiConnected()) {
                    //bofang
                    recordStartOrStop = false;
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

//    @Override
//    public void onRefresh() {
//        mPresenter.getAnswerStatus(mPPTInfoItem.getSlideId(), mPPTQuestionBean.getQuestionId());
//
//
//    }

    @Override
    public void showProgress() {
//        if (sflInput != null)
//            sflInput.setRefreshing(true);

    }

    @Override
    public void hideProgress() {
//        if (sflInput != null)
//            sflInput.setRefreshing(false);

    }

    @Override
    public void showMsg(String msg) {
        showToast(msg);

    }

    @Override
    public void commitSuccess() {
        listQuestion.setFocusable(false);
        adapter.notifyDataSetChanged();
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
            if (pptIndex == mPPTInfo.size()) {
                ivRight.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);
            } else {
                btnNext.setText("下一页");
            }

        } else {
            btnNext.setText("下一题");
        }


        mPPTInfo.set(pptIndex - 1, mPPTInfoItem);
        btnCommit.setVisibility(View.GONE);
        if (mPPTQuestionBean.getSendAnalysisFlag() == 1) {
            layoutAnalysis.setVisibility(View.VISIBLE);
        } else {

            layoutAnalysis.setVisibility(View.GONE);

        }

    }

    @Override
    public void getAnswerShow(String state) {
//        if (mPPTQuestionBean.getQuestionCompleted() == 1) {
//            if (state.equals("1")) {
//                mPPTInfoItem.setSendAnswerAnalysis(1);
//                mPPTInfo.set(pptIndex - 1, mPPTInfoItem);
//                layoutAnalysis.setVisibility(View.VISIBLE);
//            }
//
//
//        }
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

    /**
     * 新推送的小题
     *
     * @param dataList
     */

    @Override
    public void getPollingPPTQuestion(List<? extends PPTResp.DataBean.slideQuestionsBean> dataList) {
        if (dataList == null || dataList.size() == 0) {

        } else {
            mPPTInfoItem.setQuestionsCompleted(0);
            mPPTInfoItem.getSlideQuestions().addAll(dataList);
            mPPTInfo.set(pptIndex - 1, mPPTInfoItem);
            tvQuestionCount.setText("多选" + (mQuestionIndex + 1) + "/" + (quesionQuantity + dataList.size()));
            btnNext.setVisibility(View.VISIBLE);
            ivRight.setVisibility(View.VISIBLE);
        }
        handler.postDelayed(runnable, 2000);
    }

    /**
     * 自动答题的返回
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
//        btnCommit.setVisibility(View.GONE);
//        //判断是否推送了答案
//        layoutAnalysis.setVisibility(View.VISIBLE);
        if (LastOrNextFlag == 1024) {
            removeFragment();
        } else if (LastOrNextFlag == 1025) {
            goNext();
        } else if (LastOrNextFlag == 1026) {
            showToast("答题时间结束，当前题目系统已经自动作答。");
        }
    }

    /**
     * 手动发布答案
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
            listQuestion.setFocusable(false);
            mPPTQuestionBean.setSendAnalysisFlag(1);
            mPPTInfoItem.getSlideQuestions().set(mQuestionIndex, mPPTQuestionBean);
            mPPTInfo.set(pptIndex - 1, mPPTInfoItem);
            layoutAnalysis.setVisibility(View.VISIBLE);
        } else {
            handler.postDelayed(runnableFlag, 2000);
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
                    ivSandyglass.setVisibility(View.GONE);
                    btnCommit.setVisibility(View.GONE);
                    listQuestion.setFocusable(false);
                }
            } else if (mPPTQuestionBean.getQuestionCompleted() == 1) {
                tvTime.setText("已完成");
                if (mCountDown != null) {
                    mCountDown.cancel();
                    ivSandyglass.setVisibility(View.GONE);
                    btnCommit.setVisibility(View.GONE);
                    listQuestion.setFocusable(false);
                }
            } else {
                tvTime.setText(TimeUtils.millis2String(timeLeft, "mm:ss"));
            }
        }

        @Override
        public void onFinish() {
            if (mPPTQuestionBean.getQuestionCompleted() == 0) {
//                exitWhich = "5";
//                mPresenter.savePPTAnswerAuto(mPPTInfoItem.getSlideId(), mPPTInfoItem.getTeachingId());
                LastOrNextFlag = 1026;
//                mPresenter.savePPTAnswerAuto(mPPTInfoItem.getSlideId(), mPPTInfoItem.getTeachingId());
//
                mPresenter.autoAnswerSingle(mPPTQuestionBean.getQuestionId(), mPPTInfoItem.getTeachingId());

            }
            tvTime.setText("已完成");
            ivSandyglass.setVisibility(View.GONE);
            btnCommit.setVisibility(View.GONE);
            listQuestion.setFocusable(false);
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

    public class singleChoiceAdapter extends BaseAdapter {
        private List<PPTResp.DataBean.slideQuestionsBean.optionsListBean> mDataList;

        public singleChoiceAdapter(List<PPTResp.DataBean.slideQuestionsBean.optionsListBean> datalist) {
            this.mDataList = datalist;
            initChoice();

        }

        private void initChoice() {

            for (int i = 0; i < mDataList.size(); i++) {
                isSelected.put(i, false);
            }

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
                    if (mPPTQuestionBean.getSubmitType() == 0) {
                        holder.tv_index.setTextColor(getResources().getColor(R.color.text_color));
                        holder.tv_content.setTextColor(getResources().getColor(R.color.text_color));
                        holder.ll_multi_item.setBackgroundResource(R.drawable.choice_gray_bg);
                    } else {
                        holder.tv_index.setTextColor(getResources().getColor(R.color.textColorShow));
                        holder.tv_content.setTextColor(getResources().getColor(R.color.textColorShow));
                        holder.ll_multi_item.setBackgroundResource(R.drawable.choice_blue_bg);
                    }
                    isSelected.put(position, true);

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
//                            initChoice();
//                            multiChoice.clear();
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

package com.yunding.dut.ui.ppt;

import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.adapter.PPTButtonAdapter;
import com.yunding.dut.adapter.PPTMediaAdapter;
import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.presenter.ppt.PPTAnswerPresenter;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.NetworkUtils;
import com.yunding.dut.util.third.TimeUtils;
import com.yunding.dut.view.DUTHorizontalRecyclerView;
import com.yunding.dut.view.HorizontalListView;

import java.io.Serializable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yunding.dut.ui.ppt.PPTTeacherActivity.PPTINFO;
import static com.yunding.dut.ui.ppt.PPTTeacherActivity.PPT_INFO_ITEM;
import static com.yunding.dut.ui.ppt.PPTTeacherActivity.PPT_QUESTION_INFO;

/**
 * 类 名 称：ReadingInputQuestionFragment
 * <P/>描    述：阅读填空题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/8/1
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/8/1
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class PPTQuestionEnumerateTeacherFragment extends BaseFragmentInReading {
    @BindView(R.id.horizontalListview_button)
    DUTHorizontalRecyclerView horizontalListviewButton;
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
    @BindView(R.id.line)
    View line;
    @BindView(R.id.tv_question_count)
    TextView tvQuestionCount;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.et_answer)
    EditText etAnswer;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.tv_right_answer)
    TextView tvRightAnswer;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.tv_analysis)
    TextView tvAnalysis;
    @BindView(R.id.layout_analysis)
    NestedScrollView layoutAnalysis;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.btn_last)
    Button btnLast;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.btn_next)
    Button btnNext;
    Unbinder unbinder;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    private Dialog dialog;
    private View view;
    private PPTAnswerPresenter mPresenter;
    private MediaPlayer player;
    private PPTButtonAdapter pptButtonAdapter;
    private PPTMediaAdapter pptMediaAdapter;
    private int mQuestionIndex;
    private boolean mediaSetClick = true;//没有覆有点击事件 false 不能点击有覆盖
    private boolean recordStartOrStop = true;//音乐播放还是停止
    private boolean isCellPlay;/*在挂断电话的时候，用于判断是否为是来电时中断*/
    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int currentPosition;//当前音乐播放的进度
    private Timer timer;
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
    private TextView tvfeedback;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_question_enumerate_self_fragment;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        getHoldingActivity().getmToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHoldingActivity().finish();
            }
        });
        tvfeedback = getHoldingActivity().getFeedBack();
        tvfeedback.setVisibility(View.GONE);
        tvfeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFeedBackDialog();
            }
        });
        PPTTeacherActivity pa = (PPTTeacherActivity) getActivity();
        pa.setToolbarTitle("预览");

        mPPTInfo = (List<PPTResp.DataBean>) getArguments().getSerializable(PPTINFO);//全部PPT信息
        mPPTQuestionBean = (PPTResp.DataBean.slideQuestionsBean) getArguments().getSerializable(PPT_QUESTION_INFO);//问题信息
        mPPTInfoItem = (PPTResp.DataBean) getArguments().getSerializable(PPT_INFO_ITEM);//单个ppt信息
        mQuestionIndex = mPPTInfoItem.getSlideQuestions().indexOf(mPPTQuestionBean);
        pptIndex = (getArguments().getInt("position") + 1);
        quesionQuantity = mPPTInfoItem.getSlideQuestions().size();
        llRecordProgress.setVisibility(View.GONE);
        tvPage.setText("第" + mPPTInfoItem.getPageIndex() + "页");
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
        tvContent.setText(mPPTQuestionBean.getQuestionContent());
        tvAnalysis.setText(mPPTQuestionBean.getAnalysis());
        layoutAnalysis.setVisibility(View.VISIBLE);
        btnCommit.setVisibility(View.GONE);

        tvTime.setText("");
        etAnswer.setText("");
        etAnswer.setHint("");
        etAnswer.setEnabled(false);
        etAnswer.setFocusable(false);
        etAnswer.clearFocus();
        pptButtonAdapter = new PPTButtonAdapter(mPPTInfo, pptIndex);
        horizontalListviewButton.setAdapter(pptButtonAdapter);
        pptButtonAdapter.notifyDataSetChanged();
        horizontalListviewButton.smoothScrollToPosition(pptIndex - 1);
        imgPpt.setImageURI(Uri.parse(Apis.TEST_URL2 + mPPTInfoItem.getSlideImage()));
        tvQuestionCount.setText("列举" + (mQuestionIndex + 1) + "/" + quesionQuantity);
        horizontalListviewButton.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, final int position) {
                addFragment(pptUtils.changePPTTeacher(mPPTInfo, mPPTInfo.get(position), position));
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
//                            rlMedia.setVisibility(View.GONE);
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

    @OnClick({R.id.img_ppt, R.id.iv_play, R.id.iv_stop, R.id.iv_delete, R.id.btn_last, R.id.btn_next})
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
                ImageShowFragment vf = new ImageShowFragment();
                Bundle nb = new Bundle();

                nb.putString("imagePath", Apis.TEST_URL2 + mPPTInfoItem.getSlideImage());
                nb.putSerializable("pptImage", (Serializable) mPPTInfo.get(0).getPptImageList());
                nb.putInt("position", pptIndex - 1);
                nb.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                vf.setArguments(nb);
                addFragment(vf);
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

//                        player.reset();
                    ivPlay.setVisibility(View.VISIBLE);
                    ivStop.setVisibility(View.GONE);
                    llRecordProgress.setVisibility(View.GONE);
                    horizontalListviewMedia.setVisibility(View.VISIBLE);
//                    tvAllTime.setText("00:00");
                } else {
//                        player.release();
                    ivPlay.setVisibility(View.VISIBLE);
                    ivStop.setVisibility(View.GONE);
                    llRecordProgress.setVisibility(View.GONE);
                    horizontalListviewMedia.setVisibility(View.VISIBLE);

//                    rlMedia.setVisibility(View.VISIBLE);
                }
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

                    if ((pptIndex-1)==0){
                        getHoldingActivity().finish();
                    }else{
                        addFragment(pptUtils.changePPTTeacher(mPPTInfo, mPPTInfo.get(pptIndex - 2), pptIndex - 2));
                    }
                } else {
                    removeFragment();
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
                    goNext();

                } else {
                    if (pptIndex < mPPTInfo.size()) {
                        addFragment(pptUtils.changePPTTeacher(mPPTInfo, mPPTInfo.get(pptIndex), pptIndex));

                    } else {
                        getHoldingActivity().finish();
                    }
                }
                break;
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
            showToast("音频文件异常");
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
            PPTResp.DataBean.slideQuestionsBean bean = mPPTInfoItem.getSlideQuestions().get(mQuestionIndex + 1);
            Bundle bundle = new Bundle();
            bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
            bundle.putSerializable(PPT_QUESTION_INFO, bean);
            bundle.putSerializable(PPT_INFO_ITEM, mPPTInfoItem);
            bundle.putInt("position", pptIndex - 1);
            switch (bean.getQuestionType()) {
                case PPTActivity.TYPE_CHOICE:
                    PPTQuestionChoiceTeacherFragment qcf = new PPTQuestionChoiceTeacherFragment();
                    qcf.setArguments(bundle);
                    addFragment(qcf);
                    break;
                case PPTActivity.TYPE_EM:
                    PPTQuestionEnumerateTeacherFragment qef = new PPTQuestionEnumerateTeacherFragment();
                    qef.setArguments(bundle);
                    addFragment(qef);
                    break;
                case PPTActivity.TYPE_INPUT:
                    PPTQuestionInputTeacherNewFragment qif = new PPTQuestionInputTeacherNewFragment();
                    qif.setArguments(bundle);
                    addFragment(qif);
                    break;
                case PPTActivity.TYPE_MULTI_CHOICE:
                    PPTQuestionMutiChoiceTeacherFragment qmf = new PPTQuestionMutiChoiceTeacherFragment();
                    qmf.setArguments(bundle);
                    addFragment(qmf);
                    break;
                case PPTActivity.TYPE_QUESTION_ANSWER:
                    PPTQuestionAnswerTeacherFragment qaf = new PPTQuestionAnswerTeacherFragment();
                    qaf.setArguments(bundle);
                    addFragment(qaf);
                    break;

            }
        }

    }
}

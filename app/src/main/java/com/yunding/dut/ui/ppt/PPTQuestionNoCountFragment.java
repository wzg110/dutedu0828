package com.yunding.dut.ui.ppt;

import android.app.Dialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.SeekBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.adapter.PPTButtonAdapter;
import com.yunding.dut.adapter.PPTMediaAdapter;
import com.yunding.dut.model.resp.ppt.AutoAnswerSingleResp;
import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.presenter.ppt.PPTAnswerPresenter;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.NetworkUtils;
import com.yunding.dut.util.third.TimeUtils;
import com.yunding.dut.view.DUTHorizontalRecyclerView;
import com.yunding.dut.view.HorizontalListView;

import java.io.Serializable;
import java.util.ArrayList;
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
 * 类 名 称：ReadingInputQuestionFragment
 * <P/>描    述：阅读填空题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/7/18
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/7/18
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class PPTQuestionNoCountFragment extends BackHandledFragment implements IPPTContentView {
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
    @BindView(R.id.iv_tips)
    ImageView ivTips;
    @BindView(R.id.btn_last)
    Button btnLast;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_time)
    TextView tvTime;
    //    @BindView(R.id.sfl_input)
//    SwipeRefreshLayout sflInput;
    Unbinder unbinder;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.horizontalListview_button)
    DUTHorizontalRecyclerView horizontalListviewButton;
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

    private boolean mediaSetClick = true;//没有覆有点击事件 false 不能点击有覆盖

    private boolean isSeekBarChanging;//互斥变量，防止进度条与定时器冲突。
    private int currentPosition;//当前音乐播放的进度
    private Timer timer;

    private String PPTType;//学习 闯关

    private List<PPTResp.DataBean> mPPTInfo;
    private PPTResp.DataBean mPPTInfoItem;
    private PPTResp.DataBean.slideQuestionsBean mPPTQuestionBean;

    private int pptIndex;
    private String recordUrl;
    private long startTime;
    //    private List<String> pptImageList=new ArrayList<>();
    private boolean isRefreshing = true;
    private TextView tvfeedback;
    private myRunnable runnable;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ppt_noquestion;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        isRefreshing = true;
        runnable = new myRunnable();
        getHoldingActivity().getmToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), "", mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                        , mPPTInfoItem.getTeachingId(), mPPTInfo.get(pptIndex - 1).getPptStartTime(), endTime);

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

        mPPTInfo = (List<PPTResp.DataBean>) getArguments().getSerializable(PPTINFO);//全部PPT信息
        mPPTQuestionBean = (PPTResp.DataBean.slideQuestionsBean) getArguments().getSerializable(PPT_QUESTION_INFO);//问题信息
        mPPTInfoItem = (PPTResp.DataBean) getArguments().getSerializable(PPT_INFO_ITEM);//单个ppt信息
//        pptImageList= (List<String>) getArguments().getSerializable("pptImage");

        mPresenter = new PPTAnswerPresenter(this);
//        mQuestionIndex=mPPTInfoItem.getSlideQuestions().indexOf(mPPTQuestionBean);
        pptIndex = getArguments().getInt("position") + 1;
        PPTActivity pa = (PPTActivity) getActivity();
        pa.setToolbarTitle("课堂");
//        quesionQuantity=mPPTInfoItem.getSlideQuestions().size();
        llRecordProgress.setVisibility(View.GONE);
        tvPage.setText("第" + mPPTInfoItem.getPageIndex() + "页");
        /**
         * 请求
         */
        // TODO: 2017/8/2
        mPresenter.pollingPPTImage(mPPTInfoItem.getTeachingId(), mPPTInfoItem.getSlideId());
        // TODO: 2017/8/2

        // TODO: 2017/8/14
        /**
         * 轮询小题
         */
//        StringBuilder sbQuestionIds=new StringBuilder();
//        for (int i=0;i<mPPTInfoItem.getSlideQuestions().size();i++){
//            sbQuestionIds=sbQuestionIds.append(mPPTInfoItem.getSlideQuestions().get(i).getQuestionId()).append(",");
//        }
//        String idsString=sbQuestionIds.toString().substring(0,sbQuestionIds.length()-1);
//        Log.e("长度",idsString);
        mPresenter.pollingPPTQuestion(mPPTInfoItem.getTeachingId(), mPPTInfoItem.getSlideId()
                , "", "");
        // TODO: 2017/8/14
        pptButtonAdapter = new PPTButtonAdapter(mPPTInfo, pptIndex);
        horizontalListviewButton.setAdapter(pptButtonAdapter);
        pptButtonAdapter.notifyDataSetChanged();
        horizontalListviewButton.smoothScrollToPosition(pptIndex - 1);
        horizontalListviewButton.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPPTInfo.get(pptIndex - 1).setQuestionsCompleted(1);
                String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), "", mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                        , mPPTInfoItem.getTeachingId(), mPPTInfo.get(pptIndex - 1).getPptStartTime(), endTime);
                addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.get(position), position));
            }
        });
        startTime = TimeUtils.getNowTimeMills();

        mPPTInfo.get(pptIndex - 1).setPptStartTime(TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss"));

        imgPpt.setImageURI(Uri.parse(Apis.TEST_URL2 + mPPTInfoItem.getSlideImage()));
//        horizontalListviewButton.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                判断ppt是否完成
////                removeAllFragment();
//                mPPTInfo.get(pptIndex - 1).setQuestionsCompleted(1);
//                String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
//                mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), "", mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
//                        , mPPTInfoItem.getTeachingId(), mPPTInfo.get(pptIndex - 1).getPptStartTime(), endTime);
//                addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.get(position), position));
//
//            }
//        });
        if (pptIndex == 1) {
            ivLeft.setVisibility(View.GONE);
            btnLast.setVisibility(View.GONE);
        } else {

            btnLast.setText("上一页");
        }

        if (pptIndex < mPPTInfo.size()) {
            btnNext.setText("下一页");

        } else {
            ivRight.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        }

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
//            new Handler().postDelayed(new Runnable() {
//
//
//                public void run() {
//                    horizontalListviewButton.setSelection(pptIndex-1);
//                }
//            }, 100);
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
                            , "", mPPTInfoItem.getClassId(), "", et_notes.getText().toString().trim());
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
        unbinder.unbind();

    }

    @Override
    protected boolean onBackPressed() {
        String endTime = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
        mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), "", mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                , mPPTInfoItem.getTeachingId(), mPPTInfo.get(pptIndex - 1).getPptStartTime(), endTime);

        return false;
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


    @OnClick({R.id.img_ppt, R.id.btn_last, R.id.btn_next, R.id.iv_play, R.id.iv_delete, R.id.iv_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_ppt:
//                if (player != null && player.isPlaying()) {
//                    timer.cancel();
//                    timer.purge();
//                    player.stop();
//                    player.release();
//                    player = null;
//                    ivPlay.setVisibility(View.VISIBLE);
//                    ivStop.setVisibility(View.GONE);
//                    llRecordProgress.setVisibility(View.GONE);
//                    horizontalListviewMedia.setVisibility(View.VISIBLE);
//                }
//                Image vf = new Image();
//                Bundle nb = new Bundle();
//                nb.putString("imagePath", Apis.TEST_URL2 + mPPTInfoItem.getSlideImage());
//                nb.putSerializable("pptImage", (Serializable) mPPTInfo.get(0).getPptImageList());
//                nb.putInt("position", pptIndex - 1);
//                nb.putSerializable(PPTINFO, (Serializable) mPPTInfo);
//                vf.setArguments(nb);
//                addFragment(vf);
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
                mPPTInfo.get(pptIndex - 1).setQuestionsCompleted(1);
                String endTime1 = TimeUtils.millis2String(TimeUtils.getNowTimeMills(), "yyyy-MM-dd HH:mm:ss");
                mPresenter.savePPTBrowerStayTime(mPPTInfoItem.getTeachingSlideId(), "", mPPTInfoItem.getSlideId(), mPPTInfoItem.getClassId()
                        , mPPTInfoItem.getTeachingId(), mPPTInfoItem.getPptStartTime(), endTime1);
                if (pptIndex == mPPTInfo.size()) {

                    getHoldingActivity().finish();
                } else {

                    addFragment(pptUtils.changePPT(mPPTInfo, mPPTInfo.get(pptIndex), pptIndex));
                }


                break;
//            case R.id.btn_commit:
//                List<String> answerList = mInputAdapter.getData();
//                String answerTemp = new Gson().toJson(answerList);
////                mPresenter.commitAnswer();
//
//                break;
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
            case R.id.iv_play:
                if (NetworkUtils.isWifiConnected()) {
                    //bofang

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


//    @Override
//    public void onRefresh() {
////        mPresenter.getAnswerStatus(mPPTInfoItem.getSlideId(), mPPTQuestionBean.getQuestionId());
//        if (sflInput != null)
//
//            sflInput.setRefreshing(false);
//
//    }

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

    }

    @Override
    public void getAnswerShow(String state) {
        if (mPPTQuestionBean.getQuestionCompleted() == 1) {
//            if (state.equals("1")){
//                layoutAnalysis.setVisibility(View.VISIBLE);
//            }

        }
    }

    @Override
    public void saveAutoResp(PPTResp bean) {

    }

    @Override
    public void getNewPPTimage(String url) {
        if (mPPTInfoItem.getSlideImage().equals(url)) {

        } else {
            mPPTInfoItem.setSlideImage(url);
//            mPPTInfoItem.getPptImageList().set(pptIndex-1,url);
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
     * 推送新小题返回值处理
     *
     * @param dataList
     */
    @Override
    public void getPollingPPTQuestion(List<? extends PPTResp.DataBean.slideQuestionsBean> dataList) {
        if (dataList == null || dataList.size() == 0) {

        } else {
            handler.removeCallbacks(runnable);
            mPPTInfoItem.setQuestionsCompleted(0);
            List<PPTResp.DataBean.slideQuestionsBean> DATALL = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i++) {
                DATALL.add(dataList.get(i));
            }
            mPPTInfoItem.setSlideQuestions(DATALL);
            mPPTInfo.set(pptIndex - 1, mPPTInfoItem);
            removeFragment();
            goNext();

        }
        handler.postDelayed(runnable, 2000);
    }

    private void goNext() {
        PPTResp.DataBean.slideQuestionsBean bean =
                mPPTInfoItem.getSlideQuestions().get(0);
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

    /**
     * 自动提交单题答案
     *
     * @param dataBean
     */
    @Override
    public void getAutoAnswerSingle(AutoAnswerSingleResp.DataBean dataBean) {

    }

    @Override
    public void getAnalysisFlag(String flag) {

    }

    class myRunnable implements Runnable {

        @Override
        public void run() {
            if (isRefreshing) {
                if (mPPTInfoItem.getSlideQuestions() == null || mPPTInfoItem.getSlideQuestions().size() == 0) {
                    mPresenter.pollingPPTQuestion(mPPTInfoItem.getTeachingId(), mPPTInfoItem.getSlideId()
                            , "", "");
                } else {
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
    }

    @Override
    public void onPause() {
        isRefreshing = false;
        handler.removeCallbacks(runnable);
        super.onPause();
    }

}

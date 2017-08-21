package com.yunding.dut.ui.discuss;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lqr.audio.AudioRecordManager;
import com.lqr.audio.IAudioRecordListener;
import com.yunding.dut.R;
import com.yunding.dut.adapter.DiscussListMsgAdapter;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.data.discuss.DiscussAnswerCache;
import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.model.resp.discuss.DiscussMsgListResp;
import com.yunding.dut.model.resp.discuss.DiscussQuestionListResp;
import com.yunding.dut.model.resp.discuss.DiscussSubjectResp;
import com.yunding.dut.presenter.discuss.DiscussPresenter;
import com.yunding.dut.presenter.discuss.DiscussQuestionPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.util.FontsUtils;
import com.yunding.dut.util.file.FileUtil;
import com.yunding.dut.util.permission.PermissionUtil;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.RegexUtils;
import com.yunding.dut.util.third.TimeUtils;
import com.yunding.dut.util.third.ToastUtils;
import com.yunding.dut.view.DUTVerticalSmoothScrollRecycleView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 类 名 称：DiscussActivity
 * <P/>描    述：讨论组讨论页面
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 11:39
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 11:39
 * <P/>修改备注：
 * <P/>版    本：
 */
public class DiscussActivity extends ToolBarActivity implements IDiscussView, IDiscussQuestionView,View.OnLayoutChangeListener{

    private View activityRootView;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    public static String DISCUSS_SUBJECT_INFO = "discuss_subject_info";
    public static String DISCUSS_QUESTIONG_LIST_INFO = "discuss_question_list_info";
    @BindView(R.id.rl_theme)
    RelativeLayout rlTheme;
    @BindView(R.id.iv_record_tips)
    ImageView ivRecordTips;
    @BindView(R.id.iv_blue)
    ImageView ivBlue;
    @BindView(R.id.tv_subject_title)
    TextView tvSubjectTitle;
    @BindView(R.id.rv_msg_list)
    DUTVerticalSmoothScrollRecycleView rvMsgList;
    @BindView(R.id.btn_open)
    Button btnOpen;
    @BindView(R.id.btn_record)
    ImageButton btnRecord;
    @BindView(R.id.et_msg_text)
    EditText etMsgText;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.ll_input)
    LinearLayout llInput;
    @BindView(R.id.btn_input)
    ImageButton btnInput;
    @BindView(R.id.btn_press_record)
    Button btnPressRecord;
    @BindView(R.id.ll_record)
    LinearLayout llRecord;
    @BindView(R.id.tv_count_down)
    TextView tvCountDown;
    @BindView(R.id.tv_go_answer)
    ImageView tvGoAnswer;
    @BindView(R.id.tv_subject_theme)
    TextView tvSubjectTheme;
    @BindView(R.id.ll_title_msg)
    LinearLayout llTitleMsg;
    @BindView(R.id.iv_expandable)
    ImageView ivExpandable;
    private ImageView iv_record_tips;
    private boolean needSendMsg = true;//是否发送消息 默认发送
    private ArrayList<DiscussAnswerCache> mDataCache;
    private DiscussListResp.DataBean mDiscussInfo;
    private DiscussListMsgAdapter mAdapter;
    private ArrayList<DiscussQuestionListResp.DataBean> mDataQuestionList;
    private DiscussQuestionPresenter mDiscussPresenter;
    private boolean isExpand = false;//是否展开 默认false
    private DiscussPresenter mPresenter;
    private static final String TAG = "DiscussActivity";
    private int dx, dy;
    private Dialog dialog;
    private View view;
    private String serverTime;
    private List<DiscussMsgListResp.DataBean.DatasBean> mDatas;
    private boolean isRefreshing = true;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);
        ButterKnife.bind(this);
        mDiscussInfo = (DiscussListResp.DataBean) getIntent().getSerializableExtra(DISCUSS_SUBJECT_INFO);
        mPresenter = new DiscussPresenter(this);
        mDiscussPresenter = new DiscussQuestionPresenter(this);
        if (mDiscussInfo != null) {
            mPresenter.loadSubjectInfo(mDiscussInfo.getThemeId());
            mDiscussPresenter.getSubjectQuestions(mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId());
            mDiscussPresenter.getServerTime();
        }
        activityRootView=this.findViewById(R.id.rl_rootview);
        //获取屏幕高度
        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;

        refreshView();
        initRecord();
        iv_record_tips = (ImageView) this.findViewById(R.id.iv_record_tips);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        final int screenWidth1 = dm.widthPixels;
        final int screenHeight1 = dm.heightPixels - 50;
        tvGoAnswer.setOnTouchListener(new View.OnTouchListener() {
            int lastX, lastY, x, y;
            int endX, endY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                int ea = event.getAction();
                switch (ea) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();// 获取触摸事件触摸位置的原始X坐标
                        lastY = (int) event.getRawY();
                        x = (int) event.getRawX();// 获取触摸事件触摸位置的原始X坐标
                        y = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        dx = (int) event.getRawX() - lastX;
                        dy = (int) event.getRawY() - lastY;
                        int l = v.getLeft() + dx;
                        int b = v.getBottom() + dy;
                        int r = v.getRight() + dx;
                        int t = v.getTop() + dy;

                        // 下面判断移动是否超出屏幕
                        if (l < 0) {
                            l = 0;
                            r = l + v.getWidth();
                        }

                        if (t < 0) {
                            t = 0;
                            b = t + v.getHeight();
                        }

                        if (r > screenWidth1) {
                            r = screenWidth1;
                            l = r - v.getWidth();
                        }

                        if (b > screenHeight1) {
                            b = screenHeight1;
                            t = b - v.getHeight();
                        }
                        v.layout(l, t, r, b);

                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        v.postInvalidate();
                        break;
                    case MotionEvent.ACTION_UP:
                        endX = (int) event.getRawX();
                        endY = (int) event.getRawY();
                        if (Math.abs(endX - x) < 50 && Math.abs(endY - y) < 50) {
                            showQuestions1();

                        }

                        break;
                }
                return false;
            }
        });
    }

    /**
     * 验证权限
     */
    private void checkPermissions() {
        if (PermissionUtil.checkDUTPermission(this)) {
            mPresenter.refreshMsg(mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId());
        }
    }

    /**
     * 创建菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_discuss, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_group_info:
                if (mDiscussInfo != null) {
                    Intent intent = new Intent(this, DiscussGroupActivity.class);
                    intent.putExtra(DiscussGroupActivity.GROUP_INFO, mDiscussInfo);
                    startActivity(intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 刷新UI
     */
    private void refreshView() {
        if (mDiscussInfo == null) return;
        switch (mDiscussInfo.getState()) {
            case DiscussPresenter.STATE_DISCUSS_NOT_START:
                setTitleInCenter(mDiscussInfo.getGroupName() + "（未开启）");
                llInput.setVisibility(View.GONE);
                llRecord.setVisibility(View.GONE);
                ivBlue.setVisibility(View.VISIBLE);
                tvCountDown.setText(TimeUtils.millis2String(mDiscussInfo.getCountdownTime() * 60 * 1000, "mm:ss"));
                btnOpen.setVisibility(mDiscussInfo.getIsLeader() == 1 ? View.VISIBLE : View.GONE);
                break;
            case DiscussPresenter.STATE_DISCUSSING:
                setTitleInCenter(mDiscussInfo.getGroupName() + "（进行中）");
                llInput.setVisibility(View.VISIBLE);
                llRecord.setVisibility(View.GONE);
                btnOpen.setVisibility(View.GONE);
                showDiscussing();
                break;
            case DiscussPresenter.STATE_DISCUSS_FINISHED:
                setTitleInCenter(mDiscussInfo.getGroupName() + "（已结束）");
                llInput.setVisibility(View.GONE);
                llRecord.setVisibility(View.GONE);
                ivBlue.setVisibility(View.GONE);
                tvCountDown.setText("已结束");
                btnOpen.setVisibility(View.GONE);
                break;
            case DiscussPresenter.STATE_DISCUSS_OUTOFDATE:
                setTitleInCenter(mDiscussInfo.getGroupName() + "（已过期）");
                llInput.setVisibility(View.GONE);
                llRecord.setVisibility(View.GONE);
                ivBlue.setVisibility(View.GONE);
                tvCountDown.setText("已过期");
                btnOpen.setVisibility(View.GONE);
                break;
        }
    }

    @OnClick({R.id.btn_open, R.id.btn_record, R.id.btn_send, R.id.btn_input, R.id.tv_go_answer, R.id.iv_expandable})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                //开启讨论组
                showDialog();
                break;
            case R.id.btn_record:
                //切换语音输入
                llRecord.setVisibility(View.VISIBLE);
                llInput.setVisibility(View.GONE);
                break;
            case R.id.btn_send:
                //发送文字消息
                String content = etMsgText.getText().toString();
                mPresenter.sendMsg(null, mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId(), DiscussPresenter.MSG_TYPE_TEXT, content.length(), content);
                break;
            case R.id.btn_input:
                //切换文字输入
                llRecord.setVisibility(View.GONE);
                llInput.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_expandable:
                if (isExpand) {
                    isExpand = false;
                    tvSubjectTheme.setMaxLines(2);

                } else {
                    isExpand = true;
                    tvSubjectTheme.setMaxLines(30);


                }
                break;
        }
    }

    /**
     * 回调未开始处理
     */
    @Override
    public void showDiscussNotStart() {
        setTitleInCenter(mDiscussInfo.getGroupName() + "（未开始)");
        llInput.setVisibility(View.GONE);
        llRecord.setVisibility(View.GONE);
        btnOpen.setVisibility(mDiscussInfo.getIsLeader() == 1 ? View.VISIBLE : View.GONE);
    }

    /**
     * 回调进行中处理
     */
    @Override
    public void showDiscussing() {
        mDiscussInfo.setState(DiscussPresenter.STATE_DISCUSSING);//更改状态为进行中
        setTitleInCenter(mDiscussInfo.getGroupName() + "（进行中)");
        if (llInput.getVisibility() == View.GONE &&
                llRecord.getVisibility() == View.GONE) {
            llInput.setVisibility(View.VISIBLE);
            llRecord.setVisibility(View.GONE);
        }

        btnOpen.setVisibility(View.GONE);
        if (serverTime != null) {
            showCountDown();
        }

    }

    /**
     * 回调进行中处理
     */
    @Override
    public void showDiscussingN(String opentime) {
        mDiscussInfo.setState(DiscussPresenter.STATE_DISCUSSING);//更改状态为进行中
        setTitleInCenter(mDiscussInfo.getGroupName() + "（进行中)");
        if (llInput.getVisibility() == View.GONE &&
                llRecord.getVisibility() == View.GONE) {
            llInput.setVisibility(View.VISIBLE);
            llRecord.setVisibility(View.GONE);
        }
        btnOpen.setVisibility(View.GONE);
        mDiscussInfo.setOpeningTime(opentime);

        if (serverTime != null) {
            showCountDown();
        }

    }

    /**
     * 回调已结束处理
     */
    @Override
    public void showDiscussFinished() {
        isRefreshing = false;
        tvCountDown.setText("已结束");
        ivBlue.setVisibility(View.GONE);
        mDiscussInfo.setState(2);
        setTitleInCenter(mDiscussInfo.getGroupName() + "（已结束)");
        llInput.setVisibility(View.GONE);
        llRecord.setVisibility(View.GONE);
        btnOpen.setVisibility(View.GONE);
    }

    /**
     * 开启倒计时
     */
    public void showCountDown() {

        if (mDiscussInfo.getState() == DiscussPresenter.STATE_DISCUSSING && mCountDown == null) {
            tvCountDown.setVisibility(View.VISIBLE);
            ivBlue.setVisibility(View.VISIBLE);
            tvCountDown.setText(TimeUtils.millis2String(mDiscussInfo.getCountdownTime() * 60 * 1000, "mm:ss"));
            if (TextUtils.isEmpty(serverTime)) {
                mDiscussPresenter.getServerTime();
            } else {

                long spanToNow = TimeUtils.getTimeSpan(serverTime, mDiscussInfo.getOpeningTime(), ConstUtils.TimeUnit.MSEC);
                long timeLeft = (mDiscussInfo.getCountdownTime() * 60 * 1000) - spanToNow;

                mCountDown = new DiscussionCountDown(timeLeft, 1000);
                mCountDown.start();
            }


        }


    }

    DiscussionCountDown mCountDown;

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){

            rvMsgList.smoothScrollToPosition(mDatas.size());

        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){

//            rvMsgList.smoothScrollToPosition(mDatas.size());

        }
    }

    /**
     * 倒计时
     */
    private class DiscussionCountDown extends CountDownTimer {

        public DiscussionCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {

            if (l <= 0) {

                showDiscussFinished();
                tvCountDown.setVisibility(View.GONE);
                if (mCountDown != null)
                    mCountDown.cancel();
            } else {
                tvCountDown.setText(TimeUtils.millis2String(l, "mm:ss"));
            }
        }

        @Override
        public void onFinish() {
            if (mDiscussInfo.getIsLeader() == 1 ){
                mDiscussPresenter.changeDiscussState(String.valueOf(mDiscussInfo.getGroupId()), String.valueOf(mDiscussInfo.getThemeId()));
            }
            showDiscussFinished();
        }
    }

    /**
     * 页面刷新回调处理数据
     *
     * @param resp
     */
    @Override
    public void showMsgList(DiscussMsgListResp resp) {
        Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        if (isRefreshing)
                            mPresenter.refreshMsg(mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId());
                    }
                });


        if (mAdapter == null) {
            mDatas = new ArrayList<>();
            mAdapter = new DiscussListMsgAdapter(mDatas, this);
            rvMsgList.setAdapter(mAdapter);
        }
        if (mDatas.size() != resp.getData().getDatas().size()) {
            mDatas.clear();
            mDatas.addAll(resp.getData().getDatas());
            mAdapter.notifyDataSetChanged();
            rvMsgList.smoothScrollToPosition(mDatas.size());
        }
    }

    /**
     * 获取讨论组小题处理
     *
     * @param resp
     */
    @Override
    public void showQuestions(DiscussQuestionListResp resp) {

        mDataQuestionList = new ArrayList<>();
        mDataQuestionList.addAll(resp.getData());

    }

    @Override
    public void showMsg(String msg) {
        if (msg != null) {
            showToast(msg);
        } else {
            showToast(R.string.server_error);
        }
    }

    /**
     * 获取服务时间处理
     *
     * @param time
     */
    @Override
    public void getServerTime(String time) {
        serverTime = time;

        if (mDiscussInfo.getState() == 1) {
            showCountDown();
        }
    }

    //英文正则
    private String REGEX_ENG = "[\\x00-\\xff]+";

    /**
     * 显示主题等信息处理
     *
     * @param resp
     */
    @Override
    public void showSubjectInfo(DiscussSubjectResp resp) {


        if (FontsUtils.isContainChinese(resp.getData().getName()) || TextUtils.isEmpty(resp.getData().getName())) {

        } else {
            tvSubjectTitle.setTypeface(DUTApplication.getHsbwTypeFace());
        }

        if (FontsUtils.isContainChinese(resp.getData().getContent()) || TextUtils.isEmpty(resp.getData().getContent())) {

        } else {
            tvSubjectTheme.setTypeface(DUTApplication.getHsbwTypeFace());
        }
        tvSubjectTheme.setText(resp.getData().getContent());
        tvSubjectTitle.setText(resp.getData().getName());
        if (TextUtils.isEmpty(mDiscussInfo.getCountdownTime() + "")) {
        } else {
            tvCountDown.setText(TimeUtils.millis2String(mDiscussInfo.getCountdownTime() * 60 * 1000, "mm:ss"));
        }

        if (resp.getData().getIsLanguage() == 0) {

            etMsgText.addTextChangedListener(new MsgTextWatcher());
        }
    }

    /**
     * 自定义文本监视器 只能输入英文
     */
    private class MsgTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (!TextUtils.isEmpty(charSequence) && !RegexUtils.isMatch(REGEX_ENG, charSequence)) {
                showToast("只能通过英文交流");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!RegexUtils.isMatch(REGEX_ENG, editable)) {
                etMsgText.removeTextChangedListener(this);
                List<String> matchList = RegexUtils.getMatches(REGEX_ENG, editable.subSequence(0, editable.length()));
                if (matchList.size() > 0) {
                    CharSequence matchChar = RegexUtils.getMatches(REGEX_ENG, editable.subSequence(0, editable.length())).get(0);
                    editable.replace(0, editable.length(), matchChar);
                } else {
                    editable.clear();
                }
                etMsgText.addTextChangedListener(this);
            }
        }
    }

    /**
     * 自定义长按事件监听
     */
    private class LongTouch implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            float yStart = 0;
            float yEnd = 0;
            switch (motionEvent.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    btnPressRecord.setText("松开结束");
                    iv_record_tips.setVisibility(View.VISIBLE);
                    btnPressRecord.setBackgroundResource(R.drawable.btn_selsected_record);
                    yStart = motionEvent.getY();
                    startRecord();
                    break;
                case MotionEvent.ACTION_UP:
                    btnPressRecord.setText("按住说话");
                    btnPressRecord.setBackgroundResource(R.drawable.btn_normal_record);
                    yEnd = motionEvent.getY();
                    float yOffset = yStart - yEnd;
                    if (yOffset > 200) {
                        needSendMsg = false;
                    } else {
                        needSendMsg = true;
                    }

                    stopRecord();
                    iv_record_tips.setVisibility(View.GONE);

                    break;

            }
            return true;
        }
    }

    /**
     * 初始化录音
     */
    private void initRecord() {
        checkPermissions();
        btnPressRecord.setOnTouchListener(new LongTouch());
        String audioDir = FileUtil.getRecordVoiceDir();
        AudioRecordManager.getInstance(this).setAudioSavePath(audioDir);
        AudioRecordManager.getInstance(this).setMaxVoiceDuration(60);
        AudioRecordManager.getInstance(this).setAudioRecordListener(new DiscussRecordListener());
    }

    private void startRecord() {
        AudioRecordManager.getInstance(this).startRecord();
    }

    private void stopRecord() {
        AudioRecordManager.getInstance(this).stopRecord();
    }

    private class DiscussRecordListener implements IAudioRecordListener {

        @Override
        public void initTipView() {

        }

        @Override
        public void setTimeoutTipView(int counter) {

        }

        @Override
        public void setRecordingTipView() {

        }

        @Override
        public void setAudioShortTipView() {

        }

        @Override
        public void setCancelTipView() {

        }

        @Override
        public void destroyTipView() {

        }

        @Override
        public void onStartRecord() {

        }

        @Override
        public void onFinish(Uri audioPath, int duration) {
            if (needSendMsg) {
                mPresenter.sendMsg(new File(audioPath.getPath()),
                        mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId(),
                        DiscussPresenter.MSG_TYPE_VOICE, duration, "");
            } else {
                ToastUtils.showShortToast("取消发送");

            }
        }

        @Override
        public void onAudioDBChanged(int db) {

        }
    }

    /**
     * 跳转小题页面处理
     */
    private void showQuestions1() {
        Intent intent = new Intent(this, DiscussQuestionNewActivity.class);
        intent.putExtra(DiscussQuestionNewActivity.DISCUSS_INFO, mDiscussInfo);
        intent.putExtra(DiscussQuestionNewActivity.DISCUSS_QUESTIONG_LIST_INFO, mDataQuestionList);
        if (mDataCache != null) {
            intent.putExtra("datas", mDataCache);
        }
        startActivityForResult(intent, 100);
    }

    private void showDialog() {

        showDialogStart();
    }

    /**
     * 处理返回缓存数据
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 100) {
            mDataCache = new ArrayList<>();
            mDataCache.clear();
            mDataCache = (ArrayList<DiscussAnswerCache>) data.getSerializableExtra("datas");
            int status = data.getIntExtra("state", -1);
            if (status == 2) {
                showDiscussFinished();
                mDiscussInfo.setState(2);
            }

        }
    }

    /**
     * 开始提示框
     */
    private void showDialogStart() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        view = LayoutInflater.from(this).inflate(R.layout.login_exit_dialog, null);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        TextView tv = (TextView) view.findViewById(R.id.tv_message_content);
        tv.setText("开启讨论后开始进入倒计时" + mDiscussInfo.getCountdownTime() + "分钟，确定开启讨论？");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDiscussInfo != null && mPresenter != null) {
                    try {


                        mPresenter.startDiscussion(mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId());
                        mDiscussPresenter.getServerTime();
                        String content = "我已开启讨论";
                        mPresenter.sendMsg(null, mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId(), DiscussPresenter.MSG_TYPE_TEXT, content.length(), content);
                    } catch (Exception e) {
                        Log.e("ssd", e.getMessage());
                    }
                } else showMsg("开启失败");

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

    @Override
    protected void onResume() {
        super.onResume();
        activityRootView.addOnLayoutChangeListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.PERMISSION_REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.refreshMsg(mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId());
            } else {
                new MaterialDialog.Builder(this)
                        .title("抱歉")
                        .content("讨论组功能需要存储权限，不开启将无法正常工作")
                        .positiveText("确定")
                        .show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.stopRefreshMsg();
        isRefreshing = false;
        if (mCountDown != null)
            mCountDown.cancel();
        super.onDestroy();
    }

    @Override
    public void sendTextMsgSuccess() {
        etMsgText.setText("");
    }

    @Override
    public void sendVoiceMsgSuccess() {

    }

    @Override
    public void commitAnswerSuccess() {

    }

    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showBadNetwork() {

    }

    @Override
    public void showListFailed() {

    }
}

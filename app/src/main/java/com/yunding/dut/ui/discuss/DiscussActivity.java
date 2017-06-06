package com.yunding.dut.ui.discuss;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.lqr.audio.AudioRecordManager;
import com.lqr.audio.IAudioRecordListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.yunding.dut.R;
import com.yunding.dut.adapter.DiscussListMsgAdapter;
import com.yunding.dut.model.data.discuss.DiscussAnswerCache;
import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.model.resp.discuss.DiscussMsgListResp;
import com.yunding.dut.model.resp.discuss.DiscussSubjectResp;
import com.yunding.dut.presenter.discuss.DiscussPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.util.file.FileUtil;
import com.yunding.dut.util.permission.PermissionUtil;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.RegexUtils;
import com.yunding.dut.util.third.TimeUtils;
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
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DiscussActivity extends ToolBarActivity implements IDiscussView {

    public static String DISCUSS_SUBJECT_INFO = "discuss_subject_info";

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
    @BindView(R.id.expand_text_view)
    ExpandableTextView tvQuestion;
    @BindView(R.id.tv_go_answer)
    TextView tvGoAnswer;
    private ArrayList<DiscussAnswerCache> mDataCache;
    private DiscussListResp.DataBean mDiscussInfo;
    private DiscussListMsgAdapter mAdapter;

    private DiscussPresenter mPresenter;
    private static final String TAG = "DiscussActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);
        ButterKnife.bind(this);

        mDiscussInfo = (DiscussListResp.DataBean) getIntent().getSerializableExtra(DISCUSS_SUBJECT_INFO);
        mPresenter = new DiscussPresenter(this);
        if (mDiscussInfo != null) {
            mPresenter.loadSubjectInfo(mDiscussInfo.getThemeId());
        }

        refreshView();
        initRecord();
    }

    private void checkPermissions() {
        if (PermissionUtil.checkDUTPermission(this)) {
            mPresenter.refreshMsg(mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
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

    private void refreshView() {
        if (mDiscussInfo == null) return;
        switch (mDiscussInfo.getState()) {
            case DiscussPresenter.STATE_DISCUSS_NOT_START:
                setTitle(mDiscussInfo.getGroupName() + "（未开启）");
                llInput.setVisibility(View.GONE);
                llRecord.setVisibility(View.GONE);
                tvCountDown.setVisibility(View.GONE);
                tvGoAnswer.setVisibility(View.GONE);
                btnOpen.setVisibility(mDiscussInfo.getIsLeader() == 1 ? View.VISIBLE : View.GONE);
                break;
            case DiscussPresenter.STATE_DISCUSSING:
                setTitle(mDiscussInfo.getGroupName() + "（进行中）");
                llInput.setVisibility(View.VISIBLE);
                llRecord.setVisibility(View.GONE);
                tvCountDown.setVisibility(View.VISIBLE);
                tvGoAnswer.setVisibility(View.VISIBLE);
                btnOpen.setVisibility(View.GONE);
                showDiscussing();
                break;
            case DiscussPresenter.STATE_DISCUSS_FINISHED:
                setTitle(mDiscussInfo.getGroupName() + "（已结束）");
                llInput.setVisibility(View.GONE);
                llRecord.setVisibility(View.GONE);
                tvCountDown.setVisibility(View.GONE);
                tvGoAnswer.setVisibility(View.GONE);
                btnOpen.setVisibility(View.GONE);
                break;
        }
    }

    @OnClick({R.id.btn_open, R.id.btn_record, R.id.btn_send, R.id.btn_input, R.id.tv_go_answer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                showDialog();
                break;
            case R.id.btn_record:
                llRecord.setVisibility(View.VISIBLE);
                llInput.setVisibility(View.GONE);
                break;
            case R.id.btn_send:
                //发送文字消息
                String content = etMsgText.getText().toString();
                mPresenter.sendMsg(null, mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId(), DiscussPresenter.MSG_TYPE_TEXT, content.length(), content);
                break;
            case R.id.btn_input:
                llRecord.setVisibility(View.GONE);
                llInput.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_go_answer:
                showQuestions();
                break;
        }
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

    @Override
    public void showDiscussNotStart() {
        setTitle(mDiscussInfo.getGroupName() + "（未开始)");
        llInput.setVisibility(View.GONE);
        llRecord.setVisibility(View.GONE);
        btnOpen.setVisibility(mDiscussInfo.getIsLeader() == 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showDiscussing() {
        mDiscussInfo.setState(DiscussPresenter.STATE_DISCUSSING);//更改状态为进行中
        setTitle(mDiscussInfo.getGroupName() + "（进行中)");
        if (llInput.getVisibility() == View.GONE &&
                llRecord.getVisibility() == View.GONE) {
            llInput.setVisibility(View.VISIBLE);
            llRecord.setVisibility(View.GONE);
        }
        btnOpen.setVisibility(View.GONE);
        tvGoAnswer.setVisibility(View.VISIBLE);

        showCountDown();
    }

    @Override
    public void showDiscussFinished() {
        setTitle(mDiscussInfo.getGroupName() + "（已结束)");
        llInput.setVisibility(View.GONE);
        llRecord.setVisibility(View.GONE);
        btnOpen.setVisibility(View.GONE);
    }

    public void showCountDown() {
        if (mDiscussInfo.getState() == DiscussPresenter.STATE_DISCUSSING && mCountDown == null) {
            tvCountDown.setVisibility(View.VISIBLE);
            tvCountDown.setText("还剩" + mDiscussInfo.getCountdownTime() + "分钟");
            mCountDown = new DiscussionCountDown(mDiscussInfo.getCountdownTime() * 60 * 1000, 1000);
            mCountDown.start();
        }
    }

    DiscussionCountDown mCountDown;

    private class DiscussionCountDown extends CountDownTimer {

        public DiscussionCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {
            long spanToNow = TimeUtils.getTimeSpanByNow(mDiscussInfo.getOpeningTime(), ConstUtils.TimeUnit.MIN);
            long timeLeft = mDiscussInfo.getCountdownTime() - spanToNow;
            if (timeLeft <= 0) {
                tvCountDown.setVisibility(View.GONE);
                if (mCountDown != null)
                    mCountDown.cancel();
            } else {
                tvCountDown.setText("还剩" + (mDiscussInfo.getCountdownTime() - spanToNow) + "分钟");
            }
        }

        @Override
        public void onFinish() {
            showDiscussFinished();
        }
    }

    private List<DiscussMsgListResp.DataBean.DatasBean> mDatas;
    private boolean isRefreshing = true;

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

    @Override
    public void showMsg(String msg) {
        if (msg != null) {
            showToast(msg);
        } else {
            showToast(R.string.server_error);
        }
    }

    @Override
    public void sendTextMsgSuccess() {
        etMsgText.setText("");
    }

    @Override
    public void sendVoiceMsgSuccess() {

    }

    //英文正则
    private String REGEX_ENG = "[\\x00-\\xff]+";

    @Override
    public void showSubjectInfo(DiscussSubjectResp resp) {
        tvSubjectTitle.setText("题目：" + resp.getData().getName());
        tvQuestion.setText("主题：" + resp.getData().getContent());
        if (resp.getData().getIsLanguage() == 0) {
            etMsgText.addTextChangedListener(new MsgTextWatcher());
        }
    }

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

    private class LongTouch implements View.OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    btnPressRecord.setText("松开结束");
                    startRecord();
                    break;
                case MotionEvent.ACTION_UP:
                    btnPressRecord.setText("长按录音");
                    stopRecord();
                    break;
            }
            return true;
        }
    }

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
            showMsg(audioPath.toString());
            mPresenter.sendMsg(new File(audioPath.getPath()), mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId(), DiscussPresenter.MSG_TYPE_VOICE, duration, "");
        }

        @Override
        public void onAudioDBChanged(int db) {

        }
    }

    private void showQuestions() {
        Intent intent = new Intent(this, DiscussQuestionActivity.class);
        intent.putExtra(DiscussQuestionActivity.DISCUSS_INFO, mDiscussInfo);
        if (mDataCache!=null){
            intent.putExtra("datas",mDataCache);
        }
        startActivityForResult(intent,100);
    }

    private void showDialog() {

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("温馨提示");
        builder.setMessage("开启讨论后开始进入倒计时" + mDiscussInfo.getCountdownTime() + "分钟，确定开启讨论？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //开启讨论
                if (mDiscussInfo != null && mPresenter != null) {
                    mPresenter.startDiscussion(mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId());
                    String content = "我已开启讨论";
                    mPresenter.sendMsg(null, mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId(), DiscussPresenter.MSG_TYPE_TEXT, content.length(), content);
                } else showMsg("开启失败");

                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==100){
            mDataCache = new ArrayList<>();
            mDataCache.clear();
            mDataCache = (ArrayList<DiscussAnswerCache>) data.getSerializableExtra("datas");
        }
    }
}

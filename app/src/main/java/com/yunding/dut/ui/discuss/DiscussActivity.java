package com.yunding.dut.ui.discuss;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunding.dut.R;
import com.yunding.dut.adapter.DiscussListMsgAdapter;
import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.model.resp.discuss.DiscussMsgListResp;
import com.yunding.dut.presenter.discuss.DiscussPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.TimeUtils;
import com.yunding.dut.view.DUTVerticalSmoothScrollRecycleView;

import java.util.ArrayList;
import java.util.Date;
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

    @BindView(R.id.tv_question)
    TextView tvQuestion;
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

    private DiscussListResp.DataBean mDiscussInfo;
    private DiscussListMsgAdapter mAdapter;

    private DiscussPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);
        ButterKnife.bind(this);

        mDiscussInfo = (DiscussListResp.DataBean) getIntent().getSerializableExtra(DISCUSS_SUBJECT_INFO);
        initViewState();

        mPresenter = new DiscussPresenter(this);
        if (mDiscussInfo != null)
            mPresenter.refreshMsg(mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId());
    }

    @Override
    protected void onDestroy() {
        mPresenter.stopRefreshMsg();
        isRefreshing = false;
        super.onDestroy();
    }

    private void initViewState() {
        if (mDiscussInfo != null) {
            //开启讨论按钮显示状态
            btnOpen.setVisibility((mDiscussInfo.getIsLeader() == 1 && mDiscussInfo.getState() == 0)
                    ? View.VISIBLE : View.GONE);

            //标题显示状态
            switch (mDiscussInfo.getState()) {
                case 0:
                    setTitle(mDiscussInfo.getGroupName() + "（未开启）");
                    llInput.setVisibility(View.GONE);
                    llRecord.setVisibility(View.GONE);
                    break;
                case 1:
                    setTitle(mDiscussInfo.getGroupName() + "（进行中）");
                    llInput.setVisibility(View.VISIBLE);
                    llRecord.setVisibility(View.GONE);
                    break;
                case 2:
                    setTitle(mDiscussInfo.getGroupName() + "（已结束）");
                    llInput.setVisibility(View.GONE);
                    llRecord.setVisibility(View.GONE);
                    break;
            }
        }
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

    @OnClick({R.id.tv_question, R.id.btn_open, R.id.btn_record, R.id.btn_send, R.id.btn_input, R.id.btn_press_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_question:
                //答题页面
                break;
            case R.id.btn_open:
                //开启讨论
                if (mDiscussInfo != null && mPresenter != null)
                    mPresenter.startDiscussion(mDiscussInfo.getThemeId(), mDiscussInfo.getGroupId());
                break;
            case R.id.btn_record:
                llRecord.setVisibility(View.VISIBLE);
                llInput.setVisibility(View.GONE);
                break;
            case R.id.btn_send:
                //发送文字消息
                break;
            case R.id.btn_input:
                llRecord.setVisibility(View.GONE);
                llInput.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_press_record:
                //录音
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
        setTitle(mDiscussInfo.getGroupName() + "（讨论未开始)");
        llInput.setVisibility(View.GONE);
        llRecord.setVisibility(View.GONE);
        btnOpen.setVisibility(mDiscussInfo.getIsLeader() == 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showDiscussing() {
        setTitle(mDiscussInfo.getGroupName() + "（讨论进行中)");
        llInput.setVisibility(View.VISIBLE);
        llRecord.setVisibility(View.GONE);
        btnOpen.setVisibility(View.GONE);
    }

    @Override
    public void showDiscussFinished() {
        setTitle(mDiscussInfo.getGroupName() + "（讨论已结束)");
        llInput.setVisibility(View.GONE);
        llRecord.setVisibility(View.GONE);
        btnOpen.setVisibility(View.GONE);
    }

    @Override
    public void showCountDown() {
        tvCountDown.setText("还剩"+TimeUtils.getTimeSpanByNow(new Date(mDiscussInfo.getEndTime()), ConstUtils.TimeUnit.MIN)+"分钟");
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        tvCountDown.setText("还剩"+TimeUtils.getTimeSpanByNow(new Date(mDiscussInfo.getEndTime()), ConstUtils.TimeUnit.MIN)+"分钟");
                    }
                });
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
            mAdapter = new DiscussListMsgAdapter(mDatas);
            rvMsgList.setAdapter(mAdapter);
        }
        if (mDatas.size() != resp.getData().getTotal()) {
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
}

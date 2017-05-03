package com.yunding.dut.ui.discuss;

import android.os.Bundle;
import android.support.v7.widget.LinearSnapHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.yunding.dut.R;
import com.yunding.dut.adapter.DiscussQuestionListAdapter;
import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.model.resp.discuss.DiscussQuestionListResp;
import com.yunding.dut.presenter.discuss.DiscussQuestionPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.view.DUTHorizontalRecyclerView;
import com.yunding.dut.view.DUTVerticalRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscussQuestionActivity extends ToolBarActivity implements IDiscussQuestionView {

    @BindView(R.id.rv_question_list)
    DUTVerticalRecyclerView rvQuestionList;

    public static String DISCUSS_INFO = "DISCUSS_INFO";
    private DiscussListResp.DataBean mResp;

    private DiscussQuestionPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss_question);
        ButterKnife.bind(this);

        mResp = (DiscussListResp.DataBean) getIntent().getSerializableExtra(DISCUSS_INFO);
        mPresenter = new DiscussQuestionPresenter(this);
        if (mResp != null) {
            mPresenter.getSubjectQuestions(mResp.getThemeId());
        }

//        LinearSnapHelper mLinearSnapHelper = new LinearSnapHelper();
//        mLinearSnapHelper.attachToRecyclerView(rvQuestionList);

        setTitle("答题");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_discuss_questions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_commit:
                if (mResp.getIsLeader() == 1) {
                    mAdapter.commitAnswer();
                }else{
                    showToast("只有组长可以提交答案");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    private DiscussQuestionListAdapter mAdapter;
    private List<DiscussQuestionListResp.DataBean> mData;

    @Override
    public void showQuestions(DiscussQuestionListResp resp) {
        mData = new ArrayList<>();
        mData.addAll(resp.getData());
        if (mAdapter == null) {
            mAdapter = new DiscussQuestionListAdapter(mData, this, mResp);
            rvQuestionList.setAdapter(mAdapter);
        }
        mData.clear();
        mData.addAll(resp.getData());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showMsg(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        } else {
            showToast(R.string.server_error);
        }
    }

    @Override
    public void commitAnswerSuccess() {
        showSnackBar("提交成功");
        finish();
    }

    public void commitAnswer(String jsonParam) {
        mPresenter.commitAnswer(jsonParam);
    }
}

package com.yunding.dut.ui.discuss;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearSnapHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.yunding.dut.R;
import com.yunding.dut.adapter.DiscussKeepQuestionAdapter;
import com.yunding.dut.adapter.DiscussQuestionListAdapter;
import com.yunding.dut.model.data.discuss.DiscussAnswerCache;
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
    private ArrayList<DiscussAnswerCache> mDataCache;
    private static final String TAG = "DiscussQuestionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss_question);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mResp = (DiscussListResp.DataBean)intent.getSerializableExtra(DISCUSS_INFO);
        if (intent.getParcelableArrayListExtra("datas")!=null){

            mDataCache = (ArrayList<DiscussAnswerCache>) intent.getSerializableExtra("datas");
        }
        mPresenter = new DiscussQuestionPresenter(this);
        if (mResp != null) {
            mPresenter.getSubjectQuestions(mResp.getThemeId(),mResp.getGroupId());
        }
        getmToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataCache==null){
                    mDataCache = (ArrayList<DiscussAnswerCache>) mAdapter.keepAnswer();

                }else {
                    mDataCache = (ArrayList<DiscussAnswerCache>) mDiscussKeepQuestionAdapter.keepAnswer();
                    Log.e(TAG, "onClick: "+mDataCache.size() );
                }
                Intent intent = new Intent();
                intent.putExtra("datas",mDataCache);
                setResult(100,intent);
                finish();
            }
        });
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
                    if (mDataCache==null){
                        mAdapter.commitAnswer();
                    }else {
                        mDiscussKeepQuestionAdapter.commitAnswer();
                    }

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
    private DiscussKeepQuestionAdapter mDiscussKeepQuestionAdapter;
    @Override
    public void showQuestions(DiscussQuestionListResp resp) {

        if (mDataCache!=null){
            mData = new ArrayList<>();
            mData.addAll(resp.getData());
            if (mDiscussKeepQuestionAdapter==null){
                mDiscussKeepQuestionAdapter = new DiscussKeepQuestionAdapter(mData,mDataCache,this,mResp);
                rvQuestionList.setAdapter(mDiscussKeepQuestionAdapter);
            }
            mData.clear();
            mData.addAll(resp.getData());
            mDiscussKeepQuestionAdapter.notifyDataSetChanged();
        }else {
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

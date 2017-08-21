package com.yunding.dut.ui.discuss;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.yunding.dut.view.DUTVerticalRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * 类 名 称：DiscussQuestionActivity
 * <P/>描    述：废弃
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 12:01
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 12:01
 * <P/>修改备注：
 * <P/>版    本：
 */
public class DiscussQuestionActivity extends ToolBarActivity implements IDiscussQuestionView {

    @BindView(R.id.rv_question_list)
    DUTVerticalRecyclerView rvQuestionList;
    public static String DISCUSS_INFO = "DISCUSS_INFO";
    private DiscussListResp.DataBean mResp;
    private ArrayList<DiscussAnswerCache> mDataCache;
    private DiscussQuestionPresenter mPresenter;

    private static final String TAG = "DiscussQuestionActivity";
    private String serverTime;

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
        mPresenter.getServerTime();
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

    @Override
    public void getServerTime(String time) {
        serverTime=time;
    }

    public void commitAnswer(String jsonParam) {
        mPresenter.commitAnswer(jsonParam);
    }
}

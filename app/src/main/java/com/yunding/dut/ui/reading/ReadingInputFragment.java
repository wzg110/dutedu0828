package com.yunding.dut.ui.reading;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yunding.dut.R;
import com.yunding.dut.adapter.DiscussQuestionInputAdapter;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.presenter.reading.ReadingPresenter;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.TimeUtils;
import com.yunding.dut.view.DUTVerticalRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yunding.dut.ui.reading.ReadingActivity.READING_INFO;
import static com.yunding.dut.ui.reading.ReadingActivity.READING_QUESTION;

/**
 * Created by Administrator on 2017/5/22.
 */

public class ReadingInputFragment extends BaseFragmentInReading implements IReadingQuestionView {

    @BindView(R.id.iv_finish_answer)
    ImageView mIvFinishAnswer;
    @BindView(R.id.btn_commit_answer_answer)
    Button mBtnCommitAnswerAnswer;
    @BindView(R.id.rv_input_list_answer)
    DUTVerticalRecyclerView mRvInputListAnswer;
    Unbinder unbinder;
    @BindView(R.id.tv_title_answer)
    TextView mTvTitleAnswer;
    @BindView(R.id.tv_question_answer)
    TextView mTvQuestionAnswer;
    private ReadingListResp.DataBean mReadingInfo;
    private ReadingListResp.DataBean.ExercisesBean mExerciseBean;

    private int mQuestionIndex;
    private DiscussQuestionInputAdapter mInputAdapter;
    private ReadingPresenter mPresenter;

    private long mStartTime;
    private int mGoOriginalTime = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_answer;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        //初始化参数
        mReadingInfo = (ReadingListResp.DataBean) getArguments().getSerializable(READING_INFO);
        if (getArguments().getSerializable(READING_QUESTION) instanceof ReadingListResp.DataBean.ExercisesBean) {
            mExerciseBean = (ReadingListResp.DataBean.ExercisesBean) getArguments().getSerializable(READING_QUESTION);
            mQuestionIndex = mReadingInfo.getExercises().indexOf(mExerciseBean);//题目编号
        }
        mPresenter = new ReadingPresenter(this);
        mStartTime = System.currentTimeMillis();

        //初始化UI
        if (mExerciseBean == null) return;
        mTvTitleAnswer.setText(mReadingInfo.getCourseTitle());
        mTvQuestionAnswer.setText(mReadingInfo.getCourseContent());

        //初始化输入框
        String[] rightAnswerArray = mExerciseBean.getRightAnswer().split(",");
        List<String> inputList = new ArrayList<>();
        mInputAdapter = new DiscussQuestionInputAdapter(inputList, mExerciseBean.getQuestionCompleted());
        mRvInputListAnswer.setAdapter(mInputAdapter);

        if (mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED) {
            //已完成的直接显示答案
            String[] answerContent = new Gson().fromJson(mExerciseBean.getAnswerContent(), String[].class);
            if (answerContent == null) return;
            for (String answer : answerContent) {
                inputList.add(answer);
            }
        } else {
            //未完成的直接显示空
            for (String answer : rightAnswerArray) {
                inputList.add("");
            }
        }
        mInputAdapter.setNewData(inputList);


    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            showToast(R.string.server_error);
        } else {
            showToast(msg);
        }
    }

    @Override
    public void commitSuccess() {
        mExerciseBean.setQuestionCompleted(ReadingActivity.STATE_FINISHED);
        EventBus.getDefault().post(mReadingInfo);
    }

    @Override
    public void showRightAnswer() {

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
    }

    @OnClick({R.id.iv_finish_answer, R.id.btn_commit_answer_answer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish_answer:
                removeFragment();
                break;
            case R.id.btn_commit_answer_answer:
                commitAnswer();
                break;
        }
    }
     /**
     * 功能简述:提交答案
     */
    private void commitAnswer() {
        List<String> answerList = mInputAdapter.getData();
        String answerTemp = new Gson().toJson(answerList);

        long commitTime = System.currentTimeMillis();
        long timeSpan = TimeUtils.getTimeSpan(commitTime, mStartTime, ConstUtils.TimeUnit.MIN);

        if (timeSpan == 0) {
            timeSpan = 1;
        }

        if (mExerciseBean != null) {
            mReadingInfo.getExercises()
                    .get(mReadingInfo.getExercises().indexOf(mExerciseBean))
                    .setAnswerContent(answerTemp);
            mPresenter.commitAnswer(mExerciseBean.getQuestionId(), answerTemp, timeSpan, mGoOriginalTime);
        }
    }

}

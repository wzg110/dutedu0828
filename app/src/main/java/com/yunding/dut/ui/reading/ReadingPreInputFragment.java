package com.yunding.dut.ui.reading;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/**
 * Created by Administrator on 2017/5/22.
 */

public class ReadingPreInputFragment extends BaseFragmentInReading implements IReadingQuestionView {

    @BindView(R.id.iv_finish_answer)
    ImageView mIvFinishAnswer;
    @BindView(R.id.rv_input_list_answer)
    DUTVerticalRecyclerView mRvInputListAnswer;
    Unbinder unbinder;
    @BindView(R.id.tv_title_answer)
    TextView mTvTitleAnswer;
    @BindView(R.id.tv_question_answer)
    TextView mTvQuestionAnswer;
    @BindView(R.id.btn_commit_answer_answer)
    Button mBtnCommitAnswerAnswer;
    @BindView(R.id.btn_next_answer)
    Button mBtnNextAnswer;
    @BindView(R.id.tv_right_answer)
    TextView mTvRightAnswer;
    @BindView(R.id.tv_toast)
    TextView mTvToast;
    @BindView(R.id.layout_toast)
    LinearLayout mLayoutToast;
    @BindView(R.id.btn_go_original)
    Button mBtnGoOriginal;

    private ReadingListResp.DataBean mReadingInfo;
    private ReadingListResp.DataBean.PreClassExercisesBean mPreExerciseBean;

    private int mQuestionIndex;
    private DiscussQuestionInputAdapter mInputAdapter;
    private ReadingPresenter mPresenter;

    private long mStartTime;
    private int mGoOriginalTime = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_input_new;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        //初始化参数
        mReadingInfo = (ReadingListResp.DataBean) getArguments().getSerializable(ReadingActivity.READING_INFO);
        if (getArguments().getSerializable(ReadingActivity.READING_QUESTION) instanceof ReadingListResp.DataBean.PreClassExercisesBean) {
            mPreExerciseBean = (ReadingListResp.DataBean.PreClassExercisesBean) getArguments().getSerializable(ReadingActivity.READING_QUESTION);
            mQuestionIndex = mReadingInfo.getPreClassExercises().indexOf(mPreExerciseBean);//题目编号
        }
        mPresenter = new ReadingPresenter(this);
        mStartTime = System.currentTimeMillis();

        //初始化UI
        if (mPreExerciseBean == null) return;
        if (mPreExerciseBean.getQuestionType() == ReadingActivity.TYPE_CHOICE) {
            mTvTitleAnswer.setText("课前小题第" + (mQuestionIndex + 1) + "题" + "（共" + mReadingInfo.getPreClassExercises().size() + "题）");
        } else {
            mTvTitleAnswer.setText("课前小题第" + (mQuestionIndex + 1) + "题" + "（共" + mReadingInfo.getPreClassExercises().size() + "题）");
        }
        mTvQuestionAnswer.setText(mPreExerciseBean.getQuestionContent());

        //初始化输入框
        String[] rightAnswerArray = mPreExerciseBean.getRightAnswer().split(",");
        List<String> inputList = new ArrayList<>();
        mInputAdapter = new DiscussQuestionInputAdapter(inputList, mPreExerciseBean.getQuestionCompleted());
        mRvInputListAnswer.setAdapter(mInputAdapter);

        if (mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED) {
            //已完成的直接显示答案
            String[] answerContent = new Gson().fromJson(mPreExerciseBean.getAnswerContent(), String[].class);
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
        if (getArguments().getStringArrayList("answer") != null) {
            List<String> list = getArguments().getStringArrayList("answer");
            inputList.clear();
            for (int i = 0; i < list.size(); i++) {
                inputList.add(list.get(i));
            }
        }

        mInputAdapter.setNewData(inputList);
        //初始化按钮状态
        mBtnNextAnswer.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
        mBtnCommitAnswerAnswer.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);
        mBtnGoOriginal.setVisibility(view.GONE);
        mLayoutToast.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
        String answer =  mPreExerciseBean.getRightAnswer().substring( mPreExerciseBean.getRightAnswer().indexOf("[")+1, mPreExerciseBean.getRightAnswer().indexOf("]"));
        String[] sourceStrArray = answer.split(",");
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i = 0; i < sourceStrArray.length; i++) {
            int a = i + 1;
            stringBuffer.append(a + ": " + sourceStrArray[i] + "\n");
        }
        mTvRightAnswer.setText(stringBuffer.toString());
        mTvToast.setText(mPreExerciseBean.getAnalysis());

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
        mInputAdapter.setState(1);
        mBtnNextAnswer.setVisibility(View.VISIBLE);
        mBtnCommitAnswerAnswer.setVisibility(View.GONE);
        mLayoutToast.setVisibility(View.VISIBLE);
        mPreExerciseBean.setQuestionCompleted(ReadingActivity.STATE_FINISHED);
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

    @OnClick({R.id.iv_finish_answer, R.id.btn_commit_answer_answer, R.id.btn_next_answer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_finish_answer:
                removeFragment();
                break;
            case R.id.btn_commit_answer_answer:
                commitAnswer();
                break;
            case R.id.btn_next_answer:
                removeFragment();
                goNext();
                break;
        }
    }

    private void commitAnswer() {
        List<String> answerList = mInputAdapter.getData();
        String answerTemp = new Gson().toJson(answerList);

        long commitTime = System.currentTimeMillis();
        long timeSpan = TimeUtils.getTimeSpan(commitTime, mStartTime, ConstUtils.TimeUnit.MIN);

        if (timeSpan == 0) {
            timeSpan = 1;
        }

        if (mPreExerciseBean != null) {
            mReadingInfo.getPreClassExercises()
                    .get(mReadingInfo.getPreClassExercises().indexOf(mPreExerciseBean))
                    .setAnswerContent(answerTemp);
            mPresenter.commitAnswer(mPreExerciseBean.getQuestionId(), answerTemp, timeSpan, 0);
        }
    }

    private void goNext() {
        if (mReadingInfo.getPreClassExercises().size() > (mQuestionIndex + 1)) {
            //还有课前小题
            ReadingListResp.DataBean.PreClassExercisesBean bean = mReadingInfo.getPreClassExercises().get(mQuestionIndex + 1);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ReadingActivity.READING_INFO, mReadingInfo);
            bundle.putSerializable(ReadingActivity.READING_QUESTION, bean);

            switch (bean.getQuestionType()) {
                case ReadingActivity.TYPE_CHOICE:
                    //选择题
                    ReadingPreClassChoiceQuestionFragment choiceQuestionFragment = new ReadingPreClassChoiceQuestionFragment();
                    choiceQuestionFragment.setArguments(bundle);
                    addFragment(choiceQuestionFragment);
                    break;
                case ReadingActivity.TYPE_INPUT:
                    //填空题
                    ReadingPreClassInputQuestionFragment inputQuestionFragment = new ReadingPreClassInputQuestionFragment();
                    inputQuestionFragment.setArguments(bundle);
                    addFragment(inputQuestionFragment);
                    break;
                default:
                    showSnackBar("没有该题型，请反馈客服");
                    break;
            }
        } else {
            //课前小题已经答完，进入阅读页面
            Bundle bundle = new Bundle();
            bundle.putSerializable(ReadingActivity.READING_INFO, mReadingInfo);
            ReadingArticleFragment originalFragment = new ReadingArticleFragment();
            originalFragment.setArguments(bundle);
            addFragment(originalFragment);
        }
    }

    @Override
    public void onPause() {
        super.onPause();


    }
}

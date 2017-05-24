package com.yunding.dut.ui.reading;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
    @BindView(R.id.btn_next_answer)
    Button mBtnNextAnswer;
    @BindView(R.id.tv_right_answer)
    TextView mTvRightAnswer;
    @BindView(R.id.tv_toast)
    TextView mTvToast;
    @BindView(R.id.layout_toast)
    LinearLayout mLayoutToast;
    private ReadingListResp.DataBean mReadingInfo;
    private ReadingListResp.DataBean.ExercisesBean mExerciseBean;

    private int mQuestionIndex;
    private DiscussQuestionInputAdapter mInputAdapter;
    private ReadingPresenter mPresenter;

    private long mStartTime;
    private int mGoOriginalTime = 0;
    private static final String TAG = "ReadingInputFragment";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_input_new;
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

        //初始化按钮状态
        mBtnNextAnswer.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
        mBtnCommitAnswerAnswer.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);

        //初始化提示
        mLayoutToast.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
        mTvRightAnswer.setText(mExerciseBean.getRightAnswer());
        mTvToast.setText(mExerciseBean.getAnalysis());
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
        mBtnNextAnswer.setVisibility(View.VISIBLE);
        mBtnCommitAnswerAnswer.setVisibility(View.GONE);
        mLayoutToast.setVisibility(View.VISIBLE);
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
        mBtnNextAnswer.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
        mBtnCommitAnswerAnswer.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);
    }

    /**
     * 功能简述:下一步
     */
    private void goNext() {
        if (mReadingInfo.getExercises().size() > (mQuestionIndex + 1)) {
            //还有课后小题
            ReadingListResp.DataBean.ExercisesBean bean = mReadingInfo.getExercises().get(mQuestionIndex + 1);
            Bundle bundle = new Bundle();
            bundle.putSerializable(READING_INFO, mReadingInfo);
            bundle.putSerializable(READING_QUESTION, bean);
            Log.e(TAG, "goNext: ");
            switch (bean.getQuestionType()) {
                case ReadingActivity.TYPE_CHOICE:
                    //选择题
                    Log.e(TAG, "goNext: 1");
                    ReadingChoiceQuestionFragment choiceQuestionFragment = new ReadingChoiceQuestionFragment();
                    choiceQuestionFragment.setArguments(bundle);
                    addFragment(choiceQuestionFragment);
                    break;
                case ReadingActivity.TYPE_INPUT:
                    //填空题
                    Log.e(TAG, "goNext: 2");
                    ReadingInputQuestionFragment inputQuestionFragment = new ReadingInputQuestionFragment();
                    inputQuestionFragment.setArguments(bundle);
                    addFragment(inputQuestionFragment);
                    break;
                default:
                    showSnackBar("没有该题型，请反馈客服");
                    break;
            }
        } else {
            //课后小题已经答完，结束
            new MaterialDialog.Builder(getHoldingActivity()).title("恭喜").content("您已完成本次阅读，是否离开？")
                    .positiveText("是")
                    .negativeText("否")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            getHoldingActivity().finish();
                        }
                    })
                    .show();
        }
    }
}

package com.yunding.dut.ui.reading;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yunding.dut.R;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.presenter.reading.ReadingPresenter;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.TimeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 类 名 称：ReadingChoiceQuestionFragment
 * <P/>描    述：阅读课前选择题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/24 18:50
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/24 18:50
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public class ReadingPreClassChoiceQuestionFragment extends BaseFragmentInReading implements IReadingQuestionView {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.rg_options)
    RadioGroup rgOptions;
    @BindView(R.id.btn_go_original)
    Button btnGoOriginal;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_right_answer)
    TextView tvRightAnswer;
    @BindView(R.id.tv_toast)
    TextView tvToast;
    @BindView(R.id.layout_toast)
    LinearLayout layoutToast;
    Unbinder unbinder;

    private ReadingListResp.DataBean mReadingInfo;
    private ReadingListResp.DataBean.PreClassExercisesBean mPreExerciseBean;

    private int mQuestionIndex;

    private ReadingPresenter mPresenter;

    private long mStartTime;
    private int mGoOriginalTime = 0;

    private ReadingListResp.DataBean.PreClassExercisesBean.OptionsBeanX mSelectedOption;

    public ReadingPreClassChoiceQuestionFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reading_pre_class_choice_question;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        //初始化各种参数
        mReadingInfo = (ReadingListResp.DataBean) getArguments().getSerializable(ReadingActivity.READING_INFO);
        if (getArguments().getSerializable(ReadingActivity.READING_QUESTION) instanceof ReadingListResp.DataBean.PreClassExercisesBean) {
            mPreExerciseBean = (ReadingListResp.DataBean.PreClassExercisesBean) getArguments().getSerializable(ReadingActivity.READING_QUESTION);
            mQuestionIndex = mReadingInfo.getPreClassExercises().indexOf(mPreExerciseBean);//题目编号
        }
        mPresenter = new ReadingPresenter(this);
        mStartTime = System.currentTimeMillis();

        initUI();
    }

    private void initUI() {
        //初始化UI
        if (mPreExerciseBean == null) return;

        //初始化题号，题目内容
        if (mPreExerciseBean.getQuestionType() == ReadingActivity.TYPE_CHOICE) {
            tvTitle.setText("第" + (mQuestionIndex + 1) + "题" + "（选择题）");
        } else {
            tvTitle.setText("第" + (mQuestionIndex + 1) + "题" + "（填空题）");
        }
        tvQuestion.setText(mPreExerciseBean.getQuestionContent());

        //初始化选择框
        if (mPreExerciseBean.getOptions() == null) return;
        if (mPreExerciseBean.getOptions().size() == 0) return;
        for (ReadingListResp.DataBean.PreClassExercisesBean.OptionsBeanX option : mPreExerciseBean.getOptions()) {
            RadioButton rbChoice = new RadioButton(getHoldingActivity());
            rbChoice.setText(option.getOptionIndex() + "." + option.getOptionContent());
            if (mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED) {
                rbChoice.setEnabled(false);
                if (TextUtils.equals(mPreExerciseBean.getAnswerContent(), option.getOptionIndex())) {
                    rbChoice.setChecked(true);
                }
            }
            rgOptions.addView(rbChoice, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        rgOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton selected = (RadioButton) radioGroup.findViewById(i);
                String selectedString = selected.getText().toString();
                for (ReadingListResp.DataBean.PreClassExercisesBean.OptionsBeanX selectedOption : mPreExerciseBean.getOptions()) {
                    if (selectedString.startsWith(selectedOption.getOptionIndex())) {
                        mSelectedOption = selectedOption;
                    }
                }
            }
        });

        //初始化按钮状态
        btnNext.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
        btnCommit.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);

        //初始化提示
        layoutToast.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
        tvRightAnswer.setText(mPreExerciseBean.getRightAnswer());
        tvToast.setText(mPreExerciseBean.getAnalysis());

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

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
        mPreExerciseBean.setQuestionCompleted(ReadingActivity.STATE_FINISHED);
        btnCommit.setVisibility(View.GONE);
        btnNext.setVisibility(View.VISIBLE);
        layoutToast.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRightAnswer() {

    }

    @OnClick({R.id.btn_go_original, R.id.btn_commit, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_go_original:
                break;
            case R.id.btn_commit:
                commitAnswer();
                break;
            case R.id.btn_next:
                goNext();
                break;
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
            ReadingOriginalFragment originalFragment = new ReadingOriginalFragment();
            originalFragment.setArguments(bundle);
            addFragment(originalFragment);
        }
    }

    /**
     * 功能简述:提交答案
     */
    private void commitAnswer() {
        long commitTime = System.currentTimeMillis();
        long timeSpan = TimeUtils.getTimeSpan(commitTime, mStartTime, ConstUtils.TimeUnit.MIN);
        if (timeSpan == 0) {
            timeSpan = 1;
        }

        if (mPreExerciseBean != null && mSelectedOption != null) {
            mPresenter.commitAnswer(mPreExerciseBean.getQuestionId(), mSelectedOption.getOptionIndex(), timeSpan, mGoOriginalTime);
        } else {
            showToast("请选择答案");
        }
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
}

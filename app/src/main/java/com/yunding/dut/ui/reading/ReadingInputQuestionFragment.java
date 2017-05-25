package com.yunding.dut.ui.reading;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yunding.dut.ui.reading.ReadingActivity.READING_INFO;
import static com.yunding.dut.ui.reading.ReadingActivity.READING_QUESTION;

/**
 * 类 名 称：ReadingInputQuestionFragment
 * <P/>描    述：阅读填空题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/24 18:50
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/24 18:50
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public class ReadingInputQuestionFragment extends BaseFragmentInReading implements IReadingQuestionView {

    @BindView(R.id.ll_question)
    LinearLayout llQuestion;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
//    @BindView(R.id.rv_input_list)
//    DUTVerticalRecyclerView rvInputList;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.btn_go_original)
    Button btnGoOriginal;
    @BindView(R.id.tv_right_answer)
    TextView tvRightAnswer;
    @BindView(R.id.tv_toast)
    TextView tvToast;
    @BindView(R.id.layout_toast)
    LinearLayout layoutToast;
    Unbinder unbinder;
    @BindView(R.id.iv_to_answer)
    ImageView mIvToAnswer;
    private static final String TAG = "ReadingInputQuestionFra";
    private ReadingListResp.DataBean mReadingInfo;
    private ReadingListResp.DataBean.ExercisesBean mExerciseBean;

    private int mQuestionIndex;
    private DiscussQuestionInputAdapter mInputAdapter;

    private ReadingPresenter mPresenter;

    private long mStartTime;
    private int mGoOriginalTime = 0;
    private ReadingInputFragment mReadingInputFragment;
    private Bundle mBundle;

    public ReadingInputQuestionFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reading_input_question;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
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
        if (mExerciseBean.getQuestionType() == ReadingActivity.TYPE_CHOICE) {
            tvTitle.setText("课后小题第" + (1 + mQuestionIndex) + "题" + "（共" + mReadingInfo.getExercises().size() + "题）");
        } else {
            tvTitle.setText("课后小题第" + (1 + mQuestionIndex) + "题" + "（共" + mReadingInfo.getExercises().size() + "题）");
        }
        tvQuestion.setText(mExerciseBean.getQuestionContent());

//        //初始化输入框
//        String[] rightAnswerArray = mExerciseBean.getRightAnswer().split(",");
//        List<String> inputList = new ArrayList<>();
//        mInputAdapter = new DiscussQuestionInputAdapter(inputList, mExerciseBean.getQuestionCompleted());
//        rvInputList.setAdapter(mInputAdapter);
//
//        if (mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED) {
//            //已完成的直接显示答案
//            String[] answerContent = new Gson().fromJson(mExerciseBean.getAnswerContent(), String[].class);
//            if (answerContent == null) return;
//            for (String answer : answerContent) {
//                inputList.add(answer);
//            }
//        } else {
//            //未完成的直接显示空
//            for (String answer : rightAnswerArray) {
//                inputList.add("");
//            }
//        }
//        mInputAdapter.setNewData(inputList);

        //初始化按钮状态
        btnNext.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
        btnCommit.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);
        btnGoOriginal.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);

        //初始化提示
        layoutToast.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);


        String answer =  mExerciseBean.getRightAnswer().substring( mExerciseBean.getRightAnswer().indexOf("[")+1, mExerciseBean.getRightAnswer().indexOf("]"));
        String[] sourceStrArray = answer.split(",");
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i = 0; i < sourceStrArray.length; i++) {
            int a = i+1;
            stringBuffer.append(a+": "+sourceStrArray[i]+"\n");
        }
        tvRightAnswer.setText(stringBuffer.toString());
        tvToast.setText(mExerciseBean.getAnalysis());
    }

    @OnClick({R.id.btn_commit, R.id.btn_next, R.id.btn_go_original,R.id.iv_to_answer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
//                commitAnswer();
                mReadingInputFragment = new ReadingInputFragment();
                mBundle = new Bundle();
                mBundle.putSerializable(READING_INFO, mReadingInfo);
                mBundle.putSerializable(READING_QUESTION, mExerciseBean);
                mReadingInputFragment.setArguments(mBundle);
                addFragment(mReadingInputFragment);
                break;
            case R.id.btn_next:
                goNext();
                break;
            case R.id.btn_go_original:
                mGoOriginalTime++;
                goOriginal();
                break;
            case R.id.iv_to_answer:
                mReadingInputFragment = new ReadingInputFragment();
                mBundle = new Bundle();
                mBundle.putSerializable(READING_INFO, mReadingInfo);
                mBundle.putSerializable(READING_QUESTION, mExerciseBean);
                mReadingInputFragment.setArguments(mBundle);
                addFragment(mReadingInputFragment);
                break;
        }
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
        btnGoOriginal.setVisibility(View.GONE);
        btnCommit.setVisibility(View.GONE);
        btnNext.setVisibility(View.VISIBLE);
        layoutToast.setVisibility(View.VISIBLE);
        mInputAdapter.setState(1);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAnswer(ReadingListResp.DataBean dataBean){
        btnGoOriginal.setVisibility(View.GONE);
        btnCommit.setVisibility(View.GONE);
        btnNext.setVisibility(View.VISIBLE);
        layoutToast.setVisibility(View.VISIBLE);
        //初始化输入框
        mExerciseBean = dataBean.getExercises().get(mQuestionIndex);
        String[] rightAnswerArray = mExerciseBean.getRightAnswer().split(",");
        List<String> inputList = new ArrayList<>();
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
    public void showRightAnswer() {

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

    /**
     * 功能简述:跳转到阅读原文页面
     */
    private void goOriginal() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getHoldingActivity());
        builder.title(mReadingInfo.getCourseTitle());
        builder.content(mReadingInfo.getCourseContent());
        builder.positiveText("确定");
        builder.show();
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

            switch (bean.getQuestionType()) {
                case ReadingActivity.TYPE_CHOICE:
                    //选择题
                    ReadingChoiceQuestionFragment choiceQuestionFragment = new ReadingChoiceQuestionFragment();
                    choiceQuestionFragment.setArguments(bundle);
                    addFragment(choiceQuestionFragment);
                    break;
                case ReadingActivity.TYPE_INPUT:
                    //填空题
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}

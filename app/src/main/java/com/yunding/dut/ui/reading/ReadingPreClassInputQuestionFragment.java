package com.yunding.dut.ui.reading;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yunding.dut.ui.reading.ReadingActivity.READING_INFO;

/**
 * 类 名 称：ReadingInputQuestionFragment
 * <P/>描    述：阅读课前填空题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/24 18:50
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/24 18:50
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public class ReadingPreClassInputQuestionFragment extends BaseFragmentInReading implements IReadingQuestionView {

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
    @BindView(R.id.tv_right_answer)
    TextView tvRightAnswer;
    @BindView(R.id.tv_toast)
    TextView tvToast;
    @BindView(R.id.layout_toast)
    ScrollView layoutToast;
    Unbinder unbinder;
    @BindView(R.id.iv_to_answer_pre)
    ImageView mIvToAnswerPre;
    private static final String TAG = "ReadingPreClassInputQue";
    private ReadingListResp.DataBean mReadingInfo;
    private ReadingListResp.DataBean.PreClassExercisesBean mPreExerciseBean;

    private int mQuestionIndex;
    private DiscussQuestionInputAdapter mInputAdapter;

    private ReadingPresenter mPresenter;

    private long mStartTime;

    private List<String> mInputList;
    private Bundle mBundle;
    private ReadingPreInputFragment mReadingPreInputFragment;
    private ReadingListResp.DataBean.PreClassExercisesBean mBean;

    public ReadingPreClassInputQuestionFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reading_pre_class_input_question;
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
            tvTitle.setText("课前小题第" + (mQuestionIndex + 1) + "题" + "（共" + mReadingInfo.getPreClassExercises().size() + "题）");
        } else {
            tvTitle.setText("课前小题第" + (mQuestionIndex + 1) + "题" + "（共" + mReadingInfo.getPreClassExercises().size() + "题）");
        }
        tvQuestion.setText(mPreExerciseBean.getQuestionContent());

//        //初始化输入框
//        String[] rightAnswerArray = mPreExerciseBean.getRightAnswer().split(",");
//        mInputList = new ArrayList<>();
//        mInputAdapter = new DiscussQuestionInputAdapter(mInputList, mPreExerciseBean.getQuestionCompleted());
////        rvInputList.setAdapter(mInputAdapter);
//
//        if (mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED) {
//            //已完成的直接显示答案
//            String[] answerContent = new Gson().fromJson(mPreExerciseBean.getAnswerContent(), String[].class);
//            if (answerContent == null) return;
//            for (String answer : answerContent) {
//                mInputList.add(answer);
//            }
//        } else {
//            //未完成的直接显示空
//            for (String answer : rightAnswerArray) {
//                mInputList.add("");
//            }
//        }
//
//        mInputAdapter.setNewData(mInputList);

        //初始化按钮状态
        btnNext.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
        btnCommit.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);

        //初始化提示
        layoutToast.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
        String answer =  mPreExerciseBean.getRightAnswer().substring( mPreExerciseBean.getRightAnswer().indexOf("[")+1, mPreExerciseBean.getRightAnswer().indexOf("]"));
        String[] sourceStrArray = answer.split(",");
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i = 0; i < sourceStrArray.length; i++) {
            int a = i+1;
            stringBuffer.append(a+": "+sourceStrArray[i]+"\n");
        }
        tvRightAnswer.setText(stringBuffer.toString());
        tvToast.setText(mPreExerciseBean.getAnalysis());
    }

    @OnClick({R.id.btn_commit, R.id.btn_next,R.id.iv_to_answer_pre})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
//                commitAnswer();
                mBean = mReadingInfo.getPreClassExercises().get(mQuestionIndex);
                mBundle = new Bundle();
                mBundle.putSerializable(ReadingActivity.READING_INFO, mReadingInfo);
                mBundle.putSerializable(ReadingActivity.READING_QUESTION, mBean);
                mReadingPreInputFragment = new ReadingPreInputFragment();
                mReadingPreInputFragment.setArguments(mBundle);
                addFragment(mReadingPreInputFragment);
                break;
            case R.id.btn_next:
                goNext();
                break;
            case R.id.iv_to_answer_pre:
                mBean = mReadingInfo.getPreClassExercises().get(mQuestionIndex);
                mBundle = new Bundle();
                mBundle.putSerializable(ReadingActivity.READING_INFO, mReadingInfo);
                mBundle.putSerializable(ReadingActivity.READING_QUESTION, mBean);
                mReadingPreInputFragment = new ReadingPreInputFragment();
                mReadingPreInputFragment.setArguments(mBundle);
                addFragment(mReadingPreInputFragment);
                Log.e(TAG, "onViewClicked: "+ mReadingPreInputFragment);
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
        mInputAdapter.setState(1);
        mPreExerciseBean.setQuestionCompleted(ReadingActivity.STATE_FINISHED);
        btnCommit.setVisibility(View.GONE);
        btnNext.setVisibility(View.VISIBLE);
        layoutToast.setVisibility(View.VISIBLE);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAnswer(ReadingListResp.DataBean dataBean){
        btnCommit.setVisibility(View.GONE);
        btnNext.setVisibility(View.VISIBLE);
        layoutToast.setVisibility(View.VISIBLE);
        mPreExerciseBean = dataBean.getPreClassExercises().get(mQuestionIndex);
        //初始化输入框
        String[] rightAnswerArray = mPreExerciseBean.getRightAnswer().split(",");
        List<String> inputList = new ArrayList<>();
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

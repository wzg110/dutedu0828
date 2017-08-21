package com.yunding.dut.ui.reading;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.reading.ReadingDataResp;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.presenter.reading.ReadingPresenter;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.FontsUtils;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/6/28.
 */

public class ReadingTranslateQuestionFragment extends BaseFragmentInReading implements IReadingQuestionView {

    Unbinder unbinder;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.et_answer)
    EditText etAnswer;
    @BindView(R.id.ll_question)
    LinearLayout llQuestion;
    @BindView(R.id.tv_right_answer)
    TextView tvRightAnswer;
    @BindView(R.id.tv_toast)
    TextView tvToast;
    @BindView(R.id.layout_toast)
    ScrollView layoutToast;

    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.iv_go_back_reading)
    ImageView ivGoBackReading;
    @BindView(R.id.iv_go_answer_where)
    ImageView ivGoAnswerWhere;

    private ReadingListResp.DataBean.ExercisesBean mExerciseBean;//课后题bean
    private ReadingListResp.DataBean mReadingInfo;//文章全部数据的bean
    private List<ReadingListResp.DataBean.ExercisesBean.EducationAnswerFrom> mEducationAnswerFrom;
    private String answerShowTime; //0答完即显示  1 阅读课完事显示
    private int mQuestionIndex;//题号
    private long mStartTime;//答题开始时间
    private int mGoOriginalTime = 0; //统计返回原文次数
    private ReadingPresenter mPresenter;
    private ReadingActivity ra;
    private  int questionQuantity;
    private Dialog dialog;
    private  View view;

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
        mExerciseBean.setQuestionCompleted(ReadingActivity.STATE_FINISHED);
        etAnswer.setEnabled(false);
        etAnswer.setHint("");

        if (mQuestionIndex+1==mReadingInfo.getExercises().size()){
//            后面没有题了
//             TODO: 2017/7/28 三期
            mPresenter.showReadingData(mReadingInfo.getClassId(),mReadingInfo.getCourseId());
            // TODO: 2017/7/28 三期
            btnNext.setText("结束课程");
        }else{
            btnNext.setText("下一题");
        }
        btnCommit.setVisibility(View.GONE);
        ivGoBackReading.setVisibility(View.GONE);
        ivGoAnswerWhere.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.VISIBLE);

        if (answerShowTime.equals("0")){
            layoutToast.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showRightAnswer() {

    }

    @Override
    public void showReadingDataSuccess(ReadingDataResp.DataBean resp) {
        //获取数据显示
        showReadingDataDialog(resp);


    }

    public void showReadingDataDialog(ReadingDataResp.DataBean resp) {
        dialog=new Dialog(getHoldingActivity(),R.style.ActionSheetDialogStyle);
        view= LayoutInflater.from(getHoldingActivity()).inflate(R.layout.rlayout_reading_data_show,null);
        TextView tv_words_number= (TextView) view.findViewById(R.id.tv_words_number);
        TextView tv_reading_time= (TextView) view.findViewById(R.id.tv_reading_time);
        TextView tv_reading_speed= (TextView) view.findViewById(R.id.tv_reading_speed);
        TextView tv_collection_number= (TextView) view.findViewById(R.id.tv_collection_number);
        TextView tv_notes_number11= (TextView) view.findViewById(R.id.tv_notes_number111);
        ImageView rl_exit= (ImageView) view.findViewById(R.id.rl_exit);
        tv_words_number.setText(resp.getWordsQuantity()+"个");
        tv_reading_time.setText(resp.getTimeConsumedDesc());
        tv_reading_speed.setText(resp.getReadingSpeed());
        tv_collection_number.setText(resp.getWordsCollected()+"");
        tv_notes_number11.setText(resp.getNotesQuantity()+"");
        rl_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.show();//显示对话框

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_translate_question;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        ra = (ReadingActivity) getActivity();
        ra.setToolbarTitle("课后小题");
        ra.setToolbarVisiable(true);
        ra.setToolbarBGC(getResources().getColor(R.color.bg_white));
        ra.setToolbarTitleColor(getResources().getColor(R.color.title));
        mReadingInfo = (ReadingListResp.DataBean) getArguments().getSerializable(ReadingActivity.READING_INFO);
        if (getArguments().getSerializable(ReadingActivity.READING_QUESTION) instanceof ReadingListResp.DataBean.ExercisesBean) {
            mExerciseBean = (ReadingListResp.DataBean.ExercisesBean) getArguments().getSerializable(ReadingActivity.READING_QUESTION);
            mQuestionIndex = mReadingInfo.getExercises().indexOf(mExerciseBean);//题目编号
            questionQuantity=mReadingInfo.getExercises().size();
        }
        // TODO: 2017/7/28 三期
        if (mReadingInfo.getCompleted()==1||mReadingInfo.getCompleted()==2){
            ra.getToolbarExit().setVisibility(View.VISIBLE);
            ra.getToolbarExit().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.showReadingData(mReadingInfo.getClassId(),mReadingInfo.getCourseId());
                }
            });
        }else{
            ra.getToolbarExit().setVisibility(View.GONE);
        }
        // TODO: 2017/7/28 三期
        answerShowTime=mReadingInfo.getAnswerShowTime();
        mPresenter = new ReadingPresenter(this);
        mStartTime = System.currentTimeMillis();

        //答案出处list
        mEducationAnswerFrom = mExerciseBean.getEducationAnswerFroms();
        //初始化UI
        if (mExerciseBean == null) return;
        if (mExerciseBean.getQuestionType() == ReadingActivity.TYPE_MULTI_CHOICE) {
            tvTitle.setText("翻译题" + (mQuestionIndex + 1)+"/"+questionQuantity);
        } else {
            tvTitle.setText("翻译题" + (mQuestionIndex + 1)+"/"+questionQuantity);
        }
        if (FontsUtils.isContainChinese(mExerciseBean.getQuestionContent())
                ||TextUtils.isEmpty(mExerciseBean.getQuestionContent())){

        }else{
            tvQuestion.setTypeface(DUTApplication.getHsbwTypeFace());
        }
        tvQuestion.setText(mExerciseBean.getQuestionContent());

        if (mReadingInfo.getExercises().size() > (mQuestionIndex + 1)){
//            后面没有题了
            btnNext.setText("下一题");
        }else{

            btnNext.setText("结束课程");
        }

        if (mReadingInfo.getCompleted()==0){
            if (mExerciseBean.getQuestionCompleted()==ReadingActivity.STATE_FINISHED){
                if (mReadingInfo.getAnswerShowTime().equals("0")){
                    layoutToast.setVisibility(View.VISIBLE );
                }else{
                    layoutToast.setVisibility(View.GONE );
                }

            }else{
                layoutToast.setVisibility(View.GONE );
            }
            btnNext.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
            btnCommit.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);
            ivGoBackReading.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);
            ivGoAnswerWhere.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
            //初始化提示

        }else{
            btnCommit.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
            ivGoAnswerWhere.setVisibility(View.VISIBLE);
            ivGoBackReading.setVisibility(View.GONE);
            etAnswer.setFocusable(false);
            layoutToast.setVisibility(View.VISIBLE );
        }

        if (mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED) {

            String answer = mExerciseBean.getAnswerContent();
            if (TextUtils.isEmpty(answer)){
                if (FontsUtils.isContainChinese(answer)||TextUtils.isEmpty(answer)){

                }else{
                    etAnswer.setTypeface(DUTApplication.getHsbwTypeFace());
                }
                etAnswer.setHint("");
            }else{
                if (FontsUtils.isContainChinese(answer)||TextUtils.isEmpty(answer)){

                }else{
                    etAnswer.setTypeface(DUTApplication.getHsbwTypeFace());
                }
                etAnswer.setText(answer);
            }

            etAnswer.setFocusable(false);

        }
        if (FontsUtils.isContainChinese(mExerciseBean.getRightAnswer())||TextUtils.isEmpty(mExerciseBean.getRightAnswer())){

        }else{
            tvRightAnswer.setTypeface(DUTApplication.getHsbwTypeFace());
        }
        if (FontsUtils.isContainChinese(mExerciseBean.getAnalysis())||TextUtils.isEmpty(mExerciseBean.getAnalysis())){

        }else{
            tvToast.setTypeface(DUTApplication.getHsbwTypeFace());
        }
        tvRightAnswer.setText(mExerciseBean.getRightAnswer());
        tvToast.setText(mExerciseBean.getAnalysis());

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

    @OnClick({R.id.iv_go_back_reading, R.id.iv_go_answer_where, R.id.btn_commit, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_answer_where:
                if (mEducationAnswerFrom.size() == 0) {
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(getHoldingActivity());
                    builder.title("提示");
                    builder.content("当前小题没有设置答案出处，Sorry！");
                    builder.positiveText("确定");
                    builder.show();

                } else {
                    showAnswerWhere();
                }
                break;
            case R.id.iv_go_back_reading:
                mGoOriginalTime++;
                goOriginal();
                break;
            case R.id.btn_commit:
//                if (TextUtils.isEmpty(etAnswer.getText().toString().trim())) {
//                    showToast("请输入答案");
//                } else {
                    commitAnswer();
//                }
                break;
            case R.id.btn_next:
                goNext();
                break;
        }
    }

    private void goOriginal() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getHoldingActivity());
        builder.title(mReadingInfo.getCourseTitle());
        builder.content(mReadingInfo.getCourseContent());
        builder.positiveText("确定");
        builder.show();
    }

    private void showAnswerWhere() {
        //      可以将同一段string以不同样式提现的builder
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(mReadingInfo.getCourseContent());
        for (ReadingListResp.DataBean.ExercisesBean.EducationAnswerFrom item : mEducationAnswerFrom) {
            int wordStartIndex = item.getWordIndex();
            int wordEndIndex = wordStartIndex + item.getWordLength();
            BackgroundColorSpan yellowSpan = new BackgroundColorSpan(Color.YELLOW);
            stringBuilder.setSpan(yellowSpan, wordStartIndex, wordEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getHoldingActivity());
        builder.title(mReadingInfo.getCourseTitle());
        builder.content(stringBuilder);
        builder.positiveText("确定");
        builder.show();
    }

    private void commitAnswer() {
        List<String> answerList = new ArrayList<>();
        answerList.add(etAnswer.getText().toString().trim());
        String answerTemp = new Gson().toJson(answerList);

        long commitTime = System.currentTimeMillis();
        long timeSpan = TimeUtils.getTimeSpan(commitTime, mStartTime, ConstUtils.TimeUnit.MSEC);

        if (timeSpan == 0) {
            timeSpan = 1;
        }

        if (mExerciseBean != null) {
            mReadingInfo.getExercises()
                    .get(mReadingInfo.getExercises().indexOf(mExerciseBean))
                    .setAnswerContent(answerTemp);
            mPresenter.commitAnswer(mExerciseBean.getQuestionId(), answerTemp, timeSpan, mGoOriginalTime,mReadingInfo.getClassId());
        }
    }

    private void goNext() {

        if (mReadingInfo.getExercises().size() > (mQuestionIndex + 1)) {
            //还有课后小题
            ReadingListResp.DataBean.ExercisesBean bean = mReadingInfo.getExercises().get(mQuestionIndex + 1);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ReadingActivity.READING_INFO, mReadingInfo);
            bundle.putSerializable(ReadingActivity.READING_QUESTION, bean);

            switch (bean.getQuestionType()) {
                case ReadingActivity.TYPE_CHOICE:
                    //选择题
//                    ra.setToolbarTitle("选择题");
                    ReadingChoiceQuestionFragment choiceQuestionFragment = new ReadingChoiceQuestionFragment();
                    choiceQuestionFragment.setArguments(bundle);
                    addFragment(choiceQuestionFragment);
                    break;
                case ReadingActivity.TYPE_INPUT:
                    //填空题
//                    ra.setToolbarTitle("填空题");
                    ReadingInputNewFragment inputQuestionFragment = new ReadingInputNewFragment();
                    inputQuestionFragment.setArguments(bundle);
                    addFragment(inputQuestionFragment);
                    break;
                case ReadingActivity.TYPE_MULTI_CHOICE:
                    //多选
//                    ra.setToolbarTitle("选择题");
                    ReadingMultiChoiceQuestionFragment multiChoiceFragment = new ReadingMultiChoiceQuestionFragment();
                    multiChoiceFragment.setArguments(bundle);
                    addFragment(multiChoiceFragment);
                    break;
                case ReadingActivity.TYPE_TRANSLATE:
                    //翻译
//                    ra.setToolbarTitle("翻译题");
                    ReadingTranslateQuestionFragment translateQuestionFragment = new ReadingTranslateQuestionFragment();
                    translateQuestionFragment.setArguments(bundle);
                    addFragment(translateQuestionFragment);
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

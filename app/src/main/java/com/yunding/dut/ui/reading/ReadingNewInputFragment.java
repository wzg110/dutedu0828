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
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.yunding.dut.R;
import com.yunding.dut.adapter.DiscussQuestionInputAdapter;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.reading.ReadingDataResp;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.presenter.reading.ReadingPresenter;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.FontsUtils;
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
 * 类 名 称：ReadingInputQuestionFragment
 * <P/>描    述：阅读填空题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/30
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/30
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class ReadingNewInputFragment extends BaseFragmentInReading implements IReadingQuestionView {
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.tv_answer_tips1)
    TextView tvAnswerTips1;
    @BindView(R.id.rv_input_list_answer)
    DUTVerticalRecyclerView rvInputListAnswer;
    @BindView(R.id.tv_answer_tips)
    TextView tvAnswerTips;
    @BindView(R.id.tv_right_answer)
    TextView tvRightAnswer;
    @BindView(R.id.tv_toast)
    TextView tvToast;
    @BindView(R.id.layout_toast)
    ScrollView layoutToast;
    @BindView(R.id.iv_go_back_reading)
    ImageView ivGoBackReading;
    @BindView(R.id.iv_go_answer_where)
    ImageView ivGoAnswerWhere;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    Unbinder unbinder;

    private ReadingListResp.DataBean mReadingInfo;
    private ReadingListResp.DataBean.ExercisesBean mExerciseBean;
    private List<ReadingListResp.DataBean.ExercisesBean.EducationAnswerFrom> mEducationAnswerFrom;

    private int mQuestionIndex;
    private DiscussQuestionInputAdapter mInputAdapter;

    private ReadingPresenter mPresenter;
    private String answerShowTime; //0答完即显示  1 阅读课完事显示
    private long mStartTime;
    private int mGoOriginalTime = 0;
    private ReadingInputFragment mReadingInputFragment;
    private Bundle mBundle;
    private  ReadingActivity ra;
    private int questionQuantity;
    private Dialog dialog;
    private  View view;
    public ReadingNewInputFragment(){}

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

        if (mQuestionIndex+1==mReadingInfo.getExercises().size()){
//            后面没有题了
            // TODO: 2017/7/28 三期
            mPresenter.showReadingData(mReadingInfo.getClassId(),mReadingInfo.getCourseId());
            // TODO: 2017/7/28 三期
            btnNext.setText("结束课程");
        }else{
            btnNext.setText("下一题");
        }
        mInputAdapter.setState(1);

        btnCommit.setVisibility(View.GONE);
        ivGoBackReading.setVisibility(View.GONE);
        ivGoAnswerWhere.setVisibility(View.VISIBLE);
        btnNext.setVisibility(View.VISIBLE);
        if (answerShowTime.equals("0")){
            layoutToast.setVisibility(View.VISIBLE);
        }
        EventBus.getDefault().post(mReadingInfo);

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
        return R.layout.fragment_new_input_question;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {

        mReadingInfo = (ReadingListResp.DataBean) getArguments().getSerializable(READING_INFO);
        if (getArguments().getSerializable(READING_QUESTION) instanceof ReadingListResp.DataBean.ExercisesBean) {
            mExerciseBean = (ReadingListResp.DataBean.ExercisesBean) getArguments().getSerializable(READING_QUESTION);
            mQuestionIndex = mReadingInfo.getExercises().indexOf(mExerciseBean);//题目编号
            questionQuantity=mReadingInfo.getExercises().size();
        }
        answerShowTime=mReadingInfo.getAnswerShowTime();
        mPresenter = new ReadingPresenter(this);
        mStartTime = System.currentTimeMillis();
        mEducationAnswerFrom=mExerciseBean.getEducationAnswerFroms();
        ra = (ReadingActivity) getActivity();
        ra.setToolbarTitle("课后小题");
        ra.setToolbarVisiable(true);
        ra.setToolbarBGC(getResources().getColor(R.color.bg_white));
        ra.setToolbarTitleColor(getResources().getColor(R.color.title));
//        ra.setToolbarTitleColor(getResources().getColor(R.color.textColorShow));
        //初始化UI
        if (mExerciseBean == null) return;
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
        if (mExerciseBean.getQuestionType() == ReadingActivity.TYPE_INPUT) {
            tvTitle.setText("填空题" + (1 + mQuestionIndex)+"/"+questionQuantity);
        } else {
            tvTitle.setText("填空题" + (1 + mQuestionIndex)+"/"+questionQuantity);
        }
        if (mReadingInfo.getExercises().size() > (mQuestionIndex + 1)){
//            后面没有题了
            btnNext.setText("下一题");
        }else{

            btnNext.setText("结束课程");
        }
        tvQuestion.setTypeface(DUTApplication.getHsbwTypeFace());
        tvQuestion.setText(mExerciseBean.getQuestionContent());

        String[] rightAnswerArray = mExerciseBean.getRightAnswer().split(",");
        List<String> inputList = new ArrayList<>();
        mInputAdapter = new DiscussQuestionInputAdapter(inputList, mExerciseBean.getQuestionCompleted());
        rvInputListAnswer.setAdapter(mInputAdapter);

        if (mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED) {
            //已完成的直接显示答案
            String[] answerContent = new Gson().fromJson(mExerciseBean.getAnswerContent(), String[].class);
            if (answerContent == null) return;
            for (String answer : answerContent) {
                inputList.add(answer);
            }
        } else {
            //未完成的直接显示空
            if (!mExerciseBean.getAnswerContent().isEmpty()){
                String[] answerContent = new Gson().fromJson(mExerciseBean.getAnswerContent(), String[].class);
                for (String answer : answerContent) {
                    inputList.add(answer);
                }

            }else {

                for (String answer : rightAnswerArray) {

                    inputList.add("");
                }
            }

        }
//        mBtnGoOriginal.setVisibility(view.GONE);
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
            ivGoAnswerWhere.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
            ivGoBackReading.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);
            mInputAdapter.setNewData(inputList);
            btnNext.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
            btnCommit.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);

        }else{
            ivGoBackReading.setVisibility(View.GONE);
            ivGoAnswerWhere.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            btnCommit.setVisibility(View.GONE);
            mInputAdapter.setState(1);
            layoutToast.setVisibility(View.VISIBLE );
        }
        String answer =  mExerciseBean.getRightAnswer().substring( mExerciseBean.getRightAnswer().indexOf("[")+1, mExerciseBean.getRightAnswer().indexOf("]"));
        String[] sourceStrArray = answer.split(",");
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i = 0; i < sourceStrArray.length; i++) {
            int a = i + 1;
            stringBuffer.append(a + ": " + sourceStrArray[i] + "\n");
        }

        if (FontsUtils.isContainChinese(mExerciseBean.getAnalysis())|| TextUtils.isEmpty(mExerciseBean.getAnalysis())){

        }else{
            tvToast.setTypeface(DUTApplication.getHsbwTypeFace());
        }
        if (FontsUtils.isContainChinese(stringBuffer.toString())|| TextUtils.isEmpty(stringBuffer.toString())){

        }else{
            tvRightAnswer.setTypeface(DUTApplication.getHsbwTypeFace());
        }

        tvRightAnswer.setText(stringBuffer.toString());
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

        if (mExerciseBean.getQuestionCompleted() != ReadingActivity.STATE_FINISHED){
            List<String> answerList = mInputAdapter.getData();

            String answerTemp = new Gson().toJson(answerList);
            mExerciseBean.setAnswerContent(answerTemp);
        }
    }

    @OnClick({R.id.iv_go_back_reading, R.id.iv_go_answer_where, R.id.btn_commit, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back_reading:
                mGoOriginalTime++;
                goOriginal();
                break;
            case R.id.iv_go_answer_where:
                if (mEducationAnswerFrom.size()==0){
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(getHoldingActivity());
                    builder.title("提示");
                    builder.content("当前小题没有设置答案出处，Sorry！");
                    builder.positiveText("确定");
                    builder.show();

                }else{
                    showAnswerWhere();
                }
                break;
            case R.id.btn_commit:
                commitAnswer();
                break;
            case R.id.btn_next:
                goNext();
                break;
        }
    }

    private void showAnswerWhere() {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(mReadingInfo.getCourseContent());
        for (ReadingListResp.DataBean.ExercisesBean.EducationAnswerFrom item: mEducationAnswerFrom) {
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

    private void goOriginal() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getHoldingActivity());
        builder.title(mReadingInfo.getCourseTitle());
        builder.content(mReadingInfo.getCourseContent());
        builder.positiveText("确定");
        builder.show();
    }

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
//                    ra.setToolbarTitle("选择题");
                    ReadingChoiceQuestionFragment choiceQuestionFragment = new ReadingChoiceQuestionFragment();
                    choiceQuestionFragment.setArguments(bundle);
                    addFragment(choiceQuestionFragment);
                    break;
                case ReadingActivity.TYPE_INPUT:
                    //填空题
//                    ra.setToolbarTitle("填空题");
                    ReadingNewInputFragment inputQuestionFragment = new ReadingNewInputFragment();
                    inputQuestionFragment.setArguments(bundle);
                    addFragment(inputQuestionFragment);
                    break;
                case  ReadingActivity.TYPE_MULTI_CHOICE:
//                    多选题
//                    ra.setToolbarTitle("选择题");
                    ReadingMultiChoiceQuestionFragment multiChoiceFragment=new ReadingMultiChoiceQuestionFragment();
                    multiChoiceFragment.setArguments(bundle);
                    addFragment(multiChoiceFragment);
                    break;
                case ReadingActivity.TYPE_TRANSLATE:
//                    翻译题
//                    ra.setToolbarTitle("翻译题");
                    ReadingTranslateQuestionFragment translateQuestionFragment=new ReadingTranslateQuestionFragment();
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

    private void commitAnswer() {
        List<String> answerList = mInputAdapter.getData();

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
}

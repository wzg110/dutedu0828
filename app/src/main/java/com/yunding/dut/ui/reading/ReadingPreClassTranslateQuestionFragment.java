package com.yunding.dut.ui.reading;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

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
 * 类 名 称：ReadingPreClassTranslateQuestionFragment
 * <P/>描    述：课前小题 翻译
 * <P/>创 建 人：Administrator
 * <P/>创建时间：2017/6/28 15:36
 * <P/>修 改 人：Administrator
 * <P/>修改时间：2017/6/28 15:36
 * <P/>修改备注：
 * <P/>版    本：
 */
public class ReadingPreClassTranslateQuestionFragment extends BaseFragmentInReading implements IReadingQuestionView {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.et_answer)
    EditText etAnswer;

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
    Unbinder unbinder;

    private ReadingListResp.DataBean mReadingInfo;
    private ReadingListResp.DataBean.PreClassExercisesBean mPreExerciseBean;
    private int mQuestionIndex;
    private ReadingPresenter mPresenter;
    private long mStartTime;
    private ReadingActivity ra;
    private int questionQuantity;
    private String answerShowTime; //0答完即显示  1 阅读课完事显示
    private Dialog dialog;
    private View view;
    public ReadingPreClassTranslateQuestionFragment() {
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reading_pre_translate_question;
    }



    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        mReadingInfo = (ReadingListResp.DataBean) getArguments().getSerializable(ReadingActivity.READING_INFO);
        if (getArguments().getSerializable(ReadingActivity.READING_QUESTION) instanceof ReadingListResp.DataBean.PreClassExercisesBean) {
            mPreExerciseBean = (ReadingListResp.DataBean.PreClassExercisesBean) getArguments().getSerializable(ReadingActivity.READING_QUESTION);
            mQuestionIndex = mReadingInfo.getPreClassExercises().indexOf(mPreExerciseBean);//题目编号
            questionQuantity=mReadingInfo.getPreClassExercises().size();
        }

        mPresenter = new ReadingPresenter(this);
        mStartTime = System.currentTimeMillis();

        ra= (ReadingActivity) getActivity();
        ra.setToolbarTitle("课前小题");
        ra.setToolbarBGC(getResources().getColor(R.color.bg_white));
        ra.setToolbarTitleColor(getResources().getColor(R.color.title));
        answerShowTime=mReadingInfo.getAnswerShowTime();
        //初始化UI
        if (mPreExerciseBean == null) return;
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
        if (mPreExerciseBean.getQuestionType() == ReadingActivity.TYPE_TRANSLATE) {
//            tvTitle.setText("课前小题第" + (mQuestionIndex + 1) + "题（翻译）" + "（共" + mReadingInfo.getPreClassExercises().size() + "题）");
            tvTitle.setText("翻译题"+ (mQuestionIndex + 1)+"/"+questionQuantity);
        } else {
            tvTitle.setText("翻译题" + (mQuestionIndex + 1)+"/"+questionQuantity);
        }
        if (FontsUtils.isContainChinese(mPreExerciseBean.getQuestionContent())||TextUtils.isEmpty(mPreExerciseBean.getQuestionContent())){

        }else{
            tvQuestion.setTypeface(DUTApplication.getHsbwTypeFace());
        }
        tvQuestion.setText(mPreExerciseBean.getQuestionContent());


        if (mReadingInfo.getCompleted()==0){
            if (mPreExerciseBean.getQuestionCompleted()==ReadingActivity.STATE_FINISHED){
                if (mReadingInfo.getAnswerShowTime().equals("0")){
                    layoutToast.setVisibility(View.VISIBLE );
                }else{
                    layoutToast.setVisibility(View.GONE );
                }

            }else{
                layoutToast.setVisibility(View.GONE );
            }
            btnNext.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
            btnCommit.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);

        }else{
            btnCommit.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
            etAnswer.setFocusable(false);
            layoutToast.setVisibility(View.VISIBLE );
        }
        //初始化提示

        if (mPreExerciseBean.getQuestionCompleted()==ReadingActivity.STATE_FINISHED){


            String answer = mPreExerciseBean.getAnswerContent();
            if (TextUtils.isEmpty(answer)){
                if (FontsUtils.isContainChinese(answer)||TextUtils.isEmpty(answer)){

                }else{
                    etAnswer.setTypeface(DUTApplication.getHsbwTypeFace());
                }
            }else{
                etAnswer.setHint("");
            }
            etAnswer.setFocusable(false);

        }
        if (FontsUtils.isContainChinese(mPreExerciseBean.getRightAnswer())||TextUtils.isEmpty(mPreExerciseBean.getRightAnswer())){

        }else{
            tvRightAnswer.setTypeface(DUTApplication.getHsbwTypeFace());
        }
        if (FontsUtils.isContainChinese(mPreExerciseBean.getAnalysis())||TextUtils.isEmpty(mPreExerciseBean.getAnalysis())){

        }else{
            tvToast.setTypeface(DUTApplication.getHsbwTypeFace());
        }
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
        etAnswer.setEnabled(false);
        etAnswer.setHint("");
        btnCommit.setVisibility(View.GONE);
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

    @OnClick({R.id.btn_commit, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
//                if (TextUtils.isEmpty(etAnswer.getText().toString().trim())){
//                    showToast("请输入答案");
//                }else{
                    commitAnswer();
//                }

                break;
            case R.id.btn_next:
                goNext();
                break;
        }
    }

    private void commitAnswer() {
        List<String> answerList =new ArrayList<>();
        answerList.add(etAnswer.getText().toString().trim());
        String answerTemp = new Gson().toJson(answerList);

        long commitTime = System.currentTimeMillis();
        long timeSpan = TimeUtils.getTimeSpan(commitTime, mStartTime, ConstUtils.TimeUnit.MSEC);

        if (timeSpan == 0) {
            timeSpan = 1;
        }

        if (mPreExerciseBean != null) {
            mReadingInfo.getPreClassExercises()
                    .get(mReadingInfo.getPreClassExercises().indexOf(mPreExerciseBean))
                    .setAnswerContent(answerTemp);
            mPresenter.commitAnswer(mPreExerciseBean.getQuestionId(), answerTemp, timeSpan, 0,mReadingInfo.getClassId());
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

//                    ra.setToolbarTitle("选择题");
                    ReadingPreClassChoiceQuestionFragment choiceQuestionFragment = new ReadingPreClassChoiceQuestionFragment();
                    choiceQuestionFragment.setArguments(bundle);
                    addFragment(choiceQuestionFragment);
                    break;
                case ReadingActivity.TYPE_INPUT:
                    //填空题

//                    ra.setToolbarTitle("填空题");
                    ReadingPreClassInputNewFragment inputQuestionFragment = new ReadingPreClassInputNewFragment();
                    inputQuestionFragment.setArguments(bundle);
                    addFragment(inputQuestionFragment);
                    break;
                case ReadingActivity.TYPE_MULTI_CHOICE:

//                    ra.setToolbarTitle("选择题");
                    ReadingPreClassMultiChoiceQuestionFragment multiChoiceFragment = new ReadingPreClassMultiChoiceQuestionFragment();
                    multiChoiceFragment.setArguments(bundle);
                    addFragment(multiChoiceFragment);
                    break;
                case ReadingActivity.TYPE_TRANSLATE:

//                    ra.setToolbarTitle("翻译题");
                    ReadingPreClassTranslateQuestionFragment translateQuestionFragment = new ReadingPreClassTranslateQuestionFragment();
                    translateQuestionFragment.setArguments(bundle);
                    addFragment(translateQuestionFragment);
                    break;
                default:
                    showSnackBar("没有该题型，请反馈客服");
                    break;
            }
        } else {
            //课前小题已经答完，进入阅读页面
//            ra.setToolbarTitle("阅读原文");
            Bundle bundle = new Bundle();
            bundle.putSerializable(ReadingActivity.READING_INFO, mReadingInfo);
            ReadingNewArticleFragment originalFragment = new ReadingNewArticleFragment();
            originalFragment.setArguments(bundle);
            addFragment(originalFragment);
        }
    }
}

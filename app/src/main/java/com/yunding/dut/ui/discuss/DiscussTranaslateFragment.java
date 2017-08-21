package com.yunding.dut.ui.discuss;

import android.app.Dialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.data.discuss.DiscussAnswerCache;
import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.model.resp.discuss.DiscussQuestionListResp;
import com.yunding.dut.model.resp.discuss.DiscussQuestionParam;
import com.yunding.dut.presenter.discuss.DiscussQuestionPresenter;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yunding.dut.ui.discuss.DiscussQuestionNewActivity.DISCUSS_ANSWER_CACHE;
import static com.yunding.dut.ui.discuss.DiscussQuestionNewActivity.DISCUSS_QUESTION_ITEM;

/**
 * 类 名 称：ReadingInputQuestionFragment
 * <P/>描    述：阅读填空题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/7/10
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/7/10
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class DiscussTranaslateFragment extends BaseFragmentInReading implements IDiscussQuestionView {

    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.et_translate)
    EditText etTranslate;
    @BindView(R.id.btn_last)
    Button btnLast;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    Unbinder unbinder;
    @BindView(R.id.iv_blue)
    ImageView ivBlue;
    private Dialog dialog;
    private View view;
    private DiscussListResp.DataBean mDiscussInfo;//讨论组信息 上传答案用
    private ArrayList<DiscussQuestionListResp.DataBean> mDataQuestionList;//问题总数据源
    private DiscussQuestionListResp.DataBean mDataQuestionItem;//应该显示的问题详情
    private ArrayList<DiscussAnswerCache> mDataCache;//答案缓存
    private DiscussQuestionPresenter mPresenter;//提交答案
    private int mQuestionIndex;//题号
    private DiscussQuestionNewActivity da;
    private TextView tv_time;
    DiscussionCountDown mCountDown;
    private String serverTime;
    private long spanToNow, timeLeft;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discuss_translate_question;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        da = (DiscussQuestionNewActivity) getActivity();
        da.setToolBarTitle("答题");
        mDiscussInfo = (DiscussListResp.DataBean) getArguments().getSerializable(DiscussQuestionNewActivity.DISCUSS_INFO);

        if (getArguments().getSerializable(DISCUSS_QUESTION_ITEM) instanceof DiscussQuestionListResp.DataBean) {
            mDataQuestionList = (ArrayList<DiscussQuestionListResp.DataBean>) getArguments().getSerializable(DiscussQuestionNewActivity.DISCUSS_QUESTIONG_LIST_INFO);
            mDataQuestionItem = (DiscussQuestionListResp.DataBean) getArguments().getSerializable(DISCUSS_QUESTION_ITEM);
            mQuestionIndex = mDataQuestionList.indexOf(mDataQuestionItem);
        }
        mPresenter = new DiscussQuestionPresenter(this);
        mPresenter.getServerTime();

        if (getArguments().getSerializable(DISCUSS_ANSWER_CACHE) != null) {

            mDataCache = (ArrayList<DiscussAnswerCache>) getArguments().getSerializable(DISCUSS_ANSWER_CACHE);
            etTranslate.setText(mDataCache.get(mQuestionIndex).getTranslateAnswer());
        } else {
            initCache();
            etTranslate.setText(mDataCache.get(mQuestionIndex).getTranslateAnswer());
        }
        etTranslate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mDataCache.get(mQuestionIndex).setTranslateAnswer(etTranslate.getText().toString());
                da.setAnswerCache(mDataCache);
            }
        });


        if (mDiscussInfo == null) return;
        tvTitle.setText("翻译" + (1 + mQuestionIndex) + "/" + mDataQuestionList.size());
        tvQuestion.setTypeface(DUTApplication.getHsbwTypeFace());
        tvQuestion.setText(mDataQuestionItem.getContent());
        tv_time = (TextView) view.findViewById(R.id.tv_time);


        if (mDiscussInfo.getState() != 1) {
            btnCommit.setBackgroundResource(R.drawable.bg_discuss_no_commit);
            tv_time.setText("已结束");
            ivBlue.setVisibility(View.GONE);
        } else {
            if (mDiscussInfo.getIsLeader() != 1){
                btnCommit.setVisibility(View.GONE);
            }
            tv_time.setText(TimeUtils.millis2String(mDiscussInfo.getCountdownTime()*60*1000,"mm:ss"));
            btnCommit.setBackgroundResource(R.drawable.button_corners);


        }

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showQuestions(DiscussQuestionListResp resp) {

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
        getHoldingActivity().finish();

    }

    @Override
    public void getServerTime(String time) {
        serverTime=time;
        spanToNow = TimeUtils.getTimeSpan(serverTime,mDiscussInfo.getOpeningTime(), ConstUtils.TimeUnit.MSEC);
        timeLeft = mDiscussInfo.getCountdownTime()*60*1000 - spanToNow;
        if (mDiscussInfo.getState() == 1){
            mCountDown = new DiscussionCountDown(timeLeft, 1000);
            mCountDown.start();
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
        if (mCountDown != null) {
            mCountDown.cancel();
        }
    }

    @OnClick({R.id.btn_last, R.id.btn_next, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_last:
                if (mQuestionIndex == 0) {

                    showToast("当前已经为第一题");
                } else {
                    removeFragment();
                }

                break;
            case R.id.btn_next:

                goNext();

                break;
            case R.id.btn_commit:

                if (mDiscussInfo.getIsLeader() == 1) {
//                    StringBuilder sb=new StringBuilder();
//                    for(Map.Entry<String,Boolean> entrySet:multiChoice.entrySet()){
//                        sb=sb.append(entrySet.getKey()).append(",");
//
//                    }
//                    String answer=sb.substring(0,sb.length()-1).toString();
                    if (mDiscussInfo.getState() != 1) {


                    } else {
                        showDialog();
                    }


                } else {
                    showToast("只有组长可以提交答案");
                }
                break;
        }
    }

    private void goNext() {
        if ((mQuestionIndex + 1) < mDataQuestionList.size()) {

//                直接进下一题
            if (mDataQuestionList.get(mQuestionIndex + 1).getType() == 0) {
//                   选择
                DiscussChoiceFragment discussChoiceFragment = new DiscussChoiceFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(da.DISCUSS_INFO, mDiscussInfo);
                bundle.putSerializable(da.DISCUSS_QUESTIONG_LIST_INFO, mDataQuestionList);
                bundle.putSerializable(da.DISCUSS_ANSWER_CACHE, mDataCache);
                bundle.putSerializable(da.DISCUSS_QUESTION_ITEM, mDataQuestionList.get(mQuestionIndex + 1));
                discussChoiceFragment.setArguments(bundle);
                addFragment(discussChoiceFragment);
            } else if (mDataQuestionList.get(mQuestionIndex + 1).getType() == 1) {
//                    填空
                DiscussQuestionInputNewFragment discussChoiceFragment = new DiscussQuestionInputNewFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(da.DISCUSS_INFO, mDiscussInfo);
                bundle.putSerializable(da.DISCUSS_QUESTIONG_LIST_INFO, mDataQuestionList);
                bundle.putSerializable(DISCUSS_ANSWER_CACHE, mDataCache);
                bundle.putSerializable(DISCUSS_QUESTION_ITEM, mDataQuestionList.get(mQuestionIndex + 1));

                discussChoiceFragment.setArguments(bundle);
                addFragment(discussChoiceFragment);

            } else if (mDataQuestionList.get(mQuestionIndex + 1).getType() == 2) {
//                  多选
                DisucssMutiFragment discussChoiceFragment = new DisucssMutiFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(da.DISCUSS_INFO, mDiscussInfo);
                bundle.putSerializable(da.DISCUSS_QUESTIONG_LIST_INFO, mDataQuestionList);
                bundle.putSerializable(DISCUSS_ANSWER_CACHE, mDataCache);
                bundle.putSerializable(DISCUSS_QUESTION_ITEM, mDataQuestionList.get(mQuestionIndex + 1));

                discussChoiceFragment.setArguments(bundle);
                addFragment(discussChoiceFragment);
            } else if (mDataQuestionList.get(mQuestionIndex + 1).getType() == 3) {
//                    翻译
                DiscussTranaslateFragment discussChoiceFragment = new DiscussTranaslateFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(da.DISCUSS_INFO, mDiscussInfo);
                bundle.putSerializable(da.DISCUSS_QUESTIONG_LIST_INFO, mDataQuestionList);
                bundle.putSerializable(DISCUSS_ANSWER_CACHE, mDataCache);
                bundle.putSerializable(DISCUSS_QUESTION_ITEM, mDataQuestionList.get(mQuestionIndex + 1));
                discussChoiceFragment.setArguments(bundle);
                addFragment(discussChoiceFragment);

            }

        } else {
            showToast("木有下一题了");
        }
    }

    private class DiscussionCountDown extends CountDownTimer {

        public DiscussionCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {

            if (l <= 0) {
                tv_time.setText("已结束");
                ivBlue.setVisibility(View.GONE);
                if (mCountDown != null)
                    mCountDown.cancel();
            } else {
                tv_time.setText(TimeUtils.millis2String(l,"mm:ss"));
            }
        }

        @Override
        public void onFinish() {
            da.setStatus(2);
            mPresenter.changeDiscussState(String.valueOf(mDiscussInfo.getGroupId()),String.valueOf(mDiscussInfo.getThemeId()));
            tv_time.setText("已结束");
            ivBlue.setVisibility(View.GONE);
            btnCommit.setBackgroundResource(R.drawable.bg_discuss_no_commit);
            mDiscussInfo.setState(2);
            etTranslate.setFocusable(false);
        }
    }

//    private void initCache() {
//        if (mDataCache != null) {
//
//        } else {
//            mDataCache = new ArrayList<>();
//
//            for (DiscussQuestionListResp.DataBean data : mDataQuestionList) {
//                DiscussAnswerCache answerCache = new DiscussAnswerCache();
//                answerCache.setQuestion(data);
//                answerCache.setQuestionType(data.getType());
//
//                if (mDiscussInfo.getState() != 2) {
//                    List<String> inputCache = new ArrayList<>();
//                    for (String answer : data.getAnswer().split(",")) {
//                        inputCache.add("");
//                    }
//                    answerCache.setInputAnswer(inputCache);
//                    answerCache.setTranslateAnswer("");
//                    answerCache.setChoiceAnswer("");
//
//                } else {
//                    answerCache.setChoiceAnswer(data.getStudentAnswer());
//                    List<String> inputCache = new ArrayList<>();
//                    for (String answer : data.getAnswer().split(",")) {
//                        inputCache.add(answer);
//                    }
//                    answerCache.setInputAnswer(inputCache);
//                    answerCache.setTranslateAnswer(answerCache.getTranslateAnswer());
//                }
//
//                mDataCache.add(answerCache);
//            }
//
//            da.setAnswerCache(mDataCache);
//
//        }
//
//    }
    private void initCache() {
        if (mDataCache != null) {

        } else {
            mDataCache = new ArrayList<>();
            for (DiscussQuestionListResp.DataBean data : mDataQuestionList) {
                DiscussAnswerCache answerCache = new DiscussAnswerCache();
                answerCache.setQuestion(data);
                answerCache.setQuestionType(data.getType());
                answerCache.setChoiceAnswer("");
                if (mDiscussInfo.getState() != 2) {
                    Map<Integer, String> inputCache = new HashMap<>();
                    for (int i = 0; i < data.getBlanksInfo().size(); i++) {
                        inputCache.put(i, "");
                    }
                    answerCache.setInputStudentAnswer(inputCache);
                    answerCache.setTranslateAnswer("");
                } else {
                    Map<Integer, String> inputCache = new HashMap<>();
                    List<String> inputCache1 = new ArrayList<>();
                    for (String answer : data.getAnswer().split(",")) {
                        inputCache1.add(answer);
                    }

                    for (int i = 0; i < data.getBlanksInfo().size(); i++) {
                        inputCache.put(i, inputCache1.get(i));
                    }
                    answerCache.setChoiceAnswer(data.getStudentAnswer());
                    answerCache.setInputStudentAnswer(inputCache);
                    answerCache.setTranslateAnswer(answerCache.getTranslateAnswer());
                }
                mDataCache.add(answerCache);
            }



            da.setAnswerCache(mDataCache);
        }
    }

        private String getPram() {

            DiscussQuestionParam param = new DiscussQuestionParam();
            List<DiscussQuestionParam.DataBean> list = new ArrayList<>();
            for (DiscussAnswerCache cache : mDataCache) {
                DiscussQuestionParam.DataBean dataBean = new DiscussQuestionParam.DataBean();
                if (cache.getQuestionType() == 0) {
                    dataBean.setAnswer(cache.getChoiceAnswer());
                    dataBean.setGroupId(String.valueOf(mDiscussInfo.getGroupId()));
                    dataBean.setStudentId(String.valueOf(DUTApplication.getUserInfo().getUserId()));
                    dataBean.setThemeId(String.valueOf(cache.getQuestion().getThemeId()));
                    dataBean.setTopicId(String.valueOf(cache.getQuestion().getTopicId()));
                } else if (cache.getQuestionType() == 1){
                    List<String> inputAnswer = cache.getInputAnswer();
                    String answerStr = "";
                    for (String answer : inputAnswer) {
                        answerStr += (answer + ",");
                    }
                    answerStr = answerStr.substring(0, answerStr.length());
                    dataBean.setAnswer(answerStr);
                    dataBean.setGroupId(String.valueOf(mDiscussInfo.getGroupId()));
                    dataBean.setStudentId(String.valueOf(DUTApplication.getUserInfo().getUserId()));
                    dataBean.setThemeId(String.valueOf(mDiscussInfo.getThemeId()));
                    dataBean.setTopicId(String.valueOf(cache.getQuestion().getTopicId()));
                }else if (cache.getQuestionType() == 2){
                    dataBean.setAnswer(cache.getChoiceAnswer());
                    dataBean.setGroupId(String.valueOf(mDiscussInfo.getGroupId()));
                    dataBean.setStudentId(String.valueOf(DUTApplication.getUserInfo().getUserId()));
                    dataBean.setThemeId(String.valueOf(mDiscussInfo.getThemeId()));
                    dataBean.setTopicId(String.valueOf(cache.getQuestion().getTopicId()));

                }else{

                    dataBean.setAnswer(cache.getTranslateAnswer());
                    dataBean.setGroupId(String.valueOf(mDiscussInfo.getGroupId()));
                    dataBean.setStudentId(String.valueOf(DUTApplication.getUserInfo().getUserId()));
                    dataBean.setThemeId(String.valueOf(mDiscussInfo.getThemeId()));
                    dataBean.setTopicId(String.valueOf(cache.getQuestion().getTopicId()));
                }
                list.add(dataBean);
            }
            param.setData(list);
            String paramStr = new Gson().toJson(param);
            return paramStr;
        }


    private void showDialog() {
        dialog = new Dialog(getHoldingActivity(), R.style.ActionSheetDialogStyle);
        view = LayoutInflater.from(getHoldingActivity()).inflate(R.layout.login_exit_dialog, null);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        TextView tv = (TextView) view.findViewById(R.id.tv_message_content);
        tv.setText("讨论未结束可重复提交答案，确定提交？");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDiscussInfo != null && mPresenter != null) {
                    mPresenter.commitAnswer(getPram());
                } else showMsg("提交失败");

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
}

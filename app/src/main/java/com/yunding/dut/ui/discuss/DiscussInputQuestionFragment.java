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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yunding.dut.ui.discuss.DiscussQuestionNewActivity.DISCUSS_ANSWER_CACHE;
import static com.yunding.dut.ui.discuss.DiscussQuestionNewActivity.DISCUSS_QUESTION_ITEM;

/**
 * 类 名 称：DiscussInputQuestionFragment
 * <P/>描    述：（废弃）
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 11:59
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 11:59
 * <P/>修改备注：
 * <P/>版    本：
 */


public class DiscussInputQuestionFragment extends BaseFragmentInReading implements IDiscussQuestionView {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.rv_input_list_answer)
    ListView rvInputListAnswer;
    @BindView(R.id.btn_last)
    Button btnLast;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.btn_commit)
    Button btnCommit;
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
    private DiscussInputAdapter adapter;
    //    private Map<String, Boolean> multiChoice = new HashMap<>();//存放已经保存的选项
//    public static Map<Integer, Boolean> isSelected = new HashMap<>();
    private TextView tv_time;
    DiscussionCountDown mCountDown;
    private int anwseras;
    private String serverTime;
    private long spanToNow,timeLeft;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discuss_input_question;
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
        if (getArguments().getSerializable(DISCUSS_ANSWER_CACHE) != null) {
            rvInputListAnswer.clearFocus();
            mDataCache = (ArrayList<DiscussAnswerCache>) getArguments().getSerializable(DISCUSS_ANSWER_CACHE);

        }

        List<String> inputList = new ArrayList<>();
        String[] answer1 = mDataQuestionItem.getAnswer().split(",");
        //正确答案

        anwseras = answer1.length;//正确答案的长度  是用来设置空

        for (int i = 0; i < answer1.length; i++) {

            if (mDiscussInfo.getState() != 1) {
                if (mDiscussInfo.getState() == 2) {
//                    if (studentAnswer[0].equals("#3153146")) {
//                        inputList.add("");
//                    } else {


                    if ("".equals(mDataQuestionItem.getStudentAnswer())
                            ||"null".equals(mDataQuestionItem.getStudentAnswer())||
                            mDataQuestionItem.getStudentAnswer()==null){
                        inputList.add("");

                    }else{
                        String[] studentAnswer = mDataQuestionItem.getStudentAnswer().split(",");
                        if (studentAnswer.length==0){
                            inputList.add("");
                        }else{
                            inputList.add(studentAnswer[i]);
                        }
                    }
//                    }
                } else {

                    inputList.add("");
                }

            } else {
                if (mDataCache == null) {
                    inputList.add("");
                } else {
                    inputList = mDataCache.get(mQuestionIndex).getInputAnswer();
                }

            }

        }


        mPresenter = new DiscussQuestionPresenter(this);
        mPresenter.getServerTime();
        if (mDiscussInfo == null) return;
        tvTitle.setText("填空" + (1 + mQuestionIndex) + "/" + mDataQuestionList.size());
        tvQuestion.setTypeface(DUTApplication.getHsbwTypeFace());
        tvQuestion.setText(mDataQuestionItem.getContent());
        tv_time = (TextView) view.findViewById(R.id.tv_time);


        adapter = new DiscussInputAdapter(inputList);
        rvInputListAnswer.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        if (mDiscussInfo.getState() != 1) {
            if (mDiscussInfo.getState()==0){
                tv_time.setText("未开始");
            }else{

                tv_time.setText("已结束");
            }
            btnCommit.setBackgroundResource(R.drawable.bg_discuss_no_commit);

            ivBlue.setVisibility(View.GONE);


        } else {
            if (mDiscussInfo.getIsLeader() != 1){
                btnCommit.setVisibility(View.GONE);
            }
            tv_time.setText(TimeUtils.millis2String(mDiscussInfo.getCountdownTime() * 60 * 1000, "mm:ss"));

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
        showToast("提交成功");
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
                rvInputListAnswer.clearFocus();
                if (mQuestionIndex == 0) {

                    showToast("当前已经为第一题");
                } else {
                    removeFragment();
                }
                break;
            case R.id.btn_next:
                rvInputListAnswer.clearFocus();
                goNext();
                break;
            case R.id.btn_commit:
                rvInputListAnswer.clearFocus();
                if (mDiscussInfo.getIsLeader() == 1) {
//                  
                    if (mDiscussInfo.getState() != 1) {


                    } else {
                        showDialog();
//                       
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
                DiscussInputQuestionFragment discussChoiceFragment = new DiscussInputQuestionFragment();
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
            mPresenter.changeDiscussState(String.valueOf(mDiscussInfo.getGroupId()),String.valueOf(mDiscussInfo.getThemeId()));

            da.setStatus(2);
            tv_time.setText("已结束");
            ivBlue.setVisibility(View.GONE);
            btnCommit.setBackgroundResource(R.drawable.bg_discuss_no_commit);
            mDiscussInfo.setState(2);
        }
    }

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
                    List<String> inputCache = new ArrayList<>();
                    for (String answer : data.getAnswer().split(",")) {
                        inputCache.add("");
                    }
                    answerCache.setInputAnswer(inputCache);
                    answerCache.setTranslateAnswer("");
                } else {
                    List<String> inputCache = new ArrayList<>();
                    for (String answer : data.getAnswer().split(",")) {
                        inputCache.add(answer);
                    }
                    answerCache.setInputAnswer(inputCache);
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
            } else {
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
                } else showMsg("开启失败");

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

    private class DiscussInputAdapter extends BaseAdapter {
        private List<String> md;

        private DiscussInputAdapter(List<String> md1) {
            this.md = md1;

            initCache();

        }

        @Override
        public int getCount() {
            return md.size();
        }

        @Override
        public Object getItem(int position) {
            return md.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Inputholder holder;
            if (convertView == null) {
                holder = new Inputholder();
                convertView = View.inflate(getHoldingActivity(), R.layout.item_discuss_question_input_list, null);
                holder.et_input = (EditText) convertView.findViewById(R.id.et_answer);
                holder.tv_index = (TextView) convertView.findViewById(R.id.tv_blank_index);
                convertView.setTag(holder);
            } else {
                holder = (Inputholder) convertView.getTag();
            }
            holder.tv_index.setText((position + 1) + ".");

            if (mDiscussInfo.getState() != 1) {
//                不是进行中 未开始，已过期，已结束
                if (mDiscussInfo.getState() == 2) {
//                    结束
                    holder.et_input.setText(md.get(position));

                } else {
                    holder.et_input.setText("");
                }
                holder.et_input.setEnabled(false);
                holder.et_input.setFocusable(false);
            } else {
//                进行中
                holder.et_input.setEnabled(true);
                holder.et_input.setFocusable(true);
                holder.et_input.setText(mDataCache.get(mQuestionIndex).getInputAnswer().get(position));
            }
            holder.et_input.setTag(position);
            holder.et_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (mDataCache != null)
//                        md.set(helper.getAdapterPosition(), charSequence.toString());
                        mDataCache.get(mQuestionIndex).getInputAnswer().set(
                                (Integer) holder.et_input.getTag(), holder.et_input.getText().toString());

                    da.setAnswerCache(mDataCache);

                }
            });
//            holder.et_input.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    if (mDataCache != null)
////                        md.set(helper.getAdapterPosition(), charSequence.toString());
//                        mDataCache.get(mQuestionIndex).getInputAnswer().set((Integer) holder.et_input.getTag(), s.toString());
////                   Log.e("存的是什么",mDataCache.get(mQuestionIndex).getInputAnswer().get((int)holder.et_input.getTag())+"---"+(int)holder.et_input.getTag());
//
//                    da.setAnswerCache(mDataCache);
//                }
//
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });

            return convertView;
        }
    }

    class Inputholder {
        EditText et_input;
        TextView tv_index;

    }

}

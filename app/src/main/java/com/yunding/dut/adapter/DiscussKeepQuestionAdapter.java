package com.yunding.dut.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.data.discuss.DiscussAnswerCache;
import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.model.resp.discuss.DiscussQuestionListResp;
import com.yunding.dut.model.resp.discuss.DiscussQuestionParam;
import com.yunding.dut.ui.discuss.DiscussQuestionActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 类 名 称：DiscussKeepQuestionAdapter
 * <P/>描    述：保存输入答案adapter（废弃）
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 9:50
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 9:50
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DiscussKeepQuestionAdapter extends RecyclerView.Adapter {

    private final int TYPE_CHOICE = 0;
    private final int TYPE_INPUT = 1;

    private List<DiscussQuestionListResp.DataBean> mDatas;
    private List<DiscussAnswerCache> mDataCache;
    private Context mContext;
    private int a = -1;
    private DiscussListResp.DataBean mResp;
    private static final String TAG = "DiscussKeepQuestionAdap";

    public DiscussKeepQuestionAdapter(List<DiscussQuestionListResp.DataBean> datas, List<DiscussAnswerCache> dataCache, Context context, DiscussListResp.DataBean resp) {
        mDatas = datas;
        mDataCache = dataCache;
        mContext = context;
        mResp = resp;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case TYPE_CHOICE:
                view = LayoutInflater.from(DUTApplication.getInstance().getApplicationContext())
                        .inflate(R.layout.item_discuss_question_choice, parent, false);
                holder = new DiscussKeepQuestionAdapter.ChoiceViewHolder(view);
                break;
            case TYPE_INPUT:
                view = LayoutInflater.from(DUTApplication.getInstance().getApplicationContext())
                        .inflate(R.layout.item_discuss_question_input, parent, false);
                holder = new DiscussKeepQuestionAdapter.InputViewHolder(view);
                break;
            default:
                holder = null;
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final DiscussQuestionListResp.DataBean item = mDatas.get(holder.getAdapterPosition());
        if (mDataCache != null && mDataCache.size() > holder.getAdapterPosition())
            if (holder instanceof DiscussKeepQuestionAdapter.ChoiceViewHolder) {
                ((DiscussKeepQuestionAdapter.ChoiceViewHolder) holder).tvQuestionNo.setText("第" + ++position + "题");


                ((DiscussKeepQuestionAdapter.ChoiceViewHolder) holder).tvQuestionContent.setText(item.getContent());
                String options = item.getSelectOptions();
                if (!TextUtils.isEmpty(options)) {
                    final String[] optionArray = options.split("㊎");
                    if (((DiscussKeepQuestionAdapter.ChoiceViewHolder) holder).rgAnswer.getChildCount() >= optionArray.length)
                        return;
                    for (String option : optionArray) {
                        RadioButton rbChoice = new RadioButton(mContext);

                        rbChoice.setText(option);
                        if (mDataCache.get(holder.getAdapterPosition()) != null && TextUtils.equals(mDataCache.get(holder.getAdapterPosition()).getChoiceAnswer(), option)) {
                            a = rbChoice.getId();
                        }
                        if (TextUtils.equals(option, mDataCache.get(holder.getAdapterPosition()).getQuestion().getStudentAnswer())) {
                            a = rbChoice.getId();
                        }
                        ((DiscussKeepQuestionAdapter.ChoiceViewHolder) holder).rgAnswer.addView(rbChoice,
                                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    }
                    ((DiscussKeepQuestionAdapter.ChoiceViewHolder) holder).rgAnswer.check(-1);
                    ((DiscussKeepQuestionAdapter.ChoiceViewHolder) holder).rgAnswer.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                            RadioButton rbSelected = (RadioButton) radioGroup.findViewById(i);
                            if (mDataCache.get(holder.getAdapterPosition()) != null)
                                mDataCache.get(holder.getAdapterPosition()).setChoiceAnswer(rbSelected.getText().toString());
                        }
                    });
                }
            } else if (holder instanceof DiscussKeepQuestionAdapter.InputViewHolder) {
                ((DiscussKeepQuestionAdapter.InputViewHolder) holder).tvQuestionNo.setText("第" + ++position + "题");

                ((DiscussKeepQuestionAdapter.InputViewHolder) holder).tvQuestionContent.setText(item.getContent());
                if (mDataCache.get(holder.getAdapterPosition()) != null) {
                    List<String> dataList = new ArrayList<>();
                    if (!TextUtils.isEmpty(mDataCache.get(holder.getAdapterPosition()).getQuestion().getStudentAnswer())) {
                        String studentAnswer = mDataCache.get(holder.getAdapterPosition()).getQuestion().getStudentAnswer();
                        String[] answerArray = studentAnswer.split(",");
                        for (String answer : answerArray) {
                            dataList.add(answer);
                        }
                    } else {
                        dataList = mDataCache.get(holder.getAdapterPosition()).getInputAnswer();
                    }
                    ((DiscussKeepQuestionAdapter.InputViewHolder) holder).rvAnswer.setAdapter(new DiscussQuestionInputAdapter(dataList, 0));
                }
            }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getType();
    }

    private class ChoiceViewHolder extends RecyclerView.ViewHolder {

        private TextView tvQuestionNo;
        private TextView tvQuestionContent;
        private RadioGroup rgAnswer;

        public ChoiceViewHolder(View itemView) {
            super(itemView);
            tvQuestionNo = (TextView) itemView.findViewById(R.id.tv_question_no);
            tvQuestionContent = (TextView) itemView.findViewById(R.id.tv_question_content);
            rgAnswer = (RadioGroup) itemView.findViewById(R.id.rg_answer);
        }
    }

    private class InputViewHolder extends RecyclerView.ViewHolder {

        private TextView tvQuestionNo;
        private TextView tvQuestionContent;
        private RecyclerView rvAnswer;

        public InputViewHolder(View itemView) {
            super(itemView);
            tvQuestionNo = (TextView) itemView.findViewById(R.id.tv_question_no);
            tvQuestionContent = (TextView) itemView.findViewById(R.id.tv_question_content);
            rvAnswer = (RecyclerView) itemView.findViewById(R.id.rv_input_list);
        }
    }

    public void commitAnswer() {
        DiscussQuestionParam param = new DiscussQuestionParam();
        List<DiscussQuestionParam.DataBean> list = new ArrayList<>();
        for (DiscussAnswerCache cache : mDataCache) {
            DiscussQuestionParam.DataBean dataBean = new DiscussQuestionParam.DataBean();
            if (cache.getQuestionType() == 0) {
                dataBean.setAnswer(cache.getChoiceAnswer());
                dataBean.setGroupId(String.valueOf(mResp.getGroupId()));
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
                dataBean.setGroupId(String.valueOf(mResp.getGroupId()));
                dataBean.setStudentId(String.valueOf(DUTApplication.getUserInfo().getUserId()));
                dataBean.setThemeId(String.valueOf(cache.getQuestion().getThemeId()));
                dataBean.setTopicId(String.valueOf(cache.getQuestion().getTopicId()));

            }
            list.add(dataBean);
        }
        param.setData(list);
        String paramStr = new Gson().toJson(param);


        ((DiscussQuestionActivity) mContext).commitAnswer(paramStr);
    }

    public List<DiscussAnswerCache> keepAnswer() {
        return mDataCache;
    }
}

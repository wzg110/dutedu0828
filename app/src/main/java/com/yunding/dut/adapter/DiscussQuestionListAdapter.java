package com.yunding.dut.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.discuss.DiscussQuestionListResp;

import java.util.List;

/**
 * 类 名 称：DiscussQuestionListAdapter
 * <P/>描    述：讨论组题目列表
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/23 18:50
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/23 18:50
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class DiscussQuestionListAdapter extends RecyclerView.Adapter {

    private final int TYPE_CHOICE = 0;
    private final int TYPE_INPUT = 1;

    private List<DiscussQuestionListResp.DataBean> mDatas;
    private Context mContext;

    public DiscussQuestionListAdapter(List<DiscussQuestionListResp.DataBean> mDatas, Context mContext) {
        this.mDatas = mDatas;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case TYPE_CHOICE:
                view = LayoutInflater.from(DUTApplication.getInstance().getApplicationContext())
                        .inflate(R.layout.item_discuss_question_choice, parent, false);
                holder = new ChoiceViewHolder(view);
                break;
            case TYPE_INPUT:
                view = LayoutInflater.from(DUTApplication.getInstance().getApplicationContext())
                        .inflate(R.layout.item_discuss_question_input, parent, false);
                holder = new InputViewHolder(view);
                break;
            default:
                holder = null;
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ChoiceViewHolder) {

        } else if (holder instanceof InputViewHolder) {

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
}

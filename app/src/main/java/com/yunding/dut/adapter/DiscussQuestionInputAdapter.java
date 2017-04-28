package com.yunding.dut.adapter;

import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunding.dut.R;
import com.yunding.dut.model.data.discuss.DiscussAnswerCache;
import com.yunding.dut.ui.reading.ReadingActivity;

import java.util.List;

/**
 * 类 名 称：DiscussQuestionInputAdapter
 * <P/>描    述：讨论组问题列表输入框适配器
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/23 21:25
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/23 21:25
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DiscussQuestionInputAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private List<String> mData;
    private int mState;

    public DiscussQuestionInputAdapter(List<String> data, int state) {
        super(R.layout.item_discuss_question_input_list, data);
        mData = data;
        mState = state;
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        ((EditText) helper.getView(R.id.et_answer)).setTextColor(Color.BLACK);
        ((EditText) helper.getView(R.id.et_answer)).setHint("空" + String.valueOf(helper.getAdapterPosition() + 1));
        ((EditText) helper.getView(R.id.et_answer)).setText(item);
        ((EditText) helper.getView(R.id.et_answer)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mData != null)
                    mData.set(helper.getAdapterPosition(), charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        helper.getView(R.id.et_answer).setEnabled(mState == ReadingActivity.STATE_UNDER_WAY);
        if (mState == ReadingActivity.STATE_FINISHED) {
            ((EditText) helper.getView(R.id.et_answer)).setInputType(InputType.TYPE_NULL);
        }else{
            ((EditText) helper.getView(R.id.et_answer)).setInputType(InputType.TYPE_CLASS_TEXT);
        }

    }
}

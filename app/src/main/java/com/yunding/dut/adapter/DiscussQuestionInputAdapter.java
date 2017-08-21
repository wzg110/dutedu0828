package com.yunding.dut.adapter;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.ui.reading.ReadingActivity;
import com.yunding.dut.util.FontsUtils;

import java.util.List;

/**
 * 类 名 称：DiscussQuestionInputAdapter
 * <P/>描    述：讨论组问题列表输入框适配器（暂时废弃）
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

    public void setState(int state) {
        mState = state;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
        ((TextView)helper.getView(R.id.tv_blank_index)).setTypeface(DUTApplication.getHsbwTypeFace());
        ((TextView)helper.getView(R.id.tv_blank_index)).setText(String.valueOf(helper.getAdapterPosition() + 1)+". ");
       if (FontsUtils.isContainChinese(item)|| TextUtils.isEmpty(item)){

       }else{
           ((EditText) helper.getView(R.id.et_answer)).setTypeface(DUTApplication.getHsbwTypeFace());
       }

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
        helper.getView(R.id.et_answer).setFocusable(mState == ReadingActivity.STATE_UNDER_WAY);
        if (mState == ReadingActivity.STATE_FINISHED) {
            ((EditText) helper.getView(R.id.et_answer)).setInputType(InputType.TYPE_NULL);
        }else{
            ((EditText) helper.getView(R.id.et_answer)).setInputType(InputType.TYPE_CLASS_TEXT);
        }

    }
}

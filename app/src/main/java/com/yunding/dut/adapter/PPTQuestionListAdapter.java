package com.yunding.dut.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.ppt.QuestionInfoResp;
import com.yunding.dut.util.FontsUtils;

import java.util.List;

/**
 * 类 名 称：PPTQuestionListAdapter
 * <P/>描    述：PPT问题列表 （暂时废弃）
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 17:10
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 17:10
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class PPTQuestionListAdapter extends BaseQuickAdapter<QuestionInfoResp.DataBean, BaseViewHolder> {

    public PPTQuestionListAdapter(List<QuestionInfoResp.DataBean> data) {
        super(R.layout.item_ppt_question_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final QuestionInfoResp.DataBean item) {
        helper.setText(R.id.tv_question_no, "第一题");
        helper.setText(R.id.tv_count_down, item.getTimeLongs()/1000 + "秒");

        if (FontsUtils.isContainChinese(item.getContent())|| TextUtils.isEmpty(item.getContent())){

        }else{
            helper.setTypeface(R.id.tv_question_content, DUTApplication.getHsbwTypeFace());
        }
        helper.setText(R.id.tv_question_content, item.getContent());

        ((EditText)helper.getView(R.id.et_question_answer)).addTextChangedListener(new MyTextWatcher(item));
    }

    private class MyTextWatcher implements TextWatcher{

        private QuestionInfoResp.DataBean mInfo;

        MyTextWatcher(QuestionInfoResp.DataBean mInfo) {
            this.mInfo = mInfo;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            mInfo.setLocalAnswer(editable.toString());
        }
    }
}

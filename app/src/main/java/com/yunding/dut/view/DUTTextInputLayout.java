package com.yunding.dut.view;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

/**
 * 类 名 称：DUTTextInputLayout
 * <P/>描    述：自定义TextInputLayout
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 15:36
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 15:36
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DUTTextInputLayout extends TextInputLayout {
    public DUTTextInputLayout(Context context) {
        super(context);
    }

    public DUTTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DUTTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        if (getEditText() != null) {
            getEditText().addTextChangedListener(new CustomTextWatcher());
        }
    }

    private class CustomTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}

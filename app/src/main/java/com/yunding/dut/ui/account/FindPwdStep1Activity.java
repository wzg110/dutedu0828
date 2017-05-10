package com.yunding.dut.ui.account;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.yunding.dut.R;
import com.yunding.dut.ui.base.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPwdStep1Activity extends ToolBarActivity implements IFindPwdView {

    @BindView(R.id.et_phone)
    TextInputEditText etPhone;
    @BindView(R.id.til_phone)
    TextInputLayout tilPhone;
    @BindView(R.id.btn_send_sms_code)
    Button btnSendSmsCode;
    @BindView(R.id.et_sms_code)
    TextInputEditText etSmsCode;
    @BindView(R.id.til_sms_code)
    TextInputLayout tilSmsCode;
    @BindView(R.id.btn_next)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd_step_1);
        ButterKnife.bind(this);
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @OnClick({R.id.btn_send_sms_code, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send_sms_code:
                break;
            case R.id.btn_next:
                break;
        }
    }
}

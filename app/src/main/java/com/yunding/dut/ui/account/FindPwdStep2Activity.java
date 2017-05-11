package com.yunding.dut.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;

import com.yunding.dut.R;
import com.yunding.dut.presenter.account.FindPwdPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPwdStep2Activity extends ToolBarActivity implements IFindPwdViewStep2 {

    @BindView(R.id.et_new_pwd)
    TextInputEditText etNewPwd;
    @BindView(R.id.til_new_pwd)
    TextInputLayout tilNewPwd;
    @BindView(R.id.et_confirm_pwd)
    TextInputEditText etConfirmPwd;
    @BindView(R.id.til_confirm_pwd)
    TextInputLayout tilConfirmPwd;
    @BindView(R.id.btn_ok)
    Button btnOk;

    private FindPwdPresenter mPresenter;

    public static final String SMS_CODE = "sms_code";
    public static final String PHONE = "phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd_step_2);
        ButterKnife.bind(this);

        setTitle("修改密码");
        mPresenter = new FindPwdPresenter(this);
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
        String newPwd = etNewPwd.getText().toString();
        String smsCode = getIntent().getStringExtra(SMS_CODE);
        String phone = getIntent().getStringExtra(PHONE);
        mPresenter.findPwd(newPwd, smsCode, phone);
    }

    @Override
    public void showMsg(String msg) {
        showToast(msg);
    }

    @Override
    public void findSuccess() {
        showToast("修改成功");
        startActivity(new Intent(this, LoginActivity.class));
    }
}

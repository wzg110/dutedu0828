package com.yunding.dut.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yunding.dut.R;
import com.yunding.dut.presenter.account.FindPwdPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.util.third.BarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FindPwdStep2Activity extends ToolBarActivity implements IFindPwdViewStep2 {


    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_confirm_pwd)
    EditText etConfirmPwd;
    @BindView(R.id.ll_sms_code)
    LinearLayout llSmsCode;
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
        setTitleInCenter(getResources().getString(R.string.register_forgetpwd_title));

        getmToolbar().setBackgroundColor(getResources().getColor(R.color.bg_white));
        getmToolbarTitle().setTextColor(getResources().getColor(R.color.textColorShow));
        BarUtils.setColor(this, getResources().getColor(R.color.bg_white));
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

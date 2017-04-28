package com.yunding.dut.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.widget.Button;

import com.yunding.dut.R;
import com.yunding.dut.presenter.account.AccountPresenter;
import com.yunding.dut.presenter.account.ResetPwdPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPwdActivity extends ToolBarActivity implements IResetPwdView {

    @BindView(R.id.et_old_pwd)
    TextInputEditText etOldPwd;
    @BindView(R.id.til_old_pwd)
    TextInputLayout tilOldPwd;
    @BindView(R.id.et_new_pwd)
    TextInputEditText etNewPwd;
    @BindView(R.id.til_new_pwd)
    TextInputLayout tilNewPwd;
    @BindView(R.id.et_confirm_new_pwd)
    TextInputEditText etConfirmNewPwd;
    @BindView(R.id.til_confirm_new_pwd)
    TextInputLayout tilConfirmNewPwd;
    @BindView(R.id.btn_reset_pwd)
    Button btnResetPwd;

    private ResetPwdPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        ButterKnife.bind(this);

        setTitle(getString(R.string.reset_pwd_title));
        mPresenter = new ResetPwdPresenter(this);
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void invalidOldPwd() {
        tilOldPwd.setError(getString(R.string.invalid_pwd));
    }

    @Override
    public void invalidNewPwd() {
        tilNewPwd.setError(getString(R.string.invalid_pwd));
    }

    @Override
    public void invalidConfirmNewPwd() {
        tilConfirmNewPwd.setError(getString(R.string.invalid_pwd));
    }

    @Override
    public void pwdMisMatch() {
        tilConfirmNewPwd.setError(getString(R.string.pwd_mismatch));
    }

    @Override
    public void resetPwdSuccess() {
        showToast(R.string.reset_pwd_success);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        AccountPresenter.logOut();
    }

    @Override
    public void resetPwdFailed(String msg) {
        showToast(msg);
    }

    @OnClick(R.id.btn_reset_pwd)
    public void onViewClicked() {
        String oldPwd = etOldPwd.getText().toString();
        String newPwd = etNewPwd.getText().toString();
        String confirmNewPwd = etConfirmNewPwd.getText().toString();
        mPresenter.resetPwd(oldPwd, newPwd, confirmNewPwd);
    }
}

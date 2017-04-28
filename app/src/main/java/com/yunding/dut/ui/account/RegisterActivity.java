package com.yunding.dut.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.yunding.dut.R;
import com.yunding.dut.presenter.account.RegisterPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.ui.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends ToolBarActivity implements IRegisterView {

    @BindView(R.id.et_account)
    TextInputEditText etAccount;
    @BindView(R.id.til_account)
    TextInputLayout tilAccount;
    @BindView(R.id.et_name)
    TextInputEditText etName;
    @BindView(R.id.til_name)
    TextInputLayout tilName;
    @BindView(R.id.et_pwd)
    TextInputEditText etPwd;
    @BindView(R.id.til_pwd)
    TextInputLayout tilPwd;
    @BindView(R.id.et_confirm_pwd)
    TextInputEditText etConfirmPwd;
    @BindView(R.id.til_confirm_pwd)
    TextInputLayout tilConfirmPwd;
    @BindView(R.id.btn_register)
    Button btnRegister;

    private RegisterPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        setTitleInCenter(getString(R.string.register_title));
        setShowNavigation(false);

        mPresenter = new RegisterPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void invalidAccount() {
        tilAccount.setError(getString(R.string.invalid_account));
    }

    @Override
    public void invalidName() {
        tilName.setError(getString(R.string.invalid_name));
    }

    @Override
    public void invalidPwd() {
        tilPwd.setError(getString(R.string.invalid_pwd));
    }

    @Override
    public void invalidConfirmPwd() {
        tilConfirmPwd.setError(getString(R.string.invalid_pwd));
    }

    @Override
    public void pwdMismatch() {
        tilConfirmPwd.setError(getString(R.string.pwd_mismatch));
    }

    @Override
    public void registerSuccess() {
        showToast(R.string.register_success);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void registerFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @OnClick(R.id.btn_register)
    public void onViewClicked() {
        String account = etAccount.getText().toString();
        String name = etName.getText().toString();
        String pwd = etPwd.getText().toString();
        String confirmPwd = etConfirmPwd.getText().toString();
        mPresenter.register(account, name, pwd, confirmPwd);
    }
}

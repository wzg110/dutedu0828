package com.yunding.dut.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.presenter.account.LoginPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.ui.home.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends ToolBarActivity implements ILoginView {

    @BindView(R.id.et_account)
    TextInputEditText etAccount;
    @BindView(R.id.til_account)
    TextInputLayout tilAccount;
    @BindView(R.id.et_pwd)
    TextInputEditText etPwd;
    @BindView(R.id.til_pwd)
    TextInputLayout tilPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_reset_pwd)
    Button btnResetPwd;

    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setTitleInCenter(getString(R.string.login_title));
        setShowNavigation(false);

        mPresenter = new LoginPresenter(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.btn_login, R.id.btn_register, R.id.btn_reset_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String account = etAccount.getText().toString();
                String pwd = etPwd.getText().toString();
                mPresenter.login(account, pwd);
                break;
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btn_reset_pwd:
                startActivity(new Intent(this, FindPwdStep1Activity.class));
                break;
        }
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
    public void invalidAccount() {
        tilAccount.setError(getString(R.string.invalid_account));
    }

    @Override
    public void invalidPwd() {
        tilPwd.setError(getString(R.string.invalid_pwd));
    }

    @Override
    public void loginSuccess() {
        showToast(R.string.login_success);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void loginFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void goBindPhone() {
        startActivity(new Intent(this, BindPhoneActivity.class));
    }
}

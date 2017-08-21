package com.yunding.dut.ui.account;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yunding.dut.R;
import com.yunding.dut.model.resp.account.RegisterResp;
import com.yunding.dut.presenter.account.RegisterPresenter;
import com.yunding.dut.ui.base.BaseActivity;
import com.yunding.dut.util.third.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类 名 称：RegisterActivity
 * <P/>描    述：注册页面
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 11:19
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 11:19
 * <P/>修改备注：
 * <P/>版    本：
 */
public class RegisterActivity extends BaseActivity implements IRegisterView {

    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.rl_login)
    RelativeLayout rlLogin;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.rl_name)
    RelativeLayout rlName;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.rl_pwd)
    RelativeLayout rlPwd;
    @BindView(R.id.line3)
    View line3;
    @BindView(R.id.imageView4)
    ImageView imageView4;
    @BindView(R.id.et_confirm_pwd)
    EditText etConfirmPwd;
    @BindView(R.id.rl_pwd_chcek)
    RelativeLayout rlPwdChcek;
    @BindView(R.id.line4)
    View line4;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tc_back)
    TextView tcBack;
    @BindView(R.id.scrollview1)
    ScrollView scrollview1;
    private RegisterPresenter mPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() == null) {

        } else {
            getSupportActionBar().hide();
        }
        mPresenter = new RegisterPresenter(this);
        tcBack.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tcBack.getPaint().setAntiAlias(true);
    }


    @Override
    public void invalidAccount() {
        ToastUtils.showShortToast(getString(R.string.invalid_account));
    }

    @Override
    public void invalidName() {
        ToastUtils.showShortToast(getString(R.string.invalid_name));
    }

    @Override
    public void invalidPwd() {
        ToastUtils.showShortToast(getString(R.string.invalid_pwd));
    }

    @Override
    public void invalidConfirmPwd() {
        ToastUtils.showShortToast(getString(R.string.invalid_pwd));
    }

    @Override
    public void pwdMismatch() {
        ToastUtils.showShortToast(getString(R.string.pwd_mismatch));
    }

    @Override
    public void registerSuccess(RegisterResp resp) {
        showToast(R.string.register_success);
        Intent intent = new Intent(this, BindPhoneActivity.class);
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

    @OnClick({R.id.btn_register, R.id.tc_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                String account = etAccount.getText().toString();
                String name = etName.getText().toString();
                String pwd = etPwd.getText().toString();
                String confirmPwd = etConfirmPwd.getText().toString();
                mPresenter.register(account, name, pwd, confirmPwd);
                break;
            case R.id.tc_back:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}

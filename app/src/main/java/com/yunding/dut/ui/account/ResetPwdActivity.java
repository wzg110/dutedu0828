package com.yunding.dut.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.yunding.dut.R;
import com.yunding.dut.presenter.account.AccountPresenter;
import com.yunding.dut.presenter.account.ResetPwdPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.util.third.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 类 名 称：ResetPwdActivity
 * <P/>描    述：重置密码
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 11:37
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 11:37
 * <P/>修改备注：
 * <P/>版    本：
 */
public class ResetPwdActivity extends ToolBarActivity implements IResetPwdView {


    @BindView(R.id.et_old_pwd)
    EditText etOldPwd;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_confirm_new_pwd)
    EditText etConfirmNewPwd;
    @BindView(R.id.btn_reset_pwd)
    Button btnResetPwd;
    private ResetPwdPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        ButterKnife.bind(this);
        setTitleInCenter(getString(R.string.register_changepwd_title));
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
        ToastUtils.showShortToast("原始密码输入有误");

    }

    @Override
    public void invalidNewPwd() {
        ToastUtils.showShortToast(getString(R.string.invalid_pwd));
    }

    @Override
    public void invalidConfirmNewPwd() {
        ToastUtils.showShortToast(getString(R.string.invalid_pwd));
    }

    @Override
    public void pwdMisMatch() {
        ToastUtils.showShortToast(getString(R.string.pwd_mismatch));
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

package com.yunding.dut.presenter.account;

import android.text.TextUtils;

import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.account.IRegisterView;

/**
 * 类 名 称：RegisterPresenter
 * <P/>描    述：注册业务逻辑
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 14:39
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 14:39
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class RegisterPresenter extends BasePresenter {

    private IRegisterView mView;

    public RegisterPresenter(IRegisterView mView) {
        this.mView = mView;
    }

    public void register(String account, String name, String pwd, String confirmPwd) {
        account = trimStr(account);
        name = trimStr(name);
        pwd = trimStr(pwd);
        confirmPwd = trimStr(confirmPwd);
        if (!isValidAccount(account)) {
            mView.invalidAccount();
            return;
        }
        if (!isValidName(name)) {
            mView.invalidName();
            return;
        }
        if (!isValidPwd(pwd)) {
            mView.invalidPwd();
            return;
        }
        if (!isValidPwd(confirmPwd)) {
            mView.invalidConfirmPwd();
            return;
        }
        if (!TextUtils.equals(pwd, confirmPwd)) {
            mView.pwdMismatch();
            return;
        }
        mView.registerSuccess();
    }
}

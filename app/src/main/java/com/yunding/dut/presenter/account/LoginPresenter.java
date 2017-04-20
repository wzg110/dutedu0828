package com.yunding.dut.presenter.account;

import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.account.ILoginView;

/**
 * 类 名 称：LoginPresenter
 * <P/>描    述：登录业务逻辑
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 14:35
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 14:35
 * <P/>修改备注：
 * <P/>版    本：
 */

public class LoginPresenter extends BasePresenter {

    ILoginView mView;

    public LoginPresenter(ILoginView mView) {
        this.mView = mView;
    }

    public void login(String account, String pwd) {
        account = trimStr(account);
        pwd = trimStr(pwd);
        if (!isValidAccount(account)) {
            mView.invalidAccount();
            return;
        }
        if (!isValidPwd(pwd)) {
            mView.invalidPwd();
            return;
        }
        mView.loginSuccess();
    }
}

package com.yunding.dut.presenter.account;

import android.text.TextUtils;

import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.account.IResetPwdView;

/**
 * 类 名 称：ResetPwdPresenter
 * <P/>描    述：重置密码业务逻辑
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 14:43
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 14:43
 * <P/>修改备注：
 * <P/>版    本：
 */

public class ResetPwdPresenter extends BasePresenter {

    private IResetPwdView mView;

    public ResetPwdPresenter(IResetPwdView mView) {
        this.mView = mView;
    }

    public void resetPwd(String oldPwd, String newPwd, String confirmNewPwd) {
        if (!isValidPwd(oldPwd)) {
            mView.invalidOldPwd();
            return;
        }
        if (!isValidPwd(newPwd)) {
            mView.invalidNewPwd();
            return;
        }
        if (!TextUtils.equals(newPwd, confirmNewPwd)) {
            mView.pwdMisMatch();
            return;
        }
        mView.resetPwdSuccess();
    }
}

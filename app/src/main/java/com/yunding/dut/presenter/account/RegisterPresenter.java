package com.yunding.dut.presenter.account;

import android.content.Context;
import android.text.TextUtils;

import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.account.RegisterResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.account.IRegisterView;
import com.yunding.dut.util.api.ApisAccount;

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

        mView.showProgress();
        String url = ApisAccount.registerUrl(name, account, confirmPwd);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                RegisterResp resp = parseJson(response, RegisterResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        saveUserInfo(resp);
                        mView.registerSuccess(resp);
                    } else {
                        mView.registerFailed(resp.getMsg());
                    }
                } else {
                    mView.registerFailed(((Context) mView).getString(R.string.server_error));
                }
            }

            @Override
            public void onError(Exception e) {
                mView.hideProgress();
                mView.registerFailed(e.toString());
            }
        });
    }

    /**
     * 功能简述:保存用户信息
     *
     * @param resp [注册接口返回]
     */
    private void saveUserInfo(RegisterResp resp) {
        DUTApplication.getUserInfo().setUserAvatar(resp.getData().getAvatarUrl());
        DUTApplication.getUserInfo().setUserPhone(resp.getData().getPhone());
        DUTApplication.getUserInfo().setUserName(resp.getData().getName());
        DUTApplication.getUserInfo().setUserClass(resp.getData().getClassName());
        DUTApplication.getUserInfo().setUserGrade(resp.getData().getRadeName());
        DUTApplication.getUserInfo().setUserId(resp.getData().getStudentId());
    }
}

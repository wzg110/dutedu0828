package com.yunding.dut.presenter.account;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.internal.Primitives;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.data.UserInfo;
import com.yunding.dut.model.resp.account.LoginResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.account.ILoginView;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.api.ApisAccount;

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

    private ILoginView mView;

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
        mView.showProgress();
        String url = ApisAccount.loginUrl(account, pwd);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                LoginResp resp = parseJson(response, LoginResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.loginSuccess();
                        UserInfo.saveUserInfo(resp);//保存用户信息
                    } else {
                        mView.loginFailed(resp.getMsg());
                    }
                } else {
                    mView.loginFailed(((Context) mView).getString(R.string.server_error));
                }
            }

            @Override
            public void onError(Exception e) {
                mView.hideProgress();
                mView.loginFailed(e.toString());
            }
        });
    }
}

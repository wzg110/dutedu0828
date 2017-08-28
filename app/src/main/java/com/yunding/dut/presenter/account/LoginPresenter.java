package com.yunding.dut.presenter.account;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.data.UserInfo;
import com.yunding.dut.model.resp.account.LoginResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.account.ILoginView;
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

    /**
     * 功能描述： 登录
     * @param account [用户名]
     * @param pwd   [密码]
     * @param userType  [用户类别]
     */
    public void login(String account, String pwd,String userType) {
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
        String url = ApisAccount.loginUrl(account, pwd,userType);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                try {
                    LoginResp resp = parseJson(response, LoginResp.class);
                    if (resp != null) {
                        if (resp.isResult()) {
                            UserInfo.saveUserInfo(resp, "正常");//保存用户信息
                            if (TextUtils.isEmpty(DUTApplication.getUserInfo().getUserPhone())) {
                                //未绑定
                                mView.goBindPhone();
                            } else {
                                //已绑定
                                mView.loginSuccess();
                            }
                        } else {
                            mView.loginFailed(resp.getMsg());
                        }
                    } else {
                        mView.loginFailed(((Context) mView).getString(R.string.server_error));
                    }
                }catch (Exception e){
                    Log.e("asd",e.getMessage().toString());
                }
            }

            @Override
            public void onError(Exception e) {
                mView.hideProgress();
                mView.loginFailed(e.toString());
            }
        });
    }

    /**
     *  功能描述： 游客二维码登录
     * @param classId   [课程Id]
     * @param teacherId [教师I]
     */
    public void visitorLogin(String classId,String teacherId,String teachingId,String schoolCode){
        mView.showProgress();
        String url = ApisAccount.visitorLogin(classId,teacherId,teachingId,schoolCode);
//        Log.e("url",url);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                LoginResp resp = parseJson(response, LoginResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
//
                        UserInfo.saveUserInfo(resp,"游客");//保存用户信息
                            //已绑定
                            mView.loginSuccess();

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

    /**
     * 功能描述：游客邀请码登录
     * @param inviteCode    [邀请码]
     */
    public void visitorLoginWithInviteCode(String inviteCode){
        mView.showProgress();
        String url = ApisAccount.visitorLoginWithInviteCode(inviteCode);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                LoginResp resp = parseJson(response, LoginResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
//
                        UserInfo.saveUserInfo(resp,"游客");//保存用户信息
                        //已绑定
                        mView.loginSuccess();

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

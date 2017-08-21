package com.yunding.dut.presenter.account;

import android.content.Context;
import android.text.TextUtils;

import com.yunding.dut.R;
import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.account.IResetPwdView;
import com.yunding.dut.util.api.ApisAccount;

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

    /**
     * 功能描述 : 重置密码
     * @param oldPwd    [旧密码]
     * @param newPwd    [新密码]
     * @param confirmNewPwd [新密码验证]
     */
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

        mView.showProgress();
        String url = ApisAccount.resetPwdUrl(oldPwd, newPwd);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.resetPwdSuccess();
                    } else {
                        mView.resetPwdFailed(resp.getMsg());
                    }
                } else {
                    mView.resetPwdFailed(((Context) mView).getString(R.string.server_error));
                }
            }

            @Override
            public void onError(Exception e) {
                mView.hideProgress();
                mView.resetPwdFailed(e.toString());
            }
        });
    }
}

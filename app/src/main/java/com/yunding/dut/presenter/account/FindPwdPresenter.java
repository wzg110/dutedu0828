package com.yunding.dut.presenter.account;

import android.content.Context;

import com.yunding.dut.R;
import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.account.IFindPwdViewStep1;
import com.yunding.dut.ui.account.IFindPwdViewStep2;
import com.yunding.dut.util.api.ApisAccount;
import com.yunding.dut.util.third.RegexUtils;

/**
 * 类 名 称：FindPwdPresenter
 * <P/>描    述：找回密码
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/5/11 15:27
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/5/11 15:27
 * <P/>修改备注：
 * <P/>版    本：
 */

public class FindPwdPresenter extends BasePresenter {

    private IFindPwdViewStep1 mView1;
    private IFindPwdViewStep2 mView2;

    public FindPwdPresenter(IFindPwdViewStep1 mView1) {
        this.mView1 = mView1;
    }

    public FindPwdPresenter(IFindPwdViewStep2 mView2) {
        this.mView2 = mView2;
    }

    /**
     * 功能简述:发送短信验证码
     *
     * @param phone [电话号码]
     */
    public void sendSmsCode(String phone) {
        if (!RegexUtils.isMobileSimple(phone)) {
            mView1.showMsg("手机号码不合法");
            return;
        }
        String url = ApisAccount.sendSmsCode(phone);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {

                    } else {
                        mView1.showMsg(resp.getMsg());
                    }
                } else {
                    mView1.showMsg(((Context) mView1).getString(R.string.server_error));
                }
            }

            @Override
            public void onError(Exception e) {
                mView1.showMsg(e.toString());
            }
        });
    }

    /**
     * 功能简述:找回密码
     *
     * @param newPwd  [新密码]
     * @param smsCode [短信验证码]
     * @param phone   [电话号码]
     */
    public void findPwd(String newPwd, String smsCode, String phone) {
        String url = ApisAccount.findPwd(newPwd, smsCode, phone);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {

                    } else {
                        mView2.showMsg(resp.getMsg());
                    }
                } else {
                    mView2.showMsg(((Context) mView1).getString(R.string.server_error));
                }
            }

            @Override
            public void onError(Exception e) {
                mView2.showMsg(e.toString());
            }
        });
    }
}

package com.yunding.dut.presenter.account;

import android.content.Context;

import com.yunding.dut.R;
import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.account.IBindPhoneView;
import com.yunding.dut.util.api.ApisAccount;
import com.yunding.dut.util.third.RegexUtils;

/**
 * 类 名 称：BindPhonePresenter
 * <P/>描    述：绑定手机号
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/5/11 16:12
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/5/11 16:12
 * <P/>修改备注：
 * <P/>版    本：
 */

public class BindPhonePresenter extends BasePresenter {

    private IBindPhoneView mView;

    public BindPhonePresenter(IBindPhoneView mView) {
        this.mView = mView;
    }

    /**
     * 功能简述:发送短信验证码
     *
     * @param phone [电话号码]
     */
    public void sendSmsCode(String phone) {
        if (!RegexUtils.isMobileSimple(phone)) {
            mView.showMsg("手机号码不合法");
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
                        mView.showMsg(resp.getMsg());
                    }
                } else {
                    mView.showMsg(((Context) mView).getString(R.string.server_error));
                }
            }

            @Override
            public void onError(Exception e) {
                mView.showMsg(e.toString());
            }
        });
    }

    public void bindPhone(String phone, String smsCode, int studentId) {
        String url = ApisAccount.bindPhone(phone, smsCode, studentId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.bindSuccess();
                    } else {
                        mView.showMsg(resp.getMsg());
                    }
                } else {
                    mView.showMsg(((Context) mView).getString(R.string.server_error));
                }
            }

            @Override
            public void onError(Exception e) {
                mView.showMsg(e.toString());
            }
        });
    }
}

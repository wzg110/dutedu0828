package com.yunding.dut.ui.account;

import com.yunding.dut.ui.base.IBaseView;

/**
 * 类 名 称：IBindPhoneView
 * <P/>描    述：绑定手机号
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/5/10 18:53
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/5/10 18:53
 * <P/>修改备注：
 * <P/>版    本：
 */

public interface IBindPhoneView extends IBaseView {

    void showMsg(String msg);

    void bindSuccess();
    void checkSuccess();
}

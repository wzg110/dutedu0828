package com.yunding.dut.ui.account;

import com.yunding.dut.ui.base.IBaseView;

/**
 * 类 名 称：ILoginView
 * <P/>描    述：登录View
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 13:30
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 13:30
 * <P/>修改备注：
 * <P/>版    本：
 */

public interface ILoginView extends IBaseView{

    void invalidAccount();

    void invalidPwd();

    void loginSuccess();

    void loginFailed(String msg);

}

package com.yunding.dut.ui.account;

import com.yunding.dut.ui.base.IBaseView;

/**
 * 类 名 称：IResetPwdView
 * <P/>描    述：重置密码页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 13:57
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 13:57
 * <P/>修改备注：
 * <P/>版    本：
 */

public interface IResetPwdView extends IBaseView{

    void invalidOldPwd();

    void invalidNewPwd();

    void invalidConfirmNewPwd();

    void pwdMisMatch();

    void resetPwdSuccess();

    void resetPwdFailed(String msg);
}

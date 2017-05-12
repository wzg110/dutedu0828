package com.yunding.dut.ui.account;

import com.yunding.dut.model.resp.account.RegisterResp;
import com.yunding.dut.ui.base.IBaseView;

/**
 * 类 名 称：IRegisterView
 * <P/>描    述：注册页面接口
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 13:55
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 13:55
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public interface IRegisterView extends IBaseView{

    void invalidAccount();

    void invalidName();

    void invalidPwd();

    void invalidConfirmPwd();

    void pwdMismatch();

    void registerSuccess(RegisterResp resp);

    void registerFailed(String msg);
}

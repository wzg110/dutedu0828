package com.yunding.dut.presenter.account;

import com.yunding.dut.app.DUTApplication;

/**
 * 类 名 称：AccountPresenter
 * <P/>描    述：账户相关的业务逻辑，可全局使用的
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 16:58
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 16:58
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class AccountPresenter {

    public static void logOut(){
        DUTApplication.getUserInfo().clearUserInfo();
    }
}

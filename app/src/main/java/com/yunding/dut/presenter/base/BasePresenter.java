package com.yunding.dut.presenter.base;

import android.text.TextUtils;

/**
 * 类 名 称：BasePresenter
 * <P/>描    述：Presenter基类
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 15:00
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 15:00
 * <P/>修改备注：
 * <P/>版    本：
 */
public class BasePresenter {

    private final int ACCOUNT_MIN_LENGTH = 4;
    private final int ACCOUNT_MAX_LENGTH = 16;
    private final int NAME_MIN_LENGTH = 2;
    private final int NAME_MAX_LENGTH = 4;
    private final int PWD_MIN_LENGTH = 6;
    private final int PWD_MAX_LENGTH = 16;

    public String trimStr(String str) {
        return str.replaceAll(" ", "");
    }

    /**
     * 功能简述:
     * @param  account [学号]
     * @return boolean [合法：true,不合法：false]
     */
    public boolean isValidAccount(String account) {
        return !(account.length() > ACCOUNT_MAX_LENGTH || account.length() < ACCOUNT_MIN_LENGTH);
    }

    /**
     * 功能简述:
     * @param  name [姓名]
     * @return boolean [合法：true,不合法：false]
     */
    public boolean isValidName(String name) {
        return !(name.length() > NAME_MAX_LENGTH || name.length() < NAME_MIN_LENGTH);
    }

    /**
     * 功能简述:
     * @param  pwd [密码]
     * @return boolean [合法：true,不合法：false]
     */
    public boolean isValidPwd(String pwd) {
        return !(pwd.length() > PWD_MAX_LENGTH || pwd.length() < PWD_MIN_LENGTH);
    }
}

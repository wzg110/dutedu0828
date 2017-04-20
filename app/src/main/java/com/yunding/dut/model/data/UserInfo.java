package com.yunding.dut.model.data;

/**
 * Created by msy on 2017/4/20.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.yunding.dut.app.DUTApplication;

/**
 * 类 名 称：UserInfo
 * <P/>描    述：存储用户信息
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/18 10:57
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/18 10:57
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class UserInfo {

    private final String USER_INFO = "user_info";

    private final String USER_ID = "user_id";
    private final String USER_AVATAR = "user_avatar";
    private final String USER_ACCOUNT = "user_account";
    private final String USER_NAME = "user_name";
    private final String USER_GRADE = "user_grade";
    private final String USER_CLASS = "user_class";

//    private long userId;
//    private String userAvatar;
//    private String userAccount;
//    private String userName;
//    private String userClass;
//    private String userGrade;

    private SharedPreferences mPreference;
    private String SPACE = "";

    public UserInfo() {
        mPreference = DUTApplication.getInstance().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
    }

    private SharedPreferences getPreferences() {
        if (mPreference == null) {
            mPreference = DUTApplication.getInstance().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        }
        return mPreference;
    }

    public long getUserId() {
        return getPreferences().getLong(USER_ID, 0);
    }

    public void setUserId(long userId) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putLong(USER_ID, userId);
        editor.apply();
    }

    public String getUserAvatar() {
        return getPreferences().getString(USER_AVATAR, SPACE);
    }

    public void setUserAvatar(String userAvatar) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(USER_AVATAR, userAvatar);
        editor.apply();
    }

    public String getUserName() {
        return getPreferences().getString(USER_NAME, SPACE);
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public String getUserClass() {
        return getPreferences().getString(USER_CLASS, SPACE);
    }

    public void setUserClass(String userClass) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(USER_CLASS, userClass);
        editor.apply();
    }

    public String getUserGrade() {
        return getPreferences().getString(USER_GRADE, SPACE);
    }

    public void setUserGrade(String userGrade) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(USER_GRADE, userGrade);
        editor.apply();
    }

    public String getUserAccount() {
        return getPreferences().getString(USER_ACCOUNT, SPACE);
    }

    public void setUserAccount(String userAccount) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(USER_ACCOUNT, userAccount);
        editor.apply();
    }

    public void clearUserInfo(){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.clear();
        editor.apply();
    }
}


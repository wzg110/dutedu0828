package com.yunding.dut.model.data;

/**
 * Created by msy on 2017/4/20.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.account.LoginResp;

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
    private final String USER_PHONE = "user_phone";
    private final String USER_TYPE = "user_type";

    public String getSCHOOL_CODE() {
        return  getPreferences().getString(SCHOOL_CODE, SPACE);
    }

    public void setSchoolCode(String string){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(SCHOOL_CODE,string
        );
        editor.apply();
    }


    private final String SCHOOL_CODE = "school_code";
    public String getUSER_TEACHING() {
        return getPreferences().getString(USER_TEACHING, SPACE);
    }
    public void setUserTeaching(String string){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(USER_TEACHING,string
        );
        editor.apply();
    }

    private final String USER_TEACHING="user_teaching";

    public String getSTUDENT_TYPE() {
        return getPreferences().getString(STUDENT_TYPE, SPACE);
    }
    public void setStudentType(String string){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(STUDENT_TYPE,string
        );
        editor.apply();
    }
    private final String STUDENT_TYPE="student_type";

    public String getUSER_HEAD() {
        return getPreferences().getString(USER_HEAD, SPACE);
    }
    public void setUserHead(String string){
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(USER_HEAD,string
        );
        editor.apply();
    }

    private final String USER_HEAD="user_head";


    public String getUSER_TYPE() {
        return getPreferences().getString(USER_TYPE, SPACE);
    }

    public void setUserType(String string) {

        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(USER_TYPE,string
        );
        editor.apply();
    }



    public String getUSER_HEAD_BG() {
        return getPreferences().getString(USER_HEAD_BG, SPACE);
    }

    public void setUserHeadBg(String string) {
            SharedPreferences.Editor editor = getPreferences().edit();

        editor.putString(USER_HEAD_BG,string
                    );
            editor.apply();
    }
    private final String USER_HEAD_BG = "user_head_bg";

    //    private long userId;
//    private String userAvatar;
//    private String userAccount;
//    private String userName;
//    private String userClass;
//    private String userGrade;
    private String userPhone;

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


    public String getUserId() {
        return getPreferences().getString(USER_ID, SPACE);
    }

    public void setUserId(String userId) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(USER_ID, userId);

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

    public String getUserPhone() {
        return getPreferences().getString(USER_PHONE, SPACE);
    }

    public void setUserPhone(String userPhone) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(USER_PHONE, userPhone);
        editor.apply();
    }

    public void clearUserInfo() {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.clear();
        editor.apply();
    }


    public static void saveUserInfo(LoginResp resp,String userType) {
        DUTApplication.getUserInfo().setUserId(resp.getData().getStudentId());
        DUTApplication.getUserInfo().setUserName(resp.getData().getName());
        DUTApplication.getUserInfo().setUserAccount(resp.getData().getStudentNo());
        DUTApplication.getUserInfo().setUserGrade(resp.getData().getGradeName());
        DUTApplication.getUserInfo().setUserClass(resp.getData().getClassName());
        DUTApplication.getUserInfo().setUserAvatar(resp.getData().getAvatarUrl());
        DUTApplication.getUserInfo().setUserPhone(resp.getData().getPhone());
        DUTApplication.getUserInfo().setUserHeadBg("");
        DUTApplication.getUserInfo().setUserType(resp.getData().getUserType());
        DUTApplication.getUserInfo().setStudentType(userType);
        DUTApplication.getUserInfo().setUserTeaching(resp.getData().getTeachingId());
        DUTApplication.getUserInfo().setSchoolCode(resp.getData().getSchoolCode());
//
    }
}


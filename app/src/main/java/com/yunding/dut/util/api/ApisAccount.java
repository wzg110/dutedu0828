package com.yunding.dut.util.api;

import com.yunding.dut.app.DUTApplication;

/**
 * 类 名 称：ApisAccount
 * <P/>描    述：登录，注册，找回密码API
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/21 17:01
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/21 17:01
 * <P/>修改备注：
 * <P/>版    本：
 */

public class ApisAccount extends Apis {

    /**
     * 功能简述:注册接口地址
     *
     * @param name      [姓名]
     * @param studentNo [学号]
     * @param pwd       [密码]
     */
    public static String registerUrl(String name, String studentNo, String pwd) {
        String url = SERVER_URL + "student/studentregin?name=" + name
                + "&studentno=" + studentNo
                + "&password=" + pwd;
        return url;
    }

    /**
     * 功能简述:登录接口地址
     *
     * @param studentNo [学号]
     * @param pwd       [密码]
     */
    public static String loginUrl(String studentNo, String pwd, String userType) {
        String url = SERVER_URL + "student/studentlogin?studentno=" + studentNo
                + "&password=" + pwd + "&userType=" + userType;
        return url;
    }

    /**
     * 功能简述:重置密码接口地址
     *
     * @param oldPwd [旧密码]
     * @param newPwd [新密码]
     */
    public static String resetPwdUrl(String oldPwd, String newPwd) {
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "student/updatepassword?password=" + oldPwd
                + "&studentid=" + userId
                + "&newpassword=" + newPwd
                + "&userType=" + DUTApplication.getUserInfo().getUSER_TYPE();
        return url;
    }

    /**
     * 功能简述:讨论组列表接口地址
     */
    public static String getDiscussionGroupUrl() {
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "student/getstudentthemelist?studentid=" + userId;
        return url;
    }

    /**
     * 功能简述:发送短信验证码
     *
     * @param phone [手机号]
     */
    public static String sendSmsCode(String phone) {
        String url = SERVER_URL + "student/sendsmscode?phone=" + phone;
        return url;
    }

    /**
     * 功能简述:检查短信验证码
     *
     * @param phone   [电话号码]
     * @param smsCode [短信验证码]
     */
    public static String checkSmsCode(String phone, String smsCode) {
        String url = SERVER_URL + "student/ischecksmscode?phone=" + phone + "&code=" + smsCode;
        return url;
    }

    /**
     * 功能简述:通过短信验证码重置密码
     *
     * @param newPwd  [新密码]
     * @param smsCode [短信验证码]
     * @param phone   [电话号码]
     */
    public static String findPwd(String newPwd, String smsCode, String phone) {
        String url = SERVER_URL + "student/updatephonepassword?newpassword=" + newPwd + "&code=" + smsCode + "&phone=" + phone;
        return url;
    }

    /**
     * 功能简述:绑定手机号
     *
     * @param phone     [手机号]
     * @param smsCode   [短信验证码]
     * @param studentId [学生id]
     */
    public static String bindPhone(String phone, String smsCode, long studentId) {
        String url = SERVER_URL + "student/updatephone?phone=" + phone + "&code=" + smsCode + "&studentid=" + studentId;
        return url;
    }

    /**
     * 功能简述:验证手机号
     *
     * @param phoneNum
     * @return
     */
    public static String checkPhone(String phoneNum) {
        String url = SERVER_URL + "student/phonevalidate?phonenum=" + phoneNum;
        return url;
    }

    /**
     * 功能描述 游客二维码登录 二维码中扫描解析数据
     *
     * @param classId
     * @param teacherId
     * @param teachingId
     * @return
     */
    public static String visitorLogin(String classId, String teacherId, String teachingId) {
        String url = SERVER_URL + "student/savevisitor?classId="
                + classId + "&teacherId=" + teacherId + "&teachingId=" + teachingId;
        return url;
    }

    /**
     * 功能描述  游客邀请码登录
     *
     * @param inviteCode
     * @return
     */
    public static String visitorLoginWithInviteCode(String inviteCode) {
        String url = SERVER_URL + "student/invitevisitor?inviteCode=" + inviteCode;
        return url;
    }
}

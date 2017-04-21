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
        String url = SERVER_URL + "/student/studentregin?name=" + name
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
    public static String loginUrl(String studentNo, String pwd) {
        String url = SERVER_URL + "/student/studentlogin?studentno=" + studentNo
                + "&password=" + pwd;
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
        String url = SERVER_URL + "/student/updatepassword?password=" + oldPwd
                + "&studentid=" + userId
                + "&newpassword=" + newPwd;
        return url;
    }

    /**
     * 功能简述:讨论组列表接口地址
     */
    public static String getDiscussionGroupUrl() {
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "/student/getstudentthemelist?studentid=" + userId;
        return url;
    }
}

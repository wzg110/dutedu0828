package com.yunding.dut.util.api;

import com.yunding.dut.app.DUTApplication;

/**
 * 类 名 称：ApisDiscuss
 * <P/>描    述：讨论组相关API
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/21 17:02
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/21 17:02
 * <P/>修改备注：
 * <P/>版    本：
 */

public class ApisDiscuss extends Apis {

    /**
     * 功能简述:主页获取讨论组列表
     */
    public static String discussGroupListUrl() {
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "/student/getstudentthemelist?studentid=" + userId;
        return url;
    }

    /**
     * 功能简述:根据讨论组id获取讨论组信息
     *
     * @param groupId [讨论组id]
     */
    public static String discussGroupInfoUrl(long groupId) {
        String url = SERVER_URL + "/student/getgroupstudents?groupid=" + groupId;
        return url;
    }

    /**
     * 功能简述:组长开启讨论
     *
     * @param subjectId [主题id]
     * @param groupId   [讨论组id]
     */
    public static String startDiscussion(long subjectId, long groupId) {
        String url = SERVER_URL + "/student/updaterelationopenstart?themeid=" + subjectId
                + "&groupid=" + groupId;
        return url;
    }

    /**
     * 功能简述:获取讨论组消息列表（轮询）
     *
     * @param subjectId [主题id]
     * @param groupId   [组id]
     */
    public static String discussGroupMsgListUrl(long subjectId, long groupId) {
        String url = SERVER_URL + "/teacher/getthemegroupmessagelist?themeid=" + subjectId
                + "&groupid=" + groupId
                + "&pageno=" + 0
                + "&pagesize=" + Integer.MAX_VALUE;
        return url;
    }

    /**
     * 功能简述:发送消息接口
     *
     * @param subjectId [参数]
     * @param groupId   [参数]
     * @param msgType   [参数]
     * @param length    [参数]
     * @param content   [参数]
     */
    public static String discussSendMsg(long subjectId, long groupId, int msgType, int length, String content) {
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "/student/addthemegroupmessage?themeid=" + subjectId
                + "&groupid=" + groupId
                + "&studentid=" + userId
                + "&messagetype=" + msgType
                + "&messagelength=" + length
                + "&content=" + content;

        return url;
    }

    /**
     * 功能简述:讨论组页面获取主题信息
     *
     * @param subjectId [主题id]
     */
    public static String getSubjectInfo(long subjectId) {
        String url = SERVER_URL + "/teacher/getthemeinfo?themeid=" + subjectId;
        return url;
    }

    /**
     * 功能简述:讨论组页面，获取主题问题
     *
     * @param subjectId [参数]
     */
    public static String getSubjectQuestion(long subjectId, long groupId) {
        String url = SERVER_URL + "/teacher/getthemetopiclist?themeid=" + subjectId + "&groupid=" + groupId;
        return url;
    }

    /**
     * 功能简述:提交答案
     *
     * @param questionId [问题id]
     * @param answer     [答案]
     * @param subjectId  [主题id]
     * @param groupId    [讨论组id]
     */
    public static String commitAnswer(long questionId, String answer, long subjectId, long groupId) {
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "/student/submitanswer?topicid=" + questionId
                + "&answer=" + answer
                + "&themeid=" + subjectId
                + "&groupid=" + groupId
                + "&studentid=" + userId;
        return url;
    }

    /**
     * 功能简述:提交答案
     *
     * @param jsonParams [答案json]
     */
    public static String commitAnswer(String jsonParams) {
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "/student/submitanswer?json=" + jsonParams;
        return url;
    }
}

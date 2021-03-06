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
        String userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "/student/getstudentthemelist?studentid=" + userId + "&userType=" + DUTApplication.getUserInfo().getUSER_TYPE();
        return url;
    }

    /**
     * 功能简述:根据讨论组id获取讨论组信息
     *
     * @param groupId [讨论组id]
     */
    public static String discussGroupInfoUrl(String groupId) {
        String url = SERVER_URL + "/student/getgroupstudents?groupid=" + groupId;
        return url;
    }

    /**
     * 功能简述:组长开启讨论
     *
     * @param subjectId [主题id]
     * @param groupId   [讨论组id]
     */
    public static String startDiscussion(String subjectId, String groupId) {
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
    public static String discussGroupMsgListUrl(String subjectId, String groupId) {
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
    public static String discussSendMsg(String subjectId, String groupId,
                                        int msgType, int length, String content) {
        String userId = DUTApplication.getUserInfo().getUserId();
        String schoolCode = DUTApplication.getUserInfo().getSCHOOL_CODE();
        String url = SERVER_URL + "/student/addthemegroupmessage?themeid=" + subjectId
                + "&groupid=" + groupId
                + "&studentid=" + userId
                + "&messagetype=" + msgType
                + "&messagelength=" + length
                + "&content=" + content
                +"&schoolCode="+schoolCode;

        return url;
    }

    /**
     * 功能简述:讨论组页面获取主题信息
     *
     * @param subjectId [主题id]
     */
    public static String getSubjectInfo(String subjectId) {
        String url = SERVER_URL + "/teacher/getthemeinfo?themeid=" + subjectId;
        return url;
    }

    /**
     * 功能简述:讨论组页面，获取主题问题
     *
     * @param subjectId [参数]
     */
    public static String getSubjectQuestion(String subjectId, String groupId) {
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
        String userId = DUTApplication.getUserInfo().getUserId();
        String schoolCode = DUTApplication.getUserInfo().getSCHOOL_CODE();
        String url = SERVER_URL + "/student/submitanswer?topicid=" + questionId
                + "&answer=" + answer
                + "&themeid=" + subjectId
                + "&groupid=" + groupId
                + "&studentid=" + userId
                +"&schoolCode="+schoolCode;
        return url;
    }

    /**
     * 功能简述:提交答案
     *
     * @param jsonParams [答案json]
     */
    public static String commitAnswer(String jsonParams) {
        String userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "student/submitanswer?json=" + jsonParams;
        return url;
    }

    /**
     * 功能简述:提交答案 post提交
     *
     * @return
     */
    public static String commitAnswer() {
        String userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "student/submitanswer";
        return url;
    }

    /**
     * 功能描述：获取服务端时间 进行倒计时 防止本地时间与服务时间有差距
     *
     * @return
     */
    public static String serverTime() {

        String url = SERVER_URL + "student/servertime";
        return url;
    }

    /**
     * 功能描述：更改讨论组结束状态
     *
     * @param groupid
     * @param themeid
     * @return
     */
    public static String changeDissTA(String groupid, String themeid) {

        String url = SERVER_URL + "student/updaterelationshutstart?groupid=" + groupid + "&themeid=" + themeid;
        return url;
    }
}

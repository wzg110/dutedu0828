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
     * 功能简述:获取讨论组消息列表（轮询）
     *
     * @param subjectId [主题id]
     * @param groupId   [组id]
     */
    public static String discussGroupMsgListUrl(long subjectId, long groupId) {
        String url = SERVER_URL + "/teacher/getthemegroupmessagelist?themeid=" + subjectId
                + "&groupid=" + groupId;
        return url;
    }

    public static String discussSendMsg(int msgType, long subjectId, long groupId) {
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "/student/addthemegroupmessage?fileurl=123&messagetype=" + msgType
                + "&messagelength=13&themeid=" + subjectId
                + "&groupid=" + groupId
                + "&studentid=" + userId;
        return url;
    }
}

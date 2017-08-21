package com.yunding.dut.util.api;

import com.yunding.dut.app.DUTApplication;

/**
 * 类 名 称：ApisMeInfo
 * <P/>描    述：个人中心相关API
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/27 14:41
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/27 14:41
 * <P/>修改备注：
 * <P/>版    本：
 */

public class ApisMeInfo extends Apis {
    /**
     * 功能描述 上传头像
     *
     * @return
     */
    public static String getUploadAvatarUrl() {
        String url = SERVER_URL + "student/updateavatarurl";
        return url;
    }

    /**
     * 游客可以修改姓名
     *
     * @param name
     * @return
     */
    public static String changeLoginName(String name) {
        String url = SERVER_URL + "student/editvisitorname?studentId=" +
                DUTApplication.getUserInfo().getUserId() + "&name=" + name;
        return url;
    }
}

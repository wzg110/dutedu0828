package com.yunding.dut.util.api;

import com.yunding.dut.app.DUTApplication;

/**
 * 类 名 称：ApisReading
 * <P/>描    述：阅读部分API
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/24 16:09
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/24 16:09
 * <P/>修改备注：
 * <P/>版    本：
 */

public class ApisReading extends Apis {

    public static String getReadingList() {
        long userId = DUTApplication.getUserInfo().getUserId();
//        String url = "http://172.16.0.78:8080/www-0.0.1/reading/courselist?studentId=" + 1
//                + "&page=" + 1
//                + "&pagesize=" + Integer.MAX_VALUE;
        String url = SERVER_URL + "/reading/courselist?studentId=" + userId
                + "&page=" + 1
                + "&pagesize=" + Integer.MAX_VALUE;
        return url;
    }

    public static String commitReadingQuestion(String questionId, String answerContent, long answerTime, int backTime) {
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "/reading/saveanswer?studentId=" + userId
                + "&questionId=" + questionId
                + "&answerContent=" + answerContent
                + "&answerTime=" + answerTime
                + "&backTime=" + backTime;
        return url;
    }
}

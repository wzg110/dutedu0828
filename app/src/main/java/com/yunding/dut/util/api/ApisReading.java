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

    public static String markerWords(String courseId, int wordIndex, int wordLength, String word) {
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "/reading/savenewword?studentId=" + userId
                + "&courseId=" + courseId
                + "&wordIndex=" + wordIndex
                + "&wordLength=" + wordLength
                + "&word=" + word;
        return url;
    }

    public static String recordReadingTime(String courseId, int sentenceIndex, String fromSentenceIndex, long stayTime, int articleFinish) {
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "/reading/savereadingtime?studentId=" + userId
                + "&courseId=" + courseId
                + "&sentenceIndex=" + sentenceIndex
                + "&fromSentenceIndex=" + fromSentenceIndex
                + "&stayTime=" + stayTime
                + "&articleFinish=" + articleFinish;
        return url;
    }
}

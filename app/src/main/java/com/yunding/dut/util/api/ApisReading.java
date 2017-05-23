package com.yunding.dut.util.api;

import android.util.Log;

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
    private static final String TAG = "ApisReading";
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
        Log.e(TAG, "commitReadingQuestion: "+url );
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

    public static String collectWords(String english, String courseid, String characters) {
//        http://localhost:8080/edu/reading/addcollecation?english=why&courseid=1&characters=为什么&studentid=1
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = "http://172.16.0.88:8080/edu/" + "/reading/addcollecation?english=" + english+"&courseid="+courseid+
                "&characters="+characters+
                "&studentid="+userId;
        Log.e(TAG, "collectWords: "+url );
        return url;
    }
    public static String delCollectWord(int collectionid) {
//        http://localhost:8080/edu/reading/delcollecation?collectionid=3
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = "http://172.16.0.88:8080/edu/" + "/reading/delcollecation?collectionid=" + collectionid;
        Log.e(TAG, "delCollectWord: "+url );
        return url;
    }
    public static String getCollectWords() {
//        http://localhost:8080/edu/reading/getcollecationlist?studentid=1
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = "http://172.16.0.88:8080/edu/" + "/reading/getcollecationlist?studentid=" + userId;
        Log.e(TAG, "getCollectWords: "+url );
        return url;
    }


}

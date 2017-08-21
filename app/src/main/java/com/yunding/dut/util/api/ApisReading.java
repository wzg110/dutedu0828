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
    private static final String TAG = "ApisReading";

    /**
     * 功能描述： 获取阅读课列表
     *
     * @return
     */
    public static String getReadingList() {
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "reading/courselist?studentId=" + userId
                + "&page=" + 1
                + "&pagesize=" + Integer.MAX_VALUE + "&userType=" + DUTApplication.getUserInfo().getUSER_TYPE();
        return url;
    }

    /**
     * 提交阅读课小题答案get
     *
     * @param questionId
     * @param answerContent
     * @param answerTime
     * @param backTime
     * @param classId
     * @return
     */
    public static String commitReadingQuestion(String questionId, String answerContent,
                                               long answerTime, int backTime, String classId) {

        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "reading/saveanswer?studentId=" + userId
                + "&questionId=" + questionId
                + "&answerContent=" + answerContent
                + "&answerTime=" + answerTime
                + "&backTime=" + backTime + "&classId=" + classId;
//        Log.e(TAG, "commitReadingQuestion: "+url );
        return url;
    }

    /**
     * 提交阅读课小题答案post
     *
     * @return
     */
    public static String commitReadingQuestion() {

        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "reading/saveanswer";
//        Log.e(TAG, "commitReadingQuestion: "+url );
        return url;
    }

    /**
     * 标记生词
     *
     * @param courseId
     * @param wordIndex
     * @param wordLength
     * @param word
     * @param classId
     * @param wordColor
     * @return
     */
    public static String markerWords(String courseId, int wordIndex, int wordLength, String word, String classId, String wordColor) {
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "reading/savenewword?studentId=" + userId
                + "&courseId=" + courseId
                + "&wordIndex=" + wordIndex
                + "&wordLength=" + wordLength
                + "&word=" + word
                + "&classId=" + classId
                + "&wordColor=" + wordColor;
        return url;
    }

    /**
     * 删除标记
     *
     * @param newWordId
     * @return
     */
    public static String deleteMarks(String newWordId) {
        String url = SERVER_URL + "reading/deletenewword?newWordId=" + newWordId;

        return url;
    }

    /**
     * 修改标记子颜色
     *
     * @param newWordId
     * @param color
     * @return
     */
    public static String changeNewWordBG(String newWordId, String color) {
        String url = SERVER_URL + "reading/changewordcolor?newWordId=" + newWordId
                + "&wordColor=" + color;

        return url;
    }

    /**
     * 保存标记笔记
     *
     * @param newWordId
     * @param noteContent
     * @return
     */
    public static String saveWordNotes(String newWordId, String noteContent) {
        String url = SERVER_URL + "reading/savereadingnote?newWordId=" + newWordId
                + "&noteContent=" + noteContent;

        return url;
    }

    /**
     * 提交逐行阅读时间
     *
     * @param courseId
     * @param sentenceIndex
     * @param fromSentenceIndex
     * @param stayTime
     * @param articleFinish
     * @param classId
     * @return
     */
    public static String recordReadingTime(String courseId, int sentenceIndex, String fromSentenceIndex, long stayTime, int articleFinish, String classId) {
        long userId = DUTApplication.getUserInfo().getUserId();

        String url = SERVER_URL + "reading/savereadingtime?studentId=" + userId
                + "&courseId=" + courseId
                + "&sentenceIndex=" + sentenceIndex
                + "&fromSentenceIndex=" + fromSentenceIndex
                + "&stayTime=" + stayTime
                + "&articleFinish=" + articleFinish
                + "&classId=" + classId;
        return url;
    }

    /**
     * 添加收藏生词
     *
     * @param english
     * @param courseid
     * @param characters
     * @param newwordid
     * @return
     */
    public static String collectWords(String english, String courseid, String characters, String newwordid) {
//        http://localhost:8080/edu/reading/addcollecation?english=why&courseid=1&characters=为什么&studentid=1
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "reading/addcollecation?english=" + english + "&courseid=" + courseid +
                "&characters=" + characters +
                "&studentid=" + userId +
                "&newwordid=" + newwordid;
//        Log.e(TAG, "collectWords: "+url );
        return url;
    }

    /**
     * 删除收藏生词
     *
     * @param collectionid
     * @return
     */
    public static String delCollectWord(int collectionid) {
//        http://localhost:8080/edu/reading/delcollecation?collectionid=3
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "reading/delcollecation?collectionid=" + collectionid;
//        Log.e(TAG, "delCollectWord: "+url );
        return url;
    }

    /**
     * 获取收藏生词列表
     *
     * @return
     */
    public static String getCollectWords() {
//        http://localhost:8080/edu/reading/getcollecationlist?studentid=1
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "reading/getcollecationlist?studentid=" + userId + "&userType=" + DUTApplication.getUserInfo().getUSER_TYPE();
//        Log.e(TAG, "getCollectWords: "+url );
        return url;
    }

    /**
     * 记录退出阅读课位置
     *
     * @param courseId
     * @param classId
     * @param sentenceIndex
     * @return
     */
    public static String exitReading(String courseId, String classId, int sentenceIndex) {
//        http://localhost:8080/edu/reading/getcollecationlist?studentid=1
        long userId = DUTApplication.getUserInfo().getUserId();
        String url = SERVER_URL + "reading/exitreading?studentId=" + userId
                + "&courseId=" + courseId
                + "&classId=" + classId
                + "&sentenceIndex=" + sentenceIndex;
//        Log.e(TAG, "getCollectWords: "+url );
        return url;
    }

    /**
     * 阅读结束进行数据统计
     *
     * @param classId
     * @param courseId
     * @return
     */
    public static String showReadingData(String classId, String courseId) {
        String url = SERVER_URL + "reading/showreadingdata?studentId=" + DUTApplication.getUserInfo().getUserId()
                + "&courseId=" + courseId
                + "&classId=" + classId;
        return url;
    }


}

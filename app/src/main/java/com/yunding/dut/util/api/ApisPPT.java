package com.yunding.dut.util.api;

import com.yunding.dut.app.DUTApplication;

/**
 * 类 名 称：ApiPPT
 * <P/>描    述：PPT模块API
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 13:18
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 13:18
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class ApisPPT extends Apis {

    /**
     * 功能简述:获取课程列表
     *
     * @return String [课程列表访问地址]
     */
    public static String getCourseList(String teachingId) {
        String url = SERVER_URL + "pptteaching/getteachingppts?studentId=" +
                DUTApplication.getUserInfo().getUserId() + "&teachingId=" + teachingId;
        return url;
    }

    /**
     * 功能简述:获取PPT列表
     *
     * @param courseId [课程id]
     * @return String [PPT列表访问地址]
     */
    public static String getPPTList(int courseId) {
        String url = TEST_URL + "edupptapp/getedupptinfolist?teachingid=" + courseId;
        return url;
    }

    /**
     * 功能简述:获取小题详情
     *
     * @param subjectId [题目id]
     * @return String [小题详情]
     */
    public static String getQuestion(String subjectId) {
        String url = TEST_URL + "edupptapp/getsubjectlist"
                + "?subjectid=" + subjectId
                + "&studentid=" + DUTApplication.getUserInfo().getUserId();
        return url;
    }

    /**
     * 功能简述:提交答案
     *
     * @param questionId    [问题id]
     * @param answerContent [答案内容]
     * @return String [提交答案接口地址]
     */
    public static String commitAnswer(int questionId, String answerContent) {
        String url = TEST_URL + "edupptapp/backanswercontent"
                + "?answerid=" + questionId
                + "&content=" + answerContent;
        return url;
    }

    /**
     * 功能简述:添加阅读PPT日志
     *
     * @param subjectId [题目id]
     * @return 添加日志接口地址 [返回类型说明]
     */
    public static String addReadingLog(String subjectId) {
        String url = TEST_URL + "edupptapp/backanswer"
                + "?studentid=" + DUTApplication.getUserInfo().getUserId()
                + "&subjectid=" + subjectId;
        return url;
    }

    /**
     * 功能简述:获取PPT列表日志
     *
     * @param teachingId
     * @return
     */
    public static String getPPTslidesList(String teachingId) {
        String url = TEST_URL + "pptteaching/getslides?studentId=" + DUTApplication.getUserInfo().getUserId() +
                "&teachingId=" + teachingId;
        return url;
    }

    /**
     * 功能简述:学生签到
     *
     * @param teachingId
     * @param specialityId
     * @param classId
     * @param latitude
     * @param longitude
     * @return
     */
    public static String signIn(String teachingId, String specialityId,
                                String classId, String latitude, String longitude) {
        String schoolCode = DUTApplication.getUserInfo().getSCHOOL_CODE();
        String url = TEST_URL + "pptteaching/signin?studentId=" + DUTApplication.getUserInfo().getUserId() +
                "&teachingId=" + teachingId +
                "&specialityId=" + specialityId +
                "&classId=" + classId +
                "&latitude=" + latitude +
                "&longitude=" + longitude
                +"&schoolCode="+schoolCode;
        return url;
    }

    /**
     * 查询是否显示答案解析
     *
     * @param slideId
     * @param questionId
     * @return
     */
    public static String getRefreshAnalysis(String slideId, String questionId) {
        String url = TEST_URL + "pptteaching/refreshanalysis?slideId=" + slideId +
                "&questionId=" + questionId;
        return url;
    }

    /**
     * 提交答案get
     *
     * @param slideId
     * @param questionId
     * @param teachingId
     * @param selfTaughtId
     * @param content
     * @param longTime
     * @return
     */
    public static String savePPTAnswer(String slideId, String questionId,
                                       String teachingId, String selfTaughtId,
                                       String content, long longTime) {
        String url = Apis.TEST_URL + "pptteaching/saveanswer?studentId=" + DUTApplication.getUserInfo().getUserId() +
                "&slideId=" + slideId + "&questionId=" + questionId + "&teachingId=" + teachingId
                + "&selfTaughtId=" + selfTaughtId + "&content=" + content + "&longTime=" + longTime;
        return url;
    }

    /**
     * 提交答案post
     *
     * @return
     */
    public static String savePPTAnswer() {
        String url = Apis.TEST_URL + "pptteaching/saveanswer";
        return url;
    }

    /**
     * 自动提交PPT答案
     *
     * @param slideId
     * @param teachingId
     * @return
     */
    public static String savePPTAnswerAuto(String slideId, String teachingId) {
        String schoolCode = DUTApplication.getUserInfo().getSCHOOL_CODE();
        String url = Apis.TEST_URL + "pptteaching/saveanswerauto?studentId=" + DUTApplication.getUserInfo().getUserId() +

                "&teachingId=" + teachingId + "&slideId=" + slideId+"&schoolCode="+schoolCode;
        return url;
    }

    /**
     * 计算PPT停留时间
     *
     * @param teachingSlideId
     * @param selfTaughtId
     * @param slideId
     * @param classId
     * @param teachingId
     * @param startTime
     * @param endTime
     * @return
     */
    public static String savePPTBrowerStayTime(String teachingSlideId, String selfTaughtId, String slideId
            , String classId, String teachingId, String startTime, String endTime) {
        String schoolCode = DUTApplication.getUserInfo().getSCHOOL_CODE();
        String url = Apis.TEST_URL + "pptteaching/savepptbrowsestaytime?studentId=" + DUTApplication.getUserInfo().getUserId() +
                "&teachingSlideId=" + teachingSlideId +
                "&selfTaughtId=" + selfTaughtId +
                "&slideId=" + slideId +
                "&classId=" + classId +
                "&teachingId=" + teachingId +
                "&startTime=" + startTime +
                "&endTime=" + endTime
                +"&schoolCode="+schoolCode;
        return url;
    }

    /**
     * 获取自学列表ppt数据
     *
     * @param teachingId
     * @param longitude
     * @param latitude
     * @param classId
     * @param studyMode
     * @return
     */
    public static String getSelftaughtslides(String teachingId, String longitude,
                                             String latitude, String classId, int studyMode) {
        String schoolCode = DUTApplication.getUserInfo().getSCHOOL_CODE();
        String url = Apis.TEST_URL + "pptteaching/getselftaughtslides?studentId=" + DUTApplication.getUserInfo().getUserId() +

                "&classId=" + classId +
                "&teachingId=" + teachingId +
                "&longitude=" + longitude +
                "&latitude=" + latitude +
                "&studyMode=" + studyMode
                +"&schoolCode="+schoolCode;

        return url;
    }

    /**
     * 老师预览课堂列表
     *
     * @return
     */

    public static String getCourseTeacherList() {
        String url = SERVER_URL + "pptteaching/getpreviewppts?teacherId=" +
                DUTApplication.getUserInfo().getUserId();
        return url;
    }

    /**
     * 功能简述:老师预览获取PPT列表
     *
     * @param teachingId
     * @return
     */
    public static String getPPTslidesTeacherList(String teachingId) {
        String url = TEST_URL + "pptteaching/getpreviewslides?teachingId=" + teachingId;

        return url;
    }

    /**
     * 老师预览删除课堂listitem
     *
     * @param teachingId
     * @return
     */
    public static String deleteCourseTeacher(String teachingId) {
        String url = SERVER_URL + "pptteaching/deletepreviewppts?teacherId=" +
                DUTApplication.getUserInfo().getUserId() + "&teachingId=" + teachingId;
        return url;
    }

    /**
     * 轮训ppt图片
     *
     * @param teachingId
     * @param slideId
     * @return
     */
    public static String pollingPPTImage(String teachingId, String slideId) {
        String url = SERVER_URL + "pptteaching/getlatestslide?teachingId=" + teachingId
                + "&slideId=" + slideId;
        return url;
    }

    /**
     * 针对PPT的反馈
     *
     * @param teachingId
     * @param slideId
     * @param questionId
     * @param classId
     * @param selfTaughtId
     * @param replyContent
     * @return
     */
    public static String sendFeedBack(String teachingId,
                                      String slideId,
                                      String questionId,
                                      String classId,
                                      String selfTaughtId,
                                      String replyContent) {
        String schoolCode = DUTApplication.getUserInfo().getSCHOOL_CODE();
        String url = SERVER_URL + "pptteaching/savestureply?studentId=" + DUTApplication.getUserInfo().getUserId()
                + "&teachingId=" + teachingId + "&slideId=" + slideId + "&questionId=" + questionId + "&classId=" + classId +
                "&selfTaughtId=" + selfTaughtId + "&replyContent=" + replyContent+"&schoolCode="+schoolCode;
        return url;
    }

    /**
     * 轮询PPT小题
     *
     * @param teachingId
     * @param slideId
     * @param loadedQuantity
     * @param loadedQuestionIds
     * @return
     */
    public static String pollingPPTQuestion(String teachingId,
                                            String slideId,
                                            String loadedQuantity,
                                            String loadedQuestionIds) {
        String url = SERVER_URL + "pptteaching/refreshslidequestions?loadedQuantity=" + loadedQuantity
                + "&teachingId=" + teachingId + "&slideId=" + slideId + "&loadedQuestionIds=" + loadedQuestionIds;
        return url;
    }

    /**
     * 自动提交小题答案
     *
     * @param questionId
     * @param teachingId
     * @return
     */
    public static String autoAnswerSingle(String questionId, String teachingId) {
        String schoolCode = DUTApplication.getUserInfo().getSCHOOL_CODE();
        String url = SERVER_URL + "pptteaching/savetimeoutanswerauto?studentId=" + DUTApplication.getUserInfo().getUserId()
                + "&teachingId=" + teachingId + "&questionId=" + questionId+"&schoolCode="+schoolCode;
        return url;
    }

    /**
     * 轮询答案发布
     *
     * @param questionId
     * @return
     */
    public static String pollingAnalysisFlag(String questionId) {
        String url = SERVER_URL + "pptteaching/queryanalysis?questionId=" + questionId;
        return url;
    }
}

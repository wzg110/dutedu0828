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
     * @param pageSize  [每页条数]
     * @param pageIndex [页号]
     * @return String [课程列表访问地址]
     */
    public static String getCourseList(int pageSize, int pageIndex) {
        String url = TEST_URL + "/edupptapp/getedupptteachinglistbystudent"
                + "?pagesize=" + pageSize
                + "&pageindex=" + pageIndex
                + "&studentid=" + DUTApplication.getUserInfo().getUserId();
        return url;
    }

    /**
     * 功能简述:获取PPT列表
     *
     * @param courseId [课程id]
     * @return String [PPT列表访问地址]
     */
    public static String getPPTList(int courseId) {
        String url = TEST_URL + "/edupptapp/getedupptinfolist?teachingid=" + courseId;
        return url;
    }

    /**
     * 功能简述:获取小题详情
     *
     * @param subjectId [题目id]
     * @return String [小题详情]
     */
    public static String getQuestion(int subjectId) {
        String url = TEST_URL + "/edupptapp/getsubjectlist"
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
        String url = TEST_URL + "/edupptapp/backanswercontent"
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
    public static String addReadingLog(int subjectId) {
        String url = TEST_URL + "/edupptapp/backanswer"
                + "?studentid=" + DUTApplication.getUserInfo().getUserId()
                + "&subjectid=" + subjectId;
        return url;
    }
}

package com.yunding.dut.model.resp.collect;

import java.util.List;

/**
 * 类 名 称：CollectAllWordsResp
 * <P/>描    述：收藏列表
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:11
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:11
 * <P/>修改备注：
 * <P/>版    本：
 */

public class CollectAllWordsResp {


    /**
     * data : [{"characters":"Ϊʲô","collectionId":8,"courseId":1,"courseName":"Think of life as a game","createTime":"2017-05-23 10:28:52","english":"why","studentId":15000018}]
     * errors : {}
     * msg : ok
     * result : true
     * version : 1.0
     */

    private ErrorsBean errors;
    private String msg;
    private boolean result;
    private String version;
    private List<DataBean> data;

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(ErrorsBean errors) {
        this.errors = errors;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class ErrorsBean {
    }

    public static class DataBean {
        /**
         * characters : Ϊʲô
         * collectionId : 8
         * courseId : 1
         * courseName : Think of life as a game
         * createTime : 2017-05-23 10:28:52
         * english : why
         * studentId : 15000018
         */

        private String characters;
        private String collectionId;
        private String courseId;
        private String courseName;
        private String createTime;
        private String english;
        private String studentId;

        public String getCharacters() {
            return characters;
        }

        public void setCharacters(String characters) {
            this.characters = characters;
        }

        public String getCollectionId() {
            return collectionId;
        }

        public void setCollectionId(String collectionId) {
            this.collectionId = collectionId;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getEnglish() {
            return english;
        }

        public void setEnglish(String english) {
            this.english = english;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }
    }
}

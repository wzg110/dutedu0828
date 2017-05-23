package com.yunding.dut.model.resp.collect;

import java.util.List;

/**
 * Created by Administrator on 2017/5/23.
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
        private int collectionId;
        private int courseId;
        private String courseName;
        private String createTime;
        private String english;
        private int studentId;

        public String getCharacters() {
            return characters;
        }

        public void setCharacters(String characters) {
            this.characters = characters;
        }

        public int getCollectionId() {
            return collectionId;
        }

        public void setCollectionId(int collectionId) {
            this.collectionId = collectionId;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
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

        public int getStudentId() {
            return studentId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }
    }
}

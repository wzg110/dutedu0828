package com.yunding.dut.model.resp.ppt;

import java.io.Serializable;
import java.util.List;

/**
 * 类 名 称：CourseListResp
 * <P/>描    述：课程列表
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 13:44
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 13:44
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class CourseListResp {


    /**
     * data : [{"endDate":"2017-07-07 11:35:00","fileName":"英语unit1","speciality":"英语阅读与写作","startDate":"2017-07-07 11:15:00","status":2,"studyMode":1,"teachingId":1},{"endDate":"2017-07-07 11:35:00","fileName":"英语unit2","speciality":"英语阅读与写作","startDate":"2017-07-07 11:15:00","status":2,"studyMode":0,"teachingId":2},{"fileName":"英语阅读（二）UNIT2.pptx","speciality":"机械原理","startDate":"2017-07-15 11:01:24","status":1,"studyMode":0,"teachingId":59},{"endDate":"2017-07-15 11:03:18","fileName":"英语阅读（二）UNIT2.pptx","speciality":"机械原理","startDate":"2017-07-15 11:02:52","status":2,"studyMode":0,"teachingId":60}]
     * errors : {}
     * msg : ok
     * result : true
     * version : 20170715, linhai
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

    public static class DataBean implements Serializable{
        public String getSpecialityId() {
            return specialityId;
        }

        public void setSpecialityId(String specialityId) {
            this.specialityId = specialityId;
        }

        /**
         * endDate : 2017-07-07 11:35:00
         * fileName : 英语unit1
         * speciality : 英语阅读与写作
         * startDate : 2017-07-07 11:15:00
         * status : 2
         * studyMode : 1
         * teachingId : 1
         */
private  String specialityId;

        public String getClassId() {
            return classId;
        }

        public void setClassId(String classId) {
            this.classId = classId;
        }

        private  String classId;
        private String endDate;
        private String fileName;
        private String speciality;
        private String startDate;
        private int status;
        private int studyMode;
        private String teachingId;

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        private  String  cover;
        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getPlatformTime() {
            return platformTime;
        }

        public void setPlatformTime(String platformTime) {
            this.platformTime = platformTime;
        }

        private  String teacherName;
        private String platformTime;

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getSpeciality() {
            return speciality;
        }

        public void setSpeciality(String speciality) {
            this.speciality = speciality;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStudyMode() {
            return studyMode;
        }

        public void setStudyMode(int studyMode) {
            this.studyMode = studyMode;
        }

        public String getTeachingId() {
            return teachingId;
        }

        public void setTeachingId(String teachingId) {
            this.teachingId = teachingId;
        }
    }
}

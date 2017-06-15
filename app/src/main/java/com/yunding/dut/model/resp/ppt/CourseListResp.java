package com.yunding.dut.model.resp.ppt;

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
     * data : {"datas":[{"classId":3,"className":"英语口语","createTime":"2017-06-07 15:45:07","gradeId":6,"gradeName":"英语一班","id":2,"status":0},{"classId":1,"className":"软件技术","createTime":"2017-06-05 19:47:56","gradeId":1,"gradeName":"一班","id":1,"status":0}],"total":2}
     * errors : {}
     * msg : ok
     * result : true
     * version : 20170607,linfeng
     */

    private DataBean data;
    private ErrorsBean errors;
    private String msg;
    private boolean result;
    private String version;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {
        /**
         * datas : [{"classId":3,"className":"英语口语","createTime":"2017-06-07 15:45:07","gradeId":6,"gradeName":"英语一班","id":2,"status":0},{"classId":1,"className":"软件技术","createTime":"2017-06-05 19:47:56","gradeId":1,"gradeName":"一班","id":1,"status":0}]
         * total : 2
         */

        private int total;
        private List<DatasBean> datas;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<DatasBean> getDatas() {
            return datas;
        }

        public void setDatas(List<DatasBean> datas) {
            this.datas = datas;
        }

        public static class DatasBean {
            /**
             * classId : 3
             * className : 英语口语
             * createTime : 2017-06-07 15:45:07
             * gradeId : 6
             * gradeName : 英语一班
             * id : 2
             * status : 0
             */

            private int classId;
            private String className;
            private String createTime;
            private int gradeId;
            private String gradeName;
            private int id;
            private int status;

            public int getClassId() {
                return classId;
            }

            public void setClassId(int classId) {
                this.classId = classId;
            }

            public String getClassName() {
                return className;
            }

            public void setClassName(String className) {
                this.className = className;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public int getGradeId() {
                return gradeId;
            }

            public void setGradeId(int gradeId) {
                this.gradeId = gradeId;
            }

            public String getGradeName() {
                return gradeName;
            }

            public void setGradeName(String gradeName) {
                this.gradeName = gradeName;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }

    public static class ErrorsBean {
    }
}

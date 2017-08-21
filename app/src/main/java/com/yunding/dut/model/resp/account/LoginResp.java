package com.yunding.dut.model.resp.account;

import com.google.gson.annotations.SerializedName;

/**
 * 类 名 称：LoginResp
 * <P/>描    述：登录接口返回
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/21 12:10
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/21 12:10
 * <P/>修改备注：
 * <P/>版    本：
 */

public class LoginResp {


    /**
     * data : {"age":0,"avatarUrl":"123123123123","classId":1,"className":"科目1 ","createTime":"2017-04-18 13:42:12","loginCount":35,"loginEquipment":0,"loginTime":"2017-04-19 14:37:31","name":"雷雨","passWord":"","radeId":1,"radeName":"哈哈","sex":0,"studentId":1,"studentNo":"123124355"}
     * errors : {}
     * msg : ok
     * result : true
     * version : 20170418, leiyu
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
        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getTeachingId() {
            return teachingId;
        }

        public void setTeachingId(String teachingId) {
            this.teachingId = teachingId;
        }

        /**
         * age : 0
         * avatarUrl : 123123123123
         * classId : 1
         * className : 科目1
         * createTime : 2017-04-18 13:42:12
         * loginCount : 35
         * loginEquipment : 0
         * loginTime : 2017-04-19 14:37:31
         * name : 雷雨
         * passWord :
         * radeId : 1
         * radeName : 哈哈
         * sex : 0
         * studentId : 1
         * studentNo : 123124355
         */
        private String teachingId;
        private String userType;
        private long age;
        private String avatarUrl;
        private int classId;
        private String className;
        private String createTime;
        private int loginCount;
        private int loginEquipment;
        private String loginTime;
        private String name;
        private String passWord;
        private String phone;
        @SerializedName("radeId")
        private int gradeId;
        @SerializedName("radeName")
        private String gradeName;
        private int sex;
        private int studentId;
        private String studentNo;

        public long getAge() {
            return age;
        }

        public void setAge(long age) {
            this.age = age;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

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

        public int getLoginCount() {
            return loginCount;
        }

        public void setLoginCount(int loginCount) {
            this.loginCount = loginCount;
        }

        public int getLoginEquipment() {
            return loginEquipment;
        }

        public void setLoginEquipment(int loginEquipment) {
            this.loginEquipment = loginEquipment;
        }

        public String getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(String loginTime) {
            this.loginTime = loginTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassWord() {
            return passWord;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
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

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getStudentId() {
            return studentId;
        }

        public void setStudentId(int studentId) {
            this.studentId = studentId;
        }

        public String getStudentNo() {
            return studentNo;
        }

        public void setStudentNo(String studentNo) {
            this.studentNo = studentNo;
        }
    }

    public static class ErrorsBean {
    }
}

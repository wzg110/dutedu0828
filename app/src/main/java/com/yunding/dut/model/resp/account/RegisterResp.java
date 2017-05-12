package com.yunding.dut.model.resp.account;

import java.io.Serializable;

/**
 * 类 名 称：RegisterResp
 * <P/>描    述：注册接口返回
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/5/12 10:02
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/5/12 10:02
 * <P/>修改备注：
 * <P/>版    本：
 */

public class RegisterResp implements Serializable{

    /**
     * data : {"age":0,"avatarUrl":"upload/imgs/moren.jpg","classId":3,"className":"科目2","createTime":"2017-05-12 09:53:02","loginCount":0,"loginEquipment":0,"loginTime":"2017-05-12 09:53:02","name":"天明","passWord":"","phone":"","radeId":1,"radeName":"2017年级","sex":0,"studentId":123,"studentNo":"111111"}
     * errors : {}
     * msg : ok
     * result : true
     * version : 20160426,leiyu
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

    public static class DataBean implements Serializable{
        /**
         * age : 0
         * avatarUrl : upload/imgs/moren.jpg
         * classId : 3
         * className : 科目2
         * createTime : 2017-05-12 09:53:02
         * loginCount : 0
         * loginEquipment : 0
         * loginTime : 2017-05-12 09:53:02
         * name : 天明
         * passWord :
         * phone :
         * radeId : 1
         * radeName : 2017年级
         * sex : 0
         * studentId : 123
         * studentNo : 111111
         */

        private int age;
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
        private int radeId;
        private String radeName;
        private int sex;
        private int studentId;
        private String studentNo;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
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

        public String getPassWord() {
            return passWord;
        }

        public void setPassWord(String passWord) {
            this.passWord = passWord;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getRadeId() {
            return radeId;
        }

        public void setRadeId(int radeId) {
            this.radeId = radeId;
        }

        public String getRadeName() {
            return radeName;
        }

        public void setRadeName(String radeName) {
            this.radeName = radeName;
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

    public static class ErrorsBean implements Serializable{
    }
}

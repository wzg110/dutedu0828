package com.yunding.dut.model.resp.discuss;


import java.util.List;

/**
 * 类 名 称：DiscussGroupInfoResp
 * <P/>描    述：讨论组信息返回结果
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/21 19:12
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/21 19:12
 * <P/>修改备注：
 * <P/>版    本：
 */
public class DiscussGroupInfoResp {


    /**
     * {
     * "data": {
     * "students": [
     * {
     * "avatarUrl": "123",     // 头像
     * "isLeader": 1,       	//  0 普通学生  1 组长
     * "studentId": "1",    	// 学生ID
     * "studentName": "雷雨"  // 学生名称
     * },
     * {
     * "avatarUrl": "",
     * "isLeader": 0,
     * "studentId": "2",
     * "studentName": "林枫"
     * }
     * ],
     * "group": {
     * "classId": 1,  // 班级ID
     * "className": "科目1", // 班级名称
     * "createTime": "2017-04-21 14:07:14",  // 创建时间
     * "createUserId": 1,  //创建人
     * "gradeName": "哈哈", //年级ID
     * "groupId": 1, 组ID
     * "name": "爆破组", 组名称
     * "radeId": 1 年级ID
     * }
     * },
     * "errors": {},
     * "msg": "ok",
     * "result": true,
     * "version": "20170418, leiyu"
     * }
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
         * students : [{"avatarUrl":"123","isLeader":1,"studentId":"1","studentName":"雷雨"},{"avatarUrl":"","isLeader":0,"studentId":"2","studentName":"林枫"}]
         * group : {"classId":1,"className":"科目1","createTime":"2017-04-21 14:07:14","createUserId":1,"gradeName":"哈哈","groupId":1,"name":"爆破组","radeId":1}
         */

        private GroupBean group;
        private List<StudentsBean> students;

        public GroupBean getGroup() {
            return group;
        }

        public void setGroup(GroupBean group) {
            this.group = group;
        }

        public List<StudentsBean> getStudents() {
            return students;
        }

        public void setStudents(List<StudentsBean> students) {
            this.students = students;
        }

        public static class GroupBean {
            /**
             * classId : 1
             * className : 科目1
             * createTime : 2017-04-21 14:07:14
             * createUserId : 1
             * gradeName : 哈哈
             * groupId : 1
             * name : 爆破组
             * radeId : 1
             */

            private String classId;
            private String className;
            private String createTime;
            private String createUserId;
            private String gradeName;
            private String groupId;
            private String name;
            private String radeId;

            public String getClassId() {
                return classId;
            }

            public void setClassId(String classId) {
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

            public String getCreateUserId() {
                return createUserId;
            }

            public void setCreateUserId(String createUserId) {
                this.createUserId = createUserId;
            }

            public String getGradeName() {
                return gradeName;
            }

            public void setGradeName(String gradeName) {
                this.gradeName = gradeName;
            }

            public String getGroupId() {
                return groupId;
            }

            public void setGroupId(String groupId) {
                this.groupId = groupId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRadeId() {
                return radeId;
            }

            public void setRadeId(String radeId) {
                this.radeId = radeId;
            }
        }

        public static class StudentsBean {
            /**
             * avatarUrl : 123
             * isLeader : 1
             * studentId : 1
             * studentName : 雷雨
             */

            private String avatarUrl;
            private int isLeader;
            private String studentId;
            private String studentName;

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }

            public int getIsLeader() {
                return isLeader;
            }

            public void setIsLeader(int isLeader) {
                this.isLeader = isLeader;
            }

            public String getStudentId() {
                return studentId;
            }

            public void setStudentId(String studentId) {
                this.studentId = studentId;
            }

            public String getStudentName() {
                return studentName;
            }

            public void setStudentName(String studentName) {
                this.studentName = studentName;
            }
        }
    }

    public static class ErrorsBean {
    }
}

package com.yunding.dut.model.resp.discuss;

import java.io.Serializable;
import java.util.List;

/**
 * 类 名 称：DiscussListResp
 * <P/>描    述：讨论组列表返回结果
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 17:42
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 17:42
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DiscussListResp implements Serializable{


    /**
     * data : [{"content":"","countdownTime":15,"endTime":"2017-04-21","groupId":"1","isLeader":1,"num":"2","openingTime":"2017-04-20","state":"2","studentId":"1","studentName":"雷雨","themeId":"2","themeName":"wodezhuti123"}]
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

    public static class DataBean implements Serializable{
        /**
         * "content": "kaokaokaokao",   // 最新聊天记录
         * "countdownTime": 26,  // 答题限制分数
         * "endTime": "2017-04-19", // 答题结束时间
         * "groupId": "1",  // 组ID
         * "isLeader": 1, // 是否为组长
         * "num": "2", // 主题讨论人数
         * "openingTime": "2017-04-20", // 开启时间
         * "state": "2",  // 0 未开启  1开始 2 结束
         * "studentId": "1", //学生ID
         * "studentName": "雷雨", // 学生名称
         * "themeId": "1",  //主题ID
         * "themeName": "wodezhuti119 // 主题名称"
         */

        private String content;
        private int countdownTime;
        private String endTime;
        private long groupId;
        private String groupName;
        private int isLeader;
        private int num;
        private String openingTime;
        private int state;
        private long studentId;
        private String studentName;
        private long themeId;
        private String themeName;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getCountdownTime() {
            return countdownTime;
        }

        public void setCountdownTime(int countdownTime) {
            this.countdownTime = countdownTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public long getGroupId() {
            return groupId;
        }

        public void setGroupId(long groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public int getIsLeader() {
            return isLeader;
        }

        public void setIsLeader(int isLeader) {
            this.isLeader = isLeader;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getOpeningTime() {
            return openingTime;
        }

        public void setOpeningTime(String openingTime) {
            this.openingTime = openingTime;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public long getStudentId() {
            return studentId;
        }

        public void setStudentId(long studentId) {
            this.studentId = studentId;
        }

        public String getStudentName() {
            return studentName;
        }

        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }

        public long getThemeId() {
            return themeId;
        }

        public void setThemeId(long themeId) {
            this.themeId = themeId;
        }

        public String getThemeName() {
            return themeName;
        }

        public void setThemeName(String themeName) {
            this.themeName = themeName;
        }
    }
}

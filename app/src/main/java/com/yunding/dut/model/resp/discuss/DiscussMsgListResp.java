package com.yunding.dut.model.resp.discuss;

import java.util.List;

/**
 * 类 名 称：DiscussMsgListResp
 * <P/>描    述：讨论消息列表返回结果
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/22 14:08
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/22 14:08
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DiscussMsgListResp {

    /**
     * data : {"datas":[{"content":"你乱写就乱写","createTime":"2017-04-20 14:14:47","fileUrl":"","groupId":1,"messageId":4,"messageLength":5,"messageType":0,"studentId":2,"themeId":1},{"content":"kaokaokaokao","createTime":"2017-04-20 16:42:02","fileUrl":"","groupId":1,"messageId":5,"messageLength":0,"messageType":0,"studentId":1,"themeId":1},{"content":"kaokaokaokao","createTime":"2017-04-20 16:45:05","fileUrl":"","groupId":1,"messageId":6,"messageLength":15,"messageType":0,"studentId":1,"themeId":1},{"content":"kaokaokaokao","createTime":"2017-04-20 16:45:06","fileUrl":"","groupId":1,"messageId":7,"messageLength":15,"messageType":0,"studentId":1,"themeId":1},{"content":"kaokaokaokao","createTime":"2017-04-20 16:45:07","fileUrl":"","groupId":1,"messageId":8,"messageLength":15,"messageType":0,"studentId":1,"themeId":1},{"content":"kaokaokaokao","createTime":"2017-04-20 16:45:38","fileUrl":"www.baidu.com","groupId":1,"messageId":9,"messageLength":15,"messageType":1,"studentId":1,"themeId":1}],"total":6}
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
        /**
         * datas : [{"content":"你乱写就乱写","createTime":"2017-04-20 14:14:47","fileUrl":"","groupId":1,"messageId":4,"messageLength":5,"messageType":0,"studentId":2,"themeId":1},{"content":"kaokaokaokao","createTime":"2017-04-20 16:42:02","fileUrl":"","groupId":1,"messageId":5,"messageLength":0,"messageType":0,"studentId":1,"themeId":1},{"content":"kaokaokaokao","createTime":"2017-04-20 16:45:05","fileUrl":"","groupId":1,"messageId":6,"messageLength":15,"messageType":0,"studentId":1,"themeId":1},{"content":"kaokaokaokao","createTime":"2017-04-20 16:45:06","fileUrl":"","groupId":1,"messageId":7,"messageLength":15,"messageType":0,"studentId":1,"themeId":1},{"content":"kaokaokaokao","createTime":"2017-04-20 16:45:07","fileUrl":"","groupId":1,"messageId":8,"messageLength":15,"messageType":0,"studentId":1,"themeId":1},{"content":"kaokaokaokao","createTime":"2017-04-20 16:45:38","fileUrl":"www.baidu.com","groupId":1,"messageId":9,"messageLength":15,"messageType":1,"studentId":1,"themeId":1}]
         * total : 6
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
             * {
             * "content": "我就是乱写",     //消息内容
             * "createTime": "2017-04-20 14:14:17", // 创建时间
             * "fileUrl": "",      // 音频 使用此字段
             * "groupId": 1,   // 讨论组ID
             * "messageId": 3,  // 消息ID
             * "messageLength": 5, // 消息长度
             * "messageType": 0, // 消息类型(0-文字 1-语音 2-图片)
             * "studentId": 1,  // 学生ID
             * "themeId": 1  // 主题ID
             * }
             */

            private String content;
            private String createTime;
            private String fileUrl;
            private int groupId;
            private int messageId;
            private int messageLength;
            private int messageType;
            private int studentId;
            private int themeId;
            private String avatarUrl;
            private String studentName;
            private int state;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getFileUrl() {
                return fileUrl;
            }

            public void setFileUrl(String fileUrl) {
                this.fileUrl = fileUrl;
            }

            public int getGroupId() {
                return groupId;
            }

            public void setGroupId(int groupId) {
                this.groupId = groupId;
            }

            public int getMessageId() {
                return messageId;
            }

            public void setMessageId(int messageId) {
                this.messageId = messageId;
            }

            public int getMessageLength() {
                return messageLength;
            }

            public void setMessageLength(int messageLength) {
                this.messageLength = messageLength;
            }

            public int getMessageType() {
                return messageType;
            }

            public void setMessageType(int messageType) {
                this.messageType = messageType;
            }

            public int getStudentId() {
                return studentId;
            }

            public void setStudentId(int studentId) {
                this.studentId = studentId;
            }

            public int getThemeId() {
                return themeId;
            }

            public void setThemeId(int themeId) {
                this.themeId = themeId;
            }

            public String getAvatarUrl() {
                return avatarUrl;
            }

            public void setAvatarUrl(String avatarUrl) {
                this.avatarUrl = avatarUrl;
            }

            public String getStudentName() {
                return studentName;
            }

            public void setStudentName(String studentName) {
                this.studentName = studentName;
            }

            public int getState() {
                return state;
            }

            public void setState(int state) {
                this.state = state;
            }
        }
    }

    public static class ErrorsBean {
    }
}

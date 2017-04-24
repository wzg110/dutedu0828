package com.yunding.dut.model.resp.discuss;

import java.util.List;

/**
 * 类 名 称：DiscussQuestionParam
 * <P/>描    述：讨论组答题，统一提交参数
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/24 13:02
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/24 13:02
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DiscussQuestionParam {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * themeId : 1
         * topicId : 1
         * groupId : 1
         * studentId : 1
         * answer : 123
         */

        private String themeId;
        private String topicId;
        private String groupId;
        private String studentId;
        private String answer;

        public String getThemeId() {
            return themeId;
        }

        public void setThemeId(String themeId) {
            this.themeId = themeId;
        }

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }
}

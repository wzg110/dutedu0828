package com.yunding.dut.model.resp.discuss;

import java.io.Serializable;
import java.util.List;

/**
 * 类 名 称：DiscussQuestionListResp
 * <P/>描    述：主题对应的所有题目返回
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/23 17:10
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/23 17:10
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class DiscussQuestionListResp implements Serializable{


    /**
     * data : [{"answer":"不知道怎么回答","content":"信与不信在于实际","createTime":"2017-04-20 13:46:14","createUserId":1,"selectOptions":"","themeId":1,"topicId":4,"type":1},{"answer":"A","content":"世界有多少个国家","createTime":"2017-04-20 13:47:04","createUserId":1,"selectOptions":"a:16个,b:20个,c:30个","themeId":1,"topicId":5,"type":"0类型 0-选择题 1填空题"}]
     * errors : {}
     * msg : ok
     * result : true
     * version : 20170418, leiyu
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

    public static class ErrorsBean implements Serializable{
    }

    public static class DataBean implements Serializable{
        public String getUserAnswer() {
            return userAnswer;
        }

        public void setUserAnswer(String userAnswer) {
            this.userAnswer = userAnswer;
        }

        /**
         * {
         * "answer": "不知道怎么回答",     // 正确答案
         * "content": "信与不信在于实际",  // 题目内容
         * "createTime": "2017-04-20 13:46:14", // 创建时间
         * "createUserId": 1, // 创建人
         * "selectOptions": "",  // 当type = 0 的时候使用
         * "themeId": 1, // 主题ID
         * "topicId": 4, // 题ID
         * "type": 1 // 类型 0-选择题 1填空题
         * },
         * {
         * "answer": "A", // 正确答案
         * "content": "世界有多少个国家",   // 题目内容
         * "createTime": "2017-04-20 13:47:04", // 创建时间
         * "createUserId": 1, // 创建人
         * "selectOptions": "a:16个,b:20个,c:30个",  // 多选题 逗号分隔
         * "themeId": 1,  // 主题ID
         * "topicId": 5, // 题ID
         * "type": 0类型 0-选择题 1填空题
         * }
         */
        private String userAnswer;
        private String answer;
        private String content;
        private String createTime;
        private String createUserId;
        private String selectOptions;
        private String themeId;
        private String topicId;
        private int type;
        private String studentAnswer;

        public List<InputBlank> getBlanksInfo() {
            return blanksInfo;
        }

        public void setBlanksInfo(List<InputBlank> blanksInfo) {
            this.blanksInfo = blanksInfo;
        }

        private List<InputBlank>blanksInfo;
        public static class InputBlank implements Serializable{
            public int getIndex() {
                return index;
            }

            public void setIndex(int index) {
                this.index = index;
            }

            public int getLength() {
                return length;
            }

            public void setLength(int length) {
                this.length = length;
            }

            private int index;
            private int length;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

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

        public String getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(String createUserId) {
            this.createUserId = createUserId;
        }

        public String getSelectOptions() {
            return selectOptions;
        }

        public void setSelectOptions(String selectOptions) {
            this.selectOptions = selectOptions;
        }

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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getStudentAnswer() {
            return studentAnswer;
        }

        public void setStudentAnswer(String studentAnswer) {
            this.studentAnswer = studentAnswer;
        }
    }
}

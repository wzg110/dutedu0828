package com.yunding.dut.model.resp.ppt;

import java.util.List;

/**
 * 类 名 称：QuestionInfoResp
 * <P/>描    述：PPT对应题目
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 13:46
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 13:46
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class QuestionInfoResp {

    /**
     * data : [{"answerShow":1,"content":"测试时候时还是说","createTime":"2017-06-06 19:06:50","id":1,"oKAnswer":"测试时候时还是说","pPTId":8,"replyType":1,"tId":12,"teachingId":3,"timeLongs":100,"title":"测试时候时还是说"}]
     * errors : {}
     * msg : ok
     * result : true
     * version : 20170607,linfeng
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
         * answerShow : 1
         * content : 测试时候时还是说
         * createTime : 2017-06-06 19:06:50
         * id : 1
         * oKAnswer : 测试时候时还是说
         * pPTId : 8
         * replyType : 1
         * tId : 12
         * teachingId : 3
         * timeLongs : 100
         * title : 测试时候时还是说
         */

        private int answerShow;
        private String content;
        private String createTime;
        private int id;
        private String oKAnswer;
        private int pPTId;
        private int replyType;
        private int tId;
        private int teachingId;
        private int timeLongs;
        private String title;
        private String localAnswer;

        public int getAnswerShow() {
            return answerShow;
        }

        public void setAnswerShow(int answerShow) {
            this.answerShow = answerShow;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOKAnswer() {
            return oKAnswer;
        }

        public void setOKAnswer(String oKAnswer) {
            this.oKAnswer = oKAnswer;
        }

        public int getPPTId() {
            return pPTId;
        }

        public void setPPTId(int pPTId) {
            this.pPTId = pPTId;
        }

        public int getReplyType() {
            return replyType;
        }

        public void setReplyType(int replyType) {
            this.replyType = replyType;
        }

        public int getTId() {
            return tId;
        }

        public void setTId(int tId) {
            this.tId = tId;
        }

        public int getTeachingId() {
            return teachingId;
        }

        public void setTeachingId(int teachingId) {
            this.teachingId = teachingId;
        }

        public int getTimeLongs() {
            return timeLongs;
        }

        public void setTimeLongs(int timeLongs) {
            this.timeLongs = timeLongs;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLocalAnswer() {
            return localAnswer;
        }

        public void setLocalAnswer(String localAnswer) {
            this.localAnswer = localAnswer;
        }
    }
}

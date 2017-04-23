package com.yunding.dut.model.resp.discuss;

/**
 * 类 名 称：DiscussSubjectResp
 * <P/>描    述：讨论主题信息
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/23 17:30
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/23 17:30
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DiscussSubjectResp {

    /**
     * data : {"content":"wozhengzaiceshishujku","countdownTime":26,"createTime":"2017-04-19 15:37:09","createUserId":1,"isLanguage":0,"name":"wodezhuti119","start":1,"themeId":1}
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
         * {
         * "data": {
         * "content": "wozhengzaiceshishujku",
         * "countdownTime": 26,
         * "createTime": "2017-04-19 15:37:09",
         * "createUserId": 1,
         * "isLanguage": 0,
         * "name": "wodezhuti119",
         * "start": 1,
         * "themeId": 1
         * },
         * "errors": {},
         * "msg": "ok",
         * "result": true,
         * "version": "20170418, leiyu"
         * }
         */

        private String content;
        private int countdownTime;
        private String createTime;
        private int createUserId;
        private int isLanguage;
        private String name;
        private String start;
        private long themeId;

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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getCreateUserId() {
            return createUserId;
        }

        public void setCreateUserId(int createUserId) {
            this.createUserId = createUserId;
        }

        public int getIsLanguage() {
            return isLanguage;
        }

        public void setIsLanguage(int isLanguage) {
            this.isLanguage = isLanguage;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public long getThemeId() {
            return themeId;
        }

        public void setThemeId(long themeId) {
            this.themeId = themeId;
        }
    }

    public static class ErrorsBean {
    }
}

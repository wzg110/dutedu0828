package com.yunding.dut.model.resp.appupdate;

/**
 * 类 名 称：versionUpdateResp
 * <P/>描    述：版本更新返回
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:11
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:11
 * <P/>修改备注：
 * <P/>版    本：
 */

public class versionUpdateResp {

    /**
     * data : {"updatable":1,"updatemode":0}
     * errors : {}
     * msg : ok
     * result : true
     * version : 20170729, linhai
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
         * updatable : 1
         * updatemode : 0
         */

        private int updatable;
        private int updatemode;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        private String content;
        private int textSize;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        private String version;

        public int getUpdatable() {
            return updatable;
        }

        public void setUpdatable(int updatable) {
            this.updatable = updatable;
        }

        public int getUpdatemode() {
            return updatemode;
        }

        public void setUpdatemode(int updatemode) {
            this.updatemode = updatemode;
        }
    }

    public static class ErrorsBean {
    }
}


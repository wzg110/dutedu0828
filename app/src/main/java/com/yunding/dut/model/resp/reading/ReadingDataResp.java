package com.yunding.dut.model.resp.reading;

import java.io.Serializable;

/**
 * 类 名 称：ReadingDataResp
 * <P/>描    述： 阅读数据统计返回
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:16
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:16
 * <P/>修改备注：
 * <P/>版    本：
 */

public class ReadingDataResp {


    /**
     * data : {"notesQuantity":5,"readingSpeed":55.649,"timeConsumed":3.127,"timeConsumedDesc":"3分7秒","wordsCollected":5,"wordsQuantity":174}
     * errors : {}
     * msg : ok
     * result : true
     * version : 20170727, linhai
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
         * notesQuantity : 5
         * readingSpeed : 55.649
         * timeConsumed : 3.127
         * timeConsumedDesc : 3分7秒
         * wordsCollected : 5
         * wordsQuantity : 174
         */

        private int notesQuantity;
        private String readingSpeed;
        private String timeConsumed;
        private String timeConsumedDesc;
        private int wordsCollected;
        private int wordsQuantity;

        public int getNotesQuantity() {
            return notesQuantity;
        }

        public void setNotesQuantity(int notesQuantity) {
            this.notesQuantity = notesQuantity;
        }

        public String getReadingSpeed() {
            return readingSpeed;
        }

        public void setReadingSpeed(String readingSpeed) {
            this.readingSpeed = readingSpeed;
        }

        public String getTimeConsumed() {
            return timeConsumed;
        }

        public void setTimeConsumed(String timeConsumed) {
            this.timeConsumed = timeConsumed;
        }

        public String getTimeConsumedDesc() {
            return timeConsumedDesc;
        }

        public void setTimeConsumedDesc(String timeConsumedDesc) {
            this.timeConsumedDesc = timeConsumedDesc;
        }

        public int getWordsCollected() {
            return wordsCollected;
        }

        public void setWordsCollected(int wordsCollected) {
            this.wordsCollected = wordsCollected;
        }

        public int getWordsQuantity() {
            return wordsQuantity;
        }

        public void setWordsQuantity(int wordsQuantity) {
            this.wordsQuantity = wordsQuantity;
        }
    }

    public static class ErrorsBean {
    }
}

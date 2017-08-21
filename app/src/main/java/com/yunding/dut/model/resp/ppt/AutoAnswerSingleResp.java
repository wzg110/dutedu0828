package com.yunding.dut.model.resp.ppt;

/**
 * 类 名 称：AutoAnswerSingleResp
 * <P/>描    述：单个小题提交答案返回
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:14
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:14
 * <P/>修改备注：
 * <P/>版    本：
 */

public class AutoAnswerSingleResp {

    /**
     * data : {"answerTimeLimit":1213,"analysis":"第一页填空题解析","answerContent":"['']","blanksInfo":[{"index":6,"length":2}],"questionCompleted":1,"questionContent":"第一页填空题_不错啊","questionId":1928,"questionIndex":1,"questionType":1,"rightAnswer":"['答案']","sendAnalysisFlag":0,"slideId":256,"teachingId":771}
     * errors : {}
     * msg : ok
     * result : true
     * version : 20170810, linhai
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

    public static class DataBean extends PPTResp.DataBean.slideQuestionsBean {

    }

    public static class ErrorsBean {
    }
}

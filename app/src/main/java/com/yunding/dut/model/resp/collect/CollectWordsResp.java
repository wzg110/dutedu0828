package com.yunding.dut.model.resp.collect;

/**
 * 类 名 称：CollectWordsResp
 * <P/>描    述：收藏单词返回
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:12
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:12
 * <P/>修改备注：
 * <P/>版    本：
 */

public class CollectWordsResp {


    /**
     * errors : {}
     * msg : ok
     * result : true
     * version : 1.0
     */

    private ErrorsBean errors;
    private String msg;
    private boolean result;
    private String version;

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

    public static class ErrorsBean {
    }
}

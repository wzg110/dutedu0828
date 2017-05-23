package com.yunding.dut.model.resp.collect;

/**
 * Created by Administrator on 2017/5/23.
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

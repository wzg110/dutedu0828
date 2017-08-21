package com.yunding.dut.model.resp;

/**
 * 类 名 称：CommonRespNew
 * <P/>描    述： 通用数据返回带data值
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:17
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:17
 * <P/>修改备注：
 * <P/>版    本：
 */

public class CommonRespNew {
    private ErrorsBean errors;
    private String msg;
    private boolean result;
    private String version;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

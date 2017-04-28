package com.yunding.dut.model.resp;

/**
 * 类 名 称：CommonResp
 * <P/>描    述：所有接口通用返回
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/21 15:22
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/21 15:22
 * <P/>修改备注：
 * <P/>版    本：
 */

public class UploadAvatarResp {


    /**
     * errors : {}
     * msg : 您输入的学号不存在
     * result : false
     * version : 20160426,leiyu
     */

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

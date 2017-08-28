package com.yunding.dut.model.resp.ppt;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * 类 名 称：PPTListResp
 * <P/>描    述：PPT列表
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 13:45
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 13:45
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class PPTListResp implements Serializable{

    /**
     * data : [{"createTime":"2017-06-06 18:10:31","id":3,"pPTImgUrl":"upload/pptimgs/2017/06/06/1496743828125.jpg","subjectId":1},{"createTime":"2017-06-06 18:12:56","id":4,"pPTImgUrl":"upload/pptimgs/2017/06/06/1496743980124.jpg"},{"createTime":"2017-06-06 18:15:21","id":5,"pPTImgUrl":"upload/pptimgs/2017/06/06/1496744125307.png"},{"createTime":"2017-06-06 18:16:29","id":6,"pPTImgUrl":"upload/pptimgs/2017/06/06/1496744193256.jpg"},{"createTime":"2017-06-06 18:26:20","id":7,"pPTImgUrl":"upload/pptimgs/2017/06/06/1496744784049.jpg"},{"createTime":"2017-06-06 19:06:50","id":8,"pPTImgUrl":"upload/pptimgs/2017/06/06/1496747213860.jpg"}]
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

    public static class ErrorsBean implements Serializable{
    }

    public static class DataBean implements Serializable{
        public String getPageString() {
            return pageString;
        }

        public void setPageString(String pageString) {
            this.pageString = pageString;
        }

        /**
         * createTime : 2017-06-06 18:10:31
         * id : 3
         * pPTImgUrl : upload/pptimgs/2017/06/06/1496743828125.jpg
         * subjectId : 1
         */
        private String pageString;//页码
        private String createTime;
        private String id;
        @SerializedName("pPTImgUrl")
        private String PPTImgUrl;
        private String subjectId;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPPTImgUrl() {
            return PPTImgUrl;
        }

        public void setPPTImgUrl(String PPTImgUrl) {
            this.PPTImgUrl = PPTImgUrl;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }
    }
}

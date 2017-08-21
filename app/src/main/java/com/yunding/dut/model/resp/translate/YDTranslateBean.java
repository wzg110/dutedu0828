package com.yunding.dut.model.resp.translate;

import java.util.List;
/**
 * 类 名 称：YDTranslateBean
 * <P/>描    述： 有道翻译返回
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:17
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:17
 * <P/>修改备注：
 * <P/>版    本：
 */

public class YDTranslateBean {


    /**
     * translation : ["我们是一家人"]
     * basic : {"explains":["我们是一家人（歌曲名）；左麟右李（电影名）"]}
     * query : we are family
     * errorCode : 0
     * web : [{"value":["左麟右李","我们是一家人","天涯若比邻"],"key":"We are family"},{"value":["我们是一家","于天龙","我们是一家人"],"key":"we are one family"},{"value":["我们是一个大家庭","我们是一个家族"],"key":"We are a family"}]
     */

    private BasicBean basic;
    private String query;
    private int errorCode;
    private List<String> translation;
    private List<WebBean> web;

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public List<WebBean> getWeb() {
        return web;
    }

    public void setWeb(List<WebBean> web) {
        this.web = web;
    }

    public static class BasicBean {
        private List<String> explains;

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }
    }

    public static class WebBean {
        /**
         * value : ["左麟右李","我们是一家人","天涯若比邻"]
         * key : We are family
         */

        private String key;
        private List<String> value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }
}

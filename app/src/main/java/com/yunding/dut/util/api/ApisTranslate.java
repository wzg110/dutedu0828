package com.yunding.dut.util.api;

/**
 * Created by Administrator on 2017/5/17.
 */

public class ApisTranslate {

    //金山翻译
    public static String JSTRANSLATE ="http://dict-co.iciba.com/api/dictionary.php?key=8B17241EC63D9726D14C003A966370C9" ;
    public static String getJSTRANSLATE(String content){
        return JSTRANSLATE+"&w="+content+"&type=json";
    }

    public static String YDTRANSLATE = "http://fanyi.youdao.com/openapi.do?keyfrom=FFHome&key=2070942143&type=data&doctype=json&version=1.1&q=";
    public static String getYDTRANSLATE(String content){
        return YDTRANSLATE+content;
    }


}

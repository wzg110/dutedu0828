package com.yunding.dut.util.api;

/**
 * 类 名 称：ApisReading
 * <P/>描    述：阅读部分API
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/24 16:09
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/24 16:09
 * <P/>修改备注：
 * <P/>版    本：
 */

public class ApisReading extends Apis {

    public static String getReadingList(long studentId) {
//        String url = "http://172.168.137.1:8080/www-0.0.1/reading/courselist?studentId=" + studentId
//                + "&page=" + 1
//                + "&pagesize=" + Integer.MAX_VALUE;
        String url = "http://172.16.0.68:8080/www-0.0.1/reading/courselist?StudentId=1&Page=1&Pagesize=10";
        return url;
    }
}

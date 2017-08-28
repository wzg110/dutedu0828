package com.yunding.dut.model.resp.account;

/**
 * 类 名 称：dutedu
 * <P/>描    述：
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15
 * <P/>修改备注：
 * <P/>版    本：
 */

public class QRCResp {
    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    private String teacherId;
    private String  classId;

    public String getTeachingId() {
        return teachingId;
    }

    public void setTeachingId(String teachingId) {
        this.teachingId = teachingId;
    }

    private  String teachingId;

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    private String schoolCode;
}

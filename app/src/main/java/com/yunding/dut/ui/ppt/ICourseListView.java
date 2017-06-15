package com.yunding.dut.ui.ppt;

import com.yunding.dut.model.resp.ppt.CourseListResp;
import com.yunding.dut.ui.base.IBaseView;

/**
 * 类 名 称：ICourseListView
 * <P/>描    述：课程列表View
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 14:30
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 14:30
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public interface ICourseListView extends IBaseView {

    void showCourseList(CourseListResp resp);

    void showNoData();

    void showListFailed();
}

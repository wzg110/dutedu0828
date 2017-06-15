package com.yunding.dut.presenter.ppt;

import com.yunding.dut.model.resp.ppt.CourseListResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.ppt.ICourseListView;
import com.yunding.dut.util.api.ApisPPT;

/**
 * 类 名 称：CourseListPresenter
 * <P/>描    述：课程列表控制器
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 14:29
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 14:29
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class CourseListPresenter extends BasePresenter {

    private ICourseListView mView;

    public CourseListPresenter(ICourseListView mView) {
        this.mView = mView;
    }

    public void loadCourseList() {
        mView.showProgress();
        String url = ApisPPT.getCourseList(1000, 1);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                CourseListResp resp = parseJson(response, CourseListResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        if (resp.getData().getDatas().size() == 0) {
                            mView.showNoData();
                        } else {
                            mView.showCourseList(resp);
                        }
                    } else {
                        mView.showListFailed();
                    }
                } else {
                    mView.showListFailed();
                }
            }

            @Override
            public void onError(Exception e) {
                mView.hideProgress();
                mView.showListFailed();
            }
        });
    }
}

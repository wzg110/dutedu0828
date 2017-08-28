package com.yunding.dut.presenter.ppt;

import android.util.Log;

import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.ppt.IPPTListView;
import com.yunding.dut.util.api.ApisPPT;

/**
 * 类 名 称：PPTListPresenter
 * <P/>描    述：PPT列表控制器
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 16:17
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 16:17
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class PPTListPresenter extends BasePresenter {

    private IPPTListView mView;

    public PPTListPresenter(IPPTListView mView) {
        this.mView = mView;
    }

    /**
     * ppt列表
     * @param courseId
     */

    public void loadPPTList(String courseId) {
        mView.showProgress();
        String url = ApisPPT.getPPTslidesList(courseId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                Log.e("sadasdasdads",response);
                mView.hideProgress();
                PPTResp resp = parseJson(response, PPTResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        if (resp.getData().size() == 0) {
                            mView.showNoData();
                        } else {
                            mView.showPPTList(resp);
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

    /**
     * 签到
     * @param teachingId
     * @param specialityId
     * @param classId
     * @param latitude
     * @param longitude
     */
    public void signIn(String teachingId,String specialityId,
                       String classId,String latitude,String longitude) {
        String url = ApisPPT.signIn(teachingId,specialityId,classId,latitude,longitude);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                    } else {
                    }
                } else {

                }
            }
            @Override
            public void onError(Exception e) {

            }
        });

    }

    /**
     * pptlist老师
     * @param courseId
     */
    public void loadPPTListTeacher(String courseId) {
        mView.showProgress();
        String url = ApisPPT.getPPTslidesTeacherList(courseId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                PPTResp resp = parseJson(response, PPTResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        if (resp.getData().size() == 0) {
                            mView.showNoData();
                        } else {
                            mView.showPPTList(resp);
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
    public void sendFeedBack(String teachingId,
                             String slideId,
                             String questionId,
                             String classId,
                             String selfTaughtId,
                             String replyContent){
        mView.showProgress();
        String url=ApisPPT.sendFeedBack(teachingId,slideId,questionId,classId,selfTaughtId,replyContent);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.showMsg("发送成功");

                    } else {
                        mView.showMsg(resp.getMsg());

                    }
                }else{

                }
            }

            @Override
            public void onError(Exception e) {

                mView.showMsg(e.getMessage());
                mView.hideProgress();
            }
        });

    }
}

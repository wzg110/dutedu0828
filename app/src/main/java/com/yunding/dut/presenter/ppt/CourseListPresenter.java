package com.yunding.dut.presenter.ppt;

import android.util.Log;

import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.model.resp.appupdate.versionUpdateResp;
import com.yunding.dut.model.resp.ppt.CourseListResp;
import com.yunding.dut.model.resp.ppt.pptSelfListResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.ppt.ICourseListView;
import com.yunding.dut.util.api.ApisPPT;
import com.yunding.dut.util.api.ApisVersionUpdate;

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

    /**
     * 功能描述 ：加载课程列表
     */
    public void loadCourseList(String teachingId) {
        mView.showProgress();
        String url = ApisPPT.getCourseList(teachingId);
        Log.e("asdas",url);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                Log.e("asdas",response);
                mView.hideProgress();
                try{
                    CourseListResp resp = parseJson(response, CourseListResp.class);


                if (resp != null) {
                    if (resp.isResult()) {
                        if (resp.getData().size() == 0) {
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
                }catch(Exception e){
                    Log.e("eeeee",e.getMessage());
                }
            }

            @Override
            public void onError(Exception e) {
                mView.hideProgress();
                mView.showListFailed();
            }
        },this);
    }

    /**
     * 功能描述：加载PPTList列表
     * @param teachingId    [课程主键]
     * @param longitude     [经度]
     * @param latitude      [维度]
     * @param classId       [班级ID]
     * @param studyMode     [学习模式]
     */
    public void loadPPTList(String teachingId,String longitude,
                            String latitude,String classId,int studyMode) {
        mView.showDiyProgress();
        String url = ApisPPT.getSelftaughtslides(teachingId,longitude,latitude,classId,studyMode);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideDiyProgress();
                pptSelfListResp resp = parseJson(response, pptSelfListResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        if (resp.getData().getSlides().size()== 0) {
                            mView.showMsg();
                        } else {
                            mView.showPPTList(resp);
                        }
                    } else {
                        mView.showMsg();
                    }
                } else {
                    mView.showMsg();
                }
            }

            @Override
            public void onError(Exception e) {
                mView.hideDiyProgress();
//                mView.showListFailed();
            }
        });
    }

    /**
     *     功能描述： 签到
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
     * 老师预览list
     */
    public void loadCourseTeacherList() {
        mView.showProgress();
        String url = ApisPPT.getCourseTeacherList();
//        Log.e("老师预览listrequest",url);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
//                Log.e("老师预览response",response);
                mView.hideProgress();
                CourseListResp resp = parseJson(response, CourseListResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        if (resp.getData().size() == 0) {
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

    /**
     * 老师预览删除
     * @param teachingId
     */
    public void deleteCourseListItem(String  teachingId) {
        mView.showProgress();
        String url = ApisPPT.deleteCourseTeacher(teachingId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.deleteSuccess();
                    } else {
                        mView.showMsg();
                    }
                } else {
                    mView.showMsg();
                }
            }

            @Override
            public void onError(Exception e) {
                mView.hideProgress();
                mView.showMsg();
            }
        });
    }

    /**
     * 版本更新
     */
    public void versionUpdate() {

        String url = ApisVersionUpdate.appUpdateRolling();
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {

                versionUpdateResp resp = parseJson(response, versionUpdateResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.versionUpdate(resp.getData());
                    } else {
                        mView.showMsg();
                    }
                } else {
                    mView.showMsg();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

}

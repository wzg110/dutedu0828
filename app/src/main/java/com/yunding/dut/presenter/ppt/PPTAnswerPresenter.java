package com.yunding.dut.presenter.ppt;

import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.model.resp.CommonRespNew;
import com.yunding.dut.model.resp.ppt.AutoAnswerSingleResp;
import com.yunding.dut.model.resp.ppt.PPTPollingResp;
import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.ppt.IPPTContentView;
import com.yunding.dut.util.api.ApisPPT;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 类 名 称：PPTAnswerPresenter
 * <P/>描    述：ppt小题页面业务逻辑
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:54
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:54
 * <P/>修改备注：
 * <P/>版    本：
 */


public class PPTAnswerPresenter extends BasePresenter {
    private IPPTContentView mview;
    public PPTAnswerPresenter(IPPTContentView mView) {
        this.mview = mView;
    }

    /**
     * 小题提交答案
     * @param slideId       [PPTID]
     * @param questionId    [问题ID]
     * @param teachingId    [主键]
     * @param selfTaughtId  [自学ID]
     * @param content       [答案]
     * @param longTime      [时长]
     */
//    public void commitAnswer(String slideId,String questionId,String teachingId,String selfTaughtId
//            ,String content,long longTime) {
//        String url = ApisPPT.savePPTAnswer(slideId, questionId, teachingId, selfTaughtId,content,longTime);
//
//
//        request(url, new DUTResp() {
//            @Override
//            public void onResp(String response) {
//                CommonResp resp = parseJson(response, CommonResp.class);
//                if (resp != null) {
//                    if (resp.isResult()) {
//                        mview.commitSuccess();
//                        mview.showMsg("提交成功");
//                    } else {
//                        mview.showMsg(resp.getMsg());
//                    }
//                } else {
//                    mview.showMsg(null);
//                }
//            }
//
//            @Override
//            public void onError(Exception e) {
//                mview.showMsg(e.toString());
//            }
//        });
//    }
    public void commitAnswer(String slideId,String questionId,String teachingId,String selfTaughtId
            ,String content,long longTime) {
        String url = ApisPPT.savePPTAnswer();
        OkHttpUtils.post()
                .url(url)
                .addParams("studentId", DUTApplication.getUserInfo().getUserId()+"")
                .addParams("slideId", slideId)
                .addParams("questionId", questionId)
                .addParams("teachingId", teachingId)
                .addParams("selfTaughtId", selfTaughtId)
                .addParams("content", content)
                .addParams("longTime", longTime+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                        mview.showMsg(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        CommonRespNew resp = parseJson(response, CommonRespNew.class);
                        if (resp != null) {
                            if (resp.isResult()) {
                                mview.commitSuccess();
                                mview.showMsg("提交成功");
                            } else {
                                mview.showMsg(resp.getMsg());
                            }
                        } else {
                            mview.showMsg(null);
                        }
                    }
                });
    }


    /**
     *  自动提交答案（全部）
     * @param slideId
     * @param teachingId
     */
    public void savePPTAnswerAuto(String slideId,String teachingId){
        String url=ApisPPT.savePPTAnswerAuto(slideId,teachingId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                PPTResp resp = parseJson(response, PPTResp.class);

                if (resp != null) {
                    if (resp.isResult()) {

                        mview.saveAutoResp(resp);
//                        mview.showMsg("提交成功");
//                        mview.hideProgress();
                    } else {
                        mview.showMsg(resp.getMsg());
//                        mview.hideProgress();
                    }
                } else {
                    mview.showMsg(null);
//                    mview.hideProgress();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    /**
     * 获取答案发布状态 废弃
     * @param slideId
     * @param questionId
     */
    public void getAnswerStatus(String slideId,String questionId) {
        mview.showProgress();
//        String url = ApisReading.commitReadingQuestion(questionId, answerContent, answerTime, backTime);
        String url= ApisPPT.getRefreshAnalysis(slideId,questionId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonRespNew resp = parseJson(response, CommonRespNew.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mview.getAnswerShow(resp.getData());
//                        mview.showMsg("提交成功");
                        mview.hideProgress();
                    } else {
                        mview.showMsg(resp.getMsg());
                        mview.hideProgress();
                    }
                } else {
                    mview.showMsg(null);
                    mview.hideProgress();
                }
            }

            @Override
            public void onError(Exception e) {
                mview.showMsg(e.toString()
                );
                mview.hideProgress();
            }
        });
    }

    /**
     *  ppt停留时间
     * @param teachingSlideId
     * @param selfTaughtId
     * @param slideId
     * @param classId
     * @param teachingId
     * @param startTime
     * @param endTime
     */
    public  void savePPTBrowerStayTime(String teachingSlideId,String selfTaughtId,String slideId
        ,String classId ,String teachingId,String startTime,String endTime){
    String url= ApisPPT.savePPTBrowerStayTime(teachingSlideId,selfTaughtId,slideId
   ,classId, teachingId,startTime,endTime);
    request(url, new DUTResp() {
        @Override
        public void onResp(String response) {

        }

        @Override
        public void onError(Exception e) {

        }
    });

}

    /**
     * 轮询PPT图片
     * @param teachingId
     * @param slideId
     */
    public  void pollingPPTImage(String teachingId,String slideId){
    String url=ApisPPT.pollingPPTImage(teachingId,slideId);
    request(url, new DUTResp() {
        @Override
        public void onResp(String response) {
            CommonRespNew resp = parseJson(response, CommonRespNew.class);
            if (resp != null) {
                if (resp.isResult()) {
                    mview.getNewPPTimage(resp.getData());
//                        mview.showMsg("提交成功");
//                mview.hideProgress();
                } else {
                    mview.showMsg(resp.getMsg());
//                mview.hideProgress();
                }
            }else{

            }
        }

        @Override
        public void onError(Exception e) {

        }
    });
}

    /**
     * PPT反馈
     * @param teachingId
     * @param slideId
     * @param questionId
     * @param classId
     * @param selfTaughtId
     * @param replyContent
     */
    public void sendFeedBack(String teachingId,
                         String slideId,
                         String questionId,
                         String classId,
                         String selfTaughtId,
                         String replyContent){
    mview.showProgress();
    String url=ApisPPT.sendFeedBack(teachingId,slideId,questionId,classId,selfTaughtId,replyContent);
    request(url, new DUTResp() {
        @Override
        public void onResp(String response) {
            mview.hideProgress();
            CommonResp resp = parseJson(response, CommonResp.class);
            if (resp != null) {
                if (resp.isResult()) {
                    mview.showMsg("发送成功");

                } else {
                    mview.showMsg(resp.getMsg());

                }
            }else{

            }
        }

        @Override
        public void onError(Exception e) {
            mview.showMsg(e.getMessage());
            mview.hideProgress();
        }
    });

}

    /**
     * ppt轮询小题
     * @param teachingId
     * @param slideId
     * @param loadedQuantity
     * @param loadedQuestionIds
     */
    public void pollingPPTQuestion(String teachingId,
                               String slideId,
                               String  loadedQuantity,
                               String loadedQuestionIds ){

    String url=ApisPPT.pollingPPTQuestion(teachingId,slideId,loadedQuantity,loadedQuestionIds);
    request(url, new DUTResp() {
        @Override
        public void onResp(String response) {

            PPTPollingResp resp = parseJson(response, PPTPollingResp.class);

            if (resp != null) {

                if (resp.isResult()) {
                    mview.getPollingPPTQuestion(resp.getData());

                } else {
                    mview.showMsg(resp.getMsg());

                }
            }else{

            }
        }

        @Override
        public void onError(Exception e) {
            mview.showMsg(e.getMessage());

        }
    });

}

    /**
     * 自动提交小题（单题）
     * @param questionId
     * @param teachingId
     */
    public void autoAnswerSingle(String questionId,String teachingId){
    mview.showProgress();
    String url=ApisPPT.autoAnswerSingle(questionId,teachingId);
    request(url, new DUTResp() {
        @Override
        public void onResp(String response) {
            mview.hideProgress();
            AutoAnswerSingleResp resp = parseJson(response, AutoAnswerSingleResp.class);

            if (resp != null) {

                if (resp.isResult()) {

                    mview.getAutoAnswerSingle(resp.getData());

                } else {
                    mview.showMsg(resp.getMsg());

                }
            }else{

            }
        }

        @Override
        public void onError(Exception e) {
            mview.showMsg(e.getMessage());
            mview.hideProgress();
        }
    });

}

    /**
     * 轮询手动发布状态
     * @param questionId
     */
    public void getAnalysisFlag(String questionId){
    String url=ApisPPT.pollingAnalysisFlag(questionId);
    request(url, new DUTResp() {
        @Override
        public void onResp(String response) {

            CommonRespNew resp = parseJson(response, CommonRespNew.class);

            if (resp != null) {

                if (resp.isResult()) {

                    mview.getAnalysisFlag(resp.getData());

                } else {
                    mview.showMsg(resp.getMsg());

                }
            }else{

            }
        }

        @Override
        public void onError(Exception e) {
            mview.showMsg(e.getMessage());
        }
    });

}
}

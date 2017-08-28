package com.yunding.dut.presenter.discuss;

import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.model.resp.CommonRespNew;
import com.yunding.dut.model.resp.discuss.DiscussQuestionListResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.discuss.IDiscussQuestionView;
import com.yunding.dut.util.api.ApisDiscuss;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 类 名 称：DiscussQuestionPresenter
 * <P/>描    述：讨论组题目返回结果
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/24 9:34
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/24 9:34
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class DiscussQuestionPresenter extends BasePresenter {
    private static final String TAG = "DiscussQuestionPresente";
    private IDiscussQuestionView mView;

    public DiscussQuestionPresenter(IDiscussQuestionView mView) {
        this.mView = mView;
    }

    /**
     * 功能描述： 获取服务时间用来倒计时
     */
    public void getServerTime(){
        String url = ApisDiscuss.serverTime();
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                CommonRespNew r= parseJson(response, CommonRespNew.class);
                if (r != null) {
                    if (r.isResult()) {
                        mView.getServerTime(r.getData());
                    } else {
                        mView.showMsg(r.getMsg());
                    }
                } else {
                    mView.showMsg(null);
                }
            }

            @Override
            public void onError(Exception e) {

                mView.showMsg(e.toString());
            }
        });

    }

    /**
     *  功能描述： 获取问题
     * @param subjectId [主题ID]
     * @param groupId   [讨论组ID]
     */
    public void getSubjectQuestions(String subjectId,String groupId) {
        mView.showProgress();
        String url = ApisDiscuss.getSubjectQuestion(subjectId,groupId);
//        Log.e(TAG, "getSubjectQuestions: "+url );
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                DiscussQuestionListResp resp = parseJson(response, DiscussQuestionListResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.showQuestions(resp);
                    } else {
                        mView.showMsg(resp.getMsg());
                    }
                } else {
                    mView.showMsg(null);
                }
            }

            @Override
            public void onError(Exception e) {
                mView.hideProgress();
                mView.showMsg(e.toString());
            }
        });
    }

    /**
     * 功能描述 ：提交问题答案
     * @param jsonParams    [答案json字符串]
     */
//    public void commitAnswer(String jsonParams) {
//        mView.showProgress();
//        String url = ApisDiscuss.commitAnswer(jsonParams);
//        Log.e("提交问题答案",url);
//        request(url, new DUTResp() {
//            @Override
//            public void onResp(String response) {
//                mView.hideProgress();
//                CommonResp resp = parseJson(response, CommonResp.class);
//                if (resp != null) {
//                    if (resp.isResult()) {
//                        mView.commitAnswerSuccess();
//                    } else {
//                        mView.showMsg(resp.getMsg());
//                    }
//                } else {
//                    mView.showMsg(null);
//                }
//            }
//
//            @Override
//            public void onError(Exception e) {
//                mView.hideProgress();
//                mView.showMsg(e.toString());
//            }
//        });
//    }
    public void commitAnswer(String jsonParams) {
        mView.showProgress();
        String url = ApisDiscuss.commitAnswer();
//        Log.e("提交问题答案",url);
        OkHttpUtils.post()
                .url(url)
                .addParams("json", jsonParams)
                .addParams("schoolCode", DUTApplication.getUserInfo().getSCHOOL_CODE())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mView.hideProgress();
                        mView.showMsg(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mView.hideProgress();
                        CommonRespNew resp = parseJson(response, CommonRespNew.class);
                        if (resp != null) {
                            if (resp.isResult()) {
                                mView.commitAnswerSuccess();
                            } else {
                                mView.showMsg(resp.getMsg());
                            }
                        } else {
                            mView.showMsg(null);
                        }
                    }
                });
    }

    /**
     * 功能描述： 倒计时完成手动置讨论组完成状态
     * @param groupid   [讨论组ID]
     * @param themeid   [主题Id]
     */
    public void changeDiscussState(String groupid , String themeid){
        String url = ApisDiscuss.changeDissTA(groupid,themeid);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {

                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
//                        mView.commitAnswerSuccess();
                    } else {
                        mView.showMsg(resp.getMsg());
                    }
                } else {
                    mView.showMsg(null);
                }
            }

            @Override
            public void onError(Exception e) {
//                mView.hideProgress();
                mView.showMsg(e.toString());
            }
        });

    }
}

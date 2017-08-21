package com.yunding.dut.presenter.ppt;

import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.model.resp.ppt.QuestionInfoResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.ppt.IPPTInfoView;
import com.yunding.dut.util.api.ApisPPT;

/**
 * 类 名 称：PPTInfoPresenter
 * <P/>描    述：PPT详情控制器
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 17:20
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 17:20
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class PPTInfoPresenter extends BasePresenter {

    private IPPTInfoView mView;

    public PPTInfoPresenter(IPPTInfoView mView) {
        this.mView = mView;
    }

    /**
     * 加载ppt信息
     * @param subjectId
     */
    public void loadPPTInfo(int subjectId) {
        mView.showProgress();
        String url = ApisPPT.getQuestion(subjectId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                QuestionInfoResp resp = parseJson(response, QuestionInfoResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.showQuestionList(resp);
                    } else {
                        mView.showMsg(resp.getMsg());
                    }
                } else {
                    mView.showMsg("服务器错误");
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
     * 提交答案
     * @param questionId
     * @param content
     */
    public void commitAnswer(int questionId, String content) {
        mView.showProgress();
        String url = ApisPPT.commitAnswer(questionId, content);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.commitSuccess();
                    } else {
                        mView.showMsg(resp.getMsg());
                    }
                } else {
                    mView.showMsg("服务器错误");
                }
            }

            @Override
            public void onError(Exception e) {
                mView.hideProgress();
                mView.showMsg(e.toString());
            }
        });
    }

    public void addLog(int subjectid) {
        String url = ApisPPT.addReadingLog(subjectid);
        request(url, null);
    }
}

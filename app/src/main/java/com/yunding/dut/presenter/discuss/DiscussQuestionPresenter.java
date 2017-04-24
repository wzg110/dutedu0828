package com.yunding.dut.presenter.discuss;

import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.model.resp.discuss.DiscussQuestionListResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.discuss.IDiscussQuestionView;
import com.yunding.dut.util.api.ApisDiscuss;

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

    private IDiscussQuestionView mView;

    public DiscussQuestionPresenter(IDiscussQuestionView mView) {
        this.mView = mView;
    }

    public void getSubjectQuestions(long subjectId) {
        mView.showProgress();
        String url = ApisDiscuss.getSubjectQuestion(subjectId);
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

    public void commitAnswer(String jsonParams) {
        mView.showProgress();
        String url = ApisDiscuss.commitAnswer(jsonParams);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                CommonResp resp = parseJson(response, CommonResp.class);
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

            @Override
            public void onError(Exception e) {
                mView.hideProgress();
                mView.showMsg(e.toString());
            }
        });
    }
}

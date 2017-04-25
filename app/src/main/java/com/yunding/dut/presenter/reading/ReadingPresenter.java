package com.yunding.dut.presenter.reading;

import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.reading.IReadingQuestionView;
import com.yunding.dut.util.api.ApisReading;

/**
 * 类 名 称：ReadingPresenter
 * <P/>描    述：阅读页面控制器
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/25 11:37
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/25 11:37
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class ReadingPresenter extends BasePresenter {

    private IReadingQuestionView mReadingQuestionView;

    public ReadingPresenter(IReadingQuestionView readingQuestionView) {
        this.mReadingQuestionView = readingQuestionView;
    }

    public void commitAnswer(String questionId, String answerContent, long answerTime, int backTime) {
        String url = ApisReading.commitReadingQuestion(questionId, answerContent, answerTime, backTime);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonResp resp = parseJson(response,CommonResp.class);
                if(resp!=null){
                    if(resp.isResult()){
                        mReadingQuestionView.commitSuccess();
                        mReadingQuestionView.showMsg("提交成功");
                    }else{
                        mReadingQuestionView.showMsg(resp.getMsg());
                    }
                }else{
                    mReadingQuestionView.showMsg(null);
                }
            }

            @Override
            public void onError(Exception e) {
                mReadingQuestionView.showMsg(e.toString());
            }
        });
    }
}

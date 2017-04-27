package com.yunding.dut.presenter.reading;

import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.reading.IReadingArticleView;
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
    private IReadingArticleView mReadingArticleView;

    public ReadingPresenter(IReadingQuestionView readingQuestionView) {
        this.mReadingQuestionView = readingQuestionView;
    }

    public ReadingPresenter(IReadingArticleView mReadingArticleView) {
        this.mReadingArticleView = mReadingArticleView;
    }

    public void commitAnswer(String questionId, String answerContent, long answerTime, int backTime) {
        String url = ApisReading.commitReadingQuestion(questionId, answerContent, answerTime, backTime);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mReadingQuestionView.commitSuccess();
                        mReadingQuestionView.showMsg("提交成功");
                    } else {
                        mReadingQuestionView.showMsg(resp.getMsg());
                    }
                } else {
                    mReadingQuestionView.showMsg(null);
                }
            }

            @Override
            public void onError(Exception e) {
                mReadingQuestionView.showMsg(e.toString());
            }
        });
    }

    public void markerWords(String courseId, int wordIndex, int wordLength, String word) {
        String url = ApisReading.markerWords(courseId, wordIndex, wordLength, word);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mReadingArticleView.showMsg("标记成功");
                    } else {
                        mReadingArticleView.showMsg(resp.getMsg());
                    }
                } else {
                    mReadingArticleView.showMsg(null);
                }
            }

            @Override
            public void onError(Exception e) {
                mReadingArticleView.showMsg(e.toString());
            }
        });
    }

    public void commitReadingTime(String courseId, int sentenceIndex, int fromSentenceIndex, long stayTime, int articleFinish) {
        String startIndex = (fromSentenceIndex >= 0) ? String.valueOf(fromSentenceIndex) : "";
        String url = ApisReading.recordReadingTime(courseId, sentenceIndex, startIndex, stayTime, articleFinish);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                    } else {
                        mReadingArticleView.showMsg(resp.getMsg());
                    }
                } else {
                    mReadingArticleView.showMsg(null);
                }
            }

            @Override
            public void onError(Exception e) {
                mReadingArticleView.showMsg(e.getMessage());
            }
        });
    }
}

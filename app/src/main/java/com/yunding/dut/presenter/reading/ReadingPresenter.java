package com.yunding.dut.presenter.reading;

import android.util.Log;

import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.model.resp.collect.CollectWordsResp;
import com.yunding.dut.model.resp.translate.JSTranslateBean;
import com.yunding.dut.model.resp.translate.YDTranslateBean;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.reading.IReadingArticleView;
import com.yunding.dut.ui.reading.IReadingQuestionView;
import com.yunding.dut.util.api.ApisReading;
import com.yunding.dut.util.api.ApisTranslate;

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
    private static final String TAG = "ReadingPresenter";
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

    /**
     * 获取滑词翻译
     * @param content
     */
    public void getTranslation(String content){
        if (content.contains(" ")){
            //使用有道翻译
           request(ApisTranslate.getYDTRANSLATE(content), new DUTResp() {
               @Override
               public void onResp(String response) {
                   YDTranslateBean ydTranslateBean = parseJson(response,YDTranslateBean.class);
                   if (ydTranslateBean!=null){


                    switch (ydTranslateBean.getErrorCode()){
                        case 20: mReadingArticleView.showMsg("要翻译的文本过长");
                            break;
                        case 30:mReadingArticleView.showMsg("无法进行有效的翻译");
                            break;
                        case 40:mReadingArticleView.showMsg("不支持的语言类型");
                            break;
                        case 50:mReadingArticleView.showMsg("key值失效，请联系客服");
                            break;
                        case 60:mReadingArticleView.showMsg("无词典结果");
                            break;
                        case 0:mReadingArticleView.showYdTranslate(ydTranslateBean);

                    }
                   }else mReadingArticleView.showMsg("翻译失败");
                   mReadingArticleView.hideProgress();
               }

               @Override
               public void onError(Exception e) {
                    mReadingArticleView.showMsg(e.getMessage());
                   mReadingArticleView.hideProgress();
               }
           });
        }else {
            content = content.toLowerCase();
            Log.e(TAG, "getTranslation: "+content );
            //使用金山翻译
            request(ApisTranslate.getJSTRANSLATE(content), new DUTResp() {
                @Override
                public void onResp(String response) {
                    if (!response.isEmpty()){

                        JSTranslateBean jsTranslateBean = parseJson(response,JSTranslateBean.class);
                        if (jsTranslateBean!=null){
                            mReadingArticleView.showJsTranslate(jsTranslateBean);
                        }else mReadingArticleView.showMsg("翻译失败");
                    }else mReadingArticleView.showMsg("翻译失败");
                    mReadingArticleView.hideProgress();
                }

                @Override
                public void onError(Exception e) {
                        mReadingArticleView.showMsg(e.getMessage());
                    mReadingArticleView.hideProgress();
                }
            });

        }



    }

    public void collectWords(String english,String courseId,String characters){
        request(ApisReading.collectWords(english, courseId, characters), new DUTResp() {
            @Override
            public void onResp(String response) {
                CollectWordsResp collectWordsResp = parseJson(response,CollectWordsResp.class);
                if (collectWordsResp!=null){
                    if (collectWordsResp.isResult()){
                        mReadingArticleView.showMsg(collectWordsResp.getMsg());
                    }else mReadingArticleView.showMsg(collectWordsResp.getMsg());

                }


            }

            @Override
            public void onError(Exception e) {
                mReadingArticleView.showMsg(e.getMessage());
            }
        });

    }

}

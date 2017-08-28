package com.yunding.dut.presenter.reading;

import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.model.resp.CommonRespNew;
import com.yunding.dut.model.resp.collect.CollectWordsResp;
import com.yunding.dut.model.resp.reading.ReadingDataResp;
import com.yunding.dut.model.resp.reading.markResp;
import com.yunding.dut.model.resp.translate.JSTranslateBean;
import com.yunding.dut.model.resp.translate.YDTranslateBean;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.reading.IReadingArticleView;
import com.yunding.dut.ui.reading.IReadingQuestionView;
import com.yunding.dut.util.api.ApisReading;
import com.yunding.dut.util.api.ApisTranslate;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

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

    /**
     * 显示用户阅读数据信息(小题)
     * @param classId
     * @param courseId
     */
    public void showReadingData(String classId,String courseId){
        mReadingQuestionView.showProgress();
        String url=ApisReading.showReadingData(classId,courseId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                ReadingDataResp resp=parseJson(response,ReadingDataResp.class);
                if (resp!=null){
                    if (resp.isResult()) {
                        mReadingQuestionView.hideProgress();
                        mReadingQuestionView.showReadingDataSuccess(resp.getData());
//                        mReadingQuestionView.showMsg("提交成功");
                    } else {
                        mReadingQuestionView.hideProgress();
                        mReadingQuestionView.showMsg(resp.getMsg());
                    }
                }else{
                    mReadingQuestionView.hideProgress();
                    mReadingQuestionView.showMsg(null);
                }
            }

            @Override
            public void onError(Exception e) {
                mReadingQuestionView.hideProgress();
                mReadingQuestionView.showMsg(e.toString());
            }
        });
    }
    /**
     * 显示用户阅读数据信息(文章)
     * @param classId
     * @param courseId
     */
    public void showReadingDataA(String classId,String courseId){
        mReadingArticleView.showProgress();
        String url=ApisReading.showReadingData(classId,courseId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                ReadingDataResp resp=parseJson(response,ReadingDataResp.class);
//                Log.e("sss",resp.toString());
                if (resp!=null){
                    if (resp.isResult()) {
                        mReadingArticleView.hideProgress();
                        mReadingArticleView.showReadingDataSuccess(resp.getData());
//                        mReadingQuestionView.showMsg("提交成功");
                    } else {
                        mReadingArticleView.hideProgress();
                        mReadingArticleView.showMsg(resp.getMsg());
                    }
                }else{
                    mReadingArticleView.hideProgress();
                    mReadingArticleView.showMsg(null);
                }
            }

            @Override
            public void onError(Exception e) {
                mReadingArticleView.hideProgress();
                mReadingArticleView.showMsg(e.toString());
            }
        });
    }

    /**
     * 提交阅读课小题答案
     * @param questionId
     * @param answerContent
     * @param answerTime
     * @param backTime
     * @param classId
     */
//    public void commitAnswer(String questionId, String answerContent, long answerTime, int backTime,String classId) {
//        mReadingQuestionView.showProgress();
//        String url = ApisReading.commitReadingQuestion(questionId, answerContent, answerTime, backTime,classId);
//        request(url, new DUTResp() {
//            @Override
//            public void onResp(String response) {
//
//                CommonResp resp = parseJson(response, CommonResp.class);
//                if (resp != null) {
//                    if (resp.isResult()) {
//                        mReadingQuestionView.hideProgress();
//                        mReadingQuestionView.commitSuccess();
//                        mReadingQuestionView.showMsg("提交成功");
//                    } else {
//                        mReadingQuestionView.hideProgress();
//                        mReadingQuestionView.showMsg(resp.getMsg());
//                    }
//                } else {
//                    mReadingQuestionView.hideProgress();
//                    mReadingQuestionView.showMsg(null);
//                }
//            }
//
//            @Override
//            public void onError(Exception e)
//            {
//                mReadingQuestionView.hideProgress();
//                mReadingQuestionView.showMsg(e.toString());
//            }
//        });
//    }
    public void commitAnswer(String questionId, String answerContent,
                             long answerTime, int backTime,String classId) {
        mReadingQuestionView.showProgress();
        String url = ApisReading.commitReadingQuestion();
        OkHttpUtils.post()
                .url(url)
                .addParams("studentId", DUTApplication.getUserInfo().getUserId()+"")
                .addParams("questionId", questionId)
                .addParams("answerContent", answerContent)
                .addParams("answerTime", answerTime+"")
                .addParams("backTime", backTime+"")
                .addParams("classId", classId)
                .addParams("schoolCode",DUTApplication.getUserInfo().getSCHOOL_CODE())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mReadingQuestionView.hideProgress();
                        mReadingQuestionView.showMsg(e.toString());
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        mReadingQuestionView.hideProgress();
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
                });
    }

    /**
     * 标记生词
     * @param courseId
     * @param wordIndex
     * @param wordLength
     * @param word
     * @param classId
     * @param wordColor
     */
    public void markerWords(String courseId, final int wordIndex, final int wordLength, final String word, String classId, final String wordColor) {
        String url = ApisReading.markerWords(courseId, wordIndex, wordLength, word,classId,wordColor);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                markResp resp = parseJson(response, markResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mReadingArticleView.showMsg("标记成功");
                        mReadingArticleView.showMarkSuccess(resp.getData());

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

    /**
     * 删除标记
     * @param newWordId
     */
    public void deleteNewWord(final String newWordId){
        String  url=ApisReading.deleteMarks(newWordId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mReadingArticleView.deleteSuccess("删除成功",newWordId);
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

    /**
     * 修改标记颜色
     * @param newWordId
     * @param color
     */
    public void changeColor(final String newWordId,String color){
        String  url=ApisReading.changeNewWordBG(newWordId,color);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
//                        mReadingArticleView.showMsg("");
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

    /**
     * 保存笔记
     * @param newWordId
     * @param noteContent
     * @param position
     */
    public void saveWordNote(final String newWordId, final String noteContent, final int position){
        String  url=ApisReading.saveWordNotes(newWordId,noteContent);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonRespNew resp = parseJson(response, CommonRespNew.class);
                if (resp != null) {
                    if (resp.isResult()) {
//                        mReadingArticleView.showMsg();
                        mReadingArticleView.savaNoteSuccess(noteContent,newWordId,position,resp.getData());
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

    /**
     * 提交逐行阅读时间
     * @param courseId
     * @param sentenceIndex
     * @param fromSentenceIndex
     * @param stayTime
     * @param articleFinish
     * @param classId
     */
    public void commitReadingTime(String courseId, int sentenceIndex, int fromSentenceIndex,
                                  long stayTime, int articleFinish,String classId) {
        String startIndex = (fromSentenceIndex >= 0) ? String.valueOf(fromSentenceIndex) : "";
        String url = ApisReading.recordReadingTime(courseId, sentenceIndex, startIndex, stayTime, articleFinish,classId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mReadingArticleView.commitSuccess();
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
    public void getTranslation(String content,final float[] position){
        mReadingArticleView.showProgress();
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
                        case 0:mReadingArticleView.showYdTranslate(ydTranslateBean,position);

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
            //使用金山翻译
            request(ApisTranslate.getJSTRANSLATE(content), new DUTResp() {
                @Override
                public void onResp(String response) {
                    if (!response.isEmpty()){

                        JSTranslateBean jsTranslateBean = parseJson(response,JSTranslateBean.class);
                        if (jsTranslateBean!=null){
                            mReadingArticleView.showJsTranslate(jsTranslateBean,position);
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

    /**
     * 收藏生词
     * @param english
     * @param courseId
     * @param characters
     * @param newWordId
     */
    public void collectWords(String english,String courseId,String characters,String newWordId){
        request(ApisReading.collectWords(english, courseId, characters,newWordId), new DUTResp() {
            @Override
            public void onResp(String response) {
                CollectWordsResp collectWordsResp = parseJson(response,CollectWordsResp.class);
                if (collectWordsResp!=null){
                    if (collectWordsResp.isResult()){
                        mReadingArticleView.showMsg("收藏成功");
                    }else mReadingArticleView.showMsg("收藏失败");

                }


            }

            @Override
            public void onError(Exception e) {
                mReadingArticleView.showMsg(e.getMessage());
            }
        });

    }

    /**
     *
     * 记录跳出APP
     */
    public void  dapFromArticle(String courseId,String classId,int sentenceIndex){
        String url= ApisReading.exitReading(courseId,classId,sentenceIndex);
        request(url, new DUTResp() {


            @Override
            public void onResp(String response) {
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
//                        mReadingArticleView.showMsg("");
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

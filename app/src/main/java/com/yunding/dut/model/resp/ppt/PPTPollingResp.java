package com.yunding.dut.model.resp.ppt;

import java.io.Serializable;
import java.util.List;
/**
 * 类 名 称：PPTPollingResp
 * <P/>描    述：ppt轮询小题返回
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:15
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:15
 * <P/>修改备注：
 * <P/>版    本：
 */

public class PPTPollingResp {
    public static class ErrorsBean implements Serializable {
    }
    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(ErrorsBean errors) {
        this.errors = errors;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    private ErrorsBean errors;
    private String msg;
    private boolean result;
    private String version;
    private List<DataBean> data;
    private static  class DataBean extends PPTResp.DataBean.slideQuestionsBean implements Serializable{

//
//            //            "questionId":// integer 小题ID
////                    "questionContent"// string 小题内容
////                    "questionType":// integer 小题类型 1-填空 2-单选 3-多选 4-问答 5-枚举
////                    "rightAnswer":// string 填空类型题的正确答案为json字符串，多选类型题答案用英文逗号分隔的字符串
////                    "analysis":// string 答案解析
////                    "questionIndex":// integer 小题顺序号
////                    "sendAnalysisFlag":// integer 0-手动发布答案解析 1答完发布答案解析
////                    "questionCompleted":// integer 小题是否完成 0-未完成 1-完成
////                    "answerContent":// string 学生答案 形式如rightAnswer
////                     optionsList
//            public  List<InputBlank> getBlanksInfo() {
//                return blanksInfo;
//            }public void             setBlanksInfo(List<InputBlank> blanksInfo) {
//                this.blanksInfo = blanksInfo;
//            }private List<InputBlank>blanksInfo;
//            private String questionId;
//            private String questionContent;
//            private int questionType;
//            private String rightAnswer;
//            private String analysis;
//            private String questionIndex;
//            private int sendAnalysisFlag;
//            private int questionCompleted;
//            private String answerContent;
//            private List<optionsListBean> optionsList;
//            public static class InputBlank implements Serializable{
//                public int getIndex() {
//                    return index;
//                }
//
//                public void setIndex(int index) {
//                    this.index = index;
//                }
//
//                public int getLength() {
//                    return length;
//                }
//
//                public void setLength(int length) {
//                    this.length = length;
//                }
//
//                private int index;
//                private int length;
//            }
//
//            public String getQuestionId() {
//                return questionId;
//            }
//
//            public void setQuestionId(String questionId) {
//                this.questionId = questionId;
//            }
//
//            public String getQuestionContent() {
//                return questionContent;
//            }
//
//            public void setQuestionContent(String questionContent) {
//                this.questionContent = questionContent;
//            }
//
//            public int getQuestionType() {
//                return questionType;
//            }
//
//            public void setQuestionType(int questionType) {
//                this.questionType = questionType;
//            }
//
//            public String getRightAnswer() {
//                return rightAnswer;
//            }
//
//            public void setRightAnswer(String rightAnswer) {
//                this.rightAnswer = rightAnswer;
//            }
//
//            public String getAnalysis() {
//                return analysis;
//            }
//
//            public void setAnalysis(String analysis) {
//                this.analysis = analysis;
//            }
//
//            public String getQuestionIndex() {
//                return questionIndex;
//            }
//
//            public void setQuestionIndex(String questionIndex) {
//                this.questionIndex = questionIndex;
//            }
//
//            public int getSendAnalysisFlag() {
//                return sendAnalysisFlag;
//            }
//
//            public void setSendAnalysisFlag(int sendAnalysisFlag) {
//                this.sendAnalysisFlag = sendAnalysisFlag;
//            }
//
//            public int getQuestionCompleted() {
//                return questionCompleted;
//            }
//
//            public void setQuestionCompleted(int questionCompleted) {
//                this.questionCompleted = questionCompleted;
//            }
//
//            public String getAnswerContent() {
//                return answerContent;
//            }
//
//            public void setAnswerContent(String answerContent) {
//                this.answerContent = answerContent;
//            }
//
//            public List<optionsListBean> getOptionsList() {
//                return optionsList;
//            }
//
//            public void setOptionsList(List<optionsListBean> optionsList) {
//                this.optionsList = optionsList;
//            }
//
//            public static class optionsListBean implements Serializable {
//                //                 "optionId":// integer 选项ID
////                         "optionContent":// string 选项内容
////                         "isRight":// integer 是否是正确答案 0-不正确 1-正确
////                         "optionIndex"// string 选项序号 如A,B,C等
//                public String getOptionId() {
//                    return optionId;
//                }
//
//                public void setOptionId(String optionId) {
//                    this.optionId = optionId;
//                }
//
//                public String getIsRight() {
//                    return isRight;
//                }
//
//                public void setIsRight(String isRight) {
//                    this.isRight = isRight;
//                }
//
//                public String getOptionContent() {
//                    return optionContent;
//                }
//
//                public void setOptionContent(String optionContent) {
//                    this.optionContent = optionContent;
//                }
//
//                public String getOptionIndex() {
//                    return optionIndex;
//                }
//
//                public void setOptionIndex(String optionIndex) {
//                    this.optionIndex = optionIndex;
//                }
//
//                private String optionId;
//                private String isRight;
//                private String optionContent;
//                private String optionIndex;
//
//            }
//
//
        }
    }



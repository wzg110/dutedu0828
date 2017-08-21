package com.yunding.dut.model.resp.ppt;

import java.io.Serializable;
import java.util.List;

/**
 * 类 名 称：AutoAnswerResp
 * <P/>描    述：自动提交答案返回(废弃)
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:13
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:13
 * <P/>修改备注：
 * <P/>版    本：
 */

public class AutoAnswerResp {

    /**
     * data : [{"analysis":"123","answerContent":"[\"\"]","questionCompleted":1,"questionContent":"我的姓名是啥","questionId":509,"questionIndex":1,"questionType":1,"rightAnswer":"","sendAnalysisFlag":0},{"analysis":"答案是B","answerContent":"C","optionsList":[{"isRight":0,"optionContent":"计划生育科学","optionId":750,"optionIndex":"A"},{"isRight":1,"optionContent":"计算机科学","optionId":751,"optionIndex":"B"},{"isRight":0,"optionContent":"计划科学","optionId":752,"optionIndex":"C"},{"isRight":0,"optionContent":"计较科学","optionId":753,"optionIndex":"D"}],"questionCompleted":1,"questionContent":"计科是啥意思","questionId":510,"questionIndex":2,"questionType":2,"rightAnswer":"B","sendAnalysisFlag":0},{"analysis":"都是正确答案","answerContent":"C","optionsList":[{"isRight":1,"optionContent":"IE","optionId":754,"optionIndex":"A"},{"isRight":1,"optionContent":"EXCEL","optionId":755,"optionIndex":"B"},{"isRight":1,"optionContent":"flash","optionId":756,"optionIndex":"C"},{"isRight":1,"optionContent":"RAR","optionId":757,"optionIndex":"D"},{"isRight":1,"optionContent":"word","optionId":758,"optionIndex":"E"}],"questionCompleted":1,"questionContent":"这张幻灯片的windows桌面图标都有啥","questionId":511,"questionIndex":3,"questionType":3,"rightAnswer":"A,B,C,D,E","sendAnalysisFlag":1},{"analysis":"2张","answerContent":"","questionCompleted":1,"questionContent":"这张幻灯片有几张风景图片","questionId":512,"questionIndex":4,"questionType":4,"sendAnalysisFlag":0},{"analysis":"无","answerContent":"","questionCompleted":1,"questionContent":"你喜欢的windows应用都有什么举例说明3中","questionId":513,"questionIndex":5,"questionType":5,"sendAnalysisFlag":0}]
     * errors : {}
     * msg : ok
     * result : true
     * version : 20170719, linhai
     */

    private ErrorsBean errors;
    private String msg;
    private boolean result;
    private String version;
    private List<DataBean> data;

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

    public static class ErrorsBean {
    }

    public static class DataBean implements Serializable {
        public String getAnswerTimeLimit() {
            return answerTimeLimit;
        }

        public void setAnswerTimeLimit(String answerTimeLimit) {
            this.answerTimeLimit = answerTimeLimit;
        }

        /**
         * analysis : 123
         * answerContent : [""]
         * questionCompleted : 1
         * questionContent : 我的姓名是啥
         * questionId : 509
         * questionIndex : 1
         * questionType : 1
         * rightAnswer :
         * sendAnalysisFlag : 0
         * optionsList : [{"isRight":0,"optionContent":"计划生育科学","optionId":750,"optionIndex":"A"},{"isRight":1,"optionContent":"计算机科学","optionId":751,"optionIndex":"B"},{"isRight":0,"optionContent":"计划科学","optionId":752,"optionIndex":"C"},{"isRight":0,"optionContent":"计较科学","optionId":753,"optionIndex":"D"}]
         */
        private  String answerTimeLimit;
        private String questionId;
        private String questionContent;
        private int questionType;
        private String rightAnswer;
        private String analysis;
        private String questionIndex;
        private int sendAnalysisFlag;
        private int questionCompleted;
        private String answerContent;
        private List<optionsListBean> optionsList;

        public String getAnalysis() {
            return analysis;
        }

        public void setAnalysis(String analysis) {
            this.analysis = analysis;
        }

        public String getAnswerContent() {
            return answerContent;
        }

        public void setAnswerContent(String answerContent) {
            this.answerContent = answerContent;
        }

        public int getQuestionCompleted() {
            return questionCompleted;
        }

        public void setQuestionCompleted(int questionCompleted) {
            this.questionCompleted = questionCompleted;
        }

        public String getQuestionContent() {
            return questionContent;
        }

        public void setQuestionContent(String questionContent) {
            this.questionContent = questionContent;
        }

        public String getQuestionId() {
            return questionId;
        }

        public void setQuestionId(String questionId) {
            this.questionId = questionId;
        }

        public String getQuestionIndex() {
            return questionIndex;
        }

        public void setQuestionIndex(String questionIndex) {
            this.questionIndex = questionIndex;
        }

        public int getQuestionType() {
            return questionType;
        }

        public void setQuestionType(int questionType) {
            this.questionType = questionType;
        }

        public String getRightAnswer() {
            return rightAnswer;
        }

        public void setRightAnswer(String rightAnswer) {
            this.rightAnswer = rightAnswer;
        }

        public int getSendAnalysisFlag() {
            return sendAnalysisFlag;
        }

        public void setSendAnalysisFlag(int sendAnalysisFlag) {
            this.sendAnalysisFlag = sendAnalysisFlag;
        }

        public List<optionsListBean> getOptionsList() {
            return optionsList;
        }

        public void setOptionsList(List<optionsListBean> optionsList) {
            this.optionsList = optionsList;
        }

        public static class optionsListBean implements Serializable{
            /**
             * isRight : 0
             * optionContent : 计划生育科学
             * optionId : 750
             * optionIndex : A
             */

            private int isRight;
            private String optionContent;
            private int optionId;
            private String optionIndex;

            public int getIsRight() {
                return isRight;
            }

            public void setIsRight(int isRight) {
                this.isRight = isRight;
            }

            public String getOptionContent() {
                return optionContent;
            }

            public void setOptionContent(String optionContent) {
                this.optionContent = optionContent;
            }

            public int getOptionId() {
                return optionId;
            }

            public void setOptionId(int optionId) {
                this.optionId = optionId;
            }

            public String getOptionIndex() {
                return optionIndex;
            }

            public void setOptionIndex(String optionIndex) {
                this.optionIndex = optionIndex;
            }
        }
    }
}

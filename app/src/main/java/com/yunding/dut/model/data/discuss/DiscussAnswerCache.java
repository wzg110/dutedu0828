package com.yunding.dut.model.data.discuss;

import com.yunding.dut.model.resp.discuss.DiscussQuestionListResp;

import java.io.Serializable;
import java.util.List;

/**
 * 类 名 称：DiscussAnswerCache
 * <P/>描    述：用来保存用户录入答案的缓存对象
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/24 10:49
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/24 10:49
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DiscussAnswerCache implements Serializable{

    private int questionType;

    private String choiceAnswer;

    private List<String> inputAnswer;

    private DiscussQuestionListResp.DataBean question;

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getChoiceAnswer() {
        return choiceAnswer;
    }

    public void setChoiceAnswer(String choiceAnswer) {
        this.choiceAnswer = choiceAnswer;
    }

    public List<String> getInputAnswer() {
        return inputAnswer;
    }

    public void setInputAnswer(List<String> inputAnswer) {
        this.inputAnswer = inputAnswer;
    }

    public DiscussQuestionListResp.DataBean getQuestion() {
        return question;
    }

    public void setQuestion(DiscussQuestionListResp.DataBean question) {
        this.question = question;
    }
}

package com.yunding.dut.ui.discuss;

import com.yunding.dut.model.resp.discuss.DiscussQuestionListResp;
import com.yunding.dut.ui.base.IBaseView;

/**
 * 类 名 称：IDiscussQuestionView
 * <P/>描    述：讨论组答题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/23 18:34
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/23 18:34
 * <P/>修改备注：
 * <P/>版    本：
 */

public interface IDiscussQuestionView extends IBaseView{

    void showQuestions(DiscussQuestionListResp resp);

    void showMsg(String msg);

    void commitAnswerSuccess();

    void getServerTime(String time);
}

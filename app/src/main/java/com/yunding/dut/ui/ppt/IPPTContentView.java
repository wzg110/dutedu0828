package com.yunding.dut.ui.ppt;

import com.yunding.dut.model.resp.ppt.AutoAnswerSingleResp;
import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.ui.base.IBaseView;

import java.util.List;

/**
 * 类 名 称：IPPTContentView
 * <P/>描    述：ppt内容页面接口
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 16:56
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 16:56
 * <P/>修改备注：
 * <P/>版    本：
 */
public interface IPPTContentView extends IBaseView {
    void showMsg(String msg);
    void commitSuccess();
    void getAnswerShow(String state);
    void saveAutoResp(PPTResp bean);
    void getNewPPTimage(String url);
    void getPollingPPTQuestion(List<?extends PPTResp.DataBean.slideQuestionsBean> dataList);
    void getAutoAnswerSingle(AutoAnswerSingleResp.DataBean dataBean);
    void getAnalysisFlag(String flag);
}

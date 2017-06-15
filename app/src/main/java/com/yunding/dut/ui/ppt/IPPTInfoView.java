package com.yunding.dut.ui.ppt;

import com.yunding.dut.model.resp.ppt.PPTListResp;
import com.yunding.dut.model.resp.ppt.QuestionInfoResp;
import com.yunding.dut.ui.base.IBaseView;

/**
 * 类 名 称：IPPTInfoView
 * <P/>描    述：PPT详情
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 16:58
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 16:58
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public interface IPPTInfoView extends IBaseView{

    void showPPTInfo(PPTListResp.DataBean item);

    void showQuestionList(QuestionInfoResp resp);

    void showMsg(String msg);

    void commitSuccess();
}

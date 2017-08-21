package com.yunding.dut.ui.discuss;

import com.yunding.dut.model.resp.discuss.DiscussMsgListResp;
import com.yunding.dut.model.resp.discuss.DiscussSubjectResp;
import com.yunding.dut.ui.base.IBaseListView;

/**
 * 类 名 称：IDiscussView
 * <P/>描    述：讨论组消息页面接口
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/22 14:55
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/22 14:55
 * <P/>修改备注：
 * <P/>版    本：
 */
public interface IDiscussView extends IBaseListView {

    void showDiscussNotStart();

    void showDiscussing();
    void showDiscussingN(String opentime);

    void showDiscussFinished();

    void showMsgList(DiscussMsgListResp resp);

    void showMsg(String msg);

    void sendTextMsgSuccess();

    void sendVoiceMsgSuccess();

    void showSubjectInfo(DiscussSubjectResp resp);
}

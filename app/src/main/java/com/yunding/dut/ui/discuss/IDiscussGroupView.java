package com.yunding.dut.ui.discuss;

import com.yunding.dut.model.resp.discuss.DiscussGroupInfoResp;
import com.yunding.dut.ui.base.IBaseView;

/**
 * 类 名 称：IDiscussGroupView
 * <P/>描    述：讨论组信息View
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/21 19:11
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/21 19:11
 * <P/>修改备注：
 * <P/>版    本：
 */

public interface IDiscussGroupView extends IBaseView{

    void showGroupInfo(DiscussGroupInfoResp resp);

    void showGroupFailed(String msg);
}

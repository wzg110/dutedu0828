package com.yunding.dut.ui.ppt;

import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.ui.base.IBaseListView;

/**
 * 类 名 称：IPPTListView
 * <P/>描    述：PPT列表
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 16:13
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 16:13
 * <P/>修改备注：   
 * <P/>版    本：1.0
 */

public interface IPPTListView extends IBaseListView{

    void showPPTList(PPTResp resp);
    void showMsg(String msg);

}

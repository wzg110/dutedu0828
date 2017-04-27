package com.yunding.dut.ui.me;

import com.yunding.dut.ui.base.IBaseView;

/**
 * 类 名 称：IMeInfoView
 * <P/>描    述：我的信息
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/27 14:32
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/27 14:32
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public interface IMeInfoView extends IBaseView{

    void showAvatar();

    void showMsg(String msg);
}

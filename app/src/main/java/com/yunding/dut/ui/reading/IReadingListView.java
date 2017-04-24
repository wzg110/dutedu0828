package com.yunding.dut.ui.reading;

import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.ui.base.IBaseView;

/**
 * 类 名 称：IReadingView
 * <P/>描    述：阅读列表
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/24 16:07
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/24 16:07
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public interface IReadingListView extends IBaseView {

    void showReadingList(ReadingListResp resp);

    void showMsg(String msg);

}

package com.yunding.dut.ui.me;

import com.yunding.dut.model.resp.collect.CollectAllWordsResp;
import com.yunding.dut.ui.base.IBaseView;

/**
 * 类 名 称：IMeWordsView
 * <P/>描    述： 收藏生词页面
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 16:36
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 16:36
 * <P/>修改备注：
 * <P/>版    本：
 */

public interface IMeWordsView extends IBaseView{

    void showWordsList(CollectAllWordsResp collectAllWordsResp);
    void showMsg(String msg);


}

package com.yunding.dut.ui.discuss;

import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.ui.base.IBaseListView;

/**
 * 类 名 称：IDiscussView
 * <P/>描    述：讨论组列表页面接口
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 17:39
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 17:39
 * <P/>修改备注：
 * <P/>版    本：
 */

public interface IDiscussListView extends IBaseListView {

    void showDiscussList(DiscussListResp resp);

}

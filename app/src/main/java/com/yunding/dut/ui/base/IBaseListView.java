package com.yunding.dut.ui.base;

/**
 * 类 名 称：IBaseListView
 * <P/>描    述：列表视图基类
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 17:51
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 17:51
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public interface IBaseListView extends IBaseView {

    void showNoData();

    void showBadNetwork();
}

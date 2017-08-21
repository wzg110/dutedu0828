package com.yunding.dut.ui.me;

import com.yunding.dut.model.resp.translate.JSTranslateBean;
import com.yunding.dut.model.resp.translate.YDTranslateBean;
import com.yunding.dut.ui.base.IBaseView;

/**
 * 类 名 称：IMeWordTranslateView
 * <P/>描    述： 暂时启用
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 16:36
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 16:36
 * <P/>修改备注：
 * <P/>版    本：
 */
public interface IMeWordTranslateView extends IBaseView{

    void JSTranslate(JSTranslateBean jsTranslateBean);
    void YDTranslate(YDTranslateBean ydTranslateBean);
    void showMsg(String msg);

}

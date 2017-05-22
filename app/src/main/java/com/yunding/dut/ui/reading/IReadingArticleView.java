package com.yunding.dut.ui.reading;

import com.yunding.dut.model.resp.translate.JSTranslateBean;
import com.yunding.dut.model.resp.translate.YDTranslateBean;
import com.yunding.dut.ui.base.IBaseView;

/**
 * 类 名 称：IReadingArticalView
 * <P/>描    述：阅读页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/26 13:07
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/26 13:07
 * <P/>修改备注：
 * <P/>版    本：
 */

public interface IReadingArticleView extends IBaseView {

    void showMsg(String msg);
    void showJsTranslate(JSTranslateBean jsTranslateBean);
    void showYdTranslate(YDTranslateBean ydTranslateBean);
}

package com.yunding.dut.ui.me;

import com.yunding.dut.model.resp.translate.JSTranslateBean;
import com.yunding.dut.model.resp.translate.YDTranslateBean;
import com.yunding.dut.ui.base.IBaseView;

/**
 * Created by Administrator on 2017/5/23.
 */

public interface IMeWordTranslateView extends IBaseView{

    void JSTranslate(JSTranslateBean jsTranslateBean);
    void YDTranslate(YDTranslateBean ydTranslateBean);
    void showMsg(String msg);

}

package com.yunding.dut.ui.me;

import com.yunding.dut.model.resp.collect.CollectAllWordsResp;
import com.yunding.dut.ui.base.IBaseView;

/**
 * Created by Administrator on 2017/5/23.
 */

public interface IMeWordsView extends IBaseView{

    void showWordsList(CollectAllWordsResp collectAllWordsResp);
    void showMsg(String msg);


}

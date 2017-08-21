package com.yunding.dut.presenter.me;

import com.yunding.dut.model.resp.collect.CollectAllWordsResp;
import com.yunding.dut.model.resp.collect.CollectWordsResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.me.IMeWordsView;
import com.yunding.dut.util.api.ApisReading;

/**
 * 类 名 称：MeWordsPresenter
 * <P/>描    述： 收藏业务处理
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:48
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:48
 * <P/>修改备注：
 * <P/>版    本：
 */

public class MeWordsPresenter extends BasePresenter{

    private IMeWordsView mIMeWordsView;

    public MeWordsPresenter(IMeWordsView IMeWordsView) {
        mIMeWordsView = IMeWordsView;
    }

    /**
     * 功能描述：获取收藏列表
     */
    public void  getCollectWordsList(){
        mIMeWordsView.showProgress();
        request(ApisReading.getCollectWords(), new DUTResp() {
            @Override
            public void onResp(String response) {
                CollectAllWordsResp collectAllWordsResp = parseJson(response,CollectAllWordsResp.class);
                if (collectAllWordsResp!=null){
                    if (collectAllWordsResp.isResult()){
                        mIMeWordsView.showWordsList(collectAllWordsResp);
                    }else mIMeWordsView.showMsg(collectAllWordsResp.getMsg());
                }else mIMeWordsView.showMsg(null);
                mIMeWordsView.hideProgress();
            }

            @Override
            public void onError(Exception e) {
                mIMeWordsView.showMsg(e.toString());
                mIMeWordsView.hideProgress();
            }
        });



    }

    /**
     * 功能描述： 删除收藏
     * @param id    [收藏ID]
     */
    public void delCollectWords(int id){
        mIMeWordsView.showProgress();
        request(ApisReading.delCollectWord(id), new DUTResp() {
            @Override
            public void onResp(String response) {
                CollectWordsResp collectWordsResp = parseJson(response,CollectWordsResp.class);
                if (collectWordsResp!=null){
                    if (collectWordsResp.isResult()){
                        mIMeWordsView.showMsg("删除成功");
                    }else mIMeWordsView.showMsg(collectWordsResp.getMsg());
                }else mIMeWordsView.showMsg("删除失败");
                mIMeWordsView.hideProgress();
            }

            @Override
            public void onError(Exception e) {
                mIMeWordsView.showMsg(e.toString());
                mIMeWordsView.hideProgress();
            }
        });


    }

}

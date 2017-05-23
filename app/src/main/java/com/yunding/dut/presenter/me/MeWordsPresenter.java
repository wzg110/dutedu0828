package com.yunding.dut.presenter.me;

import com.yunding.dut.model.resp.collect.CollectAllWordsResp;
import com.yunding.dut.model.resp.collect.CollectWordsResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.me.IMeWordsView;
import com.yunding.dut.util.api.ApisReading;

/**
 * Created by Administrator on 2017/5/23.
 */

public class MeWordsPresenter extends BasePresenter{

    private IMeWordsView mIMeWordsView;

    public MeWordsPresenter(IMeWordsView IMeWordsView) {
        mIMeWordsView = IMeWordsView;
    }
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

package com.yunding.dut.presenter.discuss;

import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.discuss.IDiscussListView;
import com.yunding.dut.util.api.ApisDiscuss;

/**
 * 类 名 称：DiscussGroupListPresenter
 * <P/>描    述：讨论组列表Presenter
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/21 18:14
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/21 18:14
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class DiscussGroupListPresenter extends BasePresenter {

    private IDiscussListView mView;

    public DiscussGroupListPresenter(IDiscussListView mView) {
        this.mView = mView;
    }

    public void loadDiscussGroupList() {
        mView.showProgress();
        String url = ApisDiscuss.discussGroupListUrl();
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                DiscussListResp resp = parseJson(response, DiscussListResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        if (resp.getData().size() == 0) {
                            mView.showNoData();
                        }
                        mView.showDiscussList(resp);
                    } else {
                        mView.showListFailed();
                    }
                } else {
                    mView.showListFailed();
                }
            }

            @Override
            public void onError(Exception e) {
                mView.hideProgress();
                mView.showListFailed();
            }
        });
    }
}

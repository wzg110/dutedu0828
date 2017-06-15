package com.yunding.dut.presenter.ppt;

import com.yunding.dut.model.resp.ppt.PPTListResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.ppt.IPPTListView;
import com.yunding.dut.util.api.ApisPPT;

/**
 * 类 名 称：PPTListPresenter
 * <P/>描    述：PPT列表控制器
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 16:17
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 16:17
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class PPTListPresenter extends BasePresenter {

    private IPPTListView mView;

    public PPTListPresenter(IPPTListView mView) {
        this.mView = mView;
    }

    public void loadPPTList(int courseId) {
        mView.showProgress();
        String url = ApisPPT.getPPTList(courseId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                PPTListResp resp = parseJson(response, PPTListResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        if (resp.getData().size() == 0) {
                            mView.showNoData();
                        } else {
                            mView.showPPTList(resp);
                        }
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

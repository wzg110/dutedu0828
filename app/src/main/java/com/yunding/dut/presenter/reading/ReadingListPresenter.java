package com.yunding.dut.presenter.reading;

import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.reading.IReadingListView;
import com.yunding.dut.util.api.ApisReading;

/**
 * 类 名 称：ReadingListPresenter
 * <P/>描    述：阅读列表控制器
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/24 16:30
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/24 16:30
 * <P/>修改备注：
 * <P/>版    本：
 */

public class ReadingListPresenter extends BasePresenter {

    private IReadingListView mView;

    public ReadingListPresenter(IReadingListView mView) {
        this.mView = mView;
    }

    public void getReadingList() {
        mView.showProgress();
        String url = ApisReading.getReadingList();
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                ReadingListResp resp = parseJson(response, ReadingListResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.showReadingList(resp);
                    } else {
                        mView.showMsg(resp.getMsg());
                    }
                } else {
                    mView.showMsg(null);
                }

            }

            @Override
            public void onError(Exception e) {
                mView.hideProgress();
                mView.showMsg(e.toString());
            }
        });

    }
}

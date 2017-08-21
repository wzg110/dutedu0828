package com.yunding.dut.presenter.reading;

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
//页面 view接口实例
    private IReadingListView mView;
    private static final String TAG = "ReadingListPresenter";
    public ReadingListPresenter(IReadingListView mView) {
        this.mView = mView;
    }
//获取接口中数据方法

    /**
     * 获取阅读课列表
     */
    public void getReadingList() {
        mView.showProgress();
        String url = ApisReading.getReadingList();
//        OKhttp发送请求
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {

                mView.hideProgress();
//                将返回值转bean
                try{

                    ReadingListResp resp = parseJson(response, ReadingListResp.class);


                if (resp != null) {
                    if (resp.isResult()) {
//                        成功刷新页面
                        mView.showReadingList(resp);
                    } else {
//                        失败提示
                        mView.showMsg(resp.getMsg());
                    }
                } else {
//                    大挂提示
                    mView.showMsg(null);
                }
                }catch (Exception e){
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

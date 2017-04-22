package com.yunding.dut.presenter.discuss;

import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.model.resp.discuss.DiscussMsgListResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.discuss.IDiscussView;
import com.yunding.dut.util.api.ApisDiscuss;
import com.zhy.http.okhttp.OkHttpUtils;

/**
 * 类 名 称：DiscussPresenter
 * <P/>描    述：讨论组消息页面逻辑
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/22 14:54
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/22 14:54
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DiscussPresenter extends BasePresenter {

    private IDiscussView mView;

    public DiscussPresenter(IDiscussView mView) {
        this.mView = mView;
    }

    public void startDiscussion(long subjectId, long groupId) {
        String url = ApisDiscuss.startDiscussion(subjectId, groupId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.showDiscussing();
                    } else {
                        mView.showMsg(resp.getMsg());
                    }
                } else {
                    mView.showMsg(null);
                }
            }

            @Override
            public void onError(Exception e) {
                mView.showMsg(e.toString());
            }
        });
    }

    public void refreshMsg(long subjectId, long groupId) {
        String url = ApisDiscuss.discussGroupMsgListUrl(subjectId, groupId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                DiscussMsgListResp resp = parseJson(response, DiscussMsgListResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.showMsgList(resp);
                    } else {
                        mView.showMsg(resp.getMsg());
                    }
                } else {
                    mView.showMsg(null);
                }
            }

            @Override
            public void onError(Exception e) {
                mView.showMsg(e.toString());
            }
        }, this);
    }

    public void stopRefreshMsg() {
        OkHttpUtils.getInstance().cancelTag(this);
    }
}

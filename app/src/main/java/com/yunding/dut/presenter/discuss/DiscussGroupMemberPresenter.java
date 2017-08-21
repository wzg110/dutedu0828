package com.yunding.dut.presenter.discuss;

import com.yunding.dut.model.resp.discuss.DiscussGroupInfoResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.discuss.IDiscussGroupView;
import com.yunding.dut.util.api.ApisDiscuss;

/**
 * 类 名 称：DiscussGroupMemberPresenter
 * <P/>描    述：讨论组成员页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/21 19:40
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/21 19:40
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class DiscussGroupMemberPresenter extends BasePresenter {

    private IDiscussGroupView mView;

    public DiscussGroupMemberPresenter(IDiscussGroupView mView) {
        this.mView = mView;
    }

    /**
     * 功能描述： 获取讨论组人员
     *
     * @param groupId [讨论组ID]
     */
    public void loadDiscussGroupMembers(long groupId) {
        mView.showProgress();
        String url = ApisDiscuss.discussGroupInfoUrl(groupId);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                DiscussGroupInfoResp resp = parseJson(response, DiscussGroupInfoResp.class);
                if (resp != null) {
                    if (resp.isResult()) {
                        mView.showGroupInfo(resp);
                    } else {

                    }
                } else {
                    mView.showGroupFailed("");
                }
            }

            @Override
            public void onError(Exception e) {
                mView.hideProgress();
                mView.showGroupFailed(e.toString());
            }
        });
    }
}

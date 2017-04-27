package com.yunding.dut.presenter.me;

import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.me.IMeInfoView;
import com.yunding.dut.util.api.ApisMeInfo;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

/**
 * 类 名 称：MeInfoPresenter
 * <P/>描    述：个人中心
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/27 14:30
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/27 14:30
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class MeInfoPresenter extends BasePresenter {

    private IMeInfoView mView;

    public MeInfoPresenter(IMeInfoView mView) {
        this.mView = mView;
    }

    public void uploadAvatar(String filePath) {
        File file = new File(filePath);
        String url = ApisMeInfo.getUploadAvatarUrl();
        mView.showProgress();
        OkHttpUtils.post()
                .url(url)
                .addParams("studentid", String.valueOf(DUTApplication.getUserInfo().getUserId()))
                .addFile("file", file.getName(), file)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mView.hideProgress();
                        mView.showMsg(e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mView.hideProgress();
                        CommonResp resp = parseJson(response, CommonResp.class);
                        if (resp != null) {
                            if (resp.isResult()) {
                                mView.showAvatar();
                            } else {
                                mView.showMsg(resp.getMsg());
                            }
                        } else {
                            mView.showMsg(null);
                        }
                    }
                });
    }
}

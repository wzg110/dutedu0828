package com.yunding.dut.presenter.me;

import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.CommonResp;
import com.yunding.dut.model.resp.CommonRespNew;
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

    /**
     * 功能描述：上传头像
     * @param filePath 头像地址
     */
    public void uploadAvatar(String filePath) {
        File file = new File(filePath);
        String url = ApisMeInfo.getUploadAvatarUrl();
        mView.showProgress();
        OkHttpUtils.post()
                .url(url)
                .addParams("studentid", String.valueOf(DUTApplication.getUserInfo().getUserId()))
                .addParams("userType",String.valueOf(DUTApplication.getUserInfo().getUSER_TYPE()))
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
                        CommonRespNew resp = parseJson(response, CommonRespNew.class);
                        if (resp != null) {
                            if (resp.isResult()) {
                                DUTApplication.getUserInfo().setUserAvatar(resp.getData());
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

    /**
     * 功能描述 ：游客修改姓名
     * @param name  [修改姓名]
     */
    public void changeName(String name){
        mView.showProgress();
        String url = ApisMeInfo.changeLoginName(name);
        request(url, new DUTResp() {
            @Override
            public void onResp(String response) {
                mView.hideProgress();
                CommonResp resp = parseJson(response, CommonResp.class);
                if (resp != null) {
                    mView.hideProgress();
                    if (resp.isResult()) {
                        mView.changeNameSuccess();
                    } else {
                        mView.showMsg(resp.getMsg());
                    }
                } else {
                    mView.hideProgress();
                    mView.showMsg(resp.getMsg());
                }
            }

            @Override
            public void onError(Exception e) {
                mView.hideProgress();
                mView.showMsg(e.getMessage());
            }
        });

    }
}

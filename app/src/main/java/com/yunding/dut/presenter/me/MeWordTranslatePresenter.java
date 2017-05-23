package com.yunding.dut.presenter.me;

import android.util.Log;

import com.yunding.dut.model.resp.translate.JSTranslateBean;
import com.yunding.dut.model.resp.translate.YDTranslateBean;
import com.yunding.dut.presenter.base.BasePresenter;
import com.yunding.dut.ui.me.IMeWordTranslateView;
import com.yunding.dut.util.api.ApisTranslate;

/**
 * Created by Administrator on 2017/5/23.
 */

public class MeWordTranslatePresenter extends BasePresenter{

    private IMeWordTranslateView mIMeWordTranslateView;

    public MeWordTranslatePresenter(IMeWordTranslateView IMeWordTranslateView) {
        mIMeWordTranslateView = IMeWordTranslateView;
    }
    /**
     * 获取滑词翻译
     * @param content
     */
    public void getTranslation(String content){
        mIMeWordTranslateView.showProgress();
        if (content.contains(" ")){
            //使用有道翻译
            request(ApisTranslate.getYDTRANSLATE(content), new BasePresenter.DUTResp() {
                @Override
                public void onResp(String response) {
                    YDTranslateBean ydTranslateBean = parseJson(response,YDTranslateBean.class);
                    if (ydTranslateBean!=null){


                        switch (ydTranslateBean.getErrorCode()){
                            case 20: mIMeWordTranslateView.showMsg("要翻译的文本过长");
                                break;
                            case 30:mIMeWordTranslateView.showMsg("无法进行有效的翻译");
                                break;
                            case 40:mIMeWordTranslateView.showMsg("不支持的语言类型");
                                break;
                            case 50:mIMeWordTranslateView.showMsg("key值失效，请联系客服");
                                break;
                            case 60:mIMeWordTranslateView.showMsg("无词典结果");
                                break;
                            case 0:mIMeWordTranslateView.YDTranslate(ydTranslateBean);

                        }
                    }else mIMeWordTranslateView.showMsg("翻译失败");
                    mIMeWordTranslateView.hideProgress();
                }

                @Override
                public void onError(Exception e) {
                    mIMeWordTranslateView.showMsg(e.getMessage());
                    mIMeWordTranslateView.hideProgress();
                }
            });
        }else {
            content = content.toLowerCase();
            //使用金山翻译
            request(ApisTranslate.getJSTRANSLATE(content), new BasePresenter.DUTResp() {
                @Override
                public void onResp(String response) {
                    if (!response.isEmpty()){
                        JSTranslateBean jsTranslateBean = parseJson(response,JSTranslateBean.class);
                        if (jsTranslateBean!=null){
                            mIMeWordTranslateView.JSTranslate(jsTranslateBean);
                        }else mIMeWordTranslateView.showMsg("翻译失败");

                    }
                    mIMeWordTranslateView.hideProgress();
                }

                @Override
                public void onError(Exception e) {
                    mIMeWordTranslateView.hideProgress();
                    mIMeWordTranslateView.showMsg(e.getMessage());
                }
            });

        }



    }



}

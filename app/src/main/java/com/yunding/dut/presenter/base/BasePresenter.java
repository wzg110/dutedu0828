package com.yunding.dut.presenter.base;

import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.jetbrains.annotations.NotNull;

import okhttp3.Call;

/**
 * 类 名 称：BasePresenter
 * <P/>描    述：Presenter基类
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/20 15:00
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/20 15:00
 * <P/>修改备注：
 * <P/>版    本：
 */
public class BasePresenter {

    private final int ACCOUNT_MIN_LENGTH = 0;
    private final int ACCOUNT_MAX_LENGTH = 16;
    private final int NAME_MIN_LENGTH = 0;
    private final int NAME_MAX_LENGTH = 6;
    private final int PWD_MIN_LENGTH = 6;
    private final int PWD_MAX_LENGTH = 16;
//    20170622添加
    private final String PHONE_REGEX="^(((13[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(18[0-9]{1}))+\\\\d{8})$";

    public String trimStr(String str) {
        return str.replaceAll(" ", "");
    }

    /**
     * 功能简述:
     * 校验手机号码防止短信发送浪费
     * @param phonecode
     * @return
     */
    public boolean isMobile(String phonecode){
        if (phonecode.length() != 11)
            return false;

        return phonecode.matches(PHONE_REGEX);
    }

    /**
     * 功能简述:
     *
     * @param account [学号]
     * @return boolean [合法：true,不合法：false]
     */
    public boolean isValidAccount(String account) {
        return !(account.length() > ACCOUNT_MAX_LENGTH || account.length() < ACCOUNT_MIN_LENGTH);
    }

    /**
     * 功能简述:
     *
     * @param name [姓名]
     * @return boolean [合法：true,不合法：false]
     */
    public boolean isValidName(String name) {
        return !(name.length() > NAME_MAX_LENGTH || name.length() < NAME_MIN_LENGTH);
    }

    /**
     * 功能简述:
     *
     * @param pwd [密码]
     * @return boolean [合法：true,不合法：false]
     */
    public boolean isValidPwd(String pwd) {
        return !(pwd.length() > PWD_MAX_LENGTH || pwd.length() < PWD_MIN_LENGTH);
    }

    private final String TAG_URL = "request_url=";
    private final String TAG_RESP = "request_resp=";

    /**
     * 功能简述:公共请求
     *
     * @param url  [请求地址]
     * @param resp [回掉接口]
     */
    public void request(@NotNull final String url, final DUTResp resp) {
//        Log.e(TAG_URL, url);
        OkHttpUtils.get().tag(this).url(url).build().connTimeOut(20000).readTimeOut(20000).writeTimeOut(20000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Log.e(TAG_RESP, e.toString());
                if (resp != null)
                    resp.onError(e);
            }

            @Override
            public void onResponse(String response, int id) {
//                Log.e(TAG_RESP, response);
                if (resp != null)
                    resp.onResp(response);
            }
        });
    }

    /**
     * 功能简述:公共请求
     *
     * @param url  [请求地址]
     * @param resp [回掉接口]
     */
    public void request(@NotNull final String url, final DUTResp resp, Object tag) {
//        Log.e(TAG_URL, url);
        OkHttpUtils.get().tag(tag).url(url).build()
                .connTimeOut(20000).readTimeOut(20000)
                .writeTimeOut(20000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                Log.e(TAG_RESP, e.toString());
                if (resp != null)
                    resp.onError(e);
            }

            @Override
            public void onResponse(String response, int id) {
//                Log.e(TAG_RESP, response);
                if (resp != null)
                    resp.onResp(response);
            }
        });
    }

    protected <T> T parseJson(@Nullable String json, @NotNull Class<T> classOfT) {
        try {
            return new Gson().fromJson(json, classOfT);
        } catch (Exception e) {

            Log.e("exceptionBase",e.getMessage());
            return null;
        }
    }

    public interface DUTResp {

        void onResp(String response);

        void onError(Exception e);
    }
}

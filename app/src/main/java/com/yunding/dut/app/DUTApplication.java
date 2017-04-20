package com.yunding.dut.app;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.yunding.dut.model.data.UserInfo;

/**
 * 类 名 称：DUTApplication
 * <P/>描    述：Application
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/19 11:47
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/19 11:47
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public class DUTApplication extends Application {

    private static DUTApplication mApplication;
    private static UserInfo mUserInfo;

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;

        Fresco.initialize(this);
    }

    public static DUTApplication getInstance() {
        return mApplication;
    }

    public static UserInfo getUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = new UserInfo();
        }
        return mUserInfo;
    }
}

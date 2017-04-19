package com.yunding.dut.app;

import android.app.Application;

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

    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;
    }

    public static DUTApplication getmInstance() {
        return mApplication;
    }
}

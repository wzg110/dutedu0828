package com.yunding.dut.app;

import android.app.Application;
import android.graphics.Typeface;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.yunding.dut.R;
import com.yunding.dut.model.data.UserInfo;
import com.yunding.dut.util.third.ACache;
import com.yunding.dut.util.third.Utils;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

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
    private static int isShowUpdateDialog=0;
    private static ACache mCache;
    private static DUTApplication mApplication;
    private static UserInfo mUserInfo;
    private static  Typeface hsbsTypeFace;
    private  static Typeface zhTypeFace;
    @Override
    public void onCreate() {
        super.onCreate();

        mApplication = this;

        Utils.init(this);
        Fresco.initialize(this);
        hsbsTypeFace=Typeface.createFromAsset(this.getAssets(),"fonts/hsbw.ttf");
        zhTypeFace=Typeface.createFromAsset(this.getAssets(),"fonts/zh_cn.ttf");
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/zh_cn.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        mCache = ACache.get(this);
        ZXingLibrary.initDisplayOpinion(this);
    }
    public static ACache getAcache(){
        return mCache;
    }

    public static DUTApplication getInstance() {
        return mApplication;
    }
    public static int getIsShowUpdateDialog(){
        return isShowUpdateDialog;
    }
    public  void  setIsShowUpdateDialog(int s){
       this.isShowUpdateDialog=s;
    }

    public static UserInfo getUserInfo() {
        if (mUserInfo == null) {
            mUserInfo = new UserInfo();
        }
        return mUserInfo;
    }
    public static Typeface getHsbwTypeFace(){
        return hsbsTypeFace;
    }
    public static  Typeface getZhTypeFace(){
        return zhTypeFace;
    }

}

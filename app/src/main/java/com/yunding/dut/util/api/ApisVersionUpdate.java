package com.yunding.dut.util.api;

import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.util.third.AppUtils;

/**
 * 类 名 称：ReadingInputQuestionFragment
 * <P/>描    述：阅读填空题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/8/2
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/8/2
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class ApisVersionUpdate extends Apis {
    /**
     * 轮询版本更新
     * @return
     */

    public static String appUpdateRolling() {
        String  url= Apis.SERVER_URL+"versionupdatecheck?os=0&version="+
                AppUtils.getAppVersionName(DUTApplication.getInstance().getApplicationContext());
        return url;
    }
}

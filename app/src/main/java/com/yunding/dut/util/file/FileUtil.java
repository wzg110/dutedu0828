package com.yunding.dut.util.file;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * 类 名 称：FileUtil
 * <P/>描    述：文件类
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/23 15:31
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/23 15:31
 * <P/>修改备注：
 * <P/>版    本：
 */

public class FileUtil {

    private static final String APP_ROOT_DIR = Environment.getExternalStorageDirectory() + "/DUT";

//    private static final String RECORD_SUFFIX = ".voice";
private static final String RECORD_SUFFIX = ".mp3";

    public static String getRecordVoiceDir() {
        String voiceDir = APP_ROOT_DIR + "/voice/record";
        File file = new File(voiceDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return voiceDir;
    }

    public static String getDownloadVoiceDir() {
        String voiceDir = APP_ROOT_DIR + "/voice/download";
        File file = new File(voiceDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return voiceDir;
    }

    public static String getAvatarPath() {
        String avatarDir = APP_ROOT_DIR + "/img/avatar/avatar.jpg";
        File file = new File(avatarDir);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
//                Log.e("getAvatarPath", e.toString());
            }
        }
        return avatarDir;
    }

    public static String getRecordSuffix() {
        return RECORD_SUFFIX;
    }
}

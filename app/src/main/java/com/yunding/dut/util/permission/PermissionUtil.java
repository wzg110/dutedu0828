package com.yunding.dut.util.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.yunding.dut.app.DUTApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 类 名 称：PermissionUtil
 * <P/>描    述：权限类
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/24 10:12
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/24 10:12
 * <P/>修改备注：
 * <P/>版    本：
 */

public class PermissionUtil {

    public static int PERMISSION_REQUEST_CODE = 1001;

    private static void requestDUTPermission(Context context) {
        List<String> permissionsNeeded = new ArrayList<>();
        permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        permissionsNeeded.add(Manifest.permission.WAKE_LOCK);
        permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsNeeded.add(Manifest.permission.CAMERA);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        ActivityCompat.requestPermissions((Activity) context, permissionsNeeded.toArray(new String[permissionsNeeded.size()]), PERMISSION_REQUEST_CODE);
    }

    public static boolean checkDUTPermission(Context context){
        int recordAudioPermission = ContextCompat.checkSelfPermission(DUTApplication.getInstance().getApplicationContext(),Manifest.permission.RECORD_AUDIO);
        int wakeLockPermission = ContextCompat.checkSelfPermission(DUTApplication.getInstance().getApplicationContext(),Manifest.permission.WAKE_LOCK);
        int writePermission = ContextCompat.checkSelfPermission(DUTApplication.getInstance().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int locationfPermission=ContextCompat.checkSelfPermission(DUTApplication.getInstance().getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION);
        int cameraPermission=ContextCompat.checkSelfPermission(DUTApplication.getInstance().getApplicationContext(),Manifest.permission.CAMERA);
        if(cameraPermission!=PackageManager.PERMISSION_GRANTED){
            requestDUTPermission(context);
            return false;
        }
        if(recordAudioPermission!=PackageManager.PERMISSION_GRANTED){
            requestDUTPermission(context);
            return false;
        }
        if (locationfPermission!=PackageManager.PERMISSION_GRANTED){
            requestDUTPermission(context);
            return false;
        }
        if(wakeLockPermission!=PackageManager.PERMISSION_GRANTED){
            requestDUTPermission(context);
            return false;
        }
        if(writePermission!=PackageManager.PERMISSION_GRANTED){
            requestDUTPermission(context);
            return false;
        }
        return true;
    }
}

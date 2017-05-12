package com.yunding.dut.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.ui.account.BindPhoneActivity;
import com.yunding.dut.ui.account.LoginActivity;
import com.yunding.dut.ui.base.BaseActivity;
import com.yunding.dut.ui.home.HomeActivity;
import com.yunding.dut.util.permission.PermissionUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        checkPermissions();
    }

    private void checkBindState() {
        if (TextUtils.isEmpty(DUTApplication.getUserInfo().getUserPhone())) {
            //未绑定
            startActivity(new Intent(this, BindPhoneActivity.class));
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void goNext() {
        Observable.timer(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                long userId = DUTApplication.getUserInfo().getUserId();
                if (userId == 0) {
                    startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                } else {
                    checkBindState();
                }
                finish();
            }
        });
    }

    private void checkPermissions() {
        if (PermissionUtil.checkDUTPermission(this)) {
            goNext();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @android.support.annotation.NonNull String[] permissions, @android.support.annotation.NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.PERMISSION_REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                goNext();
            } else {
                new MaterialDialog.Builder(this)
                        .title("抱歉")
                        .content("讨论组功能需要存储权限，不开启将无法正常工作")
                        .positiveText("确定")
                        .show();
            }
        }
    }
}

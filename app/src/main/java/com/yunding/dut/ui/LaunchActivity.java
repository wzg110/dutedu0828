package com.yunding.dut.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.ui.account.LoginActivity;
import com.yunding.dut.ui.base.BaseActivity;
import com.yunding.dut.ui.home.HomeActivity;

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

        Observable.timer(1, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                long userId = DUTApplication.getUserInfo().getUserId();
                if (userId == 0) {
                    startActivity(new Intent(LaunchActivity.this, LoginActivity.class));
                } else {
                    startActivity(new Intent(LaunchActivity.this, HomeActivity.class));
                }
                finish();
            }
        });

    }
}

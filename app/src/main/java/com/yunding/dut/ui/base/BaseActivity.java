package com.yunding.dut.ui.base;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yunding.dut.app.DUTApplication;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void showToast(int msgId) {
        Toast.makeText(DUTApplication.getmInstance(), msgId, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String msg) {
        Toast.makeText(DUTApplication.getmInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showSnackBar(int msgId) {
        Snackbar.make(getWindow().getDecorView(), msgId, Snackbar.LENGTH_SHORT).show();
    }

    protected void showSnackBar(String msg) {
        Snackbar.make(getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    protected void showProgress() {

    }

    protected void hideProgress() {

    }
}

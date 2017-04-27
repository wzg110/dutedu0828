package com.yunding.dut.ui.base;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.view.DUTProgressDialog;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showToast(int msgId) {
        Toast.makeText(DUTApplication.getInstance(), msgId, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String msg) {
        Toast.makeText(DUTApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showSnackBar(int msgId) {
        Snackbar.make(getWindow().getDecorView(), msgId, Snackbar.LENGTH_SHORT).show();
    }

    public void showSnackBar(String msg) {
        Snackbar.make(getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    DUTProgressDialog progress;

    protected void showProgressDialog() {
        progress = new DUTProgressDialog()
                .show(getSupportFragmentManager());
        progress.setCancelable(false);
    }

    protected void hideProgressDialog() {
        if (progress != null) {
            progress.dismissAllowingStateLoss();
        }
    }

    protected void showProgressBar(){

    }

    protected void hideProgressBar(){

    }
}

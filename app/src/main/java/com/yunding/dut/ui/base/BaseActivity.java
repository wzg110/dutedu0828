package com.yunding.dut.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.view.DUTProgressDialog;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void showToast(int msgId) {
        Toast.makeText(DUTApplication.getInstance(), msgId, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String msg) {
        if (msg.contains("Exception")) {
            Toast.makeText(DUTApplication.getInstance(), R.string.net_work_error, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(DUTApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
        }
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

    protected void showProgressBar() {

    }

    protected void hideProgressBar() {

    }
}

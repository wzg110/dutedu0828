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

    protected void showProgressDialog() {

    }

    protected void hideProgressDialog() {

    }

    protected void showProgressBar(){

    }

    protected void hideProgressBar(){

    }
}

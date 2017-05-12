package com.yunding.dut.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;

import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.presenter.account.BindPhonePresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.ui.home.HomeActivity;
import com.yunding.dut.util.third.RegexUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BindPhoneActivity extends ToolBarActivity implements IBindPhoneView {

    @BindView(R.id.et_phone)
    TextInputEditText etPhone;
    @BindView(R.id.til_phone)
    TextInputLayout tilPhone;
    @BindView(R.id.btn_send_sms_code)
    Button btnSendSmsCode;
    @BindView(R.id.et_sms_code)
    TextInputEditText etSmsCode;
    @BindView(R.id.til_sms_code)
    TextInputLayout tilSmsCode;
    @BindView(R.id.btn_next)
    Button btnNext;

    private BindPhonePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);

        setTitle("绑定手机号");
        mPresenter = new BindPhonePresenter(this);
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @OnClick({R.id.btn_send_sms_code, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_send_sms_code:
                sendSmsCode();
                break;
            case R.id.btn_next:
                bindPhone();
                break;
        }
    }

    /**
     * 功能简述:绑定手机号
     */
    private void bindPhone() {
        String phone = etPhone.getText().toString();
        String smsCode = etSmsCode.getText().toString();
        long studentId = DUTApplication.getUserInfo().getUserId();
        mPresenter.bindPhone(phone, smsCode, studentId);
    }

    /**
     * 功能简述:发送短信验证码
     */
    private void sendSmsCode() {
        String phone = etPhone.getText().toString();
        if (!RegexUtils.isMobileSimple(phone)) {
            showToast("手机号码不合法");
            return;
        }
        mPresenter.sendSmsCode(phone);
        Observable.intervalRange(1, 60, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        btnSendSmsCode.setText(String.valueOf(aLong) + "s");
                        if (aLong == 60) {
                            btnSendSmsCode.setText("重新发送");
                        }
                    }
                });
    }

    @Override
    public void showMsg(String msg) {
        showToast(msg);
    }

    @Override
    public void bindSuccess() {
        DUTApplication.getUserInfo().setUserPhone(etPhone.getText().toString());
        showMsg("绑定成功");
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}

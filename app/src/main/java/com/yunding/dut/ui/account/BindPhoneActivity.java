package com.yunding.dut.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.presenter.account.BindPhonePresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.ui.home.HomeActivity;
import com.yunding.dut.util.third.BarUtils;
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
/**
 * 类 名 称：BindPhoneActivity
 * <P/>描    述：绑定手机页面
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 11:10
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 11:10
 * <P/>修改备注：
 * <P/>版    本：
 */
public class BindPhoneActivity extends ToolBarActivity implements IBindPhoneView {


    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_sms_code)
    EditText etSmsCode;
    @BindView(R.id.btn_send_sms_code)
    Button btnSendSmsCode;
    @BindView(R.id.ll_sms_code)
    LinearLayout llSmsCode;
    @BindView(R.id.btn_next)
    Button btnNext;
    private BindPhonePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);
        setTitleInCenter("账号安全");
        getmToolbar().setBackgroundColor(getResources().getColor(R.color.bg_white));
        getmToolbarTitle().setTextColor(getResources().getColor(R.color.textColorShow));
        BarUtils.setColor(this, getResources().getColor(R.color.bg_white));
        setShowNavigation(true);
        getmToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BindPhoneActivity.this.finish();
            }
        });
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

                mPresenter.checkPhone(etPhone.getText().toString());
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
                        btnSendSmsCode.setText((60-aLong) + "s");
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

    @Override
    public void checkSuccess() {
        sendSmsCode();
    }


}

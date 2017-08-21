package com.yunding.dut.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.yunding.dut.R;
import com.yunding.dut.presenter.account.FindPwdPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
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
 * 类 名 称：FindPwdStep1Activity
 * <P/>描    述： 修改密码
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 11:13
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 11:13
 * <P/>修改备注：
 * <P/>版    本：
 */
public class FindPwdStep1Activity extends ToolBarActivity implements IFindPwdViewStep1 {


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
    private FindPwdPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd_step_1);
        ButterKnife.bind(this);

//        setTitle("修改密码");
        setTitleInCenter(getResources().getString(R.string.register_forgetpwd_title));
        getmToolbar().setBackgroundColor(getResources().getColor(R.color.bg_white));
        getmToolbarTitle().setTextColor(getResources().getColor(R.color.textColorShow));
        BarUtils.setColor(this, getResources().getColor(R.color.bg_white));
        mPresenter = new FindPwdPresenter(this);
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
                nextStep();
                break;
        }
    }

    /**
     * 功能简述:下一步
     */
    private void nextStep() {
        String phone = etPhone.getText().toString();
        String code = etSmsCode.getText().toString();
        if (!RegexUtils.isMobileSimple(phone)) {
            showToast("请输入合法手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            showToast("请输入6位短信验证码");
            return;
        }
        Intent intent = new Intent(this, FindPwdStep2Activity.class);
        intent.putExtra(FindPwdStep2Activity.SMS_CODE, code);
        intent.putExtra(FindPwdStep2Activity.PHONE, phone);
        startActivity(intent);
    }

    /**
     * 功能简述:发送短信验证码，并开始倒计时
     */
    private void countDown() {
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
                        btnSendSmsCode.setText((60-aLong)+ "s");
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
    public void checkSuccess() {
        countDown();
    }


}

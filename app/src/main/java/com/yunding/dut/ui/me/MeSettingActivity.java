package com.yunding.dut.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.yunding.dut.R;
import com.yunding.dut.presenter.account.AccountPresenter;
import com.yunding.dut.ui.account.LoginActivity;
import com.yunding.dut.ui.account.ResetPwdActivity;
import com.yunding.dut.ui.base.ToolBarActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeSettingActivity extends ToolBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_setting);
        ButterKnife.bind(this);

        setTitle(getString(R.string.me_setting_title));
    }

    @OnClick({R.id.tv_reset_pwd, R.id.tv_log_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_reset_pwd:
                startActivity(new Intent(this, ResetPwdActivity.class));
                break;
            case R.id.tv_log_out:
                AccountPresenter.logOut();
                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
    }
}

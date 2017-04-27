package com.yunding.dut.ui.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.ui.base.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeActivity extends ToolBarActivity {

    @BindView(R.id.img_avatar)
    SimpleDraweeView imgAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.tv_class)
    TextView tvClass;
    @BindView(R.id.ll_my_info)
    LinearLayout llMyInfo;
    @BindView(R.id.tv_setting)
    TextView tvSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);

        setTitle(getString(R.string.me_center_title));
    }

    private void showUserInfo() {
        imgAvatar.setImageURI(Uri.parse(DUTApplication.getUserInfo().getUserAvatar()));
        tvName.setText(DUTApplication.getUserInfo().getUserName());
        tvGrade.setText(DUTApplication.getUserInfo().getUserGrade());
        tvClass.setText(DUTApplication.getUserInfo().getUserClass());
    }

    @OnClick({R.id.img_avatar, R.id.ll_my_info, R.id.tv_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_my_info:
                startActivity(new Intent(this, MeInfoActivity.class));
                break;
            case R.id.tv_setting:
                startActivity(new Intent(this, MeSettingActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showUserInfo();
    }
}

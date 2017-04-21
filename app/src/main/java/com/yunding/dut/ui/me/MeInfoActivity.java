package com.yunding.dut.ui.me;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.util.api.Apis;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeInfoActivity extends ToolBarActivity {

    @BindView(R.id.img_avatar)
    SimpleDraweeView imgAvatar;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.tv_class)
    TextView tvClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_info);
        ButterKnife.bind(this);

        setTitle(getString(R.string.me_info_title));
        showUserInfo();
    }

    private void showUserInfo() {
        imgAvatar.setImageURI(Apis.SERVER_URL + Uri.parse(DUTApplication.getUserInfo().getUserAvatar()));
        tvAccount.setText(DUTApplication.getUserInfo().getUserAccount());
        tvName.setText(DUTApplication.getUserInfo().getUserName());
        tvGrade.setText(DUTApplication.getUserInfo().getUserGrade());
        tvClass.setText(DUTApplication.getUserInfo().getUserClass());
    }

    @OnClick(R.id.img_avatar)
    public void onViewClicked() {
    }
}

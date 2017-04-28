package com.yunding.dut.ui.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yalantis.ucrop.UCrop;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.presenter.me.MeInfoPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.util.api.Apis;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPickUtils;
import me.iwf.photopicker.PhotoPicker;

public class MeInfoActivity extends ToolBarActivity implements IMeInfoView {

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

    private MeInfoPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_info);
        ButterKnife.bind(this);

        setTitle(getString(R.string.me_info_title));
        showUserInfo();

        mPresenter = new MeInfoPresenter(this);
    }

    private void showUserInfo() {
        String avatarPath = DUTApplication.getUserInfo().getUserAvatar();
        imgAvatar.setImageURI(Apis.SERVER_URL+avatarPath);
        tvAccount.setText(DUTApplication.getUserInfo().getUserAccount());
        tvName.setText(DUTApplication.getUserInfo().getUserName());
        tvGrade.setText(DUTApplication.getUserInfo().getUserGrade());
        tvClass.setText(DUTApplication.getUserInfo().getUserClass());
    }

    @OnClick({R.id.img_avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_avatar:
                ArrayList<String> pathList = new ArrayList<>();
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(false)
                        .setSelected(pathList)
                        .setPreviewEnabled(true)
                        .start(this, PhotoPicker.REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        PhotoPickUtils.onActivityResult(requestCode, resultCode, data, new PhotoPickUtils.PickHandler() {
            @Override
            public void onPickSuccess(ArrayList<String> photos) {
                mPresenter.uploadAvatar(photos.get(0));
//                UCrop.of(Uri.fromFile(new File(photos.get(0))), Uri.fromFile(new File(FileUtil.getAvatarPath())))
//                        .withAspectRatio(1, 1)
//                        .withMaxResultSize(200, 200)
//                        .start(MeInfoActivity.this);
            }

            @Override
            public void onPreviewBack(ArrayList<String> photos) {

            }

            @Override
            public void onPickFail(String error) {
                showToast(error);
            }

            @Override
            public void onPickCancle() {
                showToast("取消选择");
            }
        });

        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                showToast(resultUri.getPath());
                DUTApplication.getUserInfo().setUserAvatar(resultUri.toString());
                mPresenter.uploadAvatar(resultUri.getPath());
                showUserInfo();
            } else {
                showToast("选取失败");
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            if (cropError != null) {
                showToast(cropError.getMessage());
            }
        }
    }

    @Override
    public void showProgress() {
        showProgressDialog();
    }

    @Override
    public void hideProgress() {
        hideProgressDialog();
    }

    @Override
    public void showAvatar() {
        showUserInfo();
    }

    @Override
    public void showMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            showToast(R.string.server_error);
        } else {
            showToast(msg);
        }
    }
}

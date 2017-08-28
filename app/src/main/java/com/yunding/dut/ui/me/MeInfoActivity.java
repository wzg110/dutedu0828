package com.yunding.dut.ui.me;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yalantis.ucrop.UCrop;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.presenter.me.MeInfoPresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.util.FontsUtils;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.FastBlurUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPickUtils;
import me.iwf.photopicker.PhotoPicker;
/**
 * 类 名 称：MeInfoActivity
 * <P/>描    述：个人中心页面
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 16:38
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 16:38
 * <P/>修改备注：
 * <P/>版    本：
 */
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
    private Dialog dialog;
    private View inflate;
    private TextView tv_take_photo, tv_choose_file, tv_cancel;
    private static final int CAMERA_REQUEST_CODE=0x01;
    private static final int RESULT_REQUEST_CODE = 2;
    private Uri photoUri;
    /** 从Intent获取图片路径的KEY */
    private static final String IMAGE_FILE_NAME = "image.jpg";
    private LinearLayout ll_grade;
    private  TextView tv_number;
    private  String  newName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_info);
        ButterKnife.bind(this);
        setTitleInCenter(getString(R.string.me_info_title));
        showUserInfo();
        mPresenter = new MeInfoPresenter(this);
        ll_grade= (LinearLayout) this.findViewById(R.id.ll_grade);
        tv_number= (TextView) this.findViewById(R.id.tv_number);
        if (DUTApplication.getUserInfo().getUSER_TYPE().equals("0")){
            ll_grade.setVisibility(View.VISIBLE);
            tv_number.setText("学号");
        }else{
            ll_grade.setVisibility(View.GONE);
            tv_number.setText("用户名");

        }
    }

    private void showUserInfo() {

            String avatarPath = DUTApplication.getUserInfo().getUserAvatar();
            imgAvatar.setImageURI(Apis.SERVER_URL + avatarPath);


        if (FontsUtils.isContainChinese(DUTApplication.getUserInfo().getUserAccount())
                || TextUtils.isEmpty(DUTApplication.getUserInfo().getUserAccount())) {

        } else {
            tvAccount.setTypeface(DUTApplication.getHsbwTypeFace());
        }
        if (FontsUtils.isContainChinese(DUTApplication.getUserInfo().getUserName())
                || TextUtils.isEmpty(DUTApplication.getUserInfo().getUserName())) {

        } else {
            tvName.setTypeface(DUTApplication.getHsbwTypeFace());
        }
        if (FontsUtils.isContainChinese(DUTApplication.getUserInfo().getUserGrade())
                || TextUtils.isEmpty(DUTApplication.getUserInfo().getUserGrade())) {

        } else {
            tvGrade.setTypeface(DUTApplication.getHsbwTypeFace());
        }
        if (FontsUtils.isContainChinese(DUTApplication.getUserInfo().getUserClass())
                || TextUtils.isEmpty(DUTApplication.getUserInfo().getUserClass())) {

        } else {
            tvClass.setTypeface(DUTApplication.getHsbwTypeFace());
        }


        tvAccount.setText(DUTApplication.getUserInfo().getUserAccount());
        tvName.setText(DUTApplication.getUserInfo().getUserName());
        tvGrade.setText(DUTApplication.getUserInfo().getUserGrade());
        tvClass.setText(DUTApplication.getUserInfo().getUserClass());
    }


    private void showDialog() {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        inflate = LayoutInflater.from(this).inflate(R.layout.layout_chooseway2_changehead, null);
        tv_take_photo = (TextView) inflate.findViewById(R.id.tv_take_photo);
        tv_choose_file = (TextView) inflate.findViewById(R.id.tv_choose_file);
        tv_cancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        tv_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               takePhoto();
                if (dialog != null) {
                    dialog.dismiss();
                }

            }
        });
        tv_choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> pathList = new ArrayList<>();
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(false)
                        .setSelected(pathList)
                        .setPreviewEnabled(true)
                        .start(MeInfoActivity.this, PhotoPicker.REQUEST_CODE);
                if (dialog != null) {
                    dialog.dismiss();
                }

            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }

            }
        });
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager m = this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 1); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

//
//        lp.y = 200;//设置Dialog距离底部的距离
//       将属性设置给窗体
//        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框

    }

    private void takePhoto() {
        Intent intentFromCapture = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        String state = Environment
                .getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File path = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File file = new File(path, IMAGE_FILE_NAME);
            intentFromCapture.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(file));
        }

        startActivityForResult(intentFromCapture,
                CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        PhotoPickUtils.onActivityResult(requestCode, resultCode, data, new PhotoPickUtils.PickHandler() {
            @Override
            public void onPickSuccess(ArrayList<String> photos) {
                mPresenter.uploadAvatar(photos.get(0));
//                UCrop.of(Uri.fromFile(new File(photos.get(0))),
//                        Uri.fromFile(new File(FileUtil.getAvatarPath())))
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
        if (resultCode==RESULT_OK&&requestCode==CAMERA_REQUEST_CODE){
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                File path = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                File tempFile = new File(path, IMAGE_FILE_NAME);
//                startPhotoZoom(Uri.fromFile(tempFile));

                mPresenter.uploadAvatar(tempFile.getPath());

            } else {
                Toast.makeText(getApplicationContext(),
                        "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
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
        showMsg("上传成功");
        showUserInfo();
        
        saveHeadBg();
    }

    private void saveHeadBg() {
        if (TextUtils.isEmpty(DUTApplication.getUserInfo().getUserAvatar())){
        }else{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String blurBitmap2 = FastBlurUtil.GetUrlBitmap
                            (Apis.SERVER_URL + DUTApplication.getUserInfo().getUserAvatar(), 10,getApplicationContext());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            DUTApplication.getUserInfo().setUserHeadBg(blurBitmap2);
                        }
                    });
                }
            }).start();

        }
    }

    @Override
    public void showMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            showToast(R.string.server_error);
        } else {
            showToast(msg);
        }
    }

    @Override
    public void changeNameSuccess() {
        showToast("修改成功");
        tvName.setText(newName);
        DUTApplication.getUserInfo().setUserName(newName);

    }


    @OnClick({R.id.img_avatar,R.id.tv_name})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case  R.id.img_avatar:
                showDialog();
                break;
            case R.id.tv_name:
                if (tvName.getText().toString().trim().startsWith("游客")){

                    showChangeNameDialog();

                }else{

                }
                break;

        }

    }

    private void showChangeNameDialog() {
        dialog=new Dialog(this,R.style.ActionSheetDialogStyle);
        inflate= LayoutInflater.from(this).inflate(R.layout.change_name,null);
        Button btncancel=(Button)inflate.findViewById(R.id.btn_cancel);
        Button btnok=(Button)inflate.findViewById(R.id.btn_ok);
        final EditText et= (EditText) inflate.findViewById(R.id.et_change_name);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()){
                    dialog.dismiss();
                }
            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName=et.getText().toString().trim();
                if (TextUtils.isEmpty(newName)){
                    showToast("姓名不能为空。");
                    return;
                }
                if (newName.length()>6){
                    showToast("输入姓名长度不合法(0~6位)");
                    return;
                }
                newName=et.getText().toString().trim();
                mPresenter.changeName(et.getText().toString().trim());
                dialog.dismiss();
            }
        });

        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.show();//显示对话框
    }
}

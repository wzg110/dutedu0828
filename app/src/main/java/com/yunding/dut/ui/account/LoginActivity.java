package com.yunding.dut.ui.account;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.account.QRCResp;
import com.yunding.dut.presenter.account.LoginPresenter;
import com.yunding.dut.ui.base.BaseActivity;
import com.yunding.dut.ui.home.HomeActivity;
import com.yunding.dut.util.permission.PermissionUtil;
import com.yunding.dut.util.third.ToastUtils;
import com.yunding.dut.view.DUTProgressDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类 名 称：LoginActivity
 * <P/>描    述：登录页面
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 11:16
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 11:16
 * <P/>修改备注：
 * <P/>版    本：
 */
public class LoginActivity extends BaseActivity implements ILoginView {


    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.et_account)
    EditText etAccount;
    @BindView(R.id.rl_login)
    RelativeLayout rlLogin;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.imageView1)
    ImageView imageView1;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.rl_pwd)
    RelativeLayout rlPwd;
    @BindView(R.id.scrollview1)
    ScrollView scrollview1;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_reset_pwd)
    Button btnResetPwd;
    @BindView(R.id.rl_anim)
    ImageView rlAnim;
    @BindView(R.id.rl_register)
    RelativeLayout rlRegister;
    @BindView(R.id.qrc)
    ImageView qrc;
    @BindView(R.id.btn_login_type_student)
    Button btnLoginTypeStudent;
    @BindView(R.id.btn_login_type_visitor)
    Button btnLoginTypeVisitor;
    @BindView(R.id.btn_login_type_teacher)
    Button btnLoginTypeTeacher;
    @BindView(R.id.ll_login_type)
    LinearLayout llLoginType;
    @BindView(R.id.imageView2)
    ImageView imageView2;
    @BindView(R.id.et_visitor_account)
    EditText etVisitorAccount;
    @BindView(R.id.rl_login_visitor_input)
    RelativeLayout rlLoginVisitorInput;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.rl_login_visitor)
    RelativeLayout rlLoginVisitor;
    @BindView(R.id.rl_login_student_ro_teacher)
    RelativeLayout rlLoginStudentRoTeacher;
    private LoginPresenter mPresenter;
    private int userType = 0;//学生0 老师1

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() == null) {

        } else {
            getSupportActionBar().hide();
        }
        ButterKnife.bind(this);
        mPresenter = new LoginPresenter(this);
        etAccount.requestFocus();
        DUTApplication.getInstance().setIsShowUpdateDialog(0);
    }

private void clearEditData(){
    etAccount.setText("");
    etPwd.setText("");
    etVisitorAccount.setText("");
    etVisitorAccount.setHint("请输入邀请码");
}
    @OnClick({R.id.btn_login, R.id.btn_register, R.id.btn_reset_pwd
            , R.id.btn_login_type_student, R.id.btn_login_type_visitor, R.id.btn_login_type_teacher})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //切换学生登录
            case R.id.btn_login_type_student:
                userType = 0;
                btnLoginTypeStudent.setTextColor(getResources().getColor(R.color.textColorShow));
                btnLoginTypeTeacher.setTextColor(getResources().getColor(R.color.login_type));
                btnLoginTypeVisitor.setTextColor(getResources().getColor(R.color.login_type));
                clearEditData();
                etAccount.setHint("请输入学号");
                btnLogin.setText("登录");
                rlRegister.setVisibility(View.VISIBLE);
                rlLoginStudentRoTeacher.setVisibility(View.VISIBLE);
                rlLoginVisitor.setVisibility(View.GONE);
                break;
            //切换游客登录
            case R.id.btn_login_type_visitor:
                userType = 2;
                btnLoginTypeVisitor.setTextColor(getResources().getColor(R.color.textColorShow));
                btnLoginTypeTeacher.setTextColor(getResources().getColor(R.color.login_type));
                btnLoginTypeStudent.setTextColor(getResources().getColor(R.color.login_type));
                rlRegister.setVisibility(View.GONE);
                clearEditData();
                btnLogin.setText("进入课堂");
                rlLoginStudentRoTeacher.setVisibility(View.GONE);
                rlLoginVisitor.setVisibility(View.VISIBLE);
                break;
            //切换教师登录
            case R.id.btn_login_type_teacher:
                userType = 1;
                btnLoginTypeTeacher.setTextColor(getResources().getColor(R.color.textColorShow));
                btnLoginTypeStudent.setTextColor(getResources().getColor(R.color.login_type));
                btnLoginTypeVisitor.setTextColor(getResources().getColor(R.color.login_type));
                rlRegister.setVisibility(View.GONE);
                clearEditData();
                etAccount.setHint("请输入用户名");
                btnLogin.setText("登录");
                rlLoginStudentRoTeacher.setVisibility(View.VISIBLE);
                rlLoginVisitor.setVisibility(View.GONE);
                break;
            //登录
            case R.id.btn_login:
                if (userType != 2) {
                    String account = etAccount.getText().toString();
                    String pwd = etPwd.getText().toString();
                    mPresenter.login(account, pwd, userType + "");
                } else {
                    String inviteCode = etVisitorAccount.getText().toString().trim();
                    if (TextUtils.isEmpty(inviteCode)) {
                        showToast("邀请码不能为空");
                    } else {
                        mPresenter.visitorLoginWithInviteCode(inviteCode);
                    }
                }
                break;
            //注册
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            //找回密码
            case R.id.btn_reset_pwd:
                startActivity(new Intent(this, FindPwdStep1Activity.class));
                break;

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
    public void invalidAccount() {
        ToastUtils.showShortToast(getString(R.string.invalid_account));
    }

    @Override
    public void invalidPwd() {
        ToastUtils.showShortToast(getString(R.string.invalid_pwd));
    }

    @Override
    public void loginSuccess() {
        showToast(R.string.login_success);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void loginFailed(String msg) {
        showToast(msg);
    }

    @Override
    public void goBindPhone() {
        startActivity(new Intent(this, BindPhoneActivity.class));
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.qrc)
    public void onViewClicked() {
        //二维码扫描
        if (PermissionUtil.checkDUTPermission(this)){


        Intent intent = new Intent(LoginActivity.this, CaptureActivity.class);
        startActivityForResult(intent, 123);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
//                    Log.e("wsf",result);
                    if (result.contains("{") && result.contains("}")) {
                        try{
                            QRCResp parms =new Gson().fromJson(result, QRCResp.class);
                            mPresenter.visitorLogin(parms.getClassId(), parms.getTeacherId(),parms.getTeachingId(),parms.getSchoolCode());
                        }catch (Exception e){
                            showToast("请扫描正确二维码");
                        }

                    } else {
                        showToast("请扫描正确二维码");
                    }
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(LoginActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtil.PERMISSION_REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.CAMERA) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(LoginActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 123);
            } else {
                new MaterialDialog.Builder(this)
                        .title("抱歉")
                        .content("二维码登录需要相机权限，不开启将无法正常工作")
                        .positiveText("确定")
                        .show();
            }
        }
    }


}

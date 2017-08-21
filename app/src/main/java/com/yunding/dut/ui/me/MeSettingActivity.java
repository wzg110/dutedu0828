package com.yunding.dut.ui.me;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.yunding.dut.R;
import com.yunding.dut.presenter.account.AccountPresenter;
import com.yunding.dut.ui.account.LoginActivity;
import com.yunding.dut.ui.account.ResetPwdActivity;
import com.yunding.dut.ui.base.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 类 名 称：MeSettingActivity
 * <P/>描    述：设置页面
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 16:38
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 16:38
 * <P/>修改备注：
 * <P/>版    本：
 */
public class MeSettingActivity extends ToolBarActivity {

    @BindView(R.id.tv_reset_pwd)
    TextView tvResetPwd;
    @BindView(R.id.tv_log_out)
    TextView tvLogOut;
    private Dialog dialog;
    private Button btn_cancel,btn_ok;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_setting);
        ButterKnife.bind(this);
        setTitleInCenter(getString(R.string.me_setting_title));
    }

    @OnClick({R.id.tv_reset_pwd, R.id.tv_log_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_reset_pwd:
                startActivity(new Intent(this, ResetPwdActivity.class));
                break;
            case R.id.tv_log_out:
                showDialog();

                break;
        }
    }

    private void showDialog() {
        dialog=new Dialog(this,R.style.ActionSheetDialogStyle);
        view= LayoutInflater.from(this).inflate(R.layout.login_exit_dialog,null);
        btn_cancel= (Button) view.findViewById(R.id.btn_cancel);
        btn_ok= (Button) view.findViewById(R.id.btn_ok);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountPresenter.logOut();
                Intent intent = new Intent(MeSettingActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.show();//显示对话框

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

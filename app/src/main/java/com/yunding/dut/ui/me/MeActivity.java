package com.yunding.dut.ui.me;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.Space;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.ui.base.BaseActivity;
import com.yunding.dut.util.FontsUtils;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.FastBlurUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;
/**
 * 类 名 称：MeActivity
 * <P/>描    述： 个人信息页面
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 16:36
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 16:36
 * <P/>修改备注：
 * <P/>版    本：
 */
public class MeActivity extends BaseActivity {

    @BindView(R.id.test)
    ImageView test;
    @BindView(R.id.space8)
    Space space8;
    @BindView(R.id.btn_change_info)
    Button btnChangeInfo;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.rl_infos)
    RelativeLayout rlInfos;
    @BindView(R.id.space2)
    Space space2;
    @BindView(R.id.tv_my_word)
    TextView tvMyWord;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_avatar)
    SimpleDraweeView imgAvatar;
    private ImageView imageViewbackground;
    private static final String TAG = "MeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me);
        ButterKnife.bind(this);
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        imageViewbackground = (ImageView) this.findViewById(R.id.test);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    private void showUserInfo() {



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
//        Log.e("asdasd","头像背景"+DUTApplication.getUserInfo().getUSER_HEAD_BG());
//        Log.e("asdasd","头像"+DUTApplication.getUserInfo().getUserAvatar());

        if (TextUtils.isEmpty(DUTApplication.getUserInfo().getUSER_HEAD_BG())){

            if (TextUtils.isEmpty(DUTApplication.getUserInfo().getUserAvatar())){
                imgAvatar.setImageResource(R.drawable.ic_userhead);
                Glide.with(this).load(R.drawable.ic_userhead).bitmapTransform
                        (new BlurTransformation(this, 20,3), new CenterCrop(this))

                        .into(imageViewbackground);

            }else{

                if(TextUtils.equals("upload/imgs/moren.jpg",DUTApplication.getUserInfo().getUserAvatar())){
                    imgAvatar.setImageResource(R.drawable.ic_userhead);
                    Glide.with(this).load(R.drawable.ic_userhead).bitmapTransform
                            (new BlurTransformation(this, 20,3), new CenterCrop(this))

                            .into(imageViewbackground);
                }else{
                    imgAvatar.setImageURI(Apis.SERVER_URL + DUTApplication.getUserInfo().getUserAvatar());
                    Glide.with(this).load(Apis.SERVER_URL + DUTApplication.getUserInfo().getUserAvatar())
                            .bitmapTransform(new BlurTransformation(this, 20,3), new CenterCrop(this))
                            .into(imageViewbackground);
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



        }else{
            imgAvatar.setImageURI(Uri.fromFile(new File(DUTApplication.getUserInfo().getUSER_HEAD_BG())));
            Glide.with(this).load(Uri.fromFile(new File(DUTApplication.getUserInfo().getUSER_HEAD_BG())))

                    .bitmapTransform(new BlurTransformation(this, 20,3), new CenterCrop(this))
                    .into(imageViewbackground);

        }
        tvName.setText(DUTApplication.getUserInfo().getUserName());
        tvGrade.setText(DUTApplication.getUserInfo().getUserAccount());

    }

    @Override
    protected void onResume() {
        super.onResume();
        showUserInfo();
    }


    @OnClick({R.id.btn_change_info, R.id.tv_my_word, R.id.tv_setting,R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_change_info:
                startActivity(new Intent(this, MeInfoActivity.class));
                break;
            case R.id.tv_my_word:
                startActivity(new Intent(this, MeWordsActivity.class));
                break;

            case R.id.tv_setting:
                startActivity(new Intent(this, MeSettingActivity.class));
                break;
            case R.id.btn_back:
                     finish();
                break;
        }
    }
}

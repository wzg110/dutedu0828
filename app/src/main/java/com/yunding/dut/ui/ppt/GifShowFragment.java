package com.yunding.dut.ui.ppt;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.third.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 类 名 称：GifShowFragment
 * <P/>描    述：GIF显示页面
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 16:52
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 16:52
 * <P/>修改备注：
 * <P/>版    本：
 */
public class GifShowFragment extends BaseFragmentInReading {
    @BindView(R.id.frsco_img2)
    SimpleDraweeView frscoImg2;
    Unbinder unbinder;
    private String gifUrl;
    private Dialog dialog;
    private View view;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gif;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        getHoldingActivity().getmToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment();
            }
        });

        if (NetworkUtils.isWifiConnected()) {
            gifUrl = getArguments().getString("gifUrl");
            Uri uri = Uri.parse(gifUrl);
            DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                    //加载drawable里的一张gif图
                    .setUri(uri)//设置uri
                    .setAutoPlayAnimations(true)
                    .build();
                    //设置Controller
            frscoImg2.setController(mDraweeController);

        } else {
            showDIalog();

        }


    }

    private void showDIalog() {
        dialog = new Dialog(getHoldingActivity(), R.style.ActionSheetDialogStyle);
        view = LayoutInflater.from(getHoldingActivity()).inflate(R.layout.login_exit_dialog, null);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        TextView tv_aa = (TextView) view.findViewById(R.id.tv_message_content);
        tv_aa.setText("确认使用非WIFI网络播放GIF图片？");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                removeFragment();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gifUrl = getArguments().getString("gifUrl");
                Uri uri = Uri.parse(gifUrl);
                DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                        .setAutoPlayAnimations(true)
//加载drawable里的一张gif图
                        .setUri(uri)//设置uri
                        .build();
//设置Controller
                frscoImg2.setController(mDraweeController);
                dialog.dismiss();

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

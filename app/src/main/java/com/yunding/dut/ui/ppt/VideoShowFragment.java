package com.yunding.dut.ui.ppt;


import android.app.Dialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.yunding.dut.R;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.third.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 类 名 称：VideoShowFragment
 * <P/>描    述：
 * <P/>创 建 人：Administrator
 * <P/>创建时间：2017/7/12 13:30
 * <P/>修 改 人：Administrator
 * <P/>修改时间：2017/7/12 13:30
 * <P/>修改备注：
 * <P/>版    本：
 */

public class VideoShowFragment extends BaseFragmentInReading {
    @BindView(R.id.video_view)
    VideoView videoView;
    Unbinder unbinder;
    private  String videoUrl;
    private Dialog dialog;
    private View view;
//    private MediaController mMediaController;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video_show;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        getHoldingActivity().getmToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment();
            }
        });
        if (NetworkUtils.isWifiConnected()){
            try{

                videoUrl=getArguments().getString("videoUrl");
//                videoView.setVideoPath("http://www.androidbook.com/akc/filestorage/android/documentfiles/3389/movie.mp4");//设置播放地址
//                videoView.setVideoPath(videoUrl);
//                mMediaController = new MediaController(getHoldingActivity());//实例化控制器
//                mMediaController.show(5000);//控制器显示5s后自动隐藏
//                videoView.setMediaController(mMediaController);//绑定控制器
//                videoView.setVideoQuality(io.vov.vitamio.MediaPlayer.VIDEOQUALITY_HIGH);//设置播放画质 高画质
//                videoView.requestFocus();//取得焦点

//            showProgressDialog();
//            videoUrl=getArguments().getString("videoUrl");
//                Log.e("asdasdasd",videoUrl);
//
//
//            //设置视频控制器
            videoView.setMediaController(new MediaController(getHoldingActivity()));
                Uri uri = Uri.parse(videoUrl);
//                Uri uri = Uri.parse("http://172.16.0.222:9080/edu/pptteaching/getAttachFile?fileId=101414515037142593260115257&fileName=20170824161253396.mp4");


                //设置视频路径
//                videoView.setVideoURI(uri);
                videoView.setVideoPath(videoUrl);
                //开始播放视频

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        hideProgressDialog();
                        videoView.start();
                    }
                });

            //播放完成回调
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    hideProgressDialog();
                    showToast("已播放结束");
                }
            });
                videoView.start();
                videoView.requestFocus();

            }catch(Exception e){
                hideProgressDialog();
                removeFragment();

            }

        }else{
            showDialog();
        }




    }

    private void showDialog() {

        dialog=new Dialog(getHoldingActivity(),R.style.ActionSheetDialogStyle);
        view= LayoutInflater.from(getHoldingActivity()).inflate(R.layout.login_exit_dialog,null);
        Button btn_cancel= (Button) view.findViewById(R.id.btn_cancel);
        Button btn_ok= (Button) view.findViewById(R.id.btn_ok);
        TextView tv_aa= (TextView) view.findViewById(R.id.tv_message_content);
        tv_aa.setText("确认使用非WIFI网络播放视频？");
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
//                showProgressDialog();
                videoUrl=getArguments().getString("videoUrl");

                Uri uri = Uri.parse( videoUrl );


                //设置视频控制器
                videoView.setMediaController(new MediaController(getHoldingActivity()));
//                videoView.setVideoURI(uri);
                videoView.setVideoPath(videoUrl);

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        hideProgressDialog();
                        videoView.start();
                    }
                });

                //播放完成回调
                videoView.setOnCompletionListener( new MyPlayerOnCompletionListener());

                //设置视频路径
                videoView.start();
                videoView.requestFocus();

            }
        });
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.show();//显示对话框
    }

    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
//            hideProgressDialog();
            showToast("已播放结束");
        }
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
//        if (videoView.isPlaying()){
//            videoView.pause();
//        }
    }
}

package com.yunding.dut.ui.me;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.translate.JSTranslateBean;
import com.yunding.dut.model.resp.translate.YDTranslateBean;
import com.yunding.dut.presenter.me.MeWordTranslatePresenter;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.util.FontsUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/17.
 * 暂时废用
 */

public class MeWordTranslateActivity extends ToolBarActivity implements IMeWordTranslateView {

    @BindView(R.id.tv_voice_translate_me)
    ImageView mTvVoiceTranslateMe;
    @BindView(R.id.tv_content_translate)
    TextView mTvContentTranslate;
    @BindView(R.id.tv_soundmark_translate_me)
    TextView mTvSoundmarkTranslateMe;
    @BindView(R.id.tv_translate_translate_me)
    TextView mTvTranslateTranslateMe;
    private String meWord;
    private MediaPlayer mMediaPlayer;
    //读单词的url
    private String readUrl ;
    private MeWordTranslatePresenter mMeWordTranslatePresenter;
    private static final String TAG = "MeWordTranslateActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_word_translate);
        ButterKnife.bind(this);
        mMeWordTranslatePresenter = new MeWordTranslatePresenter(this);
        meWord = getIntent().getStringExtra(MeWordsActivity.MEWORD);
        mMeWordTranslatePresenter.getTranslation(meWord);
        mMediaPlayer = new MediaPlayer();
        if (FontsUtils.isContainChinese(meWord)
                || TextUtils.isEmpty(meWord)){

        }else{
            mTvContentTranslate.setTypeface(DUTApplication.getHsbwTypeFace());
        }
        mTvContentTranslate.setText(meWord);
    }

    @OnClick(R.id.tv_voice_translate_me)
    public void onViewClicked() {
        showProgressDialog();
        //读单词
        if (!readUrl.isEmpty()) {

            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(readUrl);
                //准备
                mMediaPlayer.prepare();
                //播放
                mMediaPlayer.start();
                hideProgressDialog();

            } catch (IOException e) {
                e.printStackTrace();
                hideProgressDialog();
            }
        }else {
            hideProgressDialog();
            showToast("该单词无发音");

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
    public void JSTranslate(JSTranslateBean jsTranslateBean) {
        if (jsTranslateBean != null) {
            if (jsTranslateBean.getWord_name()!=null) {
                mTvVoiceTranslateMe.setVisibility(View.VISIBLE);
                mTvSoundmarkTranslateMe.setVisibility(View.VISIBLE);
                JSTranslateBean.SymbolsBean partsBean = jsTranslateBean.getSymbols().get(0);
                readUrl = partsBean.getPh_tts_mp3();
                mTvSoundmarkTranslateMe.setText("英式发音:   " + partsBean.getPh_en() + "\n" + "美式发音:   " + partsBean.getPh_am());
                StringBuffer strTranslate = new StringBuffer("");
                for (int i = 0; i < partsBean.getParts().size(); i++) {
                    strTranslate.append(partsBean.getParts().get(i).getPart() + "   " + partsBean.getParts().get(i).getMeans() + "\n");
                }
                String str = strTranslate.toString();
                if (!str.isEmpty()) {
                    if (FontsUtils.isContainChinese(str)
                            || TextUtils.isEmpty(str)){

                    }else{
                        mTvTranslateTranslateMe.setTypeface(DUTApplication.getHsbwTypeFace());
                    }
                    mTvTranslateTranslateMe.setText(strTranslate);
                }
            } else showMsg("该单词无翻译");


        }
    }

    @Override
    public void YDTranslate(YDTranslateBean ydTranslateBean) {
        if (ydTranslateBean != null) {
            mTvVoiceTranslateMe.setVisibility(View.GONE);
            mTvSoundmarkTranslateMe.setVisibility(View.GONE);
            if (ydTranslateBean != null) {
                if (FontsUtils.isContainChinese(ydTranslateBean.getTranslation().get(0))
                        || TextUtils.isEmpty(ydTranslateBean.getTranslation().get(0))){

                }else{
                    mTvTranslateTranslateMe.setTypeface(DUTApplication.getHsbwTypeFace());
                }
                mTvTranslateTranslateMe.setText(ydTranslateBean.getTranslation().get(0));
            }
        } else showMsg("该句子无翻译");
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

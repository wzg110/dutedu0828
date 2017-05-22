package com.yunding.dut.ui.me;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.yunding.dut.R;
import com.yunding.dut.ui.base.ToolBarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/5/17.
 */

public class MeWordTranslateActivity extends ToolBarActivity {

    @BindView(R.id.tv_voice_translate_me)
    TextView mTvVoiceTranslateMe;
    @BindView(R.id.tv_content_translate)
    TextView mTvContentTranslate;
    @BindView(R.id.tv_soundmark_translate_me)
    TextView mTvSoundmarkTranslateMe;
    @BindView(R.id.tv_translate_translate_me)
    TextView mTvTranslateTranslateMe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_me_word_translate);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_voice_translate_me)
    public void onViewClicked() {
    }
}

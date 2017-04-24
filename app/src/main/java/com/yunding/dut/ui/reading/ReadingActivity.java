package com.yunding.dut.ui.reading;

import android.os.Bundle;

import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.ui.base.BaseFragment;
import com.yunding.dut.ui.base.BaseFragmentActivity;

public class ReadingActivity extends BaseFragmentActivity {

    public static final String READING_INFO = "reading_info";
    private ReadingListResp.DataBean mReadingInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReadingInfo = (ReadingListResp.DataBean) getIntent().getSerializableExtra(READING_INFO);
    }

    @Override
    protected BaseFragment getFirstFragment() {
        return new ReadingInputQuestionFragment();
    }
}

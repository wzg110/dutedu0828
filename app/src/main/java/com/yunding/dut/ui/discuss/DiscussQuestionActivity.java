package com.yunding.dut.ui.discuss;

import android.os.Bundle;

import com.yunding.dut.R;
import com.yunding.dut.ui.base.ToolBarActivity;
import com.yunding.dut.view.DUTHorizontalRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscussQuestionActivity extends ToolBarActivity {

    @BindView(R.id.rv_question_list)
    DUTHorizontalRecyclerView rvQuestionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss_question);
        ButterKnife.bind(this);
    }
}

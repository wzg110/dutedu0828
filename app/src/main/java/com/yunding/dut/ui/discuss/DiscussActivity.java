package com.yunding.dut.ui.discuss;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yunding.dut.R;
import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.ui.base.ToolBarActivity;

public class DiscussActivity extends ToolBarActivity {

    public static String DISCUSS_SUBJECT_INFO = "discuss_subject_info";

    private DiscussListResp.DataBean mDiscussInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discuss);

        mDiscussInfo = (DiscussListResp.DataBean) getIntent().getSerializableExtra(DISCUSS_SUBJECT_INFO);
        if (mDiscussInfo != null) {
            setTitleInCenter(mDiscussInfo.getGroupName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_discuss, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_group_info:
                if (mDiscussInfo != null) {
                    Intent intent = new Intent(this, DiscussGroupActivity.class);
                    intent.putExtra(DiscussGroupActivity.GROUP_INFO, mDiscussInfo);
                    startActivity(intent);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

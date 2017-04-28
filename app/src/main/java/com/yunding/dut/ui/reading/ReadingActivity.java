package com.yunding.dut.ui.reading;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.yunding.dut.R;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.ui.base.BaseFragment;
import com.yunding.dut.ui.base.BaseFragmentActivity;

/**
 * 类 名 称：ReadingActivity
 * <P/>描    述：控制阅读流程的Activity
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/25 9:38
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/25 9:38
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public class ReadingActivity extends BaseFragmentActivity {

    public static final String READING_INFO = "reading_info";
    public static final String READING_QUESTION = "reading_question";
    private ReadingListResp.DataBean mReadingInfo;

    public static final int TYPE_CHOICE = 2;
    public static final int TYPE_INPUT = 1;

    public static final int STATE_FINISHED = 1;
    public static final int STATE_UNDER_WAY = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected BaseFragment getFirstFragment() {
        getmToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFragment();
            }
        });

        mReadingInfo = (ReadingListResp.DataBean) getIntent().getSerializableExtra(READING_INFO);
        if (mReadingInfo != null) {
            if (mReadingInfo.getPreClassExercises() != null && mReadingInfo.getPreClassExercises().size() > 0) {
                if (mReadingInfo.getPreClassExercises().get(0).getQuestionType() == 1) {
                    //填空题
                    ReadingPreClassInputQuestionFragment readingInputFragment = new ReadingPreClassInputQuestionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(READING_INFO, mReadingInfo);
                    bundle.putSerializable(READING_QUESTION, mReadingInfo.getPreClassExercises().get(0));
                    readingInputFragment.setArguments(bundle);
                    return readingInputFragment;
                } else {
                    //选择题
                    ReadingPreClassChoiceQuestionFragment readingChoiceFragment = new ReadingPreClassChoiceQuestionFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(READING_INFO, mReadingInfo);
                    bundle.putSerializable(READING_QUESTION, mReadingInfo.getPreClassExercises().get(0));
                    readingChoiceFragment.setArguments(bundle);
                    return readingChoiceFragment;
                }
            } else {
                ReadingArticleFragment readingOriginalFragment = new ReadingArticleFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(READING_INFO, mReadingInfo);
                readingOriginalFragment.setArguments(bundle);
                return readingOriginalFragment;
            }
        } else {
            showToast("没有阅读信息");
            finish();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reading, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_close_reading:
                new MaterialDialog.Builder(this)
                        .title("提示")
                        .content("确认退出此阅读课？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                finish();
                            }
                        })
                        .show();
                break;
        }
        return true;
    }
}

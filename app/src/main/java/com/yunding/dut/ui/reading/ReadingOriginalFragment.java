package com.yunding.dut.ui.reading;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yunding.dut.R;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.ui.base.BaseFragmentInReading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 类 名 称：ReadingOriginalFragment
 * <P/>描    述：阅读原文页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/25 9:37
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/25 9:37
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public class ReadingOriginalFragment extends BaseFragmentInReading {


    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.btn_last)
    Button btnLast;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    @BindView(R.id.btn_next)
    Button btnNext;

    private ReadingListResp.DataBean mReadingInfo;

    public ReadingOriginalFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reading_original;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        mReadingInfo = (ReadingListResp.DataBean) getArguments().getSerializable(ReadingActivity.READING_INFO);

        if (mReadingInfo != null) {
            tvTitle.setText(mReadingInfo.getCourseTitle());
            tvContent.setText(mReadingInfo.getCourseContent());
        }

    }

    @OnClick({R.id.btn_last, R.id.btn_finish, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_last:
                break;
            case R.id.btn_finish:
                goNext();
                break;
            case R.id.btn_next:
                break;
        }
    }

    /**
     * 功能简述:下一步
     */
    private void goNext() {
        //还有课后小题
        if (mReadingInfo.getExercises().size() == 0) {
            showToast("没有课后小题");
            getHoldingActivity().finish();
            return;
        }
        ReadingListResp.DataBean.ExercisesBean bean = mReadingInfo.getExercises().get(0);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ReadingActivity.READING_INFO, mReadingInfo);
        bundle.putSerializable(ReadingActivity.READING_QUESTION, bean);

        switch (bean.getQuestionType()) {
            case ReadingActivity.TYPE_CHOICE:
                //选择题
                ReadingChoiceQuestionFragment choiceQuestionFragment = new ReadingChoiceQuestionFragment();
                choiceQuestionFragment.setArguments(bundle);
                addFragment(choiceQuestionFragment);
                break;
            case ReadingActivity.TYPE_INPUT:
                //填空题
                ReadingInputQuestionFragment inputQuestionFragment = new ReadingInputQuestionFragment();
                inputQuestionFragment.setArguments(bundle);
                addFragment(inputQuestionFragment);
                break;
            default:
                showSnackBar("没有该题型，请反馈客服");
                break;
        }
    }
}

package com.yunding.dut.ui.reading;


import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yunding.dut.R;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.presenter.reading.ReadingPresenter;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.SizeUtils;
import com.yunding.dut.util.third.TimeUtils;
import com.yunding.dut.view.DUTHorizontalProgressBarWithNumber;
import com.yunding.dut.view.selectable.OnSelectListener;
import com.yunding.dut.view.selectable.SelectableTextHelper;

import java.util.List;

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
public class ReadingArticleFragment extends BaseFragmentInReading implements IReadingArticleView {


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
    @BindView(R.id.progress)
    DUTHorizontalProgressBarWithNumber progressBar;

    private ReadingListResp.DataBean mReadingInfo;

    private ReadingPresenter mPresenter;

    //当前选中句子的索引
    private int mSentenceIndex = 0;

    //上一个句子的阅读开始时间
    private long mReadingStartTime = 0;

    //记录该篇文章是否读完
    private boolean mIsFinished = false;

    public ReadingArticleFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reading_original;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        mReadingInfo = (ReadingListResp.DataBean) getArguments().getSerializable(ReadingActivity.READING_INFO);
        mPresenter = new ReadingPresenter(this);

        if (mReadingInfo != null) {
            tvTitle.setText(mReadingInfo.getCourseTitle());
            tvContent.setText(mReadingInfo.getCourseContent());

            int isArticleFinished = mReadingInfo.getArticleFinish();
            if (isArticleFinished == 1) {
                btnLast.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);
                mIsFinished = true;
                tvContent.setTextColor(getResources().getColor(R.color.text_color_primary));
            } else {
                btnLast.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                mIsFinished = false;//初始化阅读状态
                mSentenceIndex = mReadingInfo.getReadingLineIndex();//
                mReadingStartTime = System.currentTimeMillis();//初始化时间
                moveToPosition(mSentenceIndex);
            }
        }

        SelectableTextHelper mSelectableTextHelper = new SelectableTextHelper.Builder(tvContent)
                .setSelectedColor(getResources().getColor(R.color.color_primary))
                .setCursorHandleSizeInDp(20)
                .setCursorHandleColor(getResources().getColor(R.color.translucent))
                .build();

        mSelectableTextHelper.setSelectListener(new OnSelectListener() {
            @Override
            public void onTextSelected(CharSequence content, int startIndex, int endIndex) {
                Log.e(getClass().toString(), "start=" + startIndex + "and end=" + endIndex + "and content=" + content);
                mPresenter.markerWords(mReadingInfo.getCourseId(), startIndex, content.length(), content.toString());
                changeMarkerBg(startIndex, endIndex, content.toString());
            }
        });

        initMarkerWords();

        refreshProgress();
    }

    private void refreshProgress() {
        int progress = (100 * (mSentenceIndex + 1)) / mReadingInfo.getSentenceInfo().size();
        progressBar.setProgress(progress);
    }

    @OnClick({R.id.btn_last, R.id.btn_finish, R.id.btn_next, R.id.btn_size_small, R.id.btn_size_middle, R.id.btn_size_big})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_last:
                if (mSentenceIndex > 0) {
                    commitLastSentenceTime();
                    mSentenceIndex -= 1;
                    moveToPosition(mSentenceIndex);
                } else {
                    showToast("已经到第一句了");
                }
                break;
            case R.id.btn_finish:
                if (mIsFinished) {
                    goNext();
                    if (mReadingInfo.getArticleFinish() == 0) {
                        commitNextSentenceTime();
                    }
                } else {
                    showToast("请完成阅读");
                }
                break;
            case R.id.btn_next:
                if (mSentenceIndex < (mReadingInfo.getSentenceInfo().size() - 1)) {
                    commitNextSentenceTime();
                    mSentenceIndex += 1;
                    if (mSentenceIndex == mReadingInfo.getSentenceInfo().size() - 1) {
                        mIsFinished = true;
                    }
                    moveToPosition(mSentenceIndex);
                } else {
                    showToast("已经到最后一句了");
                }
                break;
            case R.id.btn_size_small:
                tvContent.setTextSize(12);
                break;
            case R.id.btn_size_middle:
                tvContent.setTextSize(14);
                break;
            case R.id.btn_size_big:
                tvContent.setTextSize(16);
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

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            showToast(R.string.server_error);
        } else {
            showToast(msg);
        }
    }

    /**
     * 功能简述:改变文字颜色
     *
     * @param currentPosition [当前变色位置]
     */
    private void moveToPosition(int currentPosition) {
        List<ReadingListResp.DataBean.SentenceInfoBean> sentenceInfoList = mReadingInfo.getSentenceInfo();
        if (sentenceInfoList == null) return;
        if (currentPosition >= sentenceInfoList.size() || currentPosition < 0) return;

        ReadingListResp.DataBean.SentenceInfoBean sentenceInfo = sentenceInfoList.get(mSentenceIndex);
        int startIndex = sentenceInfo.getIndex();
        int endIndex = sentenceInfo.getIndex() + sentenceInfo.getLength();

        SpannableStringBuilder builder = new SpannableStringBuilder(tvContent.getText().toString());
        ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
        ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.rgb(230, 230, 230));
        builder.setSpan(graySpan, 0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(blackSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(graySpan, endIndex, tvContent.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvContent.setText(builder);

        refreshProgress();
    }

    /**
     * 功能简述:标记完成更改文字背景颜色
     *
     * @param startIndex [参数]
     * @param startIndex [参数]
     */
    private void changeMarkerBg(int startIndex, int endIndex, String content) {
        ReadingListResp.DataBean.NewWordsBean newWordsBean = new ReadingListResp.DataBean.NewWordsBean();
        newWordsBean.setWord(content);
        newWordsBean.setWordIndex(startIndex);
        newWordsBean.setWordLength(endIndex - startIndex);
        mReadingInfo.getNewWords().add(newWordsBean);
        initMarkerWords();
    }

    /**
     * 功能简述:初始话已标记生词的位置
     */
    private void initMarkerWords() {
        List<ReadingListResp.DataBean.NewWordsBean> newWords = mReadingInfo.getNewWords();
        SpannableStringBuilder builder = new SpannableStringBuilder(tvContent.getText().toString());
        for (ReadingListResp.DataBean.NewWordsBean newWord : newWords) {
            int startIndex = newWord.getWordIndex();
            int endIndex = startIndex + newWord.getWordLength();
            BackgroundColorSpan yellowSpan = new BackgroundColorSpan(Color.YELLOW);
            builder.setSpan(yellowSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tvContent.setText(builder);
    }

    /**
     * 功能简述:点击下一句提交
     */
    private void commitNextSentenceTime() {
        long second = TimeUtils.getTimeSpanByNow(mReadingStartTime, ConstUtils.TimeUnit.SEC);//该句子的阅读时间
        int isFinish = mSentenceIndex == (mReadingInfo.getSentenceInfo().size() - 1) ? 1 : 0;
        if (isFinish == 1) {
            mReadingInfo.setArticleFinish(1);
        }
        mPresenter.commitReadingTime(mReadingInfo.getCourseId(), mSentenceIndex, mSentenceIndex - 1, second, isFinish);
        mReadingStartTime = System.currentTimeMillis();//更新时间
    }

    /**
     * 功能简述:点击上一句提交
     */
    private void commitLastSentenceTime() {
        long second = TimeUtils.getTimeSpanByNow(mReadingStartTime, ConstUtils.TimeUnit.SEC);//该句子的阅读时间
        if (mSentenceIndex == mReadingInfo.getSentenceInfo().size() - 1) {
            mReadingInfo.setArticleFinish(1);
            mPresenter.commitReadingTime(mReadingInfo.getCourseId(), mSentenceIndex, mSentenceIndex - 1, second, 1);
        } else {
            mPresenter.commitReadingTime(mReadingInfo.getCourseId(), mSentenceIndex, mSentenceIndex + 1, second, 0);
        }
        mReadingStartTime = System.currentTimeMillis();//更新时间
    }
}

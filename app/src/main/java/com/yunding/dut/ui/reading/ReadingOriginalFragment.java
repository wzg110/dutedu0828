//package com.yunding.dut.ui.reading;
//
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.TextUtils;
//import android.text.style.ForegroundColorSpan;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.yunding.dut.R;
//import com.yunding.dut.model.resp.reading.ReadingListResp;
//import com.yunding.dut.model.resp.translate.JSTranslateBean;
//import com.yunding.dut.model.resp.translate.YDTranslateBean;
//import com.yunding.dut.presenter.reading.ReadingPresenter;
//import com.yunding.dut.ui.base.BaseFragmentInReading;
//import com.yunding.dut.util.third.ConstUtils;
//import com.yunding.dut.util.third.TimeUtils;
//import com.yunding.dut.view.DUTScrollView;
//import com.yunding.dut.view.selectable.OnSelectListener;
//import com.yunding.dut.view.selectable.SelectableTextHelper;
//
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//
///**
// * 类 名 称：ReadingOriginalFragment
// * <P/>描    述：阅读原文页面
// * <P/>创 建 人：msy
// * <P/>创建时间：2017/4/25 9:37
// * <P/>修 改 人：msy
// * <P/>修改时间：2017/4/25 9:37
// * <P/>修改备注：
// * <P/>版    本：1.0
// */
//public class ReadingOriginalFragment extends BaseFragmentInReading implements IReadingArticleView {
//
//
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.tv_content)
//    TextView tvContent;
//    @BindView(R.id.btn_last)
//    Button btnLast;
//    @BindView(R.id.btn_finish)
//    Button btnFinish;
//    @BindView(R.id.btn_next)
//    Button btnNext;
//    @BindView(R.id.lila_text)
//    LinearLayout mLilaText;
//    @BindView(R.id.lila_progress)
//    LinearLayout mLilaProgress;
//    Unbinder unbinder;
//    @BindView(R.id.sv_reading)
//    DUTScrollView mSvReading;
//
//    private ReadingListResp.DataBean mReadingInfo;
//
//    private ReadingPresenter mPresenter;
//
//    //当前选中句子的索引
//    private int mSentenceIndex = 0;
//
//    //上一个句子的阅读开始时间
//    private long mReadingStartTime = 0;
//
//    //记录该篇文章是否读完
//    private boolean mIsFinished = false;
//
//    public ReadingOriginalFragment() {
//    }
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_reading_original;
//    }
//
//    private static final String TAG = "ReadingOriginalFragment";
//    @Override
//    protected void initView(View view, Bundle saveInstanceState) {
//        //滑动监听
//        scrollListener();
//
//
//        mReadingInfo = (ReadingListResp.DataBean) getArguments().getSerializable(ReadingActivity.READING_INFO);
//
//        if (mReadingInfo != null) {
//            tvTitle.setText(mReadingInfo.getCourseTitle());
//            tvContent.setText(mReadingInfo.getCourseContent());
//
//            int isArticleFinished = mReadingInfo.getArticleFinish();
//            if (isArticleFinished == 1) {
//                btnLast.setVisibility(View.GONE);
//                btnNext.setVisibility(View.GONE);
//                mIsFinished = true;
//                tvContent.setTextColor(getResources().getColor(R.color.text_color_primary));
//            } else {
//                btnLast.setVisibility(View.VISIBLE);
//                btnNext.setVisibility(View.VISIBLE);
//                mIsFinished = false;//初始化阅读状态
//                mSentenceIndex = mReadingInfo.getReadingLineIndex();//
//                mReadingStartTime = System.currentTimeMillis();//初始化时间
//                moveToPosition();
//            }
//        }
//        mPresenter = new ReadingPresenter(this);
//
//        SelectableTextHelper mSelectableTextHelper = new SelectableTextHelper.Builder(tvContent)
//                .setSelectedColor(getResources().getColor(R.color.color_primary))
//                .setCursorHandleSizeInDp(20)
//                .setCursorHandleColor(getResources().getColor(R.color.translucent))
//                .build();
//
//        mSelectableTextHelper.setSelectListener(new OnSelectListener() {
//            @Override
//            public void onTextSelected(CharSequence content, int startIndex, int endIndex) {
//                Log.e(getClass().toString(), "start=" + startIndex + "and end=" + endIndex + "and content=" + content);
//                mPresenter.markerWords(mReadingInfo.getCourseId(), startIndex, content.length(), content.toString());
//            }
//
//            @Override
//            public void onTextTranslate(CharSequence content) {
//
//            }
//
//            @Override
//            public void onTextCollect(CharSequence content) {
//
//            }
//
//            @Override
//            public void onTextNote(CharSequence content) {
//
//            }
//
//            @Override
//            public void onTextDelete(int position) {
//
//            }
//
//            @Override
//            public void onChangeColor(int position, int color) {
//
//            }
//
//            @Override
//            public void onTextClick(int x, int y) {
//
//            }
//
//
//        });
//    }
//
//    private void scrollListener() {
//        mSvReading.setScrollViewListener(new DUTScrollView.ScrollViewListener() {
//            @Override
//            public void onScrollChanged(DUTScrollView dutScrollView, int x, int y, int oldx, int oldy) {
//                Log.e(TAG, "onScrollChanged: "+y+oldy );
//                if (oldy < y && ((y - oldy) > 15)) {// 向上
//
//                    Log.d("TAG","向上滑动");
//                    mLilaText.setVisibility(View.GONE);
//                    mLilaProgress.setVisibility(View.GONE);
//
//                }  else if (oldy > y && (oldy - y) > 15) {// 向下
//
//                    Log.d("TAG"," 向下滑动");
//                    mLilaText.setVisibility(View.VISIBLE);
//                    mLilaProgress.setVisibility(View.VISIBLE);
//                }
//
//
//            }
//        });
//    }
//
//    @OnClick({R.id.btn_last, R.id.btn_finish, R.id.btn_next})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_last:
//                if (mSentenceIndex > 0) {
//                    mSentenceIndex--;
//                    moveToPosition();
//                    commitLastSentenceTime(false);
//                } else {
//                    showToast("已经是第一句了");
//                }
//                break;
//            case R.id.btn_finish:
//                if (mIsFinished) {
//                    goNext();
//                    if (mReadingInfo.getArticleFinish() == ReadingActivity.STATE_UNDER_WAY) {
//                        commitNextSentenceTime(true);
//                    }
//                } else {
//                    showToast("请完成阅读");
//                }
//                break;
//            case R.id.btn_next:
//                if (mSentenceIndex < mReadingInfo.getSentenceInfo().size()) {
//                    mSentenceIndex++;
//                    if (mSentenceIndex < (mReadingInfo.getSentenceInfo().size() - 1)) {
//                        moveToPosition();
//                        commitNextSentenceTime(false);
//                    } else {
//                        mIsFinished = true;
//                        showToast("已经到最后一句了");
//                    }
//                } else {
//                    showToast("已经到最后一句了");
//                }
//                break;
//        }
//    }
//
//    /**
//     * 功能简述:下一步
//     */
//    private void goNext() {
//        //还有课后小题
//        if (mReadingInfo.getExercises().size() == 0) {
//            showToast("没有课后小题");
//            getHoldingActivity().finish();
//            return;
//        }
//        ReadingListResp.DataBean.ExercisesBean bean = mReadingInfo.getExercises().get(0);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(ReadingActivity.READING_INFO, mReadingInfo);
//        bundle.putSerializable(ReadingActivity.READING_QUESTION, bean);
//
//        switch (bean.getQuestionType()) {
//            case ReadingActivity.TYPE_CHOICE:
//                //选择题
//                ReadingChoiceQuestionFragment choiceQuestionFragment = new ReadingChoiceQuestionFragment();
//                choiceQuestionFragment.setArguments(bundle);
//                addFragment(choiceQuestionFragment);
//                break;
//            case ReadingActivity.TYPE_INPUT:
//                //填空题
//                ReadingInputQuestionFragment inputQuestionFragment = new ReadingInputQuestionFragment();
//                inputQuestionFragment.setArguments(bundle);
//                addFragment(inputQuestionFragment);
//                break;
//            default:
//                showSnackBar("没有该题型，请反馈客服");
//                break;
//        }
//    }
//
//    @Override
//    public void showProgress() {
//
//    }
//
//    @Override
//    public void hideProgress() {
//
//    }
//
//    @Override
//    public void showMsg(String msg) {
//        if (TextUtils.isEmpty(msg)) {
//            showToast(R.string.server_error);
//        } else {
//            showToast(msg);
//        }
//    }
//
//    @Override
//    public void showJsTranslate(JSTranslateBean jsTranslateBean) {
//
//    }
//
//    @Override
//    public void showYdTranslate(YDTranslateBean ydTranslateBean) {
//
//    }
//
//    private void moveToPosition() {
//        List<ReadingListResp.DataBean.SentenceInfoBean> sentenceInfoList = mReadingInfo.getSentenceInfo();
//        if (sentenceInfoList == null) return;
//        if (mSentenceIndex >= sentenceInfoList.size() || mSentenceIndex < 0) return;
//
//        ReadingListResp.DataBean.SentenceInfoBean sentenceInfo = sentenceInfoList.get(mSentenceIndex);
//        changeColorOfText(sentenceInfo.getIndex(), sentenceInfo.getIndex() + sentenceInfo.getLength());
//
//        if (mSentenceIndex == sentenceInfoList.size() - 1) {
//            mIsFinished = true;
//        }
//    }
//
//    private void commitNextSentenceTime(boolean isFinish) {
//        long second = TimeUtils.getTimeSpanByNow(mReadingStartTime, ConstUtils.TimeUnit.SEC);//该句子的阅读时间
//        if (!isFinish) {
//            mPresenter.commitReadingTime(mReadingInfo.getCourseId(), mSentenceIndex - 1, mSentenceIndex - 2, second, 0,mReadingInfo.getClassId());
//        } else {
//            mReadingInfo.setArticleFinish(1);
//            mPresenter.commitReadingTime(mReadingInfo.getCourseId(), mSentenceIndex - 1, mSentenceIndex - 2, second, 1,mReadingInfo.getClassId());
//        }
//        mReadingStartTime = System.currentTimeMillis();//更新时间
//    }
//
//    private void commitLastSentenceTime(boolean isFinish) {
//        long second = TimeUtils.getTimeSpanByNow(mReadingStartTime, ConstUtils.TimeUnit.SEC);//该句子的阅读时间
//        if (!isFinish) {
//            mPresenter.commitReadingTime(mReadingInfo.getCourseId(), mSentenceIndex + 1, mSentenceIndex + 2, second, 0,mReadingInfo.getClassId());
//        } else {
//            mPresenter.commitReadingTime(mReadingInfo.getCourseId(), mSentenceIndex + 1, mSentenceIndex + 2, second, 1,mReadingInfo.getClassId());
//        }
//        mReadingStartTime = System.currentTimeMillis();//更新时间
//    }
//
//    private void changeColorOfText(int startIndex, int endIndex) {
//        SpannableStringBuilder builder = new SpannableStringBuilder(tvContent.getText().toString());
//
//        ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
//        ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.rgb(230, 230, 230));
//
//        builder.setSpan(graySpan, 0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(blackSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(graySpan, endIndex, tvContent.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        tvContent.setText(builder);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // TODO: inflate a fragment view
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        unbinder = ButterKnife.bind(this, rootView);
//        return rootView;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//}

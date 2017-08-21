package com.yunding.dut.ui.reading;


import android.app.Dialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.reading.ReadingDataResp;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.model.resp.reading.markResp;
import com.yunding.dut.model.resp.translate.JSTranslateBean;
import com.yunding.dut.model.resp.translate.YDTranslateBean;
import com.yunding.dut.presenter.reading.ReadingPresenter;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.FontsUtils;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.TimeUtils;
import com.yunding.dut.view.DUTHorizontalProgressBarWithNumber;
import com.yunding.dut.view.DUTScrollView;
import com.yunding.dut.view.selectable.OnSelectListener;
import com.yunding.dut.view.selectable.SelectableTextHelper;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
    @BindView(R.id.tv_content_translate)
    TextView mTvContentTranslate;
    @BindView(R.id.tv_soundmark_translate)
    TextView mTvSoundmarkTranslate;
    @BindView(R.id.tv_translate_translate)
    TextView mTvTranslateTranslate;
    @BindView(R.id.lila_translate)
    LinearLayout mLilaTranslate;
    Unbinder unbinder;

    @BindView(R.id.lila_text)
    LinearLayout mLilaText;
    @BindView(R.id.lila_progress)
    LinearLayout mLilaProgress;
    @BindView(R.id.sv_reading)
    DUTScrollView mSvReading;
    @BindView(R.id.iv_voice_translate_en)
    ImageView mIvVoiceTranslateEn;
   private  Button btn_cancel,btn_send;
    private EditText et_notes;
    private TextView tv_note;
    private View inflate;
    private Dialog dialog;

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


    //读单词用
    private MediaPlayer mMediaPlayer;
    //读单词的url
    private String readUrl;

    private static final String TAG = "ReadingArticleFragment";
    private ReadingActivity ra;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reading_original;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {


        mSvReading.setScrollViewListener(new DUTScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(DUTScrollView dutScrollView, int x, int y, int oldx, int oldy) {

                if (y - oldy > 0) {
                    if (mLilaText.getVisibility() == View.VISIBLE) {
                        mLilaText.setVisibility(View.GONE);
                        mLilaProgress.setVisibility(View.GONE);
                    }

                } else if (y - oldy < 0) {
                    if (mLilaText.getVisibility() == View.GONE) {
                        mLilaText.setVisibility(View.VISIBLE);
                        mLilaProgress.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        //读单词用
        mMediaPlayer = new MediaPlayer();

        mReadingInfo = (ReadingListResp.DataBean) getArguments().getSerializable(ReadingActivity.READING_INFO);
        mPresenter = new ReadingPresenter(this);
        ra = (ReadingActivity) getActivity();
        ra.setToolbarTitle("阅读原文");
        if (mReadingInfo != null) {
            if (FontsUtils.isContainChinese(mReadingInfo.getCourseTitle())
                    || TextUtils.isEmpty(mReadingInfo.getCourseTitle())) {

            } else {
                tvTitle.setTypeface(DUTApplication.getHsbwTypeFace());
            }
            if (FontsUtils.isContainChinese(mReadingInfo.getCourseContent())
                    || TextUtils.isEmpty(mReadingInfo.getCourseContent())) {

            } else {
                tvContent.setTypeface(DUTApplication.getHsbwTypeFace());
            }
            tvTitle.setText(mReadingInfo.getCourseTitle());
            tvContent.setText(mReadingInfo.getCourseContent());

            int isArticleFinished = mReadingInfo.getArticleFinish();
            if (isArticleFinished == 1) {
                btnLast.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);
                mIsFinished = true;
                tvContent.setTextColor(getResources().getColor(R.color.text_color_primary));
                mSentenceIndex = mReadingInfo.getReadingLineIndex();
            } else {
                btnLast.setVisibility(View.VISIBLE);
                btnNext.setVisibility(View.VISIBLE);
                mIsFinished = false;//初始化阅读状态
                mSentenceIndex = mReadingInfo.getReadingLineIndex();//
                mReadingStartTime = System.currentTimeMillis();//初始化时间
//                moveToPosition(mSentenceIndex);
            }
        }

        SelectableTextHelper mSelectableTextHelper = new SelectableTextHelper.Builder(tvContent)
                .setSelectedColor(getResources().getColor(R.color.selectedBg))
                .setCursorHandleSizeInDp(15)
                .setSentenceInfoBean(mReadingInfo.getNewWords())
                .setCursorHandleColor(getResources().getColor(R.color.translucent))
                .build();

        mSelectableTextHelper.setSelectListener(new OnSelectListener() {


            @Override
            public void onTextSelected(CharSequence content, int startIndex, int endIndex, String color) {
//                mPresenter.markerWords(mReadingInfo.getCourseId(), startIndex, content.length(), content.toString());
//                changeMarkerBg(startIndex, endIndex, content.toString());

            }

            @Override
            public void onTextTranslate(CharSequence content, float[] position) {

            }

            //点翻译的回调
//            @Override
//            public void onTextTranslate(CharSequence content) {
//                String str = (String) content;
//                mPresenter.getTranslation(str);
//
//            }

            @Override
            public void onTextCollect(CharSequence content, String id) {

            }

            //点击收藏的回调
//            @Override
//            public void onTextCollect(CharSequence content) {
//                String str = (String) content;
////                mPresenter.collectWords(str, mReadingInfo.getCourseId(), mReadingInfo.getCourseTitle());
//            }

            @Override
            public void onTextNote(CharSequence content, int postion) {
                Toast.makeText(getHoldingActivity(), content, Toast.LENGTH_SHORT).show();
//                自定义弹窗
                showDialog(content.toString());

            }

            @Override
            public void onTextDelete(String position) {
                mPresenter.deleteNewWord(position);

            }

            @Override
            public void onChangeColor(int position, int color, String id, String colorid) {

            }


            @Override
            public void onTextClick(int x, int y) {

            }


        });

//        initMarkerWords();
        moveToPosition(mSentenceIndex);

        refreshProgress();
    }
    /**
     * EditText获取焦点并显示软键盘
     */


    private void showDialog(String content) {
        dialog = new Dialog(getHoldingActivity(), R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(getHoldingActivity()).inflate(R.layout.layout_add_notes_activity, null);
        //初始化控件
       btn_cancel= (Button) inflate.findViewById(R.id.btn_cancel);
        btn_send= (Button) inflate.findViewById(R.id.btn_send);
        et_notes= (EditText) inflate.findViewById(R.id.et_notes);
        tv_note= (TextView) inflate.findViewById(R.id.tv_notes_sentence);
        tv_note.setText(content);
        getHoldingActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_notes.getText().toString())){
                    Toast.makeText(getHoldingActivity(),"没鞋子",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(getHoldingActivity(),et_notes.getText().toString(),Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }

            }
        });
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        //获得窗体的属性
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager m = getHoldingActivity().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 1); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);

//
//        lp.y = 200;//设置Dialog距离底部的距离
//       将属性设置给窗体
//        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    private void refreshProgress() {
        int progress = (100 * (mSentenceIndex + 1)) / mReadingInfo.getSentenceInfo().size();
        progressBar.setProgress(progress);
    }

    @OnClick({R.id.btn_last, R.id.btn_finish, R.id.btn_next, R.id.btn_size_small
            , R.id.btn_size_middle, R.id.btn_size_big,
            R.id.iv_voice_translate_en, R.id.lila_translate})
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
                if (mSentenceIndex == mReadingInfo.getSentenceInfo().size() - 1) {
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
//                    if (mSentenceIndex == mReadingInfo.getSentenceInfo().size() - 1) {
//                        mIsFinished = true;
//                    }
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
            case R.id.iv_voice_translate_en:
                showProgressDialog();
                //读单词
                if (!readUrl.isEmpty()) {

                    mMediaPlayer.reset();
                    try {
                        mMediaPlayer.setDataSource(readUrl);
                        //准备
                        mMediaPlayer.prepare();
                        //播放
                        hideProgressDialog();
                        mMediaPlayer.start();

                    } catch (IOException e) {
                        hideProgressDialog();
                        e.printStackTrace();

                    }
                } else {
                    hideProgressDialog();
                    showMsg("该单词无发音");
                }
                break;
            case R.id.lila_translate:
                //翻译页面
                mLilaTranslate.setVisibility(View.GONE);
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
                ReadingNewInputFragment inputQuestionFragment = new ReadingNewInputFragment();
                inputQuestionFragment.setArguments(bundle);
                addFragment(inputQuestionFragment);
                break;
            case ReadingActivity.TYPE_MULTI_CHOICE:
//                    多选题
                ReadingMultiChoiceQuestionFragment multiChoiceFragment = new ReadingMultiChoiceQuestionFragment();
                multiChoiceFragment.setArguments(bundle);
                addFragment(multiChoiceFragment);
                break;
            case ReadingActivity.TYPE_TRANSLATE:
//                    翻译题
                ReadingTranslateQuestionFragment translateQuestionFragment = new ReadingTranslateQuestionFragment();
                translateQuestionFragment.setArguments(bundle);
                addFragment(translateQuestionFragment);
                break;
            default:
                showSnackBar("没有该题型，请反馈客服");
                break;
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
    public void showMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            showToast(R.string.server_error);
        } else {
            showToast(msg);
        }
    }

    @Override
    public void showJsTranslate(JSTranslateBean jsTranslateBean, float[] positon) {

    }

    @Override
    public void showYdTranslate(YDTranslateBean ydTranslateBean, float[] positon) {

    }

    /**
     * 使用金山的回调
     */
//    @Override
//    public void showJsTranslate(JSTranslateBean jsTranslateBean) {
//        Log.e(TAG, "showJsTranslate: " + jsTranslateBean);
//        if (jsTranslateBean != null) {
//            mLilaTranslate.setVisibility(View.VISIBLE);
//            mIvVoiceTranslateEn.setVisibility(View.VISIBLE);
//            mTvSoundmarkTranslate.setVisibility(View.VISIBLE);
//            if (jsTranslateBean.getWord_name() != null) {
//                JSTranslateBean.SymbolsBean partsBean = jsTranslateBean.getSymbols().get(0);
//                readUrl = partsBean.getPh_tts_mp3();
//                mTvContentTranslate.setText(jsTranslateBean.getWord_name());
//                mTvSoundmarkTranslate.setText("英式发音:   " + partsBean.getPh_en() + "\n" + "美式发音:   " + partsBean.getPh_am());
//                StringBuffer strTranslate = new StringBuffer("");
//                for (int i = 0; i < partsBean.getParts().size(); i++) {
//                    strTranslate.append(partsBean.getParts().get(i).getPart() + "   " + partsBean.getParts().get(i).getMeans() + "\n");
//                }
//                String str = strTranslate.toString();
//                if (!str.isEmpty()) {
//                    mTvTranslateTranslate.setText(str);
//                }
//            } else showMsg("输入单词格式有误");
//
//
//        }
//
//    }

    /**
     * 使用有道的回调
     */
//
//    @Override
//    public void showYdTranslate(YDTranslateBean ydTranslateBean) {
//        if (ydTranslateBean != null) {
//            mLilaTranslate.setVisibility(View.VISIBLE);
//            mIvVoiceTranslateEn.setVisibility(View.GONE);
//            mTvSoundmarkTranslate.setVisibility(View.GONE);
//            if (ydTranslateBean != null) {
//                mTvContentTranslate.setText(ydTranslateBean.getQuery());
//                mTvTranslateTranslate.setText(ydTranslateBean.getTranslation().get(0));
//            }
//        } else showMsg("输入格式有误");
//
//    }

    @Override
    public void deleteSuccess(String msg, String position) {
        showToast(msg);


    }

    @Override
    public void savaNoteSuccess(String context, String position, int po, String noteid) {

    }

    @Override
    public void showMarkSuccess(markResp.DataBean wordId) {

    }

    @Override
    public void showReadingDataSuccess(ReadingDataResp.DataBean resp) {

    }

    @Override
    public void commitSuccess() {

    }


    private void changeChooseBg(int position, int color) {
        List<ReadingListResp.DataBean.SentenceInfoBean> sentenceInfoList = mReadingInfo.getSentenceInfo();
        if (sentenceInfoList == null) return;
        if (mSentenceIndex >= sentenceInfoList.size() || mSentenceIndex < 0) return;
//      可以将同一段string以不同样式提现的builder
        SpannableStringBuilder builder = new SpannableStringBuilder(tvContent.getText().toString());

        if (!mIsFinished) {
            ReadingListResp.DataBean.SentenceInfoBean sentenceInfo = sentenceInfoList.get(mSentenceIndex);
            int startIndex = sentenceInfo.getIndex();
            int endIndex = sentenceInfo.getIndex() + sentenceInfo.getLength();

            ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
            ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.rgb(230, 230, 230));
            builder.setSpan(graySpan, 0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(blackSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(graySpan, endIndex, tvContent.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
            builder.setSpan(blackSpan, 0, tvContent.getText().toString().length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //改变生词底色
        List<ReadingListResp.DataBean.NewWordsBean> newWords = mReadingInfo.getNewWords();
        for (ReadingListResp.DataBean.NewWordsBean newWord : newWords) {
            int wordStartIndex = newWord.getWordIndex();
            int wordEndIndex = wordStartIndex + newWord.getWordLength();
            BackgroundColorSpan yellowSpan = new BackgroundColorSpan(color);
            builder.setSpan(yellowSpan, wordStartIndex, wordEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        for (int i = 0; i < newWords.size(); i++) {
            if (i == position) {
                int wordStartIndex = newWords.get(i).getWordIndex();
                int wordEndIndex = wordStartIndex + newWords.get(i).getWordLength();
                BackgroundColorSpan yellowSpan = new BackgroundColorSpan(color);
                builder.setSpan(yellowSpan, wordStartIndex, wordEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            } else {
                int wordStartIndex = newWords.get(i).getWordIndex();
                int wordEndIndex = wordStartIndex + newWords.get(i).getWordLength();
                BackgroundColorSpan yellowSpan = new BackgroundColorSpan(Color.YELLOW);
                builder.setSpan(yellowSpan, wordStartIndex, wordEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
//        builder.setSpan(new UnderlineSpan(), mReadingInfo.getSentenceInfo().get(position).getIndex(),
//                mReadingInfo.getSentenceInfo().get(position).getIndex()+mReadingInfo.getSentenceInfo().get(position).getLength(),
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvContent.setText(builder);
        refreshProgress();
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
//      可以将同一段string以不同样式提现的builder
        SpannableStringBuilder builder = new SpannableStringBuilder(tvContent.getText().toString());
        if (!mIsFinished) {
            ReadingListResp.DataBean.SentenceInfoBean sentenceInfo = sentenceInfoList.get(mSentenceIndex);
            int startIndex = sentenceInfo.getIndex();
            int endIndex = sentenceInfo.getIndex() + sentenceInfo.getLength();

            ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
            ForegroundColorSpan graySpan = new ForegroundColorSpan(Color.rgb(230, 230, 230));
            builder.setSpan(graySpan, 0, startIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(blackSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(graySpan, endIndex, tvContent.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ForegroundColorSpan blackSpan = new ForegroundColorSpan(Color.BLACK);
            builder.setSpan(blackSpan, 0, tvContent.getText().toString().length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //改变生词底色
        List<ReadingListResp.DataBean.NewWordsBean> newWords = mReadingInfo.getNewWords();
        for (ReadingListResp.DataBean.NewWordsBean newWord : newWords) {
            int wordStartIndex = newWord.getWordIndex();
            int wordEndIndex = wordStartIndex + newWord.getWordLength();
            BackgroundColorSpan yellowSpan = new BackgroundColorSpan(Color.YELLOW);
            builder.setSpan(yellowSpan, wordStartIndex, wordEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
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
//        initMarkerWords();
        moveToPosition(mSentenceIndex);
    }

    /**
     * 功能简述:初始话已标记生词的位置
     */
//    private void initMarkerWords() {
//        List<ReadingListResp.DataBean.NewWordsBean> newWords = mReadingInfo.getNewWords();
//        SpannableStringBuilder builder = new SpannableStringBuilder(tvContent.getText().toString());
//        for (ReadingListResp.DataBean.NewWordsBean newWord : newWords) {
//            int startIndex = newWord.getWordIndex();
//            int endIndex = startIndex + newWord.getWordLength();
//            BackgroundColorSpan yellowSpan = new BackgroundColorSpan(Color.YELLOW);
//            builder.setSpan(yellowSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        tvContent.setText(builder);
//    }

    /**
     * 功能简述:点击下一句提交
     */
    private void commitNextSentenceTime() {
        long second = TimeUtils.getTimeSpanByNow(mReadingStartTime, ConstUtils.TimeUnit.SEC);//该句子的阅读时间
        int isFinish = mSentenceIndex == (mReadingInfo.getSentenceInfo().size() - 1) ? 1 : 0;
        if (isFinish == 1) {
            mReadingInfo.setArticleFinish(1);
        }
        mPresenter.commitReadingTime(mReadingInfo.getCourseId(), mSentenceIndex, mSentenceIndex - 1, second, isFinish,mReadingInfo.getClassId());
        mReadingStartTime = System.currentTimeMillis();//更新时间
    }

    /**
     * 功能简述:点击上一句提交
     */
    private void commitLastSentenceTime() {
        long second = TimeUtils.getTimeSpanByNow(mReadingStartTime, ConstUtils.TimeUnit.SEC);//该句子的阅读时间
        if (mSentenceIndex == mReadingInfo.getSentenceInfo().size() - 1) {
            mReadingInfo.setArticleFinish(1);
            mPresenter.commitReadingTime(mReadingInfo.getCourseId(), mSentenceIndex, mSentenceIndex - 1, second, 1,mReadingInfo.getClassId());
        } else {
            mPresenter.commitReadingTime(mReadingInfo.getCourseId(), mSentenceIndex, mSentenceIndex + 1, second, 0,mReadingInfo.getClassId());
        }
        mReadingStartTime = System.currentTimeMillis();//更新时间
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        Log.e("退出", "退出");
        if (mReadingInfo.getArticleFinish() == 1) {
//            阅读完成了就不去计算了
        } else {
//            请求借口计算跳出次数
//            mPresenter.dapFromArticle();
        }
        mMediaPlayer.reset();
        mMediaPlayer.release();
        unbinder.unbind();
    }



}

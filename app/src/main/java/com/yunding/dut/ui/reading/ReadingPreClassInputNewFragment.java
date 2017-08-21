package com.yunding.dut.ui.reading;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yunding.dut.R;
import com.yunding.dut.adapter.DiscussQuestionInputAdapter;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.reading.ReadingDataResp;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.presenter.reading.ReadingPresenter;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.FontsUtils;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.SizeUtils;
import com.yunding.dut.util.third.TimeUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReadingPreClassInputNewFragment extends BaseFragmentInReading implements IReadingQuestionView {
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.content)
    AbsoluteLayout content;
    @BindView(R.id.tv_answer_tips)
    TextView tvAnswerTips;
    @BindView(R.id.tv_right_answer)
    TextView tvRightAnswer;
    @BindView(R.id.tv_toast)
    TextView tvToast;
    @BindView(R.id.layout_toast)
    ScrollView layoutToast;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    Unbinder unbinder;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    private ReadingListResp.DataBean mReadingInfo;
    private ReadingListResp.DataBean.PreClassExercisesBean mPreExerciseBean;
    private String answerShowTime; //0答完即显示  1 阅读课完事显示
    private int mQuestionIndex;
    private DiscussQuestionInputAdapter mInputAdapter;
    private ReadingPresenter mPresenter;
    private long mStartTime;
    private ReadingActivity ra;
    private int quesionQuantity;
    private Dialog dialog;
    private View view;

    private Map<Integer, String> answerMap = new HashMap<>();//存放用户输入答案
    private Map<Integer, EditText> inputEdit = new HashMap<>();//存放edittext对象
    private Layout layout;
    private ViewTreeObserver vto;
    private ViewTreeObserver.OnGlobalLayoutListener listener;

    @Override
    public void commitSuccess() {
        mPreExerciseBean.setQuestionCompleted(ReadingActivity.STATE_FINISHED);
        btnNext.setVisibility(View.VISIBLE);
        btnCommit.setVisibility(View.GONE);
        if (answerShowTime.equals("0")) {
            layoutToast.setVisibility(View.VISIBLE);
        }
        EventBus.getDefault().post(mReadingInfo);
        for (int i = 0; i < inputEdit.size(); i++) {
            inputEdit.get(i).setFocusable(false);
        }


    }

    @Override
    public void showRightAnswer() {

    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_preclass_input_new_twice;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        ra = (ReadingActivity) getActivity();
        ra.setToolbarTitle("课前小题");

        ra.setToolbarBGC(getResources().getColor(R.color.bg_white));
        ra.setToolbarTitleColor(getResources().getColor(R.color.title));

        mReadingInfo = (ReadingListResp.DataBean) getArguments().getSerializable(ReadingActivity.READING_INFO);
        if (getArguments().getSerializable(ReadingActivity.READING_QUESTION) instanceof ReadingListResp.DataBean.PreClassExercisesBean) {
            mPreExerciseBean = (ReadingListResp.DataBean.PreClassExercisesBean) getArguments().getSerializable(ReadingActivity.READING_QUESTION);
            mQuestionIndex = mReadingInfo.getPreClassExercises().indexOf(mPreExerciseBean);//题目编号
            quesionQuantity = mReadingInfo.getPreClassExercises().size();
        }

        mPresenter = new ReadingPresenter(this);
        mStartTime = System.currentTimeMillis();
        answerShowTime = mReadingInfo.getAnswerShowTime();

        //初始化UI
        if (mPreExerciseBean == null) return;
        // TODO: 2017/7/28 三期
        if (mReadingInfo.getCompleted() == 1 || mReadingInfo.getCompleted() == 2) {
            ra.getToolbarExit().setVisibility(View.VISIBLE);
            ra.getToolbarExit().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.showReadingData(mReadingInfo.getClassId(), mReadingInfo.getCourseId());
                }
            });
        } else {
            ra.getToolbarExit().setVisibility(View.GONE);
        }
        // TODO: 2017/7/28 三期
        if (mPreExerciseBean.getQuestionType() == ReadingActivity.TYPE_INPUT) {

            tvTitle.setText("填空题" + (mQuestionIndex + 1) + "/" + quesionQuantity);
        } else {
            tvTitle.setText("填空题" + (mQuestionIndex + 1) + "/" + quesionQuantity);
        }
        tvQuestion.setTypeface(DUTApplication.getHsbwTypeFace());
        tvQuestion.setText(mPreExerciseBean.getQuestionContent());

        if (mReadingInfo.getCompleted() == 0) {
            if (mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED) {
                if (mReadingInfo.getAnswerShowTime().equals("0")) {
                    layoutToast.setVisibility(View.VISIBLE);
                } else {
                    layoutToast.setVisibility(View.GONE);
                }

            } else {
                layoutToast.setVisibility(View.GONE);
            }
            btnNext.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
            btnCommit.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);

        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnCommit.setVisibility(View.GONE);
            layoutToast.setVisibility(View.VISIBLE);
        }
        String answer = mPreExerciseBean.getRightAnswer().substring(mPreExerciseBean.getRightAnswer().indexOf("[") + 1, mPreExerciseBean.getRightAnswer().indexOf("]"));
        String[] sourceStrArray = answer.split(",");
        StringBuffer stringBuffer = new StringBuffer("");
        for (int i = 0; i < sourceStrArray.length; i++) {
            int a = i + 1;
            stringBuffer.append(a + ": " + sourceStrArray[i] + "\n");
        }
        if (FontsUtils.isContainChinese(mPreExerciseBean.getAnalysis()) || TextUtils.isEmpty(mPreExerciseBean.getAnalysis())) {

        } else {
            tvToast.setTypeface(DUTApplication.getHsbwTypeFace());
        }
        if (FontsUtils.isContainChinese(stringBuffer.toString()) || TextUtils.isEmpty(stringBuffer.toString())) {

        } else {
            tvRightAnswer.setTypeface(DUTApplication.getHsbwTypeFace());

        }

        tvRightAnswer.setText(stringBuffer.toString());
        tvToast.setText(mPreExerciseBean.getAnalysis());
        if (layoutToast != null) {
            layoutToast.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {

                    if (scrollView != null) {
                        scrollView.setEnabled(layoutToast.getScrollY() == 0);
                    }
                }
            });
        }
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (layoutToast!=null){
                    layoutToast.getParent().requestDisallowInterceptTouchEvent(false);
                }

                return false;
            }
        });
        layoutToast.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        drawTextview();
    }

    private void drawTextview() {
        final SpannableStringBuilder builder = new SpannableStringBuilder(tvQuestion.getText().toString());
        for (int i = 0; i < mPreExerciseBean.getBlanksInfo().size(); i++) {
            //遍历空格
            ImageSpan ss = new ImageSpan(getHoldingActivity(), R.drawable.bgzha);
            builder.setSpan(ss, mPreExerciseBean.getBlanksInfo().get(i).getIndex()
                    , mPreExerciseBean.getBlanksInfo().get(i).getIndex() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tvQuestion.setText(builder);
        vto = tvQuestion.getViewTreeObserver();

        listener=new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                layout = tvQuestion.getLayout();
                if (layout != null) {
                    Message msg = new Message();
                    msg.what = 123;
                    handler.sendMessage(msg);
                    tvQuestion.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        };
        vto.addOnGlobalLayoutListener(listener);

    }
    // TODO: 2017/8/16
    public float getCharacterWidth(String text, float size){
        if(null == text || "".equals(text))
            return 0;
        float width = 0;
        Paint paint = new Paint();
        paint.setTextSize(size);
        float text_width = paint.measureText(text);//得到总体长度
        width = text_width/text.length();//每一个字符的长度
        return width;
    }
    public int getFontHeight(float fontSize)
    {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent);
    }

    // TODO: 2017/8/16

    Handler handler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 123) {
                String[] answerContent = new Gson().fromJson

                        (mPreExerciseBean.getAnswerContent(), String[].class);
                float letterWidth=getCharacterWidth(tvQuestion.getText().toString(),14);
                for (int i = 0; i < mPreExerciseBean.getBlanksInfo().size(); i++) {
                    answerMap.put(i, "");

                    LinearLayout ll = new LinearLayout(getHoldingActivity());
                    ll.setOrientation(LinearLayout.VERTICAL);
                    final EditText et = new EditText(ll.getContext());
                    et.setTextSize(14);
                    et.setSingleLine(true);
                    et.setBackground(null);
                    et.setHint(i + 1 + "");
                    et.setId(i);
//                    et.setTextAppearance(getHoldingActivity(),R.style.MyEditText);
                    et.setGravity(Gravity.CENTER);
                    et.setWidth(SizeUtils.dp2px(100));
                    et.setHeight(SizeUtils.dp2px(29));
                    et.setPadding(10, 0, 10, 0);
                    if (mReadingInfo.getCompleted() == 1) {
                        et.setText(answerContent[i]);
                        et.setFocusable(false);
                    } else {
                        if (mPreExerciseBean.getQuestionCompleted() == 1) {
                            et.setText(answerContent[i]);
                            et.setFocusable(false);
                        }
                    }
                    et.setTextColor(Color.BLACK);

                    ll.addView(et);
                    TextView asda = new TextView(ll.getContext());
                    asda.setWidth(SizeUtils.dp2px(100));
                    asda.setHeight(SizeUtils.dp2px(1));
                    asda.setBackgroundResource(R.color.orange);
                    ll.addView(asda);

                    final int finalI = i;
                    et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            answerMap.put(v.getId(), et.getText().toString());

                        }
                    });
                    float[] aa = getPosition(mPreExerciseBean.getBlanksInfo().get(i).getIndex());

                    AbsoluteLayout.LayoutParams lp = null;
                    lp = new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(100),
                            SizeUtils.dp2px(30), (int) (aa[0] + SizeUtils.sp2px(14)+10),
                            (int) (aa[1]-SizeUtils.dp2px(10)-15));

                    inputEdit.put(i, et);
                    content.addView(ll, lp);
                }

            }
        }
    };

    private float[] getPosition(int tv) {

        float[] aa = new float[3];
//        获取位置
        Rect bound = new Rect();
        int line = layout.getLineForOffset(tv);
        layout.getLineBounds(line, bound);

        float yAxisTop = bound.bottom;//字符顶部y坐标
        float xAxisRight = layout.getPrimaryHorizontal(tv);//字符右边x坐标
        aa[0] = xAxisRight;
        aa[1] = yAxisTop;
        aa[2] = bound.height();

        return aa;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btn_commit, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                content.clearFocus();
                commitAnswer();
                break;
            case R.id.btn_next:
                goNext();
                break;
        }
    }

    private void commitAnswer() {
        List<String> answerList = new ArrayList<>();
        for (int i = 0; i < mPreExerciseBean.getBlanksInfo().size(); i++) {
            answerList.add(answerMap.get(i).replaceAll("#", ""));
        }

        String answerTemp = new Gson().toJson(answerList);
        long commitTime = System.currentTimeMillis();
        long timeSpan = TimeUtils.getTimeSpan(commitTime, mStartTime, ConstUtils.TimeUnit.MSEC);

        if (timeSpan == 0) {
            timeSpan = 1;
        }

        if (mPreExerciseBean != null) {
            mReadingInfo.getPreClassExercises()
                    .get(mReadingInfo.getPreClassExercises().indexOf(mPreExerciseBean))
                    .setAnswerContent(answerTemp);
            mPresenter.commitAnswer(mPreExerciseBean.getQuestionId(), answerTemp, timeSpan, 0, mReadingInfo.getClassId());
        }
    }

    private void goNext() {
        if (mReadingInfo.getPreClassExercises().size() > (mQuestionIndex + 1)) {
            //还有课前小题
            ReadingListResp.DataBean.PreClassExercisesBean bean = mReadingInfo.getPreClassExercises().get(mQuestionIndex + 1);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ReadingActivity.READING_INFO, mReadingInfo);
            bundle.putSerializable(ReadingActivity.READING_QUESTION, bean);

            switch (bean.getQuestionType()) {
                case ReadingActivity.TYPE_CHOICE:
                    //选择题
                    ReadingPreClassChoiceQuestionFragment choiceQuestionFragment = new ReadingPreClassChoiceQuestionFragment();
                    choiceQuestionFragment.setArguments(bundle);
                    addFragment(choiceQuestionFragment);
                    break;
                case ReadingActivity.TYPE_INPUT:
                    //填空题
                    ReadingPreClassInputNewFragment inputQuestionFragment = new ReadingPreClassInputNewFragment();
                    inputQuestionFragment.setArguments(bundle);
                    addFragment(inputQuestionFragment);
                    break;
                case ReadingActivity.TYPE_MULTI_CHOICE:
                    ReadingPreClassMultiChoiceQuestionFragment multiChoiceFragment = new ReadingPreClassMultiChoiceQuestionFragment();
                    multiChoiceFragment.setArguments(bundle);
                    addFragment(multiChoiceFragment);
                    break;
                case ReadingActivity.TYPE_TRANSLATE:
                    ReadingPreClassTranslateQuestionFragment translateQuestionFragment = new ReadingPreClassTranslateQuestionFragment();
                    translateQuestionFragment.setArguments(bundle);
                    addFragment(translateQuestionFragment);
                    break;
                default:
                    showSnackBar("没有该题型，请反馈客服");
                    break;
            }
        } else {
            //课前小题已经答完，进入阅读页面
//            ra.setToolbarTitle("阅读原文");
            Bundle bundle = new Bundle();
            bundle.putSerializable(ReadingActivity.READING_INFO, mReadingInfo);
            ReadingNewArticleFragment originalFragment = new ReadingNewArticleFragment();
            originalFragment.setArguments(bundle);
            addFragment(originalFragment);
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
    public void showReadingDataSuccess(ReadingDataResp.DataBean resp) {
        showReadingDataDialog(resp);
    }

    public void showReadingDataDialog(ReadingDataResp.DataBean resp) {
        dialog = new Dialog(getHoldingActivity(), R.style.ActionSheetDialogStyle);
        view = LayoutInflater.from(getHoldingActivity()).inflate(R.layout.rlayout_reading_data_show, null);
        TextView tv_words_number = (TextView) view.findViewById(R.id.tv_words_number);
        TextView tv_reading_time = (TextView) view.findViewById(R.id.tv_reading_time);
        TextView tv_reading_speed = (TextView) view.findViewById(R.id.tv_reading_speed);
        TextView tv_collection_number = (TextView) view.findViewById(R.id.tv_collection_number);
        TextView tv_notes_number11 = (TextView) view.findViewById(R.id.tv_notes_number111);
        ImageView rl_exit = (ImageView) view.findViewById(R.id.rl_exit);
        tv_words_number.setText(resp.getWordsQuantity() + "个");
        tv_reading_time.setText(resp.getTimeConsumedDesc());
        tv_reading_speed.setText(resp.getReadingSpeed());
        tv_collection_number.setText(resp.getWordsCollected() + "");
        tv_notes_number11.setText(resp.getNotesQuantity() + "");
        rl_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.show();//显示对话框

    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onPause() {
        super.onPause();
        if(listener!=null){
            tvQuestion.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }
}

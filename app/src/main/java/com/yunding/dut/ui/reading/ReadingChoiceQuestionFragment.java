package com.yunding.dut.ui.reading;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.reading.ReadingDataResp;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.presenter.reading.ReadingPresenter;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.FontsUtils;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.TimeUtils;
import com.yunding.dut.util.third.Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 类 名 称：ReadingChoiceQuestionFragment
 * <P/>描    述：阅读选择题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/24 18:50
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/24 18:50
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public class ReadingChoiceQuestionFragment extends BaseFragmentInReading implements IReadingQuestionView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_right_answer)
    TextView tvRightAnswer;
    @BindView(R.id.tv_toast)
    TextView tvToast;
    @BindView(R.id.layout_toast)
    ScrollView layoutToast;
    Unbinder unbinder;
    @BindView(R.id.iv_go_back_reading)
    ImageView ivGoBackReading;
    @BindView(R.id.iv_go_answer_where)
    ImageView ivGoAnswerWhere;
    @BindView(R.id.lv_options)
    ListView lvOptions;
    @BindView(R.id.scrollview)
    ScrollView scrollview;


    private ReadingListResp.DataBean mReadingInfo;
    private ReadingListResp.DataBean.ExercisesBean mExerciseBean;
    private List<ReadingListResp.DataBean.ExercisesBean.EducationAnswerFrom> educationAnswerFrom;

    private int mQuestionIndex;

    private ReadingPresenter mPresenter;

    private long mStartTime;
    private int mGoOriginalTime = 0;
    private ReadingActivity ra;
    private ReadingListResp.DataBean.ExercisesBean.OptionsBean mSelectedOption;
    private Map<String, Boolean> multiChoice = new HashMap<>();//存放已经保存的选项
    public static Map<Integer, Boolean> isSelected = new HashMap<>();
    private singleChoiceAdapter adapter;
    private int questionQuantity;
    private String answerShowTime; //0答完即显示  1 阅读课完事显示
    private Dialog dialog;
    private View view;

    public ReadingChoiceQuestionFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reading_choice_question;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        //初始化参数
        mReadingInfo = (ReadingListResp.DataBean) getArguments().getSerializable(ReadingActivity.READING_INFO);
        if (getArguments().getSerializable(ReadingActivity.READING_QUESTION) instanceof ReadingListResp.DataBean.ExercisesBean) {
            mExerciseBean = (ReadingListResp.DataBean.ExercisesBean) getArguments().getSerializable(ReadingActivity.READING_QUESTION);
//          是不是有点冒险这样做直接indexof
            mQuestionIndex = mReadingInfo.getExercises().indexOf(mExerciseBean);//题目编号
            questionQuantity = mReadingInfo.getExercises().size();
        }
        answerShowTime = mReadingInfo.getAnswerShowTime();
        mPresenter = new ReadingPresenter(this);
        mStartTime = System.currentTimeMillis();
        educationAnswerFrom = mExerciseBean.getEducationAnswerFroms();
        ra = (ReadingActivity) getActivity();
        ra.setToolbarTitle("课后小题");
        ra.setToolbarVisiable(true);
        ra.setToolbarBGC(getResources().getColor(R.color.bg_white));
        ra.setToolbarTitleColor(getResources().getColor(R.color.title));
        //初始化UI
        if (mExerciseBean == null) return;
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
        if (mExerciseBean.getQuestionType() == ReadingActivity.TYPE_CHOICE) {
            tvTitle.setText("单选题" + (mQuestionIndex + 1) + "/" + questionQuantity);
        } else {
            tvTitle.setText("单选题" + (mQuestionIndex + 1) + "/" + questionQuantity);
        }

        tvQuestion.setTypeface(DUTApplication.getHsbwTypeFace());

        tvQuestion.setText(mExerciseBean.getQuestionContent());

        adapter = new singleChoiceAdapter(mExerciseBean.getOptions());
        lvOptions.setAdapter(adapter);
        Utility.setListViewHeightBasedOnChildren1(lvOptions);
//        setHeight();
        adapter.notifyDataSetChanged();


        //初始化按钮状态
        if (mReadingInfo.getExercises().size() > (mQuestionIndex + 1)) {
//            后面没有题了
            btnNext.setText("下一题");
        } else {

            btnNext.setText("结束课程");
        }
        if (mReadingInfo.getCompleted() == 0) {
            if (mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED) {
                if (mReadingInfo.getAnswerShowTime().equals("0")) {
                    layoutToast.setVisibility(View.VISIBLE);
                } else {
                    layoutToast.setVisibility(View.GONE);
                }
                lvOptions.setFocusable(false);
            } else {
                layoutToast.setVisibility(View.GONE);
            }

            ivGoBackReading.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);
            ivGoAnswerWhere.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
            btnNext.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
            btnCommit.setVisibility(mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);
            //初始化提示


        } else {
            ivGoBackReading.setVisibility(View.GONE);
            ivGoAnswerWhere.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
            btnCommit.setVisibility(View.GONE);
            lvOptions.setFocusable(false);
            layoutToast.setVisibility(View.VISIBLE);
        }


        tvRightAnswer.setTypeface(DUTApplication.getHsbwTypeFace());
        if (FontsUtils.isContainChinese(mExerciseBean.getAnalysis()) || TextUtils.isEmpty(mExerciseBean.getAnalysis())) {

        } else {
            tvToast.setTypeface(DUTApplication.getHsbwTypeFace());
        }

        tvRightAnswer.setText(mExerciseBean.getRightAnswer());
        tvToast.setText(mExerciseBean.getAnalysis());
        scrollview.setOnTouchListener(new View.OnTouchListener() {
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

    }

    public void setHeight() {
        int height = 0;
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View temp = adapter.getView(i, null, lvOptions);
            temp.measure(0, 0);
            height += temp.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = this.lvOptions.getLayoutParams();
        params.width = ViewGroup.LayoutParams.FILL_PARENT;
        params.height = height + 50;
        lvOptions.setLayoutParams(params);
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

    public void disableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(false);
        }
    }

    @Override
    public void commitSuccess() {
//        disableRadioGroup(rgOptions);
        lvOptions.setFocusable(false);
        adapter.notifyDataSetChanged();
        mExerciseBean.setQuestionCompleted(ReadingActivity.STATE_FINISHED);
        ivGoBackReading.setVisibility(View.GONE);
        ivGoAnswerWhere.setVisibility(View.VISIBLE);

        btnCommit.setVisibility(View.GONE);
        btnNext.setVisibility(View.VISIBLE);

        if (mReadingInfo.getExercises().size() > (mQuestionIndex + 1)) {
//            后面没有题了
            btnNext.setText("下一题");
        } else {
            // TODO: 2017/7/28 三期
            mPresenter.showReadingData(mReadingInfo.getClassId(), mReadingInfo.getCourseId());
            // TODO: 2017/7/28 三期
            btnNext.setText("结束课程");
        }
        if (answerShowTime.equals("0")) {
            layoutToast.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void showRightAnswer() {

    }

    @Override
    public void showReadingDataSuccess(ReadingDataResp.DataBean resp) {
        //获取数据显示
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
                if (mReadingInfo.getExercises().size() > (mQuestionIndex + 1)){
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                    getHoldingActivity().finish();
                }

            }
        });
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.show();//显示对话框

    }

    @OnClick({R.id.btn_commit, R.id.btn_next, R.id.iv_go_back_reading, R.id.iv_go_answer_where})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_go_back_reading:
                mGoOriginalTime++;
                goOriginal();
                break;
            case R.id.btn_commit:
                commitAnswer();
                break;
            case R.id.btn_next:
                goNext();

                break;
            case R.id.iv_go_answer_where:
                if (educationAnswerFrom.size() == 0) {
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(getHoldingActivity());
                    builder.title("提示");
                    builder.content("当前小题没有设置答案出处，Sorry！");
                    builder.positiveText("确定");
                    builder.show();

                } else {
                    showAnswerWhere();
                }
                break;
        }
    }

    /**
     * 功能简述：显示答案出处
     */

    private void showAnswerWhere() {

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(mReadingInfo.getCourseContent());
        for (ReadingListResp.DataBean.ExercisesBean.EducationAnswerFrom item : educationAnswerFrom) {
            int wordStartIndex = item.getWordIndex();
            int wordEndIndex = wordStartIndex + item.getWordLength();
            BackgroundColorSpan yellowSpan = new BackgroundColorSpan(Color.YELLOW);
            stringBuilder.setSpan(yellowSpan, wordStartIndex, wordEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        MaterialDialog.Builder builder = new MaterialDialog.Builder(getHoldingActivity());
        builder.title(mReadingInfo.getCourseTitle());
        builder.content(stringBuilder);
        builder.positiveText("确定");
        builder.show();


    }

    /**
     * 功能简述:提交答案
     */
    private void commitAnswer() {
        long commitTime = System.currentTimeMillis();
        long timeSpan = TimeUtils.getTimeSpan(commitTime, mStartTime, ConstUtils.TimeUnit.MSEC);
        if (timeSpan == 0) {
            timeSpan = 1;
        }

        if (mExerciseBean != null && multiChoice.size() != 0) {
            StringBuffer userChoice = new StringBuffer();
            for (Map.Entry<String, Boolean> entry : multiChoice.entrySet()) {
                userChoice = userChoice.append(entry.getKey()).append(",");
            }
            String userAnswer = userChoice.toString().substring(0, userChoice.length() - 1);
            mReadingInfo.getExercises()
                    .get(mReadingInfo.getExercises().indexOf(mExerciseBean))
                    .setAnswerContent(userAnswer);
            mPresenter.commitAnswer(mExerciseBean.getQuestionId(), userAnswer, timeSpan, mGoOriginalTime, mReadingInfo.getClassId());
        } else {
            showToast("请选择答案");
        }
    }

    /**
     * 功能简述:跳转到阅读原文页面
     */
    private void goOriginal() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getHoldingActivity());
        builder.title(mReadingInfo.getCourseTitle());
        builder.content(mReadingInfo.getCourseContent());
        builder.positiveText("确定");
        builder.show();
    }

    /**
     * 功能简述:下一步
     */
    private void goNext() {
        if (mReadingInfo.getExercises().size() > (mQuestionIndex + 1)) {
            //还有课后小题
            ReadingListResp.DataBean.ExercisesBean bean = mReadingInfo.getExercises().get(mQuestionIndex + 1);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ReadingActivity.READING_INFO, mReadingInfo);
            bundle.putSerializable(ReadingActivity.READING_QUESTION, bean);

            switch (bean.getQuestionType()) {
                case ReadingActivity.TYPE_CHOICE:
                    //选择题
//                    ra.setToolbarTitle("选择题");
                    ReadingChoiceQuestionFragment choiceQuestionFragment = new ReadingChoiceQuestionFragment();
                    choiceQuestionFragment.setArguments(bundle);
                    addFragment(choiceQuestionFragment);
                    break;
                case ReadingActivity.TYPE_INPUT:
                    //填空题
//                    ra.setToolbarTitle("填空题");
                    ReadingInputNewFragment inputQuestionFragment = new ReadingInputNewFragment();
                    inputQuestionFragment.setArguments(bundle);
                    addFragment(inputQuestionFragment);
                    break;
                case ReadingActivity.TYPE_MULTI_CHOICE:
//                    多选题
//                    ra.setToolbarTitle("选择题");
                    ReadingMultiChoiceQuestionFragment multiChoiceFragment = new ReadingMultiChoiceQuestionFragment();
                    multiChoiceFragment.setArguments(bundle);
                    addFragment(multiChoiceFragment);
                    break;
                case ReadingActivity.TYPE_TRANSLATE:
//                    翻译题
//                    ra.setToolbarTitle("翻译题");
                    ReadingTranslateQuestionFragment translateQuestionFragment = new ReadingTranslateQuestionFragment();
                    translateQuestionFragment.setArguments(bundle);
                    addFragment(translateQuestionFragment);
                    break;
                default:
                    showSnackBar("没有该题型，请反馈客服");
                    break;
            }
        } else {
            //课后小题已经答完，结束
            new MaterialDialog.Builder(getHoldingActivity()).title("恭喜").content("您已完成本次阅读，是否离开？")
                    .positiveText("是")
                    .negativeText("否")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            getHoldingActivity().finish();
                        }
                    })
                    .show();
        }
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
        unbinder.unbind();
    }

    public class singleChoiceAdapter extends BaseAdapter {
        private List<ReadingListResp.DataBean.ExercisesBean.OptionsBean> mDataList;

        public singleChoiceAdapter(List<ReadingListResp.DataBean.ExercisesBean.OptionsBean> datalist) {
            this.mDataList = datalist;
            initChoice();

        }

        private void initChoice() {

            for (int i = 0; i < mDataList.size(); i++) {
                isSelected.put(i, false);
            }

        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false; // true 可以点击,false 不可以点击
        }


        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(getHoldingActivity(), R.layout.multi_choice_item, null);

                holder.tv_index = (TextView) convertView.findViewById(R.id.tv_option_index);
                holder.tv_content = (TextView) convertView.findViewById(R.id.multi_option_content);
                holder.ll_multi_item = (LinearLayout) convertView.findViewById(R.id.ll_multi_choice_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.ll_multi_item.setTag(position);

            if (mExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED) {
                if (mExerciseBean.getAnswerContent().contains(mDataList.get(position).getOptionIndex())) {
                    isSelected.put(position, true);
                    holder.tv_index.setTextColor(getResources().getColor(R.color.textColorShow));
                    holder.tv_content.setTextColor(getResources().getColor(R.color.textColorShow));
                    holder.ll_multi_item.setBackgroundResource(R.drawable.choice_blue_bg);
                } else {
                    holder.tv_index.setTextColor(getResources().getColor(R.color.text_color));
                    holder.tv_content.setTextColor(getResources().getColor(R.color.text_color));
                    holder.ll_multi_item.setBackgroundResource(R.drawable.choice_gray_bg);
                }
            } else {
                if (isSelected.get(position)) {
                    holder.tv_index.setTextColor(getResources().getColor(R.color.textColorShow));
                    holder.tv_content.setTextColor(getResources().getColor(R.color.textColorShow));
                    holder.ll_multi_item.setBackgroundResource(R.drawable.choice_blue_bg);
                } else {
                    holder.tv_index.setTextColor(getResources().getColor(R.color.text_color));
                    holder.tv_content.setTextColor(getResources().getColor(R.color.text_color));
                    holder.ll_multi_item.setBackgroundResource(R.drawable.choice_gray_bg);
                }
                holder.ll_multi_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer tag = (Integer) v.getTag();
                        if (isSelected.get(tag)) {
                            isSelected.put(tag, false);
                            multiChoice.remove(mDataList.get(position).getOptionIndex());
                        } else {
                            initChoice();
                            multiChoice.clear();
                            isSelected.put(tag, true);
                            multiChoice.put(mDataList.get(position).getOptionIndex(), true);
                        }
                        notifyDataSetChanged();
                    }

                });

            }
            holder.tv_index.setVisibility(View.VISIBLE);
            holder.tv_index.setTypeface(DUTApplication.getHsbwTypeFace());
            holder.tv_content.setTypeface(DUTApplication.getHsbwTypeFace());
            holder.tv_index.setText(mDataList.get(position).getOptionIndex() + ".");
            holder.tv_content.setText(mDataList.get(position).getOptionContent());

            return convertView;
        }
    }

    class ViewHolder {
        LinearLayout ll_multi_item;
        TextView tv_index;
        TextView tv_content;

    }

}

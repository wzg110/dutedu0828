package com.yunding.dut.ui.reading;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.ScrollView;
import android.widget.TextView;

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
 *
 */

public class ReadingPreClassMultiChoiceQuestionFragment extends BaseFragmentInReading implements IReadingQuestionView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
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
    Unbinder unbinder;
    @BindView(R.id.lv_options)
    ListView lvOptions;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private ReadingListResp.DataBean mReadingInfo;//文章全部数据的bean
    private ReadingListResp.DataBean.PreClassExercisesBean mPreExerciseBean;//文章中的练习题bean
    private int mQuestionIndex;//题号
    private long mStartTime;//答题开始时间
    private ReadingPresenter mPresenter;
    private ReadingListResp.DataBean.PreClassExercisesBean.OptionsBeanX mSelectedOption;//选项内容bean
    private Map<String, Boolean> multiChoice = new HashMap<>();//存放已经保存的选项
    public static Map<Integer, Boolean> isSelected = new HashMap<>();
    private multiChoiceAdapter adapter;
    private ReadingActivity ra;
    private int questionQuantity;
    private String answerShowTime; //0答完即显示  1 阅读课完事显示
    private Dialog dialog;
    private View view;

    public ReadingPreClassMultiChoiceQuestionFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reading_pre_class_multi_choice_question;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        //初始化各种参数
        mReadingInfo = (ReadingListResp.DataBean) getArguments().getSerializable(ReadingActivity.READING_INFO);
        if (getArguments().getSerializable(ReadingActivity.READING_QUESTION) instanceof ReadingListResp.DataBean.PreClassExercisesBean) {
            mPreExerciseBean = (ReadingListResp.DataBean.PreClassExercisesBean) getArguments().getSerializable(ReadingActivity.READING_QUESTION);
            mQuestionIndex = mReadingInfo.getPreClassExercises().indexOf(mPreExerciseBean);//题目编号
            questionQuantity = mReadingInfo.getPreClassExercises().size();
        }
        mPresenter = new ReadingPresenter(this);
        mStartTime = System.currentTimeMillis();//记录起始时间
        ra = (ReadingActivity) getActivity();
        ra.setToolbarTitle("课前小题");
        ra.setToolbarBGC(getResources().getColor(R.color.bg_white));
        ra.setToolbarTitleColor(getResources().getColor(R.color.title));
        initUI();

    }

    private void initUI() {
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
        answerShowTime = mReadingInfo.getAnswerShowTime();
        //初始化题号，题目内容
        if (mPreExerciseBean.getQuestionType() == ReadingActivity.TYPE_CHOICE) {
//            tvTitle.setText("课前小题第" + (mQuestionIndex + 1) + "题（翻译）" + "（共" + mReadingInfo.getPreClassExercises().size() + "题）");
            tvTitle.setText("多选题" + (mQuestionIndex + 1) + "/" + questionQuantity);
        } else {
            tvTitle.setText("多选题" + (mQuestionIndex + 1) + "/" + questionQuantity);
        }
        tvQuestion.setTypeface(DUTApplication.getHsbwTypeFace());
        tvQuestion.setText(mPreExerciseBean.getQuestionContent());
        //初始化选择框
        if (mPreExerciseBean.getOptions() == null) return;
        if (mPreExerciseBean.getOptions().size() == 0) return;
        adapter = new multiChoiceAdapter(mPreExerciseBean.getOptions());
        lvOptions.setAdapter(adapter);
        Utility.setListViewHeightBasedOnChildren1(lvOptions);
//        setHeight();
        adapter.notifyDataSetChanged();
//
//初始化按钮状态

        //初始化提示
        if (mReadingInfo.getCompleted() == 0) {
            if (mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED) {
                if (mReadingInfo.getAnswerShowTime().equals("0")) {
                    layoutToast.setVisibility(View.VISIBLE);
                } else {
                    layoutToast.setVisibility(View.GONE);
                }
                lvOptions.setFocusable(false);
            } else {
                layoutToast.setVisibility(View.GONE);
            }

            btnNext.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.VISIBLE : View.GONE);
            btnCommit.setVisibility(mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED ? View.GONE : View.VISIBLE);


        } else {
            lvOptions.setFocusable(false);
            btnCommit.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
            layoutToast.setVisibility(View.VISIBLE);
        }
        if (FontsUtils.isContainChinese(mPreExerciseBean.getRightAnswer())
                || TextUtils.isEmpty(mPreExerciseBean.getRightAnswer())) {

        } else {
            tvRightAnswer.setTypeface(DUTApplication.getHsbwTypeFace());
        }
        if (FontsUtils.isContainChinese(mPreExerciseBean.getAnalysis())
                || TextUtils.isEmpty(mPreExerciseBean.getAnalysis())) {

        } else {
            tvToast.setTypeface(DUTApplication.getHsbwTypeFace());
        }
        tvRightAnswer.setText(mPreExerciseBean.getRightAnswer());
        tvToast.setText(mPreExerciseBean.getAnalysis());
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

    }

    @Override
    public void showProgress() {

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

    //显隐progress
    @Override
    public void hideProgress() {

    }

    //显示信息
    @Override
    public void showMsg(String msg) {
        if (TextUtils.isEmpty(msg)) {
            showToast(R.string.server_error);
        } else {
            showToast(msg);
        }

    }

    //提交成功
    @Override
    public void commitSuccess() {
        lvOptions.setFocusable(false);
        mPreExerciseBean.setQuestionCompleted(ReadingActivity.STATE_FINISHED);
        adapter.notifyDataSetChanged();
        btnCommit.setVisibility(View.GONE);
        btnNext.setVisibility(View.VISIBLE);
        if (answerShowTime.equals("0")) {
            layoutToast.setVisibility(View.VISIBLE);
        }

    }

    //显示正确答案
    @Override
    public void showRightAnswer() {

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
                commitAnswer();
                break;
            case R.id.btn_next:
                goNext();
                break;
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
//                    ra.setToolbarTitle("选择题");
                    ReadingPreClassChoiceQuestionFragment choiceQuestionFragment = new ReadingPreClassChoiceQuestionFragment();
                    choiceQuestionFragment.setArguments(bundle);
                    addFragment(choiceQuestionFragment);
                    break;
                case ReadingActivity.TYPE_INPUT:
                    //填空题
//                    ra.setToolbarTitle("填空题");
                    ReadingPreClassInputNewFragment inputQuestionFragment = new ReadingPreClassInputNewFragment();
                    inputQuestionFragment.setArguments(bundle);
                    addFragment(inputQuestionFragment);
                    break;

                case ReadingActivity.TYPE_MULTI_CHOICE:
//                    ra.setToolbarTitle("选择题");
                    ReadingPreClassMultiChoiceQuestionFragment multiChoiceFragment = new ReadingPreClassMultiChoiceQuestionFragment();
                    multiChoiceFragment.setArguments(bundle);
                    addFragment(multiChoiceFragment);
                    break;
                case ReadingActivity.TYPE_TRANSLATE:
//                    ra.setToolbarTitle("翻译题");
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
            Bundle bundle = new Bundle();
//            ra.setToolbarTitle("阅读原文");
            bundle.putSerializable(ReadingActivity.READING_INFO, mReadingInfo);
            ReadingNewArticleFragment originalFragment = new ReadingNewArticleFragment();
            originalFragment.setArguments(bundle);
            addFragment(originalFragment);
        }
    }

    private void commitAnswer() {
        long commitTime = System.currentTimeMillis();
        long timeSpan = TimeUtils.getTimeSpan(commitTime, mStartTime, ConstUtils.TimeUnit.MSEC);
        if (timeSpan == 0) {
            timeSpan = 1;
        }

        if (mPreExerciseBean != null && multiChoice.size() != 0) {
            StringBuffer userChoice = new StringBuffer();
            for (Map.Entry<String, Boolean> entry : multiChoice.entrySet()) {
                userChoice = userChoice.append(entry.getKey()).append(",");
            }
            String userAnswer = userChoice.toString().substring(0, userChoice.length() - 1);
            mReadingInfo.getPreClassExercises()
                    .get(mReadingInfo.getPreClassExercises().indexOf(mPreExerciseBean))
                    .setAnswerContent(userAnswer);
            mPresenter.commitAnswer(mPreExerciseBean.getQuestionId(), userAnswer, timeSpan, 0, mReadingInfo.getClassId());
        } else {
            showToast("请选择答案");
        }
    }

    public class multiChoiceAdapter extends BaseAdapter {
        private List<ReadingListResp.DataBean.PreClassExercisesBean.OptionsBeanX> mDataList;

        public multiChoiceAdapter(List<ReadingListResp.DataBean.PreClassExercisesBean.OptionsBeanX> datalist) {
            this.mDataList = datalist;
            initChoice();

        }

        private void initChoice() {

            for (int i = 0; i < mDataList.size(); i++) {
                isSelected.put(i, false);
            }

        }

        @Override
        public int getCount() {
            return mDataList.size();
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
            if (mPreExerciseBean.getQuestionCompleted() == ReadingActivity.STATE_FINISHED) {

                if (mPreExerciseBean.getAnswerContent().contains(mDataList.get(position).getOptionIndex())) {
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
                            isSelected.put(tag, true);

                            multiChoice.put(mDataList.get(position).getOptionIndex(), true);
                        }
                        notifyDataSetChanged();
                    }

                });

            }
            holder.tv_index.setVisibility(View.VISIBLE);
            holder.tv_content.setTypeface(DUTApplication.getHsbwTypeFace());
            holder.tv_index.setTypeface(DUTApplication.getHsbwTypeFace());
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

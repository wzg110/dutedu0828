package com.yunding.dut.ui.discuss;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.data.discuss.DiscussAnswerCache;
import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.model.resp.discuss.DiscussQuestionListResp;
import com.yunding.dut.model.resp.discuss.DiscussQuestionParam;
import com.yunding.dut.presenter.discuss.DiscussQuestionPresenter;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.SizeUtils;
import com.yunding.dut.util.third.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.yunding.dut.ui.discuss.DiscussQuestionNewActivity.DISCUSS_ANSWER_CACHE;
import static com.yunding.dut.ui.discuss.DiscussQuestionNewActivity.DISCUSS_QUESTION_ITEM;
/**
 * 类 名 称：DiscussQuestionInputNewFragment
 * <P/>描    述：填空题
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 9:42
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 9:42
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DiscussQuestionInputNewFragment extends BaseFragmentInReading implements IDiscussQuestionView {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_question)
    TextView tvQuestion;
    @BindView(R.id.content)
    AbsoluteLayout content;
    @BindView(R.id.btn_last)
    Button btnLast;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_blue)
    ImageView ivBlue;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    Unbinder unbinder;
    private Dialog dialog;
    private View view;
    private DiscussListResp.DataBean mDiscussInfo;//讨论组信息 上传答案用
    private ArrayList<DiscussQuestionListResp.DataBean> mDataQuestionList;//问题总数据源
    private DiscussQuestionListResp.DataBean mDataQuestionItem;//应该显示的问题详情
    private ArrayList<DiscussAnswerCache> mDataCache;//答案缓存
    private DiscussQuestionPresenter mPresenter;//提交答案
    private int mQuestionIndex;//题号
    private DiscussQuestionNewActivity da;
    private TextView tv_time;
    DiscussionCountDown mCountDown;
    private int anwseras;
    private String serverTime;
    private long spanToNow,timeLeft;
    private Map<Integer, String> answerMap = new HashMap<>();//存放用户输入答案
    private Map<Integer,EditText> inputEdit=new HashMap<>();//存放edittext对象
    private Layout layout;
    private ViewTreeObserver vto;
    private ViewTreeObserver.OnGlobalLayoutListener listener;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discuss_input_new;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        da = (DiscussQuestionNewActivity) getActivity();
        da.setToolBarTitle("答题");
        mDiscussInfo = (DiscussListResp.DataBean) getArguments().getSerializable(DiscussQuestionNewActivity.DISCUSS_INFO);
        if (getArguments().getSerializable(DISCUSS_QUESTION_ITEM) instanceof DiscussQuestionListResp.DataBean) {
            mDataQuestionList = (ArrayList<DiscussQuestionListResp.DataBean>) getArguments().getSerializable(DiscussQuestionNewActivity.DISCUSS_QUESTIONG_LIST_INFO);
            mDataQuestionItem = (DiscussQuestionListResp.DataBean) getArguments().getSerializable(DISCUSS_QUESTION_ITEM);
            mQuestionIndex = mDataQuestionList.indexOf(mDataQuestionItem);
        }
        if (getArguments().getSerializable(DISCUSS_ANSWER_CACHE) != null) {
            mDataCache = (ArrayList<DiscussAnswerCache>) getArguments().getSerializable(DISCUSS_ANSWER_CACHE);
        }
        mPresenter = new DiscussQuestionPresenter(this);
        mPresenter.getServerTime();
        if (mDiscussInfo == null) return;
        tvTitle.setText("填空" + (1 + mQuestionIndex) + "/" + mDataQuestionList.size());
        tvQuestion.setTypeface(DUTApplication.getHsbwTypeFace());
        tvQuestion.setText(mDataQuestionItem.getContent());
        tv_time = (TextView) view.findViewById(R.id.tv_time);

        if (mDiscussInfo.getState() != 1) {
            if (mDiscussInfo.getState() == 0) {
                tv_time.setText("未开始");
            } else {
                tv_time.setText("已结束");
            }
            btnCommit.setBackgroundResource(R.drawable.bg_discuss_no_commit);
            ivBlue.setVisibility(View.GONE);
        } else {
            if (mDiscussInfo.getIsLeader() != 1){
                btnCommit.setVisibility(View.GONE);
            }
//            tv_time.setText(TimeUtils.millis2String(mDiscussInfo.getCountdownTime() * 60 * 1000, "mm:ss"));

            btnCommit.setBackgroundResource(R.drawable.button_corners);



        }
        initCache();
        drawTextview();
    }
    private void initCache() {
        if (mDataCache != null) {

        } else {
            mDataCache = new ArrayList<>();
            for (DiscussQuestionListResp.DataBean data : mDataQuestionList) {
                DiscussAnswerCache answerCache = new DiscussAnswerCache();
                answerCache.setQuestion(data);
                answerCache.setQuestionType(data.getType());
                answerCache.setChoiceAnswer("");

                if (mDiscussInfo.getState() != 2) {
                    Map<Integer, String> inputCache = new HashMap<>();
                    for (int i = 0; i < data.getBlanksInfo().size(); i++) {
                        inputCache.put(i, "");
                    }
                    answerCache.setInputStudentAnswer(inputCache);
                    answerCache.setTranslateAnswer("");
                } else {
                    Map<Integer, String> inputCache = new HashMap<>();
                    List<String> inputCache1 = new ArrayList<>();
                    for (String answer : data.getAnswer().split(",")) {
                        inputCache1.add(answer);
                    }
                        for (int i = 0; i < data.getBlanksInfo().size(); i++) {
                            inputCache.put(i, inputCache1.get(i));
                        }
                        answerCache.setInputStudentAnswer(inputCache);
                        answerCache.setTranslateAnswer(answerCache.getTranslateAnswer());
                    }
                    mDataCache.add(answerCache);
                }

            da.setAnswerCache(mDataCache);
        }
    }
    private void drawTextview() {
        final SpannableStringBuilder builder = new SpannableStringBuilder(tvQuestion.getText().toString());
        for (int i = 0; i < mDataQuestionItem.getBlanksInfo().size(); i++) {
            //遍历空格
            ImageSpan ss = new ImageSpan(getHoldingActivity(), R.drawable.bgzha);
            builder.setSpan(ss, mDataQuestionItem.getBlanksInfo().get(i).getIndex()
                    , mDataQuestionItem.getBlanksInfo().get(i).getIndex() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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
        vto.addOnGlobalLayoutListener(listener);;

    }
    public static String [] mSplit(final String string){
        int count=0;
        int start=0;
        char[] chars=string.toCharArray();
        List<Integer> dddd=new ArrayList<>();
        for (int i=0;i<chars.length;i++){
            if (','==(chars[i])){
                count++;
                dddd.add(i);
            }
        }

        String [] a=new String[count];
        for (int i=0;i<count;i++){
                a[i]=string.substring(start, dddd.get(i));
                start=dddd.get(i)+1;
        }
        return a;
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

                String[] studentAnswer = mSplit(mDataQuestionItem.getStudentAnswer());
               Map<Integer,String> cache=mDataCache.get(mQuestionIndex).getInputStudentAnswer();
                float letterWidth=getCharacterWidth(tvQuestion.getText().toString(),14);
                for (int i = 0; i < mDataQuestionItem.getBlanksInfo().size(); i++) {
                    answerMap.put(i, "");
                    LinearLayout ll = new LinearLayout(getHoldingActivity());
                    ll.setOrientation(LinearLayout.VERTICAL);
                    final EditText et = new EditText(ll.getContext());
                    et.setTextSize(14);
                    et.setSingleLine(true);
                    et.setBackground(null);
                    et.setHint(i + 1 + "");
                    et.setId(i);
                    et.setGravity(Gravity.CENTER);
                    et.setWidth(SizeUtils.dp2px(100));
                    et.setHeight(SizeUtils.dp2px(29));
                    et.setPadding(10, 0, 10, 0);
                    if (mDiscussInfo.getState()!=1){
                        if (mDiscussInfo.getState()==2){
                            if ("".equals(mDataQuestionItem.getStudentAnswer())
                                    ||"null".equals(mDataQuestionItem.getStudentAnswer())||
                                    mDataQuestionItem.getStudentAnswer()==null) {
                                et.setText("");
                            }else{
                                et.setText(studentAnswer[i]);
                            }
                        }else {
                            et.setText("");
                        }
                        et.setFocusable(false);
                    }else{
                        if (mDataCache==null){
                            et.setText("");
                        }else{
                            et.setText(cache.get(i));
                        }
                        et.setFocusable(true);
                    }
                    et.setTextColor(Color.BLACK);
                    ll.addView(et);

                    TextView asda=new TextView(ll.getContext());
                    asda.setWidth(SizeUtils.dp2px(100));
                    asda.setHeight(SizeUtils.dp2px(1));
                    asda.setBackgroundResource(R.color.orange);
                    ll.addView(asda);
                    et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            answerMap.put(v.getId(), et.getText().toString());
                            mDataCache.get(mQuestionIndex).getInputStudentAnswer().put(v.getId(),et.getText().toString());
                            da.setAnswerCache(mDataCache);

                        }
                    });
                    float[] aa = getPosition(mDataQuestionItem.getBlanksInfo().get(i).getIndex());

                    AbsoluteLayout.LayoutParams lp = null;
                    lp = new AbsoluteLayout.LayoutParams(SizeUtils.dp2px(100),
                            SizeUtils.dp2px(30), (int) (aa[0] +SizeUtils.sp2px(14)+10),
                            (int) (aa[1] -SizeUtils.dp2px(10)-15));

                    inputEdit.put(i,et);
                    content.addView(ll, lp);
                }


            }
        }
    };
    private float[] getPosition(int tv) {
        float[] aa = new float[2];
//        获取位置
        Rect bound = new Rect();
        int line = layout.getLineForOffset(tv);
        layout.getLineBounds(line, bound);
        float yAxisTop = bound.bottom;//字符顶部y坐标
        float xAxisRight = layout.getPrimaryHorizontal(tv);//字符右边x坐标
        aa[0] = xAxisRight;
        aa[1] = yAxisTop;
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
        if (mCountDown != null) {
            mCountDown.cancel();
        }
    }

    @OnClick({R.id.btn_last, R.id.btn_next,R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_last:
             content.clearFocus();
                if (mQuestionIndex == 0) {

                    showToast("当前已经为第一题");
                } else {
                    removeFragment();
                }
                break;
            case R.id.btn_next:
                content.clearFocus();
                goNext();
                break;
            case R.id.btn_commit:
                content.clearFocus();
                if (mDiscussInfo.getIsLeader() == 1) {
                    if (mDiscussInfo.getState() != 1) {

                    } else {
                        showDialog();
                    }
                } else {
                    showToast("只有组长可以提交答案");
                }
                break;
        }
    }

    private void showDialog() {
        dialog = new Dialog(getHoldingActivity(), R.style.ActionSheetDialogStyle);
        view = LayoutInflater.from(getHoldingActivity()).inflate(R.layout.login_exit_dialog, null);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        TextView tv = (TextView) view.findViewById(R.id.tv_message_content);
        tv.setText("讨论未结束可重复提交答案，确定提交？");
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDiscussInfo != null && mPresenter != null) {
                    mPresenter.commitAnswer(getPram());
                } else showMsg("开启失败");
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
    private String getPram() {

        DiscussQuestionParam param = new DiscussQuestionParam();
        List<DiscussQuestionParam.DataBean> list = new ArrayList<>();
        for (DiscussAnswerCache cache : mDataCache) {
            DiscussQuestionParam.DataBean dataBean = new DiscussQuestionParam.DataBean();
            if (cache.getQuestionType() == 0) {
                dataBean.setAnswer(cache.getChoiceAnswer());
                dataBean.setGroupId(String.valueOf(mDiscussInfo.getGroupId()));
                dataBean.setStudentId(String.valueOf(DUTApplication.getUserInfo().getUserId()));
                dataBean.setThemeId(String.valueOf(cache.getQuestion().getThemeId()));
                dataBean.setTopicId(String.valueOf(cache.getQuestion().getTopicId()));
            } else {
                 Map<Integer,String> inputAnswer = cache.getInputStudentAnswer();
                String answerStr = "";
                for (int i=0;i<inputAnswer.size();i++) {
                    answerStr += (inputAnswer.get(i).replaceAll("#","") + ",");
                }
                answerStr = answerStr.substring(0, answerStr.length());
                dataBean.setAnswer(answerStr);
                dataBean.setGroupId(String.valueOf(mDiscussInfo.getGroupId()));
                dataBean.setStudentId(String.valueOf(DUTApplication.getUserInfo().getUserId()));
                dataBean.setThemeId(String.valueOf(mDiscussInfo.getThemeId()));
                dataBean.setTopicId(String.valueOf(cache.getQuestion().getTopicId()));
            }
            list.add(dataBean);
        }
        param.setData(list);
        String paramStr = new Gson().toJson(param);
        return paramStr;
    }
    private class DiscussionCountDown extends CountDownTimer {

        public DiscussionCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long l) {

            if (l <= 0) {
                tv_time.setText("已结束");
                ivBlue.setVisibility(View.GONE);
                if (mCountDown != null)
                    mCountDown.cancel();
            } else {
                tv_time.setText(TimeUtils.millis2String(l,"mm:ss"));
                ivBlue.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onFinish() {
            mPresenter.changeDiscussState(String.valueOf(mDiscussInfo.getGroupId()),String.valueOf(mDiscussInfo.getThemeId()));
            da.setStatus(2);
            tv_time.setText("已结束");
            ivBlue.setVisibility(View.GONE);
            btnCommit.setBackgroundResource(R.drawable.bg_discuss_no_commit);
            mDiscussInfo.setState(2);
        }
    }
    private void goNext() {
        if ((mQuestionIndex + 1) < mDataQuestionList.size()) {

//                直接进下一题
            if (mDataQuestionList.get(mQuestionIndex + 1).getType() == 0) {
//                   选择
                DiscussChoiceFragment discussChoiceFragment = new DiscussChoiceFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(da.DISCUSS_INFO, mDiscussInfo);
                bundle.putSerializable(da.DISCUSS_QUESTIONG_LIST_INFO, mDataQuestionList);
                bundle.putSerializable(DISCUSS_ANSWER_CACHE, mDataCache);
                bundle.putSerializable(DISCUSS_QUESTION_ITEM, mDataQuestionList.get(mQuestionIndex + 1));
                discussChoiceFragment.setArguments(bundle);
                addFragment(discussChoiceFragment);
            } else if (mDataQuestionList.get(mQuestionIndex + 1).getType() == 1) {
//                    填空
                DiscussQuestionInputNewFragment discussChoiceFragment = new DiscussQuestionInputNewFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(da.DISCUSS_INFO, mDiscussInfo);
                bundle.putSerializable(da.DISCUSS_QUESTIONG_LIST_INFO, mDataQuestionList);
                bundle.putSerializable(DISCUSS_ANSWER_CACHE, mDataCache);
                bundle.putSerializable(DISCUSS_QUESTION_ITEM, mDataQuestionList.get(mQuestionIndex + 1));

                discussChoiceFragment.setArguments(bundle);
                addFragment(discussChoiceFragment);

            } else if (mDataQuestionList.get(mQuestionIndex + 1).getType() == 2) {
//                  多选
                DisucssMutiFragment discussChoiceFragment = new DisucssMutiFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(da.DISCUSS_INFO, mDiscussInfo);
                bundle.putSerializable(da.DISCUSS_QUESTIONG_LIST_INFO, mDataQuestionList);
                bundle.putSerializable(DISCUSS_ANSWER_CACHE, mDataCache);
                bundle.putSerializable(DISCUSS_QUESTION_ITEM, mDataQuestionList.get(mQuestionIndex + 1));

                discussChoiceFragment.setArguments(bundle);
                addFragment(discussChoiceFragment);
            } else if (mDataQuestionList.get(mQuestionIndex + 1).getType() == 3) {
//                    翻译
                DiscussTranaslateFragment discussChoiceFragment = new DiscussTranaslateFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(da.DISCUSS_INFO, mDiscussInfo);
                bundle.putSerializable(da.DISCUSS_QUESTIONG_LIST_INFO, mDataQuestionList);
                bundle.putSerializable(DISCUSS_ANSWER_CACHE, mDataCache);
                bundle.putSerializable(DISCUSS_QUESTION_ITEM, mDataQuestionList.get(mQuestionIndex + 1));
                discussChoiceFragment.setArguments(bundle);
                addFragment(discussChoiceFragment);

            }

        } else {
            showToast("木有下一题了");
        }


    }
    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
    @Override
    public void showQuestions(DiscussQuestionListResp resp) {

    }
    @Override
    public void showMsg(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            showToast(msg);
        } else {
            showToast(R.string.server_error);
        }

    }
    @Override
    public void commitAnswerSuccess() {
        showToast("提交成功");
        getHoldingActivity().finish();
    }
    @Override
    public void getServerTime(String time) {
        serverTime=time;
        spanToNow = TimeUtils.getTimeSpan(serverTime,mDiscussInfo.getOpeningTime(), ConstUtils.TimeUnit.MSEC);
        timeLeft = mDiscussInfo.getCountdownTime()*60*1000 - spanToNow;
        if (mDiscussInfo.getState() == 1){
            mCountDown = new DiscussionCountDown(timeLeft, 1000);
            mCountDown.start();
        }
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

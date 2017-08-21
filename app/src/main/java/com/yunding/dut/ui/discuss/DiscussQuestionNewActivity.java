package com.yunding.dut.ui.discuss;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.yunding.dut.model.data.discuss.DiscussAnswerCache;
import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.model.resp.discuss.DiscussQuestionListResp;
import com.yunding.dut.ui.base.BaseFragment;
import com.yunding.dut.ui.base.BaseFragmentActivity;

import java.util.ArrayList;

/**
 * 类 名 称：DiscussQuestionNewActivity
 * <P/>描    述： fragment基础activity
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 16:31
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 16:31
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DiscussQuestionNewActivity extends BaseFragmentActivity {
    public static String DISCUSS_QUESTIONG_LIST_INFO = "discuss_question_list_info";
    public static String DISCUSS_INFO = "DISCUSS_INFO";
    public static String DISCUSS_QUESTION_ITEM="discuss_question_item";
    public static String DISCUSS_ANSWER_CACHE="discuss_answer_cache";
    private DiscussListResp.DataBean mDiscussInfo;
    private ArrayList<DiscussAnswerCache> mDataCache;
    private ArrayList<DiscussQuestionListResp.DataBean> mDataQuestionList;

    private int state=-1;

    public static final int TYPE_TRANSLATE=4;
    public static final int TYPE_MULTI_CHOICE=3;
    public static final int TYPE_CHOICE = 2;
    public static final int TYPE_INPUT = 1;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {

            removeFragment();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected BaseFragment getFirstFragment() {
        getmToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("datas",mDataCache);
                intent.putExtra("state",state);
                setResult(100,intent);
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent.getSerializableExtra("datas")!=null){
            mDataCache = (ArrayList<DiscussAnswerCache>) intent.getSerializableExtra("datas");
        }
        mDiscussInfo= (DiscussListResp.DataBean) getIntent().getSerializableExtra(DISCUSS_INFO);
        mDataQuestionList= (ArrayList<DiscussQuestionListResp.DataBean>) getIntent().getSerializableExtra(DISCUSS_QUESTIONG_LIST_INFO);
        if (mDiscussInfo != null) {
            if (mDataQuestionList!=null&& mDataQuestionList.size() > 0) {
                if (mDataQuestionList.get(0).getType()==0){
//                   选择
                    DiscussChoiceFragment discussChoiceFragment = new DiscussChoiceFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DISCUSS_INFO, mDiscussInfo);
                    bundle.putSerializable(DISCUSS_QUESTIONG_LIST_INFO, mDataQuestionList);
                    bundle.putSerializable(DISCUSS_ANSWER_CACHE,mDataCache);
                    bundle.putSerializable(DISCUSS_QUESTION_ITEM,mDataQuestionList.get(0));
                    discussChoiceFragment.setArguments(bundle);
                    return discussChoiceFragment;
                }else if (mDataQuestionList.get(0).getType()==1){
//                    填空
                    DiscussQuestionInputNewFragment discussChoiceFragment = new DiscussQuestionInputNewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DISCUSS_INFO, mDiscussInfo);
                    bundle.putSerializable(DISCUSS_QUESTIONG_LIST_INFO, mDataQuestionList);
                    if (intent.getSerializableExtra("datas")!=null){
                        bundle.putSerializable(DISCUSS_ANSWER_CACHE,mDataCache);
                    }
                    bundle.putSerializable(DISCUSS_QUESTION_ITEM,mDataQuestionList.get(0));

                    discussChoiceFragment.setArguments(bundle);
                    return discussChoiceFragment;

                }else if (mDataQuestionList.get(0).getType()==2){
//                  多选
                    DisucssMutiFragment discussChoiceFragment = new DisucssMutiFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DISCUSS_INFO, mDiscussInfo);
                    bundle.putSerializable(DISCUSS_QUESTIONG_LIST_INFO, mDataQuestionList);
                    if (intent.getSerializableExtra("datas")!=null){
                        bundle.putSerializable(DISCUSS_ANSWER_CACHE,mDataCache);
                    }
                    bundle.putSerializable(DISCUSS_QUESTION_ITEM,mDataQuestionList.get(0));

                    discussChoiceFragment.setArguments(bundle);
                    return discussChoiceFragment;
                }else if(mDataQuestionList.get(0).getType()==3){
//                    翻译
                    DiscussTranaslateFragment discussChoiceFragment = new DiscussTranaslateFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DISCUSS_INFO, mDiscussInfo);
                    bundle.putSerializable(DISCUSS_QUESTIONG_LIST_INFO, mDataQuestionList);
                    if (intent.getSerializableExtra("datas")!=null){
                        bundle.putSerializable(DISCUSS_ANSWER_CACHE,mDataCache);
                    }
                    bundle.putSerializable(DISCUSS_QUESTION_ITEM,mDataQuestionList.get(0));

                    discussChoiceFragment.setArguments(bundle);
                    return discussChoiceFragment;

                }


            }else{
                showToast("没有讨论题目");
                finish();
            }

        }else{
            showToast("出错了");
            finish();
            return null;

        }
        return null;
    }
    public void setAnswerCache(ArrayList<DiscussAnswerCache> mAnswerCache){
        this.mDataCache=mAnswerCache;
    }
    public void setStatus(int status){
        this.state=status;
    }
    public void  setToolBarTitle(String s){
        setTitleInCenter(s);
    }
}

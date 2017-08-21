package com.yunding.dut.ui.ppt;

import android.os.Bundle;

import com.yunding.dut.model.resp.ppt.pptSelfListResp;
import com.yunding.dut.ui.base.BaseFragment;
import com.yunding.dut.ui.base.BaseFragmentActivity;
import com.yunding.dut.util.third.TimeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PPTSelfActivity extends BaseFragmentActivity implements BackHandledInterface {
    public static final int TYPE_EM=5;
    public static final int TYPE_QUESTION_ANSWER=4;
    public static final int TYPE_MULTI_CHOICE=3;
    public static final int TYPE_CHOICE = 2;
    public static final int TYPE_INPUT = 1;
    public static final String PPTINFO="PPT_INFO";//所有ppt信息
    public static final String  PPT_QUESTION_INFO="PPT_QUESTION_INFO";//点击ppt的题目
    public static final String PPT_INFO_ITEM="PPTInfo_Item";//点击ppt的信息
    private pptSelfListResp.DataBean mPPTInfo;
    private  pptSelfListResp.DataBean.SlidesBean mPPTInfoItem;
    private BackHandledFragment mBackHandedFragment;
    private String studyMode;
private List<String>pptImageList=new ArrayList<>();
    @Override
    protected BaseFragment getFirstFragment() {

//        getmToolbar().setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        mPPTInfo= (pptSelfListResp.DataBean) getIntent().getSerializableExtra(PPTINFO);
        studyMode=getIntent().getStringExtra("STUDY_MODE");
        pptImageList=getIntent().getStringArrayListExtra("pptImage");
        mPPTInfoItem=mPPTInfo.getSlides().get(mPPTInfo.getLastStayPPTIndex()-1);
        if (mPPTInfoItem!=null){
            if (mPPTInfoItem.getSlideQuestions()!=null&& mPPTInfoItem.getSlideQuestions().size()>0){
                mPPTInfoItem.setStartTime(TimeUtils.getNowTimeMills());
                mPPTInfo.getSlides().set(mPPTInfo.getLastStayPPTIndex()-1,mPPTInfoItem);
                if (mPPTInfoItem.getSlideQuestions().get(mPPTInfo.getLastStayPPTQuestionIndex()-1).getQuestionType()==TYPE_INPUT){

                    PPTQuestionInputSelfNewFragment pptQuestionInputFragment = new PPTQuestionInputSelfNewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, mPPTInfoItem.getSlideQuestions().get(mPPTInfo.getLastStayPPTQuestionIndex()-1));
                    bundle.putSerializable(PPT_INFO_ITEM,mPPTInfoItem);
                    bundle.putString("STUDY_MODE",studyMode);
                    bundle.putSerializable("pptImage", (Serializable) pptImageList);
                    pptQuestionInputFragment.setArguments(bundle);
                    return pptQuestionInputFragment;

                }else if (mPPTInfoItem.getSlideQuestions().get(mPPTInfo.getLastStayPPTQuestionIndex()-1).getQuestionType()==TYPE_CHOICE){

                    PPTQuestionChoiceSelfFragment pptQuestionChoiceFragment = new PPTQuestionChoiceSelfFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putString("STUDY_MODE",studyMode);
                    bundle.putSerializable(PPT_QUESTION_INFO, mPPTInfoItem.getSlideQuestions().get(mPPTInfo.getLastStayPPTQuestionIndex()-1));
                    bundle.putSerializable(PPT_INFO_ITEM,mPPTInfoItem);
                    bundle.putSerializable("pptImage", (Serializable) pptImageList);
                    pptQuestionChoiceFragment.setArguments(bundle);
                    return pptQuestionChoiceFragment;

                }else if (mPPTInfoItem.getSlideQuestions().get(mPPTInfo.getLastStayPPTQuestionIndex()-1).getQuestionType()==TYPE_MULTI_CHOICE){

                    PPTQuestionMutiChoiceSelfFragment pptQuestionMutiChoiceFragment = new PPTQuestionMutiChoiceSelfFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putString("STUDY_MODE",studyMode);
                    bundle.putSerializable(PPT_QUESTION_INFO, mPPTInfoItem.getSlideQuestions().get(mPPTInfo.getLastStayPPTQuestionIndex()-1));
                    bundle.putSerializable(PPT_INFO_ITEM,mPPTInfoItem);
                    bundle.putSerializable("pptImage", (Serializable) pptImageList);
                    pptQuestionMutiChoiceFragment.setArguments(bundle);
                    return pptQuestionMutiChoiceFragment;

                }else if (mPPTInfoItem.getSlideQuestions().get(mPPTInfo.getLastStayPPTQuestionIndex()-1).getQuestionType()==TYPE_QUESTION_ANSWER){

                    PPTQuestionAnswerSelfFragment pptQuestionAnswerFragment = new PPTQuestionAnswerSelfFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putString("STUDY_MODE",studyMode);
                    bundle.putSerializable(PPT_QUESTION_INFO, mPPTInfoItem.getSlideQuestions().get(mPPTInfo.getLastStayPPTQuestionIndex()-1));
                    bundle.putSerializable(PPT_INFO_ITEM,mPPTInfoItem);
                    bundle.putSerializable("pptImage", (Serializable) pptImageList);
                    pptQuestionAnswerFragment.setArguments(bundle);
                    return pptQuestionAnswerFragment;

                }else if (mPPTInfoItem.getSlideQuestions().get(mPPTInfo.getLastStayPPTQuestionIndex()-1).getQuestionType()==TYPE_EM){

                    PPTQuestionEnumerateSelfFragment pptQuestionEnumerateFragment = new PPTQuestionEnumerateSelfFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putString("STUDY_MODE",studyMode);
                    bundle.putSerializable(PPT_QUESTION_INFO, mPPTInfoItem.getSlideQuestions().get(mPPTInfo.getLastStayPPTQuestionIndex()-1));
                    bundle.putSerializable(PPT_INFO_ITEM,mPPTInfoItem);
                    bundle.putSerializable("pptImage", (Serializable) pptImageList);
                    pptQuestionEnumerateFragment.setArguments(bundle);
                    return pptQuestionEnumerateFragment;

                }else{

                    PPTQuestionNoCountSelfFragment pptNoQuestionFragment = new PPTQuestionNoCountSelfFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("STUDY_MODE",studyMode);
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_INFO_ITEM,mPPTInfoItem);
                    bundle.putSerializable("pptImage", (Serializable) pptImageList);
                    pptNoQuestionFragment.setArguments(bundle);
                    return pptNoQuestionFragment;
                }

            }else{

//               没有题进没有题的页面
                PPTQuestionNoCountSelfFragment pptNoQuestionFragment = new PPTQuestionNoCountSelfFragment();
                Bundle bundle = new Bundle();
                bundle.putString("STUDY_MODE",studyMode);
                bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                bundle.putSerializable(PPT_INFO_ITEM,mPPTInfoItem);
                bundle.putSerializable("pptImage", (Serializable) pptImageList);
                pptNoQuestionFragment.setArguments(bundle);
                return pptNoQuestionFragment;
            }

        }else{
            showToast("没有ppt信息");
            finish();
            return null;
        }


    }

    @Override
    public void onBackPressed() {
        if(mBackHandedFragment == null || !mBackHandedFragment.onBackPressed()){
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                super.onBackPressed();
            }else{
                removeFragment();
            }
        }
    }
    public void setToolbarTitle(String title){
        setTitleInCenter(title);
    }
    public void setToolbarTitleColor(int color){
        setToolbarTitleColor1(color);
    }

    public void  setToolbarBGC(int id){
        setToolbarBackgroundColor(id);
    }
    public void setToolbarVisiable(boolean isVisiable){
        setToolBarVisible(isVisiable);
    }

    @Override
    public void setSelectedFragment(BackHandledFragment selectedFragment) {
        this.mBackHandedFragment = selectedFragment;
    }
}

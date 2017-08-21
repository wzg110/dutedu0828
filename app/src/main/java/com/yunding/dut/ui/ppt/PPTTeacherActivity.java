package com.yunding.dut.ui.ppt;

import android.os.Bundle;

import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.ui.base.BaseFragment;
import com.yunding.dut.ui.base.BaseFragmentActivity;
import com.yunding.dut.util.third.TimeUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 类 名 称：ReadingInputQuestionFragment
 * <P/>描    述：阅读填空题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/8/1
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/8/1
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class PPTTeacherActivity extends BaseFragmentActivity {
    //    1-填空 2-单选 3-多选 4-问答 5-枚举
    public static final int TYPE_EM=5;
    public static final int TYPE_QUESTION_ANSWER=4;
    public static final int TYPE_MULTI_CHOICE=3;
    public static final int TYPE_CHOICE = 2;
    public static final int TYPE_INPUT = 1;
    public static final String PPTINFO="PPT_INFO";//所有ppt信息
    public static final String  PPT_QUESTION_INFO="PPT_QUESTION_INFO";//点击ppt的题目
    public static final String PPT_INFO_ITEM="PPTInfo_Item";//点击ppt的信息
    private List<PPTResp.DataBean> mPPTInfo;
    private  PPTResp.DataBean mPPTInfoItem;
    private BackHandledFragment mBackHandedFragment;
    private int position;
    private List<String> pptImageList=new ArrayList<>();

    @Override
    protected BaseFragment getFirstFragment() {
        mPPTInfo= (List<PPTResp.DataBean>) getIntent().getSerializableExtra(PPTINFO);
        mPPTInfoItem= (PPTResp.DataBean) getIntent().getSerializableExtra(PPT_INFO_ITEM);
        position=getIntent().getIntExtra("position",0);
        pptImageList=getIntent().getStringArrayListExtra("pptImage");
        if (mPPTInfoItem!=null){
            if (mPPTInfoItem.getSlideQuestions()!=null&&mPPTInfoItem.getSlideQuestions().size()>0){
                mPPTInfoItem.setStartTime(TimeUtils.getNowTimeMills());
                mPPTInfo.set(position,mPPTInfoItem);
                if (mPPTInfoItem.getSlideQuestions().get(0).getQuestionType()==TYPE_INPUT){

                    PPTQuestionInputTeacherNewFragment pptQuestionInputFragment = new PPTQuestionInputTeacherNewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, mPPTInfoItem.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,mPPTInfoItem);
                    bundle.putSerializable("pptImage", (Serializable) pptImageList);
                    bundle.putInt("position",position);
                    pptQuestionInputFragment.setArguments(bundle);
                    return pptQuestionInputFragment;

                }else if (mPPTInfoItem.getSlideQuestions().get(0).getQuestionType()==TYPE_CHOICE){
                    PPTQuestionChoiceTeacherFragment pptQuestionChoiceFragment = new PPTQuestionChoiceTeacherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, mPPTInfoItem.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,mPPTInfoItem);
                    bundle.putSerializable("pptImage", (Serializable) pptImageList);
                    bundle.putInt("position",position);
                    pptQuestionChoiceFragment.setArguments(bundle);
                    return pptQuestionChoiceFragment;

                }else if (mPPTInfoItem.getSlideQuestions().get(0).getQuestionType()==TYPE_MULTI_CHOICE){

                    PPTQuestionMutiChoiceTeacherFragment pptQuestionMutiChoiceFragment = new PPTQuestionMutiChoiceTeacherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, mPPTInfoItem.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,mPPTInfoItem);
                    bundle.putSerializable("pptImage", (Serializable) pptImageList);
                    bundle.putInt("position",position);
                    pptQuestionMutiChoiceFragment.setArguments(bundle);
                    return pptQuestionMutiChoiceFragment;

                }else if (mPPTInfoItem.getSlideQuestions().get(0).getQuestionType()==TYPE_QUESTION_ANSWER){

                    PPTQuestionAnswerTeacherFragment pptQuestionAnswerFragment = new PPTQuestionAnswerTeacherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, mPPTInfoItem.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,mPPTInfoItem);
                    bundle.putSerializable("pptImage", (Serializable) pptImageList);
                    bundle.putInt("position",position);
                    pptQuestionAnswerFragment.setArguments(bundle);
                    return pptQuestionAnswerFragment;

                }else if (mPPTInfoItem.getSlideQuestions().get(0).getQuestionType()==TYPE_EM){

                    PPTQuestionEnumerateTeacherFragment pptQuestionEnumerateFragment = new PPTQuestionEnumerateTeacherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, mPPTInfoItem.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,mPPTInfoItem);
                    bundle.putSerializable("pptImage", (Serializable) pptImageList);
                    bundle.putInt("position",position);
                    pptQuestionEnumerateFragment.setArguments(bundle);
                    return pptQuestionEnumerateFragment;

                }else{
                    PPTQuestionNoCountTeacherFragment pptNoQuestionFragment = new PPTQuestionNoCountTeacherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_INFO_ITEM,mPPTInfoItem);
                    bundle.putSerializable("pptImage", (Serializable) pptImageList);
                    bundle.putInt("position",position);
                    pptNoQuestionFragment.setArguments(bundle);
                    return pptNoQuestionFragment;
                }

            }else{
//               没有题进没有题的页面
                PPTQuestionNoCountTeacherFragment pptNoQuestionFragment = new PPTQuestionNoCountTeacherFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                bundle.putSerializable(PPT_INFO_ITEM,mPPTInfoItem);
                bundle.putSerializable("pptImage", (Serializable) pptImageList);
                bundle.putInt("position",position);
                pptNoQuestionFragment.setArguments(bundle);
                return pptNoQuestionFragment;
            }

        }else{
            showToast("没有ppt信息");
            finish();
            return null;
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
}

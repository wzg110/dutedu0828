package com.yunding.dut.ui.ppt;

import android.os.Bundle;

import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.model.resp.ppt.pptSelfListResp;
import com.yunding.dut.ui.base.BaseFragmentInReading;
import com.yunding.dut.util.third.TimeUtils;

import java.io.Serializable;
import java.util.List;

import static com.yunding.dut.ui.ppt.PPTActivity.PPTINFO;
import static com.yunding.dut.ui.ppt.PPTActivity.PPT_INFO_ITEM;
import static com.yunding.dut.ui.ppt.PPTActivity.PPT_QUESTION_INFO;

/**
 * 类 名 称：ReadingInputQuestionFragment
 * <P/>描    述：阅读填空题页面
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/7/19
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/7/19
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class pptUtils {
    public static BaseFragmentInReading changePPTTeacher(List<PPTResp.DataBean>mPPTInfo, PPTResp.DataBean bean,int position){
        if (bean!=null){
            if (bean.getSlideQuestions()!=null&&bean.getSlideQuestions().size()>0){

                if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_INPUT){
                    bean.setStartTime(TimeUtils.getNowTimeMills());

                    PPTQuestionInputTeacherNewFragment pptQuestionInputFragment = new PPTQuestionInputTeacherNewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    bundle.putInt("position",position);
                    pptQuestionInputFragment.setArguments(bundle);
                    return pptQuestionInputFragment;

                }else if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_CHOICE){
                    bean.setStartTime(TimeUtils.getNowTimeMills());
                    PPTQuestionChoiceTeacherFragment pptQuestionChoiceFragment = new PPTQuestionChoiceTeacherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    bundle.putInt("position",position);
                    pptQuestionChoiceFragment.setArguments(bundle);
                    return pptQuestionChoiceFragment;

                }else if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_MULTI_CHOICE){
                    bean.setStartTime(TimeUtils.getNowTimeMills());
                    PPTQuestionMutiChoiceTeacherFragment pptQuestionMutiChoiceFragment = new PPTQuestionMutiChoiceTeacherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    bundle.putInt("position",position);
                    pptQuestionMutiChoiceFragment.setArguments(bundle);
                    return pptQuestionMutiChoiceFragment;

                }else if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_QUESTION_ANSWER){
                    bean.setStartTime(TimeUtils.getNowTimeMills());
                    PPTQuestionAnswerTeacherFragment pptQuestionAnswerFragment = new PPTQuestionAnswerTeacherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    bundle.putInt("position",position);
                    pptQuestionAnswerFragment.setArguments(bundle);
                    return pptQuestionAnswerFragment;

                }else if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_EM){
                    bean.setStartTime(TimeUtils.getNowTimeMills());
                    PPTQuestionEnumerateTeacherFragment pptQuestionEnumerateFragment = new PPTQuestionEnumerateTeacherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    bundle.putInt("position",position);
                    pptQuestionEnumerateFragment.setArguments(bundle);
                    return pptQuestionEnumerateFragment;

                }else{
                    PPTQuestionNoCountTeacherFragment pptNoQuestionFragment = new PPTQuestionNoCountTeacherFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    bundle.putInt("position",position);
                    pptNoQuestionFragment.setArguments(bundle);
                    return pptNoQuestionFragment;
                }

            }else{
//               没有题进没有题的页面
                PPTQuestionNoCountTeacherFragment pptNoQuestionFragment = new PPTQuestionNoCountTeacherFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                bundle.putSerializable(PPT_INFO_ITEM,bean);
                bundle.putInt("position",position);
                pptNoQuestionFragment.setArguments(bundle);
                return pptNoQuestionFragment;
            }

        }else{
            return null;
        }
    }
    public static BaseFragmentInReading changePPT(List<PPTResp.DataBean>mPPTInfo,
                                                  PPTResp.DataBean bean,int position){
        if (bean!=null){
            if (bean.getSlideQuestions()!=null&&bean.getSlideQuestions().size()>0){

                if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_INPUT){
                    bean.setStartTime(TimeUtils.getNowTimeMills());
                    PPTQuestionInputNewFragment pptQuestionInputFragment = new PPTQuestionInputNewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    bundle.putInt("position",position);
                    pptQuestionInputFragment.setArguments(bundle);
                    return pptQuestionInputFragment;

                }else if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_CHOICE){
                    bean.setStartTime(TimeUtils.getNowTimeMills());
                    PPTQuestionChoiceFragment pptQuestionChoiceFragment = new PPTQuestionChoiceFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    bundle.putInt("position",position);
                    pptQuestionChoiceFragment.setArguments(bundle);
                    return pptQuestionChoiceFragment;

                }else if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_MULTI_CHOICE){
                    bean.setStartTime(TimeUtils.getNowTimeMills());
                    PPTQuestionMutiChoiceFragment pptQuestionMutiChoiceFragment = new PPTQuestionMutiChoiceFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    bundle.putInt("position",position);
                    pptQuestionMutiChoiceFragment.setArguments(bundle);
                    return pptQuestionMutiChoiceFragment;

                }else if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_QUESTION_ANSWER){
                    bean.setStartTime(TimeUtils.getNowTimeMills());
                    PPTQuestionAnswerFragment pptQuestionAnswerFragment = new PPTQuestionAnswerFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    bundle.putInt("position",position);
                    pptQuestionAnswerFragment.setArguments(bundle);
                    return pptQuestionAnswerFragment;

                }else if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_EM){
                    bean.setStartTime(TimeUtils.getNowTimeMills());
                    PPTQuestionEnumerateFragment pptQuestionEnumerateFragment = new PPTQuestionEnumerateFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    bundle.putInt("position",position);
                    pptQuestionEnumerateFragment.setArguments(bundle);
                    return pptQuestionEnumerateFragment;

                }else{
                    PPTQuestionNoCountFragment pptNoQuestionFragment = new PPTQuestionNoCountFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    bundle.putInt("position",position);
                    pptNoQuestionFragment.setArguments(bundle);
                    return pptNoQuestionFragment;
                }

            }else{
//               没有题进没有题的页面
                PPTQuestionNoCountFragment pptNoQuestionFragment = new PPTQuestionNoCountFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                bundle.putSerializable(PPT_INFO_ITEM,bean);
                bundle.putInt("position",position);
                pptNoQuestionFragment.setArguments(bundle);
                return pptNoQuestionFragment;
            }

        }else{
            return null;
        }
    }
    public static BaseFragmentInReading changePPT(pptSelfListResp.DataBean mPPTInfo,
                                                  pptSelfListResp.DataBean.SlidesBean bean,String studyMode){
        if (bean!=null){
            if (bean.getSlideQuestions()!=null&&bean.getSlideQuestions().size()>0){

                if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_INPUT){
                    bean.setStartTime(TimeUtils.getNowTimeMills());
                    PPTQuestionInputSelfNewFragment pptQuestionInputFragment = new PPTQuestionInputSelfNewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putString("STUDY_MODE",studyMode);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    pptQuestionInputFragment.setArguments(bundle);
                    return pptQuestionInputFragment;

                }else if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_CHOICE){
                    bean.setStartTime(TimeUtils.getNowTimeMills());
                    PPTQuestionChoiceSelfFragment pptQuestionChoiceFragment = new PPTQuestionChoiceSelfFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putString("STUDY_MODE",studyMode);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    pptQuestionChoiceFragment.setArguments(bundle);
                    return pptQuestionChoiceFragment;

                }else if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_MULTI_CHOICE){
                    bean.setStartTime(TimeUtils.getNowTimeMills());
                    PPTQuestionMutiChoiceSelfFragment pptQuestionMutiChoiceFragment = new PPTQuestionMutiChoiceSelfFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putString("STUDY_MODE",studyMode);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    pptQuestionMutiChoiceFragment.setArguments(bundle);
                    return pptQuestionMutiChoiceFragment;

                }else if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_QUESTION_ANSWER){
                    bean.setStartTime(TimeUtils.getNowTimeMills());
                    PPTQuestionAnswerSelfFragment pptQuestionAnswerFragment = new PPTQuestionAnswerSelfFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putString("STUDY_MODE",studyMode);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    pptQuestionAnswerFragment.setArguments(bundle);
                    return pptQuestionAnswerFragment;

                }else if (bean.getSlideQuestions().get(0).getQuestionType()==PPTActivity.TYPE_EM){
                    bean.setStartTime(TimeUtils.getNowTimeMills());
                    PPTQuestionEnumerateSelfFragment pptQuestionEnumerateFragment = new PPTQuestionEnumerateSelfFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_QUESTION_INFO, bean.getSlideQuestions().get(0));
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    bundle.putString("STUDY_MODE",studyMode);
                    pptQuestionEnumerateFragment.setArguments(bundle);
                    return pptQuestionEnumerateFragment;

                }else{
                    PPTQuestionNoCountSelfFragment pptNoQuestionFragment = new PPTQuestionNoCountSelfFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                    bundle.putSerializable(PPT_INFO_ITEM,bean);
                    bundle.putString("STUDY_MODE",studyMode);
                    pptNoQuestionFragment.setArguments(bundle);
                    return pptNoQuestionFragment;
                }

            }else{
//               没有题进没有题的页面
                PPTQuestionNoCountSelfFragment pptNoQuestionFragment = new PPTQuestionNoCountSelfFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(PPTINFO, (Serializable) mPPTInfo);
                bundle.putSerializable(PPT_INFO_ITEM,bean);
                bundle.putString("STUDY_MODE",studyMode);
                pptNoQuestionFragment.setArguments(bundle);
                return pptNoQuestionFragment;
            }

        }else{
            return null;
        }
    }
}

package com.yunding.dut.ui.reading;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yunding.dut.R;
import com.yunding.dut.ui.base.BaseFragment;

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
public class ReadingChoiceQuestionFragment extends BaseFragment {


    public ReadingChoiceQuestionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reading_choice_question, container, false);
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {

    }

}

package com.yunding.dut.ui.base;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.util.FontsUtils;
import com.yunding.dut.view.ToolBarHelper;

/**
 * 类 名 称：ToolBarFragment
 * <P/>描    述：带有ToolBar的Fragment
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/19 18:09
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/19 18:09
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public abstract class ToolBarFragment extends BaseFragment {

    private ToolBarHelper mHelper;
    public Toolbar mToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mHelper = new ToolBarHelper(getHoldingActivity(), getLayoutId());
        mToolbar = mHelper.getToolBar();
        getHoldingActivity().setSupportActionBar(mToolbar);//这句代码不可以加，加上会导致menu错乱
        onCreateCustomToolBar(mToolbar);

        if (getHoldingActivity().getSupportActionBar() != null) {
            getHoldingActivity().getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        View contentView = mHelper.getContentView();
        initView(contentView, savedInstanceState);
        return contentView;
    }

    public void onCreateCustomToolBar(Toolbar toolbar) {
        toolbar.setContentInsetsRelative(0, 0);
    }

    protected void setTitleInCenter(String title) {
        if (FontsUtils.isContainChinese(title)|| TextUtils.isEmpty(title)){

        }else{
            mHelper.getTitle().setTypeface(DUTApplication.getHsbwTypeFace());
        }
        mHelper.getTitle().setText(title);
    }

    protected void setTitle(String title) {
        mToolbar.setTitle(title);
    }

    protected void setShowNavigation(boolean isShowNavigation) {
        if (!isShowNavigation) {
            mToolbar.setNavigationIcon(null);
        }
    }

    protected void setNavigationIcon(int resId) {
        mToolbar.setNavigationIcon(resId);
    }

    public Toolbar getmToolbar() {
        return mToolbar;
    }
    public SimpleDraweeView getmToolbarIvMe(){
        return  mHelper.getIvMe();
    }
}

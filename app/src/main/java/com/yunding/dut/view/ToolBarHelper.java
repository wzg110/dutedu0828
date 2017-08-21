package com.yunding.dut.view;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;

/**
 * 类 名 称：ToolBarHelper
 * <P/>描    述：绘制ToolBar的工具类
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/19 11:23
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/19 11:23
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public class ToolBarHelper {

    //上下文，创建view的时候需要用到
    private Context mContext;
    private FrameLayout mContentView;
    private Toolbar mToolBar;
    private TextView mTitle;
    private RelativeLayout mExit;
    private  TextView mFeedBack;
    private LayoutInflater mInflater;
    private SimpleDraweeView iv_me;
    private ImageView iv_ppt_list;

    public ToolBarHelper(Context context, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);

        //初始化整个内容
        initContentView();

        //初始化用户定义的布局
        initUserView(layoutId);

        //初始化toolbar
        initToolBar();
    }

    private void initContentView() {
        //直接创建一个帧布局，作为视图容器的父容器
        mContentView = new FrameLayout(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mContentView.setLayoutParams(params);

    }

    private void initUserView(int layoutId) {
        View userView = mInflater.inflate(layoutId, null);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.topMargin = (int) mContext.getResources().getDimension(R.dimen.app_toolbar_height);
        mContentView.addView(userView, params);
    }

    private void initToolBar() {
        //通过inflater获取toolbar的布局文件
        View toolbar = mInflater.inflate(R.layout.layout_toolbar, mContentView);
        mToolBar = (Toolbar) toolbar.findViewById(R.id.toolbar);
        iv_me= (SimpleDraweeView) toolbar.findViewById(R.id.iv_me);
        mTitle = (TextView) toolbar.findViewById(R.id.tv_title);
        mExit= (RelativeLayout) toolbar.findViewById(R.id.tv_exit);
        mFeedBack= (TextView) toolbar.findViewById(R.id.tv_exit1);
        iv_ppt_list= (ImageView) toolbar.findViewById(R.id.iv_ppt_list);
    }
    public FrameLayout getContentView() {
        return mContentView;
    }
    public Toolbar getToolBar() {
        return mToolBar;
    }
    public TextView getTitle() {
        return mTitle;
    }
    public RelativeLayout getExit(){
        return mExit;
    }
    public SimpleDraweeView getIvMe(){return iv_me;}
    public TextView getmFeedBack(){
        return mFeedBack;
    }
    public ImageView getIvPPTList(){return iv_ppt_list;}
}

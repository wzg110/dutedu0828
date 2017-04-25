package com.yunding.dut.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yunding.dut.app.DUTApplication;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 类 名 称：BaseFragment
 * <P/>描    述：Fragment基类
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/19 17:56
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/19 17:56
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public abstract class BaseFragmentInReading extends BaseFragment {

    protected BaseFragmentActivity mActivity;
    //解除绑定
    private Unbinder unbinder;

    public BaseFragmentInReading() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (BaseFragmentActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initView(view, savedInstanceState);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    /**
     * 功能简述: 跳转到指定的Fragment
     *
     * @param fragment [目标Fragment]
     */
    protected void addFragment(BaseFragmentInReading fragment) {
        if (fragment != null) {
            getHoldingActivity().addFragment(fragment);
        }
    }

    /**
     * 功能简述: 跳转到指定的Fragment
     *
     * @param fragment [目标Fragment]
     */
    protected void hideAndAddFragment(BaseFragmentInReading fragment) {
        if (fragment != null) {
            getHoldingActivity().hideAndAddFragment(fragment);
        }
    }

    /**
     * 功能简述: 移除当前栈顶的Fragment
     */
    protected void removeFragment() {
        getHoldingActivity().removeFragment();
    }

    protected BaseFragmentActivity getHoldingActivity() {
        return mActivity;
    }

    protected abstract int getLayoutId();

    protected abstract void initView(View view, Bundle saveInstanceState);

    public void showToast(int msgId) {
        Toast.makeText(DUTApplication.getInstance(), msgId, Toast.LENGTH_SHORT).show();
    }

    public void showToast(String msg) {
        Toast.makeText(DUTApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showSnackBar(int msgId) {
        Snackbar.make(getHoldingActivity().getWindow().getDecorView(), msgId, Snackbar.LENGTH_SHORT).show();
    }

    public void showSnackBar(String msg) {
        Snackbar.make(getHoldingActivity().getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
    }

    protected void showProgressDialog() {

    }

    protected void hideProgressDialog() {

    }

    protected void showProgressBar() {

    }

    protected void hideProgressBar() {

    }
}

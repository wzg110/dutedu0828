package com.yunding.dut.ui.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.yunding.dut.R;

import java.util.List;


/**
 * 项目名称：印乐
 * <P/>类描述：用来管理由多个Fragment组成的Activity的基类
 * <P/>创建人：天明
 * <P/>创建时间：2016/9/9 10:07
 */
public abstract class BaseFragmentActivity extends ToolBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getContentViewId());
//        ButterKnife.bind(this);

        //添加需要显示的第一个Fragment
        if (getSupportFragmentManager().getFragments() == null) {
            BaseFragment firstFragment = getFirstFragment();
            if (firstFragment != null) {
                addFragment(firstFragment);
            }
        }

    }

    /**
     * 功能简述: 返回展示Fragment基类Activity布局id
     *
     * @return [基类Activity布局id]
     */
    private int getContentViewId() {
        return R.layout.activity_base_fragment;
    }

    /**
     * 功能简述: 返回Fragment容器id
     *
     * @return [Fragment容器id]
     */
    private int getFragmentContentId() {
        return R.id.fragment_container_layout;
    }

    /**
     * 功能简述:返回第一个需要显示的Fragment
     *
     * @return [第一个需要显示的Fragment]
     */
    protected abstract BaseFragment getFirstFragment();

    /**
     * 功能简述:添加Fragment
     *
     * @param fragment [将要添加的Fragment]
     */
    public void addFragment(BaseFragment fragment) {
        if (fragment != null) {
            int id = getFragmentContentId();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(getFragmentContentId(), fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    /**
     * 功能简述:添加Fragment
     *
     * @param fragment [将要添加的Fragment]
     */
    public void hideAndAddFragment(BaseFragment fragment) {
        if (fragment != null) {
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.hide(manager.findFragmentById(getFragmentContentId()));
            transaction.add(getFragmentContentId(), fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    /**
     * 功能简述:移除Fragment
     */
    public void removeFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();

        } else {
            finish();
        }
    }

    public void removeALLFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStackImmediate(null,1);

        } else {
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}

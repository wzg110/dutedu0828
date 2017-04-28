package com.yunding.dut.ui.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.yunding.dut.R;
import com.yunding.dut.view.ToolBarHelper;


public abstract class ToolBarActivity extends BaseActivity {

    public Toolbar mToolbar;
    private ToolBarHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        mHelper = new ToolBarHelper(this, layoutResID);
        mToolbar = mHelper.getToolBar();
        setContentView(mHelper.getContentView());
        setSupportActionBar(mToolbar);
        onCreateCustomToolBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    public void onCreateCustomToolBar(Toolbar toolbar) {
        toolbar.setContentInsetsRelative(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setTitleInCenter(String title) {
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

    protected void setToolBarVisible(boolean isToolBarVisible) {
        mToolbar.setVisibility(isToolBarVisible ? View.VISIBLE : View.GONE);
    }

    public Toolbar getmToolbar() {
        return mToolbar;
    }
}

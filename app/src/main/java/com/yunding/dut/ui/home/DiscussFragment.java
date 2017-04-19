package com.yunding.dut.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yunding.dut.R;
import com.yunding.dut.ui.base.ToolBarFragment;
import com.yunding.dut.ui.me.MeActivity;

/**
 * 类 名 称：DiscussFragment
 * <P/>描    述：讨论
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/19 19:20
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/19 19:20
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public class DiscussFragment extends ToolBarFragment {


    public DiscussFragment() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_discuss;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        setTitleInCenter("讨论");
        setShowNavigation(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getmToolbar().inflateMenu(R.menu.menu_discuss);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_me:
                startActivity(new Intent(getHoldingActivity(), MeActivity.class));
                break;
        }
        return true;
    }
}

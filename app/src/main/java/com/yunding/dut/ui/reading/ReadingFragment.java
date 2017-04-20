package com.yunding.dut.ui.reading;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.yunding.dut.R;
import com.yunding.dut.ui.base.ToolBarFragment;
import com.yunding.dut.ui.me.MeActivity;

/**
 * 类 名 称：ReadingFragment
 * <P/>描    述：阅读
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/19 19:21
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/19 19:21
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public class ReadingFragment extends ToolBarFragment {

    public ReadingFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reading;
    }

    @Override
    protected void initView(View view, Bundle saveInstanceState) {
        setTitleInCenter("阅读");
        setShowNavigation(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.menu_reading,menu);//这种方法设置menu不显示
        getmToolbar().getMenu().clear();
        getmToolbar().inflateMenu(R.menu.menu_reading);
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

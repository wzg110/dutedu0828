package com.yunding.dut.ui.home;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.yunding.dut.R;
import com.yunding.dut.adapter.HomePagerAdapter;
import com.yunding.dut.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.vp_home)
    ViewPager vpHome;
    @BindView(R.id.bnv_home)
    BottomNavigationView bnvHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        initBottomNavigationView();
        HomePagerAdapter adapter = new HomePagerAdapter(getSupportFragmentManager());
        vpHome.setAdapter(adapter);
        vpHome.setOffscreenPageLimit(2);
        vpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bnvHome.setSelectedItemId(R.id.action_course);
                        break;
                    case 1:
                        bnvHome.setSelectedItemId(R.id.action_reading);
                        break;
                    case 2:
                        bnvHome.setSelectedItemId(R.id.action_discuss);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initBottomNavigationView() {
        int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},
                new int[]{android.R.attr.state_checked}
        };

        int[] colors = new int[]{
                getResources().getColor(R.color.bottom_navigation_normal),
                getResources().getColor(R.color.bottom_navigation_selected)
        };
        ColorStateList csl = new ColorStateList(states, colors);
        bnvHome.setItemTextColor(csl);
        bnvHome.setItemIconTintList(csl);
        bnvHome.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_course:
                vpHome.setCurrentItem(0, false);
                return true;
            case R.id.action_reading:
                vpHome.setCurrentItem(1, false);
                return true;
            case R.id.action_discuss:
                vpHome.setCurrentItem(2, false);
                return true;
        }
        return false;
    }
}

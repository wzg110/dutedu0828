package com.yunding.dut.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yunding.dut.ui.discuss.DiscussListFragment;
import com.yunding.dut.ui.reading.ReadingFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private static final int READING_INDEX = 0;
    private static final int DISCUSS_INDEX = 1;

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case READING_INDEX:
                fragment = new ReadingFragment();
                break;
            case DISCUSS_INDEX:
                fragment = new DiscussListFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

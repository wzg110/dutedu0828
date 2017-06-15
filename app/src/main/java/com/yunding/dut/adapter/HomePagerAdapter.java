package com.yunding.dut.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yunding.dut.ui.discuss.DiscussListFragment;
import com.yunding.dut.ui.ppt.CourseListFragment;
import com.yunding.dut.ui.reading.ReadingListFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {

    private static final int COURSE_INDEX = 0;
    private static final int READING_INDEX = 1;
    private static final int DISCUSS_INDEX = 2;

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case COURSE_INDEX:
                fragment = new CourseListFragment();
                break;
            case READING_INDEX:
                fragment = new ReadingListFragment();
                break;
            case DISCUSS_INDEX:
                fragment = new DiscussListFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

package com.yunding.dut.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.ui.discuss.DiscussListFragment;
import com.yunding.dut.ui.ppt.CourseListForTeacherFragment;
import com.yunding.dut.ui.ppt.CourseListFragment;
import com.yunding.dut.ui.reading.ReadingListFragment;
/**
 * 类 名 称：HomePagerAdapter
 * <P/>描    述： 页面tabAdapter
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:00
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:00
 * <P/>修改备注：
 * <P/>版    本：
 */
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
//                Log.e("getUSER_TYPE",DUTApplication.getUserInfo().getUSER_TYPE());
                if (DUTApplication.getUserInfo().getUSER_TYPE().equals("1")){
                    fragment = new CourseListForTeacherFragment();
                }else{
                    fragment = new CourseListFragment();

                }

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

package com.yunding.dut.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * 类 名 称：ViewPagerAdapter
 * <P/>描    述：ppt浏览viewpageradapter
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:10
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:10
 * <P/>修改备注：
 * <P/>版    本：
 */

public class  ViewPagerAdapter extends PagerAdapter {
    private List <View> mImageUrl;
    private Context mContext;
    private  int mtype;
    public ViewPagerAdapter(List<View>imageUrl,Context context,int Type){
        this.mImageUrl=imageUrl;
        this.mContext=context;
        this.mtype=Type;
    }
    @Override
    public int getCount() {
        return mImageUrl.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }


    @Override
    public void destroyItem(View view, int position, Object object)                       //销毁Item
    {
        if (mtype==0){
            ((VerticalViewPager) view).removeView(mImageUrl.get(position));
        }else{

            ((ViewPager) view).removeView(mImageUrl.get(position));
        }

    }

    @Override
    public Object instantiateItem(View view, int position)                                //实例化Item
    {
        if (mtype==0){
            ((VerticalViewPager) view).addView(mImageUrl.get(position), 0);
        }else{
            ((ViewPager) view).addView(mImageUrl.get(position), 0);
        }

        return mImageUrl.get(position);
    }

}

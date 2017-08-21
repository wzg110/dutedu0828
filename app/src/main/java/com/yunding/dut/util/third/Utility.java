package com.yunding.dut.util.third;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 类 名 称：dutedu
 * <P/>描    述：处理listview显示不全问题
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/18
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/18
 * <P/>修改备注：
 * <P/>版    本：
 */

public class Utility {


    public static void setListViewHeightBasedOnChildren1(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        View view;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, null, listView);
            //宽度为屏幕宽度
            int i1 = View.MeasureSpec.makeMeasureSpec(ScreenUtils.getScreenWidth(),
                    View.MeasureSpec.EXACTLY);
            //根据屏幕宽度计算高度
            int i2 = View.MeasureSpec.makeMeasureSpec(i1, View.MeasureSpec.UNSPECIFIED);
            view.measure(i1, i2);
            totalHeight += view.getMeasuredHeight();

        }

        params.height = totalHeight + (SizeUtils.dp2px(12)* (listAdapter.getCount()));
        listView.setLayoutParams(params);

    }
}

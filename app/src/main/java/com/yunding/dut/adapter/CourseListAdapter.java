package com.yunding.dut.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunding.dut.R;
import com.yunding.dut.model.resp.ppt.CourseListResp;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.TimeUtils;

import java.util.List;

/**
 * 类 名 称：CourseListAdapter
 * <P/>描    述：首页->课程列表适配器
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 15:39
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 15:39
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class CourseListAdapter extends BaseQuickAdapter<CourseListResp.DataBean.DatasBean, BaseViewHolder> {

    public CourseListAdapter(List<CourseListResp.DataBean.DatasBean> data) {
        super(R.layout.item_ppt_course_list, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, CourseListResp.DataBean.DatasBean item) {
        holder.setText(R.id.tv_course_name, item.getClassName());

        String time;
        if (TimeUtils.getTimeSpanByNow(item.getCreateTime(), ConstUtils.TimeUnit.DAY) <= 7) {
            time = TimeUtils.getWeek(item.getCreateTime());
        } else {
            time = TimeUtils.string2Date(item.getCreateTime(), "MM-dd").toString();
        }
        holder.setText(R.id.tv_time, time);

        switch (item.getStatus()) {
            case 0:
                holder.setText(R.id.tv_state, "未上课");
                break;
            case 1:
                holder.setText(R.id.tv_state, "上课中");
                break;
            case 2:
                holder.setText(R.id.tv_state, "");
                break;
        }
    }
}

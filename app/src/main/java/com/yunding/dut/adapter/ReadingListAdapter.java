package com.yunding.dut.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.util.FontsUtils;

import java.util.List;

/**
 * 类 名 称：ReadingListAdapter
 * <P/>描    述：阅读列表适配器
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/24 16:39
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/24 16:39
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class ReadingListAdapter extends BaseQuickAdapter<ReadingListResp.DataBean, BaseViewHolder> {

    public ReadingListAdapter(List<ReadingListResp.DataBean> data) {
        super(R.layout.item_reading_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ReadingListResp.DataBean item) {

        if (FontsUtils.isContainChinese(item.getUnitName())|| TextUtils.isEmpty(item.getUnitName())){
            helper.setTypeface(R.id.tv_unit_name, DUTApplication.getZhTypeFace());
        }else{
            helper.setTypeface(R.id.tv_unit_name, DUTApplication.getHsbwTypeFace());
        }
        if (FontsUtils.isContainChinese(item.getCourseTitle())|| TextUtils.isEmpty(item.getUnitName())){
            helper.setTypeface(R.id.tv_reading_valid_date, DUTApplication.getZhTypeFace());
        }else{
            helper.setTypeface(R.id.tv_reading_valid_date, DUTApplication.getHsbwTypeFace());
        }
        if (FontsUtils.isContainChinese(item.getCreateTime())|| TextUtils.isEmpty(item.getUnitName())){
            helper.setTypeface(R.id.tv_time_left, DUTApplication.getZhTypeFace());
        }else{
            helper.setTypeface(R.id.tv_time_left, DUTApplication.getHsbwTypeFace());
        }
        helper.setText(R.id.tv_unit_name,item.getUnitName());
        helper.setText(R.id.tv_reading_valid_date,item.getCourseTitle());
        helper.setText(R.id.tv_time_left, item.getDeadline());

    }
}

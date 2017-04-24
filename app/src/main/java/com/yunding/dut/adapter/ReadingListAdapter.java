package com.yunding.dut.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunding.dut.R;
import com.yunding.dut.model.resp.reading.ReadingListResp;
import com.yunding.dut.util.third.TimeUtils;

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
        helper.setText(R.id.tv_reading_name, item.getCourseTitle());
        helper.setText(R.id.tv_reading_valid_date, item.getCreateTime());
    }
}

package com.yunding.dut.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.discuss.DiscussListResp;
import com.yunding.dut.util.FontsUtils;

import java.util.List;

/**
 * 类 名 称：DiscussGroupListAdapter
 * <P/>描    述：讨论组列表适配器
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/21 18:01
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/21 18:01
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class DiscussGroupListAdapter extends BaseQuickAdapter<DiscussListResp.DataBean, BaseViewHolder> {

    public DiscussGroupListAdapter(List<DiscussListResp.DataBean> data) {
        super(R.layout.item_discuss_group_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscussListResp.DataBean item) {
        if (FontsUtils.isContainChinese(item.getThemeName()) || TextUtils.isEmpty(item.getThemeName())) {
            helper.setTypeface(R.id.tv_group_name, DUTApplication.getZhTypeFace());
        } else {
            helper.setTypeface(R.id.tv_group_name, DUTApplication.getHsbwTypeFace());
        }
        if (FontsUtils.isContainChinese(item.getContent()) || TextUtils.isEmpty(item.getContent())) {
            helper.setTypeface(R.id.tv_group_latest_msg, DUTApplication.getZhTypeFace());
        } else {
            helper.setTypeface(R.id.tv_group_latest_msg, DUTApplication.getHsbwTypeFace());
        }
        helper.setText(R.id.tv_group_name, item.getThemeName());
        helper.setText(R.id.tv_group_number, item.getNum() + "人");
        helper.setText(R.id.tv_group_latest_msg, item.getContent());
        helper.setText(R.id.tv_group_valid_date, item.getEndTime());
    }
}

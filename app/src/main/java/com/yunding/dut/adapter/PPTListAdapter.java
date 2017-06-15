package com.yunding.dut.adapter;

import android.net.Uri;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.model.resp.ppt.PPTListResp;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.TimeUtils;

import java.util.Date;
import java.util.List;

/**
 * 类 名 称：PPTListAdapter
 * <P/>描    述：PPT列表适配器
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/6/13 15:44
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/6/13 15:44
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class PPTListAdapter extends BaseQuickAdapter<PPTListResp.DataBean, BaseViewHolder> {

    public PPTListAdapter(List<PPTListResp.DataBean> data) {
        super(R.layout.item_ppt_ppt_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PPTListResp.DataBean item) {
        long timeSpan = TimeUtils.getTimeSpanByNow(item.getCreateTime(), ConstUtils.TimeUnit.DAY);
        if (timeSpan <= 1) {
            helper.setText(R.id.tv_time, TimeUtils.date2String(TimeUtils.string2Date(item.getCreateTime()),"HH:mm"));
        } else if (timeSpan > 1 && timeSpan <= 7) {
            helper.setText(R.id.tv_time, TimeUtils.getWeek(item.getCreateTime()) + TimeUtils.date2String(TimeUtils.string2Date(item.getCreateTime()),"HH:mm"));
        } else if (timeSpan > 7 && timeSpan <= 365) {
            helper.setText(R.id.tv_time, TimeUtils.date2String(TimeUtils.string2Date(item.getCreateTime()),"yyyy-MM-dd HH:mm"));
        }

        helper.setText(R.id.tv_page_no, "第几页");
        helper.setText(R.id.tv_question_no, item.getSubjectId() == 0 ? "" : "第一题");
        SimpleDraweeView imgPPT = helper.getView(R.id.img_ppt);
        imgPPT.setImageURI(Uri.parse(Apis.TEST_URL + item.getPPTImgUrl()));

        helper.setVisible(R.id.tv_question_no, item.getSubjectId() != 0);
    }
}

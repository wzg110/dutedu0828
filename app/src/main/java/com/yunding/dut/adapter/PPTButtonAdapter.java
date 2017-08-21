package com.yunding.dut.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunding.dut.R;
import com.yunding.dut.model.resp.ppt.PPTResp;

import java.util.List;

/**
 * 类 名 称：PPTButtonAdapter
 * <P/>描    述：PPT切换按钮Adapter
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:02
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:02
 * <P/>修改备注：
 * <P/>版    本：
 */
public class PPTButtonAdapter extends BaseQuickAdapter<PPTResp.DataBean, BaseViewHolder>{
private  int mindex;
    public PPTButtonAdapter(List<PPTResp.DataBean> data,int index) {
        super( R.layout.list_item_choose_button, data);
        this.mindex=index;
    }

    @Override
    protected void convert(BaseViewHolder helper, PPTResp.DataBean item) {

        helper.setText(R.id.tv_indexs,item.getPageIndex()+"");
        if (helper.getAdapterPosition() <= (mindex - 1)) {
            if (helper.getAdapterPosition() == (mindex - 1)) {
//                hsss.iv_select_item.setBackgroundResource(R.drawable.ic_selected_circle);
                helper.setBackgroundRes(R.id.iv_select_item,R.drawable.ic_selected_circle);
            } else {

                helper.setBackgroundRes(R.id.iv_select_item,R.drawable.ic_circle_orange);
            }

        } else {

            helper.setBackgroundRes(R.id.iv_select_item,R.drawable.ic_circle_orange);
        }

    }
}

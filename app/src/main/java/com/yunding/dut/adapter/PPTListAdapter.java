package com.yunding.dut.adapter;

import android.net.Uri;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.util.api.Apis;

import java.util.List;

/**
 * 类 名 称：PPTListAdapter
 * <P/>描    述：ppt列表Adapter
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:07
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:07
 * <P/>修改备注：
 * <P/>版    本：
 */

public class PPTListAdapter extends BaseQuickAdapter<PPTResp.DataBean, BaseViewHolder> {

    public PPTListAdapter(List<PPTResp.DataBean> data) {
        super(R.layout.item_ppt_ppt_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PPTResp.DataBean item) {
        helper.setText(R.id.tv_time,item.getPlatformTime());
        helper.setText(R.id.tv_page, "第"+item.getPageIndex()+"页");
        SimpleDraweeView imgPPT = helper.getView(R.id.img_ppt);
        imgPPT.setImageURI(Uri.parse(Apis.TEST_URL2 + item.getSlideImage()));
        if (item.getSlideQuestions()==null||item.getSlideQuestions().size()==0){
            helper.setVisible(R.id.iv_has_question_tips, false);
        }else{
            helper.setVisible(R.id.iv_has_question_tips, true);
        }

    }


}

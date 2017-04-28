package com.yunding.dut.adapter;

import android.net.Uri;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.model.resp.discuss.DiscussGroupInfoResp;
import com.yunding.dut.util.api.Apis;

import java.util.List;

/**
 * 类 名 称：DiscussGroupMemberAdapter
 * <P/>描    述：讨论组成员适配器
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/21 19:22
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/21 19:22
 * <P/>修改备注：
 * <P/>版    本：1.0
 */

public class DiscussGroupMemberAdapter extends BaseQuickAdapter<DiscussGroupInfoResp.DataBean.StudentsBean, BaseViewHolder> {

    public DiscussGroupMemberAdapter(List<DiscussGroupInfoResp.DataBean.StudentsBean> data) {
        super(R.layout.item_discuss_group_member, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DiscussGroupInfoResp.DataBean.StudentsBean item) {
        SimpleDraweeView imgAvatar = helper.getView(R.id.img_avatar);
        imgAvatar.setImageURI(Uri.parse(Apis.SERVER_URL + item.getAvatarUrl()));
        if (item.getIsLeader() == 1) {
            helper.setText(R.id.tv_name, "(组长)" + item.getStudentName());
        } else {
            helper.setText(R.id.tv_name, item.getStudentName());
        }
    }
}

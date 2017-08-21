package com.yunding.dut.adapter;

import android.net.Uri;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.discuss.DiscussGroupInfoResp;
import com.yunding.dut.util.FontsUtils;
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
        if (FontsUtils.isContainChinese(item.getStudentName())|| TextUtils.isEmpty(item.getStudentName())){

        }else{
            helper.setTypeface(R.id.tv_name, DUTApplication.getHsbwTypeFace());
        }
        if (item.getIsLeader() == 1) {

            helper.setVisible(R.id.iv_leader_tips,true);

        } else {
            helper.setVisible(R.id.iv_leader_tips,false);
        }
        helper.setText(R.id.tv_name, item.getStudentName());
    }
}

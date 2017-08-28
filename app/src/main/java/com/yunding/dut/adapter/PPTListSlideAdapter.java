package com.yunding.dut.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.yunding.dut.R;
import com.yunding.dut.model.resp.ppt.PPTResp;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.SizeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类 名 称：dutedu
 * <P/>描    述：
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/22
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/22
 * <P/>修改备注：
 * <P/>版    本：
 */

public class PPTListSlideAdapter extends BaseQuickAdapter<PPTResp.DataBean, BaseViewHolder> {
    private int mPosition=0;
    private LayoutInflater inflater;
    private Map<Integer,SimpleDraweeView> map=new HashMap<>();
    public PPTListSlideAdapter(List<PPTResp.DataBean> data) {
        super(R.layout.ppt_list_slide_item, data);
    }

public void setShowTips(int position){
    this.mPosition=position;
    notifyDataSetChanged();

}

    @Override
    protected void convert(BaseViewHolder helper, PPTResp.DataBean item) {
        helper.setText(R.id.tv_time,item.getPlatformTime());
        helper.setText(R.id.tv_page, item.getPageIndex());
        try{
        SimpleDraweeView imgPPT = helper.getView(R.id.img_ppt);
            Log.e("Lsit",Apis.TEST_URL2 + item.getSlideImage());
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(Apis.TEST_URL2 + item.getSlideImage()))
                .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(64), SizeUtils.dp2px(48)))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(imgPPT.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .setImageRequest(request)
                .build();
        imgPPT.setController(controller);
        }catch (Exception e){
            e.getStackTrace();
        }
        if (item.getSlideQuestions()==null||item.getSlideQuestions().size()==0){
            helper.setVisible(R.id.iv_has_question_tips, false);
        }else{
            helper.setVisible(R.id.iv_has_question_tips, true);
        }
        if (mPosition==helper.getAdapterPosition()){
            helper.setVisible(R.id.iv_selected, true);
        }else{
            helper.setVisible(R.id.iv_selected, false);
        }



    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);

    }

}

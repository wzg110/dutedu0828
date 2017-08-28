package com.yunding.dut.adapter;

import android.net.Uri;

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
import com.yunding.dut.model.resp.ppt.CourseListResp;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.third.SizeUtils;

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

public class CourseListAdapter extends BaseQuickAdapter<CourseListResp.DataBean, BaseViewHolder> {

    public CourseListAdapter(List<CourseListResp.DataBean> data) {
        super(R.layout.item_ppt_course_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseListResp.DataBean item) {
        helper.setText(R.id.tv_course_name, item.getSpeciality());
        helper.setText(R.id.tv_content, item.getFileName());
        helper.setText(R.id.tv_time, item.getPlatformTime());

        try {
            SimpleDraweeView imgPPT = helper.getView(R.id.img_ppt_narrow);
//        imgPPT.setImageURI(Uri.parse(Apis.TEST_URL2 + item.getSlideImage()));

            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(Apis.TEST_URL2 + item.getCover()))
                    .setResizeOptions(new ResizeOptions(SizeUtils.dp2px(80), SizeUtils.dp2px(60)))
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
        helper.setText(R.id.tv_teacher_name,item.getTeacherName());
        if (item.getStudyMode() == 0) {
            helper.setVisible(R.id.btn_change_info, true);
            switch (item.getStatus()) {
                case 0:
                    helper.setText(R.id.btn_change_info, "未上课");
                    break;
                case 1:
                    helper.setText(R.id.btn_change_info, "上课中");
                    break;
                case 2:
                    helper.setText(R.id.btn_change_info, "已结束");
                    break;
            }
        } else {
            helper.setVisible(R.id.btn_change_info, false);

        }
    }


}

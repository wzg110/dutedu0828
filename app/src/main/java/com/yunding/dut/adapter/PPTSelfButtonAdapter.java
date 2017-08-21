package com.yunding.dut.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunding.dut.R;
import com.yunding.dut.model.resp.ppt.pptSelfListResp;

import java.util.List;

/**
 * 类 名 称：PPTSelfButtonAdapter
 * <P/>描    述：自学按钮adapter
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:09
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:09
 * <P/>修改备注：
 * <P/>版    本：
 */
public class PPTSelfButtonAdapter extends BaseQuickAdapter<pptSelfListResp.DataBean.SlidesBean,BaseViewHolder>{
    private  int mindex;

    public PPTSelfButtonAdapter( List<pptSelfListResp.DataBean.SlidesBean> data,int index) {

        super(R.layout.list_item_choose_button, data);
        this.mindex=index;
    }

    @Override
    protected void convert(BaseViewHolder helper, pptSelfListResp.DataBean.SlidesBean item) {
        helper.setText(R.id.tv_indexs,(helper.getAdapterPosition()+1)+"");
        if (helper.getAdapterPosition() <= (mindex - 1)){
            if (helper.getAdapterPosition() == (mindex - 1)) {
//                hsss.iv_select_item.setBackgroundResource(R.drawable.ic_selected_circle);
                helper.setBackgroundRes(R.id.iv_select_item,R.drawable.ic_selected_circle);
            } else {

                helper.setBackgroundRes(R.id.iv_select_item,R.drawable.ic_circle_orange);
            }

        }else{
            if (item.getQuestionsCompleted()==1){
                helper.setBackgroundRes(R.id.iv_select_item,R.drawable.ic_circle_orange);
            }else{
                helper.setBackgroundRes(R.id.iv_select_item,R.drawable.ic_circle_gray);
            }

        }
    }
}
//
//public class PPTSelfButtonAdapter  extends BaseAdapter{
//    private pptSelfListResp.DataBean mDataList;
//    private Context mContext;
//    private int mPPTIndex;
//    public PPTSelfButtonAdapter(pptSelfListResp.DataBean bean,Context context,int PPTIndex){
//        this.mDataList=bean;
//        this.mContext=context;
//        this.mPPTIndex=PPTIndex;
//    }
//    @Override
//    public int getCount() {
//        return mDataList.getSlides().size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return mDataList.getSlides().get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        holder hsss;
//        if (convertView==null){
//            hsss=new holder();
//            convertView=View.inflate(mContext, R.layout.list_item_choose_button,null);
//            hsss.iv_select_item= (SimpleDraweeView) convertView.findViewById(R.id.iv_select_item);
//            hsss.tv_indexs= (TextView) convertView.findViewById(R.id.tv_indexs);
//            convertView.setTag(hsss);
//
//        }else{
//            hsss= (holder) convertView.getTag();
//        }
//
//        hsss.tv_indexs.setText((position+1)+"");
//        if (position<=(mPPTIndex-1)){
//            if (position==(mPPTIndex-1)){
//                hsss.iv_select_item.setBackgroundResource(R.drawable.ic_selected_circle);
//
//            }else{
//                hsss.iv_select_item.setBackgroundResource(R.drawable.ic_circle_orange);
//            }
//
//        }else{
//            if (mDataList.getSlides().get(position).getQuestionsCompleted()==1){
//                hsss.iv_select_item.setBackgroundResource(R.drawable.ic_circle_orange);
//            }else{
//                hsss.iv_select_item.setBackgroundResource(R.drawable.ic_circle_gray);
//            }
//
//        }
//
//        return convertView;
//    }
//    class holder{
//        TextView tv_indexs;
//        SimpleDraweeView iv_select_show;
//        SimpleDraweeView iv_select_item;
//    }
//}

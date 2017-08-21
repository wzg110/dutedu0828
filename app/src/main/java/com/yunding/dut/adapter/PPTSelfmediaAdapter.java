package com.yunding.dut.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yunding.dut.R;
import com.yunding.dut.model.resp.ppt.pptSelfListResp;

import java.util.List;

/**
 * 类 名 称：PPTSelfmediaAdapter
 * <P/>描    述：自学媒体按钮adapter
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 10:09
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 10:09
 * <P/>修改备注：
 * <P/>版    本：
 */

public class PPTSelfmediaAdapter extends BaseAdapter{
    private List<pptSelfListResp.DataBean.SlidesBean.slideFilesBean> mDataList;
    private Context mContext;
    public PPTSelfmediaAdapter( List<pptSelfListResp.DataBean.SlidesBean.slideFilesBean> dataList, Context context){
        this.mDataList=dataList;
        this.mContext=context;
    }
    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder holderview;
        if (convertView==null){
            holderview=new holder();
            convertView=View.inflate(mContext, R.layout.list_item_media,null);
            holderview.rl_bg= (RelativeLayout) convertView.findViewById(R.id.rl_bg);
            holderview.iv= (ImageView) convertView.findViewById(R.id.iv_media_type);
            convertView.setTag(holderview);
        }else{
            holderview= (holder) convertView.getTag();
        }
//        1-视频 2-音频 3-动图
        if (mDataList.get(position).getFileType()==1){
            holderview.iv.setBackgroundResource(R.drawable.ic_vedio);
        }else if (mDataList.get(position).getFileType()==2){
            holderview.iv.setBackgroundResource(R.drawable.ic_record);
        }else{
            holderview.iv.setBackgroundResource(R.drawable.ic_gif);
        }
        return convertView;
    }
    class holder{
        RelativeLayout rl_bg;
        ImageView iv;

    }
}

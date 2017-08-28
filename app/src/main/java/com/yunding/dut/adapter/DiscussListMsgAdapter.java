package com.yunding.dut.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lqr.audio.AudioPlayManager;
import com.lqr.audio.IAudioPlayListener;
import com.yunding.dut.R;
import com.yunding.dut.app.DUTApplication;
import com.yunding.dut.model.resp.discuss.DiscussMsgListResp;
import com.yunding.dut.util.api.Apis;
import com.yunding.dut.util.file.FileUtil;
import com.yunding.dut.util.third.ConstUtils;
import com.yunding.dut.util.third.TimeUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.List;

import okhttp3.Call;
/**
 * 类 名 称：DiscussListMsgAdapter
 * <P/>描    述： 讨论组轮询消息显示adapter
 * <P/>创 建 人：CM
 * <P/>创建时间：2017/8/15 9:51
 * <P/>修 改 人：CM
 * <P/>修改时间：2017/8/15 9:51
 * <P/>修改备注：
 * <P/>版    本：
 */

public class DiscussListMsgAdapter extends RecyclerView.Adapter {

    private List<DiscussMsgListResp.DataBean.DatasBean> mData;
    private Context mContext;

    private final int ITEM_TYPE_FROM_TEXT = 0;
    private final int ITEM_TYPE_FROM_VOICE = 1;
    private final int ITEM_TYPE_FROM_IMAGE = 2;
    private final int ITEM_TYPE_TO_TEXT = 3;
    private final int ITEM_TYPE_TO_VOICE = 4;
    private final int ITEM_TYPE_TO_IMAGE = 5;

    private final int MSG_TYPE_TEXT = 0;
    private final int MSG_TYPE_VOICE = 1;
    private final int MSG_TYPE_IMAGE = 2;

    public DiscussListMsgAdapter(List<DiscussMsgListResp.DataBean.DatasBean> mData, Context context) {
        this.mData = mData;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case ITEM_TYPE_FROM_TEXT:
                view = LayoutInflater.from(DUTApplication.getInstance().getApplicationContext())
                        .inflate(R.layout.item_msg_discuss_from_text, parent, false);
                holder = new ChatFromTextHolder(view);
                break;
            case ITEM_TYPE_FROM_VOICE:
                view = LayoutInflater.from(DUTApplication.getInstance().getApplicationContext())
                        .inflate(R.layout.item_msg_discuss_from_voice, parent, false);
                holder = new ChatFromVoiceHolder(view);
                break;
            case ITEM_TYPE_TO_TEXT:
                view = LayoutInflater.from(DUTApplication.getInstance().getApplicationContext())
                        .inflate(R.layout.item_msg_discuss_to_text, parent, false);
                holder = new ChatToTextHolder(view);
                break;
            case ITEM_TYPE_TO_VOICE:
                view = LayoutInflater.from(DUTApplication.getInstance().getApplicationContext())
                        .inflate(R.layout.item_msg_discuss_to_voice, parent, false);
                holder = new ChatToVoiceHolder(view);
                break;
            default:
                holder = null;
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder == null) return;
        if (position >= mData.size()) return;
        if (mData.get(position) == null) return;
        final DiscussMsgListResp.DataBean.DatasBean dataBean = mData.get(position);
        if (holder instanceof ChatFromVoiceHolder) {
            ((ChatFromVoiceHolder) holder).tvTime.setText(dataBean.getPlatformTime());
            ((ChatFromVoiceHolder) holder).tvName.setText(dataBean.getStudentName());
            ((ChatFromVoiceHolder) holder).imgFromHead.setImageURI(Uri.parse(Apis.SERVER_URL + dataBean.getAvatarUrl()));
            ((ChatFromVoiceHolder) holder).tvFromContent.setText(dataBean.getMessageLength() + "s");
            ((ChatFromVoiceHolder) holder).tvFromContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadRecord(dataBean.getFileUrl());
                }
            });
            if (position > 0) {
                final DiscussMsgListResp.DataBean.DatasBean preDataBean = mData.get(position - 1);
                long diffSec = TimeUtils.getTimeSpan(dataBean.getCreateTime(), preDataBean.getCreateTime(), ConstUtils.TimeUnit.MIN);
                ((ChatFromVoiceHolder) holder).tvTime.setVisibility(diffSec > 1 ? View.VISIBLE : View.GONE);
            }
        } else if (holder instanceof ChatFromTextHolder) {
            ((ChatFromTextHolder) holder).tvTime.setText(dataBean.getPlatformTime());
            ((ChatFromTextHolder) holder).tvName.setText(dataBean.getStudentName());
            ((ChatFromTextHolder) holder).imgFromHead.setImageURI(Uri.parse(Apis.SERVER_URL + dataBean.getAvatarUrl()));
            ((ChatFromTextHolder) holder).tvFromContent.setText(dataBean.getContent());
            if (position > 0) {
                final DiscussMsgListResp.DataBean.DatasBean preDataBean = mData.get(position - 1);
                long diffSec = TimeUtils.getTimeSpan(dataBean.getCreateTime(), preDataBean.getCreateTime(), ConstUtils.TimeUnit.MIN);
                ((ChatFromTextHolder) holder).tvTime.setVisibility(diffSec > 1 ? View.VISIBLE : View.GONE);
            }
            //添加长按复制事件
            ((ChatFromTextHolder) holder).tvFromContent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showOper(dataBean);
                    return false;
                }
            });
        } else if (holder instanceof ChatToTextHolder) {
            ((ChatToTextHolder) holder).tvTime.setText(dataBean.getPlatformTime());
            ((ChatToTextHolder) holder).tvName.setText(dataBean.getStudentName());
            ((ChatToTextHolder) holder).imgToHead.setImageURI(Uri.parse(Apis.SERVER_URL + dataBean.getAvatarUrl()));
            ((ChatToTextHolder) holder).tvToContent.setText(dataBean.getContent());
            if (position > 0) {
                final DiscussMsgListResp.DataBean.DatasBean preDataBean = mData.get(position - 1);
                long diffSec = TimeUtils.getTimeSpan(dataBean.getCreateTime(), preDataBean.getCreateTime(), ConstUtils.TimeUnit.MIN);
                ((ChatToTextHolder) holder).tvTime.setVisibility(diffSec > 1 ? View.VISIBLE : View.GONE);
            }
            //添加长按复制事件
            ((ChatToTextHolder) holder).tvToContent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showOper(dataBean);
                    return false;
                }
            });
        } else if (holder instanceof ChatToVoiceHolder) {

            ((ChatToVoiceHolder) holder).tvTime.setText(dataBean.getPlatformTime());
            ((ChatToVoiceHolder) holder).tvName.setText(dataBean.getStudentName());
            ((ChatToVoiceHolder) holder).imgToHead.setImageURI(Uri.parse(Apis.SERVER_URL + dataBean.getAvatarUrl()));
            ((ChatToVoiceHolder) holder).tvToContent.setText(dataBean.getMessageLength() + "s");
            if (position > 0) {
                final DiscussMsgListResp.DataBean.DatasBean preDataBean = mData.get(position - 1);
                long diffSec = TimeUtils.getTimeSpan(dataBean.getCreateTime(), preDataBean.getCreateTime(), ConstUtils.TimeUnit.MIN);
                ((ChatToVoiceHolder) holder).tvTime.setVisibility(diffSec > 1 ? View.VISIBLE : View.GONE);
            }
            ((ChatToVoiceHolder) holder).tvToContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downloadRecord(dataBean.getFileUrl());
                }
            });
        } else {
            //T.B.D
        }
    }

    /**
     * 功能简述:显示操作对话框
     *
     * @param dataBean [消息对象]
     */
    private void showOper(final DiscussMsgListResp.DataBean.DatasBean dataBean) {
        new MaterialDialog.Builder(mContext).items("复制").itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                switch (position) {
                    case 0:
                        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("Label", dataBean.getContent());
                        cm.setPrimaryClip(clipData);
                        Toast.makeText(mContext, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }).show();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        DiscussMsgListResp.DataBean.DatasBean dataBean = mData.get(position);
        int type = dataBean.getMessageType();
        String  userId = DUTApplication.getUserInfo().getUserId();
        if (userId.equals(dataBean.getStudentId())) {
            //我发送的
            switch (type) {
                case MSG_TYPE_TEXT:
                    return ITEM_TYPE_TO_TEXT;
                case MSG_TYPE_VOICE:
                    return ITEM_TYPE_TO_VOICE;
                case MSG_TYPE_IMAGE:
                    return ITEM_TYPE_TO_IMAGE;
                default:
                    return ITEM_TYPE_TO_TEXT;
            }
        } else {
            //发给我的
            switch (type) {
                case MSG_TYPE_TEXT:
                    return ITEM_TYPE_FROM_TEXT;
                case MSG_TYPE_VOICE:
                    return ITEM_TYPE_FROM_VOICE;
                case MSG_TYPE_IMAGE:
                    return ITEM_TYPE_FROM_IMAGE;
                default:
                    return ITEM_TYPE_FROM_TEXT;
            }
        }
    }

    private class ChatFromTextHolder extends RecyclerView.ViewHolder {

        TextView tvTime;
        SimpleDraweeView imgFromHead;
        TextView tvName;
        TextView tvFromContent;

        ChatFromTextHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.chat_item_title_date);
            imgFromHead = (SimpleDraweeView) itemView.findViewById(R.id.img_head);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvFromContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    private class ChatToTextHolder extends RecyclerView.ViewHolder {

        TextView tvTime;
        SimpleDraweeView imgToHead;
        TextView tvName;
        TextView tvToContent;

        ChatToTextHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.chat_item_title_date);
            imgToHead = (SimpleDraweeView) itemView.findViewById(R.id.img_head);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvToContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    private class ChatFromVoiceHolder extends RecyclerView.ViewHolder {

        TextView tvTime;
        SimpleDraweeView imgFromHead;
        TextView tvName;
        TextView tvFromContent;

        ChatFromVoiceHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.chat_item_title_date);
            imgFromHead = (SimpleDraweeView) itemView.findViewById(R.id.img_head);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvFromContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    private class ChatToVoiceHolder extends RecyclerView.ViewHolder {

        TextView tvTime;
        SimpleDraweeView imgToHead;
        TextView tvName;
        TextView tvToContent;

        ChatToVoiceHolder(View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.chat_item_title_date);
            imgToHead = (SimpleDraweeView) itemView.findViewById(R.id.img_head);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvToContent = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    private void downloadRecord(String path) {
        OkHttpUtils.get().url(Apis.SERVER_URL + path).build()
                .execute(new FileCallBack(FileUtil.getDownloadVoiceDir(), System.currentTimeMillis() + FileUtil.getRecordSuffix()) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(getClass().toString(), e.toString());
                    }

                    @Override
                    public void onResponse(File file, int id) {
                        startPlay(file);
                    }
                });
    }

    private void startPlay(File file) {
        AudioPlayManager.getInstance().startPlay(mContext, Uri.fromFile(file), new IAudioPlayListener() {
            @Override
            public void onStart(Uri var1) {
                //开播（一般是开始语音消息动画）
            }

            @Override
            public void onStop(Uri var1) {
                //停播（一般是停止语音消息动画）
            }

            @Override
            public void onComplete(Uri var1) {
                //播完（一般是停止语音消息动画）
            }
        });
    }
}

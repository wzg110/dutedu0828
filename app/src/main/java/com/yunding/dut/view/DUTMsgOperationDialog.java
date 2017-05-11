package com.yunding.dut.view;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.yunding.dut.R;
import com.yunding.dut.model.resp.discuss.DiscussMsgListResp;
import com.yunding.dut.util.third.ScreenUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 功能描述：长按消息，弹出的操作对话框
 */

public class DUTMsgOperationDialog extends DialogFragment {

    private Context mContext;

    private DiscussMsgListResp.DataBean.DatasBean message;

    private OnMessageItemDelete onMessageItemDelete;

    public interface OnMessageItemDelete {
        void delete();
    }

    public DUTMsgOperationDialog setOnMessageItemDelete(OnMessageItemDelete onMessageItemDelete) {
        this.onMessageItemDelete = onMessageItemDelete;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_chat_operation, container);
        ButterKnife.bind(this, view);
        return view;
    }

    public DUTMsgOperationDialog setMessage(DiscussMsgListResp.DataBean.DatasBean message) {
        this.message = message;
        return this;
    }

    public DUTMsgOperationDialog show(FragmentManager manager, Context context) {
        mContext = context;
        show(manager, "DUTMsgOperationDialog");
        return this;
    }

    @OnClick({R.id.tvCopy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCopy:
                ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Label", message.getContent());
                cm.setPrimaryClip(clipData);
                Toast.makeText(mContext, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
                break;
        }
        dismiss();
    }
}

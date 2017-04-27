package com.yunding.dut.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ContextMenu;


/**
 * 类 名 称：DUTSelectedTextView
 * <P/>描    述：自定义文本选择操作对话框
 * <P/>创 建 人：msy
 * <P/>创建时间：2017/4/26 10:10
 * <P/>修 改 人：msy
 * <P/>修改时间：2017/4/26 10:10
 * <P/>修改备注：
 * <P/>版    本：1.0
 */
public class DUTSelectedTextView extends android.support.v7.widget.AppCompatTextView{
    public DUTSelectedTextView(Context context) {
        super(context);
    }

    public DUTSelectedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DUTSelectedTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
    }
}

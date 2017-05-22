package com.yunding.dut.view.selectable;

/**
 * Created by Jaeger on 16/8/30.
 *
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
public interface OnSelectListener {
//    void onTextSelected(CharSequence content);
    void onTextSelected(CharSequence content,int startIndex,int endIndex);
    void onTextTranslate(CharSequence content);
    void onTextCollect(CharSequence content);
}

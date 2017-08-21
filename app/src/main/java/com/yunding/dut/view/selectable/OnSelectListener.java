package com.yunding.dut.view.selectable;

/**
 * Created by Jaeger on 16/8/30.
 *
 * Email: chjie.jaeger@gmail.com
 * GitHub: https://github.com/laobie
 */
public interface OnSelectListener {
    void onTextSelected(CharSequence content,int startIndex,int endIndex,String  color);
    void onTextTranslate(CharSequence content,float [] position);
    void onTextCollect(CharSequence content,String id);
    void onTextNote(CharSequence content,int postion);
    void onTextDelete(String position);
    void onChangeColor(int position,int color,String id,String colorid);
    void onTextClick(int x,int y);

}

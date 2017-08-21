package com.yunding.dut.util;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FontsUtils {
    public static boolean isContainChinese(String str) {


        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    private static Typeface cmtTypeFace;
    /**
     * 非中文内容使用此字体样式（设计需求）设置字体样式
     */
    public static void setCMTFonts(Context context, TextView textView) {
        textView.setTypeface(getCMTTypeface(context));// 设置字体样式
    }
    public static void setCMTFonts(Context context, List<TextView> textView){
        for (int i=0;i<textView.size();i++){
            textView.get(i).setTypeface(getCMTTypeface(context));
        }
    }

    /**
     * 非中文内容使用此字体样式（设计需求） 获取Typeface
     */
    public static Typeface getCMTTypeface(Context context) {
        if (cmtTypeFace == null) {
            cmtTypeFace = Typeface.createFromAsset(context.getAssets(),
                    "fonts/hsbw.ttf");// 根据路径得到Typeface
        }
        return cmtTypeFace;
    }

    /**
     * 设置全局字体样式
     *
     * @param context 上下文
     */
    public static void setAppTypeface(Context context) {
        try {
            Field field = Typeface.class.getDeclaredField("SERIF");
            field.setAccessible(true);
            field.set(null, getCMTTypeface(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeFonts(ViewGroup root, Activity act) {

        Typeface tf = Typeface.createFromAsset(act.getAssets(),
                "fonts/zh_cn.ttf");

        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(tf);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(tf);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(tf);
            } else if (v instanceof ViewGroup) {
                changeFonts((ViewGroup) v, act);
            }
        }

    }
    public static ViewGroup getRootView(Activity context)
    {
        return ((ViewGroup)context.findViewById(android.R.id.content));
    }
}

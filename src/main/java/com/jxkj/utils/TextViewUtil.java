package com.jxkj.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import java.util.List;

/**
 * Created by DY on 2019/10/30.
 */

public class TextViewUtil {

    /**
     * 设置字符串中某一关键字的颜色 （无点击事件）
     *
     * @param content 目标字符串
     * @param keyStr  关键字
     * @param color   关键字颜色
     * @return
     */
    public static SpannableString setSpanColorStr(String content, String keyStr, final int color) {
        SpannableString spannableString = new SpannableString(content);
        if (content.contains(keyStr)) {
            int startNew = 0;
            int startOld = 0;
            String temp = content;
            while (temp.contains(keyStr)) {
                spannableString.setSpan(
                        new ClickableSpan() {
                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setColor(color);
                                ds.setUnderlineText(false);
                            }

                            @Override
                            public void onClick(View widget) {
                            }
                        }, startOld + temp.indexOf(keyStr),
                        startOld + temp.indexOf(keyStr) + keyStr.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                startNew = temp.indexOf(keyStr) + keyStr.length();
                startOld += startNew;
                temp = temp.substring(startNew);
            }
        }
        return spannableString;
    }

    /**
     * 设置字符串中多个不同关键字的颜色（颜色统一, 无点击事件）
     *
     * @param content 目标字符串
     * @param keyStrs 关键字集合
     * @param color   单一的颜色值
     * @return
     */
    public static SpannableString setSpanColorStr(String content, List<String> keyStrs, final int color) {
        SpannableString spannableString = new SpannableString(content);
        for (int i = 0; i < keyStrs.size(); i++) {
            String keyStr = keyStrs.get(i);
            if (content.contains(keyStr)) {
                int startNew = 0;
                int startOld = 0;
                String temp = content;
                while (temp.contains(keyStr)) {
                    spannableString.setSpan(
                            new ClickableSpan() {
                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    super.updateDrawState(ds);
                                    ds.setColor(color);
                                    ds.setUnderlineText(false);
                                }

                                @Override
                                public void onClick(View widget) {
                                }
                            }, startOld + temp.indexOf(keyStr),
                            startOld + temp.indexOf(keyStr) + keyStr.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    startNew = temp.indexOf(keyStr) + keyStr.length();
                    startOld += startNew;
                    temp = temp.substring(startNew);
                }
            }
        }
        return spannableString;
    }

    /**
     * 设置字符串中多个关键字的不同颜色（颜色与关键字一一对应, 无点击事件）
     *
     * @param content 目标字符串
     * @param keyStrs 关键字集合
     * @param colors  颜色值的集合
     * @return
     */
    public static SpannableString setSpanColorStr(String content, List<String> keyStrs, final List<Integer> colors) {
        SpannableString spannableString = new SpannableString(content);
        for (int i = 0; i < keyStrs.size(); i++) {
            String keyStr = keyStrs.get(i);
            if (content.contains(keyStr)) {
                int startNew = 0;
                int startOld = 0;
                String temp = content;
                while (temp.contains(keyStr)) {
                    final int finalI = i;
                    spannableString.setSpan(
                            new ClickableSpan() {
                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    super.updateDrawState(ds);
                                    ds.setColor(colors.get(finalI));
                                    ds.setUnderlineText(false);
                                }

                                @Override
                                public void onClick(View widget) {
                                }
                            }, startOld + temp.indexOf(keyStr),
                            startOld + temp.indexOf(keyStr) + keyStr.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    startNew = temp.indexOf(keyStr) + keyStr.length();
                    startOld += startNew;
                    temp = temp.substring(startNew);
                }
            }
        }
        return spannableString;
    }
}

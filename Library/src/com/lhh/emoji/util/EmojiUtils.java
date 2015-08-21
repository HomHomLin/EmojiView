package com.lhh.emoji.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by linhonghong on 2015/8/21.
 */
public class EmojiUtils {
    public static float getRawSize(int unit, float size, Context context) {
        Resources r;
        r = context.getResources();
        return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
    }

    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static int parseInt(String str) {
        int res = 0;
        if (str == null || str.equals(""))
            return res;
        try {
            res = Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            res = 0;
        }
        return res;
    }
}

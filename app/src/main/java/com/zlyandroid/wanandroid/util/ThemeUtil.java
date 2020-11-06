package com.zlyandroid.wanandroid.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.TypedValue;


import com.zlyandroid.wanandroid.app.ProApplication;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yangl on 2017/5/25.
 * 主题相关工具类
 */

public class ThemeUtil {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static int getThemeColor(Context context, int attr) {
        //attr R.attr.colorAccent
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[]{attr});
        int color = a.getColor(0, 0);
        a.recycle();

        return color;
    }




    public static boolean isAutoDarkMode() {
        SharedPreferences sp = mContext.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getBoolean(AUTO_DARK_MODE_KEY, true);
    }
    public static int getCustomTheme() {
        SharedPreferences sp = mContext.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        return sp.getInt(THEME_KEY, 0);
    }

    public static void setCustomTheme(int theme) {
        SharedPreferences sp = mContext.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(THEME_KEY, theme);
        editor.apply();
    }

    public static void setDarkModeTime(boolean isStart, int value) {
        SharedPreferences sp = mContext.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (isStart) {
            editor.putInt(START_DARK_TIME_KEY, value);
        } else {
            editor.putInt(END_DARK_TIME_KEY, value);
        }
        editor.apply();
    }


    public static int[] getDarkModeTime() {
        SharedPreferences sp = mContext.getSharedPreferences(MY_SP_NAME, MODE_PRIVATE);
        int[] ret = new int[2];
        ret[0] = sp.getInt(START_DARK_TIME_KEY, 21);
        ret[1] = sp.getInt(END_DARK_TIME_KEY, 6);
        return ret;
    }

    public static final String MY_SP_NAME = "ZLYHome";
    public static final String THEME_KEY = "themeHome";
    public static final String AUTO_DARK_MODE_KEY = "auto_dark_mode";
    public static final String START_DARK_TIME_KEY = "start_dart_time";
    public static final String END_DARK_TIME_KEY = "end_dark_time";
}

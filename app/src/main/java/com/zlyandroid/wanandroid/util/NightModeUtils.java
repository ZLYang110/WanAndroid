package com.zlyandroid.wanandroid.util;

import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * @author zhangliyang
 * @date 2020/2/17
 *  暗黑模式
 */
public class NightModeUtils {
    public static boolean isNightMode(Configuration config) {
        int uiMode = config.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return uiMode == Configuration.UI_MODE_NIGHT_YES;
    }

    public static boolean isNightMode(Context context) {
        return isNightMode(context.getResources().getConfiguration());
    }

    public static void initNightMode() {
        if (SettingUtils.getInstance().isSystemTheme()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        } else {
            if (SettingUtils.getInstance().isDarkTheme()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
    }
}

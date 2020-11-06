package com.zlyandroid.wanandroid.app;

import android.content.Context;
import android.os.Handler;

import com.zlyandroid.wanandroid.https.WanCache;
import com.zlyandroid.wanandroid.ui.home.activity.ScanActivity;
import com.zlyandroid.wanandroid.ui.main.MainActivity;
import com.zlyandroid.wanandroid.util.LogUtil;
import com.zlyandroid.wanandroid.util.PreUtils;
import com.zlyandroid.wanandroid.util.ResUtils;
import com.zlyandroid.wanandroid.util.ThemeUtil;
import com.zlyandroid.wanandroid.util.display.DisplayInfoUtils;

import cat.ereza.customactivityoncrash.config.CaocConfig;
import per.goweii.burred.Blurred;

/**
 * author: zhangliyang
 * date: 2018/3/13
 */

public class AppConfig {

    public static Handler sHandler = new Handler();

    static void init(Context context){

        //初始化缓存
        PreUtils.init(context);
        //主题
        ThemeUtil.init(context);
        //高斯模糊
        Blurred.init(context);
        //屏幕资源
        DisplayInfoUtils.init(context);
        //获取资源文件的工具类
        ResUtils.init(context);
        //日志
        LogUtil.init("wanandroidaLog",true);
        //缓存
        WanCache.init();

        //崩溃检测
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
                .enabled(false) //default: true
                .showErrorDetails(false) //default: true
                .showRestartButton(false) //default: true
                .logErrorOnRestart(false) //default: true
                .trackActivities(true) //default: false
                .minTimeBetweenCrashesMs(2000) //default: 3000
                .restartActivity(MainActivity.class) //default: null (your app's launch activity)
                .errorActivity(ScanActivity.class) //default: null (default error activity)
                .apply();
    }




}

package com.zlyandroid.wanandroid.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.zlyandroid.wanandroid.app.AppContext;
import com.zlylib.upperdialog.utils.Utils;


public class AppInfoUtils {

    /**
     * 获取本地软件版本号code
     */
    public static int getVersionCode() {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = AppContext.getContext() .getPackageManager().getPackageInfo( AppContext.getContext().getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本号name
     */
    public static String getVersionName() {
        String versionName = "";
        try {
            PackageInfo packageInfo =  AppContext.getContext().getApplicationContext().getPackageManager().getPackageInfo(AppContext.getContext().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 判断应用是否已安装
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        return getAppInfo(context, packageName) != null;
    }

    public static ApplicationInfo getAppInfo(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            return context.getPackageManager().getApplicationInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static class PackageName {
        public static final String QQ = "com.tencent.mobileqq";
        public static final String WECHAT = "com.tencent.mm";
    }
}

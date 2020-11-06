package com.zlyandroid.wanandroid.app;

import android.content.Context;

/**
 * 上下文Context
 * author: 康栋普
 * date: 2018/3/13
 */

public class AppContext {
    private static Context mContext;
    private static AppContext mInstance;


    private AppContext(Context mCon) {
        mContext = mCon;
    }

    public static Context getContext() {
        if (mContext == null) {
            throw new RuntimeException("mContext未在Application中初始化");
        }
        return mContext;
    }

    public static AppContext getInstance() {
        if (mInstance == null) {
            throw new RuntimeException("AppContext未在Application中初始化");
        }
        return mInstance;
    }

    static void initialize(Context context) {
        if (mInstance == null) {
            synchronized (AppContext.class) {
                if (mInstance == null) {
                    mInstance = new AppContext(context.getApplicationContext());
                    AppConfig.init(mContext);
                }
            }
        }
    }
}

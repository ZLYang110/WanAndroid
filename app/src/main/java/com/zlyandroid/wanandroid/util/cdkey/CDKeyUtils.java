package com.zlyandroid.wanandroid.util.cdkey;

import androidx.annotation.NonNull;

import com.zlyandroid.wanandroid.BuildConfig;
import com.zlyandroid.wanandroid.util.PreUtils;


/**
 * @author zhangliyang
 * @date 2020/5/8
 */
public class CDKeyUtils {

    private static final String SP_NAME = "cdkey";
    private static final String KEY = "cdkey";

    private static boolean isActivated = false;

    private static class Holder {
        private static final CDKeyUtils sInstance = new CDKeyUtils();
    }

    public static CDKeyUtils getInstance() {
        return Holder.sInstance;
    }

    private CDKeyUtils() {
        String cdKey = PreUtils.newInstance(SP_NAME).get(KEY, "");
     //   isActivated = isActiveCDKey(UserUtils.getInstance().getUserId() + "", cdKey);
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivatedCDKey(String cdKey) {
      //  isActivated = isActiveCDKey(UserUtils.getInstance().getUserId() + "", cdKey);
        PreUtils.newInstance(SP_NAME).save(KEY, cdKey);
    }



}

package com.zlyandroid.wanandroid.util;


/**
 * @author zhangliyang
 * @date 2019/5/18
 */
public class UserInfoUtils {

    private static final String SP_NAME = "wanandroid_user_info";
    private static final String KEY_ICON = "KEY_ICON";
    private static final String KEY_BG = "KEY_BG";

    private final PreUtils mSPUtils = PreUtils.newInstance(SP_NAME);

    private static class Holder {
        private static final UserInfoUtils INSTANCE = new UserInfoUtils();
    }

    public static UserInfoUtils getInstance() {
        return Holder.INSTANCE;
    }

    private UserInfoUtils() {
    }

    public void setIcon(String icon) {
        mSPUtils.save(KEY_ICON, icon);
    }

    public String getIcon() {
        return mSPUtils.get(KEY_ICON, "");
    }

    public void setBg(String icon) {
        mSPUtils.save(KEY_BG, icon);
    }

    public String getBg() {
        return mSPUtils.get(KEY_BG, "");
    }
}

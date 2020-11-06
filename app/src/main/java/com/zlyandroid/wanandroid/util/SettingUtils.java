package com.zlyandroid.wanandroid.util;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;



/**
 * @author zhangliyang
 * @date 2019/5/18
 * GitHub: https://github.com/ZLYang110
 */
public class SettingUtils {

    private static final String SP_NAME = "setting";
    private static final String KEY_SYSTEM_THEME = "KEY_SYSTEM_THEME";
    private static final String KEY_DARK_THEME = "KEY_DARK_THEME";
    private static final String KEY_SHOW_READ_LATER = "KEY_SHOW_READ_LATER";
    private static final String KEY_SHOW_READ_RECORD = "KEY_SHOW_READ_RECORD";
    private static final String KEY_SHOW_BANNER = "KEY_SHOW_BANNER";
    private static final String KEY_SHOW_TOP = "KEY_SHOW_TOP";
    private static final String KEY_HIDE_OPEN = "KEY_HIDE_OPEN";
    private static final String KEY_HIDE_ABOUT_ME = "KEY_HIDE_ABOUT_ME";
    private static final String KEY_RV_ANIM = "KEY_RV_ANIM";
    private static final String KEY_SEARCH_HISTORY_MAX_COUNT = "KEY_SEARCH_HISTORY_MAX_COUNT";//历史记录最大数量


    private final PreUtils mSPUtils = PreUtils.newInstance(SP_NAME);

    private boolean mSystemTheme = true;//是否跟随系统
    private boolean mDarkTheme = false;//是否暗黑模式
    private boolean mShowReadLater = true;//稍后阅读
    private boolean mShowReadRecord = true;//阅读历史
    private boolean mShowBanner = true;//是否显示轮番
    private boolean mShowTop = true;//是否显示置顶
    private boolean mHideOpen = false;//是否显示开源项目
    private boolean mHideAboutMe = false;//是否关于作者

    private int mRvAnim = RvAnimUtils.RvAnim.SLIDEIN_BOTTOM;
    private int mSearchHistoryMaxCount = 100;
    private long mUpdateIgnoreDuration = 1 * 24 * 60 * 60 * 1000L;

    private static class Holder {
        private static final SettingUtils INSTANCE = new SettingUtils();
    }

    public static SettingUtils getInstance() {
        return Holder.INSTANCE;
    }

    private SettingUtils() {
        mSystemTheme = mSPUtils.get(KEY_SYSTEM_THEME, mSystemTheme);
        mDarkTheme = mSPUtils.get(KEY_DARK_THEME, mDarkTheme);
        mShowReadLater = mSPUtils.get(KEY_SHOW_READ_LATER, mShowReadLater);
        mShowReadRecord = mSPUtils.get(KEY_DARK_THEME, mShowReadRecord);
        mShowBanner = mSPUtils.get(KEY_SHOW_BANNER, mShowBanner);
        mShowTop = mSPUtils.get(KEY_SHOW_TOP, mShowTop);
        mHideAboutMe = mSPUtils.get(KEY_HIDE_ABOUT_ME, mHideAboutMe);
        mRvAnim = mSPUtils.get(KEY_RV_ANIM, mRvAnim);
        mHideOpen = mSPUtils.get(KEY_HIDE_OPEN, mHideOpen);
        mSearchHistoryMaxCount = mSPUtils.get(KEY_SEARCH_HISTORY_MAX_COUNT, mSearchHistoryMaxCount);
    }

    public void setSystemTheme(boolean systemTheme) {
        mSystemTheme = systemTheme;
        mSPUtils.save(KEY_SYSTEM_THEME, systemTheme);
    }

    public boolean isSystemTheme() {
        return mSystemTheme;
    }

    public void setDarkTheme(boolean darkTheme) {
        mDarkTheme = darkTheme;
        mSPUtils.save(KEY_DARK_THEME, darkTheme);
    }

    public boolean isDarkTheme() {
        return mDarkTheme;
    }
    public void setShowReadLater(boolean showReadLater) {
        mShowReadLater = showReadLater;
        mSPUtils.save(KEY_SHOW_READ_LATER, showReadLater);
    }
    public boolean isShowReadLater() {
        return mShowReadLater;
    }

    public boolean isShowReadRecord() {
        return mShowReadRecord;
    }

    public void setShowReadRecord(boolean showReadRecord) {
        mShowReadRecord = showReadRecord;
        mSPUtils.save(KEY_SHOW_READ_RECORD, showReadRecord);
    }
    public void setShowTop(boolean showTop) {
        mShowTop = showTop;
        mSPUtils.save(KEY_SHOW_TOP, showTop);
    }

    public boolean isShowTop() {
        return mShowTop;
    }

    public void setShowBanner(boolean showBanner) {
        mShowBanner = showBanner;
        mSPUtils.save(KEY_SHOW_BANNER, showBanner);
    }

    public boolean isShowBanner() {
        return mShowBanner;
    }

    public void setHideAboutMe(boolean hideAboutMe) {
        mHideAboutMe = hideAboutMe;
        mSPUtils.save(KEY_HIDE_ABOUT_ME, hideAboutMe);
    }
    public void setHideOpen(boolean hideOpen) {
        mHideOpen = hideOpen;
        mSPUtils.save(KEY_HIDE_OPEN, hideOpen);
    }

    public boolean isHideOpen() {
        return mHideOpen;
    }
    public boolean isHideAboutMe() {
        return mHideAboutMe;
    }

    public void setRvAnim(int anim) {
        mRvAnim = anim;
        mSPUtils.save(KEY_RV_ANIM, anim);
    }

    public int getRvAnim() {
        return mRvAnim;
    }
    public void setSearchHistoryMaxCount(int count) {
        mSearchHistoryMaxCount = count;
        mSPUtils.save(KEY_SEARCH_HISTORY_MAX_COUNT, count);
    }

    public int getSearchHistoryMaxCount() {
        return mSearchHistoryMaxCount;
    }
    public long getUpdateIgnoreDuration() {
        return mUpdateIgnoreDuration;
    }
}

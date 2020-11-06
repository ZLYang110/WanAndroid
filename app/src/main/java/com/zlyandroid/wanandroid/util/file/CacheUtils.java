package com.zlyandroid.wanandroid.util.file;

import com.zlyandroid.wanandroid.app.AppContext;

import java.io.File;


/**
 * 缓存辅助类
 * @author zhangliyang
 * @date 18/4/23
 */
public class CacheUtils {

    /**
     * 获取系统默认缓存文件夹
     * 优先返回SD卡中的缓存文件夹
     */
    public static String getCacheDir() {
        File cacheFile = null;
        if (FileUtils.isSDCardAlive()) {
            cacheFile =  AppContext.getContext().getExternalCacheDir();
        }
        if (cacheFile == null) {
            cacheFile =  AppContext.getContext().getCacheDir();
        }
        return cacheFile.getAbsolutePath();
    }

    /**
     * 获取系统默认缓存文件夹内的缓存大小
     */
    public static String getTotalCacheSize() {
        long cacheSize = FileUtils.getSize( AppContext.getContext().getCacheDir());
        if (FileUtils.isSDCardAlive()) {
            cacheSize += FileUtils.getSize( AppContext.getContext().getExternalCacheDir());
        }
        return FileUtils.formatSize(cacheSize);
    }

    /**
     * 清除系统默认缓存文件夹内的缓存
     */
    public static void clearAllCache() {
        FileUtils.delete( AppContext.getContext().getCacheDir());
        if (FileUtils.isSDCardAlive()) {
            FileUtils.delete( AppContext.getContext().getExternalCacheDir());
        }
    }

}
package com.zlyandroid.wanandroid.util.cdkey;

import androidx.annotation.NonNull;

/**
 * @author zhangliyang
 * @date 2020/5/8
 */
public interface CDKey {
    @NonNull
    String createCDKey(@NonNull String userId);

    boolean active(@NonNull String userId, @NonNull String cdkey);
}

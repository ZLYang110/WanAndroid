package com.zlyandroid.wanandroid.listener;

/**
 * @author zhangliyang
 * @date 2019/5/12
 * GitHub: https://github.com/ZLYang110
 */
public interface CacheListener<E> {
    void onSuccess(int code, E data);
    void onFailed();
}

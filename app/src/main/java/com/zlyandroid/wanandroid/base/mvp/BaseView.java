package com.zlyandroid.wanandroid.base.mvp;


import com.trello.rxlifecycle3.LifecycleTransformer;

/**
 * @author zhangliyang
 * @date 2018/4/24.
 * Description：
 */
public interface BaseView {

    /**
     * 显示加载中
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();


   /* *//**
     * 绑定Android生命周期 防止RxJava内存泄漏
     *
     * @param <T>
     * @return
     *//*
    <T> AutoDisposeConverter<T> bindAutoDispose();*/

    /**
     * 绑定生命周期
     */
    <T> LifecycleTransformer<T> bindToLife();
}

package com.zlyandroid.wanandroid.ui.core;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;

/**
 * 通用业务接口
 * author: zhangliyang
 * date: 2018/2/26
 */

public interface ICommonModel {

    /**
     * 收藏文章
     *
     * @param id 文章id
     * @param callback
     */
    void collectArticle(int id, LifecycleTransformer bindToLife, BaseObserver<String> callback);


    /**
     * 取消收藏文章
     *
     * @param id 文章id
     * @param callback
     */
    void unCollectArticle(int id,LifecycleTransformer bindToLife, BaseObserver<String> callback);

}

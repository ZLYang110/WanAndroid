package com.zlyandroid.wanandroid.ui.core.model;


import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.WanCache;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.callback.ResponseBean;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.listener.CacheListener;
import com.zlyandroid.wanandroid.ui.core.bean.BaseBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.CollectionLinkBean;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRecordBean;
import com.zlyandroid.wanandroid.util.LogUtil;

import java.util.List;

import io.reactivex.Observable;

/**
 * @author zhangliyang
 * @date 2019/5/15
 * GitHub:  https://github.com/ZLYang110
 */
public class MainModel {

    /**
     * 收藏站内文章
     * */
    public static void collectArticle(@IntRange(from = 0) int id,LifecycleTransformer bindToLife, BaseObserver<BaseBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().collectArticle(id).compose(bindToLife),callback);
    }
    /**
     * 收藏站内文章
     * */
    public static void collectArticle(String title, String author, String link,LifecycleTransformer bindToLife, BaseObserver<ArticleBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().collectArticle(title, author, link).compose(bindToLife),callback);
    }
    /**
     * 收藏网址
     * */
    public static void collectLink(String title, String link,LifecycleTransformer bindToLife,@NonNull BaseObserver<CollectionLinkBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().collectLink(title, link).compose(bindToLife),callback);
    }
    /**
     * 删除收藏网站
     * */
    public static void uncollectLink(@IntRange(from = 0) int id,LifecycleTransformer bindToLife,@NonNull BaseObserver<BaseBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().uncollectLink(id).compose(bindToLife),callback);
    }
    /**
     * 取消收藏
     * */
    public static void uncollectArticle(@IntRange(from = 0) int id,LifecycleTransformer bindToLife, BaseObserver<BaseBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().uncollectArticle(id).compose(bindToLife),callback);
    }
    /**
     * 取消收藏 我的收藏页面（该页面包含自己录入的内容）
     * */
    public static void uncollectArticle(@IntRange(from = 0) int id, int originId,LifecycleTransformer bindToLife, BaseObserver<BaseBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().uncollectArticle(id,originId).compose(bindToLife),callback);
    }

    /**
     * 收藏文章列表
     * */
    public static void getCollectArticleList(@IntRange(from = 0) int page, LifecycleTransformer bindToLife, BaseObserver<ArticleListBean> callback) {
        BaseRequest.requstCacheBean(
                WanCache.CacheKey.COLLECT_ARTICLE_LIST(page),
                ArticleListBean.class,
                RHttp.getInstance().getApi().getCollectArticleList(page).compose(bindToLife),
                callback);
    }

    public static void getCollectArticleListCache(@IntRange(from = 0) int page, LifecycleTransformer bindToLife, @NonNull BaseObserver<ArticleListBean> listener) {
        BaseRequest.cacheBean(
                WanCache.CacheKey.COLLECT_ARTICLE_LIST(page),
                ArticleListBean.class,
                RHttp.getInstance().getApi().getCollectArticleList(page).compose(bindToLife),
                listener);
    }


    /**
     * 收藏网站列表
     * */
    public static void getCollectLinkList( LifecycleTransformer bindToLife, BaseObserver<List<CollectionLinkBean>> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().getCollectLinkList().compose(bindToLife),callback);

    }





}

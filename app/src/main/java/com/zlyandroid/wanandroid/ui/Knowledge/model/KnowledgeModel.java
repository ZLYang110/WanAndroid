package com.zlyandroid.wanandroid.ui.Knowledge.model;

import androidx.annotation.IntRange;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.WanCache;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.listener.CacheListener;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.ChapterBean;
import com.zlyandroid.wanandroid.ui.Knowledge.contract.KnowledgeContract;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.contract.HomeContract;
import com.zlyandroid.wanandroid.util.LogUtil;

import java.util.List;

public class KnowledgeModel implements KnowledgeContract.Model  {
    @Override
    public void getKnowledgeListCache(LifecycleTransformer bindToLife, BaseObserver<List<ChapterBean>> callback) {
        BaseRequest.cacheList(
                WanCache.CacheKey.KNOWLEDGE_LIST,
                ChapterBean.class,
                RHttp.getInstance().getApi().getKnowledgeList().compose(bindToLife),
                callback);

    }

    @Override
    public void getKnowledgeList(LifecycleTransformer bindToLife, BaseObserver<List<ChapterBean>> callback) {

        BaseRequest.requstCacheList(
                WanCache.CacheKey.KNOWLEDGE_LIST,
                ChapterBean.class,
                RHttp.getInstance().getApi().getKnowledgeList().compose(bindToLife),
                callback);
    }

    @Override
    public void getKnowledgeListCacheAndNet(LifecycleTransformer bindToLife, BaseObserver<List<ChapterBean>> callback) {

        BaseRequest.requst(RHttp.getInstance().getApi().getKnowledgeList().compose(bindToLife),callback);
    }

    @Override
    public void getKnowledgeArticleListCache(LifecycleTransformer bindToLife, int id, @IntRange(from = 0) int page, BaseObserver<ArticleListBean> callback) {

        BaseRequest.requst(RHttp.getInstance().getApi().getKnowledgeArticleList(page, id).compose(bindToLife),callback);
    }

    @Override
    public void getKnowledgeArticleList(LifecycleTransformer bindToLife, int id, @IntRange(from = 0) int page, BaseObserver<ArticleListBean> callback) {

        BaseRequest.requst(RHttp.getInstance().getApi().getKnowledgeArticleList(page, id).compose(bindToLife),callback);
    }

}

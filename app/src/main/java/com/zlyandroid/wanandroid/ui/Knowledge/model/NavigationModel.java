package com.zlyandroid.wanandroid.ui.Knowledge.model;

import androidx.annotation.IntRange;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.WanCache;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.ChapterBean;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.NavigationBean;
import com.zlyandroid.wanandroid.ui.Knowledge.contract.KnowledgeContract;
import com.zlyandroid.wanandroid.ui.Knowledge.contract.NavigationContract;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;

import java.util.List;

public class NavigationModel implements NavigationContract.Model  {

    @Override
    public void getNaviListCache(LifecycleTransformer bindToLife, BaseObserver<List<NavigationBean>> callback) {
        BaseRequest.cacheList(
                WanCache.CacheKey.NAVI_LIST,
                NavigationBean.class,
                RHttp.getInstance().getApi().getNaviList().compose(bindToLife),
                callback);
    }

    @Override
    public void getNaviList(LifecycleTransformer bindToLife, BaseObserver<List<NavigationBean>> callback) {
        BaseRequest.requstCacheList(
                WanCache.CacheKey.NAVI_LIST,
                NavigationBean.class,
                RHttp.getInstance().getApi().getNaviList().compose(bindToLife),
                callback);
    }
}

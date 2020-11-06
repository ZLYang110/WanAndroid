package com.zlyandroid.wanandroid.ui.Knowledge.contract;

import androidx.annotation.IntRange;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.ChapterBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.BannerBean;

import java.util.List;

public interface KnowledgeContract {

    interface Model {


        void getKnowledgeListCache(LifecycleTransformer bindToLife, BaseObserver<List<ChapterBean>> callback);

        void getKnowledgeList(LifecycleTransformer bindToLife, BaseObserver<List<ChapterBean>> callback);

        void getKnowledgeListCacheAndNet(LifecycleTransformer bindToLife, BaseObserver<List<ChapterBean>> callback);

        void getKnowledgeArticleListCache(LifecycleTransformer bindToLife, int id, @IntRange(from = 0) int page, BaseObserver<ArticleListBean> callback);

        void getKnowledgeArticleList(LifecycleTransformer bindToLife,int id, @IntRange(from = 0) int page, BaseObserver<ArticleListBean> callback);
    }

    interface Presenter {

        void getKnowledgeListCache();

        void getKnowledgeList();

        void getKnowledgeListCacheAndNet();

        void getKnowledgeArticleListCache(int id,int page);

        void getKnowledgeArticleList(int id,int page);
    }
}

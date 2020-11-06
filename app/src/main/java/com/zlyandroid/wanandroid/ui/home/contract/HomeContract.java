package com.zlyandroid.wanandroid.ui.home.contract;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.BannerBean;

import java.util.List;

public interface HomeContract {

    interface Model {

        /**
         * 获取首页轮训图片
         * */
        void getBanner(LifecycleTransformer bindToLife, BaseObserver<List<BannerBean>> callback);

        /**
         * 获取首页置顶
         * */
        void getTopArticleList(LifecycleTransformer bindToLife, BaseObserver<List<ArticleBean>> callback);
        /**
         * 获取首页列表数据
         * */
        void getArticleList(int page, LifecycleTransformer bindToLife, BaseObserver<ArticleListBean> callback);
    }

    interface Presenter {

        void getBanner();

        void getTopArticleList();
        void getArticleList(int page);
    }
}

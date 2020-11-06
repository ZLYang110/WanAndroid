package com.zlyandroid.wanandroid.ui.home.model;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.BannerBean;
import com.zlyandroid.wanandroid.ui.home.contract.HomeContract;

import java.util.List;

public class HomeModel implements HomeContract.Model {


    /**
    * 获取首页轮训图片
    * */
    @Override
    public void getBanner(LifecycleTransformer bindToLife, BaseObserver<List<BannerBean>> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().getBanner().compose(bindToLife),callback);
    }
    /**
     * 获取首页列表数据
     * */
    @Override
    public void getTopArticleList(LifecycleTransformer bindToLife, BaseObserver<List<ArticleBean>> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().getTopArticleList().compose(bindToLife),callback);
    }
    /**
     * 获取首页列表数据
     * */
    @Override
    public void getArticleList(int page,LifecycleTransformer bindToLife, BaseObserver<ArticleListBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().getArticleList(page) .compose(bindToLife),callback);
    }
}

package com.zlyandroid.wanandroid.ui.web;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.ui.core.ICommonModel;
import com.zlyandroid.wanandroid.ui.home.contract.HomeContract;

public class WebViewModel implements ICommonModel {


    /**
     * 收藏
     * @param id 文章id
     * @param callback
     */
    @Override
    public void collectArticle(int id, LifecycleTransformer bindToLife, BaseObserver<String> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().collectArticle(id).compose(bindToLife),callback);
    }

    /**
     * 取消收藏
     * @param id 文章id
     * @param callback
     */
    @Override
    public void unCollectArticle(int id,LifecycleTransformer bindToLife,  BaseObserver<String> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().uncollectArticle(id).compose(bindToLife),callback);
    }
}

package com.zlyandroid.wanandroid.ui.web;

import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;

/**
 * 文章详情页Presenter
 * author: zhangliyang
 * date: 2018/4/10
 */

public class WebViewPresenter extends BasePresenter<WebViewContract.IWebView> implements WebViewContract.IWebViewPresenter {
    private WebViewModel model;

    public WebViewPresenter() {
        model = new WebViewModel();
    }

    @Override
    public void collectArticle(int id) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.collectArticle(id, mView.bindToLife(), new BaseObserver<String>() {
            @Override
            public void onSuccess(String s) {
                mView.collectSuccess(true, "收藏成功");
            }

            @Override
            public void onError(String error) {
                mView.collectFailed(true, "收藏成功");
            }


        });

    }

}

package com.zlyandroid.wanandroid.ui.main.presenter;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.event.CollectionEvent;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.core.bean.BaseBean;
import com.zlyandroid.wanandroid.ui.core.model.MainModel;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.CollectArticleEntity;
import com.zlyandroid.wanandroid.ui.home.bean.CollectionLinkBean;
import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;
import com.zlyandroid.wanandroid.ui.main.contract.LoginContract;
import com.zlyandroid.wanandroid.ui.main.model.LoginModel;
import com.zlyandroid.wanandroid.ui.main.view.LoginView;
import com.zlyandroid.wanandroid.ui.main.view.WebView;
import com.zlyandroid.wanandroid.util.LogUtil;
import com.zlyandroid.wanandroid.util.api.ToastUtils;
import com.zlyandroid.wanandroid.util.bitmap.BitmapUtils;

public class WebPresenter extends BasePresenter<WebView> {



    public void collect( CollectArticleEntity entity) {
        if (entity.isCollect()) {
            //View是否绑定 如果没有绑定，就不执行网络请求
            if (!isViewAttached()) {
                return;
            }
        }
        if (entity.getArticleId() > 0) {
            collectArticle(entity.getArticleId(), entity);
        } else {
            if (TextUtils.isEmpty(entity.getAuthor())) {
                 collectLink(entity);
            } else {
                collectArticle(entity.getTitle(), entity.getAuthor(), entity.getUrl(), entity);
            }
        }

    }
    public void uncollect(CollectArticleEntity entity) {
        if (!entity.isCollect()) {
            //View是否绑定 如果没有绑定，就不执行网络请求
            if (!isViewAttached()) {
                return;
            }
        }
        if (entity.getArticleId() > 0) {
            uncollectArticle(entity.getArticleId(), entity);
        } else {
            uncollectLink(entity.getCollectId(), entity);
        }
    }

    private void collectLink( final CollectArticleEntity entity) {
        MainModel.collectLink(entity.getTitle(),entity.getUrl(), mView.bindToLife(), new BaseObserver<CollectionLinkBean>() {
            @Override
            public void onSuccess(CollectionLinkBean data) {
                CollectionEvent.postCollectWithCollectId(data.getId());
                entity.setCollectId(data.getId());
                entity.setCollect(true);
                if (isViewAttached()) {
                    mView.collectSuccess(entity);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
    private void collectArticle(int id, final CollectArticleEntity entity) {
        MainModel.collectArticle(id, mView.bindToLife(), new BaseObserver<BaseBean>() {
            @Override
            public void onSuccess(BaseBean data) {
                CollectionEvent.postCollectWithArticleId(id);
                entity.setCollect(true);
                if (isViewAttached()) {
                    mView.collectSuccess(entity);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
    private void collectArticle(String title, String author, String link, CollectArticleEntity entity) {
        MainModel.collectArticle(title,author,link, mView.bindToLife(), new BaseObserver<ArticleBean>() {
            @Override
            public void onSuccess(ArticleBean data) {
                CollectionEvent.postCollectWithCollectId(data.getId());
                entity.setArticleId(data.getId());
                entity.setCollect(true);
                if (isViewAttached()) {
                    mView.collectSuccess(entity);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void uncollectLink(int id, final CollectArticleEntity entity) {
        MainModel.uncollectLink(id, mView.bindToLife(), new BaseObserver<BaseBean>() {
            @Override
            public void onSuccess(BaseBean data) {
                CollectionEvent.postUncollectWithCollectId(id);
                entity.setCollect(false);
                if (isViewAttached()) {
                    mView.uncollectSuccess(entity);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }
    private void uncollectArticle(int id, final CollectArticleEntity entity) {
        MainModel.uncollectArticle(id, mView.bindToLife(), new BaseObserver<BaseBean>() {
            @Override
            public void onSuccess(BaseBean data) {
                CollectionEvent.postUnCollectWithArticleId(id);
                entity.setCollect(false);
                if (isViewAttached()) {
                    mView.uncollectSuccess(entity);
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

}

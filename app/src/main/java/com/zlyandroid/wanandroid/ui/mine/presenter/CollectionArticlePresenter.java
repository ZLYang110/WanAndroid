package com.zlyandroid.wanandroid.ui.mine.presenter;

import androidx.annotation.IntRange;

import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.event.CollectionEvent;
import com.zlyandroid.wanandroid.https.WanCache;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.core.bean.BaseBean;
import com.zlyandroid.wanandroid.ui.core.model.MainModel;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.mine.model.CoinRankModel;
import com.zlyandroid.wanandroid.ui.mine.view.CoinRankView;
import com.zlyandroid.wanandroid.ui.mine.view.CollectionArticleView;
import com.zlyandroid.wanandroid.util.LogUtil;
import com.zlyandroid.wanandroid.widget.CollectView;

public class CollectionArticlePresenter extends BasePresenter<CollectionArticleView> {


    public void getCollectArticleListCache(int page) {
        MainModel.getCollectArticleListCache(page, mView.bindToLife(), new BaseObserver<ArticleListBean>() {
            @Override
            public void onSuccess(ArticleListBean callback) {
                mView.getCollectArticleListSuccess(0,callback);
            }

            @Override
            public void onError(String error) {
                mView.getCollectArticleListFailed(1,error);

            }
        });
    }

    public void getCollectArticleList(int page ) {
        MainModel.getCollectArticleList(page, mView.bindToLife(), new BaseObserver<ArticleListBean>() {
            @Override
            public void onSuccess(ArticleListBean callback) {
                mView.getCollectArticleListSuccess(0,callback);
            }

            @Override
            public void onError(String error) {
                mView.getCollectArticleListFailed(1,error);

            }
        });
    }



    public void uncollectArticle(ArticleBean item, final CollectView v) {
        MainModel.uncollectArticle(item.getId(), item.getOriginId(), mView.bindToLife(), new BaseObserver<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                item.setCollect(false);
                if (v.isChecked()) {
                    v.toggle();
                }
                CollectionEvent.postUncollect(item.getOriginId(), item.getId());
            }

            @Override
            public void onError(String error) {
                if (!v.isChecked()) {
                    v.toggle();
                }
            }
        });
    }

}

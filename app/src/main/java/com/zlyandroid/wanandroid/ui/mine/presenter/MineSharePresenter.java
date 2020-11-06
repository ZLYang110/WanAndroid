package com.zlyandroid.wanandroid.ui.mine.presenter;

import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.event.ArticleDeleteEvent;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.core.bean.BaseBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRecordBean;
import com.zlyandroid.wanandroid.ui.mine.bean.UserPageBean;
import com.zlyandroid.wanandroid.ui.mine.model.CoinModel;
import com.zlyandroid.wanandroid.ui.mine.model.MineShareModel;
import com.zlyandroid.wanandroid.ui.mine.view.CoinView;
import com.zlyandroid.wanandroid.ui.mine.view.MineShareView;

/**
 * @author zhangliyang
 * @date 2019/5/17
 * GitHub:  https://github.com/ZLYang110
 */
public class MineSharePresenter extends BasePresenter<MineShareView> {

    MineShareModel model;

    public MineSharePresenter() {
        model = new MineShareModel();
    }

    public void getMineShareArticleList( int page) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getMineShareArticleList(mView.bindToLife(), page, new BaseObserver<UserPageBean>() {
            @Override
            public void onSuccess(UserPageBean callback) {
                mView.getMineShareArticleListSuccess(1,callback.getShareArticles());
            }

            @Override
            public void onError(String error) {
                mView.getMineShareArticleListFailed(1,error);
            }

        });
    }

    public void deleteMineShareArticle(ArticleBean item) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.deleteMineShareArticle(mView.bindToLife(),item, new BaseObserver<BaseBean>() {
            @Override
            public void onSuccess(BaseBean callback) {
                ArticleDeleteEvent.postWithArticleId(item.getId());
            }

            @Override
            public void onError(String error) {
            }

        });
    }

}

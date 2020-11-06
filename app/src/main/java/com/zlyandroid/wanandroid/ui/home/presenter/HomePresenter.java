package com.zlyandroid.wanandroid.ui.home.presenter;


import com.zlyandroid.wanandroid.event.CollectionEvent;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.ui.core.bean.BaseBean;
import com.zlyandroid.wanandroid.ui.core.model.MainModel;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.BannerBean;
import com.zlyandroid.wanandroid.ui.home.contract.HomeContract;
import com.zlyandroid.wanandroid.ui.home.model.HomeModel;
import com.zlyandroid.wanandroid.ui.home.view.HomeView;
import com.zlyandroid.wanandroid.widget.CollectView;

import java.util.List;

public class HomePresenter extends BasePresenter<HomeView> implements HomeContract.Presenter{

    private HomeContract.Model model;

    public HomePresenter() {
        model = new HomeModel();
    }


    @Override
    public void getBanner() {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getBanner(mView.bindToLife(), new BaseObserver<List<BannerBean>>() {
            @Override
            public void onSuccess(List<BannerBean> bannerBeans) {
                mView.getBannerSuccess(1,bannerBeans);
            }

            @Override
            public void onError(String error) {
                mView.getBannerFail(1,error);
            }

        });


    }

    @Override
    public void getTopArticleList() {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getTopArticleList( mView.bindToLife(), new BaseObserver<List<ArticleBean>>() {
            @Override
            public void onSuccess(List<ArticleBean> articleBeans) {
                mView.getTopArticleListSuccess(1,articleBeans);
            }

            @Override
            public void onError(String error) {
                mView.getTopArticleListFailed(1,error);
            }


        });
    }

    @Override
    public void getArticleList(int page) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }

        model.getArticleList(page, mView.bindToLife(), new BaseObserver<ArticleListBean>() {
            @Override
            public void onSuccess(ArticleListBean articleListBean) {
                mView.getArticleListSuccess(1,articleListBean);
            }

            @Override
            public void onError(String error) {
                mView.getArticleListFail(1,error);
            }


        });
    }


    public void collect(ArticleBean item, final CollectView v) {
        MainModel.collectArticle(item.getId(), mView.bindToLife(), new BaseObserver<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                item.setCollect(true);
                if (!v.isChecked()) {
                    v.toggle();
                }
                CollectionEvent.postCollectWithArticleId(item.getId());
            }

            @Override
            public void onError(String error) {
                if (v.isChecked()) {
                    v.toggle();
                }
            }
        });

    }

    public void uncollect(ArticleBean item, final CollectView v) {
        MainModel.uncollectArticle(item.getId(), mView.bindToLife(), new BaseObserver<BaseBean>() {
            @Override
            public void onSuccess(BaseBean baseBean) {
                item.setCollect(false);
                if (v.isChecked()) {
                    v.toggle();
                }
                CollectionEvent.postUnCollectWithArticleId(item.getId());
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

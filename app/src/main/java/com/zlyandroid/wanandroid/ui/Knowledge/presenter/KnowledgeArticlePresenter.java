package com.zlyandroid.wanandroid.ui.Knowledge.presenter;

import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.ChapterBean;
import com.zlyandroid.wanandroid.ui.Knowledge.contract.KnowledgeContract;
import com.zlyandroid.wanandroid.ui.Knowledge.model.KnowledgeModel;
import com.zlyandroid.wanandroid.ui.Knowledge.view.KnowledgeArticleView;
import com.zlyandroid.wanandroid.ui.Knowledge.view.KnowledgeView;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;

import java.util.List;

public class KnowledgeArticlePresenter extends BasePresenter<KnowledgeArticleView>  {

    private KnowledgeContract.Model model;

    public KnowledgeArticlePresenter() {
        model = new KnowledgeModel();
    }


    public void getKnowledgeArticleListCache(int id, int page) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getKnowledgeArticleListCache(mView.bindToLife(),id,page, new BaseObserver<ArticleListBean>() {
            @Override
            public void onSuccess(ArticleListBean callback) {
                mView.getKnowledgeArticleListSuccess(1,callback);
            }

            @Override
            public void onError(String error) {
                mView.getKnowledgeArticleListFail(1,error);
            }

        });
    }

    public void getKnowledgeArticleList(int id, int page) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getKnowledgeArticleListCache(mView.bindToLife(),id,page, new BaseObserver<ArticleListBean>() {
            @Override
            public void onSuccess(ArticleListBean callback) {
                mView.getKnowledgeArticleListSuccess(1,callback);
            }

            @Override
            public void onError(String error) {
                mView.getKnowledgeArticleListFail(1,error);
            }

        });
    }


}

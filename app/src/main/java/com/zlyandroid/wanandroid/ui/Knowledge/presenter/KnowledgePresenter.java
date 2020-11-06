package com.zlyandroid.wanandroid.ui.Knowledge.presenter;

import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.https.WanCache;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.ChapterBean;
import com.zlyandroid.wanandroid.ui.Knowledge.contract.KnowledgeContract;
import com.zlyandroid.wanandroid.ui.Knowledge.model.KnowledgeModel;
import com.zlyandroid.wanandroid.ui.Knowledge.view.KnowledgeView;
import com.zlyandroid.wanandroid.ui.home.bean.BannerBean;
import com.zlyandroid.wanandroid.ui.home.contract.HomeContract;
import com.zlyandroid.wanandroid.ui.home.model.HomeModel;
import com.zlyandroid.wanandroid.ui.home.view.HomeView;

import java.util.List;

public class KnowledgePresenter extends BasePresenter<KnowledgeView>  {

    private KnowledgeContract.Model model;

    public KnowledgePresenter() {
        model = new KnowledgeModel();
    }


    public void getKnowledgeListCache() {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getKnowledgeListCache(mView.bindToLife(), new BaseObserver<List<ChapterBean> >() {
            @Override
            public void onSuccess(List<ChapterBean> callback) {
                mView.getKnowledgeListSuccess(1,callback);
            }
            @Override
            public void onError(String error) {
                mView.getKnowledgeListFail(1,error);
            }

        });
    }

    public void getKnowledgeList() {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getKnowledgeList(mView.bindToLife(), new BaseObserver<List<ChapterBean> >() {
            @Override
            public void onSuccess(List<ChapterBean> callback) {
                mView.getKnowledgeListSuccess(1,callback);
            }

            @Override
            public void onError(String error) {
                mView.getKnowledgeListFail(1,error);
            }

        });
    }

    public void getKnowledgeListCacheAndNet() {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getKnowledgeListCacheAndNet(mView.bindToLife(), new BaseObserver<List<ChapterBean> >() {
            @Override
            public void onSuccess(List<ChapterBean> callback) {
                mView.getKnowledgeListSuccess(1,callback);
            }

            @Override
            public void onError(String error) {
                mView.getKnowledgeListFail(1,error);
            }

        });
    }


}

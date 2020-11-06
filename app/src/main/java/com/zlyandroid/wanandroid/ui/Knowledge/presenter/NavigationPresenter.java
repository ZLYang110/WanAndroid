package com.zlyandroid.wanandroid.ui.Knowledge.presenter;

import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.ChapterBean;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.NavigationBean;
import com.zlyandroid.wanandroid.ui.Knowledge.contract.KnowledgeContract;
import com.zlyandroid.wanandroid.ui.Knowledge.contract.NavigationContract;
import com.zlyandroid.wanandroid.ui.Knowledge.model.KnowledgeModel;
import com.zlyandroid.wanandroid.ui.Knowledge.model.NavigationModel;
import com.zlyandroid.wanandroid.ui.Knowledge.view.KnowledgeView;
import com.zlyandroid.wanandroid.ui.Knowledge.view.NavigationView;

import java.util.List;

public class NavigationPresenter extends BasePresenter<NavigationView>  {

    private NavigationContract.Model model;

    public NavigationPresenter() {
        model = new NavigationModel();
    }


    public void getNaviListCache() {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getNaviListCache(mView.bindToLife(), new BaseObserver<List<NavigationBean>>() {
            @Override
            public void onSuccess(List<NavigationBean> navigationBeans) {
                mView.getNaviListSuccess(0,navigationBeans);
            }

            @Override
            public void onError(String error) {
                mView.getNaviListFail(1,error);
            }
        });
    }

    public void getNaviList() {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getNaviList(mView.bindToLife(), new BaseObserver<List<NavigationBean>>() {
            @Override
            public void onSuccess(List<NavigationBean> navigationBeans) {
                mView.getNaviListSuccess(0,navigationBeans);
            }

            @Override
            public void onError(String error) {
                mView.getNaviListFail(1,error);
            }
        });
    }


}

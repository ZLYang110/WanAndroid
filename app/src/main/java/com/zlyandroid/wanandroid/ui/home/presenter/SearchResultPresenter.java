package com.zlyandroid.wanandroid.ui.home.presenter;

import androidx.annotation.IntRange;

import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.HotKeyBean;
import com.zlyandroid.wanandroid.ui.home.contract.SearchHistoryContract;
import com.zlyandroid.wanandroid.ui.home.contract.SearchResultContract;
import com.zlyandroid.wanandroid.ui.home.model.SearchHistoryModel;
import com.zlyandroid.wanandroid.ui.home.model.SearchResultModel;
import com.zlyandroid.wanandroid.ui.home.view.SearchHistoryView;
import com.zlyandroid.wanandroid.ui.home.view.SearchResultView;

import java.util.List;

public class SearchResultPresenter extends BasePresenter<SearchResultView> implements SearchResultContract.Presenter {

    private SearchResultContract.Model model;

    public SearchResultPresenter() {
        model = new SearchResultModel();
    }

    @Override
    public void search(@IntRange(from = 0)int page, String searchKey) {

        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getSearchKeyListSuccess(page,searchKey,mView.bindToLife(), new BaseObserver<ArticleListBean>() {
            @Override
            public void onSuccess(ArticleListBean hotKeyBeans) {
                mView.searchSuccess(1,hotKeyBeans);
            }

            @Override
            public void onError(String error) {
                mView.searchFailed(1,error);
            }


        });
    }
}

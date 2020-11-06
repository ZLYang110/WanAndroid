package com.zlyandroid.wanandroid.ui.home.contract;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.HotKeyBean;

import java.util.List;

public interface SearchResultContract {

    interface Model {
        //根据关键字搜索
        void getSearchKeyListSuccess(int page,String searchKey,LifecycleTransformer bindToLife, BaseObserver<ArticleListBean> data);

    }

    interface Presenter {

        void search(int page,String searchKey);
    }
}

package com.zlyandroid.wanandroid.ui.home.contract;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.BannerBean;
import com.zlyandroid.wanandroid.ui.home.bean.HotKeyBean;

import java.util.List;

public interface SearchHistoryContract {

    interface Model {

        //获取关键字热搜词
        void getHotKeyListSuccess(LifecycleTransformer bindToLife, BaseObserver<List<HotKeyBean>> data);
    }

    interface Presenter {

        void getHotKeyListSuccess();
    }
}

package com.zlyandroid.wanandroid.ui.Knowledge.contract;

import androidx.annotation.IntRange;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.ChapterBean;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.NavigationBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;

import java.util.List;

public interface NavigationContract {

    interface Model {


        void getNaviListCache(LifecycleTransformer bindToLife, BaseObserver<List<NavigationBean>> callback);

        void getNaviList(LifecycleTransformer bindToLife, BaseObserver<List<NavigationBean>> callback);


    }

    interface Presenter {

        void getNaviListCache();

        void getNaviList();

    }
}

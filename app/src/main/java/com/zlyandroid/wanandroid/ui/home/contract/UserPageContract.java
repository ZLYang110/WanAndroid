package com.zlyandroid.wanandroid.ui.home.contract;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.HotKeyBean;
import com.zlyandroid.wanandroid.ui.mine.bean.UserPageBean;

import java.util.List;

public interface UserPageContract {

    interface Model {
        /**
         * 获取个人资料
         * */
        void getUserPage(int userId, int page, LifecycleTransformer bindToLife, BaseObserver<UserPageBean> callback);
    }

    interface Presenter {

        void getUserPage(int userId, int page);

    }
}

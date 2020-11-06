package com.zlyandroid.wanandroid.ui.mine.contract;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.mine.bean.UserInfoBean;

public interface MineContract {

    interface Model {
        void getUserInfo(LifecycleTransformer bindToLife, BaseObserver<UserInfoBean> callback);
    }

    interface Presenter {

        void getUserInfo();
    }
}

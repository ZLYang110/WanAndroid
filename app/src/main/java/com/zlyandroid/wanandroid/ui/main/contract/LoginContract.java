package com.zlyandroid.wanandroid.ui.main.contract;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;

public interface LoginContract {

    interface Model {
        void login(String username, String password, LifecycleTransformer bindToLife, BaseObserver<LoginBean> scheduler);
        void saveUserInfo(LoginBean bean);
        void cleanUserInfo();
    }

    interface Presenter {
        /**
         * 登陆
         *
         * @param username
         * @param password
         */
        void login(String username, String password);
        /**
         * 测试
         */
        //  void getList(String city, Integer sn,Integer pn);
    }
}

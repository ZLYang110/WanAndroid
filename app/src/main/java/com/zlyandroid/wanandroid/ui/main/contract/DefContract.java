package com.zlyandroid.wanandroid.ui.main.contract;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;

public class DefContract {


       public static void login(String username, String password, BaseObserver<LoginBean> callback) {
           RHttp.getInstance().getApi().login(username, password).compose(RxSchedulers.Obs_io_main())
                   .subscribe(callback);
       }
}

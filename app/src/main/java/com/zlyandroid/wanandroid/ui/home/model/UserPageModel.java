package com.zlyandroid.wanandroid.ui.home.model;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.ui.home.bean.HotKeyBean;
import com.zlyandroid.wanandroid.ui.home.contract.SearchHistoryContract;
import com.zlyandroid.wanandroid.ui.home.contract.UserPageContract;
import com.zlyandroid.wanandroid.ui.home.presenter.UserPagePresenter;
import com.zlyandroid.wanandroid.ui.mine.bean.UserPageBean;

import java.util.List;

public class UserPageModel implements UserPageContract.Model{


    @Override
    public void getUserPage(int userId, int page, LifecycleTransformer bindToLife, BaseObserver<UserPageBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().getUserPage(userId,page).compose(bindToLife),callback);
    }
}

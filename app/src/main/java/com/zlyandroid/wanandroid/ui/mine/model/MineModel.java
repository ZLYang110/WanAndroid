package com.zlyandroid.wanandroid.ui.mine.model;


import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.ui.mine.bean.UserInfoBean;
import com.zlyandroid.wanandroid.ui.mine.contract.MineContract;

public class MineModel implements MineContract.Model {


    @Override
    public void getUserInfo(LifecycleTransformer bindToLife, BaseObserver<UserInfoBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().getUserInfo().compose(bindToLife),callback);
    }
}

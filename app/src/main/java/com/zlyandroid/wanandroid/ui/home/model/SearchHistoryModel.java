package com.zlyandroid.wanandroid.ui.home.model;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.ui.home.bean.HotKeyBean;
import com.zlyandroid.wanandroid.ui.home.contract.HomeContract;
import com.zlyandroid.wanandroid.ui.home.contract.SearchHistoryContract;

import java.util.List;

public class SearchHistoryModel implements SearchHistoryContract.Model {


    /**
    * 获取关键字热搜记录
    * */
    @Override
    public void getHotKeyListSuccess(LifecycleTransformer bindToLife, BaseObserver<List<HotKeyBean>> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().getHotKeyList().compose(bindToLife),callback);
    }
}

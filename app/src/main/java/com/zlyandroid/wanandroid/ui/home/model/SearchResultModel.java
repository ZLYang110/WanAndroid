package com.zlyandroid.wanandroid.ui.home.model;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.HotKeyBean;
import com.zlyandroid.wanandroid.ui.home.contract.SearchHistoryContract;
import com.zlyandroid.wanandroid.ui.home.contract.SearchResultContract;

import java.util.List;

public class SearchResultModel implements SearchResultContract.Model {


    /**
    * 根据关键字搜索
    * */
    @Override
    public void getSearchKeyListSuccess(int page, String searchKey, LifecycleTransformer bindToLife, BaseObserver<ArticleListBean> callback) {

        BaseRequest.requst(RHttp.getInstance().getApi().search(page,searchKey).compose(bindToLife),callback);
    }
}

package com.zlyandroid.wanandroid.ui.mine.model;


import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRecordBean;
import com.zlyandroid.wanandroid.ui.mine.contract.MineContract;

/**
 * @author zhangliyang
 * @date 2019/5/15
     * GitHub:  https://github.com/ZLYang110
 */
public class CoinModel   {

    public void getCoin(LifecycleTransformer bindToLife, BaseObserver<Integer> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().getCoin().compose(bindToLife),callback);
    }
    public void getCoinRecordList(LifecycleTransformer bindToLife,int page, BaseObserver<CoinRecordBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().getCoinRecordList(page).compose(bindToLife),callback);
    }


}

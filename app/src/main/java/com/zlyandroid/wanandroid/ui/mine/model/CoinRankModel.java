package com.zlyandroid.wanandroid.ui.mine.model;


import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRankBean;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRecordBean;

/**
 * @author zhangliyang
 * @date 2019/5/15
 * GitHub:  https://github.com/ZLYang110
 */
public class CoinRankModel {


    public void getCoinRankList(LifecycleTransformer bindToLife,int page, BaseObserver<CoinRankBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().getCoinRankList(page).compose(bindToLife),callback);
    }


}

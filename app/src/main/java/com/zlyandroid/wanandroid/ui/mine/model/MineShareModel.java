package com.zlyandroid.wanandroid.ui.mine.model;


import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.ui.core.bean.BaseBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRankBean;
import com.zlyandroid.wanandroid.ui.mine.bean.UserPageBean;

/**
 * @author zhangliyang
 * @date 2019/5/15
 * GitHub:  https://github.com/ZLYang110
 */
public class MineShareModel {


    public void getMineShareArticleList(LifecycleTransformer bindToLife,int page, BaseObserver<UserPageBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().getMineShareArticleList(page).compose(bindToLife),callback);
    }
    public void deleteMineShareArticle(LifecycleTransformer bindToLife, ArticleBean item, BaseObserver<BaseBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().deleteMineShareArticle(item.getId()).compose(bindToLife),callback);
    }


}

package com.zlyandroid.wanandroid.ui.mine.presenter;

import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.event.CollectionEvent;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.core.bean.BaseBean;
import com.zlyandroid.wanandroid.ui.core.model.MainModel;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.CollectionLinkBean;
import com.zlyandroid.wanandroid.ui.mine.view.CollectionLinkView;

import java.util.List;

public class CollectionLinkPresenter extends BasePresenter<CollectionLinkView> {

    public void getCollectLinkList( ) {
        MainModel.getCollectLinkList(mView.bindToLife(), new BaseObserver<List<CollectionLinkBean>>() {
            @Override
            public void onSuccess(List<CollectionLinkBean> callback) {
                mView.getCollectLinkListSuccess(0,callback);
            }

            @Override
            public void onError(String error) {
                mView.getCollectLinkListFailed(1,error);
            }
        });
    }
    public void uncollectLink(CollectionLinkBean item) {

    }

    public void updateCollectLink(CollectionLinkBean item) {

    }

}

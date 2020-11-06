package com.zlyandroid.wanandroid.ui.question.model;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;

import java.util.List;

public class QuestionModel {

    public void getQuestionList(LifecycleTransformer bindToLife,int page, BaseObserver<ArticleListBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().getQuestionList(page).compose(bindToLife),callback);
    }
}

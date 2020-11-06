package com.zlyandroid.wanandroid.ui.question.presenter;

import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.Knowledge.model.KnowledgeModel;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.question.model.QuestionModel;
import com.zlyandroid.wanandroid.ui.question.view.QuestionView;

public class QuestionPresenter  extends BasePresenter<QuestionView> {

    QuestionModel model;

    public QuestionPresenter() {
        model = new QuestionModel();
    }

    public void getQuestionList(  int page) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getQuestionList(mView.bindToLife(),page, new BaseObserver<ArticleListBean>() {
            @Override
            public void onSuccess(ArticleListBean callback) {
                mView.getQuestionListSuccess(1,callback);
            }

            @Override
            public void onError(String error) {
                mView.getQuestionListFail(1,error);
            }

        });
    }



}

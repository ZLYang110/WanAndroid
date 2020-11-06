package com.zlyandroid.wanandroid.ui.home.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.BannerBean;

import java.util.List;

public interface HomeView extends BaseView {

    void getBannerSuccess(int code, List<BannerBean> data);
    void getBannerFail(int code, String msg);

    void getArticleListSuccess(int code, ArticleListBean data);
    void getArticleListFail(int code, String msg);

    void getTopArticleListSuccess(int code, List<ArticleBean> data);
    void getTopArticleListFailed(int code, String msg);
}

package com.zlyandroid.wanandroid.ui.home.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.HotKeyBean;

import java.util.List;

public interface SearchResultView extends BaseView {

    void searchSuccess(int code, ArticleListBean data);
    void searchFailed(int code, String msg);
}

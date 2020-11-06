package com.zlyandroid.wanandroid.ui.main.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.home.bean.CollectArticleEntity;
import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;

public interface WebView extends BaseView {
    void collectSuccess(CollectArticleEntity entity);
    void collectFailed(String msg);
    void uncollectSuccess(CollectArticleEntity entity);
    void uncollectFailed(String msg);
}

package com.zlyandroid.wanandroid.ui.web;

import com.zlyandroid.wanandroid.base.mvp.BaseView;

/**
 * 文章详情页协约类
 * author: zhangliyang
 * date: 2018/4/10
 */

public class WebViewContract {


    interface IWebViewPresenter{
        void collectArticle(int id);
    }

    interface IWebView extends BaseView {

        void collectSuccess(boolean isCollect, String result);
        void collectFailed(boolean isCollect, String result);
    }
}

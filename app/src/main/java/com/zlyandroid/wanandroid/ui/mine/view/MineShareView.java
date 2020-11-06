package com.zlyandroid.wanandroid.ui.mine.view;


import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;

/**
 * @author zhangliyang
 * @date 2019/5/17
 * GitHub: https://github.com/ZLYang110
 */
public interface MineShareView extends BaseView {

    void getMineShareArticleListSuccess(int code, ArticleListBean data);

    void getMineShareArticleListFailed(int code, String msg);
}

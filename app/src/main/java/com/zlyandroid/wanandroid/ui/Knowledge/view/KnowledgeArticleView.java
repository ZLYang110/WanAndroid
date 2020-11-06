package com.zlyandroid.wanandroid.ui.Knowledge.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.ChapterBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;

import java.util.List;


/**
 * @author zhangliyang
 * @date 2019/5/12
 * GitHub: https://github.com/ZLYang110
 */
public interface KnowledgeArticleView extends BaseView {
    void getKnowledgeArticleListSuccess(int code, ArticleListBean data);
    void getKnowledgeArticleListFail(int code, String msg);
}

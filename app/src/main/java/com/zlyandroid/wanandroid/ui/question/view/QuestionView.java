package com.zlyandroid.wanandroid.ui.question.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;


/**
 * @author zhangliyang
 * @date 2019/5/12
 * GitHub: https://github.com/ZLYang110
 */
public interface QuestionView  extends BaseView {
    void getQuestionListSuccess(int code, ArticleListBean data);
    void getQuestionListFail(int code, String msg);
}

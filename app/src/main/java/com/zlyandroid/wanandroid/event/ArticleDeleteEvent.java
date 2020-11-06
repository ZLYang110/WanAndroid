package com.zlyandroid.wanandroid.event;

/**
 * @author zhangliyang
 * @date 2019/5/17
 * GitHub: https://github.com/ZLYang110
 */
public class ArticleDeleteEvent extends BaseEvent {

    private int articleId;

    public static void postWithArticleId(int articleId) {
        new ArticleDeleteEvent(articleId).post();
    }

    private ArticleDeleteEvent(int articleId) {
        this.articleId = articleId;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }
}

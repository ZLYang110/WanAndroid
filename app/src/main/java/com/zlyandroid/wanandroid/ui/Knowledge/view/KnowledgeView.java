package com.zlyandroid.wanandroid.ui.Knowledge.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.ChapterBean;

import java.util.List;


/**
 * @author zhangliyang
 * @date 2019/5/12
 * GitHub: https://github.com/ZLYang110
 */
public interface KnowledgeView extends BaseView {
    void getKnowledgeListSuccess(int code, List<ChapterBean> data);
    void getKnowledgeListFail(int code, String msg);
}

package com.zlyandroid.wanandroid.ui.mine.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.home.bean.CollectionLinkBean;

import java.util.List;


/**
 * @author zhangliyang
 * @date 2019/5/17
 * GitHub:  https://github.com/ZLYang110
 */
public interface CollectionLinkView extends BaseView {
    void getCollectLinkListSuccess(int code, List<CollectionLinkBean> data);
    void getCollectLinkListFailed(int code, String msg);

    void updateCollectLinkSuccess(int code, CollectionLinkBean data);
}

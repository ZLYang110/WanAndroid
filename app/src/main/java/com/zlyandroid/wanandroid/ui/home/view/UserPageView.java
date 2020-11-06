package com.zlyandroid.wanandroid.ui.home.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.mine.bean.UserPageBean;

public interface UserPageView extends BaseView {


    void getUserPageSuccess( UserPageBean data);
    void getUserPageFail( String msg);
}

package com.zlyandroid.wanandroid.ui.mine.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.mine.bean.AboutMeBean;



public interface AboutMeView extends BaseView {
    void getAboutMeSuccess(int code, AboutMeBean data);
    void getAboutMeFailed(int code, String msg);
}

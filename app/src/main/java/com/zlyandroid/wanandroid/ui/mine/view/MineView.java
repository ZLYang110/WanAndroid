package com.zlyandroid.wanandroid.ui.mine.view;

import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;
import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.mine.bean.UserInfoBean;

public interface MineView extends BaseView {
    void getUserInfoSuccess(int code, UserInfoBean coin);

    void getUserInfoFail(int code, String msg);
}

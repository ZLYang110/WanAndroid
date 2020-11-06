package com.zlyandroid.wanandroid.ui.main.view;

import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;
import com.zlyandroid.wanandroid.base.mvp.BaseView;

public interface LoginView extends BaseView {
    @Override
    void showLoading();

    @Override
    void hideLoading();

    void onError(String throwable);

    void onSuccess(LoginBean bean);
}

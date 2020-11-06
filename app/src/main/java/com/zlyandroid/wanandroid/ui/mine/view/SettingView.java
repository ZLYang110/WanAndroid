package com.zlyandroid.wanandroid.ui.mine.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;

public interface SettingView extends BaseView {
    @Override
    void showLoading();

    @Override
    void hideLoading();

    void getCacheSizeSuccess(String size);
}

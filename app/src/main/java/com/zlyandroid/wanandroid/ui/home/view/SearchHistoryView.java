package com.zlyandroid.wanandroid.ui.home.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.home.bean.HotKeyBean;

import java.util.List;

public interface SearchHistoryView extends BaseView {

    void getHotKeyListSuccess(int code, List<HotKeyBean> data);
    void getHotKeyListFail(int code, String msg);
}

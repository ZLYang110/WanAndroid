package com.zlyandroid.wanandroid.ui.Knowledge.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.NavigationBean;

import java.util.List;



/**
 * @author zhangliyang
 * @date 2019/5/12
 * GitHub: https://github.com/ZLYang110
 */
public interface NavigationView extends BaseView {
    void getNaviListSuccess(int code, List<NavigationBean> data);
    void getNaviListFail(int code, String msg);
}

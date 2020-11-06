package com.zlyandroid.wanandroid.ui.mine.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRecordBean;
/**
 * @author zhangliyang
 * @date 2019/5/15
 * GitHub:  https://github.com/ZLYang110
 */
public interface CoinView extends BaseView {

    void getCoinSuccess(int code, int coin);

    void getCoinFail(int code, String msg);

    void getCoinRecordListSuccess(int code, CoinRecordBean data);

    void getCoinRecordListFail(int code, String msg);
}

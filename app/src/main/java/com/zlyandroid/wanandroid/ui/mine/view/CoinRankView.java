package com.zlyandroid.wanandroid.ui.mine.view;


import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRankBean;

/**
 * @author zhangliyang
 * @date 2019/5/12
 * GitHub: https://github.com/ZLYang110
 */
public interface CoinRankView extends BaseView {
    void getCoinRankListSuccess(int code, CoinRankBean data);

    void getCoinRankListFail(int code, String msg);
}

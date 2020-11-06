package com.zlyandroid.wanandroid.ui.mine.presenter;

import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRankBean;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRecordBean;
import com.zlyandroid.wanandroid.ui.mine.model.CoinModel;
import com.zlyandroid.wanandroid.ui.mine.model.CoinRankModel;
import com.zlyandroid.wanandroid.ui.mine.view.CoinRankView;
import com.zlyandroid.wanandroid.ui.mine.view.CoinView;

/**
 * @author zhangliyang
 * @date 2019/5/15
 * GitHub:  https://github.com/ZLYang110
 */
public class CoinRankPresenter extends BasePresenter<CoinRankView> {

    CoinRankModel model;

    public CoinRankPresenter() {
        model = new CoinRankModel();
    }



    public void getCoinRankList(  int page) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getCoinRankList(mView.bindToLife(),page, new BaseObserver<CoinRankBean>() {
            @Override
            public void onSuccess(CoinRankBean callback) {
                mView.getCoinRankListSuccess(1,callback);
            }

            @Override
            public void onError(String error) {
                mView.getCoinRankListFail(1,error);
            }

        });
    }

}

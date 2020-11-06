package com.zlyandroid.wanandroid.ui.mine.presenter;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRecordBean;
import com.zlyandroid.wanandroid.ui.mine.contract.MineContract;
import com.zlyandroid.wanandroid.ui.mine.model.CoinModel;
import com.zlyandroid.wanandroid.ui.mine.model.MineModel;
import com.zlyandroid.wanandroid.ui.mine.view.CoinView;
import com.zlyandroid.wanandroid.ui.mine.view.MineView;
import com.zlyandroid.wanandroid.ui.question.model.QuestionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zhangliyang
 * @date 2019/5/15
 * GitHub:  https://github.com/ZLYang110
 */
public class CoinPresenter extends BasePresenter<CoinView> {

    CoinModel model;

    public CoinPresenter() {
        model = new CoinModel();
    }

    public void getCoin( ) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getCoin(mView.bindToLife(), new BaseObserver<Integer>() {
            @Override
            public void onSuccess(Integer callback) {
                mView.getCoinSuccess(1,callback);
            }

            @Override
            public void onError(String error) {
                mView.getCoinFail(1,error);
            }

        });
    }

    public void getCoinRecordList(  int page) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getCoinRecordList(mView.bindToLife(),page, new BaseObserver<CoinRecordBean>() {
            @Override
            public void onSuccess(CoinRecordBean callback) {
                mView.getCoinRecordListSuccess(1,callback);
            }

            @Override
            public void onError(String error) {
                mView.getCoinRecordListFail(1,error);
            }

        });
    }

}

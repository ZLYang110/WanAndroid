package com.zlyandroid.wanandroid.ui.mine.presenter;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.mine.bean.UserInfoBean;
import com.zlyandroid.wanandroid.ui.mine.contract.MineContract;
import com.zlyandroid.wanandroid.ui.mine.model.MineModel;
import com.zlyandroid.wanandroid.ui.mine.view.MineView;
import com.zlyandroid.wanandroid.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinePresenter extends BasePresenter<MineView> implements MineContract.Presenter{

    private MineContract.Model model;

    public MinePresenter() {
        model = new MineModel();
    }

    @Override
    public void getUserInfo() {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getUserInfo(mView.bindToLife(), new BaseObserver<UserInfoBean>() {
            @Override
            public void onSuccess(UserInfoBean callback) {
                mView.getUserInfoSuccess(1,callback);
            }

            @Override
            public void onError(String error) {
                mView.getUserInfoFail(1,error);
            }

        });
    }





}

package com.zlyandroid.wanandroid.ui.home.presenter;


import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.home.contract.UserPageContract;
import com.zlyandroid.wanandroid.ui.home.model.UserPageModel;
import com.zlyandroid.wanandroid.ui.home.view.UserPageView;
import com.zlyandroid.wanandroid.ui.mine.bean.UserPageBean;
import com.zlyandroid.wanandroid.util.LogUtil;

public class UserPagePresenter extends BasePresenter<UserPageView> implements UserPageContract.Presenter {

    private static String TAG = "UserPagePresenter";

    private UserPageContract.Model model;

    public UserPagePresenter() {
        model = new UserPageModel();
    }



    @Override
    public void getUserPage(int userId, int page) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getUserPage(userId, page, mView.bindToLife(), new BaseObserver<UserPageBean>() {
            @Override
            public void onSuccess(UserPageBean bean) {
                mView.getUserPageSuccess(bean);
            }

            @Override
            public void onError(String error) {
                mView.getUserPageFail(error);
            }


        });
    }
}

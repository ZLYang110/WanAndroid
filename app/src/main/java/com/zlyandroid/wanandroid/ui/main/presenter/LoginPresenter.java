package com.zlyandroid.wanandroid.ui.main.presenter;

import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.ui.main.contract.LoginContract;
import com.zlyandroid.wanandroid.ui.main.model.LoginModel;
import com.zlyandroid.wanandroid.ui.main.view.LoginView;
import com.zlyandroid.wanandroid.util.LogUtil;

public class LoginPresenter extends BasePresenter<LoginView> implements LoginContract.Presenter{

    private LoginContract.Model model;

    public LoginPresenter() {
        model = new LoginModel();
    }

    @Override
    public void login(String username, String password) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        mView.showLoading();

        model.login(username, password, mView.bindToLife(),new BaseObserver<LoginBean>() {

            @Override
            public void onSuccess(LoginBean bean) {
                LogUtil.d("login--- "+bean.getUsername());
                mView.hideLoading();
                bean.setPassword(password);
                model.saveUserInfo(bean);
                mView.onSuccess(bean);
            }
            @Override
            public void onError(String error) {
                mView.onError(error);
                mView.hideLoading();
            }
        });

    }
}

package com.zlyandroid.wanandroid.ui.main.model;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.core.UserInfoManager;
import com.zlyandroid.wanandroid.https.callback.BaseRequest;
import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.ui.main.contract.LoginContract;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;

public class LoginModel implements LoginContract.Model {


    @Override
    public void login(String username, String password, LifecycleTransformer bindToLife,BaseObserver <LoginBean> callback) {
        BaseRequest.requst(RHttp.getInstance().getApi().login(username,password).compose(bindToLife),callback);
    }

    /**
     * 保存用户信息
     * @param user
     */
    @Override
    public void saveUserInfo(LoginBean user) {
        //加密保存用户信息和密钥
        UserInfoManager.saveUserInfo(user);
        UserInfoManager.saveIsLogin(true);
    }

    @Override
    public void cleanUserInfo() {
        //加密保存用户信息和密钥
        UserInfoManager.logout();
    }


}

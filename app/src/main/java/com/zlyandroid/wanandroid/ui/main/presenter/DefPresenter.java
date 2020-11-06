package com.zlyandroid.wanandroid.ui.main.presenter;

import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.event.LoginEvent;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.main.LoginActivity;
import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;
import com.zlyandroid.wanandroid.ui.main.contract.DefContract;
import com.zlyandroid.wanandroid.ui.main.contract.LoginContract;
import com.zlyandroid.wanandroid.ui.main.model.LoginModel;
import com.zlyandroid.wanandroid.ui.main.view.LoginView;
import com.zlyandroid.wanandroid.util.UserInfoUtils;

public class DefPresenter  {

    private static LoginContract.Model model = new LoginModel();
    private static class Holder {
        private static final DefPresenter INSTANCE = new DefPresenter();
    }
    public static DefPresenter getInstance() {
        return Holder.INSTANCE;
    }

    //自动登录
   public void getLogin(String name,String pwd){
       DefContract.login(name, pwd, new BaseObserver<LoginBean>() {
           @Override
           public void onSuccess(LoginBean loginBean) {
               loginBean.setPassword(pwd);
               model.saveUserInfo(loginBean);

           }

           @Override
           public void onError(String error) {
               model.cleanUserInfo();
           }
       });
    }
}

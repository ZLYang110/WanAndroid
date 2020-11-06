package com.zlyandroid.wanandroid.event;

/**
 * @author zhangliyang
 * @date 2019/5/17
 * GitHub: https://github.com/goweii
 */
public class LoginEvent extends BaseEvent {

    private boolean login;

    public LoginEvent(boolean login) {
        this.login = login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isLogin() {
        return login;
    }
}

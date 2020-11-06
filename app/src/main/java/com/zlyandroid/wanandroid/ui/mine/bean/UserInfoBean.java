package com.zlyandroid.wanandroid.ui.mine.bean;


import com.zlyandroid.wanandroid.ui.core.bean.BaseBean;

/**
 * @author zhangliyang
 * @date 2019/11/9
 * GitHub: https://github.com/ZLYang110
 */
public class UserInfoBean extends BaseBean {
    /**
     * coinCount : 3232
     * level : 33
     * rank : 1
     * userId : 20382
     * username :
     */

    private int coinCount;
    private int level;
    private int rank;
    private int userId;
    private String username;

    public int getCoinCount() {
        return coinCount;
    }

    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

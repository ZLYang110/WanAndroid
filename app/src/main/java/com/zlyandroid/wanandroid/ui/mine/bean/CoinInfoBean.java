package com.zlyandroid.wanandroid.ui.mine.bean;


import java.io.Serializable;

/**
 * @author zhangliyang
 * @date 2019/5/17
 * GitHub: https://github.com/ZLYang110
 */
public class CoinInfoBean implements Serializable {
    /**
     * coinCount : 1285
     * rank : 6
     * userId : 2
     * username : x**oyang
     *
     * "coinInfo":{"coinCount":42,"level":1,"rank":"6273","userId":33529,"username":"享**堂"}
     */

    private int coinCount;
    private int rank;
    private int userId;
    private String username;
    public boolean anim;

    public int getCoinCount() {
        return coinCount;
    }

    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
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

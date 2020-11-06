package com.zlyandroid.wanandroid.event;

import org.greenrobot.eventbus.EventBus;

/**
 * @author zhangliyang
 * @date 2019/5/17
 * GitHub: https://github.com/ZLYang110
 */
public class BaseEvent {

    public void post(){
        EventBus.getDefault().post(this);
    }

    public void postSticky() {
        EventBus.getDefault().postSticky(this);
    }

    public void removeSticky() {
        EventBus.getDefault().removeStickyEvent(this);
    }

}

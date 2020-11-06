package com.zlyandroid.wanandroid.event;

public class RefreshTodoEvent extends BaseEvent {
    public int getStatus() {
        return status;
    }

    private int status;

    public RefreshTodoEvent(int status) {
        this.status = status;
    }

}

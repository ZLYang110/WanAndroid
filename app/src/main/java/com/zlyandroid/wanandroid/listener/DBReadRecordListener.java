package com.zlyandroid.wanandroid.listener;


import com.zlyandroid.wanandroid.db.greendao.ReadRecordModel;

import java.util.List;

/**
 * Created by zhangliyang 20200615
 * Description:  数据库监听
 */
public interface DBReadRecordListener {

    void onError(String throwable);
    void onSuccess(List<ReadRecordModel> callback);

}

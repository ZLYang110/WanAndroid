package com.zlyandroid.wanandroid.ui.mine.model;


import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.db.readlater.DbReadLaterHelperImpl;
import com.zlyandroid.wanandroid.db.readrecord.DbReadRecordHelperImpl;
import com.zlyandroid.wanandroid.listener.DBReadLaterListener;
import com.zlyandroid.wanandroid.listener.DBReadRecordListener;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhangliyang
 * @date 2019/5/15
 * GitHub:  https://github.com/ZLYang110
 */
public class ReadLaterM {

    public void getReadLaterList(LifecycleTransformer bindToLife, DBReadLaterListener listener) {
        Observable.just(DbReadLaterHelperImpl.getInstance().loadAllTodoData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->  listener.onSuccess(data));

    }

    public void delectReadLaterById(LifecycleTransformer bindToLife,Long id, DBReadLaterListener listener) {
        Observable.just(DbReadLaterHelperImpl.getInstance().deletById(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->  listener.onSuccess(data));

    }

}

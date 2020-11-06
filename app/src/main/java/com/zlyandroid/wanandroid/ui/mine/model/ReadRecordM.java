package com.zlyandroid.wanandroid.ui.mine.model;


import android.annotation.SuppressLint;

import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.db.readrecord.DbReadRecordHelperImpl;
import com.zlyandroid.wanandroid.https.RHttp;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.listener.DBReadRecordListener;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRecordBean;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhangliyang
 * @date 2019/5/15
 * GitHub:  https://github.com/ZLYang110
 */
public class ReadRecordM {

    public void getReadRecordList(LifecycleTransformer bindToLife, DBReadRecordListener listener) {
        Observable.just(DbReadRecordHelperImpl.getInstance().loadAllTodoData())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->  listener.onSuccess(data));

    }

    public void delectReadRecordById(LifecycleTransformer bindToLife,Long id, DBReadRecordListener listener) {
        Observable.just(DbReadRecordHelperImpl.getInstance().deletById(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data ->  listener.onSuccess(data));

    }
    public void delectReadRecordAll(LifecycleTransformer bindToLife, DBReadRecordListener listener) {
        DbReadRecordHelperImpl.getInstance().clearAllData();

    }
}

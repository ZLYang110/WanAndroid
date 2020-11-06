package com.zlyandroid.wanandroid.https.callback;

import androidx.annotation.NonNull;

import com.zlyandroid.wanandroid.https.WanCache;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.observer.RxSchedulers;
import com.zlyandroid.wanandroid.listener.CacheListener;
import com.zlyandroid.wanandroid.util.LogUtil;

import java.util.List;

import io.reactivex.Observable;

public class BaseRequest {

    public static <T> void requst(@NonNull Observable<ResponseBean<T>> observable, @NonNull BaseObserver<T> listener){
        observable.compose(RxSchedulers.Obs_io_main()).subscribe(new BaseObserver<T>() {
            @Override
            public void onSuccess(T t) {
                listener.onSuccess(t);
            }
            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }
    public static <T> void requstCacheBean(String key, Class<T> clazz,@NonNull Observable<ResponseBean<T>> observable, @NonNull BaseObserver<T> listener){
        observable.compose(RxSchedulers.Obs_io_main()).subscribe(new BaseObserver<T>() {
            @Override
            public void onSuccess(T t) {
                WanCache.getInstance().getBean(key, clazz, new CacheListener<T>() {
                    @Override
                    public void onSuccess(int code, T data) {
                        if (!WanCache.getInstance().isSame(data, t)) {
                            WanCache.getInstance().save(key, t);
                        }
                    }
                    @Override
                    public void onFailed() {
                        WanCache.getInstance().save(key, t);
                    }
                });
                listener.onSuccess(t);
            }
            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }

    public static <T> void requstCacheList(String key, Class<T> clazz,@NonNull Observable<ResponseBean<T>> observable, @NonNull BaseObserver<List<T>> listener){
        observable.compose(RxSchedulers.Obs_io_main()).subscribe(new BaseObserver<List<T>>() {
            @Override
            public void onSuccess(List<T> t) {
                WanCache.getInstance().getBean(key, clazz, new CacheListener<T>() {
                    @Override
                    public void onSuccess(int code, T data) {
                        LogUtil.d(data);
                        if (!WanCache.getInstance().isSame(data, t)) {
                            WanCache.getInstance().save(key, t);
                        }
                    }
                    @Override
                    public void onFailed() {
                        WanCache.getInstance().save(key, t);
                    }
                });
                listener.onSuccess(t);
            }
            @Override
            public void onError(String error) {
                listener.onError(error);
            }
        });
    }


    public static <T> void cacheBean(String key,
                                     Class<T> clazz,
                                     Observable<ResponseBean<T>> observable,
                                     BaseObserver<T> listener) {
        WanCache.getInstance().getBean(key, clazz, new CacheListener<T>() {
            @Override
            public void onSuccess(int code, T data) {
                LogUtil.d(data);
                listener.onSuccess( data);
            }

            @Override
            public void onFailed() {
                observable.compose(RxSchedulers.Obs_io_main()).subscribe(new BaseObserver<T>() {
                    @Override
                    public void onSuccess(T t) {
                        WanCache.getInstance().save(key, t);
                        listener.onSuccess(t);
                    }

                    @Override
                    public void onError(String error) {
                        listener.onError(error);
                    }
                });
            }
        });
    }

    public static <T> void cacheList(String key,
                                        Class<T> clazz,
                                        Observable<ResponseBean<T>> observable,
                                        BaseObserver<List<T>> listener) {
        WanCache.getInstance().getList(key, clazz, new CacheListener<List<T>>() {
            @Override
            public void onSuccess(int code, final List<T> data) {
                listener.onSuccess(  data);
            }

            @Override
            public void onFailed() {
                observable.compose(RxSchedulers.Obs_io_main()).subscribe(new BaseObserver<List<T>>() {
                    @Override
                    public void onSuccess(List<T> t) {
                        WanCache.getInstance().save(key, t);
                        listener.onSuccess(t);
                    }

                    @Override
                    public void onError(String error) {
                        listener.onError(error);
                    }
                });
            }
        });
    }
}

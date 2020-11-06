package com.zlyandroid.wanandroid.ui.mine.presenter;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.https.WanCache;
import com.zlyandroid.wanandroid.ui.main.contract.DefContract;
import com.zlyandroid.wanandroid.ui.mine.view.SettingView;
import com.zlyandroid.wanandroid.util.file.CacheUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SettingPresenter extends BasePresenter<SettingView>  {

    public static final int THEME_DEFAULT = R.style.AppTheme;
    public static final int THEME_NIGHT = 1;


    public static int[] colors = new int[]{
            0x7bb736, 0x1e1e1e, 0xf44836, 0xf2821e, 0xd12121, 0x16c24b,
            0x16a8c2, 0x2b86e3, 0x3f51b5, 0x9c27b0, 0xcc268f, 0x39c5bb
    };

    public static int[] colorsDark = new int[]{
            0x7bb736, 0x141414, 0xf44836, 0xf2821e, 0xd12121, 0x16c24b,
            0x16a8c2, 0x2b86e3, 0x3f51b5, 0x9c27b0, 0xcc268f, 0x39c5bb
    };

    public static int[] themeIds = new int[]{
            R.style.AppTheme, THEME_NIGHT, R.style.AppTheme_2,
            R.style.AppTheme_3, R.style.AppTheme_4, R.style.AppTheme_5,
            R.style.AppTheme_6, R.style.AppTheme_7, R.style.AppTheme_8,
            R.style.AppTheme_9, R.style.AppTheme_10, R.style.AppTheme_11,
    };

    public static String[] names = new String[]{
            "原谅", "黑色", "橘红", "橘黄", "红色", "翠绿",
            "青色", "天蓝", "蓝色", "紫色", "紫红", "初音"
    };

    public void getCacheSize() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String size = CacheUtils.getTotalCacheSize();
                if (!emitter.isDisposed()) {
                    emitter.onNext(size);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String size) {
                mView.getCacheSizeSuccess(size);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    public void clearCache() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                CacheUtils.clearAllCache();
                String size = CacheUtils.getTotalCacheSize();
                WanCache.getInstance().openDiskLruCache();
                if (!emitter.isDisposed()) {
                    emitter.onNext(size);
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String size) {

                    mView.getCacheSizeSuccess(size);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }



}

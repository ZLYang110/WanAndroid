package com.zlyandroid.wanandroid.ui.mine.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.ui.mine.bean.AboutMeBean;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRankBean;
import com.zlyandroid.wanandroid.ui.mine.view.AboutMeView;
import com.zlyandroid.wanandroid.util.api.ToastUtils;
import com.zlyandroid.wanandroid.util.bitmap.BitmapUtils;
import com.zlyandroid.wanandroid.util.glide.GlideHelper;
import com.zlylib.upperdialog.listener.SimpleCallback;

import static com.zlyandroid.wanandroid.app.AppContext.getContext;


public class AboutMePresenter extends BasePresenter<AboutMeView> {


    public void saveQQQrcode(Context context) {

        GlideHelper.with(context)
                .asBitmap()
                .load(R.mipmap.weixin)
                .getBitmap(new SimpleCallback<Bitmap>() {
                    @Override
                    public void onResult(Bitmap data) {
                        if (null != BitmapUtils.saveGallery(data,  "wanAndroid_qq_qrcode_" + System.currentTimeMillis())) {
                            ToastUtils.show(context,"保存成功");
                        } else {
                            ToastUtils.show(context,"保存失败");
                        }
                    }
                });
    }
    public void saveWXQrcode(Context context) {

        GlideHelper.with(context)
                .asBitmap()
                .load(R.mipmap.weixin)
                .getBitmap(new SimpleCallback<Bitmap>() {
                    @Override
                    public void onResult(Bitmap data) {
                        if (null != BitmapUtils.saveGallery(data,  "wanAndroid_wx_qrcode_" + System.currentTimeMillis())) {
                            ToastUtils.show(context,"保存成功");
                        } else {
                            ToastUtils.show(context,"保存失败");
                        }
                    }
                });
    }
}

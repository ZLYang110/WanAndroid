package com.zlyandroid.wanandroid.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.zlyandroid.wanandroid.util.bitmap.BitmapUtils;

import java.io.File;

public class ShareUtils {


    public static void shareBitmap(Context context, Bitmap bitmap) {
        File file = BitmapUtils.saveBitmapToCache(bitmap);
        bitmap.recycle();
        if (file == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, UriUtils.getFileUri(file));
        intent = Intent.createChooser(intent, "");
        context.startActivity(intent);
    }
}

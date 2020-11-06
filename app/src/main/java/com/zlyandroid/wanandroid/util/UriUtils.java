package com.zlyandroid.wanandroid.util;

import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.zlyandroid.wanandroid.app.AppContext;

import java.io.File;

/**
 * @author zhangliyang
 * @date 2019/11/10
 */
public class UriUtils {

    public static Uri getFileUri(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(AppContext.getContext(), AppContext.getContext().getPackageName() + ".file.path.share", file);
        }
        return Uri.fromFile(file);
    }
}

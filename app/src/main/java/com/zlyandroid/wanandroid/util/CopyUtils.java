package com.zlyandroid.wanandroid.util;

import android.content.ClipboardManager;
import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zlyandroid.wanandroid.app.AppContext;
import com.zlylib.upperdialog.utils.Utils;

/**
 * 复制到剪贴板
 *
 * @author zhangliyang
 * @date 2018/8/28-下午6:58
 */
public class CopyUtils {

    public static void copyText(@NonNull String text) {
        ClipboardManager clipboardManager = (ClipboardManager) AppContext.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setText(text);
    }

    public static void copyText(@NonNull TextView textView) {
        copyText(textView.getText().toString());
    }
}

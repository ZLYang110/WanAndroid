package com.zlyandroid.wanandroid.util.log;

import android.text.TextUtils;
import android.util.Log;

import static com.zlyandroid.wanandroid.util.log.KLog.D;
import static com.zlyandroid.wanandroid.util.log.KLog.E;
import static com.zlyandroid.wanandroid.util.log.KLog.I;
import static com.zlyandroid.wanandroid.util.log.KLog.W;


/**
 * Created by zhangliyang on 15/12/11.
 */
public class KLogUtil {

    public static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || line.equals("\n") || line.equals("\t") || TextUtils.isEmpty(line.trim());
    }

    public static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }

    public static void printLine(int type, String tag, boolean isTop) {
        if (isTop) {

            switch (type) {
                case D:
                    Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
                    break;
                case E:
                    Log.e(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
                    break;
                case W:
                    Log.w(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
                    break;
                case I:
                    Log.i(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
                    break;
            }
        } else {
            switch (type) {
                case D:
                    Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
                    break;
                case E:
                    Log.e(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
                    break;
                case W:
                    Log.w(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
                    break;
                case I:
                    Log.i(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
                    break;
            }
        }
    }
}

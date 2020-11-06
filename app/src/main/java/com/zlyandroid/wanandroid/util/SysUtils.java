package com.zlyandroid.wanandroid.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Created by zhangliyang on 2020/5/2.
 *  获取系统相关信息
 */
public class SysUtils {
    private static String TAG = "SysUtils";

    /**
     * 主动获取电量
    * */
     public static int getBatteryManager(Context context){
        Log.d(TAG,"getBatteryManager--电量 =  get");
        BatteryManager batteryManager = (BatteryManager)context.getSystemService(context.BATTERY_SERVICE);
        int battery = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        Log.d(TAG,"getBatteryManager--电量 =  "+ battery);
        return battery;

    }
    /**
     * 获取手机屏幕宽高
     * */
    public static Point getDisplaySizeInfo(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point sizePoint = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //Gets the size of the display,
            display.getRealSize(sizePoint);
        } else {
            //Gets the size of the display except virtual navigation's height
            display.getSize(sizePoint);
        }
        return sizePoint;
    }


    /**
     * 获取版本号
     *
     * @param context 上下文
     *
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;

    }
    /**
     * 获取版本名称
     *
     * @param context 上下文
     *
     * @return 版本名称
     */
    public static String getVersionName(Context context) {

        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }
    /**
     * 通知系统相册更新了
     */
    public static final void notifyAlbumDataChanged(Context context, File file) {
        //通知相册更新
        Uri uri = Uri.fromFile(file);
        // 通知图库更新
        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scannerIntent);

    }
    /**
     * 调用系统相册查看图片和视频
     * context 上下文
     * path 路径
     * type 1 照片  0视频
     */
    public static final void lookAlbumDataChanged(Context context, String path,int type) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(path);     // path 是外部存储中的一个图片的路径
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Uri uri = FileProvider.getUriForFile(context, context.getPackageName()+".provider", file);
        if(type == 1){
            intent.setDataAndType(uri, "image/*");
        }else{
            intent.setDataAndType(uri, "video/*");
        }
        context.startActivity(Intent.createChooser(intent, null));

    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return Build.MODEL;
    }
}

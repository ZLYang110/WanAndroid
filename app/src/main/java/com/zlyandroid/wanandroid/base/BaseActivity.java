package com.zlyandroid.wanandroid.base;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.ui.home.view.ScanView;
import com.zlyandroid.wanandroid.util.ClickHelper;
import com.zlyandroid.wanandroid.util.ThemeUtil;
import com.zlyandroid.wanandroid.util.api.ToastUtils;
import com.zlylib.upperdialog.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;


public abstract class BaseActivity<S extends BasePresenter<ScanView>> extends RxAppCompatActivity implements ActivityPresenter, View.OnClickListener{

    private static final String TAG = "BaseActivity";

    @Override
    public Activity getActivity() {
        return this; //必须return this;
    }
    /**
     * 该Activity实例，命名为context是因为大部分方法都只需要context，写成context使用更方便
     * @warn 不能在子类中创建
     */
    protected BaseActivity<BasePresenter<ScanView>> context = null;


    private boolean isAlive = false;
    private boolean isRunning = false;

    private Unbinder unbinder;

    protected Dialog dialog;

    /***是否显示标题栏*/
    private boolean isshowtitle = true;
    /***是否显示标题栏*/
    private boolean isshowstate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化状态栏
        initWindowTitle();
        // 切换主题
        switchTheme();

        setContentView(getLayoutID());
        context = (BaseActivity<BasePresenter<ScanView>>) getActivity();

        unbinder = ButterKnife.bind(this);

        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this);
        }
        isAlive = true;
        dialog =  LoadingDialog.createLoadingDialog(this,"请稍后...");
        // 初始化控件
        initView();

    }

    public BaseActivity<BasePresenter<ScanView>> getContext() {
        return context;
    }

    /**
     * 初始化状态栏
     */
    private void initWindowTitle() {
        if (!isshowtitle) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }

        if (isshowstate) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setTheme(R.style.AppTheme);
        getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.background)));
    }
    /**
     * 是否注册事件分发，默认不绑定
     */
    protected boolean isRegisterEventBus() {
        return false;
    }



    /**
     * 点击事件，可连续点击
     */
    protected boolean onClick1(final View v){
        return false;
    }

    /**
     * 点击事件，500毫秒第一次
     */
    protected void onClick2(final View v){
    }

    /**
     * 用注解绑定点击事件时，在该方法绑定
     */
    @Override
    public void onClick(final View v) {
        if (!onClick1(v)) {
            ClickHelper.onlyFirstSameView(v, new ClickHelper.Callback() {
                @Override
                public void onClick(View view) {
                    onClick2(view);
                }
            });
        }
    }
    //启动新Activity方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



    public void hideKeyBoard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    /**
     * 中途 切换主题
     */
    public void switchTheme() {
        //直接夜间 设置退出
        int theme = ThemeUtil.getCustomTheme();
        int cur = AppCompatDelegate.getDefaultNightMode();
        //白天主题
        setTheme(theme);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    //提示 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    protected void showToast(String s) {
        ToastUtils.show(this,s);
    }

    @Override
    public final boolean isAlive() {
        return isAlive && context != null;// & ! isFinishing();导致finish，onDestroy内runUiThread不可用
    }
    @Override
    public final boolean isRunning() {
        return isRunning & isAlive();
    }


    @Override
    public void finish() {
        super.finish();//必须写在最前才能显示自定义动画
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }


    @Override
    protected void onDestroy() {
        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        unbinder.unbind();
        isAlive = false;
        isRunning = false;
        super.onDestroy();

    }


}

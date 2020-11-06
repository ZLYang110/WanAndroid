package com.zlyandroid.wanandroid.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trello.rxlifecycle3.components.support.RxFragment;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.util.ClickHelper;
import com.zlyandroid.wanandroid.util.api.ToastUtils;
import com.zlyandroid.wanandroid.util.api.dialog.DialogUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends RxFragment implements FragmentPresenter , View.OnClickListener {
    private static final String TAG = "BaseFragment";
    /**
     * 添加该Fragment的Activity
     * @warn 不能在子类中创建
     */
    protected Context context = null;
    /**
     * 该Fragment全局视图
     * @must 非abstract子类的onCreateView中return view;
     * @warn 不能在子类中创建
     */
    protected View mRootView = null;

    private boolean isAlive = false;
    private boolean isRunning = false;

    protected Unbinder unbinder;
    protected Dialog dialog;
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        if (mRootView == null) {
            mRootView = inflater.inflate(getLayoutID(),container,false);
            unbinder = ButterKnife.bind(this, mRootView);
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }

        if (isRegisterEventBus()) {
            EventBus.getDefault().register(this);
        }

        context = BaseFragment.this.getActivity();


        isAlive = true;

        dialog = DialogUtils.createLoadingDialog(getActivity(), "请稍后...");

        // 初始化控件
        initView();
        // 绑定数据
        initData();
        return mRootView;

    }
    /**
     * 是否注册事件分发，默认不绑定
     */
    protected boolean isRegisterEventBus() {
        return false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    // 实现懒加载
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisible() && mRootView != null){

        }
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public void show(String msg){
        ToastUtils.show(getActivity(),msg);
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

    @Override
    public final boolean isAlive() {
        return isAlive && context != null;// & ! isRemoving();导致finish，onDestroy内runUiThread不可用
    }
    @Override
    public final boolean isRunning() {
        return isRunning & isAlive();
    }

    @Override
    public void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isRunning = false;
    }

    /**销毁并回收内存
     * @warn 子类如果要使用这个方法内用到的变量，应重写onDestroy方法并在super.onDestroy();前操作
     */
    @Override
    public void onDestroy() {

        if (isRegisterEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        //移除view绑定
        if (unbinder != null) {
            unbinder.unbind();
        }
        isAlive = false;
        isRunning = false;
        super.onDestroy();



    }


}

package com.zlyandroid.wanandroid.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseActivity;
import com.zlyandroid.wanandroid.base.BaseMvpActivity;
import com.zlyandroid.wanandroid.core.UserInfoManager;
import com.zlyandroid.wanandroid.ui.main.MainActivity;
import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;
import com.zlyandroid.wanandroid.ui.main.presenter.DefPresenter;
import com.zlyandroid.wanandroid.ui.main.presenter.LoginPresenter;
import com.zlyandroid.wanandroid.ui.main.view.LoginView;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LaunchActivity extends AppCompatActivity {
    private static final int WAIT_TIME = 2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        setContentView(getLayoutID());
        initView();
    }


    /**
     * startActivity屏蔽物理返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
    }

    public int getLayoutID() {
        return R.layout.activity_launch;
    }

    public void initView() {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setCopyRight();
        doPreWrok();
        Observable.timer(WAIT_TIME, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> enterHome());
    }


    /**
     * 做一些启动前的工作
     */
    private void doPreWrok() {

        if (UserInfoManager.isLogin()) {
            //自动登录
            LoginBean user = UserInfoManager.getUserInfo();
            DefPresenter.getInstance().getLogin(user.getUsername(),user.getPassword());
        }
    }

    private void enterHome() {
        //LoginActivity.start(LaunchActivity.this);
        // toActivity(HomeActivity.createIntent(context));
        startActivity(new Intent(LaunchActivity.this, MainActivity.class));
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    //自动续命copyright
    private void setCopyRight() {
        int year = 2016;
        int yearNow = Calendar.getInstance().get(Calendar.YEAR);

        if (year < yearNow) {
            year = yearNow;
        }
        ((TextView) findViewById(R.id.copyright))
                .setText("©2016-" + year + " lsuplus.top");
    }

}

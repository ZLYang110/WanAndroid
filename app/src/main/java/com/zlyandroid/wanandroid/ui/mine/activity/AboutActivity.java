package com.zlyandroid.wanandroid.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseActivity;
import com.zlyandroid.wanandroid.util.AppInfoUtils;
import com.zlyandroid.wanandroid.util.IntentUtils;
import com.zlyandroid.wanandroid.widget.LogoAnimView;

import butterknife.BindView;
import butterknife.OnClick;


public class AboutActivity extends BaseActivity {

    @BindView(R.id.tv_version_name)
    TextView tv_version_name;
    @BindView(R.id.tv_web)
    TextView tv_web;
    @BindView(R.id.tv_about)
    TextView tv_about;
    @BindView(R.id.tv_github)
    TextView tv_github;
    @BindView(R.id.lav)
    LogoAnimView lav;

    public static void start(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }



    @Override
    public int getLayoutID() {
        return R.layout.activity_about;
    }

    @Override
    public void initView() {
        tv_version_name.setText(String.format("V%s(%d)",
                AppInfoUtils.getVersionName(), AppInfoUtils.getVersionCode()));
    }


    @Override
    public void initData() {

    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        lav.randomBlink();
    }

    @OnClick({
            R.id.ll_web, R.id.ll_about, R.id.ll_github
    })
    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void onClick2(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.ll_web:
                IntentUtils.openBrowser(getContext(), "https://www.wanandroid.com");

                break;
            case R.id.ll_about:
                IntentUtils.openBrowser(getContext(), "https://www.wanandroid.com/about/");

                break;
            case R.id.ll_github:
                IntentUtils.openBrowser(getContext(), "https://github.com/ZLYang110");

                break;
        }
    }
}

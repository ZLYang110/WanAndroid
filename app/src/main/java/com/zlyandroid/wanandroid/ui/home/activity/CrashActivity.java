package com.zlyandroid.wanandroid.ui.home.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.app.ProApplication;
import com.zlyandroid.wanandroid.util.CopyUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import per.goweii.statusbarcompat.StatusBarCompat;

/**
 * @author zhangliyang
 * @date 2020/7/20
 */
public class CrashActivity extends AppCompatActivity {

    @BindView(R.id.tv_log)
    TextView tv_log;
    @BindView(R.id.tv_show_log)
    TextView tv_show_log;
    @BindView(R.id.iv_bug)
    ImageView iv_bug;
    @BindView(R.id.sv)
    ScrollView sv;

    private Unbinder mUnbinder = null;
    private CaocConfig mCaocConfig;

    private boolean isLogShown = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCaocConfig = CustomActivityOnCrash.getConfigFromIntent(getIntent());
        if (mCaocConfig == null) {
            finish();
            return;
        }
        ProApplication.initDarkMode();
        StatusBarCompat.setIconMode(this, !ProApplication.isDarkMode());
        setContentView(R.layout.activity_crash);
        mUnbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @OnClick({R.id.tv_restart, R.id.tv_exit, R.id.tv_show_log})
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.tv_show_log:
                String log = tv_log.getText().toString();
                if (TextUtils.isEmpty(log)) {
                    isLogShown = true;
                    tv_show_log.setText("复制日志");
                    tv_show_log.setTextColor(getResources().getColor(R.color.text_main));
                    iv_bug.setVisibility(View.GONE);
                    sv.setVisibility(View.VISIBLE);
                    tv_log.setText(CustomActivityOnCrash.getAllErrorDetailsFromIntent(this, getIntent()));
                } else {
                    CopyUtils.copyText(log);
                    tv_show_log.setText("日志已复制");
                    tv_show_log.setTextColor(getResources().getColor(R.color.text_third));
                    tv_show_log.setEnabled(false);
                }
                break;
            case R.id.tv_exit:
                CustomActivityOnCrash.closeApplication(this, mCaocConfig);
                break;
            case R.id.tv_restart:
                CustomActivityOnCrash.restartApplication(this, mCaocConfig);
                break;
        }
    }
}

package com.zlyandroid.wanandroid.ui.mine.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.app.ProApplication;
import com.zlyandroid.wanandroid.base.BaseMvpActivity;
import com.zlyandroid.wanandroid.core.UserInfoManager;
import com.zlyandroid.wanandroid.event.LoginEvent;
import com.zlyandroid.wanandroid.event.SettingChangeEvent;
import com.zlyandroid.wanandroid.https.RDownLoad;
import com.zlyandroid.wanandroid.https.callback.Download;
import com.zlyandroid.wanandroid.https.download.DownloadCallback;
import com.zlyandroid.wanandroid.ui.mine.presenter.SettingPresenter;
import com.zlyandroid.wanandroid.ui.mine.view.SettingView;
import com.zlyandroid.wanandroid.ui.web.WebViewActivity;
import com.zlyandroid.wanandroid.util.AppInfoUtils;
import com.zlyandroid.wanandroid.util.LogUtil;
import com.zlyandroid.wanandroid.util.PermissionUtils;
import com.zlyandroid.wanandroid.util.RvAnimUtils;
import com.zlyandroid.wanandroid.util.SettingUtils;
import com.zlyandroid.wanandroid.util.ThemeUtil;
import com.zlyandroid.wanandroid.util.api.ToastUtils;
import com.zlyandroid.wanandroid.util.api.dialog.DownloadDialog;
import com.zlyandroid.wanandroid.util.api.dialog.ListDialog;
import com.zlyandroid.wanandroid.util.api.dialog.UpdateDialog;
import com.zlyandroid.wanandroid.widget.MyCircleView;
import com.zlylib.mypermissionlib.RequestListener;
import com.zlylib.mypermissionlib.RuntimeRequester;
import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;
import com.zlylib.upperdialog.TipDialog;
import com.zlylib.upperdialog.listener.SimpleCallback;

import java.io.File;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class SettingActivity extends BaseMvpActivity<SettingPresenter> implements SettingView, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.abc)
    ActionBarCommon abc;
    @BindView(R.id.sc_dark_theme)
    SwitchCompat sc_dark_theme;
    @BindView(R.id.sc_show_read_later)
    SwitchCompat sc_show_read_later;
    @BindView(R.id.sc_show_read_record)
    SwitchCompat sc_show_read_record;
    @BindView(R.id.sc_show_top)
    SwitchCompat sc_show_top;
    @BindView(R.id.sc_show_banner)
    SwitchCompat sc_show_banner;
    @BindView(R.id.sc_hide_about_me)
    SwitchCompat sc_hide_about_me;
    TextView tv_intercept_host;
    @BindView(R.id.tv_rv_anim)
    TextView tv_rv_anim;
    @BindView(R.id.tv_cache)
    TextView tv_cache;
    @BindView(R.id.tv_has_update)
    TextView tv_has_update;
    @BindView(R.id.tv_curr_version)
    TextView tv_curr_version;
    @BindView(R.id.ll_logout)
    LinearLayout ll_logout;


    public static final int requestCode = 32;
    public static final int THEME_DEFAULT = R.style.AppTheme;
    public static final int THEME_NIGHT = 1;

    @BindView(R.id.gv_theme_commons_colors)
    GridView gridView;



    private int currentSelect = 0;
    private int currentTheme = THEME_DEFAULT;

    private ColorAdapter adapter;

    /**获取启动UserActivity的intent
     * @param context
     * @return
     */
    public static Intent createIntent(Context context) {
        return new Intent(context, SettingActivity.class);
    }



    public static void start(Activity activity) {
        Intent intent = new Intent(activity, SettingActivity.class);
        activity.startActivityForResult(intent,requestCode);
        activity.overridePendingTransition(R.anim.swipeback_activity_open_right_in,
                R.anim.swipeback_activity_open_left_out);
    }
    @Override
    protected SettingPresenter createPresenter() {
        return  new SettingPresenter();
    }
    @Override
    public int getLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    public void initData() {
        tv_has_update.setText("");
        tv_curr_version.setText("当前版本" + AppInfoUtils.getVersionName());
        mShowTop = SettingUtils.getInstance().isShowTop();
        sc_show_top.setChecked(mShowTop);
        mShowBanner = SettingUtils.getInstance().isShowBanner();
        sc_show_banner.setChecked(mShowBanner);
        mShowReadLater = SettingUtils.getInstance().isShowReadLater();
        sc_show_read_later.setChecked(mShowReadLater);
        mShowReadRecord = SettingUtils.getInstance().isShowReadRecord();
        sc_show_read_record.setChecked(mShowReadRecord);
        mHideAboutMe = SettingUtils.getInstance().isHideAboutMe();
        sc_hide_about_me.setChecked(mHideAboutMe);
        mRvAnim = SettingUtils.getInstance().getRvAnim();
        tv_rv_anim.setText(RvAnimUtils.getName(mRvAnim));
        mPresenter.getCacheSize();
    }



    @Override
    public void initView() {
        abc.setOnLeftIconClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        abc.setOnRightIconClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                onChooseTheme();
            }
        });
        adapter = new ColorAdapter();
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        currentTheme = ThemeUtil.getCustomTheme();
        currentSelect = getSelect();

        if (UserInfoManager.isLogin()) {
            ll_logout.setVisibility(View.VISIBLE);
        } else {
            ll_logout.setVisibility(View.GONE);
        }
    }

    /**
     * 点击确认主题
     */
    private void onChooseTheme() {
        boolean isChange = false;
        int curr = AppCompatDelegate.getDefaultNightMode();
        int to = curr;

        // 选择的主题与之前的主题不同
        if (ThemeUtil.getCustomTheme() != mPresenter.themeIds[currentSelect]) {
            ThemeUtil.setCustomTheme(mPresenter.themeIds[currentSelect]);
            isChange = true;
            if (mPresenter.themeIds[currentSelect] == THEME_NIGHT) {
                // 选择的是夜间模式
                to = AppCompatDelegate.MODE_NIGHT_YES;
            } else {
                to = AppCompatDelegate.MODE_NIGHT_NO;
            }
        }
        if (curr != to) {
            AppCompatDelegate.setDefaultNightMode(to);
            isChange = true;
        }

        if (isChange) {
            showToast("已更改主题");
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            LogUtil.d("ThemeActivity savedInstanceState:" + savedInstanceState.getInt("position", 0));
            currentSelect = savedInstanceState.getInt("position", 0);
        }


    }



    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (currentSelect == position) return;
        currentSelect = position;
        adapter.notifyDataSetChanged();
        changeTheme(position);
        
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", currentSelect);
    }


    public int getSelect() {
        for (int i = 0; i < mPresenter.themeIds.length; i++) {
            if (currentTheme == mPresenter.themeIds[i]) {
                return i;
            }
        }
        return 0;
    }

    private void changeTheme(int position) {
        abc.setBackgroundColor(0xff000000 | mPresenter.colors[position]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0xff000000 | mPresenter.colorsDark[position]);
            //底部导航栏
        }
        if (mPresenter.themeIds[position] == THEME_NIGHT) {
            //window背景颜色
            getWindow().setBackgroundDrawable(new ColorDrawable(0xff333333));
        } else {
            getWindow().setBackgroundDrawable(new ColorDrawable(0xfff5f5f5));
        }
    }




    @OnCheckedChanged({
            R.id.sc_system_theme,
            R.id.sc_dark_theme,
            R.id.sc_show_read_later,
            R.id.sc_show_read_record,
            R.id.sc_show_top,
            R.id.sc_show_banner,
            R.id.sc_hide_about_me,

    })
    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        switch (v.getId()){
            case R.id.sc_system_theme://跟随系统暗黑
                break;
            case R.id.sc_dark_theme://暗黑
                SettingUtils.getInstance().setDarkTheme(isChecked);
                ProApplication.initDarkMode();
                break;
            case R.id.sc_show_read_later://稍后阅读
                SettingUtils.getInstance().setShowReadLater(isChecked);
                break;
            case R.id.sc_show_read_record://阅读历史
                SettingUtils.getInstance().setShowReadRecord(isChecked);
                break;
            case R.id.sc_show_top://显示置顶
                SettingUtils.getInstance().setShowTop(isChecked);
                break;
            case R.id.sc_show_banner://显示轮番
                SettingUtils.getInstance().setShowBanner(isChecked);
                break;
            case R.id.sc_hide_about_me://隐藏关于作者
                SettingUtils.getInstance().setHideAboutMe(isChecked);
                break;

        }
    }

    @OnClick({
            R.id.ll_rv_anim, R.id.ll_update,
            R.id.ll_cache, R.id.ll_about, R.id.ll_privacy_policy, R.id.ll_logout
    })
    @Override
    public void onClick(View v) {
        super.onClick(v);
    }



    @Override
    protected void onClick2(View v) {
        switch (v.getId()) {
            /*    File file2 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "QQ111111" + ".apk");
                Download bean1 = new Download();
                bean1.setLocalUrl(file2.getAbsolutePath());
                bean1.setServerUrl("https://download.xloong.com/app/ARFusionMedia_20200508__v0.0.2.apk");
                  bean1.setCallback(  new DownloadCallback() {
                    @Override
                    public void onProgress(Download.State state, long currentSize, long totalSize, float progress) {
                        LogUtil.d( "download progress currentSize:" + currentSize + "  totalSize:" + totalSize);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d( "download progress currentSize:失败 totalSize:" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(Download object) {
                        LogUtil.d( "download progress currentSize:成功 totalSize:" + object.getServerUrl());
                        showToast( "成功");
                    }
                });
                RDownLoad.get().startDownload(bean1);//开始下载*/

            case R.id.ll_rv_anim://列表动画

                ListDialog.with(SettingActivity.this)
                        .cancelable(true)
//                        .title("列表动画")
                        .datas(RvAnimUtils.getName(RvAnimUtils.RvAnim.NONE),
                                RvAnimUtils.getName(RvAnimUtils.RvAnim.ALPHAIN),
                                RvAnimUtils.getName(RvAnimUtils.RvAnim.SCALEIN),
                                RvAnimUtils.getName(RvAnimUtils.RvAnim.SLIDEIN_BOTTOM),
                                RvAnimUtils.getName(RvAnimUtils.RvAnim.SLIDEIN_LEFT),
                                RvAnimUtils.getName(RvAnimUtils.RvAnim.SLIDEIN_RIGHT))
                        .currSelectPos(SettingUtils.getInstance().getRvAnim())
                        .listener(new ListDialog.OnItemSelectedListener() {
                            @Override
                            public void onSelect(String data, int pos) {
                                LogUtil.d( RvAnimUtils.getName(pos));
                                tv_rv_anim.setText(RvAnimUtils.getName(pos));
                                SettingUtils.getInstance().setRvAnim(pos);
                            }
                        })
                        .show();
                break;
            case R.id.ll_update://更新

                UpdateDialog.with(SettingActivity.this)
                        .setUrl("地址")
                        .setUrlBackup("dd")
                        .setVersionCode(1)
                        .setVersionName("name")
                        .setForce(false)
                        .setDescription("213123213")
                        .setTime(null)
                        .setOnUpdateListener(new UpdateDialog.OnUpdateListener() {
                            @Override
                            public void onDownload(String url, String urlBackup, boolean isForce) {
                                download("",null,"",true);
                            }

                            @Override
                            public void onIgnore(int versionCode) {
                            }
                        })
                        .show();

                break;
            case R.id.ll_cache://清除缓存
                TipDialog.with(getActivity())
                         .message("确定要清除缓存吗？")
                         .onYes(new SimpleCallback<Void>() {
                    @Override
                    public void onResult(Void data) {
                        mPresenter.clearCache();
                    }
                }).show();

                break;
            case R.id.ll_about://关于
                AboutActivity.start(getContext());
                break;
            case R.id.ll_privacy_policy:
                WebViewActivity.start( getActivity(),"wanAndroid","file:///android_asset/privacy_policy.html");
                break;
            case R.id.ll_logout://退出登录
                TipDialog.with(getActivity())
                        .message("确定要退出登录吗？")
                        .onYes(new SimpleCallback<Void>() {
                            @Override
                            public void onResult(Void data) {
                                UserInfoManager.logout();
                                new LoginEvent(false).post();
                                finish();
                            }
                        })
                        .show();
                break;

        }
    }
    private static final int REQ_CODE_PERMISSION = 1;
    private RuntimeRequester mRuntimeRequester;
    private void download(final String versionName, final String url, final String urlBackup, final boolean isForce) {
        mRuntimeRequester = PermissionUtils.request(new RequestListener() {
            @Override
            public void onSuccess() {
                DownloadDialog.with(SettingActivity.this, isForce, "https://github.com/ZLYang110/MyPermission/raw/master/app/release/app-release.apk");
            }

            @Override
            public void onFailed() {

            }
        }, SettingActivity.this, REQ_CODE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mRuntimeRequester != null) {
            mRuntimeRequester.onActivityResult(requestCode);
        }
    }

    @Override
    public void getCacheSizeSuccess(String size) {
        tv_cache.setText(size);
    }

    private class ColorAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPresenter.colors.length;
        }

        @Override
        public Object getItem(int position) {
            return mPresenter.colors[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_color, null);
            MyCircleView circleView = convertView.findViewById(R.id.color);
            circleView.setColor(mPresenter.colors[position]);
            circleView.setSelect(position == currentSelect);
            ((TextView) convertView.findViewById(R.id.name)).setText(mPresenter.names[position]);
            return convertView;
        }
    }
    private boolean mShowTop;
    private boolean mShowBanner;
    private boolean mShowReadLater;
    private boolean mShowReadRecord;
    private boolean mHideAboutMe;
    private boolean mHideOpen;
    private int mRvAnim;
    private int mUrlIntercept;
    private void postSettingChangedEvent() {
        boolean showTopChanged = mShowTop != SettingUtils.getInstance().isShowTop();
        boolean showBannerChanged = mShowBanner != SettingUtils.getInstance().isShowBanner();
        boolean showReadLaterChanged = mShowReadLater != SettingUtils.getInstance().isShowReadLater();
        boolean showReadRecordChanged = mShowReadRecord != SettingUtils.getInstance().isShowReadRecord();
        boolean hideAboutMeChanged = mHideAboutMe != SettingUtils.getInstance().isHideAboutMe();
        boolean rvAnimChanged = mRvAnim != SettingUtils.getInstance().getRvAnim();
        if (showReadLaterChanged || showReadRecordChanged || showTopChanged || showBannerChanged ||
                hideAboutMeChanged || rvAnimChanged ) {
            SettingChangeEvent event = new SettingChangeEvent();
            event.setShowTopChanged(showTopChanged);
            event.setShowBannerChanged(showBannerChanged);
            event.setShowReadLaterChanged(showReadLaterChanged);
            event.setShowReadRecordChanged(showReadRecordChanged);
            event.setHideAboutMeChanged(hideAboutMeChanged);
            event.setRvAnimChanged(rvAnimChanged);
            event.post();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        postSettingChangedEvent();
    }
}

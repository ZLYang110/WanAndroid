package com.zlyandroid.wanandroid.ui.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpFragment;
import com.zlyandroid.wanandroid.core.UserInfoManager;
import com.zlyandroid.wanandroid.event.LoginEvent;
import com.zlyandroid.wanandroid.event.SettingChangeEvent;
import com.zlyandroid.wanandroid.ui.main.LoginActivity;
import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;
import com.zlyandroid.wanandroid.ui.mine.activity.AboutMeActivity;
import com.zlyandroid.wanandroid.ui.mine.activity.CoinActivity;
import com.zlyandroid.wanandroid.ui.mine.activity.CoinRankActivity;
import com.zlyandroid.wanandroid.ui.mine.activity.CollectionActivity;
import com.zlyandroid.wanandroid.ui.mine.activity.MineShareActivity;
import com.zlyandroid.wanandroid.ui.mine.activity.ReadLaterActivity;
import com.zlyandroid.wanandroid.ui.mine.activity.ReadRecordActivity;
import com.zlyandroid.wanandroid.ui.mine.activity.SettingActivity;
import com.zlyandroid.wanandroid.ui.mine.activity.TodoActivity;
import com.zlyandroid.wanandroid.ui.mine.bean.UserInfoBean;
import com.zlyandroid.wanandroid.ui.mine.presenter.MinePresenter;
import com.zlyandroid.wanandroid.ui.mine.view.MineView;
import com.zlyandroid.wanandroid.util.ImageLoader;
import com.zlyandroid.wanandroid.util.LogUtil;
import com.zlyandroid.wanandroid.util.PermissionUtils;
import com.zlyandroid.wanandroid.util.PictureSelector;
import com.zlyandroid.wanandroid.util.SettingUtils;
import com.zlyandroid.wanandroid.util.SmartRefreshUtils;
import com.zlyandroid.wanandroid.util.UserInfoUtils;
import com.zlyandroid.wanandroid.util.file.CacheUtils;
import com.zlyandroid.wanandroid.widget.CircleImageView;
import com.zlylib.mypermissionlib.RequestListener;
import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;
import com.zlylib.upperdialog.listener.SimpleListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class MineFragment extends BaseMvpFragment<MinePresenter> implements MineView {

    private static final int REQUEST_CODE_PERMISSION_ALBUM = 1;
    private static final int REQUEST_CODE_SELECT_USER_ICON = 2;
    private static final int REQUEST_CODE_CROP_USER_ICON = 3;
    private static final int REQUEST_CODE_SELECT_BG = 4;
    private static final int REQUEST_CODE_CROP_BG = 5;

    @BindView(R.id.abc)
    ActionBarCommon abc;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.nsv)
    NestedScrollView nsv;
    @BindView(R.id.iv_blur)
    ImageView iv_blur;


    @BindView(R.id.rl_mine_header)
    LinearLayout rl_mine_header;
    @BindView(R.id.ci_mine_user_img)//头像
    CircleImageView ciMineUserImg;
    @BindView(R.id.tv_mine_user_name)//昵称
    TextView tvMineUserName;
    @BindView(R.id.ll_user_id)//ID
    LinearLayout llUserId;
    @BindView(R.id.tv_user_id)//ID
    TextView tvUserId;
    @BindView(R.id.ll_user_level_ranking)
    LinearLayout ll_user_level_ranking;
    @BindView(R.id.tv_user_level)
    TextView tv_user_level;
    @BindView(R.id.tv_user_ranking)
    TextView tv_user_ranking;
    @BindView(R.id.ll_coin)//我的积分
    LinearLayout ll_coin;
    @BindView(R.id.tv_coin)//我的积分
    TextView tv_coin;

    @BindView(R.id.ll_todo)//TODO
    LinearLayout ll_todo;
    @BindView(R.id.ll_share)//我的分享
    LinearLayout ll_share;
    @BindView(R.id.ll_collect)//我的收藏
    LinearLayout ll_collect;
    @BindView(R.id.ll_read_later)//稍后阅读
    LinearLayout ll_read_later;
    @BindView(R.id.ll_read_record)//阅读历史
    LinearLayout ll_read_record;
    @BindView(R.id.ll_open)//开源项目
    LinearLayout ll_open;
    @BindView(R.id.ll_about_me)//ll_about_me
    LinearLayout ll_about_me;
    @BindView(R.id.ll_setting) //系统设置
    LinearLayout ll_setting;




    private SmartRefreshUtils mSmartRefreshUtils;

    public static MineFragment newInstance() {
        return new MineFragment();
    }
    @Override
    protected MinePresenter createPresenter() {
        return new MinePresenter();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        if (isDetached()) {
            return;
        }
        changeUserInfo();
        loadUserInfo();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChangeEvent(SettingChangeEvent event) {
        if (isDetached()) {
            return;
        }
        if (event.isShowReadLaterChanged() || event.isShowReadRecordChanged() || event.isHideAboutMeChanged() || event.isHideOpenChanged()) {
            changeMenuVisible();
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
    @Override
    public int getLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView() {
        abc.setOnRightIconClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                CoinRankActivity.start(getContext());
            }
        });
        ciMineUserImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.image_placeholder));
        tvMineUserName.setText("点击头像登陆");
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (nsv == null || rl_mine_header == null) return;
                setIvBlurHeight(rl_mine_header.getMeasuredHeight() - scrollY);
            }
        });
            srl.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                if (nsv == null || rl_mine_header == null) return;
                setIvBlurHeight(rl_mine_header.getMeasuredHeight() - nsv.getScrollY() + offset);
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {
            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {
            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
            }

            @Override
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {
                setIvBlurHeight(rl_mine_header.getMeasuredHeight() - nsv.getScrollY() - offset);
            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {
            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {
            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            }

            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
            }
        });
        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        changeMenuVisible();
        changeUserInfo();
    }

    private void setIvBlurHeight(int h) {
        if (iv_blur == null) return;
        if (h >= 0) {
            iv_blur.getLayoutParams().height = h;
        } else {
            iv_blur.getLayoutParams().height = 0;
        }
        iv_blur.requestLayout();
    }
    @Override
    public void initData() {
        loadUserInfo();
    }

    private void loadUserInfo() {
        if (UserInfoManager.isLogin()) {
            mPresenter.getUserInfo();
        }
    }

    @OnClick({R.id.rl_mine_header,R.id.ci_mine_user_img,R.id.tv_mine_user_name,
            R.id.ll_coin,R.id.ll_share,R.id.ll_collect,R.id.ll_read_later,
            R.id.ll_read_record,R.id.ll_open,R.id.ll_about_me,R.id.ll_setting ,R.id.ll_todo
    })
    @Override
    protected void onClick2(View v) {
        super.onClick2(v);
        switch (v.getId()) {
            default:
                break;

            case R.id.rl_mine_header:
                UserInfoManager.doIfLogin(getActivity());
                break;
            case R.id.ci_mine_user_img:
                if (UserInfoManager.doIfLogin(getActivity())) {
                    requestAlbumPermission(new SimpleListener() {
                        @Override
                        public void onResult() {
                            PictureSelector.select(MineFragment.this, REQUEST_CODE_SELECT_USER_ICON);
                        }
                    });
                }
                break;
            case R.id.tv_mine_user_name:
                UserInfoManager.doIfLogin(getActivity());
                break;
            case R.id.ll_coin:  // 我的积分
                if (UserInfoManager.doIfLogin(getContext())) {
                    CoinActivity.start(getContext());
                }
                break;
            case R.id.ll_todo:  // todo
                TodoActivity.start(getActivity());
                break;
            case R.id.ll_share:  // 我的分享
                if (UserInfoManager.doIfLogin(getContext())) {
                    MineShareActivity.start(getContext());
                }
                break;
            case R.id.ll_collect:  // 我的收藏
                if (UserInfoManager.doIfLogin(getContext())) {
                    CollectionActivity.start(getActivity());
                }
                break;
            case R.id.ll_read_later:  // 稍后阅读
                ReadLaterActivity.start(getActivity());
                break;
            case R.id.ll_read_record:  // 阅读历史
                ReadRecordActivity.start(getActivity());
                break;

            case R.id.ll_about_me:  // 关于作者
                AboutMeActivity.start(getActivity());
                break;
            case R.id.ll_setting:  // 系统设置
                SettingActivity.start(getActivity());
                break;

        }
    }
    @Override
    protected boolean onClick1(View v) {
        return super.onClick1(v);
    }


    private void changeMenuVisible() {
        SettingUtils settingUtils = SettingUtils.getInstance();
        if (settingUtils.isShowReadLater()) {
            ll_read_later.setVisibility(View.VISIBLE);
        } else {
            ll_read_later.setVisibility(View.GONE);
        }
        if (settingUtils.isShowReadRecord()) {
            ll_read_record.setVisibility(View.VISIBLE);
        } else {
            ll_read_record.setVisibility(View.GONE);
        }
        if (!settingUtils.isHideAboutMe()) {
            ll_about_me.setVisibility(View.VISIBLE);
        } else {
            ll_about_me.setVisibility(View.GONE);
        }
        if (!settingUtils.isHideOpen()) {
            ll_open.setVisibility(View.VISIBLE);
        } else {
            ll_open.setVisibility(View.GONE);
        }
    }
    private void changeUserInfo() {
        if (UserInfoManager.isLogin()) {
            LoginBean bean = UserInfoManager.getUserInfo();

             File file=new File(UserInfoUtils.getInstance().getIcon());
            LogUtil.d(UserInfoUtils.getInstance().getIcon()+"---"+bean.getUsername()+"--"+file.exists());
           // Glide.with(getActivity()).load(R.mipmap.image_placeholder).into(ciMineUserImg);
            //ImageLoader.userIcon(ciMineUserImg, R.mipmap.image_placeholder);
            if(!UserInfoUtils.getInstance().getIcon().isEmpty()){
                ImageLoader.userIcon(ciMineUserImg, UserInfoUtils.getInstance().getIcon());

            }

            ImageLoader.userBlur(iv_blur, UserInfoUtils.getInstance().getBg());
            tvMineUserName.setText(bean.getUsername());
            llUserId.setVisibility(View.VISIBLE);
            tvUserId.setText(bean.getId() + "");
            tv_coin.setText("");
        } else {
            ImageLoader.userIcon(ciMineUserImg, R.mipmap.image_placeholder);
            //iv_blur.setImageResource(R.color.transparent);
            tvMineUserName.setText("去登陆");
            llUserId.setVisibility(View.GONE);
            tvUserId.setText("-----");
            tv_coin.setText("");
            //
        }
    }

    private void requestAlbumPermission(@NonNull SimpleListener listener) {
        PermissionUtils.request(new RequestListener() {
            @Override
            public void onSuccess() {
                listener.onResult();
            }

            @Override
            public void onFailed() {
                show("获取存储设备读取权限失败");
            }
        }, getActivity(), REQUEST_CODE_PERMISSION_ALBUM, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            default:
                break;
            case REQUEST_CODE_SELECT_USER_ICON:
                Uri userIconUri = PictureSelector.result(resultCode, data);
                if (userIconUri != null) {
                    File sourceFile = PictureSelector.getFileFormUri(getContext(), userIconUri);
                    if (sourceFile != null) {
                        File file = new File(CacheUtils.getCacheDir(), "user_info");

                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        File clipFile = new File(file, "user_icon.png");
                        try {
                            clipFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        PictureSelector.crop(MineFragment.this, sourceFile, clipFile, REQUEST_CODE_CROP_USER_ICON);
                    }
                }
                break;
            case REQUEST_CODE_SELECT_BG:
                Uri bgUri = PictureSelector.result(resultCode, data);
                if (bgUri != null) {
                    File sourceFile = PictureSelector.getFileFormUri(getContext(), bgUri);
                    if (sourceFile != null) {
                        File file = new File(CacheUtils.getCacheDir(), "user_info");
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        File clipFile = new File(file, "user_bg.png");
                        try {
                            clipFile.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        PictureSelector.crop(MineFragment.this, sourceFile, clipFile, REQUEST_CODE_CROP_BG);
                    }
                }
                break;
            case REQUEST_CODE_CROP_USER_ICON:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(CacheUtils.getCacheDir(), "user_info");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File clipFile = new File(file, "user_icon.png");
                    if (clipFile.exists()) {
                        String path = clipFile.getAbsolutePath();
                        UserInfoUtils.getInstance().setIcon(path);
                        UserInfoUtils.getInstance().setBg(path);
                        ImageLoader.userIcon(ciMineUserImg, path);
                        ImageLoader.userBlur(iv_blur, path);
                    }
                }
                break;
            case REQUEST_CODE_CROP_BG:
                if (resultCode == Activity.RESULT_OK) {
                    File file = new File(CacheUtils.getCacheDir(), "user_info");
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File clipFile = new File(file, "user_bg.png");
                    if (clipFile.exists()) {
                        String path = clipFile.getAbsolutePath();
                        UserInfoUtils.getInstance().setBg(path);
                        ImageLoader.userBlur(iv_blur, path);
                    }
                }
                break;
        }
    }

    @Override
    public void getUserInfoSuccess(int code, UserInfoBean data) {
        mSmartRefreshUtils.success();
        tv_coin.setText(data.getCoinCount() + "");
        ll_user_level_ranking.setVisibility(View.VISIBLE);
        tv_user_level.setText(data.getLevel() + "");
        tv_user_ranking.setText(data.getRank() + "");
    }

    @Override
    public void getUserInfoFail(int code, String msg) {
        mSmartRefreshUtils.fail();
        show(msg);
        tv_coin.setText("");
        tv_user_level.setText("--");
        tv_user_ranking.setText("--");
    }
}

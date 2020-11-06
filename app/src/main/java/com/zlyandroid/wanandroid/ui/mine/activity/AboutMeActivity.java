package com.zlyandroid.wanandroid.ui.mine.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.daimajia.swipe.SwipeLayout;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseActivity;
import com.zlyandroid.wanandroid.base.BaseMvpActivity;
import com.zlyandroid.wanandroid.ui.mine.bean.AboutMeBean;
import com.zlyandroid.wanandroid.ui.mine.presenter.AboutMePresenter;
import com.zlyandroid.wanandroid.ui.mine.presenter.SettingPresenter;
import com.zlyandroid.wanandroid.ui.mine.view.AboutMeView;
import com.zlyandroid.wanandroid.util.CopyUtils;
import com.zlyandroid.wanandroid.util.ImageLoader;
import com.zlyandroid.wanandroid.util.PermissionUtils;
import com.zlyandroid.wanandroid.util.glide.GlideHelper;
import com.zlyandroid.wanandroid.widget.PercentImageView;
import com.zlylib.mypermissionlib.RequestListener;
import com.zlylib.mypermissionlib.RuntimeRequester;
import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;
import com.zlylib.upperdialog.listener.SimpleCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;


public class AboutMeActivity extends BaseMvpActivity<AboutMePresenter> implements AboutMeView {

    private static final int REQUEST_CODE_PERMISSION = 1;

    @BindView(R.id.abc)
    ActionBarCommon abc;
    @BindView(R.id.sl)
    SwipeLayout sl;
    @BindView(R.id.iv_blur)
    ImageView iv_blur;
    @BindView(R.id.civ_icon)
    ImageView civ_icon;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_sign)
    TextView tv_sign;
    @BindView(R.id.ll_github)
    LinearLayout ll_github;
    @BindView(R.id.tv_github)
    TextView tv_github;


    @BindView(R.id.rl_info)
    RelativeLayout rl_info;
    @BindView(R.id.rl_reward)
    RelativeLayout rl_reward;
    @BindView(R.id.piv_qq_qrcode)
    PercentImageView piv_qq_qrcode;
    @BindView(R.id.piv_wx_qrcode)
    PercentImageView piv_wx_qrcode;

    private RuntimeRequester mRuntimeRequester;

    public static void start(Context context) {
        Intent intent = new Intent(context, AboutMeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected AboutMePresenter createPresenter() {
        return  new AboutMePresenter();
    }


    @Override
    public int getLayoutID() {
        return R.layout.activity_about_me;
    }

    @Override
    public void initView() {

        changeVisible(View.INVISIBLE, civ_icon, tv_name, tv_sign);
    }

    @Override
    public void initData() {
        piv_qq_qrcode.setImageResource(R.mipmap.weixin);
        piv_wx_qrcode.setImageResource(R.mipmap.weixin);
        GlideHelper.with(getContext())
                .asBitmap()
                .load(R.mipmap.head)
                .getBitmap(new SimpleCallback<Bitmap>() {
                    @Override
                    public void onResult(Bitmap bitmap) {
                        ImageLoader.userIcon(civ_icon, R.mipmap.head);
                        ImageLoader.userBlur(iv_blur, R.mipmap.head);
                        iv_blur.setAlpha(0F);
                        iv_blur.post(new Runnable() {
                            @Override
                            public void run() {
                                changeViewAlpha(iv_blur, 0, 1, 600);
                                changeViewSize(iv_blur, 2, 1, 1000);
                            }
                        });
                    }
                });

        List<View> targets = new ArrayList<>();
        targets.add(civ_icon);
        tv_name.setText("ZLYang110");
        targets.add(tv_name);
        tv_sign.setText("踏实的做，机会总会有的");
        targets.add(tv_sign);
        tv_github.setText(" https://github.com/ZLYang110");
        targets.add(ll_github);
        civ_icon.post(new Runnable() {
            @Override
            public void run() {
                changeViewSize(civ_icon, 0, 1, 300);
                doDelayShowAnim(800, 60, targets.toArray(new View[0]));
            }
        });
    }



    @OnClick({
            R.id.ll_github
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
            case R.id.ll_github:
                CopyUtils.copyText("https://github.com/ZLYang110");
                showToast("已复制");
                break;

        }
    }
    @OnLongClick({R.id.piv_qq_qrcode, R.id.piv_wx_qrcode})
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.piv_qq_qrcode:
                mRuntimeRequester = PermissionUtils.request(new RequestListener() {
                    @Override
                    public void onSuccess() {
                        mPresenter.saveQQQrcode(getActivity());
                    }

                    @Override
                    public void onFailed() {
                    }
                }, getContext(), REQUEST_CODE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
            case R.id.piv_wx_qrcode:
                mRuntimeRequester = PermissionUtils.request(new RequestListener() {
                    @Override
                    public void onSuccess() {
                        mPresenter.saveWXQrcode(getActivity());
                    }

                    @Override
                    public void onFailed() {
                    }
                }, getContext(), REQUEST_CODE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mRuntimeRequester != null) {
            mRuntimeRequester.onActivityResult(requestCode);
        }
    }

    private void changeVisible(int visible, View... views) {
        for (View view : views) {
            view.setVisibility(visible);
        }
    }

    private void changeViewSize(final View target, float from, float to, long dur) {
        final ValueAnimator animator = ValueAnimator.ofFloat(from, to);
        animator.setDuration(dur);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (target == null) {
                    animator.cancel();
                    return;
                }
                float f = (float) animator.getAnimatedValue();
                target.setScaleX(f);
                target.setScaleY(f);
            }
        });
        animator.start();
    }

    private void changeViewAlpha(final View target, float from, float to, long dur) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "alpha", from, to);
        animator.setDuration(dur);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    private void doDelayShowAnim(long dur, long delay, View... targets) {
        for (int i = 0; i < targets.length; i++) {
            final View target = targets[i];
            target.setAlpha(0);
            ObjectAnimator animatorY = ObjectAnimator.ofFloat(target, "translationY", 100, 0);
            ObjectAnimator animatorA = ObjectAnimator.ofFloat(target, "alpha", 0, 1);
            animatorY.setDuration(dur);
            animatorA.setDuration((long) (dur * 0.618F));
            AnimatorSet animator = new AnimatorSet();
            animator.playTogether(animatorA, animatorY);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setStartDelay(delay * i);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    changeVisible(View.VISIBLE, target);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.start();
        }
    }

    @Override
    public void getAboutMeSuccess(int code, AboutMeBean data) {

    }

    @Override
    public void getAboutMeFailed(int code, String msg) {

    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public <T> LifecycleTransformer<T> bindToLife() {
        return null;
    }
}

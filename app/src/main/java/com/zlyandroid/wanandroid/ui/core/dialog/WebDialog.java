package com.zlyandroid.wanandroid.ui.core.dialog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.listener.OnClickListener2;
import com.zlyandroid.wanandroid.ui.home.adapter.WebDialogPagerAdapter;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.util.display.DisplayInfoUtils;
import com.zlylib.upperdialog.common.AnimatorHelper;
import com.zlylib.upperdialog.dialog.DialogLayer;

import java.util.ArrayList;
import java.util.List;



/**
 * @author zhangliyang
 * @date 2019/8/31
 */
public class WebDialog extends DialogLayer {

    private final int currPos;
    private final boolean singleTipMode;

    private OnPageChangedListener mOnPageChangedListener = null;
    private WebDialogPagerAdapter mAdapter;

    public static WebDialog create(Context context, String url) {
        List<ArticleBean> urls = new ArrayList<>(1);
        ArticleBean bean = new ArticleBean();
        bean.setLink(url);
        urls.add(bean);
        return new WebDialog(context, urls, null, 0, true);
    }

    public static WebDialog create(Context context, final List<ArticleBean> topUrls, final List<MultiItemEntity> urls, final int currPos) {
        return new WebDialog(context, topUrls, urls, currPos, false);
    }

    private WebDialog(final Context context,
                      final List<ArticleBean> topUrls,
                      final List<MultiItemEntity> urls,
                      final int currPos,
                      final boolean singleTipMode) {
        super(context);
        this.currPos = currPos;
        this.singleTipMode = singleTipMode;
        contentView(R.layout.dialog_web);
        backgroundDimDefault();
        cancelableOnTouchOutside(true);
        cancelableOnClickKeyBack(true);
        onClickToDismiss(R.id.dialog_web_iv_close);
        contentAnimator(new AnimatorCreator() {
            @Override
            public Animator createInAnimator(View target) {
                final ViewPager vp = getView(R.id.dialog_web_vp);
                View bar = target.findViewById(R.id.dialog_web_rl_bottom_bar);
                bar.setTranslationY(1000);
                vp.setPageMargin((int) DisplayInfoUtils.getInstance().dp2px(12));
                ValueAnimator vpMargin = ValueAnimator.ofInt(vp.getPageMargin(), 0);
                vpMargin.setInterpolator(new DecelerateInterpolator());
                vpMargin.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = (int) animation.getAnimatedValue();
                        vp.setPageMargin(value);
                    }
                });
                vpMargin.setStartDelay(150);
                vpMargin.setDuration(300);
                Animator barAnim = AnimatorHelper.createBottomInAnim(bar);
                barAnim.setDuration(300);
                barAnim.setStartDelay(150);
                Animator vpAlpha = AnimatorHelper.createBottomInAnim(vp);
                vpAlpha.setStartDelay(0);
                vpAlpha.setDuration(300);
                AnimatorSet set = new AnimatorSet();
                set.playTogether(vpMargin, vpAlpha, barAnim);
                return set;
            }

            @Override
            public Animator createOutAnimator(View target) {
                final ViewPager vp = getView(R.id.dialog_web_vp);
                View bar = target.findViewById(R.id.dialog_web_rl_bottom_bar);
                ValueAnimator vpMargin = ValueAnimator.ofInt(vp.getPageMargin(), vp.getPageMargin());
                vpMargin.setInterpolator(new AccelerateInterpolator());
                vpMargin.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int value = (int) animation.getAnimatedValue();
                        vp.setPageMargin(value);
                    }
                });
                vpMargin.setStartDelay(0);
                vpMargin.setDuration(300);
                Animator barAnim = AnimatorHelper.createBottomOutAnim(bar);
                barAnim.setDuration(300);
                barAnim.setStartDelay(0);
                Animator vpAlpha = AnimatorHelper.createBottomOutAnim(vp);
                vpAlpha.setStartDelay(150);
                vpAlpha.setDuration(300);
                AnimatorSet set = new AnimatorSet();
                set.playTogether(vpMargin, vpAlpha, barAnim);
                return set;
            }
        });
        mAdapter = new WebDialogPagerAdapter(getActivity(), topUrls, urls);
    }

    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    public void setOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
        mOnPageChangedListener = onPageChangedListener;
    }

    @Override
    public void onAttach() {
        super.onAttach();
        final ViewPager vp = getView(R.id.dialog_web_vp);
        final ImageView iv_back = getView(R.id.dialog_web_iv_back);
        if (singleTipMode) {
            iv_back.setVisibility(View.GONE);
        } else {
            iv_back.setVisibility(View.VISIBLE);
            iv_back.setOnClickListener(new OnClickListener2() {
                @Override
                public void onClick2(View v) {
                    if (mAdapter != null) {

                    }
                }
            });

        }
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                if (mAdapter != null) {
                    ArticleBean data = mAdapter.getArticleBean(i);
                    if (data != null) {
                        if (mOnPageChangedListener != null) {
                            mOnPageChangedListener.onPageChanged(i, data);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        mAdapter.setOnDoubleClickListener(new WebDialogPagerAdapter.OnDoubleClickListener() {
            @Override
            public void onDoubleClick(ArticleBean data) {

            }
        });
        vp.setAdapter(mAdapter);
        vp.setCurrentItem(currPos);
    }

    @Override
    public void onShow() {
        super.onShow();
    }

    @Override
    public void onPreRemove() {

        super.onPreRemove();
    }

    @Override
    public void onDetach() {

        super.onDetach();
    }



    public interface OnPageChangedListener {
        void onPageChanged(int pos, ArticleBean data);
    }

}

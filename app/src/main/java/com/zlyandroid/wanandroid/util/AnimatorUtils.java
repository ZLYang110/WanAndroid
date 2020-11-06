package com.zlyandroid.wanandroid.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

public class AnimatorUtils {
    //logo scaleRatio ratio
    private static float scaleRatio = 0.8f;
    private static final int duration = 300;
    /**
     * play animator when softkeyboard opened
     *
     * @param logoImage
     * @param mSlideContent
     * @param logoSlideDist
     */
    public static void setViewAnimatorWhenKeyboardOpened(View logoImage, View mSlideContent, float logoSlideDist) {
        logoImage.setPivotY(logoImage.getHeight());
        logoImage.setPivotX(logoImage.getWidth()/2);

        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(logoImage, View.SCALE_X, 1.0f, scaleRatio);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(logoImage, View.SCALE_Y, 1.0f, scaleRatio);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(logoImage, View.TRANSLATION_Y, 0.0f, -logoSlideDist);
        ObjectAnimator mContentAnimatorTranslateY = ObjectAnimator.ofFloat(mSlideContent, View.TRANSLATION_Y, 0.0f, -logoSlideDist);

        mAnimatorSet.play(mContentAnimatorTranslateY)
                .with(mAnimatorTranslateY)
                .with(mAnimatorScaleX)
                .with(mAnimatorScaleY);

        mAnimatorSet.setDuration(duration);
        mAnimatorSet.start();
    }

    /**
     * play animator when softkeyboard closed
     *
     * @param logoImage
     * @param mSlideContent
     */
    public static void setViewAnimatorWhenKeyboardClosed(View logoImage, View mSlideContent) {
        if (logoImage.getTranslationY() == 0) {
            return;
        }
        logoImage.setPivotY(logoImage.getHeight());
        logoImage.setPivotX(logoImage.getWidth()/2);

        AnimatorSet mAnimatorSet = new AnimatorSet();
        ObjectAnimator mAnimatorScaleX = ObjectAnimator.ofFloat(logoImage, View.SCALE_X, scaleRatio, 1.0f);
        ObjectAnimator mAnimatorScaleY = ObjectAnimator.ofFloat(logoImage, View.SCALE_Y, scaleRatio, 1.0f);
        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(logoImage, View.TRANSLATION_Y, logoImage.getTranslationY(), 0);
        ObjectAnimator mContentAnimatorTranslateY = ObjectAnimator.ofFloat(mSlideContent, View.TRANSLATION_Y, mSlideContent.getTranslationY(), 0);

        mAnimatorSet.play(mContentAnimatorTranslateY)
                .with(mAnimatorTranslateY)
                .with(mAnimatorScaleX)
                .with(mAnimatorScaleY);

        mAnimatorSet.setDuration(duration);
        mAnimatorSet.start();

    }


    public static void doIntAnim(final TextView target, int to, long duration) {
        String fromStr = target.getText().toString();
        int from;
        try {
            from = Integer.parseInt(fromStr);
        } catch (NumberFormatException e) {
            from = 0;
        }
        doIntAnim(target, from, to, duration);
    }

    public static void doIntAnim(final TextView target, int from, int to, long duration) {
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                if (target != null) {
                    target.setText(String.format("%d", value));
                }
            }
        });
        animator.setDuration(duration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }
}

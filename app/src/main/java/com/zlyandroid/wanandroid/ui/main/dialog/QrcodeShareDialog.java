package com.zlyandroid.wanandroid.ui.main.dialog;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trello.rxlifecycle3.RxLifecycle;
import com.zlyandroid.wanandroid.R;
import com.zlylib.upperdialog.common.AnimatorHelper;
import com.zlylib.upperdialog.dialog.DialogLayer;
import com.zlylib.upperdialog.manager.Layer;


/**
 * @author zhangliyang
 * @date 2019/11/9
 * GitHub: https://github.com/ZLYang110
 */
public class QrcodeShareDialog extends DialogLayer {

    private RxLifecycle mRxLife = null;
    private final String mUrl;
    private final String mTitle;
    private final OnShareClickListener mOnShareClickListener;

    public QrcodeShareDialog(Context context, String url, String title, OnShareClickListener listener) {
        super(context);
        mUrl = url;
        mTitle = title;
        mOnShareClickListener = listener;
        contentView(R.layout.dialog_qrcode_share);
        backgroundDimDefault();
        contentAnimator(new AnimatorCreator() {
            @Override
            public Animator createInAnimator(View target) {
                View rl_card = getView(R.id.dialog_qrcode_share_rl_card);
                View rl_btn = getView(R.id.dialog_qrcode_share_ll_btn);
                AnimatorSet animator = new AnimatorSet();
                animator.playTogether(
                        AnimatorHelper.createAlphaInAnim(rl_card),
                        AnimatorHelper.createBottomInAnim(rl_btn)
                );
                return animator;
            }

            @Override
            public Animator createOutAnimator(View target) {
                View rl_card = getView(R.id.dialog_qrcode_share_rl_card);
                View rl_btn = getView(R.id.dialog_qrcode_share_ll_btn);
                AnimatorSet animator = new AnimatorSet();
                animator.playTogether(
                        AnimatorHelper.createAlphaOutAnim(rl_card),
                        AnimatorHelper.createBottomOutAnim(rl_btn)
                );
                return animator;
            }
        });
        onClickToDismiss(R.id.dialog_qrcode_share_rl_content);
        onClickToDismiss(new OnClickListener() {
            @Override
            public void onClick(Layer layer, View v) {
                if (mOnShareClickListener != null) {
                    mOnShareClickListener.onSave(createCardBitmap());
                }
            }
        }, R.id.dialog_qrcode_share_iv_album);
        onClickToDismiss(new OnClickListener() {
            @Override
            public void onClick(Layer layer, View v) {
                if (mOnShareClickListener != null) {
                    mOnShareClickListener.onShare(createCardBitmap());
                }
            }
        }, R.id.dialog_qrcode_share_iv_share);
        onClick(new OnClickListener() {
            @Override
            public void onClick(Layer layer, View v) {

            }
        }, R.id.dialog_qrcode_share_rv_shici);
    }

    @Override
    public void onAttach() {
        super.onAttach();
        ImageView iv_qrcode = getView(R.id.dialog_qrcode_share_piv_qrcode);
        TextView tv_title = getView(R.id.dialog_qrcode_share_tv_title);
        Bitmap logo = BitmapFactory.decodeResource(getActivity().getResources(), R.mipmap.ic_launcher);
       // Bitmap qrcode = QRCodeEncoder.syncEncodeQRCode(mUrl, 300, Color.BLACK, Color.WHITE, logo);
       // iv_qrcode.setImageBitmap(qrcode);
        tv_title.setText(mTitle);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    private Bitmap createCardBitmap() {
        View rl_card = getView(R.id.dialog_qrcode_share_rl_card);
        Bitmap bitmap = Bitmap.createBitmap(rl_card.getWidth(), rl_card.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        rl_card.draw(canvas);
        return bitmap;
    }

    public interface OnShareClickListener {
        void onSave(Bitmap bitmap);

        void onShare(Bitmap bitmap);
    }
}

package com.zlyandroid.wanandroid.ui.main.dialog;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.core.UserInfoManager;
import com.zlyandroid.wanandroid.ui.mine.activity.SettingActivity;
import com.zlyandroid.wanandroid.util.SettingUtils;
import com.zlylib.upperdialog.Upper;
import com.zlylib.upperdialog.manager.Layer;
import com.zlylib.upperdialog.view.DragLayout;

/**
 * @author zhangliyang
 * @date 2019/5/20
 * GitHub:https://github.com/ZLYang110
 */
public class WebMenuDialog {

    public static void show(@NonNull Context context,
                            final String url,
                            final boolean collected,
                            @NonNull OnMenuClickListener listener) {
        Upper.dialog(context)
                .contentView(R.layout.dialog_web_menu)
                .backgroundDimDefault()
                .dragDismiss(DragLayout.DragStyle.Bottom)
                .gravity(Gravity.BOTTOM)
                .onClickToDismiss(
                        new Layer.OnClickListener() {
                            @Override
                            public void onClick(Layer layer, View v) {
                                switch (v.getId()) {
                                    default:
                                        break;
                                    case R.id.dialog_web_menu_iv_share_article:
                                        if (UserInfoManager.doIfLogin(v.getContext())) {
                                            listener.onShareArticle();
                                        }
                                        break;
                                    case R.id.dialog_web_menu_iv_collect:
                                        if (UserInfoManager.doIfLogin(v.getContext())) {
                                            listener.onCollect();
                                        }
                                        break;
                                    case R.id.dialog_web_menu_iv_read_later:
                                        listener.onReadLater();
                                        break;
                                    case R.id.dialog_web_menu_iv_home:
                                        listener.onHome();
                                        break;
                                    case R.id.dialog_web_menu_iv_refresh:
                                        listener.onRefresh();
                                        break;
                                    case R.id.dialog_web_menu_iv_close_activity:
                                        listener.onCloseActivity();
                                        break;
                                    case R.id.dialog_web_menu_iv_setting:
                                        SettingActivity.createIntent(context);
                                        break;
                                    case R.id.dialog_web_menu_iv_share:
                                        listener.onShare();
                                        break;
                                }
                            }
                        },
                        R.id.dialog_web_menu_iv_share_article,
                        R.id.dialog_web_menu_iv_read_later,
                        R.id.dialog_web_menu_iv_home,
                        R.id.dialog_web_menu_iv_collect,
                        R.id.dialog_web_menu_iv_refresh,
                        R.id.dialog_web_menu_iv_close_activity,
                        R.id.dialog_web_menu_iv_dismiss,
                        R.id.dialog_web_menu_iv_setting,
                        R.id.dialog_web_menu_iv_share)
                .onClick(new Layer.OnClickListener() {
                             @Override
                             public void onClick(Layer layer, View v) {
                                 switch (v.getId()) {
                                     default:
                                         break;

                                 }
                             }
                         },
                        R.id.dialog_web_menu_iv_interrupt,
                        R.id.dialog_web_menu_iv_swipe_back)
                .bindData(new Layer.DataBinder() {
                    @Override
                    public void bindData(Layer layer) {
                        TextView tv_host = layer.getView(R.id.dialog_web_menu_tv_host);
                        String host = null;
                        if (!TextUtils.isEmpty(url)) {
                            Uri uri = Uri.parse(url);
                            host = uri.getHost();
                        }
                        if (TextUtils.isEmpty(host)) {
                            tv_host.setVisibility(View.GONE);
                        } else {
                            tv_host.setVisibility(View.VISIBLE);
                            tv_host.setText("网页由 " + host + " 提供");
                        }
                        ImageView iv_collect = layer.getView(R.id.dialog_web_menu_iv_collect);
                        TextView tv_collect = layer.getView(R.id.dialog_web_menu_tv_collect);
                        switchCollectState(iv_collect, tv_collect, collected);
                        ImageView iv_interrupt = layer.getView(R.id.dialog_web_menu_iv_interrupt);
                        TextView tv_interrupt = layer.getView(R.id.dialog_web_menu_tv_interrupt);
                        ImageView iv_swipe_back = layer.getView(R.id.dialog_web_menu_iv_swipe_back);
                        iv_interrupt.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                layer.dismiss();
                                return true;
                            }
                        });
                    }
                })
                .show();
    }

    private static void switchCollectState(ImageView iv_collect, TextView tv_collect, boolean collected) {
        setIconChecked(iv_collect, collected);
        if (collected) {
            tv_collect.setText("已收藏");
        } else {
            tv_collect.setText("收藏");
        }
    }



    private static void setIconChecked(ImageView iv, boolean checked) {
        if (checked) {
            iv.setBackgroundResource(R.drawable.bg_press_color_main_radius_max);
        } else {
            iv.setBackgroundResource(R.drawable.bg_press_color_surface_top_radius_max);
        }
    }

    public interface OnMenuClickListener {
        void onShareArticle();

        void onCollect();

        void onReadLater();

        void onHome();

        void onRefresh();

        void onCloseActivity();

        void onShare();
    }

}

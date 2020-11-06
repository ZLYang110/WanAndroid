package com.zlyandroid.wanandroid.ui.web;


import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.just.agentweb.AgentWeb;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpActivity;
import com.zlyandroid.wanandroid.core.Constants;
import com.zlyandroid.wanandroid.db.greendao.ReadRecordModel;
import com.zlyandroid.wanandroid.db.readlater.DbReadLaterHelperImpl;
import com.zlyandroid.wanandroid.db.readrecord.DbReadRecordHelperImpl;
import com.zlyandroid.wanandroid.ui.home.activity.SearchActivity;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.util.IntentUtils;
import com.zlyandroid.wanandroid.util.LogUtil;
import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.titlebarlib.ActionBarSearch;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;
import com.zlylib.upperdialog.Upper;
import com.zlylib.upperdialog.common.Align;
import com.zlylib.upperdialog.common.AnimatorHelper;
import com.zlylib.upperdialog.dialog.DialogLayer;
import com.zlylib.upperdialog.manager.Layer;

import java.lang.reflect.Method;

import butterknife.BindView;

/**
 * 文章详情H5
 * author: zhangliyang
 * date: 2020/5/12
 */

public class WebViewActivity extends BaseMvpActivity<WebViewPresenter> implements WebViewContract.IWebView {

    @BindView(R.id.abc)
    ActionBarCommon abc;
    @BindView(R.id.container)
    FrameLayout container;


    private AgentWeb mAgentWeb;
    private ArticleBean bean;
    private String actionType;
    private int id;
    private String title = "";
    private String url = "";
    private DialogLayer upper_show_menu = null;

    public static void start(Context context, ArticleBean bean) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.BUNDLE_KEY.OBJ, bean);
        bundle.putString(Constants.BUNDLE_KEY.TYPE, Constants.EVENT_ACTION.HOME);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    public static void start(Context context, String  title, String  url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_KEY.TITLE, title);
        bundle.putString(Constants.BUNDLE_KEY.URL, url);
        bundle.putString(Constants.BUNDLE_KEY.TYPE, Constants.EVENT_ACTION.READLATER);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected WebViewPresenter createPresenter() {
        return new WebViewPresenter();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_webview;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;

        actionType = getIntent().getStringExtra(Constants.BUNDLE_KEY.TYPE);
        LogUtil.d("----"+actionType);
        if(actionType.equals(Constants.EVENT_ACTION.HOME) ){
            bean = (ArticleBean) bundle.getSerializable(Constants.BUNDLE_KEY.OBJ);
            if (bean != null) {
                id = bean.getId();
                title = bean.getTitle();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    title = Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY).toString();
                } else {
                    title = Html.fromHtml(title).toString();
                }
                url = bean.getLink();

                DbReadRecordHelperImpl.getInstance().add(title,url);
            }
        }else  if(actionType.equals(Constants.EVENT_ACTION.READLATER)){
            title = getIntent().getStringExtra(Constants.BUNDLE_KEY.TITLE);
            url = getIntent().getStringExtra(Constants.BUNDLE_KEY.URL);
        }

    }

    @Override
    public void initData() {

        abc.getTitleTextView().setText(title);
        abc.setOnLeftIconClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        abc.setOnRightIconClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                showMeun();

            }
        });
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(container, new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(R.color.black)
                .createAgentWeb()
                .go(url);
    }



    @Override
    public void collectSuccess(boolean isCollect, String result) {

    }

    @Override
    public void collectFailed(boolean isCollect, String result) {

    }

    //打开菜单
    private void showMeun(){
        if (upper_show_menu == null) {
            upper_show_menu = (DialogLayer) Upper.popup(abc.getRightIconView())
                    .align(Align.Direction.VERTICAL, Align.Horizontal.ALIGN_RIGHT, Align.Vertical.BELOW, true)
                    .offsetYdp(0)
                    .backgroundDimDefault()
                    .outsideTouchedToDismiss(true)
                    .outsideInterceptTouchEvent(false)
                    .contentView(R.layout.popup_meun)

                    .contentAnimator(new DialogLayer.AnimatorCreator() {
                        @Override
                        public Animator createInAnimator(View content) {
                            return AnimatorHelper.createDelayedZoomInAnim(content, 1F, 0F);
                        }

                        @Override
                        public Animator createOutAnimator(View content) {
                            return AnimatorHelper.createDelayedZoomOutAnim(content, 1F, 0F);
                        }
                    }).onClick(new Layer.OnClickListener() {
                        @Override
                        public void onClick(Layer layer, View v) {
                            share();
                            upper_show_menu.dismiss();
                        }
                    },R.id.share)
                    .onClick(new Layer.OnClickListener() {
                        @Override
                        public void onClick(Layer layer, View v) {
                            collect();
                            upper_show_menu.dismiss();
                        }
                    },R.id.collect)
                    .onClick(new Layer.OnClickListener() {
                        @Override
                        public void onClick(Layer layer, View v) {
                            DbReadLaterHelperImpl.getInstance().add(title,url);
                            showToast("添加成功");
                            upper_show_menu.dismiss();
                        }
                    },R.id.tv_readlater)
                    .onClick(new Layer.OnClickListener() {
                        @Override
                        public void onClick(Layer layer, View v) {
                            IntentUtils.openBrowser(getContext(), url);
                            upper_show_menu.dismiss();
                        }
                    },R.id.browser);
        }


        if (upper_show_menu.isShow()) {
            upper_show_menu.dismiss();
        } else {
            upper_show_menu.show();
        }
    }

    //分享
    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "玩Android分享(" + title + "):" + url);
        intent.setType("text/plain");//分享文本
        startActivity(Intent.createChooser(intent, "分享"));
    }

    //收藏
    private void collect() {
        showToast("收藏");
      //  if (bean.isCollect()) return;
       // mPresenter.collectArticle(bean.getId());
    }





    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //将事件交给AgentWeb做处理
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.destroy();
    }


}

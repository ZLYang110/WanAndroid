package com.zlyandroid.wanandroid.ui.main.acitivity;


import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpActivity;
import com.zlyandroid.wanandroid.core.Constants;
import com.zlyandroid.wanandroid.db.readlater.DbReadLaterHelperImpl;
import com.zlyandroid.wanandroid.db.readrecord.DbReadRecordHelperImpl;
import com.zlyandroid.wanandroid.listener.OnClickListener2;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.CollectArticleEntity;
import com.zlyandroid.wanandroid.ui.main.dialog.QrcodeShareDialog;
import com.zlyandroid.wanandroid.ui.main.dialog.WebMenuDialog;
import com.zlyandroid.wanandroid.ui.main.dialog.WebQuickDialog;
import com.zlyandroid.wanandroid.ui.main.presenter.WebPresenter;
import com.zlyandroid.wanandroid.ui.main.view.WebView;
import com.zlyandroid.wanandroid.ui.web.WebViewContract;
import com.zlyandroid.wanandroid.ui.web.WebViewPresenter;
import com.zlyandroid.wanandroid.util.CopyUtils;
import com.zlyandroid.wanandroid.util.InputMethodUtils;
import com.zlyandroid.wanandroid.util.IntentUtils;
import com.zlyandroid.wanandroid.util.LogUtil;
import com.zlyandroid.wanandroid.util.MD5Coder;
import com.zlyandroid.wanandroid.util.PermissionUtils;
import com.zlyandroid.wanandroid.util.ShareUtils;
import com.zlyandroid.wanandroid.util.api.ToastUtils;
import com.zlyandroid.wanandroid.util.bitmap.BitmapUtils;
import com.zlyandroid.wanandroid.util.router.Router;
import com.zlyandroid.wanandroid.util.web.WebHolder;
import com.zlyandroid.wanandroid.widget.CollectView;
import com.zlyandroid.wanandroid.widget.WebContainer;
import com.zlylib.mypermissionlib.RequestListener;
import com.zlylib.mypermissionlib.RuntimeRequester;
import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;
import com.zlylib.titlebarlib.widget.ActionBarEx;
import com.zlylib.upperdialog.Upper;
import com.zlylib.upperdialog.common.Align;
import com.zlylib.upperdialog.common.AnimatorHelper;
import com.zlylib.upperdialog.dialog.DialogLayer;
import com.zlylib.upperdialog.manager.Layer;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 文章详情H5
 * author: zhangliyang
 * date: 2020/5/12
 * GitHub:  https://github.com/ZLYang110
 */

public class WebActivity extends BaseMvpActivity<WebPresenter> implements WebView {

    private static final int REQ_CODE_PERMISSION = 1;

    @BindView(R.id.ab)
    ActionBarEx ab;
    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.cv_collect)
    CollectView cv_collect;
    @BindView(R.id.iv_into)
    ImageView iv_into;
    @BindView(R.id.wc)
    WebContainer wc;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_forward)
    ImageView iv_forward;
    @BindView(R.id.iv_menu)
    ImageView iv_menu;

    private RuntimeRequester mRuntimeRequester = null;

    private int mArticleId = -1;
    private int mCollectId = -1;
    private String mTitle = "";
    private String mAuthor = "";
    private String mUrl = "";

    private AgentWeb mAgentWeb;

    private WebHolder mWebHolder;

    private List<CollectArticleEntity> mCollectedList = new ArrayList<>(1);
    private WebQuickDialog mWebQuickDialog;

    public static void start(Context context, String url, String title,
                             int articleId, int collectId, boolean collected) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("articleId", articleId);
        intent.putExtra("collectId", collectId);
        intent.putExtra("collected", collected);
        context.startActivity(intent);
    }


    @Override
    protected WebPresenter createPresenter() {
        return new WebPresenter();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_web;
    }

    @Override
    public void initView() {
        Uri uri = Router.uri(getIntent());
        if (uri != null) {
            mUrl = uri.toString();
        } else {
            mArticleId = getIntent().getIntExtra("articleId", -1);
            mCollectId = getIntent().getIntExtra("collectId", -1);
            mTitle = getIntent().getStringExtra("title");
            mAuthor = getIntent().getStringExtra("author");
            mUrl = getIntent().getStringExtra("url");
        }
        mTitle = mTitle == null ? "" : mTitle;
        mAuthor = mAuthor == null ? "" : mAuthor;
        mUrl = mUrl == null ? "" : mUrl;
        boolean collected = getIntent().getBooleanExtra("collected", false);
        if (collected) {
            CollectArticleEntity entity = new CollectArticleEntity();
            entity.setArticleId(mArticleId);
            entity.setCollectId(mCollectId);
            entity.setTitle(mTitle);
            entity.setAuthor(mAuthor);
            entity.setUrl(mUrl);
            entity.setCollect(true);
            mCollectedList.add(entity);
        }
        iv_menu.setOnClickListener(new OnClickListener2() {
            @Override
            public void onClick2(View v) {
                showMenuDialog();
            }
        });
        iv_back.setOnClickListener(new OnClickListener2() {
            @Override
            public void onClick2(View v) {
                if (mAgentWeb.back()) {
                    mAgentWeb.back();
                } else {
                    finish();
                }
            }
        });
        iv_forward.setOnClickListener(new OnClickListener2() {
            @Override
            public void onClick2(View v) {

            }
        });
        wc.setOnTouchDownListener(new WebContainer.OnTouchDownListener() {
            @Override
            public void onTouchDown() {
                et_title.clearFocus();
            }
        });
        wc.setOnDoubleClickListener(new WebContainer.OnDoubleClickListener() {
            @Override
            public void onDoubleClick(float x, float y) {
                collect();
            }
        });
        iv_into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = et_title.getText().toString();
                if (!TextUtils.isEmpty(url)) {
                    Uri uri = Uri.parse(url);
                    if (TextUtils.equals(uri.getScheme(), "http") || TextUtils.equals(uri.getScheme(), "https")) {

                    }
                }
                et_title.clearFocus();
            }
        });
        et_title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    iv_into.performClick();
                    return true;
                }
                return false;
            }
        });
        et_title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_title.setText(mUrl);
                    InputMethodUtils.show(et_title);
                    showQuickDialog();
                } else {
                    setTitle();
                    InputMethodUtils.hide(et_title);
                    dismissQuickDialog();
                }
            }
        });
        cv_collect.setOnClickListener(new CollectView.OnClickListener() {
            @Override
            public void onClick(CollectView v) {
                if (v.isChecked()) {
                    collect();
                } else {
                    uncollect();
                }
            }
        });
    }
    private void setTitle() {
        et_title.setTag(mUrl);
        if (!TextUtils.isEmpty(mUrl)) {
            et_title.setText(mUrl);
        } else {
            et_title.setText(mUrl);
        }
    }
    @Override
    public void initData() {


        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(wc, new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(R.color.black)
                .createAgentWeb()
                .go(mUrl);
    }



    private void showQuickDialog() {
        if (mWebQuickDialog == null) {
            mWebQuickDialog = new WebQuickDialog(ab, new WebQuickDialog.OnQuickClickListener() {
                @Override
                public void onCopyLink() {
                    CopyUtils.copyText(mUrl);
                    showToast("已复制");
                }

                @Override
                public void onBrowser() {
                    IntentUtils.openBrowser(getContext(), mUrl);
                }

                @Override
                public void onWanPwd() {

                }
            });
            mWebQuickDialog.onDismissListener(new Layer.OnDismissListener() {
                @Override
                public void onDismissing(Layer layer) {
                }

                @Override
                public void onDismissed(Layer layer) {
                    et_title.clearFocus();
                }
            });
        }
        mWebQuickDialog.show();
    }

    private void dismissQuickDialog() {
        if (mWebQuickDialog != null) {
            mWebQuickDialog.dismiss();
        }
    }

    private void showMenuDialog() {
        WebMenuDialog.show(getContext(), mUrl, isCollect(), new WebMenuDialog.OnMenuClickListener() {
            @Override
            public void onShareArticle() {
               // ShareArticleActivity.start(getContext(), mWebHolder.getTitle(), mWebHolder.getUrl());
            }

            @Override
            public void onCollect() {
                toggleCollect();
            }

            @Override
            public void onReadLater() {
            }

            @Override
            public void onHome() {

            }

            @Override
            public void onRefresh() {
                mAgentWeb.getUrlLoader();
            }

            @Override
            public void onCloseActivity() {
                finish();
            }

            @Override
            public void onShare() {
                new QrcodeShareDialog(getContext(), mUrl, mTitle, new QrcodeShareDialog.OnShareClickListener() {
                    @Override
                    public void onSave(Bitmap bitmap) {
                        saveQrcodeGallery(bitmap);
                    }

                    @Override
                    public void onShare(Bitmap bitmap) {
                        shareBitmap(bitmap);
                    }
                }).show();
            }
        });
    }
    private void shareBitmap(final Bitmap bitmap) {
        ShareUtils.shareBitmap(getContext(), bitmap);
    }

    private void saveQrcodeGallery(final Bitmap bitmap) {
        mRuntimeRequester = PermissionUtils.request(new RequestListener() {
            @Override
            public void onSuccess() {
                 saveGallery(bitmap, "wanandroid_article_qrcode_" + MD5Coder.encode(mUrl) + "_" + System.currentTimeMillis());
            }

            @Override
            public void onFailed() {
            }
        }, getContext(), REQ_CODE_PERMISSION, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    public void saveGallery(Bitmap bitmap, String name) {
        if (null != BitmapUtils.saveGallery(bitmap, name)) {
            showToast("以保存到相册");
        } else {
            showToast("保存失败");
        }
        bitmap.recycle();
    }
    //分享
    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, "玩Android分享(" + mTitle + "):" + mUrl);
        intent.setType("text/plain");//分享文本
        startActivity(Intent.createChooser(intent, "分享"));
    }

    private void toggleCollect() {
        if (isCollect()) {
            uncollect();
        } else {
            collect();
        }
    }

    private boolean isCollect() {
        CollectArticleEntity entity = findCollectArticleEntity();
        if (entity == null) return false;
        return entity.isCollect();
    }

    private CollectArticleEntity findCollectArticleEntity() {
        String url =mUrl;
        CollectArticleEntity collectArticleEntity = null;
        for (CollectArticleEntity entity : mCollectedList) {
            if (TextUtils.equals(entity.getUrl(), url)) {
                collectArticleEntity = entity;
                break;
            }
        }
        return collectArticleEntity;
    }

    private CollectArticleEntity newCollectArticleEntity() {
        CollectArticleEntity entity = new CollectArticleEntity();
        entity.setCollect(false);
        String url = mUrl;
        entity.setUrl(url);
        if (TextUtils.equals(url, mUrl)) {
            entity.setCollectId(mCollectId);
            if (mArticleId > 0) {
                entity.setArticleId(mArticleId);
            } else {
                entity.setAuthor(mAuthor);
                entity.setTitle(TextUtils.isEmpty(mTitle) ?   mTitle: mTitle);
            }
        } else {
            entity.setTitle(mTitle);
        }
        return entity;
    }
    private void resetCollect() {
        cv_collect.setChecked(isCollect(), true);
    }

    private void collect() {
        CollectArticleEntity entity = findCollectArticleEntity();
        if (entity != null) {
            if (entity.isCollect()) {
                resetCollect();
                return;
            }
        }
        if (entity == null) {
            entity = newCollectArticleEntity();
        }
        mPresenter.collect(entity);
    }

    private void uncollect() {
        CollectArticleEntity entity = findCollectArticleEntity();
        if (entity == null) {
            resetCollect();
            return;
        }
        if (!entity.isCollect()) {
            resetCollect();
            return;
        }
        mPresenter.uncollect(entity);
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


    @Override
    public void collectSuccess(CollectArticleEntity entity) {

    }

    @Override
    public void collectFailed(String msg) {

    }

    @Override
    public void uncollectSuccess(CollectArticleEntity entity) {

    }

    @Override
    public void uncollectFailed(String msg) {

    }
}

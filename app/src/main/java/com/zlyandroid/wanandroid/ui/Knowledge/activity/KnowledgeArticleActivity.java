package com.zlyandroid.wanandroid.ui.Knowledge.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpActivity;
import com.zlyandroid.wanandroid.base.BaseMvpFragment;
import com.zlyandroid.wanandroid.core.Config;
import com.zlyandroid.wanandroid.event.ScrollTopEvent;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.ChapterBean;
import com.zlyandroid.wanandroid.ui.Knowledge.fragment.KnowledgeArticleFragment;
import com.zlyandroid.wanandroid.ui.Knowledge.presenter.KnowledgeArticlePresenter;
import com.zlyandroid.wanandroid.ui.Knowledge.presenter.KnowledgePresenter;
import com.zlyandroid.wanandroid.ui.Knowledge.view.KnowledgeArticleView;
import com.zlyandroid.wanandroid.ui.Knowledge.view.KnowledgeView;
import com.zlyandroid.wanandroid.ui.core.adapter.MultiFragmentPagerAdapter;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.util.MagicIndicatorUtils;
import com.zlyandroid.wanandroid.util.MultiStateUtils;
import com.zlyandroid.wanandroid.util.router.Router;
import com.zlylib.multistateview.MultiStateView;
import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.upperdialog.listener.SimpleCallback;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.List;

import butterknife.BindView;
/**
 * @author zhangliyang
 * @date 2019/5/12
 * GitHub: https://github.com/ZLYang110
 */
public class KnowledgeArticleActivity extends BaseMvpActivity<KnowledgePresenter> implements KnowledgeView {

    @BindView(R.id.abc)
    ActionBarCommon abc;
    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.mi)
    MagicIndicator mi;
    @BindView(R.id.vp)
    ViewPager vp;

    private long lastClickTime = 0L;
    private int lastClickPos = 0;
    private MultiFragmentPagerAdapter<ChapterBean, KnowledgeArticleFragment> mAdapter;
    private CommonNavigator mCommonNavigator;
    private int mSuperChapterId;
    private int mChapterId;

    public static void start(Context context, ChapterBean chapterBean, int currPos) {
        Intent intent = new Intent(context, KnowledgeArticleActivity.class);
        intent.putExtra("chapterBean", chapterBean);
        intent.putExtra("currPos", currPos);
        context.startActivity(intent);
    }
    public static void start(Context context, int superChapterId, String superChapterName, int chapterId) {
        Intent intent = new Intent(context, KnowledgeArticleActivity.class);
        intent.putExtra("superChapterName", superChapterName);
        intent.putExtra("superChapterId", superChapterId);
        intent.putExtra("chapterId", chapterId);
        context.startActivity(intent);
    }


    public static void start(Context context, ArticleBean.TagsBean tag) {
        if (tag == null) return;
        String url = tag.getUrl();
        if (TextUtils.isEmpty(url)) return;
        // /wxarticle/list/410/1
        // /article/list/0?cid=440
        // /project/list/1?cid=367
        // wana://www.wanandroid.com/wxarticle/list/410/1?cid=440
        Uri uri = Uri.parse(Router.SCHEME + "://" + Router.HOST + url);
        try {
            String cid = uri.getQueryParameter("cid");
            if (TextUtils.isEmpty(cid)) {
                List<String> paths = uri.getPathSegments();
                if (paths != null && paths.size() >= 3) {
                    cid = paths.get(2);
                }
            }
            int chapterId = Integer.parseInt(cid);
            if (chapterId > 0) {
                start(context, 0, "", chapterId);
            }
        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    protected KnowledgePresenter createPresenter() {
        return new KnowledgePresenter();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_knowledge_article;
    }

    @Override
    public void initView() {
        abc.getTitleTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyScrollTop(vp.getCurrentItem());
            }
        });
        mAdapter = new MultiFragmentPagerAdapter<>(
                getSupportFragmentManager(),
                new MultiFragmentPagerAdapter.FragmentCreator<ChapterBean, KnowledgeArticleFragment>() {
                    @Override
                    public KnowledgeArticleFragment create(ChapterBean data, int pos) {
                        return KnowledgeArticleFragment.create(data, pos);
                    }

                    @Override
                    public String getTitle(ChapterBean data) {
                        return data.getName();
                    }
                });
        vp.setAdapter(mAdapter);
        mCommonNavigator = MagicIndicatorUtils.commonNavigator(mi, vp, mAdapter, new SimpleCallback<Integer>() {
            @Override
            public void onResult(Integer data) {
                notifyScrollTop(data);
            }
        });
    }

    @Override
    public void initData() {
        ChapterBean bean = (ChapterBean) getIntent().getSerializableExtra("chapterBean");
        int currPos = getIntent().getIntExtra("currPos", 0);
        if (bean != null) {
            initVP(bean, currPos);
        } else {
            msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
            mSuperChapterId = getIntent().getIntExtra("superChapterId", -1);
            mChapterId = getIntent().getIntExtra("chapterId", -1);
            if (mSuperChapterId <= 0 && mChapterId <= 0) {
                msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
            } else {
                String superChapterName = getIntent().getStringExtra("superChapterName");
                abc.getTitleTextView().setText(superChapterName);
                mPresenter.getKnowledgeListCacheAndNet();
            }
        }
    }
    private void initVP(ChapterBean bean, int currPos) {
        msv.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        abc.getTitleTextView().setText(bean.getName());
        mAdapter.setDataList(bean.getChildren());
        mCommonNavigator.notifyDataSetChanged();
        vp.setCurrentItem(currPos);
    }

    private void notifyScrollTop(int pos) {
        long currClickTime = System.currentTimeMillis();
        if (lastClickPos == pos && currClickTime - lastClickTime <= Config.SCROLL_TOP_DOUBLE_CLICK_DELAY) {
            new ScrollTopEvent(KnowledgeArticleFragment.class, vp.getCurrentItem()).post();
        }
        lastClickPos = pos;
        lastClickTime = currClickTime;
    }



    @Override
    public void getKnowledgeListSuccess(int code, List<ChapterBean> data) {
        ChapterBean superChapterBean = null;
        for (ChapterBean bean : data) {
            if (bean.getId() == mSuperChapterId) {
                superChapterBean = bean;
                break;
            }
        }
        int currPos = 0;
        if (superChapterBean == null) {
            for (ChapterBean scb : data) {
                List<ChapterBean> chapters = scb.getChildren();
                for (int i = 0; i < chapters.size(); i++) {
                    ChapterBean cb = chapters.get(i);
                    if (cb.getId() == mChapterId) {
                        superChapterBean = scb;
                        currPos = i;
                        break;
                    }
                }
            }
        } else {
            List<ChapterBean> chapters = superChapterBean.getChildren();
            for (int i = 0; i < chapters.size(); i++) {
                ChapterBean cb = chapters.get(i);
                if (cb.getId() == mChapterId) {
                    currPos = i;
                    break;
                }
            }
        }
        if (superChapterBean != null) {
            initVP(superChapterBean, currPos);
        } else {
            msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    @Override
    public void getKnowledgeListFail(int code, String msg) {
        showToast(msg);
        msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }
}

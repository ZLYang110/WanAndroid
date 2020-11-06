package com.zlyandroid.wanandroid.ui.Knowledge.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpFragment;
import com.zlyandroid.wanandroid.event.ScrollTopEvent;
import com.zlyandroid.wanandroid.ui.Knowledge.adapter.KnowledgeAdapter;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.ChapterBean;
import com.zlyandroid.wanandroid.ui.Knowledge.presenter.KnowledgeArticlePresenter;
import com.zlyandroid.wanandroid.ui.Knowledge.presenter.KnowledgePresenter;
import com.zlyandroid.wanandroid.ui.Knowledge.view.KnowledgeArticleView;
import com.zlyandroid.wanandroid.ui.Knowledge.view.KnowledgeView;
import com.zlyandroid.wanandroid.ui.home.adapter.ArticleAdapter;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.web.WebViewActivity;
import com.zlyandroid.wanandroid.util.MultiStateUtils;
import com.zlyandroid.wanandroid.util.RvAnimUtils;
import com.zlyandroid.wanandroid.util.RvScrollTopUtils;
import com.zlyandroid.wanandroid.util.SettingUtils;
import com.zlyandroid.wanandroid.util.SmartRefreshUtils;
import com.zlylib.multistateview.MultiStateView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

/**
 * @author zhangliyang
 * @date 2019/5/12
 * GitHub: https://github.com/ZLYang110
 */

public class KnowledgeArticleFragment extends BaseMvpFragment<KnowledgeArticlePresenter> implements KnowledgeArticleView {

    private static final int PAGE_START = 0;

    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.rv)
    RecyclerView rv;

    private SmartRefreshUtils mSmartRefreshUtils;
    private ArticleAdapter mAdapter;


    private ChapterBean mChapterBean;
    private int mPosition = -1;

    private int currPage = PAGE_START;

    public static KnowledgeArticleFragment create(ChapterBean chapterBean, int position) {
        KnowledgeArticleFragment fragment = new KnowledgeArticleFragment();
        Bundle args = new Bundle(2);
        args.putSerializable("chapterBean", chapterBean);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScrollTopEvent(ScrollTopEvent event) {
        if (!getClass().equals(event.getClazz())) {
            return;
        }
        if (mPosition != event.getPosition()) {
            return;
        }
        if (isAdded() && !isDetached()) {
            RvScrollTopUtils.smoothScrollTop(rv);
        }
    }
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
    @Override
    protected KnowledgeArticlePresenter createPresenter() {
        return new KnowledgeArticlePresenter();
    }
    /**
     * TODO状态改变后，Fragment再次可见，则更新数据
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            currPage = PAGE_START;
           // getKnowledgeArticleList(false);
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_knowledge_article;
    }

    @Override
    public void initView() {
        Bundle args = getArguments();
        if (args != null) {
            mChapterBean = (ChapterBean) args.getSerializable("chapterBean");
            mPosition = args.getInt("position", -1);
        }
        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        mSmartRefreshUtils.setRefreshListener(new SmartRefreshUtils.RefreshListener() {
            @Override
            public void onRefresh() {
                currPage = PAGE_START;
                getKnowledgeArticleList(true);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ArticleAdapter();
        RvAnimUtils.setAnim(mAdapter, SettingUtils.getInstance().getRvAnim());
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getKnowledgeArticleList(false);
            }
        }, rv);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleBean item = mAdapter.getArticleBean(position);
                if (item != null) {
                    WebViewActivity.start( getActivity(),mAdapter.getArticleBean(position));
                }
            }
        });

        rv.setAdapter(mAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, new MultiStateUtils.SimpleListener() {
            @Override
            public void onResult() {
                msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
                currPage = PAGE_START;
                getKnowledgeArticleList(false);
            }
        });
    }

    @Override
    public void initData() {
        if (mChapterBean != null) {
            msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
            mPresenter.getKnowledgeArticleListCache(mChapterBean.getId(), currPage);
        } else {
            msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    public void getKnowledgeArticleList(boolean refresh) {
        if (mChapterBean != null) {
            mPresenter.getKnowledgeArticleList(mChapterBean.getId(), currPage);
        }
    }
    @Override
    public void getKnowledgeArticleListSuccess(int code, ArticleListBean data) {
        currPage = data.getCurPage() + PAGE_START;
        if (data.getCurPage() == 1) {
            mAdapter.setNewData(data.getDatas());
            mAdapter.setEnableLoadMore(true);
            if (data.getDatas() == null || data.getDatas().isEmpty()) {
                msv.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            } else {
                msv.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }
        } else {
            mAdapter.addData(data.getDatas());
            mAdapter.loadMoreComplete();
        }
        if (data.isOver()) {
            mAdapter.loadMoreEnd();
        }
        mSmartRefreshUtils.success();
    }

    @Override
    public void getKnowledgeArticleListFail(int code, String msg) {
        show(msg);
        mSmartRefreshUtils.fail();
        mAdapter.loadMoreFail();
        if (currPage == PAGE_START) {
            msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }
}

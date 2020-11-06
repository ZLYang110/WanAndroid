package com.zlyandroid.wanandroid.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpActivity;
import com.zlyandroid.wanandroid.core.Config;
import com.zlyandroid.wanandroid.event.ArticleDeleteEvent;
import com.zlyandroid.wanandroid.event.SettingChangeEvent;
import com.zlyandroid.wanandroid.ui.home.activity.UserPageActivity;
import com.zlyandroid.wanandroid.ui.home.adapter.ArticleAdapter;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.mine.adapter.CoinRankAdapter;
import com.zlyandroid.wanandroid.ui.mine.adapter.MineShareArticleAdapter;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinInfoBean;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRankBean;
import com.zlyandroid.wanandroid.ui.mine.presenter.CoinRankPresenter;
import com.zlyandroid.wanandroid.ui.mine.presenter.MineSharePresenter;
import com.zlyandroid.wanandroid.ui.mine.view.CoinRankView;
import com.zlyandroid.wanandroid.ui.mine.view.MineShareView;
import com.zlyandroid.wanandroid.ui.web.WebViewActivity;
import com.zlyandroid.wanandroid.util.MultiStateUtils;
import com.zlyandroid.wanandroid.util.RvAnimUtils;
import com.zlyandroid.wanandroid.util.RvScrollTopUtils;
import com.zlyandroid.wanandroid.util.SettingUtils;
import com.zlyandroid.wanandroid.util.SmartRefreshUtils;
import com.zlylib.multistateview.MultiStateView;
import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class MineShareActivity extends BaseMvpActivity<MineSharePresenter> implements MineShareView {

    private static final int PAGE_START = 1;


    @BindView(R.id.abc)
    ActionBarCommon abc;
    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.rv)
    RecyclerView rv;

    private int currPage = PAGE_START;
    private SmartRefreshUtils mSmartRefreshUtils;
    private MineShareArticleAdapter mAdapter;

    private long lastClickTime = 0L;

    public static void start(Context context) {
        Intent intent = new Intent(context, MineShareActivity.class);
        context.startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChangeEvent(SettingChangeEvent event) {
        if (event.isRvAnimChanged()) {
            RvAnimUtils.setAnim(mAdapter, SettingUtils.getInstance().getRvAnim());
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onArticleDeleteEvent(ArticleDeleteEvent event) {
        if (event.getArticleId() <= 0) {
            currPage = PAGE_START;
            mPresenter.getMineShareArticleList(currPage);
        } else {
            mAdapter.forEach(new ArticleAdapter.ArticleForEach() {
                @Override
                public boolean forEach(int dataPos, int adapterPos, ArticleBean bean) {
                    if (event.getArticleId() == bean.getId()) {
                        mAdapter.remove(adapterPos);
                        return true;
                    }
                    return false;
                }
            });
        }
    }
    @Override
    protected MineSharePresenter createPresenter() {
        return new MineSharePresenter();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_mine_share;
    }

    @Override
    public void initView() {
        abc.setOnRightIconClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
               // ShareArticleActivity.start(getContext());
            }
        });
        abc.getTitleTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currClickTime = System.currentTimeMillis();
                if (currClickTime - lastClickTime <= Config.SCROLL_TOP_DOUBLE_CLICK_DELAY) {
                    RvScrollTopUtils.smoothScrollTop(rv);
                }
                lastClickTime = currClickTime;
            }
        });

        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        mSmartRefreshUtils.setRefreshListener(new SmartRefreshUtils.RefreshListener() {
            @Override
            public void onRefresh() {
                currPage = PAGE_START;
                mPresenter.getMineShareArticleList(currPage);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MineShareArticleAdapter();
        RvAnimUtils.setAnim(mAdapter, SettingUtils.getInstance().getRvAnim());
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getMineShareArticleList(currPage);
            }
        }, rv);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.closeAll(null);
                ArticleBean item = mAdapter.getArticleBean(position);
                if (item == null) {
                    return;
                }
                switch (view.getId()) {
                    default:
                        break;
                    case R.id.rl_article:
                        WebViewActivity.start( getActivity(),item.getTitle(),item.getLink());
                        break;
                    case R.id.tv_delete:
                        mPresenter.deleteMineShareArticle(item);
                        break;
                }
            }
        });
        rv.setAdapter(mAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, new MultiStateUtils.SimpleListener() {
            @Override
            public void onResult() {
                msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
                currPage = PAGE_START;
                mPresenter.getMineShareArticleList(currPage);
            }
        });
    }

    @Override
    public void initData() {
        msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
        currPage = PAGE_START;
        mPresenter.getMineShareArticleList(currPage);
    }



    @Override
    public void getMineShareArticleListSuccess(int code, ArticleListBean data) {
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
            mAdapter.addData(data.getArticles());
            mAdapter.loadMoreComplete();
        }
        if (data.isOver()) {
            mAdapter.loadMoreEnd();
        }
        mSmartRefreshUtils.success();
    }

    @Override
    public void getMineShareArticleListFailed(int code, String msg) {
        showToast(msg);
        mSmartRefreshUtils.fail();
        mAdapter.loadMoreFail();
        if (currPage == PAGE_START) {
            msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }
}

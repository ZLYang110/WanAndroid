package com.zlyandroid.wanandroid.ui.mine.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpFragment;
import com.zlyandroid.wanandroid.core.ScrollTop;
import com.zlyandroid.wanandroid.event.CollectionEvent;
import com.zlyandroid.wanandroid.event.SettingChangeEvent;
import com.zlyandroid.wanandroid.ui.home.adapter.ArticleAdapter;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.mine.presenter.CollectionArticlePresenter;
import com.zlyandroid.wanandroid.ui.mine.view.CollectionArticleView;
import com.zlyandroid.wanandroid.ui.web.WebViewActivity;
import com.zlyandroid.wanandroid.util.MultiStateUtils;
import com.zlyandroid.wanandroid.util.RvAnimUtils;
import com.zlyandroid.wanandroid.util.RvScrollTopUtils;
import com.zlyandroid.wanandroid.util.SettingUtils;
import com.zlyandroid.wanandroid.util.SmartRefreshUtils;
import com.zlyandroid.wanandroid.widget.CollectView;
import com.zlylib.multistateview.MultiStateView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;


/**
 * @author zhangliyang
 * @date 2019/5/17
 * GitHub: https://github.com/ZLYang110
 */
public class CollectionArticleFragment extends BaseMvpFragment<CollectionArticlePresenter> implements ScrollTop, CollectionArticleView {

    public static final int PAGE_START = 0;

    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.rv)
    RecyclerView rv;

    private SmartRefreshUtils mSmartRefreshUtils;
    private ArticleAdapter mAdapter = null;

    private int currPage = PAGE_START;

    public static CollectionArticleFragment create() {
        return new CollectionArticleFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollectionEvent(CollectionEvent event) {
        if (isDetached()) {
            return;
        }
        if (event.isCollect()) {
            currPage = PAGE_START;
            mPresenter.getCollectArticleList(currPage);
        } else {
            mPresenter.getCollectArticleList(PAGE_START);
            if (event.getArticleId() != -1 || event.getCollectId() != -1) {
                mAdapter.forEach(new ArticleAdapter.ArticleForEach() {
                    @Override
                    public boolean forEach(int dataPos, int adapterPos, ArticleBean bean) {
                        if (event.getArticleId() != -1) {
                            if (bean.getOriginId() == event.getArticleId()) {
                                mAdapter.remove(adapterPos);
                                return true;
                            }
                        } else if (event.getCollectId() != -1) {
                            if (bean.getId() == event.getCollectId()) {
                                mAdapter.remove(adapterPos);
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChangeEvent(SettingChangeEvent event) {
        if (isDetached()) {
            return;
        }
        if (event.isRvAnimChanged()) {
            RvAnimUtils.setAnim(mAdapter, SettingUtils.getInstance().getRvAnim());
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }





    @Override
    protected CollectionArticlePresenter createPresenter() {
        return new CollectionArticlePresenter();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_collection_article;
    }

    @Nullable


    @Override
    public void initView() {
            mSmartRefreshUtils = SmartRefreshUtils.with(srl);
            mSmartRefreshUtils.pureScrollMode();
            mSmartRefreshUtils.setRefreshListener(new SmartRefreshUtils.RefreshListener() {
                @Override
                public void onRefresh() {
                    currPage = PAGE_START;
                    mPresenter.getCollectArticleList(currPage);
                }
            });
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            mAdapter = new ArticleAdapter();
            RvAnimUtils.setAnim(mAdapter, SettingUtils.getInstance().getRvAnim());
            mAdapter.setEnableLoadMore(false);
            mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    mPresenter.getCollectArticleList(currPage);
                }
            }, rv);
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    ArticleBean item = mAdapter.getArticleBean(position);
                    if (item != null) {
                        WebViewActivity.start( getActivity(),item.getTitle(),item.getLink());
                    }
                }
            });
            mAdapter.setOnItemChildViewClickListener(new ArticleAdapter.OnItemChildViewClickListener() {
                @Override
                public void onCollectClick(BaseViewHolder helper, CollectView v, int position) {
                    ArticleBean item = mAdapter.getArticleBean(position);
                    if (item != null) {
                        mPresenter.uncollectArticle(item, v);
                    }
                }


            });
            rv.setAdapter(mAdapter);
            MultiStateUtils.setEmptyAndErrorClick(msv, new MultiStateUtils.SimpleListener() {
                @Override
                public void onResult() {
                    msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
                    currPage = PAGE_START;
                    mPresenter.getCollectArticleList(currPage);
                }
            });

    }


    @Override
    public void initData() {
        msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
        mPresenter.getCollectArticleListCache(currPage);
    }

    @Override
    public void getCollectArticleListSuccess(int code, ArticleListBean data) {
        if (data.getDatas() != null) {
            for (ArticleBean articleBean : data.getArticles()) {
                articleBean.setCollect(true);
            }
        }
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
    public void getCollectArticleListFailed(int code, String msg) {
        show(msg);
        mSmartRefreshUtils.fail();
        mAdapter.loadMoreFail();
        if (currPage == PAGE_START) {
            msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    @Override
    public void scrollTop() {
        if (isAdded() && !isDetached()) {
            RvScrollTopUtils.smoothScrollTop(rv);
        }
    }
}

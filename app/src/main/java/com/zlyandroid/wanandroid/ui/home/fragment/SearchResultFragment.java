package com.zlyandroid.wanandroid.ui.home.fragment;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpFragment;
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.event.SettingChangeEvent;
import com.zlyandroid.wanandroid.ui.home.adapter.ArticleAdapter;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.presenter.SearchResultPresenter;
import com.zlyandroid.wanandroid.ui.home.view.SearchResultView;
import com.zlyandroid.wanandroid.ui.web.WebViewActivity;
import com.zlyandroid.wanandroid.util.MultiStateUtils;
import com.zlyandroid.wanandroid.util.RvAnimUtils;
import com.zlyandroid.wanandroid.util.SettingUtils;
import com.zlyandroid.wanandroid.util.SmartRefreshUtils;
import com.zlylib.multistateview.MultiStateView;
import com.zlylib.upperdialog.listener.SimpleListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class SearchResultFragment extends BaseMvpFragment<SearchResultPresenter> implements SearchResultView {

    private static final int PAGE_START = 0;

    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.rv)
    RecyclerView rv;

    private int currPage = PAGE_START;
    private SmartRefreshUtils mSmartRefreshUtils;
    private ArticleAdapter mAdapter;

    private String mKey;
    public static SearchResultFragment newInstance() {
        return new SearchResultFragment();
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
    protected SearchResultPresenter createPresenter() {
        return new SearchResultPresenter();
    }
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_search_result;
    }

    @Override
    public void initView() {
        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        mSmartRefreshUtils.setRefreshListener(new SmartRefreshUtils.RefreshListener() {
            @Override
            public void onRefresh() {
                currPage = PAGE_START;
                mPresenter.search(currPage, mKey);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ArticleAdapter();
        RvAnimUtils.setAnim(mAdapter, SettingUtils.getInstance().getRvAnim());
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.search(currPage, mKey);
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
                search(mKey);
            }
        });
    }

    @Override
    public void initData() {

    }
    public void search(String key){
        if (!isAdded()) {
            return;
        }
        msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
        mAdapter.setNewData(null);
        mKey = key;
        currPage = PAGE_START;
        mPresenter.search(currPage, mKey);
    }
    @Override
    public void searchSuccess(int code, ArticleListBean data) {
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
    public void searchFailed(int code, String msg) {
        show(msg);
        mSmartRefreshUtils.fail();
        mAdapter.loadMoreFail();
        if (currPage == PAGE_START) {
            msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }
}

package com.zlyandroid.wanandroid.ui.Knowledge.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpFragment;
import com.zlyandroid.wanandroid.core.ScrollTop;
import com.zlyandroid.wanandroid.ui.Knowledge.adapter.KnowledgeAdapter;
import com.zlyandroid.wanandroid.ui.Knowledge.adapter.NavigationAdapter;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.ChapterBean;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.NavigationBean;
import com.zlyandroid.wanandroid.ui.Knowledge.presenter.KnowledgePresenter;
import com.zlyandroid.wanandroid.ui.Knowledge.presenter.NavigationPresenter;
import com.zlyandroid.wanandroid.ui.Knowledge.view.KnowledgeView;
import com.zlyandroid.wanandroid.ui.Knowledge.view.NavigationView;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.web.WebViewActivity;
import com.zlyandroid.wanandroid.util.LogUtil;
import com.zlyandroid.wanandroid.util.MultiStateUtils;
import com.zlyandroid.wanandroid.util.RvScrollTopUtils;
import com.zlylib.multistateview.MultiStateView;

import java.util.List;

import butterknife.BindView;

/**
 * @author zhangliyang
 * @date 2019/5/12
 * GitHub: https://github.com/ZLYang110
 */

public class NavigationFragment extends BaseMvpFragment<NavigationPresenter> implements ScrollTop, NavigationView {

    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.rv)
    RecyclerView rv;


    private NavigationAdapter mAdapter;

    public static NavigationFragment create() {
        return new NavigationFragment();
    }


    @Override
    protected NavigationPresenter createPresenter() {
        return new NavigationPresenter();
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
            //mPresenter.getNaviList();
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_knowledge_navigation_child;
    }

    @Override
    public void initView() {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new NavigationAdapter();
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnItemClickListener(new NavigationAdapter.OnItemClickListener() {
            @Override
            public void onClick(ArticleBean bean, int pos) {
                WebViewActivity.start( getActivity(),bean);
            }
        });
        rv.setAdapter(mAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, new MultiStateUtils.SimpleListener() {
            @Override
            public void onResult() {
                msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
                mPresenter.getNaviList();
            }
        });
    }

    @Override
    public void initData() {
        msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
        mPresenter.getNaviListCache();
    }
    @Override
    public void scrollTop() {
        if (isAdded() && !isDetached()) {
            RvScrollTopUtils.smoothScrollTop(rv);
        }
    }

    @Override
    public void getNaviListSuccess(int code, List<NavigationBean> data) {
        mAdapter.setNewData(data);
        if (data == null || data.isEmpty()) {
            msv.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            msv.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
    }

    @Override
    public void getNaviListFail(int code, String msg) {
        show(msg);
        msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }


}

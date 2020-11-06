package com.zlyandroid.wanandroid.ui.Knowledge.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseFragment;
import com.zlyandroid.wanandroid.base.BaseMvpFragment;
import com.zlyandroid.wanandroid.core.ScrollTop;
import com.zlyandroid.wanandroid.ui.Knowledge.activity.KnowledgeArticleActivity;
import com.zlyandroid.wanandroid.ui.Knowledge.adapter.KnowledgeAdapter;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.ChapterBean;
import com.zlyandroid.wanandroid.ui.Knowledge.presenter.KnowledgePresenter;
import com.zlyandroid.wanandroid.ui.Knowledge.view.KnowledgeView;
import com.zlyandroid.wanandroid.ui.home.HomePageFragment;
import com.zlyandroid.wanandroid.util.LogUtil;
import com.zlyandroid.wanandroid.util.MultiStateUtils;
import com.zlyandroid.wanandroid.util.RvScrollTopUtils;
import com.zlylib.multistateview.MultiStateView;
import com.zlylib.upperdialog.listener.SimpleListener;

import java.util.List;

import butterknife.BindView;

/**
 * @author zhangliyang
 * @date 2019/5/12
 * GitHub: https://github.com/ZLYang110
 */

public class KnowledgeFragment extends BaseMvpFragment<KnowledgePresenter> implements ScrollTop,KnowledgeView {

    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.rv)
    RecyclerView rv;

    private KnowledgeAdapter mAdapter;

    public static KnowledgeFragment create() {
        return new KnowledgeFragment();
    }


    @Override
    protected KnowledgePresenter createPresenter() {
        return new KnowledgePresenter();
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
           // mPresenter.getKnowledgeListCache();
        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_knowledge_navigation_child;
    }

    @Override
    public void initView() {
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new KnowledgeAdapter();
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnItemClickListener(new KnowledgeAdapter.OnItemClickListener() {
            @Override
            public void onClick(ChapterBean bean, int pos) {
                KnowledgeArticleActivity.start(getContext(), bean, pos);
            }
        });
        rv.setAdapter(mAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, new MultiStateUtils.SimpleListener() {
            @Override
            public void onResult() {
                msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
                LogUtil.d("setEmptyAndErrorClick");
                mPresenter.getKnowledgeList();
            }
        });
    }

    @Override
    public void initData() {
        msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
        mPresenter.getKnowledgeListCache();
    }

    @Override
    public void scrollTop() {
        if (isAdded() && !isDetached()) {
            RvScrollTopUtils.smoothScrollTop(rv);
        }
    }
    @Override
    public void getKnowledgeListSuccess(int code, List<ChapterBean> data) {
        mAdapter.setNewData(data);
        LogUtil.d(data);
        if (data == null || data.isEmpty()) {
            msv.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            msv.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
    }

    @Override
    public void getKnowledgeListFail(int code, String msg) {
        show(msg);
        msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }


}

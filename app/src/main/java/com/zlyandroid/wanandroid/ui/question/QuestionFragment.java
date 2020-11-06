package com.zlyandroid.wanandroid.ui.question;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpFragment;
import com.zlyandroid.wanandroid.ui.home.adapter.ArticleAdapter;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;
import com.zlyandroid.wanandroid.ui.mine.presenter.MinePresenter;
import com.zlyandroid.wanandroid.ui.mine.view.MineView;
import com.zlyandroid.wanandroid.ui.question.presenter.QuestionPresenter;
import com.zlyandroid.wanandroid.ui.question.view.QuestionView;
import com.zlyandroid.wanandroid.ui.web.WebViewActivity;
import com.zlyandroid.wanandroid.util.MultiStateUtils;
import com.zlyandroid.wanandroid.util.RvAnimUtils;
import com.zlyandroid.wanandroid.util.SettingUtils;
import com.zlyandroid.wanandroid.util.SmartRefreshUtils;
import com.zlylib.multistateview.MultiStateView;
import com.zlylib.titlebarlib.ActionBarCommon;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class QuestionFragment extends BaseMvpFragment<QuestionPresenter> implements QuestionView {

    private static final int PAGE_START = 1;

    @BindView(R.id.abc)
    ActionBarCommon abc;
    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.rv)
    RecyclerView rv;

    private ArticleAdapter mAdapter;
    private SmartRefreshUtils mSmartRefreshUtils;

    private int currPage = PAGE_START;

    public static QuestionFragment create() {
        return new QuestionFragment();
    }

    @Override
    protected QuestionPresenter createPresenter() {
        return new QuestionPresenter();
    }
    @Override
    public int getLayoutID() {
        return R.layout.fragment_question;
    }

    @Override
    public void initView() {
        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        mSmartRefreshUtils.setRefreshListener(new SmartRefreshUtils.RefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });
        mSmartRefreshUtils.setLoadMoreListener(new SmartRefreshUtils.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.getQuestionList(currPage);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ArticleAdapter();
        RvAnimUtils.setAnim(mAdapter, SettingUtils.getInstance().getRvAnim());
        mAdapter.setEnableLoadMore(false);
        rv.setAdapter(mAdapter);
        //item 点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WebViewActivity.start( getActivity(),mAdapter.getArticleBean(position));
            }
        });

        MultiStateUtils.setEmptyAndErrorClick(msv, new MultiStateUtils.SimpleListener() {
            @Override
            public void onResult() {
                msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
                initData();
            }
        });
    }

    @Override
    public void initData() {
        msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
        mPresenter.getQuestionList(0);

    }



    @Override
    public void getQuestionListSuccess(int code, ArticleListBean data) {
        currPage = data.getCurPage() + PAGE_START;
        if (data.getCurPage() == PAGE_START) {

            mAdapter.setNewData(data.getDatas());
        }else {
            mAdapter.addData(data.getDatas());
            mAdapter.loadMoreComplete();
        }
        if (data.isOver()) {
            mAdapter.loadMoreEnd();
        } else {
            if (!mAdapter.isLoadMoreEnable()) {
                mAdapter.setEnableLoadMore(true);
            }
        }
        mSmartRefreshUtils.success();
        msv.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    }

    @Override
    public void getQuestionListFail(int code, String msg) {
        mSmartRefreshUtils.fail();
        mAdapter.loadMoreFail();
        msv.setViewState(MultiStateView.VIEW_STATE_EMPTY);
    }
}

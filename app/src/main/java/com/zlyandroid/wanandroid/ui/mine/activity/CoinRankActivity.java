package com.zlyandroid.wanandroid.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpActivity;
import com.zlyandroid.wanandroid.ui.core.dialog.WebDialog;
import com.zlyandroid.wanandroid.ui.home.activity.UserPageActivity;
import com.zlyandroid.wanandroid.ui.mine.adapter.CoinRankAdapter;
import com.zlyandroid.wanandroid.ui.mine.adapter.CoinRecordAdapter;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinInfoBean;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRankBean;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRecordBean;
import com.zlyandroid.wanandroid.ui.mine.presenter.CoinPresenter;
import com.zlyandroid.wanandroid.ui.mine.presenter.CoinRankPresenter;
import com.zlyandroid.wanandroid.ui.mine.view.CoinRankView;
import com.zlyandroid.wanandroid.ui.mine.view.CoinView;
import com.zlyandroid.wanandroid.util.AnimatorUtils;
import com.zlyandroid.wanandroid.util.MultiStateUtils;
import com.zlyandroid.wanandroid.util.RvAnimUtils;
import com.zlyandroid.wanandroid.util.SettingUtils;
import com.zlylib.multistateview.MultiStateView;
import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;

import butterknife.BindView;

public class CoinRankActivity extends BaseMvpActivity<CoinRankPresenter> implements CoinRankView {

    private static final int PAGE_START = 1;

    @BindView(R.id.abc)
    ActionBarCommon abc;
    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.rv)
    RecyclerView rv;

    private int currPage = PAGE_START;
    private CoinRankAdapter mAdapter = null;

    public static void start(Context context) {
        Intent intent = new Intent(context, CoinRankActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected CoinRankPresenter createPresenter() {
        return new CoinRankPresenter();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_coin_rank;
    }

    @Override
    public void initView() {

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CoinRankAdapter();
        RvAnimUtils.setAnim(mAdapter, SettingUtils.getInstance().getRvAnim());
        mAdapter.setEnableLoadMore(false);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getCoinRankList(currPage);
            }
        }, rv);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CoinInfoBean item = mAdapter.getItem(position);
                if (item != null) {
                    UserPageActivity.start(getContext(), item.getUserId());
                }
            }
        });
        rv.setAdapter(mAdapter);

        MultiStateUtils.setEmptyAndErrorClick(msv, new MultiStateUtils.SimpleListener() {
            @Override
            public void onResult() {
                msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
                currPage = PAGE_START;
                mPresenter.getCoinRankList(currPage);
            }
        });
    }

    @Override
    public void initData() {
        msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
        currPage = PAGE_START;
        mPresenter.getCoinRankList(currPage);
    }


    @Override
    public void getCoinRankListSuccess(int code, CoinRankBean data) {
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
    }

    @Override
    public void getCoinRankListFail(int code, String msg) {
        mAdapter.loadMoreFail();
        if (currPage == PAGE_START) {
            msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }
}

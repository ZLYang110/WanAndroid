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
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.ui.core.dialog.WebDialog;
import com.zlyandroid.wanandroid.ui.mine.adapter.CoinRecordAdapter;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRecordBean;
import com.zlyandroid.wanandroid.ui.mine.presenter.CoinPresenter;
import com.zlyandroid.wanandroid.ui.mine.view.CoinView;
import com.zlyandroid.wanandroid.util.AnimatorUtils;
import com.zlyandroid.wanandroid.util.MultiStateUtils;
import com.zlyandroid.wanandroid.util.RvAnimUtils;
import com.zlyandroid.wanandroid.util.SettingUtils;
import com.zlylib.multistateview.MultiStateView;
import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;

import butterknife.BindView;

public class CoinActivity extends BaseMvpActivity<CoinPresenter> implements CoinView {

    private static final int PAGE_START = 1;

    @BindView(R.id.abc)
    ActionBarCommon abc;
    @BindView(R.id.tv_coin)
    TextView tv_coin;
    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.rv)
    RecyclerView rv;

    private int currPage = PAGE_START;
    private CoinRecordAdapter mCoinRecordAdapter = null;

    public static void start(Context context) {
        Intent intent = new Intent(context, CoinActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected CoinPresenter createPresenter() {
        return new CoinPresenter();
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_coin;
    }

    @Override
    public void initView() {
        abc.setOnRightIconClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                WebDialog.create(getContext(), "https://www.wanandroid.com/blog/show/2653").show();
            }
        });
        tv_coin.setText("0");
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mCoinRecordAdapter = new CoinRecordAdapter();
        RvAnimUtils.setAnim(mCoinRecordAdapter, SettingUtils.getInstance().getRvAnim());
        mCoinRecordAdapter.setEnableLoadMore(false);
        mCoinRecordAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getCoinRecordList(currPage);
            }
        }, rv);
        rv.setAdapter(mCoinRecordAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, new MultiStateUtils.SimpleListener() {
            @Override
            public void onResult() {
                msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
                currPage = PAGE_START;
                mPresenter.getCoinRecordList(currPage);
            }
        });
    }

    @Override
    public void initData() {
        mPresenter.getCoin();
        msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
        currPage = PAGE_START;
        mPresenter.getCoinRecordList(currPage);
    }

    @Override
    public void getCoinSuccess(int code, int coin) {
        AnimatorUtils.doIntAnim(tv_coin, coin, 1000);
    }


    @Override
    public void getCoinFail(int code, String msg) {
showToast(msg);
    }

    @Override
    public void getCoinRecordListSuccess(int code, CoinRecordBean data) {
        currPage = data.getCurPage() + PAGE_START;
        if (data.getCurPage() == 1) {
            mCoinRecordAdapter.setNewData(data.getDatas());
            mCoinRecordAdapter.setEnableLoadMore(true);
            if (data.getDatas() == null || data.getDatas().isEmpty()) {
                msv.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            } else {
                msv.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }
        } else {
              mCoinRecordAdapter.addData(data.getDatas());
            mCoinRecordAdapter.loadMoreComplete();
        }
        if (data.isOver()) {
            mCoinRecordAdapter.loadMoreEnd();
        }
    }

    @Override
    public void getCoinRecordListFail(int code, String msg) {
        mCoinRecordAdapter.loadMoreFail();
        if (currPage == PAGE_START) {
            msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }
}

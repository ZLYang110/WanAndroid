package com.zlyandroid.wanandroid.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpActivity;
import com.zlyandroid.wanandroid.db.greendao.ReadLaterModel;
import com.zlyandroid.wanandroid.db.greendao.ReadRecordModel;
import com.zlyandroid.wanandroid.db.readlater.DbReadLaterHelperImpl;
import com.zlyandroid.wanandroid.db.readrecord.DbReadRecordHelperImpl;
import com.zlyandroid.wanandroid.event.SettingChangeEvent;
import com.zlyandroid.wanandroid.ui.mine.adapter.ReadLaterAdapter;
import com.zlyandroid.wanandroid.ui.mine.adapter.ReadRecordAdapter;
import com.zlyandroid.wanandroid.ui.mine.presenter.ReadRecordPresenter;
import com.zlyandroid.wanandroid.ui.mine.presenter.ReadlaterPresenter;
import com.zlyandroid.wanandroid.ui.mine.view.ReadLaterView;
import com.zlyandroid.wanandroid.ui.mine.view.ReadRecordView;
import com.zlyandroid.wanandroid.ui.web.WebViewActivity;
import com.zlyandroid.wanandroid.util.CopyUtils;
import com.zlyandroid.wanandroid.util.IntentUtils;
import com.zlyandroid.wanandroid.util.LogUtil;
import com.zlyandroid.wanandroid.util.RvAnimUtils;
import com.zlyandroid.wanandroid.util.SettingUtils;
import com.zlyandroid.wanandroid.util.SmartRefreshUtils;
import com.zlylib.multistateview.MultiStateView;
import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;
import com.zlylib.upperdialog.TipDialog;
import com.zlylib.upperdialog.listener.SimpleCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class ReadLaterActivity extends BaseMvpActivity<ReadlaterPresenter> implements ReadLaterView {

    private static final int PAGE_START = 1;

    @BindView(R.id.abc)
    ActionBarCommon abc;
    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.rv)
    RecyclerView rv;

    private SmartRefreshUtils mSmartRefreshUtils;
    private ReadLaterAdapter mAdapter;



    public static void start(Context context) {
        Intent intent = new Intent(context, ReadLaterActivity.class);
        context.startActivity(intent);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChangeEvent(SettingChangeEvent event) {
        if (isDestroyed()) {
            return;
        }
        if (event.isRvAnimChanged()) {
            RvAnimUtils.setAnim(mAdapter, SettingUtils.getInstance().getRvAnim());
        }
    }
    @Override
    protected ReadlaterPresenter createPresenter() {
        return new ReadlaterPresenter();
    }
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
    @Override
    public int getLayoutID() {
        return R.layout.activity_read_later;
    }

    @Override
    public void initView() {
        abc.setOnRightTextClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                TipDialog.with(getContext())
                        .message("确定要全部删除吗？")
                        .onYes(new SimpleCallback<Void>() {
                            @Override
                            public void onResult(Void data) {
                                DbReadLaterHelperImpl.getInstance().clearAllData();
                                mAdapter.setNewData(null);
                                msv.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                            }
                        })
                        .show();
            }
        });
        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        mSmartRefreshUtils.setRefreshListener(new SmartRefreshUtils.RefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getReadLaterList();
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ReadLaterAdapter();
        RvAnimUtils.setAnim(mAdapter, SettingUtils.getInstance().getRvAnim());
        mAdapter.setEnableLoadMore(false);

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.closeAll(null);
                ReadLaterModel item = mAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                switch (view.getId()) {
                    default:
                        break;
                    case R.id.rl_top:
                        WebViewActivity.start( getActivity(),item.getTitle(),item.getLink());
                        break;
                    case R.id.tv_copy:
                        CopyUtils.copyText(item.getLink());
                        showToast("复制成功");
                        break;
                    case R.id.tv_open:
                        if (TextUtils.isEmpty(item.getLink())) {
                            showToast("链接为空");
                            break;
                        }
                        if (getContext() != null) {
                            IntentUtils.openBrowser(getContext(), item.getLink());
                        }
                        break;
                    case R.id.tv_delete:
                        if (item != null) {
                            mPresenter.delectReadLaterById(item.getId());
                            mAdapter.remove(position);
                        }
                        break;
                }
            }
        });
        rv.setAdapter(mAdapter);
    }

    @Override
    public void initData() {
        msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
        mPresenter.getReadLaterList();
    }



    @Override
    public void getReadLaterListSuccess(List<ReadLaterModel> data) {
        if(data.size()>0){
            mAdapter.setNewData(data);
            msv.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }else{
            mAdapter.setNewData(null);
            msv.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            abc.getRightTextView().setVisibility(View.GONE);
        }
        mSmartRefreshUtils.success();
        mAdapter.loadMoreEnd();
    }

    @Override
    public void getReadLaterListFailed() {
        mAdapter.loadMoreFail();
        msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void removeReadLaterSuccess(List<ReadLaterModel> data) {
        if(data.size()==0){
            abc.getRightTextView().setVisibility(View.GONE);
        }
    }

    @Override
    public void removeReadLaterFailed() {

    }
}

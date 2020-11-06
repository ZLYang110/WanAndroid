package com.zlyandroid.wanandroid.ui.mine.fragment;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewParent;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpFragment;
import com.zlyandroid.wanandroid.core.ScrollTop;
import com.zlyandroid.wanandroid.event.CollectionEvent;
import com.zlyandroid.wanandroid.event.SettingChangeEvent;
import com.zlyandroid.wanandroid.ui.home.bean.CollectionLinkBean;
import com.zlyandroid.wanandroid.ui.mine.adapter.CollectionLinkAdapter;
import com.zlyandroid.wanandroid.ui.mine.presenter.CollectionLinkPresenter;
import com.zlyandroid.wanandroid.ui.mine.view.CollectionLinkView;
import com.zlyandroid.wanandroid.ui.web.WebViewActivity;
import com.zlyandroid.wanandroid.util.CopyUtils;
import com.zlyandroid.wanandroid.util.IntentUtils;
import com.zlyandroid.wanandroid.util.MultiStateUtils;
import com.zlyandroid.wanandroid.util.RvAnimUtils;
import com.zlyandroid.wanandroid.util.RvScrollTopUtils;
import com.zlyandroid.wanandroid.util.SettingUtils;
import com.zlyandroid.wanandroid.util.SmartRefreshUtils;
import com.zlylib.multistateview.MultiStateView;
import com.zlylib.upperdialog.listener.SimpleCallback;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
/**
 * @author zhangliyang
 * @date 2019/5/17
 * GitHub: https://github.com/ZLYang110
 */
public class CollectionLinkFragment extends BaseMvpFragment<CollectionLinkPresenter> implements ScrollTop, CollectionLinkView {

    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.rv)
    RecyclerView rv;

    private SmartRefreshUtils mSmartRefreshUtils;
    private CollectionLinkAdapter mAdapter;

    public static CollectionLinkFragment create() {
        return new CollectionLinkFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollectionEvent(CollectionEvent event) {
        if (isDetached()) {
            return;
        }
        if (event.isCollect()) {
            mPresenter.getCollectLinkList();
        } else {
            mPresenter.getCollectLinkList();
            if (event.getCollectId() != -1) {
                List<CollectionLinkBean> list = mAdapter.getData();
                for (int i = 0; i < list.size(); i++) {
                    CollectionLinkBean item = list.get(i);
                    if (item.getId() == event.getCollectId()) {
                        mAdapter.remove(i);
                        break;
                    }
                }
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
    protected CollectionLinkPresenter createPresenter() {
        return new CollectionLinkPresenter();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_collection_link;
    }

    @Override
    public void initView() {
        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        mSmartRefreshUtils.setRefreshListener(new SmartRefreshUtils.RefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getCollectLinkList();
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new CollectionLinkAdapter();
        RvAnimUtils.setAnim(mAdapter, SettingUtils.getInstance().getRvAnim());
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.closeAll(null);
                CollectionLinkBean item = mAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                switch (view.getId()) {
                    default:
                        break;
                    case R.id.rl_top:
                        WebViewActivity.start( getActivity(),item.getName(),item.getLink());
                        break;
                    case R.id.tv_copy:
                        CopyUtils.copyText(item.getLink());
                        show("复制成功");
                        break;
                    case R.id.tv_open:
                        if (TextUtils.isEmpty(item.getLink())) {
                            show("链接为空");
                            break;
                        }
                        if (getContext() != null) {
                            IntentUtils.openBrowser(getContext(), item.getLink());
                        }
                        break;
                    case R.id.tv_edit:

                        break;
                    case R.id.tv_delete:
                        break;
                }
            }
        });
        rv.setAdapter(mAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, new MultiStateUtils.SimpleListener() {
            @Override
            public void onResult() {
                msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
                mPresenter.getCollectLinkList();
            }
        });

    }

    @Override
    public void initData() {
        msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
        mPresenter.getCollectLinkList();
    }



    @Override
    public void getCollectLinkListSuccess(int code, List<CollectionLinkBean> data) {
        List<CollectionLinkBean> copyList;
        if (data != null) {
            copyList = new ArrayList<>(data);
        } else {
            copyList = new ArrayList<>(0);
        }
        Collections.reverse(copyList);
        mAdapter.setNewData(copyList);
        mSmartRefreshUtils.success();
        if (copyList.isEmpty()) {
            msv.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            msv.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
    }

    @Override
    public void getCollectLinkListFailed(int code, String msg) {
        show(msg);
        mSmartRefreshUtils.fail();
        msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void updateCollectLinkSuccess(int code, CollectionLinkBean data) {
        List<CollectionLinkBean> list = mAdapter.getData();
        for (int i = 0; i < list.size(); i++) {
            CollectionLinkBean bean = list.get(i);
            if (bean.getId() == data.getId()) {
                bean.setName(data.getName());
                bean.setLink(data.getLink());
                mAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    @Override
    public void scrollTop() {
        if (isAdded() && !isDetached()) {
            RvScrollTopUtils.smoothScrollTop(rv);
        }
    }
}

package com.zlyandroid.wanandroid.ui.home.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpActivity;
import com.zlyandroid.wanandroid.ui.home.adapter.ArticleAdapter;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.presenter.UserPagePresenter;
import com.zlyandroid.wanandroid.ui.home.view.UserPageView;
import com.zlyandroid.wanandroid.ui.mine.bean.UserPageBean;
import com.zlyandroid.wanandroid.ui.web.WebViewActivity;
import com.zlyandroid.wanandroid.util.CopyUtils;
import com.zlyandroid.wanandroid.util.MultiStateUtils;
import com.zlyandroid.wanandroid.util.RandomUtils;
import com.zlyandroid.wanandroid.util.RvAnimUtils;
import com.zlyandroid.wanandroid.util.SettingUtils;
import com.zlyandroid.wanandroid.util.SmartRefreshUtils;
import com.zlylib.multistateview.MultiStateView;
import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;

import java.util.Random;

import butterknife.BindView;


/**
 * @author zhangliyang
 * @date 2019/5/17
 * GitHub: https://github.com/ZLYang110
 */
public class UserPageActivity extends BaseMvpActivity<UserPagePresenter> implements UserPageView {

    private static String TAG = "UserPageActivity";
    private static final int PAGE_START = 1;

    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.msv_list)
    MultiStateView msv_list;
    @BindView(R.id.cl)
    CoordinatorLayout cl;
    @BindView(R.id.ctbl)
    CollapsingToolbarLayout ctbl;
    @BindView(R.id.abl)
    AppBarLayout abl;
    @BindView(R.id.abc)
    ActionBarCommon abc;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.iv_blur)
    ImageView iv_blur;
    @BindView(R.id.rl_user_info)
    RelativeLayout rl_user_info;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.tv_user_name)
    TextView tv_user_name;
    @BindView(R.id.tv_user_id)
    TextView tv_user_id;
    @BindView(R.id.tv_user_coin)
    TextView tv_user_coin;
    @BindView(R.id.tv_user_ranking)
    TextView tv_user_ranking;

    private SmartRefreshUtils mSmartRefreshUtils;
    private ArticleAdapter mAdapter;

    private int currPage = PAGE_START;
    private int mUserId = -1;

    public static void start(Context context, int userId) {
        Log.d(TAG,"getUserPage  id =="  + userId);
        Intent intent = new Intent(context, UserPageActivity.class);
        intent.putExtra("id", userId);
        context.startActivity(intent);
    }

    @Override
    protected UserPagePresenter createPresenter() {
        return new UserPagePresenter();
    }

    @Override
    public boolean isRegisterEventBus() {
        return false;
    }
    @Override
    public int getLayoutID() {
        return R.layout.activity_user_page;
    }

    @Override
    public void initView() {
        mUserId = getUserIdFromIntent(this.getIntent());
        Log.d(TAG,"getUserPage" + mUserId);
        abc.setOnRightTextClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                String userId = String.valueOf(mUserId);
                String salt = RandomUtils.randomLetter(10 - userId.length());
                StringBuilder id = new StringBuilder();
                Random random = new Random();
                for (int i = 0; i < userId.length(); i++) {
                    int l = userId.length() - i;
                    int maxi = salt.length() - l - 1;
                    maxi = maxi < 2 ? 2 : maxi;
                    int ii = random.nextInt(maxi);
                    id.append(salt.substring(0, ii));
                    id.append(userId.charAt(i));
                    salt = salt.substring(ii);
                }
                id.append(salt);
                StringBuilder s = new StringBuilder();
                s.append("【玩口令】你的好友给你分享了一个神秘用户，户制泽条消息");
                s.append("打開最美玩安卓客户端揭开他/她的神秘面纱");
                CopyUtils.copyText(s.toString());
                showToast("口令已复制");
            }
        });
        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        mSmartRefreshUtils.setRefreshListener(new SmartRefreshUtils.RefreshListener() {
            @Override
            public void onRefresh() {
                currPage = PAGE_START;
                getUserPage(true);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ArticleAdapter();
        RvAnimUtils.setAnim(mAdapter, SettingUtils.getInstance().getRvAnim());
        mAdapter.setEnableLoadMore(false);

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getUserPage(true);
            }
        }, rv);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ArticleBean item = mAdapter.getArticleBean(position);
                if (item != null) {
                    WebViewActivity.start( getActivity(),item);
                }
            }
        });

        rv.setAdapter(mAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, new MultiStateUtils.SimpleListener() {
            @Override
            public void onResult() {
                msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
                currPage = PAGE_START;
                getUserPage(true);
            }
        });
        MultiStateUtils.setEmptyAndErrorClick(msv_list, new MultiStateUtils.SimpleListener() {
            @Override
            public void onResult() {
                msv_list.setViewState(MultiStateView.VIEW_STATE_LOADING);
                currPage = PAGE_START;
                getUserPage(true);
            }
        });

        srl.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                if (iv_blur != null && rl_user_info != null) {
                    iv_blur.getLayoutParams().height = rl_user_info.getMeasuredHeight() + offset;
                    iv_blur.requestLayout();
                }
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {
            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {
            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
            }

            @Override
            public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {
                if (iv_blur != null && rl_user_info != null) {
                    iv_blur.getLayoutParams().height = rl_user_info.getMeasuredHeight() - offset;
                    iv_blur.requestLayout();
                }
            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {
            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {
            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            }

            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
            }
        });

        abl.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout abl, int offset) {
                if (Math.abs(offset) == abl.getTotalScrollRange()) {
                    abc.getTitleTextView().setAlpha(1F);
                    abc.setBackgroundResource(R.color.basic_ui_action_bar_bg);
                } else {
                    abc.getTitleTextView().setAlpha(0F);
                    abc.setBackgroundResource(R.color.transparent);
                }
            }
        });
        ctbl.post(new Runnable() {
            @Override
            public void run() {
                ctbl.setMinimumHeight(abc.getMeasuredHeight());
                ctbl.setScrimVisibleHeightTrigger(abc.getMeasuredHeight());
            }
        });
    }
    public void getUserPage(boolean refresh) {
        Log.d(TAG,"getUserPage"  );
        Log.d(TAG,"getUserPage" +mUserId + "---"+currPage);
        mPresenter.getUserPage(mUserId, currPage);
    }
    @Override
    public void initData() {
        msv.setViewState(MultiStateView.VIEW_STATE_LOADING);
        msv_list.setVisibility(View.GONE);
        currPage = PAGE_START;
        getUserPage(false);
    }


    @Override
    public void getUserPageSuccess(UserPageBean data) {
        currPage = data.getShareArticles().getCurPage() + PAGE_START;
        if (data.getShareArticles().getCurPage() == 1) {
            abc.getTitleTextView().setText(data.getCoinInfo().getUsername());
            tv_user_name.setText(data.getCoinInfo().getUsername());
            tv_user_id.setText("" + data.getCoinInfo().getUserId());
            tv_user_coin.setText("" + data.getCoinInfo().getCoinCount());
            tv_user_ranking.setText("" + data.getCoinInfo().getRank());
            mAdapter.setNewData(data.getShareArticles().getDatas());
            mAdapter.setEnableLoadMore(true);
            msv_list.setVisibility(View.VISIBLE);

            if (data.getShareArticles().getDatas() == null || data.getShareArticles().getDatas().isEmpty()) {
                msv_list.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            } else {
                msv_list.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                msv.setViewState(MultiStateView.VIEW_STATE_CONTENT);

            }
        } else {
            mAdapter.addData(data.getShareArticles().getDatas());
            mAdapter.loadMoreComplete();
        }
        if (data.getShareArticles().isOver()) {
            mAdapter.loadMoreEnd();
        }
        mSmartRefreshUtils.success();
    }

    @Override
    public void getUserPageFail(String msg) {
        showToast(msg);
        mAdapter.loadMoreFail();
        if (currPage == PAGE_START) {
            msv.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }
    }

    private int getUserIdFromIntent(Intent intent) {
        int id = mUserId;
        try {
            id = intent.getIntExtra("id", id);
        } catch (NullPointerException ignore) {
        }
        if (id < 0) {
            abc.getRightTextView().setVisibility(View.GONE);
        } else {
            abc.getRightTextView().setVisibility(View.VISIBLE);
        }
        return id;
    }
}

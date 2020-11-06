package com.zlyandroid.wanandroid.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoaderInterface;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpFragment;
import com.zlyandroid.wanandroid.db.readrecord.DbReadRecordHelperImpl;
import com.zlyandroid.wanandroid.event.SettingChangeEvent;
import com.zlyandroid.wanandroid.ui.core.dialog.WebDialog;
import com.zlyandroid.wanandroid.ui.home.activity.ScanActivity;
import com.zlyandroid.wanandroid.ui.home.activity.SearchActivity;
import com.zlyandroid.wanandroid.ui.home.adapter.ArticleAdapter;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.BannerBean;
import com.zlyandroid.wanandroid.ui.home.presenter.HomePresenter;
import com.zlyandroid.wanandroid.ui.home.view.HomeView;
import com.zlyandroid.wanandroid.ui.web.WebViewActivity;
import com.zlyandroid.wanandroid.util.ImageLoader;
import com.zlyandroid.wanandroid.util.MultiStateUtils;
import com.zlyandroid.wanandroid.util.RvAnimUtils;
import com.zlyandroid.wanandroid.util.SettingUtils;
import com.zlyandroid.wanandroid.util.SmartRefreshUtils;
import com.zlyandroid.wanandroid.util.display.DisplayInfoUtils;
import com.zlyandroid.wanandroid.widget.CollectView;
import com.zlylib.multistateview.MultiStateView;
import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;
import com.zlylib.upperdialog.manager.Layer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomePageFragment extends BaseMvpFragment<HomePresenter> implements HomeView {

    private static final String TAG = "HomePageFragment";
    private static final int PAGE_START = 0;

    @BindView(R.id.abc)
    ActionBarCommon abc;
    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.rv)
    RecyclerView rv;

    private Banner mBanner;
    private ArticleAdapter mAdapter;
    private SmartRefreshUtils mSmartRefreshUtils;
    private List<Object> mBannerDatas;
    private int currPage = PAGE_START;
    private WebDialog mWebDialog;

    public static HomePageFragment create() {
        return new HomePageFragment();
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }
    @Override
    public int getLayoutID() {
        return R.layout.fragment_home;
    }
    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSettingChangeEvent(SettingChangeEvent event) {
        if (isDetached()) {
            return;
        }
        if (event.isShowTopChanged()) {
            if (SettingUtils.getInstance().isShowTop()) {
                mPresenter.getTopArticleList();
            } else {
                removeTopItems();
            }
        }
        if (event.isShowBannerChanged()) {
            createHeaderBanner();
            if (SettingUtils.getInstance().isShowBanner()) {
                mPresenter.getBanner();
            }
        }
        if (event.isRvAnimChanged()) {
            RvAnimUtils.setAnim(mAdapter, SettingUtils.getInstance().getRvAnim());
        }
    }


    @Override
    public void initView() {
        abc.setOnRightIconClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.start(getActivity());
            }
        });
        abc.setOnLeftIconClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                ScanActivity.start(getActivity());
            }
        });
        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        mSmartRefreshUtils.setRefreshListener(new SmartRefreshUtils.RefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG,"loadData setRefreshListener" );
                initData();
            }
        });
        mSmartRefreshUtils.setLoadMoreListener(new SmartRefreshUtils.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPresenter.getArticleList(currPage);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ArticleAdapter();
        RvAnimUtils.setAnim(mAdapter, SettingUtils.getInstance().getRvAnim());
        mAdapter.setEnableLoadMore(false);
        rv.setAdapter(mAdapter);
        createHeaderBanner();
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                showWebDialog(position);
                return true;
            }
        });
        //item 点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                 WebViewActivity.start( getActivity(),mAdapter.getArticleBean(position));
            }
        });
        mAdapter.setOnItemChildViewClickListener(new ArticleAdapter.OnItemChildViewClickListener() {
            @Override
            public void onCollectClick(BaseViewHolder helper, CollectView v, int position) {
                ArticleBean item = mAdapter.getArticleBean(position);
                if (item != null) {
                    if (v.isChecked()) {
                        mPresenter.collect(item, v);
                    } else {
                        mPresenter.uncollect(item, v);
                    }
                }
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

        Log.d(TAG,"loadData " );
        if (SettingUtils.getInstance().isShowBanner()) {
            mPresenter.getBanner();
        }

        if (SettingUtils.getInstance().isShowTop()) {
            mPresenter.getTopArticleList();
        }
        mPresenter.getArticleList(0);

    }



    @Override
    public void getBannerSuccess(int code, List<BannerBean> data) {
        mSmartRefreshUtils.success();
        if (mBannerDatas == null) {
            mBannerDatas = new ArrayList<>();
        }
        mBannerDatas.clear();
        mBannerDatas.addAll(data);
        refreshBannerTitles();
        mBanner.setImages(mBannerDatas);
        mBanner.start();

    }
    private void refreshBannerTitles() {
        List<String> titles = new ArrayList<>(mBannerDatas.size());
        for (Object bean : mBannerDatas) {
            if (bean instanceof BannerBean) {
                titles.add(((BannerBean) bean).getTitle());
            } else {
                titles.add("");
            }
        }
        Log.d(TAG,"refreshBannerTitles"+titles.toString());
        mBanner.setBannerTitles(titles);
    }
    @Override
    public void getBannerFail(int code, String msg) { }

    @Override
    public void getArticleListSuccess(int code, ArticleListBean data) {
        currPage = data.getCurPage() + PAGE_START;
        if (data.getCurPage() == 1) {
            List<MultiItemEntity> newList = new ArrayList<>();
            List<MultiItemEntity> oldList = mAdapter.getData();
            for (MultiItemEntity entity : oldList) {
                if (entity.getItemType() == ArticleAdapter.ITEM_TYPE_ARTICLE) {
                    ArticleBean bean = (ArticleBean) entity;
                    if (bean.isTop()) {
                        newList.add(bean);
                    }
                }
            }
            newList.addAll(data.getDatas());
            mAdapter.setNewData(data.getDatas());
        }else {
            mAdapter.addData(data.getDatas());
            if (mWebDialog != null) {
                mWebDialog.notifyDataSetChanged();
            }
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
    public void getArticleListFail(int code, String msg) {
      //  show(msg);
        mSmartRefreshUtils.fail();
        mAdapter.loadMoreFail();
        msv.setViewState(MultiStateView.VIEW_STATE_EMPTY);
    }

    @Override
    public void getTopArticleListSuccess(int code, List<ArticleBean> data) {
        msv.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        for (ArticleBean bean : data) {
            bean.setTop(true);
        }
        List<MultiItemEntity> newList = new ArrayList<>(data);
        List<MultiItemEntity> oldList = mAdapter.getData();
        for (MultiItemEntity entity : oldList) {
            if (entity.getItemType() == ArticleAdapter.ITEM_TYPE_ARTICLE) {
                ArticleBean bean = (ArticleBean) entity;
                if (!bean.isTop()) {
                    newList.add(bean);
                }
            }
        }
        mAdapter.setNewData(newList);
    }

    @Override
    public void getTopArticleListFailed(int code, String msg) {
        msv.setViewState(MultiStateView.VIEW_STATE_EMPTY);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mBanner != null) {
            mBanner.startAutoPlay();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mBanner != null) {
            mBanner.stopAutoPlay();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    //删除置顶
    private void removeTopItems() {
        List<MultiItemEntity> list = mAdapter.getData();
        int from = -1;
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            MultiItemEntity entity = list.get(i);
            if (from < 0) {
                if (entity.getItemType() == ArticleAdapter.ITEM_TYPE_ARTICLE) {
                    ArticleBean bean = (ArticleBean) entity;
                    if (bean.isTop()) {
                        from = i;
                    }
                }
            }
            if (from >= 0) {
                if (entity.getItemType() == ArticleAdapter.ITEM_TYPE_ARTICLE) {
                    ArticleBean bean = (ArticleBean) entity;
                    if (!bean.isTop()) {
                        break;
                    }
                }
                count++;
            }
        }
        if (from >= 0) {
            for (int i = 0; i < count; i++) {
                mAdapter.remove(from);
            }
        }
    }

    //长按弹出WebDialog
    private void showWebDialog(int position) {
        mWebDialog = WebDialog.create(getActivity(), null, mAdapter.getData(), position);
        mWebDialog.setOnPageChangedListener(new WebDialog.OnPageChangedListener() {
            @Override
            public void onPageChanged(int pos, ArticleBean data) {
                int headerCount = mAdapter.getHeaderLayoutCount();
                int currItemPos = 0;
                List<MultiItemEntity> datas = mAdapter.getData();
                for (int i = 0; i < datas.size(); i++) {
                    MultiItemEntity entity = datas.get(i);
                    if (entity.getItemType() == ArticleAdapter.ITEM_TYPE_ARTICLE) {
                        ArticleBean bean = (ArticleBean) entity;
                        if (bean.getId() == data.getId()) {
                            currItemPos = headerCount + i;
                            //DbReadRecordHelperImpl.getInstance().add(bean.getTitle(),bean.getLink());
                            break;

                        }
                    }
                }
                if (currItemPos < 0) {
                    currItemPos = 0;
                }
                if (currItemPos > mAdapter.getItemCount() - 1) {
                    currItemPos = mAdapter.getItemCount() - 1;
                }
                rv.smoothScrollToPosition(currItemPos);
            }
        });
        mWebDialog.onDismissListener(new Layer.OnDismissListener() {
            @Override
            public void onDismissing(Layer layer) {
            }

            @Override
            public void onDismissed(Layer layer) {
                mWebDialog = null;
            }
        });
        mWebDialog.show();
    }

    private void createHeaderBanner() {
          Log.d(TAG,"createHeaderBanner mBanner" +mBanner +"getActivity()" +getActivity() );
        if (mBanner == null && getActivity() != null) {
            mBanner = new Banner(getActivity());
            int height = (int) (DisplayInfoUtils.getInstance().getWidthPixels() * (9F / 16F));

            mBanner.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            mBanner.setImageLoader(new ImageLoaderInterface<FrameLayout>() {
                @Override
                public FrameLayout createImageView(Context context) {
                    FrameLayout container = new FrameLayout(context);
                    container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    return container;
                }

                @Override
                public void displayImage(Context context, Object data, FrameLayout container) {
                    if (data instanceof BannerBean) {
                        ImageView imageView = new ImageView(context);
                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        if (container.getChildCount() > 0) {
                            container.removeAllViews();
                        }
                        container.addView(imageView);

                        ImageLoader.banner(imageView, ((BannerBean) data).getImagePath());
                    }
                }
            });
            mBanner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);//设置页码与标题
            mBanner.setBannerAnimation(Transformer.Default);
            mBanner.startAutoPlay();
            mBanner.setDelayTime(2000);   //设置轮播时间
            //设置标题集合（当banner样式有显示title时）
            mBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Object obj = mBannerDatas.get(position);
                    if (obj instanceof BannerBean) {
                        BannerBean bean = (BannerBean) obj;
                        show(bean.getTitle());
                    }
                }
            });
            mAdapter.addHeaderView(mBanner, 0);
        }
        if (SettingUtils.getInstance().isShowBanner()) {
            mBanner.setVisibility(View.VISIBLE);
        } else {
            mBanner.setVisibility(View.GONE);
        }
    }

}

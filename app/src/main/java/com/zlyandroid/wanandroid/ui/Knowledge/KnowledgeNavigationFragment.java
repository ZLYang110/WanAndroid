package com.zlyandroid.wanandroid.ui.Knowledge;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseFragment;
import com.zlyandroid.wanandroid.base.BaseMvpFragment;
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.core.Config;
import com.zlyandroid.wanandroid.core.ScrollTop;
import com.zlyandroid.wanandroid.ui.Knowledge.fragment.KnowledgeFragment;
import com.zlyandroid.wanandroid.ui.Knowledge.fragment.NavigationFragment;
import com.zlyandroid.wanandroid.ui.core.adapter.FixedFragmentPagerAdapter;
import com.zlyandroid.wanandroid.ui.mine.presenter.MinePresenter;
import com.zlyandroid.wanandroid.util.MagicIndicatorUtils;
import com.zlylib.titlebarlib.widget.ActionBarEx;
import com.zlylib.upperdialog.listener.SimpleCallback;

import butterknife.BindView;


public class KnowledgeNavigationFragment extends BaseFragment implements ScrollTop {

    @BindView(R.id.ab)
    ActionBarEx ab;
    @BindView(R.id.vp)
    ViewPager vp;

    private FixedFragmentPagerAdapter mAdapter;

    private long lastClickTime = 0L;
    private int lastClickPos = 0;


    public static KnowledgeNavigationFragment create() {
        return new KnowledgeNavigationFragment();
    }


    @Override
    public int getLayoutID() {
        return R.layout.fragment_knowledge_navigation;
    }

    @Override
    public void initView() {
        mAdapter = new FixedFragmentPagerAdapter(getChildFragmentManager());
        mAdapter.setTitles("体系", "导航");
        mAdapter.setFragmentList(
                KnowledgeFragment.create(),
                NavigationFragment.create()
        );
        vp.setAdapter(mAdapter);
        vp.setOffscreenPageLimit(2);
        MagicIndicatorUtils.commonNavigator(ab.getView(R.id.mi), vp, mAdapter, new SimpleCallback<Integer>(){
            @Override
            public void onResult(Integer data) {
                 notifyScrollTop(data);
            }
        });

    }

    @Override
    public void initData() {

    }

    @Override
    public void scrollTop() {
        if (isAdded() && !isDetached()) {
            Fragment fragment = mAdapter.getItem(vp.getCurrentItem());
            if (fragment instanceof ScrollTop) {
                ScrollTop scrollTop = (ScrollTop) fragment;
                scrollTop.scrollTop();
            }
        }
    }
    private void notifyScrollTop(int pos) {
        long currClickTime = System.currentTimeMillis();
        if (lastClickPos == pos && currClickTime - lastClickTime <= Config.SCROLL_TOP_DOUBLE_CLICK_DELAY) {
            Fragment fragment = mAdapter.getItem(vp.getCurrentItem());
            if (fragment instanceof ScrollTop) {
                ScrollTop scrollTop = (ScrollTop) fragment;
                scrollTop.scrollTop();
            }
        }
        lastClickPos = pos;
        lastClickTime = currClickTime;
    }


}

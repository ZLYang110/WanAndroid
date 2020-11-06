package com.zlyandroid.wanandroid.ui.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseActivity;
import com.zlyandroid.wanandroid.core.Config;
import com.zlyandroid.wanandroid.core.ScrollTop;
import com.zlyandroid.wanandroid.ui.core.adapter.FixedFragmentPagerAdapter;
import com.zlyandroid.wanandroid.ui.mine.fragment.CollectionArticleFragment;
import com.zlyandroid.wanandroid.ui.mine.fragment.CollectionLinkFragment;
import com.zlyandroid.wanandroid.util.MagicIndicatorUtils;
import com.zlylib.titlebarlib.widget.ActionBarEx;
import com.zlylib.upperdialog.listener.SimpleCallback;

import butterknife.BindView;

public class CollectionActivity extends BaseActivity {

    @BindView(R.id.ab)
    ActionBarEx ab;
    @BindView(R.id.vp)
    ViewPager vp;

    private FixedFragmentPagerAdapter mAdapter;
    private long lastClickTime = 0L;
    private int lastClickPos = 0;

    public static void start(Context context) {
        Intent intent = new Intent(context, CollectionActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_collection;
    }

    @Override
    public void initView() {
        ImageView ivBack = ab.getView(R.id.action_bar_fixed_magic_indicator_iv_back);
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter = new FixedFragmentPagerAdapter(getSupportFragmentManager());
        mAdapter.setTitles("文章", "网址");
        mAdapter.setFragmentList(
                CollectionArticleFragment.create(),
                CollectionLinkFragment.create()
        );
        vp.setAdapter(mAdapter);
        vp.setOffscreenPageLimit(2);
        MagicIndicatorUtils.commonNavigator(ab.getView(R.id.mi), vp, mAdapter, new SimpleCallback<Integer>() {
            @Override
            public void onResult(Integer data) {
                notifyScrollTop(data);
            }
        });
    }

    @Override
    public void initData() {

    }

    private void notifyScrollTop(int pos) {
        long currClickTime = System.currentTimeMillis();
        if (lastClickPos == pos && currClickTime - lastClickTime <= Config.SCROLL_TOP_DOUBLE_CLICK_DELAY) {
            Fragment fragment = mAdapter.getItem(pos);
            if (fragment instanceof ScrollTop) {
                ScrollTop scrollTop = (ScrollTop) fragment;
                scrollTop.scrollTop();
            }
        }
        lastClickPos = pos;
        lastClickTime = currClickTime;
    }
}

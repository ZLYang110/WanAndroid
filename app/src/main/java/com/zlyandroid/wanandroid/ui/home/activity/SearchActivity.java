package com.zlyandroid.wanandroid.ui.home.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseActivity;
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.ui.home.fragment.SearchHistoryFragment;
import com.zlyandroid.wanandroid.ui.home.fragment.SearchResultFragment;
import com.zlyandroid.wanandroid.ui.home.view.ScanView;
import com.zlyandroid.wanandroid.util.EditTextUtils;
import com.zlyandroid.wanandroid.util.InputMethodUtils;
import com.zlylib.titlebarlib.ActionBarSearch;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;

import butterknife.BindView;

public class SearchActivity extends BaseActivity<BasePresenter<ScanView>> {

    @BindView(R.id.abs)
    ActionBarSearch abs;
    @BindView(R.id.fl)
    FrameLayout fl;

    private FragmentManager mFragmentManager;
    private SearchHistoryFragment mSearchHistoryFragment;
    private SearchResultFragment mSearchResultFragment;

    private boolean mIsResultPage = false;

    public static void start(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }
    @Override
    public int getLayoutID() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        abs.setOnLeftIconClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsResultPage) {
                    showHistoryFragment();
                } else {
                    finish();
                }
            }
        });
        abs.setOnRightTextClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                String key = abs.getEditTextView().getText().toString();
                search(key);
            }
        });

        abs.getEditTextView().setSingleLine();
        abs.getEditTextView().setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        abs.getEditTextView().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_SEARCH)) {
                    String key = abs.getEditTextView().getText().toString();
                    search(key);
                    return true;
                }
                return false;
            }
        });

        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        Fragment searchHistoryFragment = mFragmentManager.findFragmentByTag(SearchHistoryFragment.class.getName());
        if (searchHistoryFragment == null) {
            mSearchHistoryFragment = SearchHistoryFragment.newInstance();
            transaction.add(R.id.fl, mSearchHistoryFragment, SearchHistoryFragment.class.getName());
        } else {
            mSearchHistoryFragment = (SearchHistoryFragment) searchHistoryFragment;
        }
        Fragment searchResultFragment = mFragmentManager.findFragmentByTag(SearchResultFragment.class.getName());
        if (searchResultFragment == null) {
            mSearchResultFragment = SearchResultFragment.newInstance();
            transaction.add(R.id.fl, mSearchResultFragment, SearchResultFragment.class.getName());
        } else {
            mSearchResultFragment = (SearchResultFragment) searchResultFragment;
        }
        transaction.show(mSearchHistoryFragment);
        transaction.hide(mSearchResultFragment);
        transaction.commit();
    }

    @Override
    public void initData() {

    }
    @Override
    public void onBackPressed() {
        if (mIsResultPage) {
            showHistoryFragment();
        } else {
            super.onBackPressed();
        }
    }
    public void search(String key) {
        InputMethodUtils.hide(abs.getEditTextView());
        abs.getEditTextView().clearFocus();
        if (TextUtils.isEmpty(key)) {
            if (mIsResultPage) {
                showHistoryFragment();
            }
        } else {
            EditTextUtils.setTextWithSelection(abs.getEditTextView(), key);
            if (!mIsResultPage) {
                showResultFragment();
            }
            mSearchHistoryFragment.addHistory(key);
            mSearchResultFragment.search(key);
        }
    }

    private void showHistoryFragment() {
        mIsResultPage = false;
        FragmentTransaction t = mFragmentManager.beginTransaction();
        t.hide(mSearchResultFragment);
        t.show(mSearchHistoryFragment);
        t.commit();
    }
    private void showResultFragment() {
        mIsResultPage = true;
        FragmentTransaction t = mFragmentManager.beginTransaction();
        t.hide(mSearchHistoryFragment);
        t.show(mSearchResultFragment);
        t.commit();
    }
}

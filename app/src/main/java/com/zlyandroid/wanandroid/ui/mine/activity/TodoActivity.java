package com.zlyandroid.wanandroid.ui.mine.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.app.ProApplication;
import com.zlyandroid.wanandroid.base.BaseMvpActivity;
import com.zlyandroid.wanandroid.core.Constants;

import com.zlyandroid.wanandroid.event.RefreshTodoEvent;
import com.zlyandroid.wanandroid.ui.mine.fragment.TodoListFragment;
import com.zlyandroid.wanandroid.ui.mine.presenter.SettingPresenter;

import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;


import java.io.File;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


public class TodoActivity extends BaseMvpActivity  {

    @BindView(R.id.abc)
    ActionBarCommon abc;

    @BindView(R.id.todo_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.todo_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.todo_floating_action_btn)
    FloatingActionButton mFloatingActionButton;
    @BindView(R.id.todo_bottom_navigation_view)
    BottomNavigationView mBottomNavigationView;

    private SparseArray<TodoListFragment> fragmentSparseArray = new SparseArray<>(5);
    private SparseArray<String> mTodoTypeArray = new SparseArray<>(5);
    private static int mTodoStatus = 0;

    public static int getTodoStatus() {
        return mTodoStatus;
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, TodoActivity.class);
        activity.startActivity(intent);

    }
    @Override
    protected SettingPresenter createPresenter() {
        return  new SettingPresenter();
    }
    @Override
    public int getLayoutID() {
        return R.layout.activity_todo;
    }

    @Override
    public void initView() {
        abc.setOnLeftIconClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initTodoTypeList();
        initViewPagerAndTabLayout();
        initBottomNavigationView();
    }
    @Override
    public void initData() {

    }



    @OnClick({R.id.todo_floating_action_btn})
    @Override
    protected void onClick2(View v) {
        switch (v.getId()) {
            case R.id.todo_floating_action_btn:
                AddTodoActivity.start(getActivity());
                break;

        }
    }
    private void initTodoTypeList() {
        mTodoTypeArray.put(Constants.TODO_KEY.TODO_TYPE_ALL, getString(R.string.todo_all));
        mTodoTypeArray.put(Constants.TODO_KEY.TODO_TYPE_WORK, getString(R.string.todo_work));
        mTodoTypeArray.put(Constants.TODO_KEY.TODO_TYPE_STUDY, getString(R.string.todo_study));
        mTodoTypeArray.put(Constants.TODO_KEY.TODO_TYPE_LIFE, getString(R.string.todo_life));
        mTodoTypeArray.put(Constants.TODO_KEY.TODO_TYPE_OTHER, getString(R.string.todo_other));
    }
    private void initViewPagerAndTabLayout() {
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                TodoListFragment todoListFragment = fragmentSparseArray.get(position);
                if (todoListFragment != null) {
                    return todoListFragment;
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.TODO_KEY.TODO_TYPE, position);
                    todoListFragment = TodoListFragment.newInstance(bundle);
                    fragmentSparseArray.put(position, todoListFragment);
                    return todoListFragment;
                }
            }

            @Override
            public int getCount() {
                return mTodoTypeArray == null ? 0 : mTodoTypeArray.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTodoTypeArray.get(position);
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            }
        });

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //取消页面切换动画
                mViewPager.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    private void initBottomNavigationView() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_not_todo:
                    if (mTodoStatus == 1) {
                        mTodoStatus = 0;
                        new RefreshTodoEvent(0).post();
                    }
                    break;
                case R.id.action_todo_done:
                    if (mTodoStatus == 0) {
                        mTodoStatus = 1;
                        new RefreshTodoEvent(1).post();

                    }
                    break;
                default:
                    break;
            }
            return true;
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

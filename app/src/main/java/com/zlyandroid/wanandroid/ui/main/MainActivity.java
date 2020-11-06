package com.zlyandroid.wanandroid.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpActivity;
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.core.Constants;
import com.zlyandroid.wanandroid.ui.Knowledge.KnowledgeNavigationFragment;
import com.zlyandroid.wanandroid.ui.Knowledge.fragment.NavigationFragment;
import com.zlyandroid.wanandroid.ui.home.HomePageFragment;
import com.zlyandroid.wanandroid.ui.mine.MineFragment;
import com.zlyandroid.wanandroid.ui.question.QuestionFragment;
import com.zlyandroid.wanandroid.util.LogUtil;
import com.zlyandroid.wanandroid.util.PermissionUtils;
import com.zlyandroid.wanandroid.widget.MyBottomTab;
import com.zlylib.mypermissionlib.RequestListener;
import com.zlylib.upperdialog.listener.SimpleListener;

import butterknife.BindView;

public class MainActivity extends BaseMvpActivity {


    @BindView(R.id.bottom_bar)
    MyBottomTab bottomBar;


    private HomePageFragment mHomePagerFragment;//首页
    private KnowledgeNavigationFragment mKnowledgeFragment;//体系
    private QuestionFragment mNavigationFragment;//
    private MineFragment mineFragment;//我的
    private int mLastFgIndex = -1;
    private int mCurrentFgIndex=0;//当前所在Fragment


    public static Intent createIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutID() {
        return R.layout.activity_home;
    }

    @Override
    public void initView() {

        if(bottomBar!=null){
            bottomBar.setOnTabChangeListener((v, position, isChange) -> setSelect(position, isChange));
        }
        doPermission();
    }
    @Override
    public void initData() {
        showFragment(0);

    }


    private void setSelect(int position, boolean isChange) {
        showFragment(position);

    }
    private void showFragment(int index) {
        mCurrentFgIndex = index;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideFragment(transaction);
        mLastFgIndex = index;
        switch (index) {
            case Constants.TAG_FRAGMENT.TYPE_HOME_PAGER:
                if (mHomePagerFragment == null) {
                    mHomePagerFragment = HomePageFragment.create();
                    transaction.add(R.id.fragment_group, mHomePagerFragment);
                }
                transaction.show(mHomePagerFragment);
                break;
            case Constants.TAG_FRAGMENT.TYPE_KNOWLEDGE:
                if (mKnowledgeFragment == null) {
                    mKnowledgeFragment = KnowledgeNavigationFragment.create();
                    transaction.add(R.id.fragment_group, mKnowledgeFragment);
                }
                transaction.show(mKnowledgeFragment);
                break;
            case Constants.TAG_FRAGMENT.TYPE_NAVIGATION:
                if (mNavigationFragment == null) {
                    mNavigationFragment = QuestionFragment.create();
                    transaction.add(R.id.fragment_group, mNavigationFragment);
                }
                transaction.show(mNavigationFragment);
                break;
            case Constants.TAG_FRAGMENT.TYPE_MINE:
                if (mineFragment == null) {
                    mineFragment = MineFragment.newInstance();
                    transaction.add(R.id.fragment_group, mineFragment);
                }
                transaction.show(mineFragment);
                break;


            default:

                break;
        }
        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        switch (mLastFgIndex) {
            case Constants.TAG_FRAGMENT.TYPE_HOME_PAGER:
                if (mHomePagerFragment != null) {
                    transaction.hide(mHomePagerFragment);
                }
                break;
            case Constants.TAG_FRAGMENT.TYPE_KNOWLEDGE:
                if (mKnowledgeFragment != null) {
                    transaction.hide(mKnowledgeFragment);
                }
                break;
            case Constants.TAG_FRAGMENT.TYPE_NAVIGATION:
                if (mNavigationFragment != null) {
                    transaction.hide(mNavigationFragment);
                }
                break;

            case Constants.TAG_FRAGMENT.TYPE_MINE:
                if (mineFragment != null) {
                    transaction.hide(mineFragment);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.d("HomeActivity onActivityResult resultCode" + resultCode);
        if (resultCode == RESULT_OK) {
            LogUtil.d("resultCode == RESULT_OK  requestCode== "+requestCode );
            switch (requestCode) {


            }
        } else
            LogUtil.d("resultCode != RESULT_OK");
        hideKeyBoard();
    }
    private void doPermission() {
        PermissionUtils.request(new RequestListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed() {

            }
        }, getActivity(),0, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }


}

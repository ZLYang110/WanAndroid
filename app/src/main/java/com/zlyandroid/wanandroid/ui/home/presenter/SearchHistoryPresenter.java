package com.zlyandroid.wanandroid.ui.home.presenter;


import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.ui.home.bean.HotKeyBean;
import com.zlyandroid.wanandroid.ui.home.contract.SearchHistoryContract;
import com.zlyandroid.wanandroid.ui.home.model.SearchHistoryModel;
import com.zlyandroid.wanandroid.ui.home.view.SearchHistoryView;
import com.zlyandroid.wanandroid.util.SearchHistoryUtils;
import com.zlyandroid.wanandroid.util.SettingUtils;

import java.util.List;

public class SearchHistoryPresenter extends BasePresenter<SearchHistoryView> implements SearchHistoryContract.Presenter{

    private final SearchHistoryUtils mSearchHistoryUtils = SearchHistoryUtils.newInstance();
    private SearchHistoryContract.Model model;

    public SearchHistoryPresenter() {
        model = new SearchHistoryModel();
    }


    @Override
    public void getHotKeyListSuccess() {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getHotKeyListSuccess(mView.bindToLife(), new BaseObserver<List<HotKeyBean>>() {
            @Override
            public void onSuccess(List<HotKeyBean> hotKeyBeans) {
                mView.getHotKeyListSuccess(1,hotKeyBeans);
            }

            @Override
            public void onError(String error) {
                mView.getHotKeyListFail(1,error);
            }


        });
    }
    public List<String> getHistory(){
        return mSearchHistoryUtils.get();
    }
    public void saveHistory(List<String> list){
        List<String> saves = list;
        int max = SettingUtils.getInstance().getSearchHistoryMaxCount();
        if (list != null && list.size() > max) {
            saves = list.subList(0, max - 1);
        }
        mSearchHistoryUtils.save(saves);
    }
}

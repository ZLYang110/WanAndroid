package com.zlyandroid.wanandroid.ui.mine.presenter;

import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.db.greendao.ReadLaterModel;
import com.zlyandroid.wanandroid.db.greendao.ReadRecordModel;
import com.zlyandroid.wanandroid.listener.DBReadLaterListener;
import com.zlyandroid.wanandroid.listener.DBReadRecordListener;
import com.zlyandroid.wanandroid.ui.mine.model.ReadLaterM;
import com.zlyandroid.wanandroid.ui.mine.model.ReadRecordM;
import com.zlyandroid.wanandroid.ui.mine.view.ReadLaterView;
import com.zlyandroid.wanandroid.ui.mine.view.ReadRecordView;

import java.util.List;

/**
 * @author zhangliyang
 * @date 2019/5/15
 * GitHub:  https://github.com/ZLYang110
 */
public class ReadlaterPresenter extends BasePresenter<ReadLaterView> {

    ReadLaterM model;

    public ReadlaterPresenter() {
        model = new ReadLaterM();
    }

    public void getReadLaterList( ) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getReadLaterList(mView.bindToLife(), new DBReadLaterListener() {

            @Override
            public void onSuccess(List<ReadLaterModel> callback) {
                mView.getReadLaterListSuccess(callback);
            }
            @Override
            public void onError(String throwable) {
                mView.getReadLaterListFailed();
            }


        });
    }

    public void delectReadLaterById(Long id) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.delectReadLaterById(mView.bindToLife(), id, new DBReadLaterListener() {

            @Override
            public void onSuccess(List<ReadLaterModel> callback) {
                mView.removeReadLaterSuccess(callback);
            }
            @Override
            public void onError(String throwable) {
                mView.removeReadLaterFailed();
            }


        });
    }

}

package com.zlyandroid.wanandroid.ui.mine.presenter;

import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.db.greendao.ReadRecordModel;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.listener.DBReadRecordListener;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRecordBean;
import com.zlyandroid.wanandroid.ui.mine.model.ReadRecordM;
import com.zlyandroid.wanandroid.ui.mine.view.ReadRecordView;

import java.util.List;

/**
 * @author zhangliyang
 * @date 2019/5/15
 * GitHub:  https://github.com/ZLYang110
 */
public class ReadRecordPresenter extends BasePresenter<ReadRecordView> {

    ReadRecordM model;

    public ReadRecordPresenter() {
        model = new ReadRecordM();
    }

    public void getReadRecordList( ) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.getReadRecordList(mView.bindToLife(), new DBReadRecordListener() {

            @Override
            public void onSuccess(List<ReadRecordModel> callback) {
                mView.getReadRecordListSuccess(callback);
            }
            @Override
            public void onError(String throwable) {
                mView.getReadRecordListFailed();
            }


        });
    }

    public void delectReadRecordById(Long id) {
        //View是否绑定 如果没有绑定，就不执行网络请求
        if (!isViewAttached()) {
            return;
        }
        model.delectReadRecordById(mView.bindToLife(), id, new DBReadRecordListener() {

            @Override
            public void onSuccess(List<ReadRecordModel> callback) {
                mView.removeReadRecordSuccess(callback);
            }
            @Override
            public void onError(String throwable) {
                mView.removeReadRecordFailed();
            }


        });
    }

}

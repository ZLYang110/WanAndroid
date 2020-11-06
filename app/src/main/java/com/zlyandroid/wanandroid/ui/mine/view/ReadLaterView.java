package com.zlyandroid.wanandroid.ui.mine.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.db.greendao.ReadLaterModel;

import java.util.List;


/**
 * @author zhangliyang
 * @date 2019/5/23
 * GitHub: https://github.com/ZLYang110
 */
public interface ReadLaterView extends BaseView {
    void getReadLaterListSuccess(List<ReadLaterModel> list);

    void getReadLaterListFailed();

    void removeReadLaterSuccess(List<ReadLaterModel> list);

    void removeReadLaterFailed();


}

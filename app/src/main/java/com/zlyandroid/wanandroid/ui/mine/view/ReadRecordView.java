package com.zlyandroid.wanandroid.ui.mine.view;

import com.zlyandroid.wanandroid.base.mvp.BaseView;
import com.zlyandroid.wanandroid.db.greendao.ReadLaterModel;
import com.zlyandroid.wanandroid.db.greendao.ReadRecordModel;

import java.util.List;


/**
 * @author zhangliyang
 * @date 2019/5/23
 * GitHub: https://github.com/ZLYang110
 */
public interface ReadRecordView extends BaseView {
    void getReadRecordListSuccess(List<ReadRecordModel> list);

    void getReadRecordListFailed();

    void removeReadRecordSuccess(List<ReadRecordModel> list);

    void removeReadRecordFailed();

}

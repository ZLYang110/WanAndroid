/*
 *     (C) Copyright 2019, ForgetSky.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.zlyandroid.wanandroid.db.readlater;

import android.database.sqlite.SQLiteDatabase;

import com.zlyandroid.wanandroid.app.AppContext;
import com.zlyandroid.wanandroid.core.Config;
import com.zlyandroid.wanandroid.db.greendao.DaoMaster;
import com.zlyandroid.wanandroid.db.greendao.DaoSession;
import com.zlyandroid.wanandroid.db.greendao.ReadLaterModel;
import com.zlyandroid.wanandroid.db.greendao.ReadLaterModelDao;
import com.zlyandroid.wanandroid.db.greendao.ReadRecordModel;
import com.zlyandroid.wanandroid.db.greendao.TodoData;
import com.zlyandroid.wanandroid.db.greendao.TodoDataDao;
import com.zlyandroid.wanandroid.db.todo.DbToDoHelper;
import com.zlyandroid.wanandroid.util.LogUtil;

import java.util.List;


/**
 * @author zhangliyang
 * @date 19-3-4
 */

public class DbReadLaterHelperImpl implements DbReadLaterHelper {


  private static final int HISTORY_LIST_SIZE = 100;

    private DaoSession daoSession;
    private List<ReadLaterModel> dataList;
    static DbReadLaterHelperImpl mDbHelperImpl=null;
    public static DbReadLaterHelperImpl getInstance() {
        if (mDbHelperImpl == null) {
            synchronized(DbReadLaterHelperImpl.class) {
                if (mDbHelperImpl == null) {
                    mDbHelperImpl =  new DbReadLaterHelperImpl();
                }
            }
        }
        return mDbHelperImpl;
    }
    public DbReadLaterHelperImpl() {
        initGreenDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper devOpenHelper =
                new DaoMaster.DevOpenHelper(AppContext.getContext(), Config.DB_NAME);
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();

    }
    public void add(String title,String link){
        ReadLaterModel data =new   ReadLaterModel();
        data.setTitle(title);
        data.setLink(link);
        data.setTime(System.currentTimeMillis());
        addData(data);
    }

    @Override
    public List<ReadLaterModel> addData(ReadLaterModel data) {
        getTodoDataList();


        if (dataList.size() < HISTORY_LIST_SIZE) {
            getDataDao().insert(data);
        } else {
            dataList.remove(0);
            dataList.add(data);
            getDataDao().deleteAll();
            getDataDao().insertInTx(dataList);
        }
        return dataList;
    }

    @Override
    public void clearAllData() {
        getDataDao().deleteAll();
    }

    @Override
    public List<ReadLaterModel> deletById(Long id) {
          getDataDao().deleteByKey(id);
        getTodoDataList();
        return dataList;
    }


    @Override
    public List<ReadLaterModel> loadAllTodoData() {
        getTodoDataList();
        return dataList;
    }

    @Override
    public ReadLaterModel loadAllById(Long id) {
        return getDataDao().load(id);
    }


    private void getTodoDataList() {
        dataList = getDataDao().loadAll();
        getLogDataList(dataList);
    }
    private ReadLaterModelDao getDataDao() {
        return daoSession.getReadLaterModelDao();
    }

    public void getLogDataList(List<ReadLaterModel> data) {

        StringBuilder sbStr=new StringBuilder();
        sbStr.append("{"+"\n") ;
        for (ReadLaterModel faceRecordData : data) {
            sbStr.append(faceRecordData.getStrintg() +"\n") ;
        }
        sbStr.append("}"+"\n") ;
        LogUtil.i(sbStr.toString());

    }
}
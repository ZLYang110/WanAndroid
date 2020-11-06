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

package com.zlyandroid.wanandroid.db.todo;

import android.database.sqlite.SQLiteDatabase;


import com.zlyandroid.wanandroid.app.AppContext;
import com.zlyandroid.wanandroid.core.Config;
import com.zlyandroid.wanandroid.db.greendao.DaoMaster;
import com.zlyandroid.wanandroid.db.greendao.DaoSession;
import com.zlyandroid.wanandroid.db.greendao.TodoData;
import com.zlyandroid.wanandroid.db.greendao.TodoDataDao;
import com.zlyandroid.wanandroid.util.LogUtil;

import java.util.List;



/**
 * @author zhangliyang
 * @date 19-3-4
 */

public class DbToDoHelperImpl implements DbToDoHelper {


  private static final int HISTORY_LIST_SIZE = 100;

    private DaoSession daoSession;
    private List<TodoData> faceDataList;
    static DbToDoHelperImpl mDbHelperImpl=null;
    public static DbToDoHelperImpl getInstance() {
        if (mDbHelperImpl == null) {
            synchronized(DbToDoHelperImpl.class) {
                if (mDbHelperImpl == null) {
                    mDbHelperImpl =  new DbToDoHelperImpl();
                }
            }
        }
        return mDbHelperImpl;
    }
    public DbToDoHelperImpl() {
        initGreenDao();
    }

    private void initGreenDao() {
        DaoMaster.DevOpenHelper devOpenHelper =
                new DaoMaster.DevOpenHelper(AppContext.getContext(), Config.DB_NAME);
        SQLiteDatabase database = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();

    }


    @Override
    public List<TodoData> addTodoData(TodoData data) {
        getTodoDataList();


        if (faceDataList.size() < HISTORY_LIST_SIZE) {
            getTodoDataDao().insert(data);
        } else {
            faceDataList.remove(0);
            faceDataList.add(data);
            getTodoDataDao().deleteAll();
            getTodoDataDao().insertInTx(faceDataList);
        }
        return faceDataList;
    }

    @Override
    public void clearAllFaceData() {
        daoSession.getTodoDataDao().deleteAll();
    }

    @Override
    public List<TodoData> deletTodoDataById(Long id) {
        daoSession.getTodoDataDao().deleteByKey(id);
        getTodoDataList();
        return faceDataList;
    }

    @Override
    public List<TodoData> loadAllTodoData() {
        getTodoDataList();
        return faceDataList;
    }

    @Override
    public  TodoData loadAllTodoDataById(Long id) {
        return  daoSession.getTodoDataDao().load(id);
    }

    @Override
    public List<TodoData> updataTodoData(TodoData data) {
        daoSession.getTodoDataDao().update(data);
        getTodoDataList();
        return faceDataList;
    }

    @Override
    public List<TodoData> whereTodoData(int type, int isAccomplish) {
        if(type == 0){
            return  getTodoDataDao().queryBuilder().where(TodoDataDao.Properties.IsAccomplish.eq(isAccomplish)).list();
        }else{
            return  getTodoDataDao().queryBuilder().where(TodoDataDao.Properties.Type.eq(type),TodoDataDao.Properties.IsAccomplish.eq(isAccomplish)).list();
        }

    }


    private void getTodoDataList() {
        faceDataList = getTodoDataDao().loadAll();
        getLogDataList(faceDataList);
    }
    private TodoDataDao getTodoDataDao() {
        return daoSession.getTodoDataDao();
    }

    public void getLogDataList(List<TodoData> data) {

        StringBuilder sbStr=new StringBuilder();
        sbStr.append("{"+"\n") ;
        for (TodoData faceRecordData : data) {
            sbStr.append(faceRecordData.getStrintg() +"\n") ;
        }
        sbStr.append("}"+"\n") ;
        LogUtil.i(sbStr.toString());

    }
}

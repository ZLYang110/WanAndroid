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

package com.zlyandroid.wanandroid.db.readrecord;




import com.zlyandroid.wanandroid.db.greendao.ReadLaterModel;
import com.zlyandroid.wanandroid.db.greendao.ReadRecordModel;
import com.zlyandroid.wanandroid.db.greendao.ReadRecordModelDao;

import java.util.List;

/**
 * @author zhangliyang
 * @date 19-3-4
 */

public interface DbReadRecordHelper {

    /**
     * Add search history data
     *
     * @param data  added string
     * @return  List<FaceData>
     */
    List<ReadRecordModel> addData(ReadRecordModel data);

    /**
     * Clear all search FaceData data
     */
    void clearAllData();

    /**
     * Clear all search FaceData data
     */
    List<ReadRecordModel> deletById(Long id);

    /**
     * Load all history data
     *
     * @return List<FaceData>
     */
    List<ReadRecordModel> loadAllTodoData();
    /**
     * Load all history data
     *
     * @return List<FaceData>
     */
    ReadRecordModel loadAllById(Long id);


}
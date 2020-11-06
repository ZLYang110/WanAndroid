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

package com.zlyandroid.wanandroid.core;


public class Constants {



    /**
     * Tag fragment
     */
    public static class TAG_FRAGMENT {

        public static final int TYPE_HOME_PAGER = 0;
        public static final int TYPE_KNOWLEDGE = 1;
        public static final int TYPE_NAVIGATION = 2;
        public static final int TYPE_MINE = 3;
    }

    //用户相关
    public static class USERINFO_KEY {
        public static final String SP_LOGIN = "user_info";//sp key
        public static final String USER_INFO = "mUserInfo";  //用户信息
        public static final String IS_LOGIN = "mIsLogin";    //登录状态
        public static final String AES = "mAES";//用户信息密钥
    }


    //事件Action
    public static class EVENT_ACTION {
        public static final String HOME = "home";
        public static final String READLATER = "readlater";

    }

    //Intent传值
    public static class BUNDLE_KEY {
        public static final String ID = "_id";
        public static final String TITLE = "title";
        public static final String URL = "url";
        public static final String OBJ = "obj";
        public static final String TYPE = "type";
        public static final String CHAPTER_ID = "chapter_id";
        public static final String CHAPTER_NAME = "chapter_name";
        public static final String COLLECT_TYPE = "collect_type";//1收藏列表文章 2收藏站内文章
    }

    //TODO
    public static class TODO_KEY {
        public static final String TODO_TYPE = "todo_type";
        public static final String TODO_DATA = "todo_data";
        public static final int TODO_TYPE_ALL = 0;
        public static final int TODO_TYPE_WORK = 1;
        public static final int TODO_TYPE_STUDY = 2;
        public static final int TODO_TYPE_LIFE = 3;
        public static final int TODO_TYPE_OTHER = 4;

        public static final String KEY_TODO_TITLE = "title";
        public static final String KEY_TODO_CONTENT = "content";
        public static final String KEY_TODO_DATE = "date";
        public static final String KEY_TODO_TYPE = "type";
        public static final String KEY_TODO_STATUS = "status";
        public static final String KEY_TODO_PRIORITY = "priority";
        public static final String KEY_TODO_ORDERBY= "orderby";

        public static final int TODO_PRIORITY_FIRST = 1;
        public static final int TODO_PRIORITY_SECOND = 2;
    }



}

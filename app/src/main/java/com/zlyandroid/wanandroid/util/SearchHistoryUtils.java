package com.zlyandroid.wanandroid.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * @author zhangliyang
 * @date 2019/5/30
 * GitHub: https://github.com/ZLYang110
 */
public class SearchHistoryUtils {

    private static final String SP_NAME = "search_history";
    private static final String KEY_HISTORY = "KEY_HISTORY";

    private final PreUtils mSPUtils = PreUtils.newInstance(SP_NAME);
    private final Gson mGson = new Gson();

    public static SearchHistoryUtils newInstance() {
        return new SearchHistoryUtils();
    }

    private SearchHistoryUtils() {
    }

    public void save(List<String> historys) {
        if (historys == null || historys.isEmpty()) {
            mSPUtils.clear();
            return;
        }
        String json = mGson.toJson(historys);
        mSPUtils.save(KEY_HISTORY, json);
    }

    public List<String> get() {
        String json = mSPUtils.get(KEY_HISTORY, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return mGson.fromJson(json, new TypeToken<List<String>>(){}.getType());
        } catch (Exception e){
            mSPUtils.clear();
            return null;
        }
    }
}

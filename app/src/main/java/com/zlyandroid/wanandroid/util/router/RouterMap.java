package com.zlyandroid.wanandroid.util.router;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.zlyandroid.wanandroid.ui.home.activity.UserPageActivity;
import com.zlyandroid.wanandroid.ui.main.acitivity.WebActivity;
import com.zlyandroid.wanandroid.ui.mine.activity.SettingActivity;
import com.zlyandroid.wanandroid.ui.web.WebViewActivity;
import com.zlylib.upperdialog.utils.Utils;


/**
 * @author zhangliyang
 * @date 2019/12/28
 * GitHub: https://github.com/ZLYang110
 */
public enum RouterMap {
    NULL(null, null),
    WEB("/main/web", WebActivity.class),
    USER_PAGE("/main/user_page", UserPageActivity.class),
    SETTING("/mine/setting", SettingActivity.class);
    //ABOUT_ME("/mine/about_me", AboutMeActivity.class),
   // ARTICLE_LIST("/main/article_list", ArticleListActivity.class);

    private final String path;
    private final Class<? extends Activity> clazz;

    RouterMap(String path, Class<? extends Activity> clazz) {
        this.path = path;
        this.clazz = clazz;
    }

    @NonNull
    public static RouterMap from(String path) {
        if (!TextUtils.isEmpty(path)) {
            for (RouterMap routerMap : RouterMap.values()) {
                if (TextUtils.equals(routerMap.path, path)) {
                    return routerMap;
                }
            }
        }
        return NULL;
    }

    public String url() {
        // wana://www.wanandroid.com/user_page?id=1
        return Router.SCHEME + "://" + Router.HOST + path;
    }

    public String url(Param... param) {
        StringBuilder s = new StringBuilder(url());
        for (int i = 0; i < param.length; i++) {
            Param p = param[i];
            if (i == 0) s.append("?");
            else s.append("&");
            s.append(p.key).append("=").append(p.value);
        }
        return s.toString();
    }

    public void navigation(String url) {
        if (clazz == null) return;
        try {
            Intent intent = new Intent(Utils.getAppContext(), clazz);
            intent.putExtra(Router.PARAM_URL, url);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Utils.getAppContext().startActivity(intent);
        } catch (Exception ignore) {
        }
    }
}

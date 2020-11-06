package com.zlyandroid.wanandroid.util;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author zhangliyang
 * @date 2019/5/30
 * GitHub: https://github.com/ZLYang110
 */
public class RvScrollTopUtils {

    public static void smoothScrollTop(RecyclerView rv){
        if (rv != null) {
            RecyclerView.LayoutManager layoutManager = rv.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                int first = linearLayoutManager.findFirstVisibleItemPosition();
                int last = linearLayoutManager.findLastVisibleItemPosition();
                int visibleCount = last - first + 1;
                int scrollIndex = visibleCount * 2 - 1;
                if (first > scrollIndex) {
                    rv.scrollToPosition(scrollIndex);
                }
            }
            rv.smoothScrollToPosition(0);
        }
    }

}

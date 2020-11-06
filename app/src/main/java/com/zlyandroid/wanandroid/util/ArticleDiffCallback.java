package com.zlyandroid.wanandroid.util;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.zlyandroid.wanandroid.ui.home.adapter.ArticleAdapter;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;

public class ArticleDiffCallback {

    public static Boolean areItemsTheSame(MultiItemEntity oldItem,MultiItemEntity newItem){
        boolean bool= false;
        if (oldItem.getItemType() == newItem.getItemType()) {

        }else{
            bool= false;
        }
        return bool;
    }
}

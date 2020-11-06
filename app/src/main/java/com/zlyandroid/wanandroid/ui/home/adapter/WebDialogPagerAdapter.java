package com.zlyandroid.wanandroid.ui.home.adapter;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.tencent.smtt.sdk.WebView;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;

import java.util.List;


/**
 * @author zhangliyang
 * @date 2019/8/31
 */
public class WebDialogPagerAdapter extends PagerAdapter {

    private final Activity mActivity;
    private final List<ArticleBean> mTopUrls;
    private final List<MultiItemEntity> mUrls;

    private OnDoubleClickListener mOnDoubleClickListener = null;

    public WebDialogPagerAdapter(Activity activity, List<ArticleBean> topUrls, List<MultiItemEntity> urls) {
        mTopUrls = topUrls;
        mUrls = urls;
        mActivity = activity;
    }

    public void setOnDoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
        mOnDoubleClickListener = onDoubleClickListener;
    }





    public ArticleBean getArticleBean(int position) {
        MultiItemEntity entity = getBean(position);
        if (entity != null && entity.getItemType() == ArticleAdapter.ITEM_TYPE_ARTICLE) {
            return (ArticleBean) entity;
        }
        return null;
    }

    public MultiItemEntity getBean(int pos) {
        int topUrlCount = mTopUrls == null ? 0 : mTopUrls.size();
        if (pos < topUrlCount) {
            return mTopUrls.get(pos);
        }
        return mUrls.get(pos - topUrlCount);
    }

    @Override
    public int getCount() {
        int count = 0;
        if (mTopUrls != null) {
            count += mTopUrls.size();
        }
        if (mUrls != null) {
            count += mUrls.size();
        }
        return count;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final MultiItemEntity data = getBean(position);
        final ArticleBean bean;
        if (data.getItemType() == ArticleAdapter.ITEM_TYPE_ARTICLE) {
            bean = (ArticleBean) data;
        } else {
            bean = null;
        }
        View rootView = LayoutInflater.from(container.getContext()).inflate(R.layout.dialog_web_vp_item, container, false);
        WebView wc = rootView.findViewById(R.id.dialog_web_wc);
        wc.loadUrl(bean.getLink());

        container.addView(rootView);
        return rootView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    public interface OnDoubleClickListener {
        void onDoubleClick(ArticleBean data);
    }
}

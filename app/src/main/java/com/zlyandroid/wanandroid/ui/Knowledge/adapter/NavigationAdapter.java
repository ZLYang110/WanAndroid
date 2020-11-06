package com.zlyandroid.wanandroid.ui.Knowledge.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.flexbox.FlexboxLayout;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.NavigationBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.util.LogUtil;

import java.util.LinkedList;
import java.util.Queue;


/**
 * @author zhangliyang
 * @date 2019/5/12
 * GitHub: https://github.com/ZLYang110
 */
public class NavigationAdapter extends BaseQuickAdapter<NavigationBean, BaseViewHolder> {

    private LayoutInflater mInflater = null;
    private Queue<TextView> mFlexItemTextViewCaches = new LinkedList<>();
    private OnItemClickListener mOnItemClickListener = null;

    public NavigationAdapter() {
        super(R.layout.rv_item_knowledge);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    protected void convert(BaseViewHolder helper, NavigationBean item) {
        helper.setText(R.id.tv_name, item.getName());
        LogUtil.d(item.getName());
        FlexboxLayout fbl = helper.getView(R.id.fbl);
        for (int i = 0; i < item.getArticles().size(); i++) {
            final ArticleBean childItem = item.getArticles().get(i);
            TextView child = createOrGetCacheFlexItemTextView(fbl);
            child.setText(childItem.getTitle());
            int finalI = i;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onClick(childItem, finalI);
                    }
                }
            });
            fbl.addView(child);
        }
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
        FlexboxLayout fbl = holder.getView(R.id.fbl);
        for (int i = 0; i < fbl.getChildCount(); i++) {
            mFlexItemTextViewCaches.offer((TextView) fbl.getChildAt(i));
        }
        fbl.removeAllViews();
    }

    private TextView createOrGetCacheFlexItemTextView(FlexboxLayout fbl) {
        TextView tv = mFlexItemTextViewCaches.poll();
        if (tv != null) {
            return tv;
        }
        return createFlexItemTextView(fbl);
    }

    private TextView createFlexItemTextView(FlexboxLayout fbl) {
        if (mInflater == null) {
            mInflater = LayoutInflater.from(fbl.getContext());
        }
        return (TextView) mInflater.inflate(R.layout.rv_item_knowledge_child, fbl, false);
    }

    public interface OnItemClickListener {
        void onClick(ArticleBean bean, int pos);
    }
}

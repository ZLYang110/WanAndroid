package com.zlyandroid.wanandroid.ui.home.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.core.UserInfoManager;
import com.zlyandroid.wanandroid.event.CollectionEvent;
import com.zlyandroid.wanandroid.listener.OnClickListener2;
import com.zlyandroid.wanandroid.ui.Knowledge.activity.KnowledgeArticleActivity;
import com.zlyandroid.wanandroid.ui.home.activity.UserPageActivity;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.mine.activity.CoinActivity;
import com.zlyandroid.wanandroid.ui.web.WebViewActivity;
import com.zlyandroid.wanandroid.util.ImageLoader;
import com.zlyandroid.wanandroid.util.StringUtils;
import com.zlyandroid.wanandroid.widget.CollectView;

import java.util.Collection;
import java.util.List;

/**
 * @author zhangliyang
 */
public class ArticleAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public static final int ITEM_TYPE_ARTICLE = 0;
    public static final int ITEM_TYPE_AD = 1;

    private OnItemChildViewClickListener mOnItemChildViewClickListener = null;

    public ArticleAdapter() {
        super(null);
        addItemType(ITEM_TYPE_ARTICLE, getArticleLayoutId());
        addItemType(ITEM_TYPE_AD, R.layout.rv_item_ad);
    }

    protected int getArticleLayoutId() {
        return R.layout.rv_item_article;
    }

    public void setOnItemChildViewClickListener(OnItemChildViewClickListener onItemChildViewClickListener) {
        mOnItemChildViewClickListener = onItemChildViewClickListener;
    }


    @Override
    public void addData(@NonNull Collection<? extends MultiItemEntity> newData) {

        super.addData(newData);
    }

    public ArticleBean getArticleBean(int position) {
        MultiItemEntity entity = getItem(position);
        if (entity != null && entity.getItemType() == ITEM_TYPE_ARTICLE) {
                return (ArticleBean) entity;
        }
        return null;
    }



    public void notifyAllUnCollect() {
        forEach(new ArticleForEach() {
            @Override
            public boolean forEach(int dataPos, int adapterPos, ArticleBean bean) {
                if (bean.isCollect()) {
                    bean.setCollect(false);
                    notifyItemChanged(adapterPos);
                }
                return false;
            }
        });
    }

    public void notifyCollectionEvent(CollectionEvent event) {
        forEach(new ArticleForEach() {
            @Override
            public boolean forEach(int dataPos, int adapterPos, ArticleBean bean) {
                if (bean.getId() == event.getArticleId()) {
                    if (bean.isCollect() != event.isCollect()) {
                        bean.setCollect(event.isCollect());
                        notifyItemChanged(adapterPos);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void forEach(@NonNull ArticleForEach articleForEach) {
        List<MultiItemEntity> list = getData();
        for (int i = 0; i < list.size(); i++) {
            MultiItemEntity item = list.get(i);
            if (item.getItemType() == ITEM_TYPE_ARTICLE) {
                ArticleBean bean = (ArticleBean) item;
                if (articleForEach.forEach(i, i + getHeaderLayoutCount(), bean)) {
                    break;
                }
            }
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, MultiItemEntity item) {
        switch (helper.getItemViewType()) {
            case ITEM_TYPE_ARTICLE:
                Log.d(TAG,"ArticleList item"+item);
                convertArticle(helper, (ArticleBean) item);
                break;
            case ITEM_TYPE_AD:
                break;
        }
    }

    protected void convertArticle(BaseViewHolder helper, ArticleBean item) {
        bindArticle(helper.itemView, item, new OnCollectListener() {
            @Override
            public void collect(ArticleBean item, CollectView v) {
                if (mOnItemChildViewClickListener != null) {
                    mOnItemChildViewClickListener.onCollectClick(helper, v, helper.getAdapterPosition() - getHeaderLayoutCount());
                }
            }

            @Override
            public void uncollect(ArticleBean item, CollectView v) {
                if (mOnItemChildViewClickListener != null) {
                    mOnItemChildViewClickListener.onCollectClick(helper, v, helper.getAdapterPosition() - getHeaderLayoutCount());
                }
            }
        });
    }

    private boolean scrolling = false;

    public interface OnItemChildViewClickListener {
        void onCollectClick(BaseViewHolder helper, CollectView v, int position);
    }
    public interface OnCollectListener {
        void collect(ArticleBean item, CollectView v);

        void uncollect(ArticleBean item, CollectView v);
    }


    public static void bindArticle(View view, ArticleBean item , OnCollectListener onCollectListener) {
        TextView tv_top = view.findViewById(R.id.tv_top);
        TextView tv_new = view.findViewById(R.id.tv_new);
        TextView tv_author = view.findViewById(R.id.tv_author);
        TextView tv_tag = view.findViewById(R.id.tv_tag);
        TextView tv_time = view.findViewById(R.id.tv_time);
        ImageView iv_img = view.findViewById(R.id.iv_img);
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_desc = view.findViewById(R.id.tv_desc);
        TextView tv_chapter_name = view.findViewById(R.id.tv_chapter_name);
        CollectView cv_collect = view.findViewById(R.id.cv_collect);

        if (item.isTop()) {
            tv_top.setVisibility(View.VISIBLE);
        } else {
            tv_top.setVisibility(View.GONE);
        }
        if (item.isFresh()) {
            tv_new.setVisibility(View.VISIBLE);
        } else {
            tv_new.setVisibility(View.GONE);
        }
        tv_author.setText(item.getAuthor());
        if (item.getTags() != null && item.getTags().size() > 0) {
            tv_tag.setText(item.getTags().get(0).getName());
            tv_tag.setVisibility(View.VISIBLE);
            tv_tag.setOnClickListener(new OnClickListener2() {
                @Override
                public void onClick2(View v) {
                    KnowledgeArticleActivity.start(v.getContext(), item.getTags().get(0));
                }
            });
        } else {
            tv_tag.setVisibility(View.GONE);
        }
        tv_time.setText(item.getNiceDate());
        if (!TextUtils.isEmpty(item.getEnvelopePic())) {
            ImageLoader.image(iv_img, item.getEnvelopePic());
            iv_img.setVisibility(View.VISIBLE);
        } else {
            iv_img.setVisibility(View.GONE);
        }
        tv_title.setText(Html.fromHtml(item.getTitle()));
        if (TextUtils.isEmpty(item.getDesc())) {
            tv_desc.setVisibility(View.GONE);
            tv_title.setSingleLine(false);
        } else {
            tv_desc.setVisibility(View.VISIBLE);
            tv_title.setSingleLine(true);
            String desc = Html.fromHtml(item.getDesc()).toString();
            desc = StringUtils.removeAllBank(desc, 2);
            tv_desc.setText(desc);
        }
        item.setVisible(View.GONE);
        tv_chapter_name.setText(Html.fromHtml(formatChapterName(item.getSuperChapterName(), item.getChapterName())));
        if (item.isCollect()) {
            cv_collect.setChecked(true);
        } else {
            cv_collect.setChecked(false);
        }
        tv_chapter_name.setOnClickListener(new OnClickListener2() {
            @Override
            public void onClick2(View v) {
                Log.d(TAG,"getUserPage  id =="+ item.getTitle() +"---------++=="+ item.getUserId());
                KnowledgeArticleActivity.start(v.getContext(),
                        item.getSuperChapterId(), item.getSuperChapterName(),
                        item.getChapterId());
            }
        });
        tv_author.setOnClickListener(new OnClickListener2() {
            @Override
            public void onClick2(View v) {

                UserPageActivity.start(v.getContext(), item.getUserId());
            }
        });

        cv_collect.setOnClickListener(new CollectView.OnClickListener() {
            @Override
            public void onClick(CollectView v) {
                if (v.isChecked()) {
                    if (onCollectListener != null) {
                        onCollectListener.collect(item, v);
                    }
                } else {
                    if (onCollectListener != null) {
                        onCollectListener.uncollect(item, v);
                    }
                }
            }
        });

    }

    private static String formatChapterName(String... names) {
        StringBuilder format = new StringBuilder();
        for (String name : names) {
            if (!TextUtils.isEmpty(name)) {
                if (format.length() > 0) {
                    format.append("·");
                }
                format.append(name);
            }
        }
        return format.toString();
    }

    public interface ArticleForEach {
        boolean forEach(int dataPos, int adapterPos, ArticleBean bean);
    }

    public interface PageLoadedCallback {
        void pageLoaded(int startPos, List<? super MultiItemEntity> pageData);
    }
}

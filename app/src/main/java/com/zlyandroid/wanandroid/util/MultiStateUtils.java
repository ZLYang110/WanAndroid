package com.zlyandroid.wanandroid.util;

import android.view.View;

import com.zlyandroid.wanandroid.R;
import com.zlylib.multistateview.MultiStateView;
import com.zlylib.upperdialog.listener.SimpleListener;

public class MultiStateUtils {



    //点击重试
    public static void setEmptyAndErrorClick(MultiStateView view, final SimpleListener listener) {
        view.getView(MultiStateView.VIEW_STATE_EMPTY).findViewById(R.id.retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onResult();
            }
        });
    }



    public interface SimpleListener {
        void onResult();
    }
}

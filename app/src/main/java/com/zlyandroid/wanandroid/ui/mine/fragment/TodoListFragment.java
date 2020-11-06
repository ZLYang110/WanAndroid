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

package com.zlyandroid.wanandroid.ui.mine.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.app.AppConfig;
import com.zlyandroid.wanandroid.base.BaseMvpFragment;
import com.zlyandroid.wanandroid.core.Constants;
import com.zlyandroid.wanandroid.db.todo.DbToDoHelperImpl;
import com.zlyandroid.wanandroid.db.greendao.TodoData;
import com.zlyandroid.wanandroid.event.RefreshTodoEvent;
import com.zlyandroid.wanandroid.ui.mine.activity.AddTodoActivity;
import com.zlyandroid.wanandroid.ui.mine.activity.TodoActivity;
import com.zlyandroid.wanandroid.ui.mine.adapter.TodoListAdapter;
import com.zlyandroid.wanandroid.ui.mine.presenter.TodoListPresenter;
import com.zlyandroid.wanandroid.util.LogUtil;
import com.zlyandroid.wanandroid.util.SmartRefreshUtils;
import com.zlylib.multistateview.MultiStateView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;


/**
 * @author zhangliyang
 * @date 2019/3/17
 * GitHub: https://github.com/ZLYang110
 */
public class TodoListFragment extends BaseMvpFragment<TodoListPresenter> {

    @BindView(R.id.msv)
    MultiStateView msv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;
    @BindView(R.id.todo_list_recycler_view)
    RecyclerView rv;

    TextView mTodoDelete;
    TextView mTodoChangeStatus;
    View popContentView;

    List<TodoData> mTodoItemDataList;
    private SmartRefreshUtils mSmartRefreshUtils;

    private TodoListAdapter mAdapter;
    private PopupWindow popupWindow;
    private boolean isVisible = false;//当前Fragment是否可见
    private boolean isLoaded = false; //当前Fragment是否已经加载

    private int type = 0;
    private int status = 0;
    private Long clickTodoId;
    private int clickTodoPosition;
    public static TodoListFragment newInstance(Bundle bundle) {
        TodoListFragment fragment = new TodoListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    /**
     * TODO状态改变后，Fragment再次可见，则更新数据
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisible = isVisibleToUser;
        LogUtil.d("isVisibleToUser= "+isVisibleToUser+ "--isLoaded= "+isLoaded);
        if (isVisibleToUser && isLoaded) {
            LogUtil.d("isVisibleToUser= "+isVisibleToUser+ "--isLoaded   sadasas=== "+isLoaded);
            updataView();
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshTodoEvent(RefreshTodoEvent refreshTodoEvent) {
        isLoaded = true;
        if(isVisible){
            if(refreshTodoEvent.getStatus()>-1){
                //使用最新状态
                mTodoItemDataList=DbToDoHelperImpl.getInstance().whereTodoData(refreshTodoEvent.getStatus(),status);
                mAdapter.setNewData(mTodoItemDataList);
                LogUtil.d(refreshTodoEvent.getStatus()+"====="+status);
               LogUtil.d(mTodoItemDataList.toArray());
            }
        }
    }
   private void updataView(){
        if(mAdapter!=null){

            //使用最新状态
            status = TodoActivity.getTodoStatus();
            mTodoItemDataList=DbToDoHelperImpl.getInstance().whereTodoData(type,status);
            mAdapter.setNewData(mTodoItemDataList);
            if(mTodoItemDataList.size()==0){
                msv.setViewState(MultiStateView.VIEW_STATE_UNKNOWN);
            }else{
                msv.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected TodoListPresenter createPresenter() {
        return new TodoListPresenter();
    }

    @Override
    public int getLayoutID() {
        return R.layout.fragment_todo_list;
    }

    @Override
    public void initView() {
        assert getArguments() != null;
        type = getArguments().getInt(Constants.TODO_KEY.TODO_TYPE);
        //使用最新状态
        status = TodoActivity.getTodoStatus();
        mTodoItemDataList=DbToDoHelperImpl.getInstance().whereTodoData(type,status);
        mSmartRefreshUtils = SmartRefreshUtils.with(srl);
        mSmartRefreshUtils.pureScrollMode();
        mSmartRefreshUtils.setRefreshListener(new SmartRefreshUtils.RefreshListener() {
            @Override
            public void onRefresh() {
                AppConfig.sHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updataView();
                        mSmartRefreshUtils.success();
                    }
                },1000);

            }
        });

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        initRecyclerView();
        initPopWindow();
    }

    @Override
    public void initData() {

    }

    private void  initPopWindow(){
        popContentView = LayoutInflater.from(getActivity()).inflate(
                R.layout.todo_popup_window, null);
        mTodoChangeStatus = popContentView.findViewById(R.id.todo_change_status);
        mTodoDelete = popContentView.findViewById(R.id.todo_delete);
        mTodoChangeStatus.setOnClickListener(v -> {
            if (status == 0) {
                TodoData todoData= DbToDoHelperImpl.getInstance().loadAllTodoDataById(clickTodoId);
                todoData.setIsAccomplish(1);
                DbToDoHelperImpl.getInstance().updataTodoData(todoData);
            } else {
                TodoData todoData= DbToDoHelperImpl.getInstance().loadAllTodoDataById(clickTodoId);
                todoData.setIsAccomplish(0);
                DbToDoHelperImpl.getInstance().updataTodoData(todoData);
            }
            updataView();
            popupWindow.dismiss();
        });
        mTodoDelete.setOnClickListener(v -> {
            popupWindow.dismiss();
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.confirm_delete_todo)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DbToDoHelperImpl.getInstance().deletTodoDataById(clickTodoId);
                            updataView();
                        }
                    })

                    .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    }).show();
        });
    }

    private void initRecyclerView() {
        mAdapter = new TodoListAdapter(R.layout.item_todo_list, mTodoItemDataList);
        mAdapter.setOnItemClickListener((adapter, view, position) -> openEditTodo(view, position));
        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                clickTodoId = mAdapter.getData().get(position).getId();
                clickTodoPosition =position;
                if (status == 1) {
                    mTodoChangeStatus.setText(R.string.marked_todo);
                } else {
                    mTodoChangeStatus.setText(R.string.marked_completed);
                }
                popupWindow = showPopupWindow(view, popContentView);
                return true;
            }
        });
        rv.setHasFixedSize(true);
        rv.setAdapter(mAdapter);
    }


    private void openEditTodo(View view, int position) {
        if (mAdapter.getData().size() <= 0 || mAdapter.getData().size() < position) {
            return;
        }
        Intent intent = new Intent(getActivity(), AddTodoActivity.class);
        intent.putExtra(Constants.TODO_KEY.TODO_DATA, mAdapter.getData().get(position).getId());
        getActivity().startActivity(intent);
    }

    public static PopupWindow showPopupWindow(View anchorView, View contentView) {
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(contentView.getBackground());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);
        int windowPos[] = calculatePopWindowPos(anchorView, contentView);
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY, windowPos[0], windowPos[1]);
        return popupWindow;
    }
    /**
     * 计算出来的位置，y方向就在anchorView的中心对齐显示，x方向就是与View的中心点对齐
     *
     * @param anchorView  呼出window的view
     * @param contentView window的内容布局
     * @return window显示的左上角的xOff, yOff坐标
     */
    private static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        final int anchorWidth = anchorView.getWidth();
        final int screenHeight = anchorView.getContext().getResources().getDisplayMetrics().heightPixels;
        final int screenWidth = anchorView.getContext().getResources().getDisplayMetrics().widthPixels;
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (anchorLoc[1] >  screenHeight / 3);
        //偏移，否则会弹出在屏幕外
        int offset = windowWidth > anchorWidth ? (windowWidth - anchorWidth) : 0;
        //实际坐标中心点为触发view的中间
        windowPos[0] = (anchorLoc[0] + anchorWidth / 2) + offset;
        int offset2 = windowPos[0] + windowWidth - screenWidth;
        if (offset2 > 0) {
            windowPos[0] = windowPos[0] - offset2;
        }
        windowPos[1] = isNeedShowUp ? anchorLoc[1] - windowHeight + anchorHeight / 2 : anchorLoc[1] + anchorHeight / 2;
        return windowPos;
    }
}

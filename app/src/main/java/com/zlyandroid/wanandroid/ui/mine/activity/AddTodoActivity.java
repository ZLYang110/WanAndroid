package com.zlyandroid.wanandroid.ui.mine.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpActivity;
import com.zlyandroid.wanandroid.base.mvp.BasePresenter;
import com.zlyandroid.wanandroid.core.Constants;
import com.zlyandroid.wanandroid.db.todo.DbToDoHelperImpl;
import com.zlyandroid.wanandroid.db.greendao.TodoData;
import com.zlyandroid.wanandroid.event.RefreshTodoEvent;
import com.zlyandroid.wanandroid.util.DateUtils;
import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;


public class AddTodoActivity extends BaseMvpActivity {


    private static final String TAG = "AddTodoActivity";


    @BindView(R.id.abc)
    ActionBarCommon abc;
    @BindView(R.id.et_add_todo_title)
    EditText mAddTodoTitle;
    @BindView(R.id.et_add_todo_content)
    EditText mAddTodoContent;
    @BindView(R.id.rg_todo_priority)
    RadioGroup mPriorityRg;
    @BindView(R.id.rb_todo_priority_1)
    RadioButton mTodoPriority1;
    @BindView(R.id.rb_todo_priority_2)
    RadioButton mTodoPriority2;
    @BindView(R.id.tv_add_todo_label_content)
    TextView mAddTodoLabel;
    @BindView(R.id.tv_add_todo_date_content)
    TextView mAddTodoDate;

    private SparseArray<String> mTodoLabelArray = new SparseArray<>(5);
    private int mTodoId =-1;
    private int mTodoStatus = 0;
    String choiceLabel;
    private AlertDialog mDialog;

    TodoData todoItemData;

    public static void start(Context activity) {
        Intent intent = new Intent(activity, AddTodoActivity.class);
        activity.startActivity(intent);
    }
    @Override
    protected BasePresenter createPresenter() {
        return null;
    }
    @Override
    public int getLayoutID() {
        return R.layout.activity_add_todo;
    }

    @Override
    public void initData() {

    }


    @Override
    public void initView() {
        abc.setOnLeftIconClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTodoLabelArray.put(Constants.TODO_KEY.TODO_TYPE_ALL, getString(R.string.todo_no_label));
        mTodoLabelArray.put(Constants.TODO_KEY.TODO_TYPE_WORK, getString(R.string.todo_work));
        mTodoLabelArray.put(Constants.TODO_KEY.TODO_TYPE_STUDY, getString(R.string.todo_study));
        mTodoLabelArray.put(Constants.TODO_KEY.TODO_TYPE_LIFE, getString(R.string.todo_life));
        mTodoLabelArray.put(Constants.TODO_KEY.TODO_TYPE_OTHER, getString(R.string.todo_other));

       Long todoId =  getIntent().getLongExtra(Constants.TODO_KEY.TODO_DATA, 99999999);

        if (todoId != 99999999) {
            mTodoId=1;
            todoItemData=  DbToDoHelperImpl.getInstance().loadAllTodoDataById(todoId);
            abc.getTitleTextView().setText(R.string.todo_edit_title);
            mAddTodoTitle.setText(todoItemData.getTitle());
            mAddTodoContent.setText(todoItemData.getContent());
            if (todoItemData.getPriority() == 1) {
                mTodoPriority1.setChecked(true);
                mTodoPriority2.setChecked(false);
            } else {
                mTodoPriority1.setChecked(false);
                mTodoPriority2.setChecked(true);
            }

            mAddTodoLabel.setText(mTodoLabelArray.get(todoItemData.getType()));

            mAddTodoDate.setText(todoItemData.getDateStr());

        } else {
            mTodoId = -1;
            abc.getTitleTextView().setText(R.string.todo_new_title);
            mAddTodoLabel.setText(mTodoLabelArray.get(getIntent().getIntExtra(Constants.TODO_KEY.TODO_TYPE, 0)));
            mAddTodoDate.setText(DateUtils.getCurrentDate());
        }
    }

    @OnClick({R.id.tv_add_todo_label_content, R.id.tv_add_todo_date_content,
            R.id.iv_label_arrow_right, R.id.iv_date_arrow_right,
            R.id.bt_todo_save})
    @Override
    public void onClick(View v) {
        super.onClick(v);
    }



    @Override
    protected void onClick2(View v) {
        switch (v.getId()) {
            case R.id.tv_add_todo_label_content:
            case R.id.iv_label_arrow_right:
                choiceLabel = mAddTodoLabel.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.bt_choose_label);
                builder.setSingleChoiceItems(R.array.todo_labels,
                        mTodoLabelArray.indexOfValue(choiceLabel),
                        (dialog, which) -> choiceLabel = mTodoLabelArray.get(which));
                builder.setPositiveButton(R.string.ok,
                        (dialog, which) -> mAddTodoLabel.setText(choiceLabel));
                builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
                });
                AlertDialog alertDialog = builder.show();
                break;
            case R.id.tv_add_todo_date_content:
            case R.id.iv_date_arrow_right:
                Calendar calendar = DateUtils.dateString2Calendar(mAddTodoDate.getText().toString());
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mAddTodoDate.setText(String.format("%d-%d-%d", year, month + 1, dayOfMonth));
                    }
                }, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
            case R.id.bt_todo_save:
                if(TextUtils.isEmpty(mAddTodoTitle.getText().toString()) ){
                    mAddTodoTitle.requestFocus();
                    showToast("标题不能为空");
                    break;
                }

                if (mTodoId == -1) {
                    TodoData todoData=new TodoData();
                    todoData.setTitle(mAddTodoTitle.getText().toString());
                    todoData.setContent( mAddTodoContent.getText().toString());
                    // todoData.setDate(mAddTodoDate.getText().toString());
                    todoData.setDateStr(mAddTodoDate.getText().toString());
                    todoData.setPriority( mTodoPriority1.isChecked() ?
                            Constants.TODO_KEY.TODO_PRIORITY_FIRST : Constants.TODO_KEY.TODO_PRIORITY_SECOND);
                    todoData.setType(mTodoLabelArray.indexOfValue(
                            mAddTodoLabel.getText().toString()));
                    todoData.setIsAccomplish(0);
                    DbToDoHelperImpl.getInstance().addTodoData(todoData);
                } else {
                    todoItemData.setTitle(mAddTodoTitle.getText().toString());
                    todoItemData.setContent( mAddTodoContent.getText().toString());
                    // todoData.setDate(mAddTodoDate.getText().toString());
                    todoItemData.setDateStr(mAddTodoDate.getText().toString());
                    todoItemData.setPriority( mTodoPriority1.isChecked() ?
                            Constants.TODO_KEY.TODO_PRIORITY_FIRST : Constants.TODO_KEY.TODO_PRIORITY_SECOND);
                    todoItemData.setType(mTodoLabelArray.indexOfValue(
                            mAddTodoLabel.getText().toString()));
                    DbToDoHelperImpl.getInstance().updataTodoData(todoItemData);
                }
                mAddTodoTitle.setText("");
                mAddTodoContent.setText("");
                mTodoPriority1.setChecked(true);
                mAddTodoTitle.requestFocus();
                showToast("保存成功");
                new RefreshTodoEvent(-1).post();

                break;
            default:
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}

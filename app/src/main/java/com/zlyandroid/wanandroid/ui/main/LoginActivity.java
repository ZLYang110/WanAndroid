package com.zlyandroid.wanandroid.ui.main;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.zlyandroid.wanandroid.R;
import com.zlyandroid.wanandroid.base.BaseMvpActivity;
import com.zlyandroid.wanandroid.core.UserInfoManager;
import com.zlyandroid.wanandroid.event.LoginEvent;
import com.zlyandroid.wanandroid.listener.MyTextWatcher;
import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;
import com.zlyandroid.wanandroid.ui.main.presenter.LoginPresenter;
import com.zlyandroid.wanandroid.ui.main.view.LoginView;
import com.zlyandroid.wanandroid.util.AnimatorUtils;
import com.zlyandroid.wanandroid.util.SysUtils;
import com.zlyandroid.wanandroid.util.display.KeyboardWatcher;
import com.zlylib.titlebarlib.ActionBarCommon;
import com.zlylib.titlebarlib.OnActionBarChildClickListener;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseMvpActivity<LoginPresenter> implements LoginView , KeyboardWatcher.SoftKeyboardStateListener, View.OnFocusChangeListener {

    @BindView(R.id.abc)
    ActionBarCommon abc;

    @BindView(R.id.logo)
    ImageView logo;

    @BindView(R.id.slide_content)
    LinearLayout mSlideContent;

    @BindView(R.id.et_login_name)
    EditText etLoginName;
    @BindView(R.id.iv_clean_phone)
    ImageView iv_clean_phone;
    @BindView(R.id.et_login_pas)
    EditText etLoginPas;
    @BindView(R.id.clean_password)
    ImageView clean_password;
    @BindView(R.id.iv_show_pwd)
    ImageView iv_show_pwd;
    @BindView(R.id.cb_rem_user)
    CheckBox cbRemUser;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.service)
    LinearLayout service;


    private KeyboardWatcher keyboardWatcher;
    private int mRealScreenHeight = 0;
    //the position of mSlideContent on screen Y
    private int mSlideViewY = 0;


    @Override
    public int getLayoutID() {
        return R.layout.activity_login;
    }
    @Override
    protected LoginPresenter createPresenter() {
        return  new LoginPresenter();
    }
    @Override
    public void initView() {
        abc.setOnLeftIconClickListener(new OnActionBarChildClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnLogin.setOnClickListener(this);
        etLoginName.addTextChangedListener(textWatcher);
        etLoginPas.addTextChangedListener(textWatcher2);
        mRealScreenHeight = SysUtils.getDisplaySizeInfo(getContext()).y;
    }

    @Override
    public void initData() {

    }

    String pwd;
    @OnClick({R.id.btn_login,R.id.iv_clean_phone,R.id.clean_password,R.id.iv_show_pwd,R.id.tv_register,R.id.tv_forgetPwd})
    @Override
    protected void onClick2(View view) {
        switch (view.getId()) {
            case R.id.iv_clean_phone:
                etLoginName.setText("");
                break;
            case R.id.clean_password:
                etLoginPas.setText("");
                break;
            case R.id.iv_show_pwd:
                if (etLoginPas.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    etLoginPas.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    iv_show_pwd.setImageResource(R.mipmap.ic_pass_visuable);
                } else {
                    etLoginPas.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    iv_show_pwd.setImageResource(R.mipmap.ic_pass_gone);
                }
                  pwd = etLoginPas.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    etLoginPas.setSelection(pwd.length());
                break;
            case R.id.btn_login:
                String email = etLoginName.getText().toString().trim();
                  pwd = etLoginPas.getText().toString().trim();
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd))
                    // mPresenter.getList("北京",0,10);
                    mPresenter.login("zqpm","123456");

                break;
            case R.id.tv_register:
                MainActivity.createIntent(context);
                break;
            case R.id.tv_forgetPwd:
                MainActivity.createIntent(context);
                break;

        }
    }

    private MyTextWatcher textWatcher = new MyTextWatcher() {
        @Override
        public void afterMyTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(editable) && iv_clean_phone.getVisibility() == View.GONE) {
                iv_clean_phone.setVisibility(View.VISIBLE);
            } else if (TextUtils.isEmpty(editable)) {
                iv_clean_phone.setVisibility(View.GONE);
            }
        }
    };
    private MyTextWatcher textWatcher2 = new MyTextWatcher() {
        @Override
        public void afterMyTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(editable) && clean_password.getVisibility() == View.GONE) {
                clean_password.setVisibility(View.VISIBLE);
            } else if (TextUtils.isEmpty(editable)) {
                clean_password.setVisibility(View.GONE);
            }

        }
    };

    @Override
    public void onError(String throwable) {

    }

    @Override
    public void onSuccess(LoginBean bean) {
        showToast("登录成功");
        new LoginEvent(true).post();
       // toActivity(HomeActivity.createIntent(context),false);
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.swipeback_activity_close_top_in,
                R.anim.swipeback_activity_close_bottom_out);
    }


    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.overridePendingTransition(R.anim.swipeback_activity_open_bottom_in,
                    R.anim.swipeback_activity_open_top_out);
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        keyboardWatcher = new KeyboardWatcher(getActivity().findViewById(Window.ID_ANDROID_CONTENT));
        keyboardWatcher.addSoftKeyboardStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        keyboardWatcher.removeSoftKeyboardStateListener(this);
    }
    @Override
    public void onSoftKeyboardOpened(int keyboardSize) {
        int[] location = new int[2];
        //get mSlideContent's location on screen
        mSlideContent.getLocationOnScreen(location);
        if (mSlideViewY == 0) {
            mSlideViewY = location[1];
        }
        int bottom = mRealScreenHeight - (mSlideViewY + mSlideContent.getHeight());

        if (keyboardSize > bottom) {
            int slideDist = keyboardSize - bottom;
            AnimatorUtils.setViewAnimatorWhenKeyboardOpened(logo, mSlideContent, slideDist);
        }
    }

    @Override
    public void onSoftKeyboardClosed() {
        AnimatorUtils.setViewAnimatorWhenKeyboardClosed(logo, mSlideContent);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (hasFocus) {
                if (keyboardWatcher.isSoftKeyboardOpened()){
                    keyboardWatcher.setIsSoftKeyboardOpened(true);
                } else {
                    keyboardWatcher.setIsSoftKeyboardOpened(false);
                }
            }
        }
    }
}

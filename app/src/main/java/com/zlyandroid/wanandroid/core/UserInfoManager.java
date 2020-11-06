package com.zlyandroid.wanandroid.core;


import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.zlyandroid.wanandroid.ui.main.LoginActivity;
import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;
import com.zlyandroid.wanandroid.util.AesEncryptionUtils;
import com.zlyandroid.wanandroid.util.LogUtil;
import com.zlyandroid.wanandroid.util.PreUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.crypto.spec.SecretKeySpec;

/**
 * 用户信息管理类
 * author: zhangliyang
 * date: 2018/3/1
 */

public class UserInfoManager {
    private static String TAG = "UserInfoManager";
    private final static PreUtils mSPUtils = PreUtils.newInstance(Constants.USERINFO_KEY.SP_LOGIN);
    /**
     * 获取用户信息
     * @return
     */
    public static LoginBean getUserInfo() {
        SecretKeySpec keySpec = getAesKey();
        String userInfo = AesEncryptionUtils.decrypt(keySpec, (String) mSPUtils.get(Constants.USERINFO_KEY.USER_INFO, ""));
        if (TextUtils.isEmpty(userInfo)) return null;
        try {
            return translateStringTOUserInfo(userInfo);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 保存用户信息
     * @param user
     */
    public static void saveUserInfo(LoginBean user){
        try {
            String  userInfo = translateUserInfoTOString(user);
            SecretKeySpec key = AesEncryptionUtils.createKey();
            String aesContent = AesEncryptionUtils.encrypt(key, userInfo);
            //保存用户信息
            mSPUtils.save(Constants.USERINFO_KEY.USER_INFO, aesContent);
            //保存密钥
            saveAesKey(key);
        }  catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void logout() {
        mSPUtils.clear();
    }

    private static void saveAesKey(SecretKeySpec keySpec){
        mSPUtils.save(Constants.USERINFO_KEY.AES, Base64.encodeToString(keySpec.getEncoded(),Base64.DEFAULT));
    }

    private static SecretKeySpec getAesKey(){
        String keyStr = (String) mSPUtils.get(Constants.USERINFO_KEY.AES, "");
        return AesEncryptionUtils.getSecretKey(Base64.decode(keyStr, Base64.DEFAULT));
    }

    public static boolean isLogin() {
        return (boolean) mSPUtils.get(Constants.USERINFO_KEY.IS_LOGIN, false);
    }

    public static void saveIsLogin(boolean isLogin){
        mSPUtils.save(Constants.USERINFO_KEY.IS_LOGIN,isLogin);
    }

    /**
     * User 转 String
     * @param user
     * @return
     * @throws IOException
     */
    private static String translateUserInfoTOString(LoginBean user) throws IOException{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(user);
        return Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
    }

    /**
     * String 转 User
     * @param userStr
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private static LoginBean translateStringTOUserInfo(String userStr) throws IOException, ClassNotFoundException {
        if (userStr == null) return null;
        byte[] base64Bytes = Base64.decode(userStr,Base64.DEFAULT);
        ByteArrayInputStream bis = new ByteArrayInputStream(base64Bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        return (LoginBean) ois.readObject();
    }

    public static int getUserId() {
        LoginBean loginBean = getUserInfo();
        if (loginBean == null) {
            return 0;
        }
        return loginBean.getId();
    }


    public static boolean doIfLogin(Context context) {
        if (isLogin()) {
            return true;
        } else {
            LoginActivity.start(context);
            return false;
        }
    }

}

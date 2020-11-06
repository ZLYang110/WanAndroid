package com.zlyandroid.wanandroid.https.callback;


/**
 * 实体基类
 * Created by zhangliyang on 2018/2/1.
 */
public class ResponseBean<T> {
    private static int SUCCESS_CODE = 0;//成功的code

    /**
     * 服务器返回的错误码
     */
    public int errorCode;
    /**
     * 服务器返回的成功或失败的提示
     */
    public String errorMsg;
    /**
     * 服务器返回的数据
     */
    public T data;


    public int getCode() {
        return errorCode;
    }

    public void setCode(int code) {
        this.errorCode = code;
    }

    public String getMsg() {
        return errorMsg;
    }

    public void setMsg(String msg) {
        this.errorMsg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseBean(int errorCode, String errorMsg, T data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }
    public boolean isSuccess() {
        return getCode() == SUCCESS_CODE;
    }
    @Override
    public String toString() {
        return "BaseBean{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}

package com.zlyandroid.wanandroid.https;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.zlyandroid.wanandroid.https.api.APIService;
import com.zlyandroid.wanandroid.https.observer.BaseObserver;
import com.zlyandroid.wanandroid.https.retrofit.RetrofitUtils;
import com.zlyandroid.wanandroid.https.utils.RequestUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;


public class RHttp {
    String baseUrl = "https://www.wanandroid.com/";

    private static volatile RHttp instance;
    private APIService apiService;


    /*请求参数*/
    private Map<String, Object> parameter;
    /*header*/
    private Map<String, Object> header;

    /*超时时长*/
    private long timeout;
    /*时间单位*/
    private TimeUnit timeUnit;


    /*构造函数*/
    private RHttp(Builder builder) {
        this.parameter = builder.parameter;
        this.header = builder.header;
        this.timeout = builder.timeout;
        this.timeUnit = builder.timeUnit;
    }
    /*构造函数*/
    private RHttp() {  }



    public static RHttp getInstance() {
        if (instance == null) {
            synchronized (RHttp.class) {
                if (instance == null) {
                    instance = new RHttp();
                }
            }
        }
        return instance;
    }
    public APIService getApi() {
        /*Api接口*/
         apiService = RetrofitUtils.get().getRetrofit(baseUrl, getTimeout(), getTimeUnit()).create(APIService.class);
        return apiService;
    }



    /*执行普通Http请求*/
    public void execute(BaseObserver httpCallback) {
        if (httpCallback == null) {
            throw new NullPointerException("HttpObserver must not null!");
        } else {
            doRequest();
        }
    }

    /*执行请求*/
    private void doRequest() {

        /*header处理*/
        disposeHeader();

        /*Parameter处理*/
        disposeParameter();


    }




    /*获取基础URL*/
    private String getBaseUrl() {
        //如果没有重新指定URL则是用默认配置
        return TextUtils.isEmpty(baseUrl) ? Configure.get().getBaseUrl() : baseUrl;
    }

    /*获取超时时间*/
    private long getTimeout() {
        //当前请求未设置超时时间则使用全局配置
        return timeout == 0 ? Configure.get().getTimeout() : timeout;
    }

    /*获取超时时间单位*/
    private TimeUnit getTimeUnit() {
        //当前请求未设置超时时间单位则使用全局配置
        return timeUnit == null ? Configure.get().getTimeUnit() : timeUnit;
    }


    /*处理Header*/
    private void disposeHeader() {

        /*header空处理*/
        if (header == null) {
            header = new TreeMap<>();
        }

        //添加基础 Header
        Map<String, Object> baseHeader = Configure.get().getBaseHeader();
        if (baseHeader != null && baseHeader.size() > 0) {
            header.putAll(baseHeader);
        }

        if (!header.isEmpty()) {
            //处理header中文或者换行符出错问题
            for (String key : header.keySet()) {
                header.put(key, RequestUtils.getHeaderValueEncoded(header.get(key)));
            }
        }

    }

    /*处理 Parameter*/
    private void disposeParameter() {

        /*空处理*/
        if (parameter == null) {
            parameter = new TreeMap<>();
        }

        //添加基础 Parameter
        Map<String, Object> baseParameter = Configure.get().getBaseParameter();
        if (baseParameter != null && baseParameter.size() > 0) {
            parameter.putAll(baseParameter);
        }
    }

    /**
     * Configure配置
     */
    public static final class Configure {

        /*请求基础路径*/
        String baseUrl;
        /*超时时长*/
        long timeout;
        /*时间单位*/
        TimeUnit timeUnit;
        /*全局上下文*/
        Context context;
        /*全局Handler*/
        Handler handler;
        /*请求参数*/
        Map<String, Object> parameter;
        /*header*/
        Map<String, Object> header;
        /*是否显示Log*/
        boolean showLog;


        public static Configure get() {
            return Configure.Holder.holder;
        }

        private static class Holder {
            private static Configure holder = new Configure();
        }

        private Configure() {
            timeout = 60;//默认60秒
            timeUnit = TimeUnit.SECONDS;//默认秒
            showLog = true;//默认打印LOG
        }

        /*请求基础路径*/
        public RHttp.Configure baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        /*基础参数*/
        public RHttp.Configure baseParameter(Map<String, Object> parameter) {
            this.parameter = parameter;
            return this;
        }

        public Map<String, Object> getBaseParameter() {
            return parameter;
        }

        /*基础Header*/
        public RHttp.Configure baseHeader(Map<String, Object> header) {
            this.header = header;
            return this;
        }

        public Map<String, Object> getBaseHeader() {
            return header == null ? new TreeMap<String, Object>() : header;
        }

        /*超时时长*/
        public RHttp.Configure timeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        public long getTimeout() {
            return timeout;
        }

        /*是否显示LOG*/
        public RHttp.Configure showLog(boolean showLog) {
            this.showLog = showLog;
            return this;
        }

        public boolean isShowLog() {
            return showLog;
        }

        /*时间单位*/
        public RHttp.Configure timeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        /*Handler*/
        public Handler getHandler() {
            return handler;
        }

        /*Context*/
        public Context getContext() {
            return context;
        }

        /*初始化全局上下文*/
        public RHttp.Configure init(Application app) {
            this.context = app.getApplicationContext();
            this.handler = new Handler(Looper.getMainLooper());
            return this;
        }

    }

    /**
     * Builder
     * 构造Request所需参数，按需设置
     */
    public static final class Builder {
       
        /*请求参数*/
        Map<String, Object> parameter;
        /*header*/
        Map<String, Object> header;

        boolean isJson;
        /*超时时长*/
        long timeout;
        /*时间单位*/
        TimeUnit timeUnit;

        public Builder() {
        }



        /* 增加 Parameter 不断叠加参数 包括基础参数 */
        public RHttp.Builder addParameter(Map<String, Object> parameter) {
            if (this.parameter == null) {
                this.parameter = new TreeMap<>();
            }
            this.parameter.putAll(parameter);
            return this;
        }

        /*设置 Parameter 会覆盖 Parameter 包括基础参数*/
        public RHttp.Builder setParameter(Map<String, Object> parameter) {
            this.parameter = parameter;
            return this;
        }



        /* 增加 Header 不断叠加 Header 包括基础 Header */
        public RHttp.Builder addHeader(Map<String, Object> header) {
            if (this.header == null) {
                this.header = new TreeMap<>();
            }
            this.header.putAll(header);
            return this;
        }

        /*设置 Header 会覆盖 Header 包括基础参数*/
        public RHttp.Builder setHeader(Map<String, Object> header) {
            this.header = header;
            return this;
        }


        /*超时时长*/
        public RHttp.Builder timeout(long timeout) {
            this.timeout = timeout;
            return this;
        }

        /*时间单位*/
        public RHttp.Builder timeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
            return this;
        }

        public RHttp build() {
            return new RHttp(this);
        }
    }

}

package com.zlyandroid.wanandroid.https.retrofit;


import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zlyandroid.wanandroid.app.AppConfig;
import com.zlyandroid.wanandroid.app.AppContext;
import com.zlyandroid.wanandroid.https.convert.MyGsonConverterFactory;
import com.zlyandroid.wanandroid.https.gson.DoubleDefaultAdapter;
import com.zlyandroid.wanandroid.https.gson.IntegerDefaultAdapter;
import com.zlyandroid.wanandroid.https.gson.LongDefaultAdapter;
import com.zlyandroid.wanandroid.https.gson.StringNullAdapter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit工具类
 * 获取Retrofit 默认使用OkHttpClient
 *
 * @author zhangliyang
 */
public class RetrofitUtils {
    private static String TAG = "RetrofitUtils";

    private static RetrofitUtils instance = null;
    private static Retrofit.Builder retrofit;
    static ClearableCookieJar cookieJar;
    private Gson gson;

    private RetrofitUtils() {
        retrofit = new Retrofit.Builder();
    }

    public static RetrofitUtils get() {
        if (instance == null) {
            synchronized (RetrofitUtils.class) {
                if (instance == null) {
                    instance = new RetrofitUtils();
                    cookieJar  =
                    new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(AppContext.getContext()));
                }
            }
        }
        return instance;
    }

    /**
     * 获取Retrofit
     *
     * @param baseUrl 基础URL
     * @return
     */
    public Retrofit getRetrofit(String baseUrl, long timeout, TimeUnit timeUnit) {
        // Retrofit.Builder retrofit = new Retrofit.Builder();
        retrofit
                .client(getOkHttpClientBase(timeout, timeUnit))
                .baseUrl(baseUrl)

                .addConverterFactory(GsonConverterFactory.create())//添加json转换框架(正常转换框架)
               // .addConverterFactory(MyGsonConverterFactory.create(buildGson()))//添加json自定义（根据需求，此种方法是拦截gson解析所做操作）
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());//支持RxJava2
        return retrofit.build();
    }

    /**
     * 获取Retrofit
     *
     * @param baseUrl
     * @param client
     * @return
     */
    public Retrofit getRetrofit(String baseUrl, OkHttpClient client) {
        // Retrofit.Builder retrofit = new Retrofit.Builder();

        retrofit
                .client(client)
                .baseUrl(baseUrl)

                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        return retrofit.build();
    }
    /**
     * 增加后台返回""和"null"的处理,如果后台返回格式正常
     * 1.int=>0
     * 2.double=>0.00
     * 3.long=>0L
     * 4.String=>""
     *
     * @return
     */
    public Gson buildGson() {
        if (gson == null) {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Integer.class, new IntegerDefaultAdapter())
                    .registerTypeAdapter(int.class, new IntegerDefaultAdapter())
                    .registerTypeAdapter(Double.class, new DoubleDefaultAdapter())
                    .registerTypeAdapter(double.class, new DoubleDefaultAdapter())
                    .registerTypeAdapter(Long.class, new LongDefaultAdapter())
                    .registerTypeAdapter(long.class, new LongDefaultAdapter())
                    .registerTypeAdapter(String.class, new StringNullAdapter())
                    .create();
        }
        return gson;
    }

    /**
     * 获取OkHttpClient
     * 备注:下载时不能使用OkHttpClient单例,在拦截器中处理进度会导致多任务下载混乱
     *
     * @param timeout
     * @param interceptorArray
     * @return
     */
    public OkHttpClient getOkHttpClient(long timeout, TimeUnit timeUnit, Interceptor... interceptorArray) {
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        //超时设置
        okHttpClient
                .dns(new ApiDns())//处理一些识别识别不了 ipv6手机，如小米  实现方案  将ipv6与ipv4置换位置，首先用ipv4解析
                .connectTimeout(timeout, timeUnit)
                .writeTimeout(timeout, timeUnit)
                .readTimeout(timeout, timeUnit)
                .cookieJar(cookieJar)
        ;

        /**
         * https设置
         * 备注:信任所有证书,不安全有风险
         */
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        okHttpClient.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        /**
         * 配置https的域名匹配规则，不需要就不要加入，使用不当会导致https握手失败
         * 备注:google平台不允许直接返回true
         */
        //okHttpClient.hostnameVerifier(new HostnameVerifier() {        });

        //Interceptor设置
        if (interceptorArray != null) {
            for (Interceptor interceptor : interceptorArray) {
                okHttpClient.addInterceptor(interceptor);
            }
        }
        return okHttpClient.build();
    }


    /**
     * 获取下载时使用 OkHttpClient
     *
     * @param interceptorArray
     * @return
     */
    public OkHttpClient getOkHttpClientDownload(Interceptor... interceptorArray) {
        final long timeout = 60;//超时时长
        final TimeUnit timeUnit = TimeUnit.SECONDS;//单位秒
        return getOkHttpClient(timeout, timeUnit, interceptorArray);
    }

    /**
     * 获取基础Http请求使用 OkHttpClient
     *
     * @param timeout  超时时间
     * @param timeUnit 超时时间单位
     * @return
     */
    public OkHttpClient getOkHttpClientBase(long timeout, TimeUnit timeUnit) {
        //日志拦截器
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(TAG,"okHttp:" + message);
            }
        });
        //must
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //网络请求拦截器
        Interceptor httpInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Response response;
                try {
                    response = chain.proceed(request);
                } catch (final Exception e) {
                    throw e;
                }
                return response;
            }
        };

        Interceptor[] interceptorArray = new Interceptor[]{logInterceptor, httpInterceptor};
        return getOkHttpClient(timeout, timeUnit, interceptorArray);
    }

}

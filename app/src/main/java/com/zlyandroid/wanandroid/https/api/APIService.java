package com.zlyandroid.wanandroid.https.api;



import com.zlyandroid.wanandroid.ui.Knowledge.bean.ChapterBean;
import com.zlyandroid.wanandroid.ui.Knowledge.bean.NavigationBean;
import com.zlyandroid.wanandroid.ui.core.bean.BaseBean;
import com.zlyandroid.wanandroid.ui.home.bean.CollectionLinkBean;
import com.zlyandroid.wanandroid.ui.main.bean.LoginBean;
import com.zlyandroid.wanandroid.https.callback.ResponseBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleBean;
import com.zlyandroid.wanandroid.ui.home.bean.ArticleListBean;
import com.zlyandroid.wanandroid.ui.home.bean.BannerBean;
import com.zlyandroid.wanandroid.ui.home.bean.HotKeyBean;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRankBean;
import com.zlyandroid.wanandroid.ui.mine.bean.CoinRecordBean;
import com.zlyandroid.wanandroid.ui.mine.bean.UserInfoBean;
import com.zlyandroid.wanandroid.ui.mine.bean.UserPageBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author zhangliyang
 * @date 2018/4/24.
 * Description：
 */
public interface APIService {

    /**
     * 登录
     * 方法： POST
     * 参数：
     * username，password
     * 登录后会在cookie中返回账号密码，只要在客户端做cookie持久化存储即可自动登录验证。
     */

    @FormUrlEncoded
    @POST("user/login")
    Observable<ResponseBean<LoginBean>> login(@Field("username") String username,
                                                       @Field("password") String password);

    /**
     * 测试
     * http://image.so.com/j?q=北京&sn=0&pn=50
     * ?q={q}&sn={sn}&pn={pn}
     * Flowable
     */
    @GET("j")
    Observable<ResponseBean<LoginBean>> getList(@Query("q") String city,
                                                  @Query("sn") Integer sn,
                                                  @Query("pn") Integer pn);

    /**
     * 首页banner
     */
    @GET("banner/json")
    Observable<ResponseBean<List<BannerBean>>> getBanner();

    /**
     * 置顶文章
     * 方法：GET
     */
    @GET("article/top/json")
    Observable<ResponseBean<List<ArticleBean>>> getTopArticleList();
    /**
     * 首页文章列表
     * 方法：GET
     * 参数：页码，拼接在连接中，从0开始。
     */
    @GET("article/list/{page}/json")
    Observable<ResponseBean<ArticleListBean>> getArticleList(@Path("page") int page);

    /**
     * 搜索热词
     */
    @GET("hotkey/json")
    Observable<ResponseBean<List<HotKeyBean>>> getHotKeyList();

    /**
     * 搜索
     * 方法：POST
     * 参数：
     * 页码：拼接在链接上，从0开始。
     * k ： 搜索关键词
     * 支持多个关键词，用空格隔开
     */
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    Observable<ResponseBean<ArticleListBean>> search(@Path("page") int page,
                                                     @Field("k") String key);

    /**
     * 收藏站内文章
     * 方法：POST
     * 参数： 文章id，拼接在链接中。
     */
    @POST("lg/collect/{id}/json")
    Observable<ResponseBean<BaseBean>> collectArticle(@Path("id") int id);
    /**
     * 收藏站外文章
     * 方法：POST
     * 参数：
     * title，author，link
     */
    @FormUrlEncoded
    @POST("lg/collect/add/json")
    Observable<ResponseBean<ArticleBean>> collectArticle(@Field("title") String title,
                                                        @Field("author") String author,
                                                        @Field("link") String link);

    /**
     * 取消收藏 文章列表
     * 方法：POST
     * 参数：
     * id:拼接在链接上 id传入的是列表中文章的id。
     */
    @POST("lg/uncollect_originId/{id}/json")
    Observable<ResponseBean<BaseBean>> uncollectArticle(@Path("id") int id);
    /**
     * 取消收藏 我的收藏页面（该页面包含自己录入的内容）
     * 方法：POST
     * 参数：
     * id:拼接在链接上
     * originId:列表页下发，无则为-1
     * originId 代表的是你收藏之前的那篇文章本身的id； 但是收藏支持主动添加，这种情况下，没有originId则为-1
     */
    @FormUrlEncoded
    @POST("lg/uncollect/{id}/json")
    Observable<ResponseBean<BaseBean>> uncollectArticle(@Path("id") int id,
                                                       @Field("originId") int originId);


    /**
     * 收藏网址
     * 方法：POST
     * 参数：
     * name,link
     */
    @FormUrlEncoded
    @POST("lg/collect/addtool/json")
    Observable<ResponseBean<CollectionLinkBean>> collectLink(@Field("name") String name,
                                                            @Field("link") String link);

    /**
     * 删除收藏网站
     * 方法：POST
     * 参数：
     * id
     */
    @FormUrlEncoded
    @POST("lg/collect/deletetool/json")
    Observable<ResponseBean<BaseBean>> uncollectLink(@Field("id") int id);

    /**
     * 收藏文章列表
     * 方法：GET
     * 参数： 页码：拼接在链接中，从0开始。
     */
    @GET("lg/collect/list/{page}/json")
    Observable<ResponseBean<ArticleListBean>> getCollectArticleList(@Path("page") int page);

    /**
     * 收藏网站列表
     * 方法：GET
     */
    @GET("lg/collect/usertools/json")
    Observable<ResponseBean<List<CollectionLinkBean>>> getCollectLinkList();

    /**
     * 分享人对应列表数据
     * page 从1开始
     */
    @GET("user/{userId}/share_articles/{page}/json")
    Observable<ResponseBean<UserPageBean>> getUserPage(@Path("userId") int userId,
                                                       @Path("page") int page);



    /**
     * 搜索热词
     */
    @GET("navi/json")
    Observable<ResponseBean<List<NavigationBean>>> getNaviList();

    /**
     * 体系数据
     */
    @GET("tree/json")
    Observable<ResponseBean<List<ChapterBean>>> getKnowledgeList();

    /**
     * 知识体系下的文章
     * 方法：GET
     * 参数：
     * cid 分类的id，上述二级目录的id
     * 页码：拼接在链接上，从0开始。
     */
    @GET("article/list/{page}/json")
    Observable<ResponseBean<ArticleListBean>> getKnowledgeArticleList(@Path("page") int page,
                                                                     @Query("cid") int id);

    /**
     * 问答
     * pageId,拼接在链接上，例如上面的1
     */
    @GET("wenda/list/{page}/json")
    Observable<ResponseBean<ArticleListBean>> getQuestionList(@Path("page") int page);

    /**
     * 获取个人积分
     */
    @GET("lg/coin/userinfo/json")
    Observable<ResponseBean<UserInfoBean>> getUserInfo();

    /**
     * 获取个人积分
     */
    @GET("lg/coin/getcount/json")
    Observable<ResponseBean<Integer>> getCoin();


    /**
     * 获取个人积分获取列表
     * page 1开始
     */
    @GET("lg/coin/list/{page}/json")
    Observable<ResponseBean<CoinRecordBean>> getCoinRecordList(@Path("page") int page);

    /**
     * 积分排行榜接口
     * page 1开始
     */
    @GET("coin/rank/{page}/json")
    Observable<ResponseBean<CoinRankBean>> getCoinRankList(@Path("page") int page);




    /**
     * 自己的分享的文章列表
     * 页码，从1开始
     */
    @GET("user/lg/private_articles/{page}/json")
    Observable<ResponseBean<UserPageBean>> getMineShareArticleList(@Path("page") int page);

    /**
     * 删除自己分享的文章
     * 文章id，拼接在链接上
     */
    @POST("lg/user_article/delete/{id}/json")
    Observable<ResponseBean<BaseBean>> deleteMineShareArticle(@Path("id") int id);

    /**
     * 分享文章
     * 注意需要登录后查看，如果为CSDN，简书等链接会直接通过审核，在对外的分享文章列表中展示。
     * title
     * link
     */
    @FormUrlEncoded
    @POST("lg/user_article/add/json")
    Observable<ResponseBean<BaseBean>> shareArticle(@Field("title") String title,
                                                    @Field("link") String link);
}

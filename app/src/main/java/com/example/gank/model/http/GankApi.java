package com.example.gank.model.http;

import com.example.gank.model.bean.GankItemList;
import com.example.gank.model.bean.GankResponseList;
import com.example.gank.model.bean.GankSearchList;

import java.util.List;


import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by dell on 2016/9/16.
 */
public interface GankApi {

/*
    文章列表
* */
    @GET("data/{type}/{num}/{page}")
    Observable<GankResponseList<List<GankItemList>>> getArticleList(@Path("type") String type,
                                                                     @Path("num") int  num,
                                                                     @Path("page") int page);
    /*
    * 图片
    * */
    @GET("data/福利/{num}/{page}")
    Observable<GankResponseList<List<GankItemList>>> getPic(@Path("num") int num,
                                                            @Path("page") int page);

    /*
    * 随机图片
    * */
    @GET("random/data/福利/{page}")
    Observable<GankResponseList<List<GankItemList>>> getRandomPic(@Path("page") int page);

    /*    * 搜索
    * */
    @GET("search/query/{query}/category/{category}/count/{count}/page/{page}")
    Observable<GankResponseList<List<GankSearchList>>> getSeachList(@Path("query") String query,
                                                                    @Path("category") String category,
                                                                    @Path("count") int count,
                                                                    @Path("page") int page);
}

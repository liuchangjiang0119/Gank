package com.example.gank.gank;

import android.util.Log;

import com.example.gank.App;
import com.example.gank.SystemUtil;
import com.example.gank.model.bean.GankItemList;
import com.example.gank.model.bean.GankResponseList;
import com.example.gank.model.bean.GankSearchList;
import com.example.gank.model.http.GankApi;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by dell on 2016/9/16.
 */
public class GankMainPresenter implements GankMainContract.Presenter {

    public static final String BASE_URL = "http://gank.io/api/";
    public static final String TYPE_ANDROID = "Android";
    public static final String TYPE_IOS = "iOS";
    public static final String TYPE_WEB = "前端";
    public static final String TYPE_PIC = "福利";

    public static final int NUM_OF_PAGE = 20;

    private int currentPage = 1;

    GankMainContract.View mView;
    GankMainContract.PicView mPicView;
    GankMainContract.SearchView mSearchView;

    public GankMainPresenter(GankMainContract.SearchView searchView) {
        mSearchView = searchView;
    }

    public GankMainPresenter(GankMainContract.View view) {
        this.mView=view;
    }

    public GankMainPresenter(GankMainContract.PicView picView,String type){
        this.mPicView = picView;
    }


    @Override
    public void loadArticle(String type) {


        getGankApiWithCache().getArticleList(type,NUM_OF_PAGE,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankResponseList<List<GankItemList>>>() {
                    @Override
                    public void onCompleted() {


                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        mView.loadError();


                    }

                    @Override
                    public void onNext(GankResponseList<List<GankItemList>> list) {
                        mView.addItemList(list.getResults());

                    }
                });

    }


    @Override
    public void loadSearch(String query) {
        getGankApi().getSeachList(query,"all",NUM_OF_PAGE,1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankResponseList<List<GankSearchList>>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("---------","success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("---------","failed");
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(GankResponseList<List<GankSearchList>> list) {
                        mSearchView.addSearchResult(list.getResults());
                    }
                });

    }

    @Override
    public void loadMoreSearch(String query) {
        getGankApi().getSeachList(query,"all",NUM_OF_PAGE,++currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankResponseList<List<GankSearchList>>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("----------","more");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GankResponseList<List<GankSearchList>> list) {
                        mSearchView.addMoreSearchResult(list.getResults());
                    }
                });
    }

    @Override
    public void loadMoreArticle(String type) {
        getGankApiWithCache().getArticleList(type,NUM_OF_PAGE,++currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankResponseList<List<GankItemList>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.loadError();
                    }

                    @Override
                    public void onNext(GankResponseList<List<GankItemList>> list) {
                        mView.addMoreItemList(list.getResults());
                    }
                });
    }

    @Override
    public void loadMoreRandomPic() {
        getGankApi().getPic(21,++currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankResponseList<List<GankItemList>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GankResponseList<List<GankItemList>> list) {
                        mPicView.addMoreRandomPic(list.getResults());
                    }
                });

    }

    @Override
    public void loadImage() {
        getGankApi().getRandomPic(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankResponseList<List<GankItemList>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(GankResponseList<List<GankItemList>> list) {
                        mView.addPicUrl(list.getResults().get(0).getUrl());
                    }
                });

    }

    @Override
    public void loadRandomPic() {
        getGankApi().getPic(21,currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankResponseList<List<GankItemList>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mPicView.loadError();
                    }

                    @Override
                    public void onNext(GankResponseList<List<GankItemList>> list) {
                        mPicView.addRandomPic(list.getResults());
                    }
                });

    }
    static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());
            if (SystemUtil.isNetConnected(App.getInstance())){
                Log.d("------------","has net");
                int maxAge = 60*60;
                return response.newBuilder().header("Cache-Control","public,max-age="+maxAge).removeHeader("Pragma").build();
            }else {
                Log.d("--------------","no net");
                int maxStale = 60*60*24*28;
                return response.newBuilder().header("Cache-Control","public,only-if-cache,max-stale="+maxStale).removeHeader("Pragma").build();
            }

        }
    };
    public GankApi getGankApiWithCache(){
        File cacheDirectory = new File(App.getInstance().getExternalCacheDir(),"respons");
        int cacheSize = 10*1024*1024;
        Cache cache = new Cache(cacheDirectory,cacheSize);
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR)
                .cache(cache).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        GankApi gankApiWithCache = retrofit.create(GankApi.class);
        return gankApiWithCache;

    }
    public GankApi getGankApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        GankApi gankApi = retrofit.create(GankApi.class);
        return gankApi;
    }

}

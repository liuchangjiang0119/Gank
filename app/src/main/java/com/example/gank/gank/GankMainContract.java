package com.example.gank.gank;

import com.example.gank.base.BasePresent;
import com.example.gank.base.BaseView;
import com.example.gank.model.bean.GankItemList;
import com.example.gank.model.bean.GankSearchList;

import java.util.List;

/**
 * Created by dell on 2016/9/16.
 */
public class GankMainContract {

    public  interface Presenter extends BasePresent{
        void loadArticle(String type);
        void loadSearch(String query);
        void loadMoreArticle(String type);
        void loadMoreSearch(String query);
        void loadMoreRandomPic();
        void loadImage();
        void loadRandomPic();
    }
    public interface View extends BaseView<Presenter>{
        void addItemList(List<GankItemList> lists);
        void addMoreItemList(List<GankItemList> lists);
        void addPicUrl(String url);
        void loadError();

    }
    public interface PicView extends BaseView<Presenter>{
        void addRandomPic(List<GankItemList> lists);
        void addMoreRandomPic(List<GankItemList> lists);
        void loadError();
    }

    public interface SearchView extends BaseView<Presenter>{
        void addSearchResult(List<GankSearchList> lists);
        void addMoreSearchResult(List<GankSearchList> lists);
    }

}

package com.example.gank.gank.fragment;

import com.example.gank.dao.MyFavoriteDao;
import com.example.gank.model.bean.GankItemList;
import com.example.gank.model.bean.MyFavorite;

import java.util.List;

/**
 * Created by dell on 2016/10/16.
 */

public interface CallBack {
    void starActivity(GankItemList list);
    void starPicActivity(String arg);

}

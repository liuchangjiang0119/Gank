package com.example.gank;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.gank.dao.DaoMaster;
import com.example.gank.dao.DaoSession;

/**
 * Created by dell on 2016/9/20.
 */
public class App extends Application {
    private static App instance;
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase mDatabase;
    private DaoSession mDaoSession;
    private DaoMaster mDaoMaster;
    public static App getInstance(){


        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        initDataBase();
    }

    void initDataBase(){
        mHelper = new DaoMaster.DevOpenHelper(this,"favorite",null);
        mDatabase = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mDatabase);
        mDaoSession = mDaoMaster.newSession();


    }
    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }
}

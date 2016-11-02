package com.example.gank.gank.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.gank.App;
import com.example.gank.R;
import com.example.gank.SystemUtil;
import com.example.gank.dao.MyFavoriteDao;
import com.example.gank.model.bean.MyFavorite;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tt.whorlviewlibrary.WhorlView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleDetailAcitvity extends AppCompatActivity {
    @BindView(R.id.x5webview)
    com.tencent.smtt.sdk.WebView x5Webview;

    @BindView(R.id.whorlView)
    WhorlView mWhorlView;

    private String url;
    private MyFavoriteDao mFavoriteDao;
    private MyFavorite mFavorite;
    private String desc;
    private String who;
    private String publishedAt;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);
        ButterKnife.bind(this);
        mContext = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("干货详情");
        mWhorlView.start();
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        desc = intent.getStringExtra("desc");
        publishedAt = intent.getStringExtra("publishedAt");
        who = (intent.getStringExtra("who") !=null)?intent.getStringExtra("who") : "佚名";





        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initWebView();
//

        mFavoriteDao = App.getInstance().getDaoSession().getMyFavoriteDao();

    }


    public void initWebView(){
        x5Webview.setVisibility(View.GONE);
        WebSettings webSettings = x5Webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        x5Webview.loadUrl(url);
        x5Webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String s) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, s);
            }

            @Override
            public void onPageFinished(WebView view, String s) {
                super.onPageFinished(view, s);
                mWhorlView.setVisibility(View.GONE);
                x5Webview.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&x5Webview.canGoBack()){
            x5Webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.activity_article_menu,menu);
        if (mFavoriteDao.queryBuilder().where(MyFavoriteDao.Properties.Url.eq(url)).list().size()!=0){
            menu.findItem(R.id.favorite).setIcon(R.drawable.ic_favorite_black_24dp);
        }
        MenuItem menuItem = menu.findItem(R.id.favorite);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mFavorite = new MyFavorite(null, url, desc, who, publishedAt.substring(5,10));
                if (item.getIcon().getConstantState().
                        equals(ContextCompat.getDrawable(mContext,R.drawable.ic_favorite_border_black_24dp).getConstantState())){
                Log.d("-------------",item.getIcon().toString());
                item.setIcon(R.drawable.ic_favorite_black_24dp);

                    mFavoriteDao.insert(mFavorite);
                    Toast.makeText(ArticleDetailAcitvity.this, "收藏成功", Toast.LENGTH_LONG).show();
                }else if (item.getIcon().getConstantState().
                        equals(ContextCompat.getDrawable(mContext,R.drawable.ic_favorite_black_24dp).getConstantState())){
                    item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                    mFavoriteDao.queryBuilder().where(MyFavoriteDao.Properties.Url.eq(url)).buildDelete().executeDeleteWithoutDetachingEntities();
                    Toast.makeText(ArticleDetailAcitvity.this, "取消收藏", Toast.LENGTH_LONG).show();
                    }


                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                super.onBackPressed();
                break;
            case R.id.copy:
                SystemUtil.copy(url,mContext);
                Toast.makeText(ArticleDetailAcitvity.this,"链接已复制",Toast.LENGTH_LONG).show();
                break;
            case R.id.browser:
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}

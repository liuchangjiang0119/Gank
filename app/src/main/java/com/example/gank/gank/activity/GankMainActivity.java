package com.example.gank.gank.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.gank.App;
import com.example.gank.R;
import com.example.gank.dao.MyFavoriteDao;
import com.example.gank.gank.GankMainFragment;
import com.example.gank.gank.fragment.CallBack;
import com.example.gank.model.bean.GankItemList;
import com.example.gank.model.bean.MyFavorite;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GankMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,CallBack {

    @BindView(R.id.toolbar)
    Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        toolbar.setTitle("干货集中营");
        setSupportActionBar(toolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new GankMainFragment()).commit();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("确定要退出吗？")
                .setPositiveButton("确定",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)MenuItemCompat.getActionView(searchItem);
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(GankMainActivity.this,SearchActivity.class);
                intent.putExtra("info",query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();




        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.my_favorite :
                Intent intent = new Intent(this,MyFavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.night_model:
                if(item.getTitle()=="夜间模式"){
                    item.setTitle("日间模式");
                    item.setIcon(R.drawable.ic_brightness_low_black_24dp);
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else {
                    item.setTitle("夜间模式");
                    item.setIcon(R.drawable.ic_brightness_3_black_24dp);

                }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




    @Override
    public void starActivity(GankItemList lists) {
        Intent intent = new Intent(this,ArticleDetailAcitvity.class);
        intent.putExtra("url",lists.getUrl());
        intent.putExtra("desc",lists.getDesc());
        intent.putExtra("who",lists.getWho());
        intent.putExtra("publishedAt",lists.getPublishedAt());
        startActivity(intent);

    }

    @Override
    public void starPicActivity(String arg) {
        Intent intent = new Intent(this,PicDetailActivity.class);
        intent.putExtra("pic_url",arg);
        startActivity(intent);
    }


}

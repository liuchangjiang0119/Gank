package com.example.gank.gank.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gank.App;
import com.example.gank.R;
import com.example.gank.dao.MyFavoriteDao;
import com.example.gank.gank.adapter.MyFavoriteRecyclerViewAdapter;
import com.example.gank.gank.adapter.SearchResultViewAdapter;
import com.example.gank.model.bean.MyFavorite;

import org.greenrobot.greendao.query.QueryBuilder;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyFavoriteActivity extends AppCompatActivity {

    @BindView(R.id.favorite_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.favorite_text)
    TextView mTextView;

    private LinearLayoutManager mLayoutManager;
    private MyFavoriteRecyclerViewAdapter mAdapter;
    private List<MyFavorite> favoriteList = new ArrayList<>();
    private MyFavoriteDao mFavoriteDao;
    private List<String> pupList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        ButterKnife.bind(this);
        mTextView.setVisibility(View.GONE);
        setTitle("我的收藏");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        queryData();
        if (favoriteList.size()==0){
            mRecyclerView.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
        }
        initView();
        mAdapter.setFavoriteList(favoriteList);

    }

    void initView(){
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyFavoriteRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MyFavoriteRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MyFavoriteActivity.this,ArticleDetailAcitvity.class);
                intent.putExtra("url",favoriteList.get(position).getUrl());
                intent.putExtra("who",favoriteList.get(position).getWho());
                intent.putExtra("publishedAt",favoriteList.get(position).getPublishedAt());
                intent.putExtra("desc",favoriteList.get(position).getDesc());

                startActivity(intent);
            }
        });
        pupList.add("删除");
        mAdapter.setOnItemLongClickListener(new MyFavoriteRecyclerViewAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                showPopupView(view,position);
            }
        });

    }
    void queryData(){
        mFavoriteDao = App.getInstance().getDaoSession().getMyFavoriteDao();
        QueryBuilder queryBuilder = mFavoriteDao.queryBuilder();
        favoriteList = queryBuilder.list();
    }
    void showPopupView(View view, final int position){
        final int item_position = position;
        final ListPopupWindow listPopupWindow =new ListPopupWindow(this);

        listPopupWindow.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,pupList));
        listPopupWindow.setWidth(400);
        listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        listPopupWindow.setHorizontalOffset(400);
        listPopupWindow.setAnchorView(view);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int which, long id) {
                mFavoriteDao.queryBuilder().where(MyFavoriteDao.Properties.Url.
                        eq(favoriteList.get(item_position).getUrl())).buildDelete().
                        executeDeleteWithoutDetachingEntities();
                        mAdapter.deleteItem(item_position);
                        listPopupWindow.dismiss();

            }
        });
        listPopupWindow.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }
}

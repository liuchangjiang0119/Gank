package com.example.gank.gank.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.gank.R;
import com.example.gank.gank.GankMainContract;
import com.example.gank.gank.GankMainPresenter;
import com.example.gank.gank.adapter.SearchResultViewAdapter;
import com.example.gank.model.bean.GankSearchList;
import com.tt.whorlviewlibrary.WhorlView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class SearchActivity extends AppCompatActivity implements GankMainContract.SearchView {


    @BindView(R.id.recylerView_search)
    RecyclerView mRecyclerView;
    @BindView(R.id.search_fresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.search_whorl)
    WhorlView mWhorlView;

    private String searchInfo;
    private SearchResultViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private GankMainPresenter mPresenter;
    private List<GankSearchList> mSearchLists;
    private boolean canAddMore = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        setTitle("搜索结果");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mRecyclerView.setVisibility(View.GONE);
        mWhorlView.setVisibility(View.VISIBLE);
        mWhorlView.start();
        Intent intent = getIntent();
        searchInfo = intent.getStringExtra("info");

        mSearchLists = new ArrayList<>();
        mPresenter = new GankMainPresenter(this);

        initView();
        getData();


    }

    void initView(){
        mAdapter = new SearchResultViewAdapter(this);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadSearch(searchInfo);
            }
        });
        mAdapter.setOnItemClickListener(new SearchResultViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(SearchActivity.this,ArticleDetailAcitvity.class);
                intent.putExtra("url",mSearchLists.get(position).getUrl());
                startActivity(intent);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE&&
                        mLayoutManager.findLastVisibleItemPosition()+1==mLayoutManager.getItemCount()){
                        if (canAddMore) {
                            mPresenter.loadMoreSearch(searchInfo);
                        }else {
                            Toast.makeText(SearchActivity.this,"无更多结果",Toast.LENGTH_LONG).show();

                        }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    void getData(){

        mPresenter.loadSearch(searchInfo);
    }


    @Override
    public void addSearchResult(List<GankSearchList> lists) {
        mWhorlView.stop();
        mWhorlView.setVisibility(GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        if (lists!=null){
            mSearchLists.clear();
            mSearchLists.addAll(lists);
            if (mRefreshLayout.isRefreshing()){
                mRefreshLayout.setRefreshing(false);
            }
            mAdapter.setSearchList(mSearchLists);
        }else {
            Toast.makeText(SearchActivity.this,"搜索结果为空",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void addMoreSearchResult(List<GankSearchList> lists) {
        if (lists.size()==0){
            canAddMore = false;
        }
        mSearchLists.addAll(lists);
        mAdapter.setSearchList(mSearchLists);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}

package com.example.gank.gank.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gank.R;
import com.example.gank.gank.GankMainContract;
import com.example.gank.gank.GankMainPresenter;
import com.example.gank.gank.adapter.ArticleRecyclerViewAdapter;
import com.example.gank.model.bean.GankItemList;
import com.tt.whorlviewlibrary.WhorlView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2016/10/13.
 */

public class IosFragment extends Fragment implements GankMainContract.View{
    @BindView(R.id.recylerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_fresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.fragment_whorlView)
    WhorlView mWhorlView;

    private ArticleRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private String pic_url;
    private List<GankItemList> mLists;
    private CallBack mCallBack;
    private GankMainPresenter mPresenter;
//
    public static IosFragment newInstance(){
        Bundle bundle = new Bundle();
        bundle.putString("TYPE","IOS");
        IosFragment iosFragment = new IosFragment();
        iosFragment.setArguments(bundle);
        return iosFragment;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()){
//            mLists = new ArrayList<>();
//            pic_list = new ArrayList<>();
//            mPresenter = new GankMainPresenter(this);
//            mPresenter.loadImage();
//            mPresenter.loadArticle(GankMainPresenter.TYPE_IOS);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBack = (CallBack)context;
    }

    //
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("Ios","onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.d("Ios","onCreateView");
        return inflater.inflate(R.layout.article_list_fragment,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        mRecyclerView.setVisibility(View.GONE);
        mWhorlView.setVisibility(View.VISIBLE);
        mWhorlView.start();
        initRecyclerView();
//        Log.d("Ios","onViewCreate");
        getData();



    }

    @Override
    public void onResume() {
        super.onResume();
//        Log.d("Ios","onresume");
    }
    public void getData(){
        mLists = new ArrayList<>();

        mPresenter = new GankMainPresenter(this);
        if (pic_url==null) {
            mPresenter.loadImage();
        }else {
            mAdapter.setPicUrl(pic_url);
        }
        mPresenter.loadArticle(GankMainPresenter.TYPE_IOS);
    }

    @Override
    public void addMoreItemList(List<GankItemList> lists) {
        mLists.addAll(lists);
        mAdapter.setItemList(mLists);
    }

    public void initRecyclerView(){
        mAdapter = new ArticleRecyclerViewAdapter(getActivity().getApplicationContext(),GankMainPresenter.TYPE_IOS);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter.setOnItemClickListener(new ArticleRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCallBack.starActivity(mLists.get(position));
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE&&
                        mLayoutManager.findLastVisibleItemPosition()+1==mLayoutManager.getItemCount()){
                    mPresenter.loadMoreArticle(GankMainPresenter.TYPE_IOS);
                }

            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadImage();
                mPresenter.loadArticle(GankMainPresenter.TYPE_IOS);
            }
        });

    }

    @Override
    public void addItemList(List<GankItemList> lists) {
        if (mWhorlView.isCircling()){
            mWhorlView.stop();
            mWhorlView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        if (lists!=null){
            mLists.clear();
            mLists.addAll(lists);
            mAdapter.setItemList(lists);
            mRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void addPicUrl(String url) {

        pic_url = url;
        mAdapter.setPicUrl(pic_url);
    }
    @Override
    public void loadError() {
        mRefreshLayout.setRefreshing(false);
        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
    }


}

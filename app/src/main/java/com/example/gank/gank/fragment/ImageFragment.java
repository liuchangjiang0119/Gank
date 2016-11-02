package com.example.gank.gank.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gank.R;
import com.example.gank.gank.GankMainContract;
import com.example.gank.gank.GankMainPresenter;
import com.example.gank.gank.adapter.ArticleRecyclerViewAdapter;
import com.example.gank.gank.adapter.RandomPicRecyclerViewAdapter;
import com.example.gank.model.bean.GankItemList;
import com.tt.whorlviewlibrary.WhorlView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2016/9/23.
 */
public class ImageFragment extends Fragment implements GankMainContract.PicView {
    @BindView(R.id.pic_recyclerView)
    RecyclerView pic_recyclerView;
    @BindView(R.id.swipe_fresh_pic)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.pic_whorlView)
    WhorlView mWhorlView;

    private StaggeredGridLayoutManager mLayoutManager;
    List<GankItemList> mLists;
    private GankMainPresenter mPresenter;
    private CallBack mCallBack;

    private RandomPicRecyclerViewAdapter mAdapter;
    public static ImageFragment newInstance(){
        Bundle bundle = new Bundle();
        bundle.putInt("TYPE",3);
        ImageFragment imageFragment = new ImageFragment();
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBack = (CallBack)context;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.random_pic_fragment,container,false);
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pic_recyclerView.setVisibility(View.GONE);
        mWhorlView.setVisibility(View.VISIBLE);
        mWhorlView.start();
        initview();
        getData();

    }
    public void getData(){
        mLists = new ArrayList<>();
        mPresenter = new GankMainPresenter(this,"PIC");
        mPresenter.loadRandomPic();
    }

    public void initview(){
        mLayoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        pic_recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RandomPicRecyclerViewAdapter(getActivity());
        mAdapter.setOnItemClickListener(new ArticleRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCallBack.starPicActivity(mLists.get(position).getUrl());
            }
        });
        pic_recyclerView.setAdapter(mAdapter);
        pic_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int [] span = new int[mLayoutManager.getSpanCount()];
                int [] lastPositions = mLayoutManager.findLastVisibleItemPositions(span);
                int lastPosition = findMax(lastPositions) ;

                if (newState==RecyclerView.SCROLL_STATE_IDLE&&
                        lastPosition+1==mLayoutManager.getItemCount()){
                    mPresenter.loadMoreRandomPic();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadRandomPic();
            }
        });

    }



    @Override
    public void addRandomPic(List<GankItemList> lists) {
        if (mWhorlView.isCircling()){
            mWhorlView.stop();
            mWhorlView.setVisibility(View.GONE);
            pic_recyclerView.setVisibility(View.VISIBLE);
        }
        if (lists!=null){
            mLists.clear();
            mLists.addAll(lists);

            mAdapter.setRandomPicList(mLists);
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void addMoreRandomPic(List<GankItemList> lists) {
        mLists.addAll(lists);
        mAdapter.setRandomPicList(mLists);
    }

    private int findMax(int [] array) {
        int max = array[0];
        for (int value : array) {
            if (value > max) {
                max = value;
            }
        }
        return max;

    }

    @Override
    public void loadError() {
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(getActivity(),"加载失败",Toast.LENGTH_LONG).show();
    }
}

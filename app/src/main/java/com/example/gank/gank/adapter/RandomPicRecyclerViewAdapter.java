package com.example.gank.gank.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.gank.R;
import com.example.gank.model.bean.GankItemList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2016/9/23.
 */
public class RandomPicRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;

    private ImageView mImageView;
    List<GankItemList> mLists = new ArrayList<>();

    public RandomPicRecyclerViewAdapter(Context context) {

        mContext = context;
    }

    public void setRandomPicList(List<GankItemList> lists){
        this.mLists = lists;
        this.notifyDataSetChanged();

    }
    private ArticleRecyclerViewAdapter.OnItemClickListener mOnItemClickListener;
    private ArticleRecyclerViewAdapter.OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(ArticleRecyclerViewAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(ArticleRecyclerViewAdapter.OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.item_random_pic,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        /*StaggeredGridLayoutManager 加载item上海图片位置发生变化，尚未解决，暂且设置为固定值
        * */
        params.height = 600;
        holder.itemView.setLayoutParams(params);

        mImageView =((ViewHolder)holder).random_image;

        Glide.with(mContext).load(mLists.get(position).getUrl()).asBitmap().into(mImageView);
        if (mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mLists.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.random_image)
        ImageView random_image;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

package com.example.gank.gank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gank.R;
import com.example.gank.model.bean.MyFavorite;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2016/10/20.
 */

public class MyFavoriteRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<MyFavorite> mList = new ArrayList<>();
    public MyFavoriteRecyclerViewAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setFavoriteList(List<MyFavorite> list){
        mList = list;
        this.notifyDataSetChanged();
    }
    public void deleteItem(int item_position){
        mList.remove(item_position);
        this.notifyItemRemoved(item_position);
    }

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
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
        View view = mInflater.inflate(R.layout.item_article,parent,false);
        MyFavoriteViewHolder viewHolder = new MyFavoriteViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((MyFavoriteViewHolder)holder).title_text.setText(mList.get(position).getDesc());
        ((MyFavoriteViewHolder)holder).author_text.setText(mList.get(position).getWho());
        ((MyFavoriteViewHolder)holder).data_text.setText(mList.get(position).getPublishedAt());
        if (mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });
        }
        if (mOnItemLongClickListener !=null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.itemView,position);
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyFavoriteViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.title_text)
        TextView title_text;
        @BindView(R.id.author_text)
        TextView author_text;
        @BindView(R.id.data_text)
        TextView data_text;

        public MyFavoriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

package com.example.gank.gank.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gank.R;
import com.example.gank.model.bean.GankSearchList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2016/10/18.
 */

public class SearchResultViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<GankSearchList> mSearchLists = new ArrayList<>();

    public SearchResultViewAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);

    }
    public void setSearchList(List<GankSearchList> list){
        this.mSearchLists = list;
        this.notifyDataSetChanged();

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
        ResultViewHolder viewHolder = new ResultViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ((ResultViewHolder)holder).title_text.setText(mSearchLists.get(position).getDesc());
        String who = mSearchLists.get(position).getWho();
        ((ResultViewHolder)holder).author_text.setText((who!=null) ? who :"佚名");
        ((ResultViewHolder)holder).data_text.setText(mSearchLists.get(position).getPublishedAt().substring(0,10));
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
        return mSearchLists.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title_text)
        TextView title_text;
        @BindView(R.id.author_text)
        TextView author_text;
        @BindView(R.id.data_text)
        TextView data_text;

        public ResultViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

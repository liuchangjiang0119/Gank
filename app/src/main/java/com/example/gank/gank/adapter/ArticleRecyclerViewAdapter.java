package com.example.gank.gank.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gank.R;
import com.example.gank.model.bean.GankItemList;

import org.w3c.dom.ls.LSInput;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dell on 2016/9/18.
 */
public class ArticleRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private ImageView mImageView;

    private Context mContext;
    private String type;
    private String url;
    List<GankItemList> mLists = new ArrayList<GankItemList>();

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


    public ArticleRecyclerViewAdapter(Context context,String type) {
        this.type = type;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);


    }
    public void setItemList(List<GankItemList> list){

        this.mLists = list;

        this.notifyDataSetChanged();

    }

    public void setPicUrl(String url){
        this.url = url;

        this.notifyDataSetChanged();
    }

    /*
    * 用枚举列出RecyclerView的两种item

    * */
    public enum ITEM_TYPE{
        ITEM_TYPE_IMAGE,
        ITEM_TYPE_ARTICLE;


}
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal()){
            return new ImageViewHolder(mInflater.inflate(R.layout.item_image,parent,false));
        }else {
            return new ArticleViewHolder(mInflater.inflate(R.layout.item_article,parent,false));
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageViewHolder){
            mImageView = ((ImageViewHolder)holder).mImageView;
            /*
            * Glide进行图片访问
            * */
            Glide.with(mContext).load(url).asBitmap()
                    .error(R.mipmap.image)
                    .into(mImageView);


        }else if (holder instanceof ArticleViewHolder){

            ((ArticleViewHolder)holder).title_text.setText(mLists.get(position).getDesc());
            String who = mLists.get(position).getWho();
            if (who==null){
                who = "佚名";
            }
            ((ArticleViewHolder)holder).author_text.setText(who);
            String date = mLists.get(position).getPublishedAt();

            ((ArticleViewHolder)holder).data_text.setText(date.substring(5,10));
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


    }

    @Override
    public int getItemViewType(int position) {
        if (position ==0){
            return ITEM_TYPE.ITEM_TYPE_IMAGE.ordinal();
        }else {
            return ITEM_TYPE.ITEM_TYPE_ARTICLE.ordinal();
        }
    }

    @Override
    public int getItemCount() {

        return mLists.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.girl_image)
        ImageView mImageView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.title_text)
        TextView title_text;
        @BindView(R.id.author_text)
        TextView author_text;
        @BindView(R.id.data_text)
        TextView data_text;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}

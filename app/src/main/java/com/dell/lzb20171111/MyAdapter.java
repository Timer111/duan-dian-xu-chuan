package com.dell.lzb20171111;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dell.lzb20171111.bean.MyBean;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2017/11/11.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>  {

    private List<MyBean.DataBean> list = new ArrayList<>();
    private Context context;

    public MyAdapter(List<MyBean.DataBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public  void addData(List<MyBean.DataBean> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
        holder.tv.setText(list.get(position).getTitle());
        holder.time.setText(list.get(position).getOtime());
        Uri uri = Uri.parse(list.get(position).getImg());
        holder.sim.setImageURI(uri);
        holder.itemView.setTag(position);

        if(onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.OnItemClick(holder.itemView,position);
                }
            });
        }
        if(onItemLongClickListener != null){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemLongClickListener.onItemLongClickListener(holder.itemView,position);
                    return true;
                }
            });
        }
    }



    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final SimpleDraweeView sim;
        private final TextView tv;
        private final TextView time;

        public ViewHolder(View itemView) {
            super(itemView);

            sim = itemView.findViewById(R.id.item_sim);
            tv = itemView.findViewById(R.id.item_tv);
            time = itemView.findViewById(R.id.item_time);
        }
    }
    //设置点击事件
    OnRecyclerViewItemClickListener onItemClickListener;
    OnRecyclerViewItemLongClickListener onItemLongClickListener;
    public interface OnRecyclerViewItemClickListener{
        void OnItemClick(View view,int position);
    }

    public interface OnRecyclerViewItemLongClickListener{
        void onItemLongClickListener(View view,int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }


}

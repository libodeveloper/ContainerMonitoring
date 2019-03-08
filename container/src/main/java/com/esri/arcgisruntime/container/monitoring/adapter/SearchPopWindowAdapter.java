package com.esri.arcgisruntime.container.monitoring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;

import java.util.List;

/**
 * Created by 李波 on 2019/3/8.
 * 编号搜索
 */
public class SearchPopWindowAdapter extends RecyclerView.Adapter<SearchPopWindowAdapter.ViewHolder> {

    private List<String> dataLists;
    private Context context;
    private SearchPopWindowAdapter.OnItemClickListener clickListener;
    public void setDataLists(List<String> dataLists) {
        this.dataLists = dataLists;
    }

    public SearchPopWindowAdapter(Context context, List<String> datas) {
        this.context = context;
        dataLists = datas;

    }

    @Override
    public SearchPopWindowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_popwindow,parent,false);
        SearchPopWindowAdapter.ViewHolder viewHolder = new SearchPopWindowAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tvSearchNumber.setText(dataLists.get(position));

        holder.ivdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataLists.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(v,position);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return dataLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvSearchNumber;
        private ImageView ivdelete;
        private View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvSearchNumber = itemView.findViewById(R.id.tvSearchNumber);
            ivdelete = itemView.findViewById(R.id.ivDelete);

        }
    }


    public void setOnItemClickListener(SearchPopWindowAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }
}

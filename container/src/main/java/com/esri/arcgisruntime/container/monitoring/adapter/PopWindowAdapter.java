package com.esri.arcgisruntime.container.monitoring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.esri.arcgisruntime.container.monitoring.R;

import java.util.List;

public class PopWindowAdapter extends RecyclerView.Adapter<PopWindowAdapter.ViewHolder> {

    private List<String> dataLists;
    private Context context;
    private PopWindowAdapter.OnItemClickListener clickListener;
    public void setDataLists(List<String> dataLists) {
        this.dataLists = dataLists;
    }

    public PopWindowAdapter(Context context, List<String> datas) {
        this.context = context;
        dataLists = datas;

    }


    @Override
    public PopWindowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_popwindow,parent,false);
        PopWindowAdapter.ViewHolder viewHolder = new PopWindowAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {


        holder.tvNumberType.setText(dataLists.get(position));

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
        private TextView tvNumberType;
        private View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvNumberType = itemView.findViewById(R.id.tvNumberType);

        }
    }


    public void setOnItemClickListener(PopWindowAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }
}

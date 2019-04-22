package com.esri.arcgisruntime.container.monitoring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.bean.NumberCache;
import com.esri.arcgisruntime.container.monitoring.bean.SearchNumberBean;
import com.esri.arcgisruntime.container.monitoring.global.Constants;
import com.esri.arcgisruntime.container.monitoring.utils.ACache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 李波 on 2019/3/8.
 * 编号搜索
 */
public class SearchPopWindowAdapter extends RecyclerView.Adapter<SearchPopWindowAdapter.ViewHolder> {

    List<SearchNumberBean.RowsBean> data;
//    private List<String> dataLists = new ArrayList<>();
    private Context context;
    private SearchPopWindowAdapter.OnItemClickListener clickListener;
    int type;
    boolean isShowDel = true;

    public void setDataLists(List<SearchNumberBean.RowsBean> data,int type) {
        this.data = data;
        this.type = type;
        notifyDataSetChanged();

    }

    public void setIshowDel(boolean isShowDel){
        this.isShowDel = isShowDel;
    }

    public SearchPopWindowAdapter(Context context, List<SearchNumberBean.RowsBean> data,int type) {
        this.context = context;
        this.data = data;
        this.type = type;
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

        if (isShowDel){
            holder.ivdelete.setVisibility(View.VISIBLE);
        } else{
            holder.ivdelete.setVisibility(View.GONE);
        }

        if (type ==0)
            holder.tvSearchNumber.setText(data.get(position).getContainer_code());
        else if (type ==1 )
            holder.tvSearchNumber.setText(data.get(position).getLock_code());

        holder.ivdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);

                NumberCache numberCache = (NumberCache) ACache.get(context).getAsObject(Constants.KEY_ACACHE_NUMBERCACHE);
                if (type ==0)
                    numberCache.setContainerRows(data);
                else if (type ==1)
                    numberCache.setLockRows(data);
                ACache.get(context).put(Constants.KEY_ACACHE_NUMBERCACHE,numberCache);

                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onItemClick(v,position,data.get(position));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
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
        public void onItemClick(View itemView, int pos,SearchNumberBean.RowsBean rowsBean);
    }
}

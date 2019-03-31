package com.esri.arcgisruntime.container.monitoring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.bean.BillQueryBean;
import com.esri.arcgisruntime.container.monitoring.bean.BusinessQueryBean;

import java.util.List;

/**
 * Created by 李波 on 2019/3/7.
 * 业务查询结果adapter
 */
public class BusinessQueryResultAdapter extends RecyclerView.Adapter<BusinessQueryResultAdapter.ViewHolder> {

    private List<BusinessQueryBean> dataLists;
    private Context context;
    private BusinessQueryResultAdapter.OnItemClickListener clickListener;
    public void setDataLists(List<BusinessQueryBean> dataLists) {
        this.dataLists = dataLists;
    }

    public BusinessQueryResultAdapter(Context context, List<BusinessQueryBean> datas) {
        this.context = context;
        dataLists = datas;

    }

    public void setData(List<BusinessQueryBean> dataLists){
        this.dataLists =dataLists;
        notifyDataSetChanged();
    }


    @Override
    public BusinessQueryResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_business_query_result,parent,false);
        BusinessQueryResultAdapter.ViewHolder viewHolder = new BusinessQueryResultAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        BusinessQueryBean businessQueryBean = dataLists.get(position);

        holder.tvSeniority.setText(businessQueryBean.getSeniority()+"");
        holder.tvLockNumber.setText(businessQueryBean.getLocknumber());
        holder.tvSite.setText(businessQueryBean.getSite());
        holder.tvTimes.setText(businessQueryBean.getTimes());

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

        private TextView tvSeniority;
        private TextView tvLockNumber;
        private TextView tvSite;
        private TextView tvTimes;

        private View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvSeniority = itemView.findViewById(R.id.tvSeniority);
            tvLockNumber = itemView.findViewById(R.id.tvLockNumber);
            tvSite = itemView.findViewById(R.id.tvSite);
            tvTimes = itemView.findViewById(R.id.tvTimes);

        }
    }


    public void setOnItemClickListener(BusinessQueryResultAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }
}

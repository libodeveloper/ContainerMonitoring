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
import com.esri.arcgisruntime.container.monitoring.bean.BusinessQueryResultBean;

import java.util.List;

/**
 * Created by 李波 on 2019/3/7.
 * 业务查询结果adapter
 */
public class BusinessQueryResultAdapter extends RecyclerView.Adapter<BusinessQueryResultAdapter.ViewHolder> {

    private List<BusinessQueryResultBean.RowsBean> dataLists;
    private Context context;
    private BusinessQueryResultAdapter.OnItemClickListener clickListener;
    private int type=1; //1-关锁使用频率查询   2-路径使用频率查询

    public BusinessQueryResultAdapter(Context context,int type ,List<BusinessQueryResultBean.RowsBean> datas) {
        this.context = context;
        dataLists = datas;
        this.type = type;
    }

    public void setData(List<BusinessQueryResultBean.RowsBean> dataLists){
        this.dataLists =dataLists;
        notifyDataSetChanged();
    }


    @Override
    public BusinessQueryResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        if (type == 1 )
            view = LayoutInflater.from(context).inflate(R.layout.item_business_query_result,parent,false);
        else if (type ==2)
            view = LayoutInflater.from(context).inflate(R.layout.item_business_query_route_result,parent,false);

        BusinessQueryResultAdapter.ViewHolder viewHolder = new BusinessQueryResultAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        BusinessQueryResultBean.RowsBean businessQueryBean = dataLists.get(position);

        holder.tvSeniority.setText(businessQueryBean.getSeniority()+"");
        holder.tvTimes.setText(businessQueryBean.getNum());

        if (type == 1){
            holder.tvSite.setText(businessQueryBean.getDestinationName());
            holder.tvLockNumber.setText(businessQueryBean.getLock_code());
        }else if (type ==2){
            holder.tvRouteCode.setText(businessQueryBean.getRoute_code());
            holder.tvStartSite.setText(businessQueryBean.getLaunchName());
            holder.tvEndSite.setText(businessQueryBean.getDestName());
        }

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
        private TextView tvRouteCode;
        private TextView tvStartSite;
        private TextView tvEndSite;

        private View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvSeniority = itemView.findViewById(R.id.tvSeniority);
            tvTimes = itemView.findViewById(R.id.tvTimes);
            if (type==1){
                tvLockNumber = itemView.findViewById(R.id.tvLockNumber);
                tvSite = itemView.findViewById(R.id.tvSite);
            }else if (type ==2){
                tvRouteCode = itemView.findViewById(R.id.tvRouteCode);
                tvStartSite = itemView.findViewById(R.id.tvStartSite);
                tvEndSite = itemView.findViewById(R.id.tvEndSite);
            }

        }
    }


    public void setOnItemClickListener(BusinessQueryResultAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }
}

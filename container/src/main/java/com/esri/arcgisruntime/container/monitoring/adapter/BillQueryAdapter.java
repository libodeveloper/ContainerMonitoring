package com.esri.arcgisruntime.container.monitoring.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esri.arcgisruntime.container.monitoring.R;
import com.esri.arcgisruntime.container.monitoring.bean.BillQueryBean;

import java.util.List;

/**
 * Created by 李波 on 2019/3/7.
 * 单据查询结果adapter
 */
public class BillQueryAdapter extends RecyclerView.Adapter<BillQueryAdapter.ViewHolder> {

    private List<BillQueryBean> dataLists;
    private Context context;
    private BillQueryAdapter.OnItemClickListener clickListener;
    public void setDataLists(List<BillQueryBean> dataLists) {
        this.dataLists = dataLists;
    }

    public BillQueryAdapter(Context context, List<BillQueryBean> datas) {
        this.context = context;
        dataLists = datas;

    }

    public void setData(List<BillQueryBean> data){
        this.dataLists = data;
        notifyDataSetChanged();
    }

    @Override
    public BillQueryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bill_query_result,parent,false);
        BillQueryAdapter.ViewHolder viewHolder = new BillQueryAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.tvSequenceNumbe.setText(dataLists.get(position).getSequenceNumbe());
        holder.tvContainerNumber.setText(dataLists.get(position).getContainerNumber());
        holder.tvOrderNumber.setText(dataLists.get(position).getOrderNumber());

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
        private TextView tvSequenceNumbe;   //序号
        private TextView tvContainerNumber; //集装箱编号
        private TextView tvOrderNumber;     //单据编号
        private View itemView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvSequenceNumbe = itemView.findViewById(R.id.tvSequenceNumbe);
            tvContainerNumber = itemView.findViewById(R.id.tvContainerNumber);
            tvOrderNumber = itemView.findViewById(R.id.tvOrderNumber);

        }
    }


    public void setOnItemClickListener(BillQueryAdapter.OnItemClickListener listener) {
        clickListener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View itemView, int pos);
    }
}

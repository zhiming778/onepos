package com.example.onepos.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onepos.R;
import com.example.onepos.model.CustomerOrder;

import java.util.List;

public class ModifyOrderAdapter extends RecyclerView.Adapter<ModifyOrderAdapter.ModifyViewHolder> {


    private List<CustomerOrder> list;
    private OnModifyOrderListener listener;
    private final String[] ORDER_TYPES, PAYMENT_TYPES;
    public interface OnModifyOrderListener {
        void onModifyOrder(long id);
    }

    public ModifyOrderAdapter(OnModifyOrderListener listener, final String[] ORDER_TYPES, final String[] PAYMENT_TYPES) {
        this.listener = listener;
        this.ORDER_TYPES = ORDER_TYPES;
        this.PAYMENT_TYPES = PAYMENT_TYPES;
    }

    @NonNull
    @Override
    public ModifyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_modify_order, null);
        return new ModifyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModifyViewHolder holder, int pos) {
        holder.tvOrderNum.setText(String.format("# %d", list.get(pos).getId()));
        holder.tvOrderType.setText(ORDER_TYPES[list.get(pos).getOrderType()]);
        holder.tvTotal.setText(String.format("$ %.2f", list.get(pos).getTotal()));
        holder.tvPaymentType.setText(PAYMENT_TYPES[list.get(pos).getPaymentType()]);
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        else
            return list.size();
    }

    public void setList(List<CustomerOrder> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    class ModifyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvOrderNum, tvOrderType, tvTotal, tvPaymentType;
        ModifyViewHolder(@NonNull View view) {
            super(view);
            tvOrderNum = view.findViewById(R.id.tv_order_number);
            tvOrderType = view.findViewById(R.id.tv_order_type);
            tvTotal = view.findViewById(R.id.tv_total);
            tvPaymentType = view.findViewById(R.id.tv_payment_type);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onModifyOrder(list.get(getAdapterPosition()).getId());
        }
    }

}

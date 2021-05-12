package com.example.onepos.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onepos.R;
import com.example.onepos.model.CustomerOrder;
import com.example.onepos.util.DateUtil;
import com.example.onepos.util.MLog;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    static String[] ORDER_TYPES;
    static String FORMAT_CURRENCY;
    private List<CustomerOrder> list;
    private OrderHistoryListener listener;

    public interface OrderHistoryListener {
        void orderHistoryClick(int pos);
    }

    public OrderHistoryAdapter(String[] ORDER_TYPES, String FORMAT_CURRENCY, OrderHistoryListener listener) {
        this.ORDER_TYPES = ORDER_TYPES;
        this.FORMAT_CURRENCY = FORMAT_CURRENCY;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_order_history, null);
        return new OrderHistoryViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        final CustomerOrder customerOrder = list.get(position);
        holder.setDate(customerOrder.getDate());
        holder.setOrderType(customerOrder.getOrderType());
        holder.setTotal(customerOrder.getTotal());
    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        else
            return list.size();
    }

    static class OrderHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvDate, tvOrderType, tvTotal;
        private OrderHistoryListener listener;
        OrderHistoryViewHolder(@NonNull View itemView, OrderHistoryListener listener) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvOrderType = itemView.findViewById(R.id.tv_order_type);
            tvTotal = itemView.findViewById(R.id.tv_total);
            itemView.setOnClickListener(this);
            this.listener = listener;
        }

        void setDate(long millis) {
            tvDate.append(DateUtil.millisToDate(millis));
        }

        void setOrderType(int orderType) {
            tvOrderType.append(OrderHistoryAdapter.ORDER_TYPES[orderType]);
        }

        void setTotal(double total) {
            tvTotal.append(String.format(FORMAT_CURRENCY, total));
        }

        @Override
        public void onClick(View v) {
            listener.orderHistoryClick(getAdapterPosition());
        }
    }

    public void setList(List<CustomerOrder> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}

package com.example.onepos.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onepos.R;
import com.example.onepos.model.OrderItem;
import com.example.onepos.model.OrderMenuItem;
import com.example.onepos.model.OrderModifierItem;
import com.example.onepos.model.Receipt;
import com.example.onepos.util.MLog;
import com.example.onepos.util.OrderListener;
import com.example.onepos.view.fragment.OrderFragment;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ItemViewHolder> {

    private final int VIEW_TYPE_MENUITEM = 0;
    private final int View_TYPE_MODIFIERITEM = 1;
    private Receipt receipt;
    private int indexSelected, colorSelected, colorUnselected, colorModeDeleted, colorModeOld, colorModeAdded;
    private OrderListener listener;
    private final String PRICE_FORMAT;
    private final String DISCOUNT_FORMAT;
    private final int marginStart;


    public ReceiptAdapter(Context context, Receipt receipt, OrderListener listener) {
        indexSelected = -1;
        this.receipt = receipt;
        colorSelected = context.getColor(R.color.colorSelected);
        colorUnselected = context.getColor(R.color.colorWidgetBackground);
        colorModeDeleted = context.getColor(R.color.colorModeDeleted);
        colorModeOld = context.getColor(R.color.colorMain);
        colorModeAdded = context.getColor(R.color.colorText);
        PRICE_FORMAT = context.getString(R.string.format_currency);
        DISCOUNT_FORMAT = context.getString(R.string.format_discount);
        setHasStableIds(false);
        this.listener = listener;
        marginStart = (int)context.getResources().getDimension(R.dimen.distance_quantity);
    }

    @Override
    public int getItemViewType(int pos) {
        if (receipt == null || receipt.getNumOfItems()==0) {
            return super.getItemViewType(pos);
        }
        else
        {
            OrderItem item = receipt.get(pos);
            if (item instanceof OrderMenuItem) {
                return VIEW_TYPE_MENUITEM;
            }
            else{
                if (item instanceof OrderModifierItem) {
                    return View_TYPE_MODIFIERITEM;
                }
                else
                    throw new IllegalArgumentException("It's neither an instance of OrderMenuItem nor OrderModifierItem.");
            }
        }
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View view;
        view = li.inflate(R.layout.receipt_orderitem, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int pos) {
        OrderItem orderItem = receipt.get(pos);
        MLog.d("mode = "+orderItem.getMode());
        holder.tvPrice.setText(String.format(PRICE_FORMAT, orderItem.getPrice()));
        holder.tvName.setText(orderItem.getTitle(DISCOUNT_FORMAT));
        if (getItemViewType(pos)==View_TYPE_MODIFIERITEM) {
            if (orderItem.getQuantity() > 1) {
                holder.tvQuantity.setVisibility(View.VISIBLE);
                holder.tvQuantity.setText(String.valueOf(receipt.get(pos).getQuantity()));
            }
            else
                holder.tvQuantity.setVisibility(View.GONE);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setMarginStart(marginStart);
        }
        else{
            holder.tvQuantity.setText(String.valueOf(receipt.get(pos).getQuantity()));
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setMarginStart(0);
        }
        if (pos == indexSelected)
            holder.itemView.setBackgroundColor(colorSelected);
        else
            holder.itemView.setBackgroundColor(colorUnselected);
        if (orderItem.getMode()==OrderItem.MODE_OLD){
            holder.tvQuantity.setTextColor(colorModeOld);
            holder.tvPrice.setTextColor(colorModeOld);
            holder.tvName.setTextColor(colorModeOld);
        }
        if (orderItem.getMode()==OrderItem.MODE_DELETED){
            holder.tvQuantity.setTextColor(colorModeDeleted);
            holder.tvPrice.setTextColor(colorModeDeleted);
            holder.tvName.setTextColor(colorModeDeleted);
        }
        if (orderItem.getMode() == OrderItem.MODE_ADDED) {
            holder.tvPrice.setTextColor(colorModeAdded);
            holder.tvName.setTextColor(colorModeAdded);
        }
    }

    @Override
    public int getItemCount() {
        if (receipt == null) {
            return 0;
        }
        else
            return receipt.getNumOfItems();
    }
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvQuantity, tvName, tvPrice;
        ItemViewHolder(@NonNull View view) {
            super(view);
            tvQuantity = view.findViewById(R.id.tv_quantity);
            tvName = view.findViewById(R.id.tv_name);
            tvPrice = view.findViewById(R.id.tv_price);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            clickEvent(this);
        }
    }
    private void clickEvent(RecyclerView.ViewHolder viewHolder) {
        int pos = viewHolder.getAdapterPosition();
        if (pos != indexSelected) {
            int temp = indexSelected;
            indexSelected = pos;
            listener.onOrder(indexSelected, OrderFragment.REQUEST_RECEIPT);
            notifyItemChanged(indexSelected);
            notifyItemChanged(temp);
        }
    }
    public void resetIndexSelected() {
        int temp = indexSelected;
        indexSelected = -1;
        listener.onOrder(indexSelected, OrderFragment.REQUEST_RECEIPT);
        notifyItemChanged(temp);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        receipt = null;
        listener = null;
    }
}

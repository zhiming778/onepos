package com.example.onepos.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onepos.R;
import com.example.onepos.model.ModifierItem;
import com.example.onepos.util.OrderListener;
import com.example.onepos.view.fragment.OrderFragment;

import java.util.List;

public class ModifierAdapter extends RecyclerView.Adapter<ModifierAdapter.ModifierViewHolder> {

    private List<ModifierItem> listModifier;
    private int currentModifierType;
    private OrderListener listener;


    public ModifierAdapter(Fragment fragment)
    {
        this.listener = (OrderListener)fragment;
        currentModifierType = 0;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public int getItemCount() {
        if (listModifier == null)
            return 0;
        else
            return listModifier.size();
    }

    @NonNull
    @Override
    public ModifierViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View view = li.inflate(R.layout.cell_menuitem, parent, false);
        return new ModifierViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModifierViewHolder holder, int pos) {
        holder.setModifierItem(listModifier.get(pos).getName());
    }


    class ModifierViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvModifierItem;
        public ModifierViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModifierItem = itemView.findViewById(R.id.tv_menuitem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onOrder(getAdapterPosition(), OrderFragment.REQUEST_MODIFIER);
        }

        public void setModifierItem(String name) {
            tvModifierItem.setText(name);
        }
    }

    public int getCurrentModifierType() {
        return currentModifierType;
    }

    public void setCurrentModifierType(int currentModifierType) {
        this.currentModifierType = currentModifierType;
    }

    public void setList(List<ModifierItem> list) {
        if (listModifier == null) {
            listModifier = list;
        } else {
            listModifier.clear();
            listModifier.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        listModifier = null;
        listener = null;
    }
}

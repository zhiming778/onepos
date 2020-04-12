package com.example.onepos.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onepos.R;
import com.example.onepos.model.MenuItem;
import com.example.onepos.util.OrderListener;
import com.example.onepos.view.fragment.OrderFragment;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private List<MenuItem> listCategory;
    private int indexSelected;
    private OrderListener listener;
    private int colorSelected;
    private int colorUnselected;

    public CategoryAdapter(final int colorSelected, final int colorUnselected, int indexSelected, OrderListener listener) {
        this.indexSelected = indexSelected;
        this.listener = listener;
        this.colorSelected = colorSelected;
        this.colorUnselected = colorUnselected;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        return new CategoryViewHolder(li.inflate(R.layout.cell_category, parent, false));
    }

    @Override
    public int getItemCount() {
        if (listCategory == null)
            return 0;
        else
            return listCategory.size();
    }

    @Override
    public int getItemViewType(int pos) {
        return super.getItemViewType(pos);
    }

    @Override
    public long getItemId(int pos) {
        if (listCategory == null)
            return 0;
        else
            return listCategory.get(pos).getId();
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int pos) {
        TextView tvCategory = holder.tvCategory;
        tvCategory.setText(listCategory.get(pos).getName().toUpperCase());
        if (pos==indexSelected){ //TODO do further research about reused viewholders.
            holder.tvCategory.setBackgroundColor(colorSelected);
        }
        else
            holder.tvCategory.setBackgroundColor(colorUnselected);
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvCategory;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvCategory = (TextView) itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if (pos!=indexSelected) {
                listener.onOrder(pos, OrderFragment.REQUEST_CATEGORY); //TODO it could be optimized ?
                int temp = indexSelected;
                indexSelected = pos;
                notifyItemChanged(indexSelected);
                notifyItemChanged(temp);
            }
        }
    }
    public void setListCategory(List<MenuItem> listCategory) {
        if (this.listCategory == null) {
            this.listCategory = listCategory;
        } else {
            this.listCategory.clear();
            this.listCategory.addAll(listCategory);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.listCategory.clear();
        listener = null;
    }
}

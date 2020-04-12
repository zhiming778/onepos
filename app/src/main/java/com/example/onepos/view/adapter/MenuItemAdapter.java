package com.example.onepos.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onepos.R;
import com.example.onepos.model.MenuItem;
import com.example.onepos.util.OrderListener;
import com.example.onepos.view.fragment.OrderFragment;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.MenuItemViewHolder> {

    private boolean isMainLevel;
    private List<MenuItem> listMenuItem;
    private OrderListener listener;
    private final String TXT_RETURN;

    public MenuItemAdapter(Fragment fragment) {
        this.listener = (OrderListener)fragment;
        isMainLevel = true;
        TXT_RETURN = fragment.getString(R.string.btn_return);
    }

    @Override
    public int getItemViewType(int pos) {
        return super.getItemViewType(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @NonNull
    @Override
    public MenuItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View view = li.inflate(R.layout.cell_menuitem, parent, false);
        return new MenuItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemViewHolder holder, int pos) {
        if ((!isMainLevel) && pos == listMenuItem.size()) {
            ((TextView)(holder.itemView)).setText(TXT_RETURN);
        }
        else{
            ((TextView)(holder.itemView)).setText(listMenuItem.get(pos).getName());
        }
    }

    @Override
    public int getItemCount() {
        if (listMenuItem==null)
            return 0;
        else
        {
            if (isMainLevel) {
                return listMenuItem.size();
            } else {
                return listMenuItem.size() + 1;
            }
        }
    }

    class MenuItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public MenuItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);        //TODO set recyclerview unclickable while loading new items
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            if (!isMainLevel && pos == listMenuItem.size()) {
                listener.onOrder(pos, OrderFragment.REQUEST_TYPE_RETURN);
            }
            else{
                MenuItem menuItem = listMenuItem.get(pos);
                if (menuItem.hasDescendant()){
                    listener.onOrder(pos, OrderFragment.REQUEST_TYPE_SUBMENUITEM);
                }
                else{
                    listener.onOrder(pos, OrderFragment.REQUEST_TYPE_ADDTOORDER);
                }
            }
        }
    }

    public void setListAndLevel(List<MenuItem> listMenuItem, boolean isMainLevel) {
        this.isMainLevel = isMainLevel;
        if (this.listMenuItem == null) {
            this.listMenuItem = listMenuItem;
        } else {
            this.listMenuItem.clear();
            this.listMenuItem.addAll(listMenuItem);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        listMenuItem = null;
        listener = null;
    }
}

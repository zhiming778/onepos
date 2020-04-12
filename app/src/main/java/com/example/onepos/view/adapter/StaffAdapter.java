package com.example.onepos.view.adapter;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onepos.R;
import com.example.onepos.model.Staff;
import com.example.onepos.util.MLog;

public class StaffAdapter extends RecyclerView.Adapter<StaffAdapter.StaffViewHolder> {

    private Cursor cursor;
    private final int COLUMN_ID = 0, COLUMN_TITLE = 1, COLUMN_NAME = 2;
    private final String[] titles;

    public interface OnItemClickListener{
        void onItemClick(long id);
    }

    private OnItemClickListener onItemClickListener;

    public StaffAdapter(Fragment fragment) {
        this.onItemClickListener = (OnItemClickListener)fragment;
        titles = fragment.getContext().getResources().getStringArray(R.array.staff_titles);
    }

    @Override
    public long getItemId(int pos) {
        return super.getItemId(pos);
    }


    @NonNull
    @Override
    public StaffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_staff, parent, false);
        return new StaffViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffViewHolder holder, int pos) {
        cursor.moveToPosition(pos);
        holder.tvTitle.setText(titles[cursor.getInt(COLUMN_TITLE)]);
        holder.tvName.setText(cursor.getString(COLUMN_NAME));
        holder.id = cursor.getLong(COLUMN_ID);
    }
    static class StaffViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName, tvTitle;
        long id;
        OnItemClickListener onItemClickListener;
        StaffViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTitle = itemView.findViewById(R.id.tv_title);
            itemView.setOnClickListener(this);
            this.onItemClickListener = onItemClickListener;
        }
        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(id);
        }
    }
    @Override
    public int getItemCount() {
        if (cursor!=null)
            return cursor.getCount();
        else
            return 0;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (cursor!=null) {
            cursor.close();
            cursor = null;
        }
        onItemClickListener = null;
    }

    public void setCursor(Cursor cursor) {
        if (this.cursor != null && (!this.cursor.isClosed())) {
            this.cursor.close();
        }
        this.cursor = cursor;
        notifyDataSetChanged();
    }
}

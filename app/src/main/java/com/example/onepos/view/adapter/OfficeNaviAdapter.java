package com.example.onepos.view.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.onepos.R;
import com.example.onepos.util.MLog;

public class OfficeNaviAdapter extends BaseAdapter implements View.OnClickListener {

    private final String[] list;
    private int indexSelected;
    private OnItemClickListener onItemClickListener;
    private int colorSelected;
    private int colorUnselected;

    private int count = 0;

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }

    public OfficeNaviAdapter(final String[] list, Activity activity) {
        this.list = list;
        onItemClickListener = (OnItemClickListener) activity;
        colorSelected = activity.getColor(R.color.colorSelected);
        colorUnselected = activity.getColor(R.color.colorWidgetBackground);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public String getItem(int pos) {
        return list[pos];
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup viewGroup) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_office_navi, viewGroup, false);
            view.setOnClickListener(this);
            ++count;
        }
        else
            view = convertView;
        view.setTag(pos);
        ((TextView) view).setText(list[pos]);
        if (pos==indexSelected)
            view.setBackgroundColor(colorSelected);
        else
            view.setBackgroundColor(colorUnselected);
        return view;
    }
    @Override
    public void onClick(View view) {
        int pos = (int) view.getTag();
        if (pos==indexSelected)
            return;
        indexSelected = pos;
        onItemClickListener.onItemClick(pos);
        notifyDataSetChanged();
    }

}

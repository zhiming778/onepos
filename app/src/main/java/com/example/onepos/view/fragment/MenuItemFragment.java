package com.example.onepos.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onepos.R;
import com.example.onepos.view.adapter.MenuItemAdapter;
import com.example.onepos.viewmodel.OrderViewModel;

public class MenuItemFragment extends Fragment{

    public static final String TAG = "menu_item_fragment";
    private MenuItemAdapter menuItemAdapter;
    private final String IS_MAIN_MENU = "is_main_menu";
    private OrderViewModel orderViewModel;
    private RecyclerView rvMenuItem;
    private View rootView;
    private GridLayoutManager gridLayoutManager;

    public static MenuItemFragment newInstance() {
        MenuItemFragment menuItemFragment = new MenuItemFragment();
        //Bundle bundle = new Bundle();
        //menuItemFragment.setArguments(bundle);
        return menuItemFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView =  inflater.inflate(R.layout.fragment_menuitem_list, container, false);
        rvMenuItem = rootView.findViewById(R.id.rv_menuitem);
        menuItemAdapter = new MenuItemAdapter(getParentFragment());
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvMenuItem.setLayoutManager(gridLayoutManager);
        rvMenuItem.setAdapter(menuItemAdapter);
        orderViewModel = ViewModelProviders.of(getActivity()).get(OrderViewModel.class);
        orderViewModel.getLiveSublistMenuItem().observe(this, list->{
            menuItemAdapter.setListAndLevel(list, orderViewModel.isMainLevel());
        });
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        orderViewModel.getLiveSublistMenuItem().removeObservers(this);
    }
}
